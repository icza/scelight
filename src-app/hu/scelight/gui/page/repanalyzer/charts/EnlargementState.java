/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.gui.page.repanalyzer.charts;

/**
 * Charts enlargement state.
 * 
 * @author Andras Belicza
 */
enum EnlargementState {
	
	/** Normal, not enlarged. */
	NORMAL,
	
	/** Enlarged, inside the tab. */
	ENLARGED,
	
	/** Enlarged, main navigation bar hidden. */
	ENLARGED_FULL;
	
	
	/**
	 * Returns the next state.
	 * 
	 * @return the next state
	 */
	public EnlargementState next() {
		return VALUES[ ( ordinal() + 1 ) % VALUES.length ];
	}
	
	
	/** Cache of the values array. */
	public static final EnlargementState[] VALUES = values();
	
}
