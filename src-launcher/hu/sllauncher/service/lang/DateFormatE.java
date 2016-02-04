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

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Predefined date format.
 * 
 * @author Andras Belicza
 */
public enum DateFormatE {
	
	/** <code>yyyy-MM-dd</code> */
	YYYY_MM_DD( "yyyy-MM-dd" ),
	
	/** <code>yyyy/MM/dd</code> */
	YYYY_MM_DD2( "yyyy/MM/dd" ),
	
	/** <code>yyyy.MM.dd</code> */
	YYYY_MM_DD3( "yyyy.MM.dd" ),
	
	/** <code>MM-dd-yyyy</code> */
	MM_DD_YYYY( "MM-dd-yyyy" ),
	
	/** <code>MM/dd/yyyy</code> */
	MM_DD_YYYY2( "MM/dd/yyyy" ),
	
	/** <code>MM.dd.yyyy</code> */
	MM_DD_YYYY3( "MM.dd.yyyy" ),
	
	/** <code>MM-dd-yyyy</code> */
	DD_MM_YYYY( "dd-MM-yyyy" ),
	
	/** <code>dd/MM/yyyy</code> */
	DD_MM_YYYY2( "dd/MM/yyyy" ),
	
	/** <code>dd.MM.yyyy</code> */
	DD_MM_YYYY3( "dd.MM.yyyy" ),
	
	/** <code>yy-MM-dd</code> */
	YY_MM_DD( "yy-MM-dd" ),
	
	/** <code>yy/MM/dd</code> */
	YY_MM_DD2( "yy/MM/dd" ),
	
	/** <code>yy.MM.dd</code> */
	YY_MM_DD3( "yy.MM.dd" ),
	
	/** <code>MM-dd-yy</code> */
	MM_DD_YY( "MM-dd-yy" ),
	
	/** <code>MM/dd/yy</code> */
	MM_DD_YY2( "MM/dd/yy" ),
	
	/** <code>MM.dd.yy</code> */
	MM_DD_YY3( "MM.dd.yy" ),
	
	/** <code>dd-MM-yy</code> */
	DD_MM_YY( "dd-MM-yy" ),
	
	/** <code>dd/MM/yy</code> */
	DD_MM_YY2( "dd/MM/yy" ),
	
	/** <code>dd.MM.yy</code> */
	DD_MM_YY3( "dd.MM.yy" );
	
	
	/** Pattern string of the date format. */
	public final String pattern;
	
	/** String value of the date format. */
	public final String stringValue;
	
	
	/**
	 * Creates a new {@link DateFormatE}.
	 * 
	 * @param pattern date format pattern
	 */
	private DateFormatE( final String pattern ) {
		this.pattern = pattern;
		stringValue = "<html><span style='" + LRHtml.STYLE_HIGHLIGHTED + "'>" + new SimpleDateFormat( pattern ).format( new Date() ) + "</span>&nbsp;&nbsp;"
		        + pattern.replace( "yyyy", "year" ).replace( "yy", "year" ).replace( "MM", "month" ).replace( "dd", "day" ) + "</html>";
	}
	
	@Override
	public String toString() {
		return stringValue;
	}
	
}
