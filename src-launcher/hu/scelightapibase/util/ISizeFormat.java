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

import hu.sllauncher.util.SizeFormat;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Representation formats of a size (capacity) value.
 * 
 * @author Andras Belicza
 * 
 * @see IEnum
 */
public interface ISizeFormat extends IEnum {
	
	/** Auto, depends on size value. */
	ISizeFormat         AUTO       = SizeFormat.AUTO;
	
	/** Bytes. */
	ISizeFormat         BYTES      = SizeFormat.BYTES;
	
	/** KB. */
	ISizeFormat         KB         = SizeFormat.KB;
	
	/** MB. */
	ISizeFormat         MB         = SizeFormat.MB;
	
	/** GB. */
	ISizeFormat         GB         = SizeFormat.GB;
	
	/** TB. */
	ISizeFormat         TB         = SizeFormat.TB;
	
	
	/** An unmodifiable list of all the size formats. */
	List< ISizeFormat > VALUE_LIST = Collections.unmodifiableList( Arrays.< ISizeFormat > asList( SizeFormat.VALUES ) );
	
	
	/**
	 * Returns a formatted size value.
	 * 
	 * @param size size to be formatted
	 * @param fractionDigits number of fraction digits if output is not in bytes
	 * @return the formatted size value
	 */
	String formatSize( final long size, final int fractionDigits );
	
}
