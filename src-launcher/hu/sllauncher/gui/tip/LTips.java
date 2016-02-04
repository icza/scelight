/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllauncher.gui.tip;

import static hu.sllauncher.gui.tip.LTips.ResUtil.t;
import hu.sllauncher.util.LRHtml;

/**
 * Launcher tip HTML template resource collection.
 * 
 * <p>
 * <i>Implementation note:</i> This is an interface instead of a class because constants defined in an interface are implicitly <code>public</code>,
 * <code>static</code> and <code>final</code>, and so these keywords can be omitted!
 * </p>
 * 
 * @author Andras Belicza
 */
public interface LTips {
	
	/** Settings tip. */
	LRHtml SETTINGS = new LRHtml( "Settings", t( "settings" ) );
	
	
	
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
