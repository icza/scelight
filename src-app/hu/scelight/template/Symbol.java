/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.template;

import hu.scelight.gui.icon.Icons;
import hu.scelight.sc2.rep.model.attributesevents.GameMode;
import hu.scelight.sc2.rep.model.details.Race;
import hu.scelight.sc2.rep.model.details.Result;
import hu.scelight.sc2.rep.model.initdata.gamedesc.ExpansionLevel;
import hu.scelight.sc2.rep.model.initdata.gamedesc.Region;
import hu.scelight.sc2.rep.model.initdata.userinitdata.League;
import hu.scelight.sc2.rep.repproc.Format;
import hu.scelight.service.env.Env;
import hu.scelightapibase.util.gui.HasRIcon;
import hu.sllauncher.gui.icon.LRIcon;

/**
 * Name template symbol.
 * 
 * @author Andras Belicza
 */
public enum Symbol implements HasRIcon {
	
	/** Original file name. */
	ORIG_NAME( Icons.F_CARD, "origName", "example name", "Original file name" ),
	
	
	
	/** Map name. */
	MAP_NAME( Icons.F_MAP, "map", "The Big Game Hunters LE", "Map name" ),
	
	/** First <code>X</code> words of the map name. */
	MAP_WORDS( Icons.F_MAP, "mapWordsX", "Big Game", "<html>First <font color=red><b>X</b></font> words of the map name</html>" ),
	
	/** Map name acronym. */
	MAP_ACRONYM( Icons.F_MAP, "mapAcronym", "BGHL", "Map name acronym (first letters of the words of the map)" ),
	
	
	
	/** Date of the game. */
	DATE( Icons.F_CALENDAR_BLUE, "date", "2013-06-28", "Date of the game" ),
	
	/** Date and time of the game. */
	DATE_TIME( Icons.F_CALENDAR_BLUE, "dateTime", "2013-06-28 15_23_01", "Date and time of the game" ),
	
	/** Short date of the game. */
	DATE_SHORT( Icons.F_CALENDAR_BLUE, "dateShort", "13-06-28", "Short date of the game" ),
	
	/** Short date and time of the game. */
	DATE_TIME_SHORT( Icons.F_CALENDAR_BLUE, "dateTimeShort", "13-06-28 15_23_01", "Short date and time of the game" ),
	
	/** Tiny date of the game. */
	DATE_TINY( Icons.F_CALENDAR_BLUE, "dateTiny", "130628", "Tiny date of the game" ),
	
	/** Tiny date and time of the game. */
	DATE_TIME_TINY( Icons.F_CALENDAR_BLUE, "dateTimeTiny", "130628 152301", "Tiny date and time of the game" ),
	
	/** Replay version. */
	VERSION( Icons.F_DOCUMENT_ATTRIBUTE_V, "version", "2.0.8", "Replay version" ),
	
	/** Full Replay version. */
	VERSION_FULL( Icons.F_DOCUMENT_ATTRIBUTE_V, "versionFull", "2.0.8.25604", "Full replay version (build number included)" ),
	
	/** Replay version build number. */
	VERSION_BUILD( Icons.F_DOCUMENT_ATTRIBUTE_B, "buildNum", "25604", "Replay version build number" ),
	
	
	
	/** Replay version. */
	LENGTH( Icons.F_CLOCK_SELECT, "length", "08_42", "Game length" ),
	
	/** Game mode. */
	MODE( GameMode.RICON, "mode", "AutoMM", "Game mode" ),
	
	/** Race matchup. */
	MATCHUP( Race.RICON, "matchup", "TZvPP", "Race matchup" ),
	
	/** Game region. */
	REGION( Region.RICON, "region", "Europe", "Game region" ),
	
	/** Game region code. */
	REGION_CODE( Region.RICON, "regionCode", "EU", "Game region code" ),
	
	/** Expansion level. */
	EXPANSION( ExpansionLevel.RICON, "expansion", "HotS", "Expansion level" ),
	
