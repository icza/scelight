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

import hu.scelight.service.sc2reg.GameStatus;
import hu.scelightapibase.util.IEnum;
import hu.scelightapibase.util.gui.HasRIcon;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Possible SC2 game status.
 * 
 * @author Andras Belicza
 * 
 * @see ISc2Monitor#getGameStatus()
 * @see IGameChangeEvent
 * @see IEnum
 */
public interface IGameStatus extends HasRIcon, IEnum {
	
	/** No game in progress. */
	IGameStatus         ENDED      = GameStatus.ENDED;
	
	/** There is a game in progress. */
	IGameStatus         STARTED    = GameStatus.STARTED;
	
	/** Status unknown. */
	IGameStatus         UNKNOWN    = GameStatus.UNKNOWN;
	
	
	/** An unmodifiable list of all the game statuses. */
	List< IGameStatus > VALUE_LIST = Collections.unmodifiableList( Arrays.< IGameStatus > asList( GameStatus.VALUES ) );
	
}
