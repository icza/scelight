/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapi.sc2.rep.model.gameevents.cmd;

import hu.scelight.sc2.rep.model.gameevents.cmd.TagTransformation;
import hu.scelightapibase.util.IEnum;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Unit tag display transformation strategy.
 * 
 * @author Andras Belicza
 * 
 * @see IEnum
 */
public interface ITagTransformation extends IEnum {
	
	/** Original tag in decimal radix. */
	ITagTransformation         DECIMAL    = TagTransformation.DECIMAL;
	
	/** Original tag in hexadecimal radix. */
	ITagTransformation         HEXA       = TagTransformation.HEXA;
	
	/** Shuffled tag in radix 36. */
	ITagTransformation         SHUFFLED   = TagTransformation.SHUFFLED;
	
	
	/** An unmodifiable list of all the tag transformations. */
	List< ITagTransformation > VALUE_LIST = Collections.unmodifiableList( Arrays.< ITagTransformation > asList( TagTransformation.VALUES ) );
	
	
	/**
	 * Converts the specified unit tag to a displayable string.
	 * 
	 * @param tag unit tag to be transformed
	 * @return the specified unit tag converted to a displayable string
	 */
	String tagToString( int tag );
	
}