	/** Game format. */
	FORMAT( Format.RICON, "format", "2v2", "Game format" ),
	
	/** Game format. */
	LEAGUE_MATCHUP( League.RICON, "leagueMatchup", "DDvMS", "League matchup" ),
	
	/** Comma separated list of player names. */
	PLAYERS( Icons.F_USERS, "players", "Bob, Ben, Rob, Ann", "Comma separated list of player names" ),
	
	/** Comma separated list of player names grouped by teams. */
	PLAYERS_GROUPED( Icons.F_USERS, "playersGrouped", "Bob, Ben vs Rob, Ann", "Comma separated list of player names grouped by teams" ),
	
	/** Comma separated list of winner player names. */
	WINNERS( Icons.SC2_VICTORY, "winners", "Bob, Ben", "Comma separated list of winner player names" ),
	
	/** Comma separated list of loser player names. */
	LOSERS( Icons.SC2_DEFEAT, "losers", "Rob, Ann", "Comma separated list of loser player names" ),
	
	/** Average player league. */
	AVG_LEAGUE(
	        League.RICON,
	        "avgLeague",
	        "D",
	        "Average league of players (first letter). B = Bronze, S = Silver, G = Gold, P = Platinum, D = Diamond, M = Master, R = Grandmaster, U = Unranked, - = Unknown" ),
	
	/** Long average player league. */
	AVG_LEAGUE_LONG( League.RICON, "avgLeagueLong", "Diamond", "Long average league of players" ),
	
	/** Weighted Average player APM. */
	AVG_APM( Icons.MY_APM, "avgApm", "126", "Weighted Average APM (Actions / min) of players" ),
	
	/** Weighted Average player SPM. */
	AVG_SPM( Icons.MY_SPM, "avgSpm", "12.47", "Weighted Average SPM (Screens / min) of players" ),
	
	/** Weighted Average player SQ. */
	AVG_SQ( Icons.MY_SQ, "avgSq", "55", "Weighted Average SQ (Spending Quotient) of players" ),
	
	/** Average player Supply-capped percent. */
	AVG_SUPPLY_CAPPED( Icons.MY_SCP, "avgSupplyCapped", "8.21", "Average Supply-capped percent of players" ),
	
	
	
	/** Counter with <code>X</code> digits which starts at 1 and gets gets incremented by 1 on each use. */
	COUNTER(
	        Icons.F_COUNTER,
	        "counterX",
	        "0001",
	        "<html>Counter with <font color=red><b>X</b></font> digits which starts at 1 and gets incremented by 1 on each use.</html>",
	        SymbolScope.MANUAL_RENAME ),
	
	
	
	/** Replay count with X digits in target folder +1. */
	REPLAY_COUNT(
	        Icons.F_COUNTER,
	        "repCountX",
	        "0001",
	        "<html>Replay count with <font color=red><b>X</b></font> digits, its value is the number of replays in target folder +1.</html>",
	        SymbolScope.AUTO_RENAME ),
	
	
	
	/** Sub-folder separator. */
	FOLDER_SEPARATOR( Icons.F_BLUE_FOLDER_TREE, "folderSep", "\\", "Sub-folder separator" ),
	
	/** Opening square brackets. */
	BRACKETS_OPEN( null, "O", "[", "Opening square brackets" ),
	
	/** Closing square brackets. */
	BRACKETS_CLOSE( null, "C", "]", "Closing square brackets" ),
	
	
	
	/** Player Info Block. */
	PIB(
	        Icons.F_USERS,
	        "<>",
	        "Bob (T), Ben (Z) vs Rob (P), Ann (P)",
	        "Player Info Block. Everything inside this will be inserted for all players, for example \"<[PIBplayer] ([PIBrace])>\"" ),
	
	/** Player name in PIB. */
	PIB_PLAYER( Icons.F_USER, "PIBplayer", "Bob", "Player name in Player Info Block", SymbolScope.PIB ),
	
