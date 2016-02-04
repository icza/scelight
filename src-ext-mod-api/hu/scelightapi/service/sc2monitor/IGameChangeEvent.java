/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapi.service.sc2monitor;

/**
 * Event describing a game status change.
 * 
 * @author Andras Belicza
 * 
 * @see IGameChangeListener
 */
public interface IGameChangeEvent {
	
	/**
	 * Returns the old game status (before the change).
	 * 
	 * @return the old game status (before the change)
	 */
	IGameStatus getOldStatus();
	
	/**
	 * Returns the new game status (after the change).
	 * 
	 * @return the new game status (after the change)
	 */
	IGameStatus getNewStatus();
	
}
