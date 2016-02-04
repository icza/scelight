/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.sc2.textures;

import hu.sllauncher.gui.icon.LRIcon;
import hu.slsc2textures.r.TR;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * StarCraft II texture caching and servicing.
 * 
 * @author Andras Belicza
 */
public class Textures {
	
	/** Loaded textures mapped from icon name to ricon. */
	private static Map< String, TRIcon > ICON_NAME_RICON_MAP = new HashMap<>();
	
	/** Name of the lazily loaded icon to be returned if an icon is missing. */
	public static final String           MISSING_ICON_NAME   = "btn-missing-kaeo";
	
	/**
	 * Returns the icon with the specified name.
	 * 
	 * @param name name of the icon to return
	 * @return the icon with the specified name
	 */
	public static LRIcon getIcon( final String name ) {
		TRIcon ricon = ICON_NAME_RICON_MAP.get( name );
		
		if ( ricon == null ) {
			final URL url = TR.get( name + ".dds" );
			
			if ( url == null ) {
				TRIcon missingRicon = ICON_NAME_RICON_MAP.get( MISSING_ICON_NAME );
				if ( missingRicon == null )
					ICON_NAME_RICON_MAP.put( MISSING_ICON_NAME, missingRicon = new TRIcon( TR.get( MISSING_ICON_NAME + ".dds" ) ) );
				ricon = missingRicon;
			} else
				ricon = new TRIcon( url );
			
			ICON_NAME_RICON_MAP.put( name, ricon );
		}
		
		return ricon;
	}
	
}
