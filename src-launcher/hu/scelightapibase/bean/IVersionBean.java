/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapibase.bean;

/**
 * Version bean interface.
 * 
 * <p>
 * Defines the version of an entity (which can be an application, a module etc.).
 * </p>
 * 
 * @author Andras Belicza
 * 
 * @see hu.scelightapi.util.IVersionView IVersionView
 * @see IBean
 */
public interface IVersionBean extends Comparable< IVersionBean >, IBean {
	
	/**
	 * The major version number
	 * 
	 * @return the major version number
	 */
	int getMajor();
	
	/**
	 * The minor version number.
	 * 
	 * @return minor version number
	 */
	int getMinor();
	
	/**
	 * The revision version number.
	 * 
	 * @return revision version number
	 */
	int getRevision();
	
	/**
	 * Returns a string representation of the version.
	 * <p>
	 * Returns a non-full version string (a full version includes the revision number even if its zero).
	 * </p>
	 * 
	 * @return a string representation of the version
	 * 
	 * @see #toString(boolean)
	 */
	@Override
	String toString();
	
	/**
	 * Returns a string representation of the version.
	 * <p>
	 * A full version includes the revision number even if its zero.
	 * </p>
	 * 
	 * @param full tells if a full format is required
	 * @return a string representation of the version
	 */
	String toString( boolean full );
	
}
