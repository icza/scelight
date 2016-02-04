/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.sc2.rep.model.initdata.gamedesc;

import hu.scelight.sc2.rep.model.attributesevents.AttributesEvents;
import hu.scelightapi.sc2.rep.model.initdata.gamedesc.IGameSpeed;

/**
 * Game speed.
 * 
 * @author Andras Belicza
 */
public enum GameSpeed implements IGameSpeed {
	
	/** Slower. */
	SLOWER( "Slower", "Slor", 60 ),
	
	/** Slow. */
	SLOW( "Slow", "Slow", 45 ),
	
	/** Normal. */
	NORMAL( "Normal", "Norm", 36 ),
	
	/** Fast. */
	FAST( "Fast", "Fast", 30 ),
	
	/** Faster. */
	FASTER( "Faster", "Fasr", 26 );
	
	
	/** Text value of the game speed. */
	public final String text;
	
	/** Game speed value used for {@link AttributesEvents#A_GAME_SPEED}. */
	public final String gameSpeed;
	
	/** The relative time speed value (relative to other game speeds). */
	public final long   relativeSpeed;
	
	
	/**
	 * Creates a new {@link GameSpeed}.
	 * 
	 * @param text text value
	 * @param gameSpeed game speed value used for {@link AttributesEvents#A_GAME_SPEED}
	 * @param relativeSpeed the relative time speed value
	 */
	private GameSpeed( final String text, final String gameSpeed, final long relativeSpeed ) {
		this.text = text;
		this.gameSpeed = gameSpeed;
		this.relativeSpeed = relativeSpeed;
	}
	
	@Override
	public long getRelativeSpeed() {
		return relativeSpeed;
	}
	
	@Override
	public long convertToRealTime( final long gameTimeValue ) {
		return this == NORMAL ? gameTimeValue : gameTimeValue * relativeSpeed / NORMAL.relativeSpeed;
	}
	
	@Override
	public long convertToGameTime( final long realTimeValue ) {
		return this == NORMAL ? realTimeValue : realTimeValue * NORMAL.relativeSpeed / relativeSpeed;
	}
	
	@Override
	public String toString() {
		return text;
	}
	
	
	/** Cache of the values array. */
	public static final GameSpeed[] VALUES = values();
	
	
}
