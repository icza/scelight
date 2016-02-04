/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapi.search;

import hu.scelight.search.FilterBy;
import hu.scelightapibase.util.IEnum;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Replay filter by entity.
 * 
 * @author Andras Belicza
 * 
 * @see IEnum
 */
public interface IFilterBy extends IEnum {
	
	// SC2 CLIENT GROUP
	
	/** Region. */
	IFilterBy         REGION              = FilterBy.REGION;
	
	/** Expansion. */
	IFilterBy         EXPANSION           = FilterBy.EXPANSION;
	
	/** Replay version. */
	IFilterBy         VERSION             = FilterBy.VERSION;
	
	/** Replay version build number. */
	IFilterBy         BUILD_NUMBER        = FilterBy.BUILD_NUMBER;
	
	/** Base build. */
	IFilterBy         BASE_BUILD          = FilterBy.BASE_BUILD;
	
	/** Beta / PTR. */
	IFilterBy         BETA_PTR            = FilterBy.BETA_PTR;
	
	
	// REPLAY GROUP
	
	/** Map name. */
	IFilterBy         MAP_NAME            = FilterBy.MAP_NAME;
	
	/** Game mode. */
	IFilterBy         GAME_MODE           = FilterBy.GAME_MODE;
	
	/** Length in seconds. */
	IFilterBy         LENGTH              = FilterBy.LENGTH;
	
	/** Race matchup. */
	IFilterBy         RACE_MATCHUP        = FilterBy.RACE_MATCHUP;
	
	/** League matchup. */
	IFilterBy         LEAGUE_MATCHUP      = FilterBy.LEAGUE_MATCHUP;
	
	/** Game speed. */
	IFilterBy         GAME_SPEED          = FilterBy.GAME_SPEED;
	
	/** Replay date. */
	IFilterBy         REPLAY_DATE         = FilterBy.REPLAY_DATE;
	
	/** Competitive. */
	IFilterBy         COMPETITIVE         = FilterBy.COMPETITIVE;
	
	/** Game speed. */
	IFilterBy         FORMAT              = FilterBy.FORMAT;
	
	/** Map width. */
	IFilterBy         MAP_WIDTH           = FilterBy.MAP_WIDTH;
	
	/** Map height. */
	IFilterBy         MAP_HEIGHT          = FilterBy.MAP_HEIGHT;
	
	/** Map file name. */
	IFilterBy         MAP_FILE_NAME       = FilterBy.MAP_FILE_NAME;
	
	
	
	// CHAT GROUP
	
	/** Any chat message. */
	IFilterBy         ANY_CHAT_MESSAGE    = FilterBy.ANY_CHAT_MESSAGE;
	
	/** All chat messages. */
	IFilterBy         ALL_CHAT_MESSAGES   = FilterBy.ALL_CHAT_MESSAGES;
	
	/** Chat messages count. */
	IFilterBy         CHAT_MESSAGES_COUNT = FilterBy.CHAT_MESSAGES_COUNT;
	
	
	
	// REPLAY FILE GROUP
	
	/** File name. */
	IFilterBy         FILE_NAME           = FilterBy.FILE_NAME;
	
	/** File path name. */
	IFilterBy         FILE_PATH           = FilterBy.FILE_PATH;
	
	/** File size in bytes. */
	IFilterBy         FILE_SIZE           = FilterBy.FILE_SIZE;
	
	/** File created time. */
	IFilterBy         FILE_CREATED        = FilterBy.FILE_CREATED;
	
	/** File last modified time. */
	IFilterBy         FILE_MODIFIED       = FilterBy.FILE_MODIFIED;
	
	/** File accessed time. */
	IFilterBy         FILE_ACCESSED       = FilterBy.FILE_ACCESSED;
	
	
	
	// PLAYERS AVG GROUP
	
	/** Average APM. */
	IFilterBy         AVG_APM             = FilterBy.AVG_APM;
	
	/** Average League. */
	IFilterBy         AVG_LEAGUE          = FilterBy.AVG_LEAGUE;
	
	/** Average SPM. */
	IFilterBy         AVG_SPM             = FilterBy.AVG_SPM;
	
	/** Average SQ. */
	IFilterBy         AVG_SQ              = FilterBy.AVG_SQ;
	
	/** Average Levels. */
	IFilterBy         AVG_LEVELS          = FilterBy.AVG_LEVELS;
	
	/** Average Supply-capped %. */
	IFilterBy         AVG_SUPPLY_CAPPED   = FilterBy.AVG_SUPPLY_CAPPED;
	
	
	// PLAYER SPECIFIC GROUP
	
	/** Player name. */
	IFilterBy         NAME                = FilterBy.NAME;
	
	/** Player name. */
	IFilterBy         FULL_NAME           = FilterBy.FULL_NAME;
	
	/** Player clan name. */
	IFilterBy         CLAN                = FilterBy.CLAN;
	
	/** Player Toon. */
	IFilterBy         TOON                = FilterBy.TOON;
	
	/** Player APM. */
	IFilterBy         APM                 = FilterBy.APM;
	
	/** Player League. */
	IFilterBy         LEAGUE              = FilterBy.LEAGUE;
	
	/** Player SPM. */
	IFilterBy         SPM                 = FilterBy.SPM;
	
	/** Player SQ. */
	IFilterBy         SQ                  = FilterBy.SQ;
	
	/** Player Race. */
	IFilterBy         RACE                = FilterBy.RACE;
	
	/** Player Result. */
	IFilterBy         RESULT              = FilterBy.RESULT;
	
	/** Player Levels. */
	IFilterBy         LEVELS              = FilterBy.LEVELS;
	
	/** Supply-capped %. */
	IFilterBy         SUPPLY_CAPPED       = FilterBy.SUPPLY_CAPPED;
	
	/** Player Control. */
	IFilterBy         CONTROL             = FilterBy.CONTROL;
	
	/** Player Team. */
	IFilterBy         TEAM                = FilterBy.TEAM;
	
	/** Player Color. */
	IFilterBy         COLOR               = FilterBy.COLOR;
	
	/** Player Start direction. */
	IFilterBy         START_DIR           = FilterBy.START_DIR;
	
	
	/** An unmodifiable list of all the filter by's. */
	List< IFilterBy > VALUE_LIST          = Collections.unmodifiableList( Arrays.< IFilterBy > asList( FilterBy.VALUES ) );
	
	/** An unmodifiable list of player-specific filter by values. */
	List< IFilterBy > PLAYER_VALUES       = Collections.unmodifiableList( Arrays.< IFilterBy > asList( FilterBy.PLAYER_VALUES ) );
	
	
	/**
	 * Returns an unmodifiable list of operators supported by the filter by.
	 * 
	 * @return an unmodifiable list of operators supported by the filter by
	 */
	List< ? extends IOperator > getOperatorList();
	
	/**
	 * Returns the type of the filter value.
	 * 
	 * @return the type of the filter value
	 */
	Class< ? > getType();
	
}
