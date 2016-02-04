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

import hu.scelightapibase.util.ISizeFormat;
import hu.scelightapibase.util.ISizeValue;

/**
 * A wrapper class holding a size value in bytes and format hint for rendering (to a text).
 * 
 * <p>
 * Implementation is IMMUTABLE.
 * </p>
 * 
 * @author Andras Belicza
 */
public class SizeValue implements ISizeValue {
	
	/** Size value in bytes. */
	public final long        value;
	
	/** Size format hint for renderers. */
	public final ISizeFormat sizeFormat;
	
	/**
	 * Creates a new {@link SizeValue} with {@link SizeFormat#AUTO} size format.
	 * 
	 * @param value size value in bytes
	 */
	public SizeValue( final long value ) {
		this( value, SizeFormat.AUTO );
	}
	
	/**
	 * Creates a new {@link SizeValue}.
	 * 
	 * @param value size value in bytes
	 * @param sizeFormat size format hint for renderers
	 */
	public SizeValue( final long value, final ISizeFormat sizeFormat ) {
		this.value = value;
		this.sizeFormat = sizeFormat;
	}
	
	@Override
	public long getValue() {
		return value;
	}
	
	@Override
	public ISizeFormat getSizeFormat() {
		return sizeFormat;
	}
	
	@Override
	public int compareTo( final ISizeValue o ) {
		return Long.compare( value, o.getValue() );
	}
	
	@Override
	public String toString() {
		return sizeFormat.formatSize( value, 2 );
	}
	
}
