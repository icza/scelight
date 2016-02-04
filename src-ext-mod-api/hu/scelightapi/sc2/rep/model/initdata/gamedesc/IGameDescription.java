/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapi.sc2.rep.model.initdata.gamedesc;

import hu.scelightapi.util.IStructView;

/**
 * Game description.
 * 
 * @author Andras Belicza
 */
public interface IGameDescription extends IStructView {
	
	// Structure field names and paths (all literal strings are interned)
	
	/** Cache handles field name. */
	String   F_CACHE_HANDLES            = "cacheHandles";
	
	/** Default AI build field name. */
	String   F_DEFAULT_AI_BUILD         = "defaultAIBuild";
	
	/** Default difficulty field name. */
	String   F_DEFAULT_DIFFICULTY       = "defaultDifficulty";
	
	/** Game cache name field name. */
	String   F_GAME_CACHE_NAME          = "gameCacheName";
	
	/** Game options field name. */
	String   F_GAME_OPTIONS             = "gameOptions";
	
	/** Advanced shared controls field path. */
	String[] P_ADVANCED_SHARED_CONTROLS = { F_GAME_OPTIONS, "advancedSharedControl" };
	
	/** AMM field path. */
	String[] P_AMM                      = { F_GAME_OPTIONS, "amm" };
	
	/** Battle net field path. */
	String[] P_BATTLE_NET               = { F_GAME_OPTIONS, "battleNet" };
	
	/** Client debug flags field path. */
	String[] P_CLIENT_DEBUG_FLAGS       = { F_GAME_OPTIONS, "clientDebugFlags" };
	
	/**
	 * Competitive field path.<br>
	 * Present in base versions from 24674, replaces {@link #P_RANKED}.
	 * 
	 * @see #P_RANKED
	 */
	String[] P_COMPETITIVE              = { F_GAME_OPTIONS, "competitive" };
	
	/**
	 * Ranked field path.<br>
	 * Present only up to base build 23260, missing starting from 24674 (replaced by {@link #P_COMPETITIVE}).
	 * 
	 * @see #P_COMPETITIVE
	 */
	String[] P_RANKED                   = { F_GAME_OPTIONS, "ranked" };
	
	/** Fog field path. */
	String[] P_FOG                      = { F_GAME_OPTIONS, "fog" };
	
	/** Lock teams field path. */
	String[] P_LOCK_TEAMS               = { F_GAME_OPTIONS, "lockTeams" };
	
	/** No victory or defeat field path. */
	String[] P_NO_VICTORY_DEFEAT        = { F_GAME_OPTIONS, "noVictoryOrDefeat" };
	
	/** Observers field path. */
	String[] P_OBSERVERS                = { F_GAME_OPTIONS, "observers" };
	
	/** Random races field path. */
	String[] P_RANDOM_RACES             = { F_GAME_OPTIONS, "randomRaces" };
	
	/** Teams together field path. */
	String[] P_TEAMS_TOGETHER           = { F_GAME_OPTIONS, "teamsTogether" };
	
	/** User difficulty field path. */
	String[] P_USER_DIFFICULTY          = { F_GAME_OPTIONS, "userDifficulty" };
	
	/** Game speed field name. */
	String   F_GAME_SPEED               = "gameSpeed";
	
	/** Game type field name. */
	String   F_GAME_TYPE                = "gameType";
	
	/** Is Blizzard map field name. */
	String   F_IS_BLIZZARD_MAP          = "isBlizzardMap";
	
	/** Is coop mode field name. */
	String   F_IS_COOP_MODE             = "isCoopMode";
	
	/** Is premade FFA field name. */
	String   F_IS_PREMADE_FFA           = "isPremadeFFA";
	
	/** Map author name field name. */
	String   F_MAP_AUTHOR_NAME          = "mapAuthorName";
	
	/** Map file name field name. */
	String   F_MAP_FILE_NAME            = "mapFileName";
	
	/** Map file checksum field name. */
	String   F_MAP_FILE_CHECKSUM        = "mapFileSyncChecksum";
	
	/** Map size X field name. */
	String   F_MAP_SIZE_X               = "mapSizeX";
	
	/** Map size Y field name. */
	String   F_MAP_SIZE_Y               = "mapSizeY";
	
	/** Max colors field name. */
	String   F_MAX_COLORS               = "maxColors";
	
	/** Max controls field name. */
	String   F_MAX_CONTROLS             = "maxControls";
	
	/** Max observers field name. */
	String   F_MAX_OBSERVERS            = "maxObservers";
	
	/** Max players field name. */
	String   F_MAX_PLAYERS              = "maxPlayers";
	
	/** Max races field name. */
	String   F_MAX_RACES                = "maxRaces";
	
	/** Max teams field name. */
	String   F_MAX_TEAMS                = "maxTeams";
	
	/** Max users field name. */
	String   F_MAX_USERS                = "maxUsers";
	
	/** Mod file sync checksum field name. */
	String   F_MOD_FILE_SYNC_CHECKSUM   = "modFileSyncChecksum";
	
	/** Random value field name. */
	String   F_RANDOM_VALUE             = "randomValue";
	
	/** Slot descriptions field name. */
	String   F_SLOT_DESCRIPTIONS        = "slotDescriptions";
	
	
	/**
	 * Returns the defaultAiBuild.
	 * 
	 * @return the defaultAiBuild
	 */
	Integer getDefaultAiBuild();
	
	/**
	 * Returns the defaultDifficulty.
	 * 
	 * @return the defaultDifficulty
	 */
	Integer getDefaultDifficulty();
	
	/**
	 * Returns the gameCacheName.
	 * 
	 * @return the gameCacheName
	 */
	String getGameCacheName();
	
