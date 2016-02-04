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

import hu.scelightapibase.util.IDurationFormat;
import hu.scelightapibase.util.IDurationValue;

/**
 * A wrapper class holding a time duration value in milliseconds and format hint for rendering (to a text).
 * 
 * <p>
 * Implementation is IMMUTABLE.
 * </p>
 * 
 * @author Andras Belicza
 */
public class DurationValue implements IDurationValue {
	
	/** Time duration value in milliseconds. */
	public final long            value;
	
	/** Duration format hint for renderers. */
	public final IDurationFormat durationFormat;
	
	/**
	 * Creates a new {@link DurationValue}.
	 * 
	 * @param value time duration value in milliseconds
	 */
	public DurationValue( final long value ) {
		this( value, DurationFormat.AUTO );
	}
	
	/**
	 * Creates a new {@link DurationValue}.
	 * 
	 * @param value time duration value in milliseconds
	 * @param durationFormat duration format hint for renderers
	 */
	public DurationValue( final long value, final IDurationFormat durationFormat ) {
		this.value = value;
		this.durationFormat = durationFormat;
	}
	
	@Override
	public long getValue() {
		return value;
	}
	
	@Override
	public IDurationFormat getDurationFormat() {
		return durationFormat;
	}
	
	@Override
	public int compareTo( final IDurationValue o ) {
		return Long.compare( value, o.getValue() );
	}
	
	@Override
	public String toString() {
		return durationFormat.formatDuration( value );
	}
	
}
