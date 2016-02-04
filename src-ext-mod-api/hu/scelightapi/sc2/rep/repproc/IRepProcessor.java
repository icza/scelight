/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapi.sc2.rep.repproc;

import hu.scelightapi.sc2.map.IMapInfo;
import hu.scelightapi.sc2.map.IMapObjects;
import hu.scelightapi.sc2.rep.factory.IRepParserEngine;
import hu.scelightapi.sc2.rep.model.IReplay;
import hu.scelightapi.sc2.rep.model.details.IResult;
import hu.scelightapi.sc2.rep.model.gameevents.cmd.ITagTransformation;
import hu.scelightapi.sc2.rep.model.initdata.gamedesc.ICacheHandle;
import hu.scelightapi.sc2.rep.model.initdata.gamedesc.IGameSpeed;
import hu.scelightapi.sc2.rep.model.initdata.userinitdata.ILeague;

import java.nio.file.Path;

/**
 * Replay processor. Contains utilities and gives a higher level abstraction than the {@link IReplay}.
 * 
 * @author Andras Belicza
 * 		
 * @see IRepParserEngine
 * @see IReplay
 * @see IRepParserEngine#getRepProc(Path)
 * @see IRepParserEngine#parseAndWrapReplay(Path)
 */
public interface IRepProcessor {
	
	/**
	 * Returns the processed replay file.
	 * 
	 * @return the processed replay file
	 */
	Path getFile();
	
	/**
	 * Returns the {@link IReplay} model object.
	 * 
	 * @return the {@link IReplay} model object
	 */
	IReplay getReplay();
	
	/**
	 * Tells if real-time time measurement is to be used.
	 * 
	 * @return true if real-time time measurement is to be used; false otherwise
	 */
	boolean isRealTime();
	
	/**
	 * Returns the initial time to exclude from per-minute calculations in game-time seconds.
	 * 
	 * @return the initial time to exclude from per-minute calculations in game-time seconds
	 */
	int getInitialPerMinCalcExclTime();
	
	/**
	 * Tells if detected format is to be overridden based on matchup.
	 * 
	 * @return true if detected format is to be overridden based on matchup; false otherwise
	 */
	boolean isOverrideFormatBasedOnMatchup();
	
	/**
	 * Tells if largest remaining team is to be declared winner if result info is missing.
	 * 
	 * @return true if largest remaining team is to be declared winner if result info is missing; false otherwise
	 */
	boolean isLargestRemainingTeamWins();
	
	/**
	 * Returns the unit tag transformation strategy that is used when unit tags are displayed.
	 * 
	 * @return the unit tag transformation strategy
	 */
	ITagTransformation getTagTransformation();
	
	/**
	 * Returns the game speed.
	 * 
	 * @return the game speed
	 * 		
	 * @see #getConverterGameSpeed()
	 */
	IGameSpeed getGameSpeed();
	
	/**
	 * Returns the game speed to be used to convert between game-time and real-time.
	 * 
	 * <p>
	 * Game speeds instances support converting time values between game time and real time. This game speed is used by {@link #convertToRealTime(long)} and
	 * {@link #convertToRealTime(long)}.
	 * </p>
	 * 
	 * <p>
	 * Note: since Legacy of the Void expansion is using real-time, {@link IGameSpeed#NORMAL} is returned always in case of LotV replays.
	 * </p>
	 * 
	 * @return the game speed to be used to convert between game-time and real-time
	 *         
	 * @see #getConverterGameSpeed()
	 *      
	 * @since 1.5
	 */
	IGameSpeed getConverterGameSpeed();
	
	/**
	 * Returns the users (players, observers etc.). Contains no <code>null</code> values.
	 * 
	 * @return the users (players, observers etc.)
	 */
	IUser[] getUsers();
	
	/**
	 * Returns the original users, this array is never reordered.
	 * 
	 * @return the original users
	 */
	IUser[] getOriginalUsers();
	
	/**
	 * Returns the users indexed by user id, might contain <code>null</code> values.
	 * 
	 * @return the users indexed by user id
	 */
	IUser[] getUsersByUserId();
	
	/**
	 * Returns the users indexed by player id. First element is <code>null</code> (player id is 1-based)
	 * 
	 * @return the users indexed by player id
	 */
	IUser[] getUsersByPlayerId();
	
