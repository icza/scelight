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

import hu.scelightapibase.util.IDateFormat;
import hu.scelightapibase.util.IDateValue;

import java.util.Date;

/**
 * A wrapper class holding a date+time value and format hint for rendering (to a text).
 * 
 * <p>
 * Implementation is IMMUTABLE (as far as {@link Date} being immutable... which is not sadly).
 * </p>
 * 
 * @author Andras Belicza
 */
public class DateValue implements IDateValue {
	
	/** Date value . */
	public final Date        value;
	
	/** Date format hint for renderers. */
	public final IDateFormat dateFormat;
	
	/**
	 * Creates a new {@link DateValue}.
	 * 
	 * @param value date value
	 */
	public DateValue( final Date value ) {
		this( value, DateFormat.DATE_TIME );
	}
	
	/**
	 * Creates a new {@link DateValue}.
	 * 
	 * @param value date value
	 * @param dateFormat date format hint for renderers
	 */
	public DateValue( final Date value, final IDateFormat dateFormat ) {
		this.value = value;
		this.dateFormat = dateFormat;
	}
	
	@Override
	public Date getValue() {
		return value;
	}
	
	@Override
	public IDateFormat getDateFormat() {
		return dateFormat;
	}
	
	@Override
	public int compareTo( final IDateValue o ) {
		return value.compareTo( o.getValue() );
	}
	
	@Override
	public String toString() {
		return dateFormat.formatDate( value );
	}
	
}
