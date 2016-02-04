/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllauncher.service.lang;

import hu.sllauncher.util.LRHtml;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 * Predefined number format.
 * 
 * @author Andras Belicza
 */
public enum NumberFormatE {
	
	/** <code>1,234,567.89</code> */
	D_DDD__DD( 3, ',', '.' ),
	
	/** <code>1 234 567,89</code> */
	D_DDD__DD2( 3, ' ', ',' ),
	
	/** <code>1_234_567.89</code> */
	D_DDD__DD3( 3, '_', '.' ),
	
	/** <code>1_234_567,89</code> */
	D_DDD__DD4( 3, '_', ',' ),
	
	/** <code>1 234 567.89</code> */
	D_DDD__DD5( 3, ' ', '.' ),
	
	/** <code>1234567.89</code> */
	DDDD__DD( 0, '_', '.' ),
	
	/** <code>1234567,89</code> */
	DDDD__DD2( 0, '_', ',' );
	
	
	/** Grouping size. A value of <code>0</code> indicates no grouping. */
	public final int    groupingSize;
	
	/** Grouping separator. */
	public final char   groupingSeparator;
	
	/** Decimal separator. */
	public final char   decimalSeparator;
	
	/** String value of the number format. */
	public final String stringValue;
	
	
	/**
	 * Creates a new {@link NumberFormatE}.
	 * 
	 * @param groupingSize grouping size
	 * @param groupingSeparator grouping separator
	 * @param decimalSeparator decimal separator
	 */
	private NumberFormatE( final int groupingSize, final char groupingSeparator, final char decimalSeparator ) {
		this.groupingSize = groupingSize;
		this.groupingSeparator = groupingSeparator;
		this.decimalSeparator = decimalSeparator;
		
		final DecimalFormat decimalFormat = getDecimalFormat();
		decimalFormat.setMinimumFractionDigits( 2 );
		decimalFormat.setMaximumFractionDigits( 2 );
		
		stringValue = "<html><code><span style='" + LRHtml.STYLE_HIGHLIGHTED + "'>" + decimalFormat.format( 1_234_567.89 )
		        + "</span> = 123456789 / 100</code></html>";
	}
	
	/**
	 * Returns a new {@link DecimalFormat} configured to follow the rules represented by this number format.
	 * 
	 * @return a new {@link DecimalFormat} configured to follow the rules represented by this number format
	 */
	public DecimalFormat getDecimalFormat() {
		return configureDecimalFormat( new DecimalFormat() );
	}
	
	/**
	 * Configures the specified {@link DecimalFormat} to follow the rules represented by this number format.
	 * 
	 * @param decimalFormat decimal format to be configured
	 * @return the specified {@link DecimalFormat}
	 */
	public DecimalFormat configureDecimalFormat( final DecimalFormat decimalFormat ) {
		if ( groupingSize == 0 )
			decimalFormat.setGroupingUsed( false );
		else
			decimalFormat.setGroupingSize( groupingSize );
		
		final DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setGroupingSeparator( groupingSeparator );
		dfs.setDecimalSeparator( decimalSeparator );
		decimalFormat.setDecimalFormatSymbols( dfs );
		
		return decimalFormat;
	}
	
	@Override
	public String toString() {
		return stringValue;
	}
	
}