	/**
	 * Returns the users that have a player object (participants of the game). Contains no <code>null</code> values.
	 * 
	 * @return the users that have a player object
	 */
	IUser[] getPlayerUsers();
	
	/**
	 * Calculates the SQ (Spending Quotient).
	 * <p>
	 * Algorithm: <br>
	 * <blockquote><code>SQ = 35 * ( 0.00137 * I - ln( U ) ) + 240</code></blockquote>
	 * </p>
	 * <p>
	 * Where <code>U</code> is the average unspent resources and <code>I</code> is the average income; and and samples are taken up to the loop of the last cmd
	 * game event of the user.
	 * </p>
	 * <p>
	 * Source: <a href="http://www.teamliquid.net/forum/viewmessage.php?topic_id=266019">Do you macro like a pro?</a>
	 * </p>
	 * 
	 * @param unspentResources unspent resources (Resources Current; including minerals and vespene)
	 * @param income income (Resource Collection Rate; including minerals and vespene)
	 * @return the calculated SQ (Spending Quotient)
	 */
	int calculateSQ( int unspentResources, int income );
	
	/**
	 * Calculates a Per Minute value as a <code>int</code> type (e.g. Actions Per Minute, Screens Per minute).
	 * 
	 * @param count number of anything, e.g. number of actions or screens
	 * @param loops time in loops where the count (e.g. actions) spread
	 * @return the calculated Per Minute value (e.g. Actions Per Minute, Screens Per minute)
	 * 		
	 * @see #calculatePerMinute(double, int)
	 */
	int calculatePerMinute( long count, int loops );
	
	/**
	 * Calculates a Per Minute value as a <code>double</code> type (e.g. Actions Per Minute, Screens Per minute).
	 * 
	 * @param count number anything, e.g. number of actions or screens
	 * @param loops time in loops where the count (e.g. actions) spread
	 * @return the calculated Per Minute value (e.g. Actions Per Minute, Screens Per minute)
	 * 		
	 * @see #calculatePerMinute(long, int)
	 */
	double calculatePerMinute( double count, int loops );
	
	/**
	 * Returns the user for the specified user id.
	 * 
	 * @param userId user id to return the user for
	 * @return the user for the specified user id.
	 */
	IUser getUser( int userId );
	
	/**
	 * Returns the race match-up, for example: "ZTvPP"
	 * 
	 * @return the race match-up
	 */
	String getRaceMatchup();
	
	/**
	 * Returns the league match-up, for example: "DPvPG".
	 * 
	 * @return the league match-up
	 */
	String getLeagueMatchup();
	
	/**
	 * Returns the game format.
	 * 
	 * @return the game format
	 */
	IFormat getFormat();
	
	/**
	 * Guesses the format from the race match-up.
	 * 
	 * @return the guessed format; {@link IFormat#CUSTOM} if format cannot be guessed
	 */
	IFormat guessFormat();
	
	/**
	 * Tells if the replay is an Archon mode game.
	 * <p>
	 * A game is Archon mode if the {@link IFormat} (as returned by {@link #getFormat()} is {@link IFormat#ARCHON}.
	 * </p>
	 * 
	 * @return true if the replay is an Archon mode game; false otherwise
	 * 
	 * @since 1.5.1
	 */
	boolean isArchon();
	
	/**
	 * This method converts game-time to real-time based on the user's preference <i>at construction time</i> (the value is cached and used so no inconsistent
	 * time values are produced if the setting is modified later).
	 * 
	 * @param gameMs game time to be converted
	 * @return the same time if real time is not enabled; else the game time converted to real time
	 * 		
	 * @see #convertToGameTime(long)
	 */
	long convertToRealTime( long gameMs );
	
