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
 * StarCraft II monitor interface.
 * 
 * <p>
 * One should always first test if the SC2 monitor is supported on the current platform with the {@link #isSupported()} method, and only then can other methods
 * be called.<br>
 * Calling methods other than {@link #isSupported()} if the SC2 monitor is not supported results in undefined behavior (might be no effect, <code>null</code>
 * returned value or even an Exception being thrown).
 * </p>
 * 
 * @author Andras Belicza
 * 
 * @see IGameChangeListener
 */
public interface ISc2Monitor {
	
	/**
	 * Tells if the SC2 monitor service is supported on the current platform.
	 * 
	 * @return true if the SC2 monitor service is supported on the current platform; false otherwise
	 */
	boolean isSupported();
	
	/**
	 * Returns the current game status.
	 * 
	 * @return the current game status
	 */
	IGameStatus getGameStatus();
	
	/**
	 * Returns the time when the current game status was detected.<br>
	 * The returned value is the number of milliseconds since January 1, 1970, 00:00:00 GMT.<br>
	 * <code>0L</code> is returned if it is unknown when current game status took effect (this is the case when the application starts).
	 * 
	 * @return the time when the current game status was detected
	 */
	long getGameStatusSince();
	
	/**
	 * Adds an {@link IGameChangeListener} which will be called when the game (status) changes.
	 * 
	 * @param listener listener to be added
	 * 
	 * @see #removeGameChangeListener(IGameChangeListener)
	 */
	void addGameChangeListener( IGameChangeListener listener );
	
	/**
	 * Removes an {@link IGameChangeListener}.
	 * 
	 * @param listener listener to be removed
	 * 
	 * @see #addGameChangeListener(IGameChangeListener)
	 */
	void removeGameChangeListener( IGameChangeListener listener );
	
	/**
	 * Returns the current, live APM.
	 * 
	 * @return the current APM or <code>null</code> if some error occurred
	 */
	Integer getApm();
	
}
