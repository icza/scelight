/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllauncher.gui.comp.logo;

/**
 * Logo state.
 * 
 * @author Andras Belicza
 */
enum State {
	
	/** Text in front, animation in background. */
	TEXT_FRONT,
	
	/** Animation in front, text in background. */
	ANIM_FRONT,
	
	/** Animation only. */
	ANIM_ONLY,
	
	/** Text only. */
	TEXT_ONLY;
	
	
	/**
	 * Returns the next state.
	 * 
	 * @return the next state
	 */
	public State next() {
		return VALUES[ ( ordinal() + 1 ) % VALUES.length ];
	}
	
	
	/** Cache of the values array. */
	public static final State[] VALUES = values();
	
}
