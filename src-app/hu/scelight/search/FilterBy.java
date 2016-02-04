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

import hu.belicza.andras.util.VersionView;
import hu.scelight.sc2.rep.model.attributesevents.GameMode;
import hu.scelight.sc2.rep.model.details.Race;
import hu.scelight.sc2.rep.model.details.Result;
import hu.scelight.sc2.rep.model.initdata.gamedesc.ExpansionLevel;
import hu.scelight.sc2.rep.model.initdata.gamedesc.GameSpeed;
import hu.scelight.sc2.rep.model.initdata.gamedesc.Region;
import hu.scelight.sc2.rep.model.initdata.lobbystate.Controller;
import hu.scelight.sc2.rep.model.initdata.lobbystate.PlayerColor;
import hu.scelight.sc2.rep.model.initdata.userinitdata.League;
import hu.scelight.sc2.rep.repproc.Format;
import hu.scelightapi.search.IFilterBy;
import hu.scelightapi.search.IOperator;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Replay filter by entity.
 * 
 * @author Andras Belicza
 */
public enum FilterBy implements IFilterBy {
	
	// SC2 CLIENT GROUP
	
	/** Region. */
	REGION( "Region", Operator.ENUM_OPERATORS, Region.class ),
	
	/** Expansion. */
	EXPANSION( "Expansion", Operator.ENUM_OPERATORS, ExpansionLevel.class ),
	
	/** Replay version. */
	VERSION( "Version", Operator.VERSION_OPERATORS, VersionView.class ),
	
	/** Replay version build number. */
	BUILD_NUMBER( "Build number", Operator.NUMBER_OPERATORS, Integer.class ),
	
	/** Base build. */
	BASE_BUILD( "Base build", Operator.NUMBER_OPERATORS, Integer.class ),
	
	/** Beta / PTR. */
	BETA_PTR( "Is Beta / PTR?", Operator.BOOL_OPERATORS, Boolean.class ),
	
	
	// REPLAY GROUP
	
	/** Map name. */
	MAP_NAME( "Map name", Operator.TEXT_OPERATORS, String.class ),
	
	/** Game mode. */
	GAME_MODE( "Game mode", Operator.ENUM_OPERATORS, GameMode.class ),
	
	/** Length in seconds. */
	LENGTH( "Length (seconds)", Operator.NUMBER_OPERATORS, Integer.class ),
	
	/** Race matchup. */
	RACE_MATCHUP( "Race matchup", Operator.TEXT_OPERATORS, String.class ),
	
	/** League matchup. */
	LEAGUE_MATCHUP( "League matchup", Operator.TEXT_OPERATORS, String.class ),
	
	/** Game speed. */
	GAME_SPEED( "Game speed", Operator.ENUM_OPERATORS, GameSpeed.class ),
	
	/** Replay date. */
	REPLAY_DATE( "Replay date", Operator.NUMBER_OPERATORS, Date.class ),
	
	/** Competitive. */
	COMPETITIVE( "Is Competitive?", Operator.BOOL_OPERATORS, Boolean.class ),
	
	/** Game speed. */
	FORMAT( "Format", Operator.ENUM_OPERATORS, Format.class ),
	
	/** Map width. */
	MAP_WIDTH( "Map width", Operator.NUMBER_OPERATORS, Integer.class ),
	
	/** Map height. */
	MAP_HEIGHT( "Map height", Operator.NUMBER_OPERATORS, Integer.class ),
	
	/** Map file name. */
	MAP_FILE_NAME( "Map file name", Operator.TEXT_OPERATORS, String.class ),
	
	
	
	// CHAT GROUP
	
	/** Any chat message. */
	ANY_CHAT_MESSAGE( "Any chat message", Operator.TEXT_OPERATORS, String.class ),
	
	/** All chat messages. */
	ALL_CHAT_MESSAGES( "All chat messages", Operator.TEXT_OPERATORS, String.class ),
	
	/** Chat messages count. */
	CHAT_MESSAGES_COUNT( "Chat messages count", Operator.NUMBER_OPERATORS, Integer.class ),
	
	
	
	// REPLAY FILE GROUP
	
	/** File name. */
	FILE_NAME( "File name", Operator.TEXT_OPERATORS, String.class ),
	
	/** File path name. */
	FILE_PATH( "File path (folder+name)", Operator.TEXT_OPERATORS, String.class ),
	
	/** File size in bytes. */
	FILE_SIZE( "File size (bytes)", Operator.NUMBER_OPERATORS, Integer.class ),
	
	/** File created time. */
	FILE_CREATED( "File created", Operator.NUMBER_OPERATORS, Date.class ),
	
