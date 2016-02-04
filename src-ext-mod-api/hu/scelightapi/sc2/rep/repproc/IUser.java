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

import hu.scelightapi.sc2.rep.model.details.IDetails;
import hu.scelightapi.sc2.rep.model.details.IPlayer;
import hu.scelightapi.sc2.rep.model.details.IToon;
import hu.scelightapi.sc2.rep.model.gameevents.camera.ICameraUpdateEvent;
import hu.scelightapi.sc2.rep.model.initdata.lobbystate.IPlayerColor;
import hu.scelightapi.sc2.rep.model.initdata.lobbystate.ISlot;
import hu.scelightapi.sc2.rep.model.initdata.userinitdata.IUserInitData;

import java.awt.geom.Point2D;

/**
 * User. Grouped and precomputed info about a user.
 * 
 * <p>
 * There is a user to all players and observers, including computers.
 * </p>
 * 
 * @author Andras Belicza
 * 		
 * @see IRepProcessor
 */
public interface IUser {
	
	/**
	 * Returns the toon of the user.
	 * 
	 * @return the toon of the user
	 */
	IToon getToon();
	
	/**
	 * Returns the reference to the {@link IRepProcessor}.
	 * 
	 * @return the reference to the {@link IRepProcessor}
	 */
	IRepProcessor getRepProcessor();
	
	/**
	 * Returns the slot index of the user.
	 * 
	 * @return the slot index of the user
	 */
	int getSlotIdx();
	
	/**
	 * Returns the player index of the user.<br>
	 * In ladder games <code>playerId = playerIdx + 1</code> but it can deviate in custom games.
	 * 
	 * @return the player index of the user
	 */
	int getPlayerIdx();
	
	/**
	 * Returns the slot of the user.
	 * 
	 * @return the slot of the user
	 */
	ISlot getSlot();
	
	/**
	 * Returns the user init data of the user.
	 * 
	 * @return the user init data of the user
	 */
	IUserInitData getUid();
	
	/**
	 * Returns the {@link IPlayer} object from the {@link IDetails#getPlayerList()}, if any.
	 * 
	 * @return the {@link IPlayer} object from the {@link IDetails#getPlayerList()}, if any
	 */
	IPlayer getPlayer();
	
	/**
	 * Returns the player user index after optional reordering (e.g. favored player list) in {@link IRepProcessor#getPlayerUsers()}.
	 * 
	 * @return the player user index after optional reordering
	 */
	int getPlayerUserIdx();
	
	/**
	 * Returns the name of the user excluding clan tag.
	 * 
	 * @return the name of the user excluding clan tag
	 */
	String getName();
	
	/**
	 * Returns the full name of the user including clan tag.
	 * 
	 * @return the full name of the user including clan tag
	 */
	String getFullName();
	
	/**
	 * Returns the user index (index in the {@link IRepProcessor#getUsers()} array.
	 * 
	 * @return the user index
	 */
	int getUserIdx();
	
	/**
	 * Returns the first camera update event which has a target point.
	 * 
	 * @return the first camera update event which has a target point
	 */
	ICameraUpdateEvent getFirstCamUpdateEvent();
	
	/**
	 * Returns the loop at which the user left the game.
	 * 
	 * @return the loop at which the user left the game
	 */
	int getLeaveLoop();
	
	/**
	 * Returns the last loop when the user issued a command game event.
	 * 
	 * @return the last loop when the user issued a command game event
	 */
	int getLastCmdLoop();
	
	/**
	 * Returns the number of actions to be included in the APM calculation.
	 * 
	 * @return the number of actions to be included in the APM calculation
	 */
	int getApmActions();
	
	/**
	 * Returns the APM of the user.
	 * <p>
	 * Algorithm: See {@link IRepProcessor#calculatePerMinute(long, int)}.<br>
	 * Additionally: time range: initial time is excluded based on setting, and last loop is the loop of the last cmd game event of the user; and actions are
	 * all game events within this time range except camera update game events.
	 * </p>
	 * 
	 * @return the APM of the user
	 */
	int getApm();
	
	/**
	 * Returns the number of actions to be included in the SPM calculation.
	 * 
	 * @return the number of actions to be included in the SPM calculation
	 */
	int getSpmActions();
	
	/**
	 * Returns the SPM of the user.
	 * <p>
	 * Algorithm: See {@link IRepProcessor#calculatePerMinute(double, int)}.<br>
	 * Additionally: time range: initial time is excluded based on setting, and last loop is the loop of the last cmd game event of the user; and actions are
	 * all camera update events which move the screen by a distance larger than 15.
	 * </p>
	 * 
	 * @return the SPM of the user
	 */
	double getSpm();
	
	/**
	 * Returns the SQ (Spending Quotient) of the user.
	 * <p>
	 * Algorithm: See {@link IRepProcessor#calculateSQ(int, int)}.<br>
	 * Additionally: samples are taken up to the loop of the last cmd game event of the user.
	 * </p>
	 * 
	 * @return the SQ (Spending Quotient) of the user.
	 */
	int getSq();
	
	/**
	 * Returns the direction of the start location of the player from the center of the map in the range of 1..12 as an hour value.
	 * 
	 * @return the direction of the start location of the player from the center of the map in the range of 1..12 as an hour value
	 */
	int getStartDirection();
	
	/**
	 * Returns the supply-capped percent (ratio of supply-capped time up until the last cmd game event and the time of the last cmd game event of the user).
	 * 
	 * @return the supply-capped percent (ratio of supply-capped time up until the last cmd game event and the time of the last cmd game event of the user)
	 */
	double getSupplyCappedPercent();
	
	/**
	 * Returns the start location on the map, matched by the first camera update event (which has a target point).
	 * 
	 * @return the start location on the map, matched by the first camera update event (which has a target point)
	 */
	Point2D.Float getStartLocation();
	
	/**
	 * Returns the player color of the user.
	 * <p>
	 * The returned color originates from {@link ISlot#getPlayerColor()} in non-archon games, and in case of {@link IFormat#ARCHON} it returns the tandem
	 * leader's color.
	 * </p>
	 * 
	 * @return the player color of the user
	 * 		
	 * @since 1.5.1
	 */
	IPlayerColor getPlayerColor();
	
}
