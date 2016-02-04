/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.service.sc2reg;

import hu.scelightapi.service.sc2monitor.IGameChangeEvent;

/**
 * Event describing a game status change.
 * 
 * @author Andras Belicza
 */
public class GameChangeEvent implements IGameChangeEvent {
	
	/** Old game status (before the change). */
	public final GameStatus oldStatus;
	
	/** New game status (after the change). */
	public final GameStatus newStatus;
	
	
	/**
	 * Creates a new {@link GameChangeEvent}.
	 * 
	 * @param oldStatus old game status (before the change)
	 * @param newStatus new game status (after the change)
	 */
	public GameChangeEvent( final GameStatus oldStatus, final GameStatus newStatus ) {
		this.oldStatus = oldStatus;
		this.newStatus = newStatus;
	}
	
	@Override
	public GameStatus getOldStatus() {
		return oldStatus;
	}
	
	@Override
	public GameStatus getNewStatus() {
		return newStatus;
	}
	
}
