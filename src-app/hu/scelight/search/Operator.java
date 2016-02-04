/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.search;

import hu.scelightapi.search.IOperator;

/**
 * Replay filter operator.
 * 
 * @author Andras Belicza
 */
public enum Operator implements IOperator {
	
	/** Equal to: = */
	EQUAL( "=", "<html>=&nbsp;&nbsp;<i><font color=#777777>(equal)</font></i></html>" ),
	
	/** Not equal to: &ne; */
	NOT_EQUAL( "&ne;", "<html>&ne;&nbsp;&nbsp;<i><font color=#777777>(not equal)</font></i></html>" ),
	
	/** Greater than: &gt; */
	GREATER_THAN( "&gt;", "<html>&gt;&nbsp;&nbsp;<i><font color=#777777>(greater than)</font></i></html>" ),
	
	/** Greater than or equal to: &ge; */
	GREATER_THAN_OR_EQUAL( "&ge;", "<html>&ge;&nbsp;&nbsp;<i><font color=#777777>(greater than or equal)</font></i></html>" ),
	
	/** Less than: &lt; */
	LESS_THAN( "&lt;", "<html>&lt;&nbsp;&nbsp;<i><font color=#777777>(less than)</font></i></html>" ),
	
	/** Less than or equal to: &le; */
	LESS_THAN_OR_EQUAL( "&le;", "<html>&le;&nbsp;&nbsp;<i><font color=#777777>(less than or equal)</font></i></html>" ),
	
	/** Contains. */
	CONTAINS( "contains", "contains" ),
	
	/** Not contains. */
	NOT_CONTAINS( "does not contain", "does not contain" ),
	
	/** Starts with. */
	STARTS_WITH( "starts with", "starts with" ),
	
	/** Not starts with. */
	NOT_STARTS_WITH( "does not start with", "does not start with" ),
	
	/** Ends with. */
	ENDS_WITH( "ends with", "ends with" ),
	
	/** Not ends with. */
	NOT_ENDS_WITH( "does not end with", "does not end with" ),
	
	/**
	 * MATCHES (regular expression).<br>
	 * For Regular expression syntax see: <a href="http://docs.oracle.com/javase/7/docs/api/java/util/regex/Pattern.html#sum">Regexp</a>.
	 */
	MATCHES( "MATCHES", "<html>MATCHES&nbsp;&nbsp;<i><font color=#777777>(regexp)</font></i></html>" );
	
	
	/** HTML text value of the operator without the leading and trailing <code>&lt;html&gt;</code> tags. */
	public final String htmlText;
	
	/** Formatted string value of the operator. */
	public final String stringValue;
	
	
	/**
	 * Creates a new {@link Operator}.
	 * 
	 * @param htmlText HTML text of the operator without the leading and trailing <code>&lt;html&gt;</code> tags
	 * @param stringValue formatted string value of the operator
	 */
	private Operator( final String htmlText, final String stringValue ) {
		this.htmlText = htmlText;
		this.stringValue = stringValue;
	}
	
	@Override
	public String getHtmlText() {
		return htmlText;
	}
	
	@Override
	public String toString() {
		return stringValue;
	}
	
	
	/** Cache of the values array. */
	public static final Operator[] VALUES            = values();
	
	/** Collection of operators for string types. */
	public static final Operator[] TEXT_OPERATORS    = { CONTAINS, NOT_CONTAINS, STARTS_WITH, NOT_STARTS_WITH, ENDS_WITH, NOT_ENDS_WITH, EQUAL, NOT_EQUAL,
	        LESS_THAN, LESS_THAN_OR_EQUAL, GREATER_THAN, GREATER_THAN_OR_EQUAL, MATCHES };
	
	/** Collection of operators for number types. */
	public static final Operator[] NUMBER_OPERATORS  = { EQUAL, GREATER_THAN, GREATER_THAN_OR_EQUAL, LESS_THAN, LESS_THAN_OR_EQUAL, NOT_EQUAL };
	
	/** Collection of operators for enumerated types. */
	public static final Operator[] ENUM_OPERATORS    = NUMBER_OPERATORS;
	
	/** Collection of operators for boolean types. */
	public static final Operator[] BOOL_OPERATORS    = { EQUAL, NOT_EQUAL };
	
	/** Collection of operators for version types. */
	public static final Operator[] VERSION_OPERATORS = NUMBER_OPERATORS;
	
}
