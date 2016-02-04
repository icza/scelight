/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.service.mapdl;

import hu.scelight.gui.icon.Icons;
import hu.scelight.sc2.rep.model.initdata.gamedesc.CacheHandle;
import hu.scelight.sc2.rep.model.initdata.gamedesc.Region;
import hu.scelight.service.env.Env;
import hu.scelight.service.settings.Settings;
import hu.scelight.util.job.DownloaderJob;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Map downloader job.
 * 
 * @author Andras Belicza
 */
public class MapDownloaderJob extends DownloaderJob {
	
	/** Cache handle of the map to be downloaded. */
	private final CacheHandle cacheHandle;
	
	/**
	 * Creates a new {@link MapDownloaderJob}.
	 * 
	 * @param cacheHandle cache handle of the map to be downloaded
	 */
	public MapDownloaderJob( final CacheHandle cacheHandle ) {
		super( "Map dl'er: " + cacheHandle.contentDigest, Icons.F_MAP );
		
		this.cacheHandle = cacheHandle;
		file = Env.APP_SETTINGS.get( Settings.SC2_MAPS_FOLDER ).resolve( cacheHandle.getRelativeFile() );
	}
	
	@Override
	public void jobRun() {
		Region region = null;
		
		// Regions to try: preferred region and replay's region
		for ( int i = Env.APP_SETTINGS.get( Settings.USE_PREFERRED_MAP_DL_REGION ) ? 0 : 1; i < 2 && mayContinue(); i++ ) {
			if ( i == 0 ) {
				// Preferred region
				region = Env.APP_SETTINGS.get( Settings.PREFERRED_MAP_DL_REGION );
			} else {
				// Replay's region
				final Region repRegion = Region.fromCode( cacheHandle.regionCode );
				if ( region == repRegion )
					continue; // Preferred region equals to the replay's region, do not try it again
				region = repRegion;
			}
			
			try {
				url = new URL( region.depotServerUrl, cacheHandle.getFileName() );
				
				super.jobRun();
				
				if ( isSuccess() )
					return; // No need to try other regions
			} catch ( final MalformedURLException me ) {
				// We will try other regions.
			}
		}
	}
	
}
