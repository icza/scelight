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
 * This interface indicates that the implementor instance is in fact an instance of {@link Enum} and makes its methods accessible without having to cast to
 * {@link Enum}. This also means that you can safely use reference equality check on instances.
 * 
 * @author Andras Belicza
 */
public interface IEnum {
	
	/**
	 * This method is to make {@link Enum#name()} accessible.
	 * 
	 * @return the value specified by {@link Enum#name()}
	 */
	String name();
	
	/**
	 * This method is to make {@link Enum#ordinal()} accessible.
	 * 
	 * @return the value specified by {@link Enum#ordinal()}
	 */
	int ordinal();
	
	/**
	 * Usually returns a human readable name of the enum instance.
	 * 
	 * <p>
	 * This may or may not be HTML formatted text, but it can be used as the text of Swing components.
	 * </p>
	 * 
	 * @return a human readable name of the enum instance
	 */
	@Override
	String toString();
	
}
