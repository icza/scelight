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
 * A generic reference holder object whose reference can be changed.
 * 
 * @param <T> type of the held object
 * 
 * @author Andras Belicza
 * 
 * @see hu.scelightapi.service.IFactory#newHolder()
 * @see hu.scelightapi.service.IFactory#newHolder(Object)
 */
public interface IHolder< T > {
	
	/**
	 * Returns the reference of the held object.
	 * 
	 * @return the reference of the held object
	 */
	T get();
	
	/**
	 * Sets the reference of the held object
	 * 
	 * @param value the reference to be set as the held object
	 */
	void set( T value );
	
}
