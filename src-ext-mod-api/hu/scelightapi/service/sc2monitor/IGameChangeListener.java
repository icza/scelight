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
 * Game (status) change listener.
 * 
 * @author Andras Belicza
 * 
 * @see ISc2Monitor#addGameChangeListener(IGameChangeListener)
 * @see ISc2Monitor#removeGameChangeListener(IGameChangeListener)
 * @see GameChangeAdapter
 * @see IGameChangeEvent
 */
public interface IGameChangeListener {
	
	/**
	 * This method is called when game (status) changes, no matter what the status transition is.
	 * 
	 * @param event details of the game (status) change
	 */
	void gameChanged( IGameChangeEvent event );
	
	/**
	 * Called when a game starts.
	 * 
	 * <p>
	 * This method is called only if the game status changes from {@link IGameStatus#ENDED} to {@link IGameStatus#STARTED}.<br>
	 * A {@link #gameChanged(IGameChangeEvent)} call will precede this call.
	 * </p>
	 * 
	 * @param event event describing the game (status) change
	 * 
	 * @see #gameChanged(IGameChangeEvent)
	 */
	void gameStarted( IGameChangeEvent event );
	
	/**
	 * Called when a game ends.
	 * 
	 * <p>
	 * This method is called only if the game status changes from {@link IGameStatus#STARTED} to {@link IGameStatus#ENDED}.<br>
	 * A {@link #gameChanged(IGameChangeEvent)} call will precede this call.
	 * </p>
	 * 
	 * @param event event describing the game (status) change
	 * 
	 * @see #gameChanged(IGameChangeEvent)
	 */
	void gameEnded( IGameChangeEvent event );
	
}
