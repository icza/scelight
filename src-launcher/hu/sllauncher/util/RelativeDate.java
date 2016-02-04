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

import hu.scelightapibase.util.IRelativeDate;

import java.util.Date;

/**
 * Represents an absolute date as a relative date, relative to the creation time (current time at the object creation).
 * 
 * <p>
 * {@link #toString()} is properly overridden to produce the relative date as a human readable string (in English).
 * </p>
 * 
 * <p>
 * Implementation is IMMUTABLE.
 * </p>
 * 
 * @author Andras Belicza
 */
public class RelativeDate implements IRelativeDate {
	
	/**
	 * Time units used in string representations.
	 * 
	 * @author Andras Belicza
	 */
	private enum Unit {
		
		/** Second. */
		SEC( LUtils.MS_IN_MIN, 1000, 's', " sec", false ),
		
		/** Minute. */
		MIN( LUtils.MS_IN_HOUR, LUtils.MS_IN_MIN, 'm', " min", false ),
		
		/** Hour. */
		HOUR( LUtils.MS_IN_DAY, LUtils.MS_IN_HOUR, 'h', " hour", true ),
		
		/** Day. */
		DAY( LUtils.MS_IN_WEEK, LUtils.MS_IN_DAY, 'd', " day", true ),
		
		/** Week. */
		WEEK( LUtils.MS_IN_MONTH, LUtils.MS_IN_WEEK, 'w', " week", true ),
		
		/** Month. */
		MONTH( LUtils.MS_IN_YEAR, LUtils.MS_IN_MONTH, 'M', " month", true ),
		
		/** Year. */
		YEAR( Long.MAX_VALUE, LUtils.MS_IN_YEAR, 'y', " year", true );
		
		
		/** Milliseconds limit for this unit to be used. */
		public final long    msLimit;
		
		/** Number of milliseconds in this unit. */
		public final long    ms;
		
		/** Letter (short form) of this unit. */
		public final char    letter;
		
		/** Name (long form) of this unit. Starts with a space intended for building purposes. */
		public final String  name;
		
		/** Tells if the name of this unit has a plural form. */
		public final boolean plural;
		
		/**
		 * Creates a new {@link Unit}.
		 * 
		 * @param msLimit milliseconds limit for this unit to be used
		 * @param ms number of milliseconds in this unit
		 * @param letter letter (short form) of this unit
		 * @param name name (long form) of this unit
		 * @param plural Tells if the name of this unit has a plural form
		 */
		private Unit( final long msLimit, final long ms, final char letter, final String name, final boolean plural ) {
			this.msLimit = msLimit;
			this.ms = ms;
			this.letter = letter;
			this.name = name;
			this.plural = plural;
		}
	}
	
	/** Cache of the Units array. */
	private static final Unit[] UNITS = Unit.values();
	
	
	
	/** Absolute value of the represented relative time to the creation time, in milliseconds. */
	public final long           deltaMs;
	
	/**
	 * Tells if the represented absolute time is in the future.<br>
	 * Note: if the represented absolute date is equal to the creation date, this will be <code>false</code>.
	 */
	public final boolean        future;
	
	
	/** Default value of long form used by {@link #toString()}. */
	public final boolean        longForm;
	
	/** Default value of tokens used by {@link #toString()}. */
	public final int            tokens;
	
	/** Default value of append era used by {@link #toString()}. */
	public final boolean        appendEra;
	
	
	/**
	 * Creates a new {@link RelativeDate}.
	 * 
	 * @param date absolute date to be represented relatively to the current time
	 */
	public RelativeDate( final Date date ) {
		this( date.getTime() );
	}
	
	/**
	 * Creates a new {@link RelativeDate}.
	 * 
	 * @param date absolute date to be represented relatively to the current time
	 */
	public RelativeDate( final long date ) {
		this( date, false, 2, true );
	}
	
	/**
	 * Creates a new {@link RelativeDate}.
	 * 
	 * See {@link #toString(boolean, int, boolean)} for details.
	 * 
	 * @param date absolute date to be represented relatively to the current time
	 * @param longForm tells if long form is to be used for time units
	 * @param tokens tells how many tokens to include
	 * @param appendEra tells if era is to be appended; era is <code>"ago"</code> for past times and <code>"from now"</code> for future times
	 * 
	 * @see #toString(boolean, int, boolean)
	 */
	public RelativeDate( final Date date, final boolean longForm, final int tokens, final boolean appendEra ) {
		this( date.getTime(), longForm, tokens, appendEra );
	}
	
	/**
	 * Creates a new {@link RelativeDate}.
	 * 
	 * See {@link #toString(boolean, int, boolean)} for details.
	 * 
	 * @param date absolute date to be represented relatively to the current time
	 * @param longForm tells if long form is to be used for time units
	 * @param tokens tells how many tokens to include
	 * @param appendEra tells if era is to be appended; era is <code>"ago"</code> for past times and <code>"from now"</code> for future times
	 * 
	 * @see #toString(boolean, int, boolean)
	 */
	public RelativeDate( final long date, final boolean longForm, final int tokens, final boolean appendEra ) {
		final long delta = date - System.currentTimeMillis();
		deltaMs = delta < 0 ? -delta : delta;
		future = delta > 0;
		
		this.longForm = longForm;
		this.tokens = tokens;
		this.appendEra = appendEra;
	}
	
	@Override
	public long getDeltaMs() {
		return deltaMs;
	}
	
	@Override
	public boolean isFuture() {
		return future;
	}
	
	@Override
	public boolean isLongForm() {
		return longForm;
	}
	
	@Override
	public int getTokens() {
		return tokens;
	}
	
	@Override
	public boolean isAppendEra() {
		return appendEra;
	}
	
	@Override
	public String toString() {
		return toString( longForm, tokens, appendEra );
	}
	
	@Override
	public String toString( final boolean longForm, int tokens, final boolean appendEra ) {
		final StringBuilder sb = new StringBuilder();
		
		long deltaMs = this.deltaMs;
		
		for ( ; tokens > 0; tokens-- ) {
			for ( final Unit unit : UNITS ) {
				if ( deltaMs >= unit.msLimit )
					continue;
				
				final long value = deltaMs / unit.ms;
				sb.append( value );
				if ( longForm ) {
					sb.append( unit.name ); // Starts with a space
					if ( unit.plural && value != 1 )
						sb.append( 's' );
				} else
					sb.append( unit.letter );
				
				if ( unit == Unit.SEC ) // If unit already is the smallest, do not append more of them (all would be 0).
					tokens = 0;
				
				if ( tokens > 1 )
					deltaMs %= unit.ms;
				
				break; // Break units, go on to the next token
			}
			
			if ( tokens > 1 )
				if ( longForm )
					sb.append( tokens == 2 ? " and " : ", " );
				else
					sb.append( ' ' );
		}
		
		if ( appendEra )
			sb.append( future ? " from now" : " ago" );
		
		return sb.toString();
	}
	
	@Override
	public int compareTo( final IRelativeDate rd ) {
		if ( future )
			return rd.isFuture() ? Long.compare( deltaMs, rd.getDeltaMs() ) : 1;
		else
			return rd.isFuture() ? -1 : Long.compare( rd.getDeltaMs(), deltaMs );
	}
	
}
