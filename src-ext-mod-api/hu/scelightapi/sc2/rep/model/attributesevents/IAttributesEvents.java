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

import hu.scelightapi.util.IStructView;

import java.util.Map;

/**
 * StarCraft II Replay attributes events.
 * 
 * @author Andras Belicza
 */
public interface IAttributesEvents extends IStructView {
	
	// Structure field names and paths (all literal strings are interned)
	
	/** Source field name. */
	String  F_SOURCE                   = "source";
	
	/** Map namespace field name. */
	String  F_MAP_NAMESPACE            = "mapNamespace";
	
	/** Scopes field name. */
	String  F_SCOPES                   = "scopes";
	
	
	// Scope constants, used as the player id (starting from 1)
	
	/** Global scope (applies to all players). */
	Integer S_GLOBAL                   = 16;
	
	
	// Attribute id constants
	
	// Global scope attributes
	
	/** Controller attribute id. */
	Integer A_CONTROLLER               = 500;
	
	/** Rules attribute id. */
	Integer A_RULES                    = 1000;
	
	/** Is premade game attribute id. */
	Integer A_IS_PREMADE_GAME          = 1001;
	
	/** Parties private attribute id. */
	Integer A_PARTIES_PRIVATE          = 2000;
	
	/** Parties premade attribute id. */
	Integer A_PARTIES_PREMADE          = 2001;
	
	/** Game speed attribute id. */
	Integer A_GAME_SPEED               = 3000;
	
	/** Lobby delay attribute id. */
	Integer A_LOBBY_DELAY              = 3006;
	
	/** Game mode attribute id. */
	Integer A_GAME_MODE                = 3009;
	
	/** Privacy option attribute id. */
	Integer A_PRIVACY_OPTION           = 4000;
	
	/** Locked alliances attribute id. */
	Integer A_LOCKED_ALLIANCES         = 3010;
	
	// Player scope attributes
	
	/** Parties premade 1v1 attribute id. */
	Integer A_PARTIES_PREMADE_1V1      = 2002;
	
	/** Parties premade 2v2 attribute id. */
	Integer A_PARTIES_PREMADE_2V2      = 2003;
	
	/** Parties premade 3v3 attribute id. */
	Integer A_PARTIES_PREMADE_3V3      = 2004;
	
	/** Parties premade 4v4 attribute id. */
	Integer A_PARTIES_PREMADE_4V4      = 2005;
	
	/** Parties premade FFA attribute id. */
	Integer A_PARTIES_PREMADE_FFA      = 2006;
	
	/** Parties premade 5v5 attribute id. */
	Integer A_PARTIES_PREMADE_5V5      = 2007;
	
	/** Parties premade 6v6 attribute id. */
	Integer A_PARTIES_PREMADE_6V6      = 2008;
	
	/** Parties private one attribute id. */
	Integer A_PARTIES_PRIVATE_ONE      = 2010;
	
	/** Parties private two attribute id. */
	Integer A_PARTIES_PRIVATE_TWO      = 2011;
	
	/** Parties private three attribute id. */
	Integer A_PARTIES_PRIVATE_THREE    = 2012;
	
	/** Parties private four attribute id. */
	Integer A_PARTIES_PRIVATE_FOUR     = 2013;
	
	/** Parties private five attribute id. */
	Integer A_PARTIES_PRIVATE_FIVE     = 2014;
	
	/** Parties private six attribute id. */
	Integer A_PARTIES_PRIVATE_SIX      = 2015;
	
	/** Parties private seven attribute id. */
	Integer A_PARTIES_PRIVATE_SEVEN    = 2016;
	
	/** Parties private FFA attribute id. */
	Integer A_PARTIES_PRIVATE_FFA      = 2017;
	
	/** Parties private custom attribute id. */
	Integer A_PARTIES_PRIVATE_CUSTOM   = 2018;
	
	/** Parties private eight attribute id. */
	Integer A_PARTIES_PRIVATE_EIGHT    = 2019;
	
	/** Parties private nine attribute id. */
	Integer A_PARTIES_PRIVATE_NINE     = 2020;
	
	/** Parties private ten attribute id. */
	Integer A_PARTIES_PRIVATE_TEN      = 2021;
	
	/** Parties private eleven attribute id. */
	Integer A_PARTIES_PRIVATE_ELEVEN   = 2022;
	
	/** Race attribute id. */
	Integer A_RACE                     = 3001;
	
	/** Party color attribute id. */
	Integer A_PARTY_COLOR              = 3002;
	
	/** Handicap attribute id. */
	Integer A_HANDICAP                 = 3003;
	
	/** AI skill attribute id. */
	Integer A_AI_SKILL                 = 3004;
	
	/** AI race attribute id. */
	Integer A_AI_RACE                  = 3005;
	
	/** Participant role attribute id. */
	Integer A_PARTICIPANT_ROLE         = 3007;
	
	/** Watcher type attribute id. */
	Integer A_WATCHER_TYPE             = 3008;
	
	/** AI build first attribute id. */
	Integer A_AI_BUILD_FIRST           = 3100;
	
	/** AI build last attribute id. */
	Integer A_AI_BUILD_LAST            = 3300;
	
	/** Using custom observer UI attribute id. */
	Integer A_USING_CUSTOM_OBSERVER_UI = 4001;
	
	
	/**
	 * @return the source
	 */
	Integer getSource();
	
	/**
	 * @return the mapNamespace
	 */
	Integer getMapNamespace();
	
	/**
	 * @return the scopes
	 */
	Map< Integer, Map< Integer, ? extends IAttribute > > getScopes();
	
	/**
	 * Returns the game mode.
	 * 
	 * @return the game mode
	 */
	IGameMode getGameMode();
	
	
	/**
	 * Returns a converted, display friendly string-map source structure.
	 * <p>
	 * The source structure of the attributes events contains scopes as integer maps (where keys are integers). This is not display friendly (user friendly).
	 * This method creates / converts those integer maps to string maps (where the keys are informative strings).
	 * </p>
	 * 
	 * @return a converted string-map source structure
	 * @see #getStruct()
	 */
	Map< String, Object > getStringStruct();
	
}
