/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllauncher.gui.page.extmods.installed;

import hu.sllauncher.bean.VersionBean;
import hu.sllauncher.bean.module.ExtModManifestBean;
import hu.sllauncher.bean.module.FileBean;
import hu.sllauncher.bean.module.ModuleBean;
import hu.sllauncher.service.env.LEnv;
import hu.sllauncher.service.updater.Updater;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXB;

/**
 * Utility class for installed external modules.
 * 
 * @author Andras Belicza
 */
public class InstExtModsUtil {
	
	/** Path of the external module manifest file. Relative to the version folder of the module. */
	private static final Path EXT_MOD_MANIFEST_PATH = Paths.get( "Scelight-mod-x-manifest.xml" );
	
	/**
	 * Returns the list of installed external modules.
	 * 
	 * @return the list of installed external modules
	 */
	public static List< ExtModManifestBean > detectInstalledExtMods() {
		final List< ExtModManifestBean > manifestList = new ArrayList<>();
		
		final List< ModuleBean > modList = Updater.detectInstalledModules( LEnv.PATH_EXT_MODS );
		if ( modList == null )
			return manifestList;
		
		for ( final ModuleBean mb : modList ) {
			// Load ext mod manifest
			Path manifestPath = null;
			
			for ( final FileBean fb : mb.getFileList() ) {
				final Path filePath = Paths.get( fb.getPath() );
				// Full manifest path example: "Scelight/mod-x/some-mod/1.0/Scelight-mod-x-manifest.xml"
				if ( filePath.getNameCount() == 5 && filePath.getFileName().equals( EXT_MOD_MANIFEST_PATH ) ) {
					manifestPath = LEnv.PATH_APP.resolve( filePath.subpath( 1, filePath.getNameCount() ) );
					break;
				}
			}
			
			if ( manifestPath == null )
				continue; // No manifest file found
				
			final ExtModManifestBean manifest;
			try {
				manifest = JAXB.unmarshal( manifestPath.toFile(), ExtModManifestBean.class );
			} catch ( final Exception e ) {
				LEnv.LOGGER.debug( "Invalid external module manifest: " + manifestPath, e );
				continue;
			}
			
			// Verify manifest
			
			if ( !mb.getFolder().equals( manifest.getFolder() ) ) {
				LEnv.LOGGER.debug( "Invalid external module manifest, mismatching folder specified: " + manifest.getFolder() + "; was expecting: "
				        + mb.getFolder() );
				continue;
			}
			
			final VersionBean version = VersionBean.fromString( manifestPath.getName( manifestPath.getNameCount() - 2 ).toString() );
			if ( version == null || !version.equals( manifest.getVersion() ) ) {
				LEnv.LOGGER.debug( "Invalid external module manifest, mismatching version specified: " + manifest.getVersion() + "; was expecting: " + version );
				continue;
			}
			
			if ( manifest.getAuthorList() == null || manifest.getAuthorList().isEmpty() ) {
				LEnv.LOGGER.debug( "Invalid external module manifest, missing author list property!" );
				continue;
			}
			
			if ( manifest.getMainClass() == null || manifest.getMainClass().isEmpty() ) {
				LEnv.LOGGER.debug( "Invalid external module manifest, missing main class property!" );
				continue;
			}
			
			// Manifest verified.
			manifest.setModuleBean( mb );
			manifestList.add( manifest );
		}
		
		return manifestList;
	}
	
}
