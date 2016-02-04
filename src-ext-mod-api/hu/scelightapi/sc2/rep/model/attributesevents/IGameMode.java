/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapi.sc2.rep.model.attributesevents;

import hu.scelight.sc2.rep.model.attributesevents.GameMode;
import hu.scelightapibase.util.IEnum;
import hu.scelightapibase.util.gui.HasRIcon;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Game mode.
 * 
 * @author Andras Belicza
 * 
 * @see IEnum
 */
public interface IGameMode extends HasRIcon, IEnum {
	
	/** AutoMM (Automatic/Anonymous Matchmaking), for example ladder games. */
	IGameMode         AMM           = GameMode.AMM;
	
	/** Private. */
	IGameMode         PRIVATE       = GameMode.PRIVATE;
	
	/** Public. */
	IGameMode         PUBLIC        = GameMode.PUBLIC;
	
	/** Single player. */
	IGameMode         SINGLE_PLAYER = GameMode.SINGLE_PLAYER;
	
	/** Unknown. */
	IGameMode         UNKNOWN       = GameMode.UNKNOWN;
	
	
	/** An unmodifiable list of all the game modes. */
	List< IGameMode > VALUE_LIST    = Collections.unmodifiableList( Arrays.< IGameMode > asList( GameMode.VALUES ) );
	
}