	/**
	 * This method converts real-time to game time based on the user's preference <i>at construction time</i> (the value is cached and used so no inconsistent
	 * time values are produced if the setting is modified later).
	 * 
	 * <p>
	 * On a game speed conversion from game-time to real-time is multiplication by a constant, converting back means to multiply with the reciprocal of the
	 * constant:<br>
	 * realMs = gameMs * c<br>
	 * gameMs = realMs / c<br>
	 * <br>
	 * Converting values in which time is a divisor must be reversed, for example:<br>
	 * APM = actions / time<br>
	 * game APM = actions / game time<br>
	 * real APM = actions / real time = actions / ( game time * c) = ( actions / game time ) / c = game APM / c<br>
	 * </p>
	 * 
	 * <p>
	 * <b>So basically this method can be used to convert APM values from game-time to real-time!</b>
	 * </p>
	 * 
	 * @param realMs real time to be converted
	 * @return the same time if real time is not enabled; else the real time converted to game time
	 * 		
	 * @see #convertToRealTime(long)
	 */
	long convertToGameTime( long realMs );
	
	/**
	 * Converts the specified game loop to time (milliseconds).
	 * 
	 * <p>
	 * This method also converts game-time to real-time based on the user's preference <i>at construction time</i> (the value is cached and used so no
	 * inconsistent time values are produced if the setting is modified later).
	 * </p>
	 * 
	 * @param gameloop game loop to be converted
	 * @return the specified game loop in time (milliseconds)
	 */
	long loopToTime( int gameloop );
	
	/**
	 * Returns the game length in milliseconds.
	 * 
	 * @return the game length in milliseconds
	 */
	long getLengthMs();
	
	/**
	 * Returns a formatted time string converted from the specified game loop.
	 * 
	 * @param gameloop game loop to convert and format
	 * @return a formatted time string converted from the specified game loop
	 */
	String formatLoopTime( int gameloop );
	
	/**
	 * Returns a comma separated list of player names grouped by team.
	 * 
	 * @return a comma separated list of player names grouped by team
	 */
	String getPlayersGrouped();
	
	/**
	 * Returns a comma separated list of player names whose result is the specified optional result.
	 * 
	 * @param result optional result filter; if specified, only players having this result will be included
	 * @return a comma separated list of player names
	 */
	String getPlayersStringOfResult( IResult result );
	
	/**
	 * Returns a comma separated list of player names.
	 * 
	 * @return a comma separated list of player names
	 */
	String getPlayersString();
	
	/**
	 * Returns a comma separated list of winner player names.
	 * 
	 * @return a comma separated list of winner player names
	 */
	String getWinnersString();
	
	/**
	 * Returns a comma separated list of loser player names.
	 * 
	 * @return a comma separated list of loser player names
	 */
	String getLosersString();
	
	/**
	 * Returns the weighted average player APM.
	 * 
	 * <p>
	 * The weights are the times used to calculate a player APM value. Only human players are included.
	 * </p>
	 * 
	 * @return the average player APM
	 */
	int getAvgAPM();
	
	/**
	 * Returns the weighted average player SPM.
	 * 
	 * <p>
	 * The weights are the times used to calculate a player SPM value. Only human players are included.
	 * </p>
	 * 
	 * @return the average player SPM
	 */
	double getAvgSPM();
	
	/**
	 * Returns the weighted average player SQ.
	 * 
	 * <p>
	 * The weights are the times used to calculate a player SQ value.
	 * </p>
	 * 
	 * @return the average player APM
	 */
	int getAvgSQ();
	
	/**
	 * Returns the average player levels.
	 * 
	 * @return the average player levels
	 */
	int getAvgLevels();
	
	/**
	 * Returns the average supply-capped percent.
	 * 
	 * @return the average supply-capped percent
	 */
	double getAvgSupplyCappedPercent();
	
	/**
	 * Returns the average player league.
	 * 
	 * <p>
	 * The algorithm rounds up, for example if there are 2 players having 2 different subsequent leagues, then the average will be the higher league.
	 * </p>
	 * 
	 * @return the average player league
	 */
	ILeague getAvgLeague();
	
	/**
	 * Returns the cache handle of the map file.
	 * 
	 * @return the cache handle of the map file
	 */
	ICacheHandle getMapCacheHandle();
	
	/**
	 * Returns the number of chat messages.
	 * 
	 * @return the number of chat messages or -1 if the message events is not parsed
	 */
	int getChatMessagesCount();
	
	/**
	 * Returns the map info.
	 * 
	 * @return the map info
	 */
	IMapInfo getMapInfo();
	
	/**
	 * Returns the map objects.
	 * 
	 * @return the map objects
	 */
	IMapObjects getMapObjects();
	
}
