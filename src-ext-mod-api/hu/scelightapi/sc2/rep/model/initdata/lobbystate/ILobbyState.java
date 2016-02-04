/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapi.sc2.rep.model.initdata.lobbystate;

import hu.scelightapi.util.IStructView;

/**
 * Lobby state.
 * 
 * @author Andras Belicza
 */
public interface ILobbyState extends IStructView {
	
	// Structure field names and paths (all literal strings are interned)
	
	/** Default AI build field name. */
	String F_DEFAULT_AI_BUILD   = "defaultAIBuild";
	
	/** Default difficulty field name. */
	String F_DEFAULT_DIFFICULTY = "defaultDifficulty";
	
	/** Game duration field name. */
	String F_GAME_DURATION      = "gameDuration";
	
	/** Host user id field name. */
	String F_HOST_USER_ID       = "hostUserId";
	
	/** Is single player field name. */
	String F_IS_SINGLE_PLAYER   = "isSinglePlayer";
	
	/** Max observers field name. */
	String F_MAX_OBSERVERS      = "maxObservers";
	
	/** Max users field name. */
	String F_MAX_USERS          = "maxUsers";
	
	/** Phase field name. */
	String F_PHASE              = "phase";
	
	/** Random seed field name. */
	String F_RANDOM_SEED        = "randomSeed";
	
	/** Slots field name. */
	String F_SLOTS              = "slots";
	
	
	/**
	 * Returns the defaultAiBuild.
	 * 
	 * @return the defaultAiBuild
	 */
	Integer getDefaultAiBuild();
	
	/**
	 * Returns the defaultDifficulty.
	 * 
	 * @return the defaultDifficulty
	 */
	Integer getDefaultDifficulty();
	
	/**
	 * . Returns the gameDuration
	 * 
	 * @return the gameDuration
	 */
	Integer getGameDuration();
	
	/**
	 * Returns the hostUserId.
	 * 
	 * @return the hostUserId
	 */
	Integer getHostUserId();
	
	/**
	 * Returns the isSinglePlayer.
	 * 
	 * @return the isSinglePlayer
	 */
	Boolean getIsSinglePlayer();
	
	/**
	 * Returns the maxObservers.
	 * 
	 * @return the maxObservers
	 */
	Integer getMaxObservers();
	
	/**
	 * Returns the maxUsers.
	 * 
	 * @return the maxUsers
	 */
	Integer getMaxUsers();
	
	/**
	 * Returns the phase.
	 * 
	 * @return the phase
	 */
	Integer getPhase();
	
	/**
	 * Returns the randomSeed.
	 * 
	 * @return the randomSeed
	 */
	Integer getRandomSeed();
	
	/**
	 * Returns the slots.
	 * 
	 * @return the slots
	 */
	ISlot[] getSlots();
	
}
