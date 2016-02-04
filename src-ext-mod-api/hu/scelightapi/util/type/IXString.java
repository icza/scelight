/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapi.util.type;

/**
 * A {@link String} wrapper which stores the original byte array it was created from.
 * 
 * @author Andras Belicza
 */
public interface IXString {
	
	/**
	 * Returns the source of the string, interpreted as UTF-8 encoded characters.
	 * 
	 * @return the source of the string, interpreted as UTF-8 encoded characters
	 */
	byte[] getArray();
	
	/**
	 * Returns the wrapped {@link String}.
	 * 
	 * @return the wrapped {@link String}
	 */
	@Override
	String toString();
	
}
