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

import hu.scelight.gui.icon.Icons;
import hu.scelightapi.service.sc2monitor.IGameStatus;
import hu.sllauncher.gui.icon.LRIcon;

/**
 * Possible SC2 game status.
 * 
 * @author Andras Belicza
 */
public enum GameStatus implements IGameStatus {
	
	/** No game in progress. */
	ENDED( "Game Ended", Icons.F_STATUS_BUSY, 0 ),
	
	/** There is a game in progress. */
	STARTED( "Game Started", Icons.F_STATUS, 1 ),
	
	/** Status unknown. */
	UNKNOWN( "Game Status Unknown", Icons.F_STATUS_OFFLINE, -1 );
	
	
	/** Text value of the result. */
	public final String text;
	
	/** Ricon of the game status. */
	public final LRIcon ricon;
	
	/** (Windows) registry value associated with this game status. */
	public final int    registryValue;
	
	
	/**
	 * Creates a new {@link GameStatus}.
	 * 
	 * @param text text value
	 * @param ricon ricon of the game status
	 * @param registryValue registry value associated with this game status
	 */
	private GameStatus( final String text, final LRIcon ricon, final int registryValue ) {
		this.text = text;
		this.ricon = ricon;
		this.registryValue = registryValue;
	}
	
	@Override
	public LRIcon getRicon() {
		return ricon;
	}
	
	@Override
	public String toString() {
		return text;
	}
	
	
	/**
	 * Returns the game status associated with the specified registry value.
	 * 
	 * @param registryValue registry value to return the game status for
	 * @return the game status associated with the specified registry value; or {@link #UNKNOWN} if no game status is found for the specified registry value
	 */
	public static GameStatus fromRegistryValue( final int registryValue ) {
		for ( final GameStatus s : VALUES )
			if ( s.registryValue == registryValue )
				return s;
		
		return UNKNOWN;
	}
	
	/** Cache of the values array. */
	public static final GameStatus[] VALUES = values();
	
}
