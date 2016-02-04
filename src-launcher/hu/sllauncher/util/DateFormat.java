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
import hu.sllauncher.service.env.LEnv;

import java.util.Date;

/**
 * Representation formats of date+time values.
 * 
 * @author Andras Belicza
 */
public enum DateFormat implements IDateFormat {
	
	/** Date only. */
	DATE( "Date" ),
	
	/** Time only. */
	TIME( "Time" ),
	
	/** Date+Time. */
	DATE_TIME( "Date+Time" ),
	
	/** Date+Time including milliseconds. */
	DATE_TIME_MS( "Date+Time ms" ),
	
	/** Date+Time+Short Relative time. */
	DATE_TIME_REL_SHORT( "Date+Time+Relative short" ),
	
	/** Date+Time+Long Relative time. */
	DATE_TIME_REL_LONG( "Date+Time+Relative long" ),
	
	/** Short Relative time only. */
	RELATIVE_SHORT( "Short Relative" ),
	
	/** Long Relative time only. */
	RELATIVE_LONG( "Long Relative" );
	
	
	/** Text value of the date format. */
	public final String text;
	
	
	/**
	 * Creates a new {@link DateFormat}.
	 * 
	 * @param text text value of the date format
	 */
	private DateFormat( final String text ) {
		this.text = text;
	}
	
	@Override
	public String toString() {
		return text;
	}
	
	@Override
    public String formatDate( final Date date ) {
		switch ( this ) {
			case DATE :
				return LEnv.LANG.formatDate( date );
			case TIME :
				return LEnv.LANG.formatTime( date );
			default:
			case DATE_TIME :
				return LEnv.LANG.formatDateTime( date );
			case DATE_TIME_MS :
				return LEnv.LANG.formatDateTimeMs( date );
			case DATE_TIME_REL_SHORT :
				return LEnv.LANG.formatDateTimeWithRelative( date, false );
			case DATE_TIME_REL_LONG :
				return LEnv.LANG.formatDateTimeWithRelative( date );
			case RELATIVE_SHORT :
				return new RelativeDate( date, false, 2, true ).toString();
			case RELATIVE_LONG :
				return new RelativeDate( date, true, 2, true ).toString();
		}
	}
	
	
	/** Cache of the values array. */
	public static final DateFormat[] VALUES = values();
	
}
