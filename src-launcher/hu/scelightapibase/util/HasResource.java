/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapibase.util;

import java.net.URL;

/**
 * Interface telling that an instance has a static resource ({@link URL}).
 * 
 * @author Andras Belicza
 */
public interface HasResource {
	
	/**
	 * Returns the represented uniform resource locator.
	 * 
	 * @return the represented uniform resource locator
	 */
	URL getResource();
	
}
