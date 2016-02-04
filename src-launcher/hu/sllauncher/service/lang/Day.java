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

/**
 * Day of the week.
 * 
 * @author Andras Belicza
 */
public enum Day {
	
	/** Monday. */
	MONDAY( "Monday" ),
	
	/** Tuesday. */
	TUESDAY( "Tuesday" ),
	
	/** Wednesday. */
	WEDNESDAY( "Wednesday" ),
	
	/** Thursday. */
	THURSDAY( "Thursday" ),
	
	/** Friday. */
	FRIDAY( "Friday" ),
	
	/** Saturday. */
	SATURDAY( "Saturday" ),
	
	/** Sunday. */
	SUNDAY( "Sunday" );
	
	
	/** String value of the day. */
	public final String text;
	
	
	/**
	 * Creates a new {@link Day}.
	 * 
	 * @param text string value of the month
	 */
	private Day( final String text ) {
		this.text = text;
	}
	
	@Override
	public String toString() {
		return text;
	}
	
	
	/** Cache of the values array. */
	public static final Day[] VALUES = values();
	
}
