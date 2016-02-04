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

import hu.sllauncher.util.DateFormat;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Representation formats of date+time values.
 * 
 * @since 1.1
 * 
 * @author Andras Belicza
 * 
 * @see IEnum
 */
public interface IDateFormat extends IEnum {
	
	/** Date only. */
	IDateFormat DATE = DateFormat.DATE;
	
	/** Time only. */
	IDateFormat TIME = DateFormat.TIME;
	
	/** Date+Time. */
	IDateFormat DATE_TIME = DateFormat.DATE_TIME;
	
	/** Date+Time including milliseconds. */
	IDateFormat DATE_TIME_MS = DateFormat.DATE_TIME_MS;
	
	/** Date+Time+Short Relative time. */
	IDateFormat DATE_TIME_REL_SHORT = DateFormat.DATE_TIME_REL_SHORT;
	
	/** Date+Time+Long Relative time. */
	IDateFormat DATE_TIME_REL_LONG = DateFormat.DATE_TIME_REL_LONG;
	
	/** Short Relative time only. */
	IDateFormat RELATIVE_SHORT = DateFormat.RELATIVE_SHORT;
	
	/** Long Relative time only. */
	IDateFormat RELATIVE_LONG = DateFormat.RELATIVE_LONG;
	
	
	/** An unmodifiable list of all the date formats. */
	List< IDateFormat > VALUE_LIST   = Collections.unmodifiableList( Arrays.< IDateFormat > asList( DateFormat.VALUES ) );
	
	
	/**
	 * Returns a formatted date value.
	 * 
	 * @param date date value to be formatted
	 * @return a formatted date value
	 */
	String formatDate( Date date );
	
	
}
