/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapi.sc2.rep.model.initdata.userinitdata;

import hu.scelightapi.sc2.rep.model.details.IDetails;
import hu.scelightapi.sc2.rep.model.initdata.gamedesc.ICacheHandle;
import hu.scelightapi.util.IStructView;

/**
 * User init data.
 * 
 * @author Andras Belicza
 */
public interface IUserInitData extends IStructView {
	
	// Structure field names and paths (all literal strings are interned)
	
	/** Clan tag field name. */
	String   F_CLAN_TAG             = "clanTag";
	
	/** Clan logo field name. */
	String   F_CLAN_LOGO            = "clanLogo";
	
	/** Combined race levels field name. */
	String   F_COMBINED_RACE_LEVELS = "combinedRaceLevels";
	
	/** Custom interface field name. */
	String   F_CUSTOM_INTERFACE     = "customInterface";
	
	/** Examine field name. */
	String   F_EXAMINE              = "examine";
	
	/** Highest league field name. */
	String   F_HIGHEST_LEAGUE       = "highestLeague";
	
	/** Name field name. */
	String   F_NAME                 = "name";
	
	/** Observe field name. */
	String   F_OBSERVE              = "observe";
	
	/** Race preference field name. */
	String   F_RACE_PREFERENCE      = "racePreference";
	
	/** Race preference race field path. */
	String[] P_RACE_PREFERENCE_RACE = { F_RACE_PREFERENCE, "race" };
	
	/** Random seed field name. */
	String   F_RANDOM_SEED          = "randomSeed";
	
	/** Team preference field name. */
	String   F_TEAM_PREFERENCE      = "teamPreference";
	
	/** Team preference team field path. */
	String[] P_TEAM_PREFERENCE_TEAM = { F_TEAM_PREFERENCE, "team" };
	
	/** Test auto field name. */
	String   F_TEST_AUTO            = "testAuto";
	
	/** Test map field name. */
	String   F_TEST_MAP             = "testMap";
	
	/** MMR */
	String   F_MMR = "scaledRating";
	
	
	/**
	 * Returns the associated player id for this user init data (in {@link IDetails#getPlayerList()})
	 *
	 * @return the associated player id for this user init data (in {@link IDetails#getPlayerList()})
	 */
	int getPlayerId();
	
	/**
	 * Returns the clan tag.
	 * 
	 * @return the clan tag
	 */
	String getClanTag();
	
	/**
	 * Returns the user name.
	 * 
	 * @return the user name
	 */
	String getName();
	
	/**
	 * Returns the full user name (with clan tag included).
	 * 
	 * @return the full user name (with clan tag included)
	 */
	String getFullName();
	
	/**
	 * Returns the clanLogo.
	 * 
	 * @return the clanLogo
	 */
	ICacheHandle getClanLogo();
	
	/**
	 * Return the combinedRaceLevels.
	 * 
	 * @return the combinedRaceLevels
	 */
	Integer getCombinedRaceLevels();
	
	/**
	 * Returns the customInterface.
	 * 
	 * @return the customInterface
	 */
	Boolean getCustomInterface();
	
	/**
	 * Returns the examine.
	 * 
	 * @return the examine
	 */
	Boolean getExamine();
	
	/**
	 * Returns the highestLeague id.
	 * 
	 * @return the highestLeague id
	 */
	Integer getHighestLeagueId();
	
	/**
	 * Returns the observe.
	 * 
	 * @return the observe
	 */
	Integer getObserve();
	
	/**
	 * Returns the racePreference.
	 * 
	 * @return the racePreference
	 */
	Integer getRacePreference();
	
	/**
	 * Returns the randomSeed.
	 * 
	 * @return the randomSeed
	 */
	Integer getRandomSeed();
	
	/**
	 * Returns the teamPreference.
	 * 
	 * @return the teamPreference
	 */
	Integer getTeamPreference();
	
	/**
	 * Returns the testAuto.
	 * 
	 * @return the testAuto
	 */
	Boolean getTestAuto();
	
	/**
	 * Returns the testMap.
	 * 
	 * @return the testMap
	 */
	Boolean getTestMap();
	
	/**
	 * Returns the highest league.
	 * 
	 * @return the highest league
	 */
	ILeague getHighestLeague();
	
	/**
	 * Returns the MMR.
	 *
	 * @return MMR
	 */
	Integer getMMR();
}
