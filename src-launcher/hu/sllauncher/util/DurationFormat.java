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
import hu.sllauncher.service.env.LEnv;

/**
 * Representation formats of a time duration value.
 * 
 * @author Andras Belicza
 */
public enum DurationFormat implements IDurationFormat {
	
	/** Auto, depends on duration value. */
	AUTO( "Auto" ),
	
	/** Millisecond. */
	MS( "ms" ),
	
	/** Second. */
	SEC( "sec" ),
	
	/** Second and millisecond. */
	SEC_MS( "sec.ms" ),
	
	/** Minute and second. */
	MIN_SEC( "min:sec" ),
	
	/** Hour and minute and second. */
	HOUR_MIN_SEC( "hour:min:sec" );
	
	
	/** Text value of the time duration format. */
	public final String text;
	
	
	/**
	 * Creates a new {@link DurationFormat}.
	 * 
	 * @param text text value of the duration format
	 */
	private DurationFormat( final String text ) {
		this.text = text;
	}
	
	@Override
	public String toString() {
		return text;
	}
	
	@Override
	public String formatDuration( long ms ) {
		switch ( this ) {
			case AUTO :
				if ( ms < LUtils.MS_IN_HOUR )
					return MIN_SEC.formatDuration( ms );
				else
					return HOUR_MIN_SEC.formatDuration( ms );
				
			default : // default is only a syntactical requirement, we've covered all cases...
			case MS :
				return LEnv.LANG.formatNumber( ms ) + " ms";
			case SEC :
				return LEnv.LANG.formatNumber( ms / 1000 ) + " sec";
			case SEC_MS :
				return LEnv.LANG.formatNumber( ms / 1000.0, 3 ) + " ms";
			case MIN_SEC : {
				final StringBuilder b = new StringBuilder( 8 );
				
				// Min
				if ( ms < 10 * LUtils.MS_IN_MIN )
					b.append( '0' );
				b.append( ms / LUtils.MS_IN_MIN );
				// Sec
				b.append( ':' );
				ms %= LUtils.MS_IN_MIN;
				if ( ms < 10_000 )
					b.append( '0' );
				b.append( ms / 1000 );
				
				return b.toString();
			}
			case HOUR_MIN_SEC : {
				final StringBuilder b = new StringBuilder( 8 );
				
				// Hour
				if ( ms < 10 * LUtils.MS_IN_HOUR )
					b.append( '0' );
				b.append( ms / LUtils.MS_IN_HOUR );
				// Min
				b.append( ':' );
				ms %= LUtils.MS_IN_HOUR;
				if ( ms < 10 * LUtils.MS_IN_MIN )
					b.append( '0' );
				b.append( ms / LUtils.MS_IN_MIN );
				// Sec
				b.append( ':' );
				ms %= LUtils.MS_IN_MIN;
				if ( ms < 10_000 )
					b.append( '0' );
				b.append( ms / 1000 );
				
				return b.toString();
			}
		}
	}
	
	
	/** Cache of the values array. */
	public static final DurationFormat[] VALUES = values();
	
}
