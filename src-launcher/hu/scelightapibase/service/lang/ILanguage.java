/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapibase.service.lang;

import hu.scelightapibase.bean.person.IPersonNameBean;

import java.util.Date;

/**
 * Language and locale specific utilities.
 * 
 * @author Andras Belicza
 */
public interface ILanguage {
	
	/** Max number of fraction digits to be allowed/handled. */
	int MAX_FRACTION_DIGITS = 5;
	
	
	/**
	 * Formats the specified date (using the user's date/time format preference).
	 * 
	 * @param date date to be formatted
	 * @return the formatted date
	 */
	String formatDate( Date date );
	
	/**
	 * Formats the specified time (using the user's date/time format preference).
	 * 
	 * @param time time to be formatted
	 * @return the formatted time
	 */
	String formatTime( Date time );
	
	/**
	 * Formats the specified date+time (using the user's date/time format preference).
	 * 
	 * @param dateTime date+time to be formatted
	 * @param ms tells if milliseconds is to be included
	 * @return the formatted date+time
	 */
	String formatDateTime( Date dateTime, boolean ms );
	
	/**
	 * Formats the specified date+time (using the user's date/time format preference).
	 * 
	 * @param dateTime date+time to be formatted
	 * @return the formatted date+time
	 */
	String formatDateTime( Date dateTime );
	
	/**
	 * Formats the specified date+time (milliseconds included) (using the user's date/time format preference).
	 * 
	 * @param dateTime date+time to be formatted
	 * @return the formatted date+time (milliseconds included)
	 */
	String formatDateTimeMs( Date dateTime );
	
	/**
	 * Formats the specified date+time (using the user's date/time format preference), also appending the relative date+time in parenthesis (using long form).
	 * The returned string is HTML text (starts and ends with the <code>&lt;html&gt;</code> tags).
	 * 
	 * @param dateTime date+time to be formatted
	 * @return the formatted date+time (HTML text)
	 * 
	 * @see #formatDateTimeWithRelative(Date, boolean)
	 */
	String formatDateTimeWithRelative( Date dateTime );
	
	/**
	 * Formats the specified date+time (using the user's date/time format preference), also appending the relative date+time in parenthesis. The returned string
	 * is HTML text (starts and ends with the <code>&lt;html&gt;</code> tags).
	 * 
	 * @param dateTime date+time to be formatted
	 * @param longForm tells if long form is to be used in the relative time part
	 * @return the formatted date+time (HTML text)
	 * 
	 * @since 1.1
	 */
	String formatDateTimeWithRelative( Date dateTime, boolean longForm );
	
	/**
	 * Formats the specified number (using the user's number format preference).
	 * 
	 * @param n number to be formatted
	 * @return the formatted number
	 * 
	 * @see #formatNumber(double, int)
	 */
	String formatNumber( long n );
	
	/**
	 * Formats the specified number (using the user's number format preference).
	 * 
	 * @param n number to be formatted
	 * @param fractionDigits number of fraction digits to use
	 * @return the formatted number
	 * 
	 * @throws IllegalArgumentException if fraction digits is negative or greater than {@link #MAX_FRACTION_DIGITS} ({@value #MAX_FRACTION_DIGITS})
	 * 
	 * @see #formatNumber(long)
	 */
	String formatNumber( double n, int fractionDigits );
	
	/**
	 * Formats the specified person name (using the user's person name format preference).
	 * 
	 * @param personName person name to be formatted
	 * @return the formatted person name
	 */
	String formatPersonName( IPersonNameBean personName );
	
}
