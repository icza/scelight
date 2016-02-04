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
 * General generic producer interface.
 * <p>
 * Takes no argument and produces a value.
 * </p>
 * 
 * @param <R> type of the produced value
 * 
 * @author Andras Belicza
 */
public interface Producer< R > {
	
	/**
	 * Produces a value.
	 * 
	 * @return the produced value
	 */
	R produce();
	
}
