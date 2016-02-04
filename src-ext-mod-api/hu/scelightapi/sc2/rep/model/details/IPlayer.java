/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapi.sc2.rep.model.details;

import hu.scelightapi.sc2.rep.model.initdata.lobbystate.ISlot;
import hu.scelightapi.util.IStructView;

/**
 * Player.
 * 
 * @author Andras Belicza
 */
public interface IPlayer extends IStructView {
	
	// Structure field names and paths (all literal strings are interned)
	
	/** Color field name. */
	String   F_COLOR               = "color";
	
	/** Alpha color component field path . */
	String[] P_COLOR_A             = { F_COLOR, "a" };
	
	/** Red color component field path . */
	String[] P_COLOR_R             = { F_COLOR, "r" };
	
	/** Green color component field path . */
	String[] P_COLOR_G             = { F_COLOR, "g" };
	
	/** Blue color component field path . */
	String[] P_COLOR_B             = { F_COLOR, "b" };
	
	/** Control field name. */
	String   F_CONTROL             = "control";
	
	/** Handicap field name. */
	String   F_HANDICAP            = "handicap";
	
	/** Name field name. */
	String   F_NAME                = "name";
	
	/** Observe field name. */
	String   F_OBSERVE             = "observe";
	
	/** Race field name. */
	String   F_RACE                = "race";
	
	/** Result field name. */
	String   F_RESULT              = "result";
	
	/** Team id field name. */
	String   F_TEAM_ID             = "teamId";
	
	/** Toon field name. */
	String   F_TOON                = "toon";
	
	/** Working set slot id field name. */
	String   F_WORKING_SET_SLOT_ID = "workingSetSlotId";
	
	
	/**
	 * Returns the player name.
	 * 
	 * @return the player name
	 */
	String getName();
	
	/**
	 * Returns the localized race.
	 * 
	 * @return the localized race
	 */
	String getRaceString();
	
	/**
	 * Returns the race.
	 * 
	 * @return the race
	 */
	IRace getRace();
	
	/**
	 * Returns the working set slot id.
	 * 
	 * @return the working set slot id
	 */
	Integer getWorkingSetSlotId();
	
	/**
	 * Returns the ARGB color.
	 * 
	 * @return the ARGB color
	 */
	int[] getArgb();
	
	/**
	 * @return the control
	 */
	Integer getControl();
	
	/**
	 * @return the handicap
	 */
	Integer getHandicap();
	
	/**
	 * @return the observe
	 */
	Integer getObserve();
	
	/**
	 * Returns the recorded result.
	 * 
	 * @return the recorded result
	 * 
	 * @see #getDeducedResult()
	 * @see #getResult()
	 */
	IResult getRecordedResult();
	
	/**
	 * Returns the deduced result.
	 * 
	 * @return the deduced result
	 * 
	 * @see #getRecordedResult()
	 * @see #getResult()
	 */
	IResult getDeducedResult();
	
	/**
	 * Returns the match result of the player, either the one recorded in the replay ({@link #getRecordedResult()}, or the deduced result (
	 * {@link #getDeducedResult()}.
	 * 
	 * @return the match result
	 * 
	 * @see #getRecordedResult()
	 * @see #getDeducedResult()
	 */
	IResult getResult();
	
	/**
	 * Tells if the match result returned by {@link #getResult()} is originating from the replay or is a deduced result determined by an algorithm.
	 * 
	 * @return true if the match result originates from the replay; false if it is a deduced result determined by an algorithm
	 */
	boolean isResultDeduced();
	
	/**
	 * Returns the team id, not always accurate! Use {@link ISlot#getTeamId()} instead!
	 * 
	 * @return the team id, not always accurate! Use {@link ISlot#getTeamId()} instead!
	 * 
	 * @see ISlot#getTeamId()
	 */
	@Deprecated
	Integer getTeamId();
	
	/**
	 * Returns the toon of the player.
	 * 
	 * @return the toon of the player
	 */
	IToon getToon();
	
}
