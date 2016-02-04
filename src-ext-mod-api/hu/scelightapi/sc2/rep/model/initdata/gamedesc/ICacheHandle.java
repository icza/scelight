/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapi.sc2.rep.model.initdata.gamedesc;

import java.nio.file.Path;

/**
 * Cache handle (dependency).
 * 
 * @author Andras Belicza
 */
public interface ICacheHandle {
	
	/**
	 * Returns the dependency type (extension).
	 * 
	 * @return the dependency type (extension)
	 */
	String getType();
	
	/**
	 * Returns the region code.
	 * 
	 * @return the region code
	 */
	String getRegionCode();
	
	/**
	 * Returns the digest of the dependency content.
	 * 
	 * @return the digest of the dependency content
	 */
	String getContentDigest();
	
	/**
	 * Returns the SC2 Map folder relative map file.
	 * 
	 * @return the SC2 Map folder relative map file
	 */
	Path getRelativeFile();
	
	/**
	 * Returns the file name of the cache handle (with extension).
	 * 
	 * @return the file name of the cache handle (with extension)
	 */
	String getFileName();
	
	/**
	 * Returns the content of the cache handle if it is a <i>standard data</i>.
	 * 
	 * @return the content of the cache handle if it is a <i>standard data</i>; <code>null</code> otherwise
	 */
	String getStandardData();
	
	/**
	 * Returns the file name (with extension), and with the region code in square brackets. Also if the dependency is a <i>standard data</i>, it will be
	 * appended in parenthesis.
	 * 
	 * <p>
	 * Example:<br>
	 * <code>6de41503baccd05656360b6f027db88169fa1989bb6357b1b215a2547939f5fb.s2ma [EU] (Standard Data: Core.SC2Mod)</code>
	 * </p>
	 * 
	 * @return a string with the file name, extension, region code and optionally the content if the cache handle is a <i>standard data</i>
	 */
	@Override
	String toString();
	
}
