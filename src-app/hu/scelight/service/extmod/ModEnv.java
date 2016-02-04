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
import hu.scelightapi.IModEnv;
import hu.scelightapibase.bean.settings.ISettingsBean;
import hu.scelightapibase.bean.settings.type.ISetting;
import hu.sllauncher.bean.module.ExtModManifestBean;
import hu.sllauncher.bean.settings.SettingsBean;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXB;

/**
 * External module environment implementation.
 * 
 * @author Andras Belicza
 */
public class ModEnv implements IModEnv {
	
	/** Reference to the external module manifest bean. */
	public final ExtModManifestBean           manifestBean;
	
	/** External module root folder. */
	public final Path                         rootFolder;
	
	/** External module version folder. */
	public final Path                         versionFolder;
	
	/** External module cache folder. */
	public final Path                         cacheFolder;
	
	
	/** Map of already initialized settings beans. */
	private final Map< String, SettingsBean > nameSettingsBeanMap = new HashMap<>( 6 );
	
	/** List of initialized settings beans in initialization order. */
	private final ArrayList< SettingsBean >   settingsBeanList    = new ArrayList<>( 4 );
	
	
	/**
	 * Creates a new {@link ModEnv}.
	 * 
	 * @param manifestBean reference to the external module manifest bean
	 * @param rootFolder External module root folder
	 * @param cacheParentFolder parent of external module cache folders
	 */
	public ModEnv( final ExtModManifestBean manifestBean, final Path rootFolder, final Path cacheParentFolder ) {
		this.manifestBean = manifestBean;
		this.rootFolder = rootFolder;
		versionFolder = rootFolder.resolve( manifestBean.getVersion().toString() );
		cacheFolder = cacheParentFolder.resolve( manifestBean.getFolder() );
	}
	
	@Override
	public ExtModManifestBean getManifest() {
		return manifestBean;
	}
	
	@Override
	public Path getRootFolder() {
		return rootFolder;
	}
	
	@Override
	public Path getVersionFolder() {
		return versionFolder;
	}
	
	@Override
	public Path getCacheFolder() {
		if ( !Files.exists( cacheFolder ) )
			try {
				Files.createDirectories( cacheFolder );
			} catch ( final IOException ie ) {
				Env.LOGGER.error( "Failed to create external module cache folder: " + cacheFolder, ie );
			}
		
		return cacheFolder;
	}
	
	@Override
	public synchronized SettingsBean initSettingsBean( final String name, final List< ISetting< ? > > validSettingList ) {
		if ( nameSettingsBeanMap.containsKey( name ) )
			throw new IllegalArgumentException( "A settings bean with the name has already been initialized: " + name );
		
		// A call to getCacheFolder() will also create it if it does not exist yet.
		final Path path = getCacheFolder().resolve( name + ".xml" );
		
		SettingsBean settings = null;
		try {
			settings = JAXB.unmarshal( path.toFile(), SettingsBean.class );
		} catch ( final Exception e ) {
			Env.LOGGER.warning( "Could not read " + manifestBean.getName() + " external module settings, the default settings will be used: " + path );
			if ( Files.exists( path ) ) // Only log exception if file exists but failed to read it.
				Env.LOGGER.debug( "Reason:", e );
			else
				Env.LOGGER.debug( "Reason: File does not exist: " + path );
			
			settings = new SettingsBean();
		}
		settings.configureSave( manifestBean.getName(), manifestBean.getVersion(), path );
		settings.setValidSettingList( validSettingList );
		
		if ( settings.getSaveTime() == null )
			settings.save();
		
		nameSettingsBeanMap.put( name, settings );
		settingsBeanList.add( settings );
		
		return settings;
	}
	
	@Override
	@SuppressWarnings( "unchecked" )
	public List< ISettingsBean > getSettingsBeanList() {
		// Return only a clone, because if an iterator is used on the returned list, we could not modify it (add new settings bean).
		return (List< ISettingsBean >) settingsBeanList.clone();
	}
	
	@Override
	public SettingsBean getSettingsBean( final String name ) {
		return nameSettingsBeanMap.get( name );
	}
	
}
