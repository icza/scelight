/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapi.r;

import java.io.InputStream;
import java.net.URL;

/**
 * Indicator class for the source of static resources of the External Module API.
 * 
 * Also provides helper methods to access resources.
 * 
 * @author Andras Belicza
 */
public class XR {
	
	/**
	 * Returns a {@link URL} for the specified name.
	 * 
	 * @param name name of the resource find
	 * @return the resource locator or <code>null</code> if no resource found for the specified name
	 */
	public static URL get( final String name ) {
		return XR.class.getResource( name );
	}
	
	/**
	 * Returns an input stream to read from the resource specified by its name.
	 * 
	 * @param name name of the resource find
	 * @return an input stream to read from the specified resource or <code>null</code> if no resource found for the specified name
	 */
	public static InputStream getStream( final String name ) {
		return XR.class.getResourceAsStream( name );
	}
	
}
