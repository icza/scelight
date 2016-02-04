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

/**
 * Represents an absolute date as a relative date, relative to the creation time (current time at the object creation).
 * 
 * <p>
 * {@link #toString()} is properly overridden to produce the relative date as a human readable string (in English).
 * </p>
 * 
 * <p>
 * Instances are "table-friendly": you can use instances in tables and they can be sorted properly.
 * </p>
 * 
 * @author Andras Belicza
 * 
 * @see hu.scelightapi.service.IFactory#newRelativeDate(java.util.Date)
 * @see hu.scelightapi.service.IFactory#newRelativeDate(long)
 * @see hu.scelightapi.service.IFactory#newRelativeDate(java.util.Date, boolean, int, boolean)
 * @see hu.scelightapi.service.IFactory#newRelativeDate(long, boolean, int, boolean)
 */
public interface IRelativeDate extends Comparable< IRelativeDate > {
	
	/**
	 * Returns the absolute value of the represented relative time to the creation time, in milliseconds.
	 * 
	 * @return the absolute value of the represented relative time to the creation time, in milliseconds
	 */
	long getDeltaMs();
	
	/**
	 * Tells if the represented absolute time is in the future.<br>
	 * Note: if the represented absolute date is equal to the creation date, this will be <code>false</code>.
	 * 
	 * @return true if the represented absolute time is in the future; false otherwise
	 */
	boolean isFuture();
	
	
	/**
	 * Returns the default value of long form used by {@link #toString()}.
	 * 
	 * @return the default value of long form used by {@link #toString()}
	 */
	boolean isLongForm();
	
	/**
	 * Returns the default value of tokens used by {@link #toString()}.
	 * 
	 * @return the default value of tokens used by {@link #toString()}
	 */
	int getTokens();
	
	/**
	 * Returns the default value of append era used by {@link #toString()}.
	 * 
	 * @return the default value of append era used by {@link #toString()}
	 */
	boolean isAppendEra();
	
	/**
	 * Returns a human readable English string representation of this relative date using the default configuration returned by the {@link #isLongForm()},
	 * {@link #getTokens()} and {@link #isAppendEra()} methods.
	 * 
	 * @see #toString(boolean, int, boolean)
	 */
	@Override
	String toString();
	
	/**
	 * Returns a human readable English string representation of this relative date.
	 * 
	 * <p>
	 * The <code>longForm</code> parameter specifies if a long format is to be used, with whole words, else only letters.
	 * <table border=1>
	 * <tr>
	 * <th align=left>Unit long format:
	 * <td>sec
	 * <td>min
	 * <td>hour(s)
	 * <td>day(s)
	 * <td>week(s)
	 * <td>month(s)
	 * <td>year(s)
	 * <tr>
	 * <th align=left>Unit short format:
	 * <td>s
	 * <td>m
	 * <td>h
	 * <td>d
	 * <td>w
	 * <td>M
	 * <td>y
	 * </table>
	 * </p>
	 * 
	 * <p>
	 * The <code>tokens</code> parameter specifies how many tokens to include in the output.<br>
	 * Example outputs in case the relative date is <code>94903000 ms</code> in the past:
	 * <table border=1>
	 * <tr>
	 * <th align=left>Tokens:
	 * <td>1
	 * <td>2
	 * <td>3
	 * <td>&ge;4
	 * <tr>
	 * <th align=left>Long format:
	 * <td>1 day ago
	 * <td>1 day and 2 hours ago
	 * <td>1 day, 2 hours and 21 min ago
	 * <td>1 day, 2 hours, 21 min and 43 sec ago
	 * <tr>
	 * <th align=left>Short format:
	 * <td>1d ago
	 * <td>1d 2h ago
	 * <td>1d 2h 21m ago
	 * <td>1d 2h 21m 43s ago
	 * </table>
	 * </p>
	 * 
	 * @param longForm tells if long form is to be used for time units
	 * @param tokens tells how many tokens to include
	 * @param appendEra tells if era is to be appended; era is <code>"ago"</code> for past times and <code>"from now"</code> for future times
	 * @return a human readable English string representation of this relative date
	 */
	String toString( boolean longForm, int tokens, boolean appendEra );
	
}
