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

import hu.scelight.gui.icon.Icons;
import hu.scelightapi.search.IFilterBy;
import hu.scelightapi.search.IFilterByGroup;
import hu.sllauncher.gui.icon.LRIcon;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

/**
 * Replay filter by group entity.
 * 
 * @author Andras Belicza
 */
public enum FilterByGroup implements IFilterByGroup {
	
	/** SC2 client group. */
	SC2( "SC2 client", Icons.SC2_ICON, FilterBy.REGION, FilterBy.EXPANSION, FilterBy.VERSION, FilterBy.BUILD_NUMBER, FilterBy.BASE_BUILD, FilterBy.BETA_PTR ),
	
	/** Replay properties. */
	REPLAY(
	        "Replay",
	        Icons.SC2_REPLAY,
	        FilterBy.MAP_NAME,
	        FilterBy.GAME_MODE,
	        FilterBy.LENGTH,
	        FilterBy.RACE_MATCHUP,
	        FilterBy.LEAGUE_MATCHUP,
	        FilterBy.GAME_SPEED,
	        FilterBy.REPLAY_DATE,
	        FilterBy.COMPETITIVE,
	        FilterBy.FORMAT,
	        FilterBy.MAP_WIDTH,
	        FilterBy.MAP_HEIGHT,
	        FilterBy.MAP_FILE_NAME ),
	
	/** Chat message group. */
	CHAT( "Chat", Icons.F_BALLOONS, FilterBy.ANY_CHAT_MESSAGE, FilterBy.ALL_CHAT_MESSAGES, FilterBy.CHAT_MESSAGES_COUNT ),
	
	/** Replay File group. */
	FILE(
	        "Replay File",
	        Icons.F_BLUE_DOCUMENT,
	        FilterBy.FILE_NAME,
	        FilterBy.FILE_PATH,
	        FilterBy.FILE_SIZE,
	        FilterBy.FILE_CREATED,
	        FilterBy.FILE_MODIFIED,
	        FilterBy.FILE_ACCESSED ),
	
	/** Players Average group. */
	PLAYERS_AVG(
	        "Players Avg",
	        Icons.F_USERS,
	        FilterBy.AVG_APM,
	        FilterBy.AVG_LEAGUE,
	        FilterBy.AVG_SPM,
	        FilterBy.AVG_SQ,
	        FilterBy.AVG_LEVELS,
	        FilterBy.AVG_SUPPLY_CAPPED ),
	
	/** Any Player group. */
	ANY_PLAYER( "Any Player", Icons.F_USER_SILHOUETTE_QUESTION, FilterBy.PLAYER_VALUES ),
	
	/** All Players group. */
	ALL_PLAYERS( "All Players", Icons.F_USER_SILHOUETTE, FilterBy.PLAYER_VALUES ),
	
	/** Player A group. */
	PLAYER_A( "Player A", Icons.F_USER, FilterBy.PLAYER_VALUES ),
	
	/** Player A group. */
	PLAYER_B( "Player B", Icons.F_USER_RED, FilterBy.PLAYER_VALUES ),
	
	/** Player A group. */
	PLAYER_C( "Player C", Icons.F_USER_GREEN, FilterBy.PLAYER_VALUES ),
	
	/** Player A group. */
	PLAYER_D( "Player D", Icons.F_USER_YELLOW, FilterBy.PLAYER_VALUES );
	
	
	
	/** Text representation of the filter by. */
	public final String           text;
	
	/** Ricon of the filter by group. */
	public final LRIcon           ricon;
	
	/** Filter by's of the group. */
	public final FilterBy[]       filterBys;
	
	/** An unmodifiable list of filter by's of the group. */
	public final List< FilterBy > filterByList;
	
	
	/**
	 * Creates a new {@link FilterByGroup}.
	 * 
	 * @param text text representation of the filter by group
	 * @param ricon ricon of the filter by group
	 * @param filterBys filter by's of the group
	 */
	private FilterByGroup( final String text, final LRIcon ricon, final FilterBy... filterBys ) {
		this.text = text;
		this.ricon = ricon;
		this.filterBys = filterBys;
		filterByList = Collections.unmodifiableList( Arrays.asList( filterBys ) );
	}
	
	@Override
	public String toString() {
		return text;
	}
	
	@Override
	public LRIcon getRicon() {
		return ricon;
	}
	
	@Override
	public List< ? extends IFilterBy > getFilterByList() {
		return filterByList;
	}
	
	
	/** Cache of the values array. */
	public static final FilterByGroup[]      VALUES                    = values();
	
	/** Player-specific filter by groups. */
	public static final Set< FilterByGroup > PLAYER_SPECIFIC_VALUE_SET = EnumSet.of( PLAYER_A, PLAYER_B, PLAYER_C, PLAYER_D );
	
}
