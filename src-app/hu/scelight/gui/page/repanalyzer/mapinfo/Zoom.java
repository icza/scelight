/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.gui.page.repanalyzer.mapinfo;

/**
 * Zoom (factor).
 * 
 * @author Andras Belicza
 */
public enum Zoom {
	
	/** Fit to window zoom. */
	FIT_TO_WINDOW( "Fit to Window" ),
	
	/** 1/2x zoom. */
	HALF( "x1/2" ),
	
	/** 1x zoom. */
	X1( 1 ),
	
	/** 2x zoom. */
	X2( 2 ),
	
	/** 3x zoom. */
	X3( 3 ),
	
	/** 4x zoom. */
	X4( 4 ),
	
	/** 5x zoom. */
	X5( 5 ),
	
	/** 6x zoom. */
	X6( 6 ),
	
	/** 7x zoom. */
	X7( 7 ),
	
	/** 8x zoom. */
	X8( 8 ),
	
	/** 16x zoom. */
	X16( 16 ),
	
	/** 32x zoom. */
	X32( 32 );
	
	
	/** Text value of the zoom. */
	public final String text;
	
	/** Zoom factor. */
	public final int    factor;
	
	/**
	 * Creates a new {@link Zoom}.
	 * 
	 * @param text text value
	 */
	private Zoom( final String text ) {
		this( text, 0 );
	}
	
	/**
	 * Creates a new {@link Zoom}.
	 * 
	 * @param factor zoom factor
	 */
	private Zoom( final int factor ) {
		this( "x" + factor, factor );
	}
	
	/**
	 * Creates a new {@link Zoom}.
	 * 
	 * @param text text value
	 * @param factor zoom factor
	 */
	private Zoom( final String text, final int factor ) {
		this.text = text;
		this.factor = factor;
	}
	
	@Override
	public String toString() {
		return text;
	}
	
	
	
	/** Cache of the values array. */
	public static final Zoom[] VALUES = values();
	
}
