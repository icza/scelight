/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.gui.tip;

import static hu.scelight.gui.tip.Tips.ResUtil.t;
import hu.scelight.util.RHtml;

/**
 * Application tip HTML template resource collection.
 * 
 * <p>
 * <i>Implementation note:</i> This is an interface instead of a class because constants defined in an interface are implicitly <code>public</code>,
 * <code>static</code> and <code>final</code>, and so these keywords can be omitted!
 * </p>
 * 
 * @author Andras Belicza
 */
public interface Tips {
	
	/** Replay List Table tip. */
	RHtml REPLAY_LIST_TABLE           = new RHtml( "Replay List Table", t( "replay-list-table" ) );
	
	/** Map Image Zooming and Scrolling tip. */
	RHtml MAP_IMAGE_ZOOMING_SCROLLING = new RHtml( "Map Image Zooming and Scrolling", t( "map-image-zooming-scrolling" ) );
	
	/** Raw Data Text Selection tip. */
	RHtml TEXT_SELECTION              = new RHtml( "Text Selection", t( "text-selection" ) );
	
	/** Text Search in Binary Data tip. */
	RHtml BINARY_DATA_TEXT_SEARCH     = new RHtml( "Text Search in Binary Data", t( "binary-data-text-search" ) );
	
	
	
	/**
	 * Utility class to help assemble resource URL strings.
	 * 
	 * @author Andras Belicza
	 */
	public static class ResUtil {
		
		/**
		 * Returns the resource string of the help content specified by its name.
		 * 
		 * @param name name of the help content whose URL to return
		 * @return the resource string of the help content specified by its name
		 */
		public static String t( final String name ) {
			return "html/tip/" + name + ".html";
		}
		
	}
	
}
