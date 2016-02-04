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

import hu.scelightapi.service.IFactory;
import hu.scelightapibase.bean.IVersionBean;

/**
 * Interface representing a version with any number of parts.
 * 
 * <p>
 * {@link #equals(Object)}, {@link #hashCode()} and {@link #toString()} are properly overridden.
 * </p>
 * 
 * <p>
 * Instances can be acquired by {@link IFactory#newVersionView(int...)} or {@link IFactory#newVersionView(String)}.
 * </p>
 * 
 * @author Andras Belicza
 * 
 * @see IFactory#newVersionView(int...)
 * @see IFactory#newVersionView(String)
 * @see IVersionBean
 */
public interface IVersionView extends Comparable< IVersionView > {
	
	/**
	 * Returns the number of parts of this version view.
	 * 
	 * @return the number of parts of this version view
	 */
	int length();
	
	/**
	 * Returns the <code>i</code><sup>th</sup> part of this version view.
	 * 
	 * @param i part number to be returned (0-based)
	 * @return the <code>i</code><sup>th</sup> part of this version view
	 */
	int part( int i );
	
	/**
	 * Implements strict comparison.
	 * 
	 * @see #compareTo(IVersionView, boolean)
	 */
	@Override
	int compareTo( IVersionView v );
	
	/**
	 * Compares this version view to the specified other version view.
	 * 
	 * <p>
	 * If strict mode is not enabled and the common parts of the version views are equal, the result will be 0.<br>
	 * If strict mode is enabled and the common parts of the version views are equal, the version view with more parts will be the greater.
	 * </p>
	 * 
	 * Examples:
	 * <table border=1 cellspacing=0 cellpadding=3>
	 * <tr>
	 * <th>Strict?
	 * <th>This
	 * <th>Other version
	 * <th>Result
	 * 
	 * <tr>
	 * <td>true
	 * <td>1.2.3
	 * <td>1.2.3
	 * <td>0
	 * <tr>
	 * <td>true
	 * <td>1.3.3
	 * <td>1.2.3.1
	 * <td>1
	 * <tr>
	 * <td>true
	 * <td>1.2.3
	 * <td>1.2.3.1
	 * <td>-1
	 * 
	 * <tr>
	 * <td>false
	 * <td>1.2.3
	 * <td>1.2.3
	 * <td>0
	 * <tr>
	 * <td>false
	 * <td>1.3.3
	 * <td>1.2.3.1
	 * <td>1
	 * <tr>
	 * <td>false
	 * <td>1.2.3
	 * <td>1.2.3.1
	 * <td><font color='red'>0</font>
	 * </table>
	 * 
	 * @param v other version view to compare to
	 * @param strict tells if only the common parts has to be checked
	 * @return a negative number if this version view is less than the specified other version, 0 if equals to, and a positive number if this version view is
	 *         greater than the specified other version
	 * 
	 * @see #compareTo(IVersionView)
	 */
	int compareTo( IVersionView v, boolean strict );
	
	/**
	 * Properly overridden to check if the number of parts and the parts are equal.
	 */
	@Override
	boolean equals( Object obj );
	
	/**
	 * Properly overridden along with {@link #equals(Object)}, {@link IVersionView}'s can be used in hash-based structures.
	 */
	@Override
	int hashCode();
	
	/**
	 * Properly overridden to return the string representation of this version view.
	 */
	@Override
	String toString();
	
}
