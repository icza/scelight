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

/**
 * A modifiable <code>boolean</code> wrapper.
 * 
 * <p>
 * For useful cases see {@link Int}.
 * </p>
 * 
 * @author Andras Belicza
 * 
 * @see Int
 */
public class Bool implements Comparable< Bool > {
	
	/** The wrapped boolean value. */
	public boolean value;
	
	/**
	 * Creates a new {@link Bool}.
	 */
	public Bool() {
	}
	
	/**
	 * Creates a new {@link Bool}.
	 * 
	 * @param value initial value
	 */
	public Bool( final boolean value ) {
		this.value = value;
	}
	
	/**
	 * Returns <code>true</code> if both boolean wrapper has the same value.
	 */
	@Override
	public boolean equals( final Object o ) {
		if ( o == this )
			return true;
		if ( !( o instanceof Bool ) )
			return false;
		
		return value == ( (Bool) o ).value;
	}
	
	@Override
	public int hashCode() {
		return Boolean.valueOf( value ).hashCode();
	}
	
	@Override
	public int compareTo( final Bool b ) {
		return Boolean.compare( value, b.value );
	}
	
}
