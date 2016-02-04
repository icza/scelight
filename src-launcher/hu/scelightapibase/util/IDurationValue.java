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
 * A wrapper interface holding a time duration value in milliseconds and format hint for rendering (to a text).
 * 
 * <p>
 * Instances are "table-friendly": you can use instances in tables and they can be sorted properly.
 * </p>
 * 
 * @author Andras Belicza
 * 
 * @see hu.scelightapi.service.IFactory#newDurationValue(long)
 * @see hu.scelightapi.service.IFactory#newDurationValue(long, IDurationFormat)
 * @see IDurationFormat
 */
public interface IDurationValue extends Comparable< IDurationValue > {
	
	/**
	 * Returns the time duration value in milliseconds.
	 * 
	 * @return the time duration value in milliseconds
	 */
	long getValue();
	
	/**
	 * Returns the duration format hint for renderers.
	 * 
	 * @return the duration format hint for renderers
	 */
	IDurationFormat getDurationFormat();
	
}