	/**
	 * Returns the gameSpeed.
	 * 
	 * @return the gameSpeed
	 */
	Integer getGameSpeedInt();
	
	/**
	 * Returns the gameType.
	 * 
	 * @return the gameType
	 */
	Integer getGameType();
	
	/**
	 * Returns the isBlizzardMap.
	 * 
	 * @return the isBlizzardMap
	 */
	Boolean getIsBlizzardMap();
	
	/**
	 * Returns the isCoopMode.
	 * 
	 * @return the isCoopMode
	 */
	Boolean getIsCoopMode();
	
	/**
	 * Returns the isPremadeFfa.
	 * 
	 * @return the isPremadeFfa
	 */
	Boolean getIsPremadeFfa();
	
	/**
	 * Returns the mapAuthorName.
	 * 
	 * @return the mapAuthorName
	 */
	String getMapAuthorName();
	
	/**
	 * Returns the mapFileName.
	 * 
	 * @return the mapFileName
	 */
	String getMapFileName();
	
	/**
	 * Returns the mapFileChecksum.
	 * 
	 * @return the mapFileChecksum
	 */
	Long getMapFileChecksum();
	
	/**
	 * Returns the mapSizeX.
	 * 
	 * @return the mapSizeX
	 */
	Integer getMapSizeX();
	
	/**
	 * Returns the mapSizeY.
	 * 
	 * @return the mapSizeY
	 */
	Integer getMapSizeY();
	
	/**
	 * Returns the maxColors.
	 * 
	 * @return the maxColors
	 */
	Integer getMaxColors();
	
	/**
	 * Returns the maxControls.
	 * 
	 * @return the maxControls
	 */
	Integer getMaxControls();
	
	/**
	 * Returns the maxObservers.
	 * 
	 * @return the maxObservers
	 */
	Integer getMaxObservers();
	
	/**
	 * Returns the maxPlayers.
	 * 
	 * @return the maxPlayers
	 */
	Integer getMaxPlayers();
	
	/**
	 * Returns the maxRaces.
	 * 
	 * @return the maxRaces
	 */
	Integer getMaxRaces();
	
	/**
	 * Returns the maxTeams.
	 * 
	 * @return the maxTeams
	 */
	Integer getMaxTeams();
	
	/**
	 * Returns the maxUsers.
	 * 
	 * @return the maxUsers
	 */
	Integer getMaxUsers();
	
	/**
	 * Returns the modFileSyncChecksum.
	 * 
	 * @return the modFileSyncChecksum
	 */
	Integer getModFileSyncChecksum();
	
	/**
	 * Returns the randomValue.
	 * 
	 * @return the randomValue
	 */
	Integer getRandomValue();
	
	/**
	 * Returns the slotDescriptions.
	 * 
	 * @return the slotDescriptions
	 */
	ISlotDescription[] getSlotDescriptions();
	
	/**
	 * Returns the advancedSharedControls.
	 * 
	 * @return the advancedSharedControls
	 */
	Boolean getAdvancedSharedControls();
	
	/**
	 * Returns the amm.
	 * 
	 * @return the amm
	 */
	Boolean getAmm();
	
	/**
	 * Returns the battleNet.
	 * 
	 * @return the battleNet
	 */
	Boolean getBattleNet();
	
	/**
	 * Returns the clientDebugFlags.
	 * 
	 * @return the clientDebugFlags
	 */
	Integer getClientDebugFlags();
	
	/**
	 * Returns the competitive field value. Competitive means either ranked or unranked game.<br>
	 * Present in base versions from 24674, replaces {@link #P_RANKED}.
	 * 
	 * @return the competitive field value
	 * @see #getCompetitive()
	 * @see #getCompetitiveOrRanked()
	 */
	Boolean getCompetitive();
	
	/**
	 * Returns the ranked field value. Ranked means "ladder" game.<br>
	 * Present only up to base build 23260, missing starting from 24674 (replaced by {@link #P_COMPETITIVE}).
	 * 
	 * @return the ranked field value
	 * @see #getRanked()
	 * @see #getCompetitiveOrRanked()
	 */
	Boolean getRanked();
	
	/**
	 * Returns the competitive or ranked field, whichever exists.
	 * 
	 * @return the competitive or ranked field, whichever exists
	 */
	Boolean getCompetitiveOrRanked();
	
	/**
	 * Returns the fog.
	 * 
	 * @return the fog
	 */
	Integer getFog();
	
	/**
	 * Returns the lockTeams.
	 * 
	 * @return the lockTeams
	 */
	Boolean getLockTeams();
	
	/**
	 * Returns the noVictoryDefeat.
	 * 
	 * @return the noVictoryDefeat
	 */
	Boolean getNoVictoryDefeat();
	
	/**
	 * Returns the observers.
	 * 
	 * @return the observers
	 */
	Integer getObservers();
	
	/**
	 * Returns the randomRaces.
	 * 
	 * @return the randomRaces
	 */
	Boolean getRandomRaces();
	
	/**
	 * Returns the teamsTogether.
	 * 
	 * @return the teamsTogether
	 */
	Boolean getTeamsTogether();
	
	/**
	 * Returns the userDifficulty.
	 * 
	 * @return the userDifficulty
	 */
	Integer getUserDifficulty();
	
	/**
	 * Returns the game speed.
	 * 
	 * @return the game speed
	 */
	IGameSpeed getGameSpeed();
	
	/**
	 * Returns the expansion level.
	 * 
	 * @return the expansion level
	 */
	IExpansionLevel getExpansionLevel();
	
	/**
	 * Returns the region.
	 * 
	 * @return the region
	 */
	IRegion getRegion();
	
	/**
	 * Returns the cache handles.
	 * 
	 * @return the cache handles
	 */
	ICacheHandle[] getCacheHandles();
	
}
