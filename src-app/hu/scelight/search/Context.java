/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.search;

import hu.scelightapi.sc2.rep.model.IReplay;
import hu.scelightapi.sc2.rep.model.initdata.gamedesc.IGameDescription;
import hu.scelightapi.sc2.rep.repproc.IRepProcessor;
import hu.scelightapi.sc2.rep.repproc.IUser;

import java.util.EnumMap;
import java.util.Map;

/**
 * Search context.
 * 
 * <p>
 * Might store search state and cached values.
 * </p>
 * 
 * @author Andras Belicza
 */
class Context {
	
	/** Replay processor being tested. */
	public final IRepProcessor               repProc;
	
	/** Replay being tested. */
	public final IReplay                     replay;
	
	/** Game description of the replay being tested. */
	public final IGameDescription            gd;
	
	/** Map that contains the substitutable users for the player-specific filter by groups ({@link FilterByGroup#PLAYER_SPECIFIC_VALUE_SET}). */
	public final Map< FilterByGroup, IUser > filterByGroupUserMap = new EnumMap<>( FilterByGroup.class );
	
	
	/** User of the player to apply player-specific filter by's ({@link FilterBy#PLAYER_VALUES}). */
	public IUser                             user;
	
	
	/**
	 * Creates a new {@link Context}.
	 * 
	 * @param repProc reference to the replay processor
	 */
	public Context( final IRepProcessor repProc ) {
		this.repProc = repProc;
		replay = repProc.getReplay();
		gd = replay.getInitData().getGameDescription();
	}
	
}