	/** File last modified time. */
	FILE_MODIFIED( "File last modified", Operator.NUMBER_OPERATORS, Date.class ),
	
	/** File accessed time. */
	FILE_ACCESSED( "File last accessed", Operator.NUMBER_OPERATORS, Date.class ),
	
	
	
	// PLAYERS AVG GROUP
	
	/** Average APM. */
	AVG_APM( "Average APM", Operator.NUMBER_OPERATORS, Integer.class ),
	
	/** Average League. */
	AVG_LEAGUE( "Average League", Operator.ENUM_OPERATORS, League.class ),
	
	/** Average SPM. */
	AVG_SPM( "Average SPM", Operator.NUMBER_OPERATORS, Integer.class ),
	
	/** Average SQ. */
	AVG_SQ( "Average SQ", Operator.NUMBER_OPERATORS, Integer.class ),
	
	/** Average Levels. */
	AVG_LEVELS( "Average Levels", Operator.NUMBER_OPERATORS, Integer.class ),
	
	/** Average Supply-capped %. */
	AVG_SUPPLY_CAPPED( "Avg. Supply-capped %", Operator.NUMBER_OPERATORS, Integer.class ),
	
	
	// PLAYER SPECIFIC GROUP
	
	/** Player name. */
	NAME( "Name", Operator.TEXT_OPERATORS, String.class ),
	
	/** Player name. */
	FULL_NAME( "Full name (with clan)", Operator.TEXT_OPERATORS, String.class ),
	
	/** Player clan name. */
	CLAN( "Clan", Operator.TEXT_OPERATORS, String.class ),
	
	/** Player Toon. */
	TOON( "Toon", Operator.TEXT_OPERATORS, String.class ),
	
	/** Player APM. */
	APM( "APM", Operator.NUMBER_OPERATORS, Integer.class ),
	
	/** Player League. */
	LEAGUE( "League", Operator.ENUM_OPERATORS, League.class ),
	
	/** Player SPM. */
	SPM( "SPM", Operator.NUMBER_OPERATORS, Integer.class ),
	
	/** Player SQ. */
	SQ( "SQ", Operator.NUMBER_OPERATORS, Integer.class ),
	
	/** Player Race. */
	RACE( "Race", Operator.ENUM_OPERATORS, Race.class ),
	
	/** Player Result. */
	RESULT( "Result", Operator.ENUM_OPERATORS, Result.class ),
	
	/** Player Levels. */
	LEVELS( "Levels", Operator.NUMBER_OPERATORS, Integer.class ),
	
	/** Supply-capped %. */
	SUPPLY_CAPPED( "Supply-capped %", Operator.NUMBER_OPERATORS, Integer.class ),
	
	/** Player Control. */
	CONTROL( "Control", Operator.ENUM_OPERATORS, Controller.class ),
	
	/** Player Team. */
	TEAM( "Team", Operator.NUMBER_OPERATORS, Integer.class ),
	
	/** Player Color. */
	COLOR( "Color", Operator.ENUM_OPERATORS, PlayerColor.class ),
	
	/** Player Start direction. */
	START_DIR( "Start direction", Operator.NUMBER_OPERATORS, Integer.class );
	
	
	
	/** Text representation of the filter by. */
	public final String           text;
	
	/** Collection of operators supported by the filter by. */
	public final Operator[]       operators;
	
	/** An unmodifiable list of operators supported by the filter by. */
	public final List< Operator > operatorList;
	
	/** Type of the filter value. */
	public final Class< ? >       type;
	
	/**
	 * Creates a new {@link FilterBy}.
	 * 
	 * @param text text representation of the filter by
	 * @param operators operators supported by the filter by
	 * @param type type of the filter value
	 */
	private FilterBy( final String text, final Operator[] operators, final Class< ? > type ) {
		this.text = text;
		this.operators = operators;
		operatorList = Collections.unmodifiableList( Arrays.asList( operators ) );
		this.type = type;
	}
	
	@Override
	public List< ? extends IOperator > getOperatorList() {
		return operatorList;
	}
	
	@Override
	public Class< ? > getType() {
		return type;
	}
	
	@Override
	public String toString() {
		return text;
	}
	
	
	/** Cache of the values array. */
	public static final FilterBy[] VALUES        = values();
	
	/** Player-specific filter by values. */
	public static final FilterBy[] PLAYER_VALUES = { NAME, FULL_NAME, CLAN, RACE, LEAGUE, RESULT, TOON, APM, SPM, SQ, LEVELS, SUPPLY_CAPPED, CONTROL, TEAM,
	        COLOR, START_DIR                    };
	
}
