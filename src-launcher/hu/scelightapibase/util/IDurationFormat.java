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

import hu.sllauncher.util.DurationFormat;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Representation formats of a time duration value.
 * 
 * @author Andras Belicza
 * 
 * @see IEnum
 * @see IDurationValue
 */
public interface IDurationFormat extends IEnum {
	
	/** Auto, depends on duration value. */
	IDurationFormat         AUTO         = DurationFormat.AUTO;
	
	/** Millisecond. */
	IDurationFormat         MS           = DurationFormat.MS;
	
	/** Second. */
	IDurationFormat         SEC          = DurationFormat.SEC;
	
	/** Second and millisecond. */
	IDurationFormat         SEC_MS       = DurationFormat.SEC_MS;
	
	/** Minute and second. */
	IDurationFormat         MIN_SEC      = DurationFormat.MIN_SEC;
	
	/** Hour and minute and second. */
	IDurationFormat         HOUR_MIN_SEC = DurationFormat.HOUR_MIN_SEC;
	
	
	/** An unmodifiable list of all the duration formats. */
	List< IDurationFormat > VALUE_LIST   = Collections.unmodifiableList( Arrays.< IDurationFormat > asList( DurationFormat.VALUES ) );
	
	
	/**
	 * Returns a formatted time duration value.
	 * 
	 * @param ms millisecond time duration to be formatted
	 * @return the formatted time duration value
	 */
	String formatDuration( long ms );
	
}
