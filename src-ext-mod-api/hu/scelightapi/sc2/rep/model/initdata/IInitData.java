/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapi.sc2.rep.model.initdata;

import hu.scelightapi.sc2.rep.model.initdata.gamedesc.IGameDescription;
import hu.scelightapi.sc2.rep.model.initdata.lobbystate.ILobbyState;
import hu.scelightapi.sc2.rep.model.initdata.userinitdata.IUserInitData;
import hu.scelightapi.util.IStructView;

/**
 * StarCraft II Replay init data.
 * 
 * @author Andras Belicza
 */
public interface IInitData extends IStructView {
	
	// Structure field names and paths (all literal strings are interned)
	
	/** User init data field name. */
	String F_USER_INIT_DATA   = "userInitialData";
	
	/** Game description field name. */
	String F_GAME_DESCRIPTION = "gameDescription";
	
	/** Lobby state field name. */
	String F_LOBBY_STATE      = "lobbyState";
	
	
	/**
	 * Returns the array of user init data.
	 * 
	 * @return the array of user init data
	 */
	IUserInitData[] getUserInitDatas();
	
	/**
	 * Returns the game description.
	 * 
	 * @return the game description
	 */
	IGameDescription getGameDescription();
	
	/**
	 * Returns the lobby state.
	 * 
	 * @return the lobby state
	 */
	ILobbyState getLobbyState();
	
}