	/** Player race in PIB. */
	PIB_RACE( Race.RICON, "PIBrace", "T", "Player race (first letter) in Player Info Block. T = Terran, Z = Zerg, P = Protoss, - = Unknown", SymbolScope.PIB ),
	
	/** Long player race in PIB. */
	PIB_RACE_LONG( Race.RICON, "PIBraceLong", "Terran", "Long player race in Player Info Block", SymbolScope.PIB ),
	
	/** Player league in PIB. */
	PIB_LEAGUE(
	        League.RICON,
	        "PIBleague",
	        "D",
	        "Player league (first letter) in Player Info Block. B = Bronze, S = Silver, G = Gold, P = Platinum, D = Diamond, M = Master, R = Grandmaster, U = Unranked, - = Unknown",
	        SymbolScope.PIB ),
	
	/** Long player league in PIB. */
	PIB_LEAGUE_LONG( League.RICON, "PIBleagueLong", "Diamond", "Long player league in Player Info Block", SymbolScope.PIB ),
	
	/** Player result in PIB. */
	PIB_RESULT(
	        Result.RICON,
	        "PIBresult",
	        "V",
	        "Player result (first letter) in Player Info Block. V = Victory, D = Defeat, T = Tie, - = Unknown",
	        SymbolScope.PIB ),
	
	/** Long player result in PIB. */
	PIB_RESULT_LONG( Result.RICON, "PIBresultLong", "Victory", "Long player result in Player Info Block", SymbolScope.PIB ),
	
	/** Player APM in PIB. */
	PIB_APM( Icons.MY_APM, "PIBapm", "126", "Player APM (Actions / min) in Player Info Block", SymbolScope.PIB ),
	
	/** Player SPM in PIB. */
	PIB_SPM( Icons.MY_SPM, "PIBspm", "12.47", "Player SPM (Screens / min) in Player Info Block", SymbolScope.PIB ),
	
	/** Player SQ in PIB. */
	PIB_SQ( Icons.MY_SQ, "PIBsq", "55", "Player SQ (Spending Quotient) in Player Info Block", SymbolScope.PIB ),
	
	/** Player Supply-capped percent in PIB. */
	PIB_SUPPLY_CAPPED( Icons.MY_SCP, "PIBsupplyCapped", "8.21", "Player Supply-capped percent in Player Info Block", SymbolScope.PIB ),
	
	/** Player start direction. */
	PIB_START_DIR(
	        Icons.F_DIRECTION,
	        "PIBstartDir",
	        "1",
	        "Direction of the player start location in Player Info Block. It is a time hour value (e.g. 1 o'clock)",
	        SymbolScope.PIB ),
	
	
	
	/** Name of the <code>X</code>-th player. */
	PLAYER( Icons.F_USER, "playerX", "Bob", "<html>Name of the <font color=red><b>X</b></font>-th player.</html>" ),
	
	/** Race of the <code>X</code>-th player. */
	RACE(
	        Race.RICON,
	        "raceX",
	        "T",
	        "<html>Race (first letter) of the <font color=red><b>X</b></font>-th player. T = Terran, Z = Zerg, P = Protoss, - = Unknown</html>" ),
	
	/** Long race of the <code>X</code>-th player. */
	RACE_LONG( Race.RICON, "raceLongX", "Terran", "<html>Long race of the <font color=red><b>X</b></font>-th player.</html>" ),
	
	/** League of the <code>X</code>-th player. */
	LEAGUE(
	        League.RICON,
	        "leagueX",
	        "D",
	        "<html>League (first letter) of the <font color=red><b>X</b></font>-th player. B = Bronze, S = Silver, G = Gold, P = Platinum, D = Diamond, M = Master, R = Grandmaster, U = Unranked, - = Unknown</html>" ),
	
	/** Long league of the <code>X</code>-th player. */
	LEAGUE_LONG( League.RICON, "leagueLongX", "Diamond", "<html>Long league of the <font color=red><b>X</b></font>-th player.</html>" ),
	
