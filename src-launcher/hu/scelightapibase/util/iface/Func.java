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
 * General generic function interface.
 * <p>
 * Takes a parameter and returns a value.
 * </p>
 * 
 * @param <T> input type of the function
 * @param <R> result or return type of the function
 * 
 * @author Andras Belicza
 */
public interface Func< T, R > {
	
	/**
	 * Executes the function.
	 * 
	 * @param t parameter of the function
	 * @return the result of the execution
	 */
	R exec( T t );
	
}
