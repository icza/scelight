/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapi.util;

import java.util.Map;

/**
 * Interface modeling a class which has a source key-value structure (a {@link Map}) where keys are {@link String}s, and provides easy access to its content.
 * 
 * @author Andras Belicza
 */
public interface IStructView {
	
	/**
	 * Returns the value of the field specified by its name.
	 * 
	 * @param <T> (expected) type of the value
	 * @param name name of the field
	 * @return the value of the field specified by its name
	 */
	< T > T get( final String name );
	
	/**
	 * Returns the value for the field specified by its path.
	 * 
	 * <p>
	 * This method returns a value acquired in <code>n</code> steps (where <code>n</code> is the number of path elements).<br>
	 * The first step is executed on <code>this</code>, and the step result is the value returned by {@link #get(String)} for the first path element.<br>
	 * The result of the <code>i</code><sup>th</sup> step is the value returned by {@link #get(String)} called on the result of the <code>i-1</code>
	 * <sup>th</sup> step which must be of type {@link IStructView}.<br>
	 * The return value of the method is the result of the <code>n</code><sup>th</sup> step.<br>
	 * <br>
	 * If the result of any steps is <code>null</code>, then <code>null</code> is returned.
	 * </p>
	 * 
	 * @param <T> (expected) type of the value
	 * @param path path to the field
	 * @return the value of the field specified by its path
	 */
	< T > T get( final String[] path );
	
	/**
	 * Returns the source structure.
	 * 
	 * @return the source structure
	 */
	Map< String, Object > getStruct();
	
}