	/** Result of the <code>X</code>-th player. */
	RESULT(
	        Result.RICON,
	        "resultX",
	        "V",
	        "<html>Result (first letter) of the <font color=red><b>X</b></font>-th player. V = Victory, D = Defeat, T = Tie, - = Unknown</html>" ),
	
	/** Long result of the <code>X</code>-th player. */
	RESULT_LONG( Result.RICON, "resultLongX", "Victory", "<html>Long result of the <font color=red><b>X</b></font>-th player.</html>" ),
	
	/** APM of the <code>X</code>-th player. */
	APM( Icons.MY_APM, "apmX", "126", "<html>APM (Actions / min) of the <font color=red><b>X</b></font>-th player.</html>" ),
	
	/** SPM of the <code>X</code>-th player. */
	SPM( Icons.MY_SPM, "spmX", "12.47", "<html>SPM (Screens / min) of the <font color=red><b>X</b></font>-th player.</html>" ),
	
	/** SQ of the <code>X</code>-th player. */
	SQ( Icons.MY_SQ, "sqX", "55", "<html>SQ (Spending Quotient) of the <font color=red><b>X</b></font>-th player.</html>" ),
	
	/** Supply-capped percent of the <code>X</code>-th player. */
	SUPPLY_CAPPED( Icons.MY_SCP, "supplyCappedX", "8.21", "<html>Supply-capped percent of the <font color=red><b>X</b></font>-th player.</html>" ),
	
	/** Start direction of the <code>X</code>-th player. */
	START_DIR(
	        Icons.F_DIRECTION,
	        "startDirX",
	        "1",
	        "<html>Direction of the start location of the <font color=red><b>X</b></font>-th player. It is a time hour value (e.g. 1 o'clock)</html>" );
	
	
	
	/** Ricon of the symbol. */
	public final LRIcon      ricon;
	
	/** Id of the template symbol. */
	public final String      id;
	
	/** Example for the symbol. */
	public final String      example;
	
	/** One-line description. */
	public final String      description;
	
	/** Text value of the template symbol. */
	public final String      text;
	
	/** Optional scope. */
	public final SymbolScope scope;
	
	/** Tells if the symbol has a parameter, if the last character is an <code>'X'</code>. */
	public final boolean     hasParam;
	
	
	/**
	 * Creates a new {@link Symbol}.
	 * 
	 * @param ricon ricon of the symbol
	 * @param id id of the template symbol
	 * @param example example for the symbol
	 * @param description one-line description
	 */
	private Symbol( final LRIcon ricon, final String id, final String example, final String description ) {
		this( ricon, id, example, description, null );
	}
	
	/**
	 * Creates a new {@link Symbol}.
	 * 
	 * @param ricon ricon of the symbol
	 * @param id id of the template symbol
	 * @param example example for the symbol
	 * @param description one-line description
	 * @param scope optional symbol scope
	 */
	private Symbol( final LRIcon ricon, final String id, final String example, final String description, final SymbolScope scope ) {
		this.ricon = ricon;
		this.id = id;
		this.example = example;
		this.description = description;
		this.text = "<>".equals( id ) ? id : '[' + id + ']';
		this.scope = scope;
		hasParam = id.charAt( id.length() - 1 ) == 'X';
		
		if ( Env.DEV_MODE ) {
			if ( id.indexOf( '[' ) >= 0 || id.indexOf( ']' ) >= 0 )
				throw new RuntimeException( "Symbol id cannot contain square brackets: " + id );
			if ( id.indexOf( '{' ) >= 0 || id.indexOf( '}' ) >= 0 )
				throw new RuntimeException( "Symbol id cannot contain braces: " + id );
		}
	}
	
	@Override
	public LRIcon getRicon() {
		return ricon;
	}
	
	@Override
	public String toString() {
		return text;
	}
	
	
	/** Cache of the values array. */
	public static final Symbol[] VALUES = values();
	
	
}
