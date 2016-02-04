/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllauncher.util;

import java.nio.file.Files;
import java.util.Map;

/**
 * A modifiable <code>int</code> wrapper.
 * 
 * <p>
 * Useful for example when:
 * </p>
 * <ul>
 * <li>A counter is to be stored in a {@link Map}; so no new {@link Integer}s have to be allocated and put back when counter value is changed.
 * <li>An anonymous class needs to refer to / change a counter defined outside of it (typical use case when counting files with
 * {@link Files#walkFileTree(java.nio.file.Path, java.nio.file.FileVisitor)}).
 * </ul>
 * 
 * @author Andras Belicza
 */
public class Int implements Comparable< Int > {
	
	/** The wrapped int value. */
	public int value;
	
	/**
	 * Creates a new {@link Int}.
	 */
	public Int() {
	}
	
	/**
	 * Creates a new {@link Int}.
	 * 
	 * @param value initial value
	 */
	public Int( final int value ) {
		this.value = value;
	}
	
	/**
	 * Returns <code>true</code> if both int wrapper has the same value.
	 */
	@Override
	public boolean equals( final Object o ) {
		if ( o == this )
			return true;
		if ( !( o instanceof Int ) )
			return false;
		
		return value == ( (Int) o ).value;
	}
	
	@Override
	public int hashCode() {
		return value;
	}
	
	@Override
	public int compareTo( final Int i ) {
		return Integer.compare( value, i.value );
	}
	
}
