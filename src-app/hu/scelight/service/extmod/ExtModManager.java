/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.service.extmod;

import hu.scelight.service.env.Env;
import hu.scelightapi.IExternalModule;
import hu.sllauncher.ScelightLauncher;
import hu.sllauncher.bean.module.ExtModManifestBean;
import hu.sllauncher.bean.module.FileBean;
import hu.sllauncher.bean.module.ModulesBean;
import hu.sllauncher.bean.module.ModulesBeanOrigin;
import hu.sllauncher.gui.page.extmods.OffExtModConfBean;
import hu.sllauncher.gui.page.extmods.OffExtModConfsBean;
import hu.sllauncher.gui.page.extmods.installed.InstExtModConfBean;
import hu.sllauncher.gui.page.extmods.installed.InstExtModConfsBean;
import hu.sllauncher.gui.page.extmods.installed.InstExtModsUtil;
import hu.sllauncher.service.env.LEnv;
import hu.sllauncher.service.settings.LSettings;
import hu.sllauncher.util.NormalThread;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * External module manager.
 * 
 * @author Andras Belicza
 */
public class ExtModManager {
	
	/** External mods cache path. */
	private static final Path                  PATH_EXT_MODS_CACHE    = Env.PATH_WORKSPACE.resolve( "mod-x-cache" );
	
	
	/** List of installed external module manifests. */
	private final List< ExtModManifestBean >   extModManifestList     = InstExtModsUtil.detectInstalledExtMods();
	
	/** List of loaded / started external module handlers. */
	private final List< ExtModHandler >        extModHandlerList      = new ArrayList<>();
	
	/** A map of loaded / started external modules, mapped from their folder. */
	private final Map< String, ExtModHandler > folderExtModHandlerMap = new HashMap<>();
	
	
	/**
	 * Creates a new {@link ExtModManager}.
	 */
	public ExtModManager() {
		purgeMissingOffExtModConfigs();
		purgeMissingInstExtModConfigs();
		
		// Start modules from a new thread with normal priority so if the modules start new threads,
		// they will inherit the proper normal priority by default.
		final NormalThread starterThread = new NormalThread( "External Module Starter" ) {
			@Override
			public void run() {
				startEnabledExtMods();
			}
		};
		starterThread.start();
		
		// Cannot wait here for ext modules to be started:
		// We're in the EDT, and if modules want to use the EDT (e.g. to build their GUI) => DEADLOCK!
		// starterThread.waitToFinish();
	}
	
	/**
	 * Removes configs of official external modules from the settings which are no longer available.
	 */
	private static void purgeMissingOffExtModConfigs() {
		// Only do this if modules bean is valid (not fake) (else we don't have info about official external modules).
		final ModulesBean modules = ScelightLauncher.INSTANCE().getModules();
		
		if ( modules.getOrigin() == ModulesBeanOrigin.UPDATER_FAKE )
			return;
		
		final OffExtModConfsBean offExtModConfsBean = Env.LAUNCHER_SETTINGS.get( LSettings.OFF_EXT_MOD_CONFS );
		
		OffExtModConfsBean offExtModConfsBeanClone = null;
		
		for ( final OffExtModConfBean conf : offExtModConfsBean.getOffExtModConfBeanList() )
			if ( modules.getExtModRefForFolder( conf.getFolder() ) == null ) {
				// No official external module found for the module specified by conf, remove it from the settings
				if ( offExtModConfsBeanClone == null )
					offExtModConfsBeanClone = offExtModConfsBean.cloneBean();
				offExtModConfsBeanClone.getOffExtModConfBeanList().remove( offExtModConfsBeanClone.getModuleConfForFolder( conf.getFolder() ) );
			}
		
		if ( offExtModConfsBeanClone != null )
			Env.LAUNCHER_SETTINGS.set( LSettings.OFF_EXT_MOD_CONFS, offExtModConfsBeanClone );
	}
	
	/**
	 * Removes configs of installed external modules from the settings which are no longer available.
	 */
	private void purgeMissingInstExtModConfigs() {
		final InstExtModConfsBean instExtModConfsBean = Env.LAUNCHER_SETTINGS.get( LSettings.INST_EXT_MOD_CONFS );
		
		InstExtModConfsBean instExtModConfsBeanClone = null;
		
		for ( final InstExtModConfBean conf : instExtModConfsBean.getInstExtModConfBeanList() ) {
			boolean found = false;
			for ( final ExtModManifestBean mf : extModManifestList )
				if ( mf.getFolder().equals( conf.getFolder() ) ) {
					found = true;
					break;
				}
			
			if ( !found ) {
				// No installed external module found for the module specified by conf, remove it from the settings
				if ( instExtModConfsBeanClone == null )
					instExtModConfsBeanClone = instExtModConfsBean.cloneBean();
				instExtModConfsBeanClone.getInstExtModConfBeanList().remove( instExtModConfsBeanClone.getModuleConfForFolder( conf.getFolder() ) );
			}
		}
		
		if ( instExtModConfsBeanClone != null )
			Env.LAUNCHER_SETTINGS.set( LSettings.INST_EXT_MOD_CONFS, instExtModConfsBeanClone );
	}
	
