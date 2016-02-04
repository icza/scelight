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

/**
 * A wrapper interface holding a size value in bytes and format hint for rendering (to a text).
 * 
 * <p>
 * Instances are "table-friendly": you can use instances in tables and they can be sorted properly.
 * </p>
 * 
 * @author Andras Belicza
 * 
 * @see hu.scelightapi.service.IFactory#newSizeValue(long)
 * @see hu.scelightapi.service.IFactory#newSizeValue(long, ISizeFormat)
 * @see ISizeFormat
 */
public interface ISizeValue extends Comparable< ISizeValue > {
	
	/**
	 * Returns the size value in bytes.
	 * 
	 * @return the size value in bytes
	 */
	long getValue();
	
	/**
	 * Returns the size format hint for renderers.
	 * 
	 * @return the size format hint for renderers
	 */
	ISizeFormat getSizeFormat();
	
}
