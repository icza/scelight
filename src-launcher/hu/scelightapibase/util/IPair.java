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
 * A pair of generic-type objects.
 * 
 * @param <T1> type of the first object
 * @param <T2> type of the second object
 * 
 * @author Andras Belicza
 * 
 * @see hu.scelightapi.service.IFactory#newPair(Object, Object)
 */
public interface IPair< T1, T2 > {
	
	/**
	 * Returns the first object.
	 * 
	 * @return the first object
	 */
	T1 getValue1();
	
	/**
	 * Returns the second object.
	 * 
	 * @return the second object
	 */
	T2 getValue2();
	
	/**
	 * Returns <code>true</code> if both values of the pairs in this object and in the other one are equal.
	 * 
	 * <p>
	 * If a value is <code>null</code>, then the value in the other pair must also be <code>null</code> in order to be equal.
	 * </p>
	 */
	@Override
	boolean equals( Object o );
	
	/**
	 * Properly overrides {@link Object#hashCode()}.
	 */
	@Override
	int hashCode();
	
}
