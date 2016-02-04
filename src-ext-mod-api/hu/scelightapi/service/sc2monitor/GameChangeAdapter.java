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
 * Adapter class for the {@link IGameChangeListener} so you only have to override the methods you're interested in.
 * 
 * @author Andras Belicza
 * 
 * @see IGameChangeEvent
 */
public class GameChangeAdapter implements IGameChangeListener {
	
	@Override
	public void gameChanged( final IGameChangeEvent event ) {
		// Empty implementation, this is an adapter class.
	}
	
	@Override
	public void gameStarted( final IGameChangeEvent event ) {
		// Empty implementation, this is an adapter class.
	}
	
	@Override
	public void gameEnded( final IGameChangeEvent event ) {
		// Empty implementation, this is an adapter class.
	}
	
}
