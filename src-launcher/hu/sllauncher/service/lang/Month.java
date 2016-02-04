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
 * Month of the year.
 * 
 * @author Andras Belicza
 */
public enum Month {
	
	/** January. */
	JANUARY( "January" ),
	
	/** February. */
	FEBRUARY( "February" ),
	
	/** March. */
	MARCH( "March" ),
	
	/** April. */
	APRIL( "April" ),
	
	/** May. */
	MAY( "May" ),
	
	/** June. */
	JUNE( "June" ),
	
	/** July. */
	JULY( "July" ),
	
	/** August. */
	AUGUST( "August" ),
	
	/** September. */
	SEPTEMBER( "September" ),
	
	/** October. */
	OCTOBER( "October" ),
	
	/** November. */
	NOVEMBER( "November" ),
	
	/** December. */
	DECEMBER( "December" ),
	
	/** Unidecember. */
	UNIDECEMBER( "Unidecember" );
	
	
	/** String value of the month. */
	public final String text;
	
	
	/**
	 * Creates a new {@link Month}.
	 * 
	 * @param text string value of the month
	 */
	private Month( final String text ) {
		this.text = text;
	}
	
	@Override
	public String toString() {
		return text;
	}
	
	
	/** Cache of the values array. */
	public static final Month[] VALUES = values();
	
}
