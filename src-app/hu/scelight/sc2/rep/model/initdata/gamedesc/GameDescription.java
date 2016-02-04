/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.sc2.rep.model.initdata.gamedesc;

import hu.belicza.andras.util.StructView;
import hu.belicza.andras.util.type.XString;
import hu.scelightapi.sc2.rep.model.initdata.gamedesc.ICacheHandle;
import hu.scelightapi.sc2.rep.model.initdata.gamedesc.IGameDescription;

import java.util.Map;

/**
 * Game description.
 * 
 * @author Andras Belicza
 */
public class GameDescription extends StructView implements IGameDescription {
	
	// Eagerly initialized "cached" values
	
	/** Cache handles. */
	public final CacheHandle[] cacheHandles;
	
	/** Map size X. */
	public final Integer       mapSizeX;
	
	/** Map size X. */
	public final Integer       mapSizeY;
	
	
	// Lazily initialized "cached" values
	
	/** Slot descriptions. */
	private SlotDescription[]  slotDescriptions;
	
	/** Game speed. */
	private GameSpeed          gameSpeed;
	
	/** Expansion level. */
	private ExpansionLevel     expansionLevel;
	
	/** Region. */
	private Region             region;
	
	
	/**
	 * Creates a new {@link GameDescription}.
	 * 
	 * @param struct game description data structure
	 */
	public GameDescription( final Map< String, Object > struct ) {
		super( struct );
		
		final XString[] arr = get( F_CACHE_HANDLES );
		cacheHandles = new CacheHandle[ arr.length ];
		for ( int i = arr.length - 1; i >= 0; i-- )
			cacheHandles[ i ] = new CacheHandle( arr[ i ] );
		
		mapSizeX = get( F_MAP_SIZE_X );
		mapSizeY = get( F_MAP_SIZE_Y );
	}
	
	@Override
	public Integer getDefaultAiBuild() {
		return get( F_DEFAULT_AI_BUILD );
	}
	
	@Override
	public Integer getDefaultDifficulty() {
		return get( F_DEFAULT_DIFFICULTY );
	}
	
	@Override
	public String getGameCacheName() {
		return get( F_GAME_CACHE_NAME ).toString();
	}
	
	@Override
	public Integer getGameSpeedInt() {
		return get( F_GAME_SPEED );
	}
	
	@Override
	public Integer getGameType() {
		return get( F_GAME_TYPE );
	}
	
	@Override
	public Boolean getIsBlizzardMap() {
		return get( F_IS_BLIZZARD_MAP );
	}
	
	@Override
	public Boolean getIsCoopMode() {
		return get( F_IS_COOP_MODE );
	}
	
	@Override
	public Boolean getIsPremadeFfa() {
		return get( F_IS_PREMADE_FFA );
	}
	
	@Override
	public String getMapAuthorName() {
		return get( F_MAP_AUTHOR_NAME ).toString();
	}
	
	@Override
	public String getMapFileName() {
		return get( F_MAP_FILE_NAME ).toString();
	}
	
	@Override
	public Long getMapFileChecksum() {
		return get( F_MAP_FILE_CHECKSUM );
	}
	
	@Override
	public Integer getMapSizeX() {
		return mapSizeX;
	}
	
	@Override
	public Integer getMapSizeY() {
		return mapSizeY;
	}
	
	@Override
	public Integer getMaxColors() {
		return get( F_MAX_COLORS );
	}
	
	@Override
	public Integer getMaxControls() {
		return get( F_MAX_CONTROLS );
	}
	
	@Override
	public Integer getMaxObservers() {
		return get( F_MAX_OBSERVERS );
	}
	
	@Override
	public Integer getMaxPlayers() {
		return get( F_MAX_PLAYERS );
	}
	
	@Override
	public Integer getMaxRaces() {
		return get( F_MAX_RACES );
	}
	
	@Override
	public Integer getMaxTeams() {
		return get( F_MAX_TEAMS );
	}
	
	@Override
	public Integer getMaxUsers() {
		return get( F_MAX_USERS );
	}
	
	@Override
	public Integer getModFileSyncChecksum() {
		return get( F_MOD_FILE_SYNC_CHECKSUM );
	}
	
	@Override
	public Integer getRandomValue() {
		return get( F_RANDOM_VALUE );
	}
	
	@Override
	public SlotDescription[] getSlotDescriptions() {
		if ( slotDescriptions == null ) {
			final Map< String, Object >[] array = get( F_SLOT_DESCRIPTIONS );
			
			slotDescriptions = new SlotDescription[ array.length ];
			for ( int i = array.length - 1; i >= 0; i-- )
				slotDescriptions[ i ] = new SlotDescription( array[ i ] );
		}
		
		return slotDescriptions;
	}
	
	@Override
	public Boolean getAdvancedSharedControls() {
		return get( P_ADVANCED_SHARED_CONTROLS );
	}
	
	@Override
	public Boolean getAmm() {
		return get( P_AMM );
	}
	
	@Override
	public Boolean getBattleNet() {
		return get( P_BATTLE_NET );
	}
	
	@Override
	public Integer getClientDebugFlags() {
		return get( P_CLIENT_DEBUG_FLAGS );
	}
	
	@Override
	public Boolean getCompetitive() {
		return get( P_COMPETITIVE );
	}
	
	@Override
	public Boolean getRanked() {
		return get( P_RANKED );
	}
	
	@Override
	public Boolean getCompetitiveOrRanked() {
		final Boolean competitive = get( P_COMPETITIVE );
		return competitive == null ? this.< Boolean > get( P_RANKED ) : competitive;
	}
	
	@Override
	public Integer getFog() {
		return get( P_FOG );
	}
	
	@Override
	public Boolean getLockTeams() {
		return get( P_LOCK_TEAMS );
	}
	
	@Override
	public Boolean getNoVictoryDefeat() {
		return get( P_NO_VICTORY_DEFEAT );
	}
	
	@Override
	public Integer getObservers() {
		return get( P_OBSERVERS );
	}
	
	@Override
	public Boolean getRandomRaces() {
		return get( P_RANDOM_RACES );
	}
	
	@Override
	public Boolean getTeamsTogether() {
		return get( P_TEAMS_TOGETHER );
	}
	
	@Override
	public Integer getUserDifficulty() {
		return get( P_USER_DIFFICULTY );
	}
	
	@Override
	public GameSpeed getGameSpeed() {
		if ( gameSpeed == null ) {
			gameSpeed = GameSpeed.VALUES[ getGameSpeedInt() ];
		}
		
		return gameSpeed;
	}
	
	@Override
	public ExpansionLevel getExpansionLevel() {
		if ( expansionLevel == null ) {
			expLevCycle:
			for ( final ExpansionLevel el : ExpansionLevel.VALUES )
				for ( final CacheHandle handle : cacheHandles )
					if ( handle.contentDigest.equals( el.cacheHandleDigest ) ) {
						expansionLevel = el;
						break expLevCycle;
					}
			if ( expansionLevel == null )
				expansionLevel = ExpansionLevel.UNKNOWN;
		}
		
		return expansionLevel;
	}
	
	@Override
	public Region getRegion() {
		if ( region == null )
			region = cacheHandles.length == 0 ? Region.UNKNOWN : Region.fromCode( cacheHandles[ 0 ].regionCode );
		
		return region;
	}
	
	@Override
	public ICacheHandle[] getCacheHandles() {
		return cacheHandles;
	}
	
}
