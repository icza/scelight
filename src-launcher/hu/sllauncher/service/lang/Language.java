/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllauncher.service.lang;

import hu.scelightapibase.bean.person.IPersonNameBean;
import hu.scelightapibase.bean.settings.ISettingChangeEvent;
import hu.scelightapibase.bean.settings.ISettingChangeListener;
import hu.scelightapibase.service.lang.ILanguage;
import hu.sllauncher.bean.settings.type.Setting;
import hu.sllauncher.service.env.LEnv;
import hu.sllauncher.service.settings.LSettings;
import hu.sllauncher.util.LUtils;
import hu.sllauncher.util.RelativeDate;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Language and locale specific utilities.
 * 
 * @author Andras Belicza
 */
public class Language implements ILanguage {
	
	/** Date format. */
	private DateFormat                   dateFormat;
	
	/** Time format. */
	private DateFormat                   timeFormat;
	
	/** Date+time format. */
	private DateFormat                   dateTimeFormat;
	
	/** Date+time format which includes milliseconds. */
	private DateFormat                   dateTimeFormatMs;
	
	/** Decimal format used to format numbers. */
	private DecimalFormat                decimalFormat;
	
	/**
	 * Decimal formats used to format numbers with different fraction digits. The decimal format instance at index <code>i</code> will use a number of fraction
	 * digits <code>i</code>.
	 */
	private DecimalFormat[]              fractionDecimalFormats;
	
	/** Person name format. */
	private PersonNameFormat             personNameFormat;
	
	/** Setting change listener to listen to locale setting changes. */
	private final ISettingChangeListener scl;
	
	/**
	 * Creates a new {@link Language}.
	 */
	public Language() {
		scl = new ISettingChangeListener() {
			@Override
			public void valuesChanged( final ISettingChangeEvent event ) {
				if ( event.affected( LSettings.DATE_FORMAT ) ) {
					final DateFormatE dateFormatE = event.get( LSettings.DATE_FORMAT );
					dateFormat = new SimpleDateFormat( dateFormatE.pattern );
					timeFormat = new SimpleDateFormat( "HH:mm:ss" );
					dateTimeFormat = new SimpleDateFormat( dateFormatE.pattern + " HH:mm:ss" );
					dateTimeFormatMs = new SimpleDateFormat( dateFormatE.pattern + " HH:mm:ss.SSS" );
				}
				
				if ( event.affected( LSettings.NUMBER_FORMAT ) ) {
					decimalFormat = event.get( LSettings.NUMBER_FORMAT ).getDecimalFormat();
					fractionDecimalFormats = new DecimalFormat[ MAX_FRACTION_DIGITS + 1 ]; // +1 for zero digits
					for ( int i = 0; i < fractionDecimalFormats.length; i++ ) {
						fractionDecimalFormats[ i ] = (DecimalFormat) decimalFormat.clone();
						fractionDecimalFormats[ i ].setMinimumFractionDigits( i );
						fractionDecimalFormats[ i ].setMaximumFractionDigits( i );
					}
				}
				
				if ( event.affected( LSettings.PERSON_NAME_FORMAT ) )
					personNameFormat = event.get( LSettings.PERSON_NAME_FORMAT );
			}
		};
		
		LEnv.LAUNCHER_SETTINGS.addAndExecuteChangeListener(
		        LUtils.< Setting< ? > > asNewSet( LSettings.DATE_FORMAT, LSettings.NUMBER_FORMAT, LSettings.PERSON_NAME_FORMAT ), scl );
	}
	
	@Override
	public String formatDate( final Date date ) {
		synchronized ( dateFormat ) {
			try {
				return dateFormat.format( date );
			} catch ( final Exception e ) {
				return null;
			}
		}
	}
	
	@Override
	public String formatTime( final Date time ) {
		synchronized ( timeFormat ) {
			try {
				return timeFormat.format( time );
			} catch ( final Exception e ) {
				return null;
			}
		}
	}
	
	@Override
	public String formatDateTime( final Date dateTime, final boolean ms ) {
		return ms ? formatDateTimeMs( dateTime ) : formatDateTime( dateTime );
	}
	
	@Override
	public String formatDateTime( final Date dateTime ) {
		synchronized ( dateTimeFormat ) {
			try {
				return dateTimeFormat.format( dateTime );
			} catch ( final Exception e ) {
				return null;
			}
		}
	}
	
	@Override
	public String formatDateTimeMs( final Date dateTime ) {
		synchronized ( dateTimeFormatMs ) {
			try {
				return dateTimeFormatMs.format( dateTime );
			} catch ( final Exception e ) {
				return null;
			}
		}
	}
	
	@Override
	public String formatDateTimeWithRelative( final Date dateTime ) {
		return formatDateTimeWithRelative( dateTime, true );
	}
	
	@Override
	public String formatDateTimeWithRelative( final Date dateTime, final boolean longForm ) {
		return "<html>" + formatDateTime( dateTime ) + " <i><font color=#a07000>(" + new RelativeDate( dateTime, longForm, 2, true ) + ")</font></i></html>";
	}
	
	@Override
	public String formatNumber( final long n ) {
		synchronized ( decimalFormat ) {
			return decimalFormat.format( n );
		}
	}
	
	@Override
	public String formatNumber( final double n, int fractionDigits ) {
		if ( fractionDigits < 0 )
			throw new IllegalArgumentException( "Fraction digits cannot be negative!" );
		if ( fractionDigits > MAX_FRACTION_DIGITS )
			throw new IllegalArgumentException( "Fraction digits cannot be greater than " + MAX_FRACTION_DIGITS + " (provided: " + fractionDigits + ")!" );
		
		synchronized ( fractionDecimalFormats[ fractionDigits ] ) {
			return fractionDecimalFormats[ fractionDigits ].format( n );
		}
	}
	
	@Override
	public String formatPersonName( final IPersonNameBean personName ) {
		final StringBuilder b = new StringBuilder();
		
		final String[] parts = new String[ 4 ];
		parts[ personNameFormat.firstIdx ] = personName.getFirst();
		parts[ personNameFormat.middleIdx ] = personName.getMiddle();
		parts[ personNameFormat.nickIdx ] = personName.getNick();
		parts[ personNameFormat.lastIdx ] = personName.getLast();
		
		for ( int i = 0; i < parts.length; i++ )
			if ( parts[ i ] != null ) {
				if ( b.length() > 0 )
					b.append( ' ' );
				
				if ( i == personNameFormat.nickIdx ) // Put nickname in quotes
					b.append( '"' );
				
				b.append( parts[ i ] );
				
				if ( i == personNameFormat.nickIdx ) // Put nickname in quotes
					b.append( '"' );
			}
		
		return b.toString();
	}
	
}
