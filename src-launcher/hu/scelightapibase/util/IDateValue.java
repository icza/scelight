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

import java.util.Date;

/**
 * A wrapper interface holding a date and format hint for rendering (to a text).
 * 
 * <p>
 * Instances are "table-friendly": you can use instances in tables and they can be sorted properly.
 * </p>
 * 
 * @since 1.1
 * 
 * @author Andras Belicza
 * 
 * @see hu.scelightapi.service.IFactory#newDateValue(Date)
 * @see hu.scelightapi.service.IFactory#newDateValue(Date, IDateFormat)
 * @see IDateFormat
 */
public interface IDateValue extends Comparable< IDateValue > {
	
	/**
	 * Returns the date value.
	 * 
	 * @return the date value
	 */
	Date getValue();
	
	/**
	 * Returns the date format hint for renderers.
	 * 
	 * @return the date format hint for renderers
	 */
	IDateFormat getDateFormat();
	
}
