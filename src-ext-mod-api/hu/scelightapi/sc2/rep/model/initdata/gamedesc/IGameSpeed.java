/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapi.sc2.rep.model.initdata.gamedesc;

import hu.scelight.sc2.rep.model.initdata.gamedesc.GameSpeed;
import hu.scelightapibase.util.IEnum;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Game speed.
 * 
 * @author Andras Belicza
 * 
 * @see IEnum
 */
public interface IGameSpeed extends IEnum {
	
	/** Slower. */
	IGameSpeed         SLOWER     = GameSpeed.SLOWER;
	
	/** Slow. */
	IGameSpeed         SLOW       = GameSpeed.SLOW;
	
	/** Normal. */
	IGameSpeed         NORMAL     = GameSpeed.NORMAL;
	
	/** Fast. */
	IGameSpeed         FAST       = GameSpeed.FAST;
	
	/** Faster. */
	IGameSpeed         FASTER     = GameSpeed.FASTER;
	
	
	/** An unmodifiable list of all the game speeds. */
	List< IGameSpeed > VALUE_LIST = Collections.unmodifiableList( Arrays.< IGameSpeed > asList( GameSpeed.VALUES ) );
	
	
	/**
	 * Returns the relative time speed value (relative to other game speeds).
	 * @return the relative time speed value (relative to other game speeds)
	 */
	long getRelativeSpeed();
	
	/**
	 * Converts a game-time value to real-time value (value can be game loop, time).
	 * 
	 * @param gameTimeValue game-time value to be converted to real-time
	 * @return the game-time converted to real-time
	 */
	long convertToRealTime( long gameTimeValue );
	
	/**
	 * Converts a real-time value to game-time value (value can be game loop, time).
	 * 
	 * @param realTimeValue game-time value to be converted to real-time
	 * @return the real-time converted to game-time
	 */
	long convertToGameTime( long realTimeValue );
	
}
