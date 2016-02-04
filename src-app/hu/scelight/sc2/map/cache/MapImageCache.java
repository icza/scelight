/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.sc2.map.cache;

import hu.scelight.sc2.map.MapParser;
import hu.scelight.sc2.rep.repproc.RepProcessor;
import hu.scelight.service.env.Env;
import hu.scelight.util.Utils;
import hu.sllauncher.bean.VersionBean;
import hu.sllauncher.gui.icon.LRIcon;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.xml.bind.JAXB;

/**
 * Map image cache.
 * 
 * @author Andras Belicza
 */
public class MapImageCache {
	
	/** Version of the map image cache. */
	public static final VersionBean VERSION          = new VersionBean( 1, 1 );
	
	
	/** Folder where the map images are stored. */
	private static final Path       CACHE_FOLDER     = Env.PATH_WORKSPACE.resolve( "map-img-cache" );
	
	/** Path of the cache config bean file. */
	private static final Path       PATH_CONFIG_BEAN = CACHE_FOLDER.resolve( "config.xml" );
	
	/** Cache config bean. */
	private static ConfigBean       config;
	static {
		// Load config on "startup"
		ConfigBean config = getConfig();
		
		if ( config == null || !VERSION.equals( config.getVersion() ) ) {
			// Clear map image cache
			Utils.deletePath( CACHE_FOLDER );
			
			// Recreate cache folder
			if ( !Files.exists( CACHE_FOLDER ) )
				try {
					Files.createDirectories( CACHE_FOLDER );
				} catch ( final IOException ie ) {
					Env.LOGGER.error( "Failed to create map image cache folder: " + CACHE_FOLDER, ie );
				}
			
			// Re-save current config
			config = new ConfigBean();
			config.setVersion( VERSION );
			setConfig( config );
		}
	}
	
	/**
	 * Returns the cache config bean.
	 * 
	 * @return the cache config bean
	 */
	private static ConfigBean getConfig() {
		if ( config == null && Files.exists( PATH_CONFIG_BEAN ) )
			try {
				config = JAXB.unmarshal( PATH_CONFIG_BEAN.toFile(), ConfigBean.class );
			} catch ( final Exception e ) {
				Env.LOGGER.error( "Failed to read map image cache config from: " + PATH_CONFIG_BEAN, e );
			}
		
		return config;
	}
	
	/**
	 * Sets the cache config bean.
	 * 
	 * @param config cache config bean to be set
	 */
	private static void setConfig( final ConfigBean config ) {
		try {
			JAXB.marshal( config, PATH_CONFIG_BEAN.toFile() );
			MapImageCache.config = config;
		} catch ( final Exception e ) {
			Env.LOGGER.error( "Failed to write map image cache config to: " + PATH_CONFIG_BEAN, e );
		}
	}
	
	
	/** In-memory map image cache, mapped from map hash name. */
	private static final Map< String, LRIcon > MAP_HASH_IMAGE_MAP = new HashMap<>();
	
	/**
	 * Returns the map image of the specified map file.
	 * 
	 * @param repProc {@link RepProcessor} whose map image to return
	 * @return the map image of the specified map file
	 */
	public static LRIcon getMapImage( final RepProcessor repProc ) {
		// Locally tested edited maps do not have cache handles, only the map name is filled in the game descriptions of the init data
		if ( repProc.getMapCacheHandle() == null )
			return null;
		
		final String hash = repProc.getMapCacheHandle().contentDigest;
		LRIcon ricon = MAP_HASH_IMAGE_MAP.get( hash );
		
		if ( ricon == null ) {
			final Path imgFile = CACHE_FOLDER.resolve( hash + ".jpg" );
			// Have to synchronize this, else if the same image is written by 2 threads, file gets corrupted.
			synchronized ( hash.intern() ) {
				if ( !Files.exists( imgFile ) ) {
					final BufferedImage mapImage = MapParser.getMapImage( repProc );
					if ( mapImage != null )
						try {
							ImageIO.write( mapImage, "jpg", imgFile.toFile() );
						} catch ( final IOException ie ) {
							Env.LOGGER.error( "Failed to write map image: " + imgFile, ie );
						}
				}
			}
			if ( Files.exists( imgFile ) )
				try {
					MAP_HASH_IMAGE_MAP.put( hash, ricon = new LRIcon( imgFile.toUri().toURL() ) );
					ricon.setStringValue( hash );
				} catch ( final MalformedURLException mue ) {
					// Never to happen
					Env.LOGGER.error( "", mue );
				}
		}
		
		return ricon;
	}
	
}
