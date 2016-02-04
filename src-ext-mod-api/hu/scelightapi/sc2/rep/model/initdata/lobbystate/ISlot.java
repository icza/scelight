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

import hu.scelightapi.sc2.rep.model.details.IRace;
import hu.scelightapi.service.IFactory;
import hu.scelightapi.util.IStructView;

/**
 * Slot.
 * 
 * @author Andras Belicza
 */
public interface ISlot extends IStructView {
	
	// Structure field names and paths (all literal strings are interned)
	
	/** AI build field name. */
	String	 F_AI_BUILD				 = "aiBuild";
									 
	/** Color pref field name. */
	String	 F_COLOR_PREF			 = "colorPref";
									 
	/** Color of color pref field path. */
	String[] P_COLOR_PREF_COLOR		 = { F_COLOR_PREF, "color" };
									 
	/** Control field name. */
	String	 F_CONTROL				 = "control";
									 
	/** Difficulty field name. */
	String	 F_DIFFICULTY			 = "difficulty";
									 
	/** Handicap field name. */
	String	 F_HANDICAP				 = "handicap";
									 
	/** Licenses field name. */
	String	 F_LICENSES				 = "licenses";
									 
	/** Observe field name. */
	String	 F_OBSERVE				 = "observe";
									 
	/** Race pref field name. */
	String	 F_RACE_PREF			 = "racePref";
									 
	/** Race of race pref field path. */
	String[] P_RACE_PREF_RACE		 = { F_RACE_PREF, "race" };
									 
	/** Rewards field name. */
	String	 F_REWARDS				 = "rewards";
									 
	/** Team id field name. */
	String	 F_TEAM_ID				 = "teamId";
									 
	/** Toon handle field name. */
	String	 F_TOON_HANDLE			 = "toonHandle";
									 
	/** User id field name. */
	String	 F_USER_ID				 = "userId";
									 
	/** Working set slot id field name. */
	String	 F_WORKING_SET_SLOT_ID	 = "workingSetSlotId";
									 
	/** Logo index field name. */
	String	 F_LOGO_INDEX			 = "logoIndex";
									 
	/** Tandem leader user id (in case of Archon mode games). Only present in 3.0.0. */
	String	 F_TANDEM_LEADER_USER_ID = "tandemLeaderUserId";
									 
	/** Tandem leader user id (in case of Archon mode games). Only present from 3.1.0. */
	String	 F_TANDEM_LEADER_ID		 = "tandemLeaderId";
									 
	/** Tandem id. Only present from 3.1.0 */
	String	 F_TANDEM_ID			 = "tandemId";
									 
									 
	/**
	 * @return the aiBuild
	 */
	Integer getAiBuild();
	
	/**
	 * @return the colorPref
	 */
	Integer getColorPref();
	
	/**
	 * @return the control
	 */
	Integer getControl();
	
	/**
	 * @return the difficulty
	 */
	Integer getDifficulty();
	
	/**
	 * @return the handicap
	 */
	Integer getHandicap();
	
	/**
	 * @return the licenses
	 */
	Integer[] getLicenses();
	
	/**
	 * @return the observe
	 */
	Integer getObserve();
	
	/**
	 * @return the racePref
	 */
	Integer getRacePref();
	
	/**
	 * @return the rewards
	 */
	Long[] getRewards();
	
	/**
	 * Returns the team id. The real team id.
	 * 
	 * @return the team id
	 */
	Integer getTeamId();
	
	/**
	 * Returns the toon handle. It's in the form required by {@link IFactory#newToon(String)}.
	 * 
	 * @return the toon handle
	 */
	String getToonHandle();
	
	/**
	 * Returns the user id.<br>
	 * Computers don't have a user id (they don't issue commands).
	 * 
	 * @return the user id
	 */
	Integer getUserId();
	
	/**
	 * @return the workingSetSlotId
	 */
	Integer getWorkingSetSlotId();
	
	/**
	 * Returns the player color associated with the slot.
	 * 
	 * @return the player color associated with the slot
	 */
	IPlayerColor getPlayerColor();
	
	/**
	 * Returns the controller of the slot.
	 * 
	 * @return the controller of the slot
	 */
	IController getController();
	
	/**
	 * Returns the chosen race.
	 * 
	 * @return the chosen race
	 */
	IRace getChosenRace();
	
	/**
	 * Returns the user role.
	 * 
	 * @return the user role
	 */
	IRole getRole();
	
	/**
	 * Returns the logo index.
	 * 
	 * @return the logo index
	 * 		
	 * @since 1.4.1
	 */
	Integer getLogoIndex();
	
	/**
	 * Returns the tandem leader user id (in case of Archon mode games).
	 * 
	 * @return the tandem leader user id
	 * 		
	 * @since 1.5.1
	 */
	Integer getTandemLeaderUserId();
	
	/**
	 * Returns the tandem id.
	 * 
	 * @return the tandem id
	 * 		
	 * @since 1.5.2
	 */
	Integer getTandemId();
	
}
