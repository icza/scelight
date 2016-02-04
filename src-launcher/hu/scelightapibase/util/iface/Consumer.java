/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapibase.util.iface;

/**
 * General generic consumer interface.
 * 
 * @param <T> type of the consumed value
 * 
 * @author Andras Belicza
 */
public interface Consumer< T > {
	
	/**
	 * Consumes a value.
	 * 
	 * @param value value to be consumed
	 */
	void consume( T value );
	
}