	/**
	 * Loads and starts enabled external modules.
	 */
	private void startEnabledExtMods() {
		final InstExtModConfsBean instExtModConfsBean = Env.LAUNCHER_SETTINGS.get( LSettings.INST_EXT_MOD_CONFS );
		
		for ( final ExtModManifestBean mf : extModManifestList ) {
			final InstExtModConfBean conf = instExtModConfsBean.getModuleConfForFolder( mf.getFolder() );
			if ( conf == null || !Boolean.TRUE.equals( conf.getEnabled() ) )
				continue; // Module not enabled
				
			Env.LOGGER.debug( "Starting external module: " + mf.getName() );
			
			URLClassLoader ucl = null;
			IExternalModule extModInstance = null;
			try {
				// Determine module class path
				final List< URL > cpUrlList = new ArrayList<>();
				for ( final FileBean file : mf.getModuleBean().getFileList() ) {
					if ( !ScelightLauncher.INSTANCE().isClassPathEntry( file ) )
						continue;
					
					Path filePath = Paths.get( file.getPath() );
					filePath = Env.PATH_APP.resolve( filePath.subpath( 1, filePath.getNameCount() ) );
					cpUrlList.add( filePath.toUri().toURL() );
				}
				
				// The next class loader is an app-lifetime class loader, no need to ever close it,
				// so it's safe to ignore potential the resource leak warning.
				ucl = new URLClassLoader( cpUrlList.toArray( new URL[ cpUrlList.size() ] ), getClass().getClassLoader() );
				
				final Class< ? extends IExternalModule > extModClass = ucl.loadClass( mf.getMainClass() ).asSubclass( IExternalModule.class );
				// So far so good, it is IExternalModule
				extModInstance = extModClass.getConstructor().newInstance();
				
				final ModEnv modEnv = new ModEnv( mf, Env.PATH_EXT_MODS.resolve( mf.getFolder() ), PATH_EXT_MODS_CACHE );
				
				extModInstance.init( mf, Services.INSTANCE, modEnv );
				
				// No Exception thrown => module started successfully.
				final ExtModHandler handler = new ExtModHandler( mf, modEnv, ucl, extModClass, extModInstance );
				extModHandlerList.add( handler );
				folderExtModHandlerMap.put( mf.getFolder(), handler );
				
			} catch ( final Throwable t ) {
				LEnv.LOGGER.error( "Failed to start external module: " + mf.getName(), t );
				
				// If ext mod instantiation succeeded, destroy it
				if ( extModInstance != null )
					try {
						extModInstance.destroy();
					} catch ( final Throwable t2 ) {
						// silently ignore
					}
				
				if ( ucl != null )
					try {
						ucl.close();
					} catch ( final IOException ie ) {
						// silently ignore
					}
				
				continue;
			}
		}
	}
	
	/**
	 * Tells if the external module specified by its folder is running.
	 * 
	 * @param folder folder of the external module to be checked
	 * @return true if the external module specified by its folder is running; false otherwise
	 */
	public boolean isModuleStarted( final String folder ) {
		return folderExtModHandlerMap.containsKey( folder );
	}
	
	/**
	 * Destroys started enabled external modules.
	 */
	public void destroyStartedExtMods() {
		for ( final ExtModHandler handler : extModHandlerList ) {
			try {
				handler.extModInstance.destroy();
			} catch ( final Throwable t ) {
				// Log t as debug, module were started successfully...
				Env.LOGGER.debug( "Failed to destroy external module: " + handler.manifest.getName(), t );
			}
			
			try {
				handler.classLoader.close();
			} catch ( final IOException ie ) {
				// silently ignore
			}
		}
		
		extModHandlerList.clear();
		folderExtModHandlerMap.clear();
	}
	
	/**
	 * Returns the list of loaded / started external module manifests.
	 * 
	 * @return the list of loaded / started external module manifests
	 */
	public List< ExtModManifestBean > getStartedExtModManifestList() {
		final List< ExtModManifestBean > manifestList = new ArrayList<>( extModHandlerList.size() );
		
		for ( final ExtModHandler handler : extModHandlerList )
			manifestList.add( handler.manifest );
		
		return manifestList;
	}
	
	/**
	 * Returns the list of loaded / started external module environments.
	 * 
	 * @return the list of loaded / started external module environments
	 */
	public List< ModEnv > getStartedExtModEnvList() {
		final List< ModEnv > modEnvList = new ArrayList<>( extModHandlerList.size() );
		
		for ( final ExtModHandler handler : extModHandlerList )
			modEnvList.add( handler.modEnv );
		
		return modEnvList;
	}
	
}
