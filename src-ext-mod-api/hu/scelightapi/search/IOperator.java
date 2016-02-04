/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapi.search;

import hu.scelight.search.Operator;
import hu.scelightapibase.util.IEnum;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Replay filter operator.
 * 
 * @author Andras Belicza
 * 
 * @see IEnum
 */
public interface IOperator extends IEnum {
	
	/** Equal to: = */
	IOperator         EQUAL                 = Operator.EQUAL;
	
	/** Not equal to: &ne; */
	IOperator         NOT_EQUAL             = Operator.NOT_EQUAL;
	
	/** Greater than: &gt; */
	IOperator         GREATER_THAN          = Operator.GREATER_THAN;
	
	/** Greater than or equal to: &ge; */
	IOperator         GREATER_THAN_OR_EQUAL = Operator.GREATER_THAN_OR_EQUAL;
	
	/** Less than: &lt; */
	IOperator         LESS_THAN             = Operator.LESS_THAN;
	
	/** Less than or equal to: &le; */
	IOperator         LESS_THAN_OR_EQUAL    = Operator.LESS_THAN_OR_EQUAL;
	
	/** Contains. */
	IOperator         CONTAINS              = Operator.CONTAINS;
	
	/** Not contains. */
	IOperator         NOT_CONTAINS          = Operator.NOT_CONTAINS;
	
	/** Starts with. */
	IOperator         STARTS_WITH           = Operator.STARTS_WITH;
	
	/** Not starts with. */
	IOperator         NOT_STARTS_WITH       = Operator.NOT_STARTS_WITH;
	
	/** Ends with. */
	IOperator         ENDS_WITH             = Operator.ENDS_WITH;
	
	/** Not ends with. */
	IOperator         NOT_ENDS_WITH         = Operator.NOT_ENDS_WITH;
	
	/**
	 * MATCHES (regular expression).<br>
	 * For Regular expression syntax see: <a href="http://docs.oracle.com/javase/7/docs/api/java/util/regex/Pattern.html#sum">Regexp</a>.
	 */
	IOperator         MATCHES               = Operator.MATCHES;
	
	
	/** An unmodifiable list of all the operators. */
	List< IOperator > VALUE_LIST            = Collections.unmodifiableList( Arrays.< IOperator > asList( Operator.VALUES ) );
	
	/** An unmodifiable list of operators for string types. */
	List< IOperator > TEXT_OPERATORS        = Collections.unmodifiableList( Arrays.< IOperator > asList( Operator.NUMBER_OPERATORS ) );
	
	/** An unmodifiable list of operators for number types. */
	List< IOperator > NUMBER_OPERATORS      = Collections.unmodifiableList( Arrays.< IOperator > asList( Operator.NUMBER_OPERATORS ) );
	
	/** An unmodifiable list of operators for enumerated types. */
	List< IOperator > ENUM_OPERATORS        = Collections.unmodifiableList( Arrays.< IOperator > asList( Operator.ENUM_OPERATORS ) );
	
	/** An unmodifiable list of operators for boolean types. */
	List< IOperator > BOOL_OPERATORS        = Collections.unmodifiableList( Arrays.< IOperator > asList( Operator.BOOL_OPERATORS ) );
	
	/** An unmodifiable list of operators for version types. */
	List< IOperator > VERSION_OPERATORS     = Collections.unmodifiableList( Arrays.< IOperator > asList( Operator.VERSION_OPERATORS ) );
	
	
	/**
	 * Returns the HTML text value of the operator without the leading and trailing <code>&lt;html&gt;</code> tags.
	 * 
	 * @return the HTML text value of the operator without the leading and trailing <code>&lt;html&gt;</code> tags
	 */
	String getHtmlText();
	
}
