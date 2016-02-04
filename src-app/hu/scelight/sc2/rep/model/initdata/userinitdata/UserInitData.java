/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.sc2.rep.model.initdata.userinitdata;

import hu.belicza.andras.util.StructView;
import hu.belicza.andras.util.type.XString;
import hu.scelight.sc2.rep.model.details.Details;
import hu.scelight.sc2.rep.model.initdata.gamedesc.CacheHandle;
import hu.scelightapi.sc2.rep.model.initdata.userinitdata.IUserInitData;

import java.util.Map;

/**
 * User init data.
 * 
 * @author Andras Belicza
 */
public class UserInitData extends StructView implements IUserInitData {
	
	// Eagerly initialized "cached" values
	
	
	// Lazily initialized "cached" values
	
	/** Associated player id for this user init data (in {@link Details#getPlayerList()}) */
	public final int              playerId;
	
	/** Clan tag. */
	public final String           clanTag;
	
	/** Name. */
	public final String           name;
	
	/** Full Name (with clan tag included). */
	public final String           fullName;
	
	/** The highest league. */
	private League                highestLeague;
	
	/** Cache handle of the clan logo. */
	private CacheHandle           clanLogo;
	
	
	/**
	 * Creates a new {@link UserInitData}.
	 * 
	 * @param struct user init data data structure
	 * @param playerId associated player id for this slot (in {@link Details#getPlayerList()})
	 */
	public UserInitData( final Map< String, Object > struct, final int playerId ) {
		super( struct );
		
		this.playerId = playerId;
		
		final XString xs = get( F_CLAN_TAG );
		clanTag = xs == null || xs.array.length == 0 ? null : xs.toString();
		name = get( F_NAME ).toString();
		fullName = clanTag == null ? name : '[' + clanTag + ']' + name;
	}
	
	@Override
	public int getPlayerId() {
		return playerId;
	}
	
	@Override
	public String getClanTag() {
		return clanTag;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public String getFullName() {
		return fullName;
	}
	
	@Override
	public CacheHandle getClanLogo() {
		// Clan logo is a cache handle
		if ( clanLogo == null ) {
			final XString cacheHandle = get( F_CLAN_LOGO );
			if ( cacheHandle != null )
				clanLogo = new CacheHandle( cacheHandle );
		}
		
		return clanLogo;
	}
	
	@Override
	public Integer getCombinedRaceLevels() {
		return get( F_COMBINED_RACE_LEVELS );
	}
	
	@Override
	public Boolean getCustomInterface() {
		return get( F_CUSTOM_INTERFACE );
	}
	
	@Override
	public Boolean getExamine() {
		return get( F_EXAMINE );
	}
	
	@Override
	public Integer getHighestLeagueId() {
		return get( F_HIGHEST_LEAGUE );
	}
	
	@Override
	public Integer getObserve() {
		return get( F_OBSERVE );
	}
	
	@Override
	public Integer getRacePreference() {
		return get( P_RACE_PREFERENCE_RACE );
	}
	
	@Override
	public Integer getRandomSeed() {
		return get( F_RANDOM_SEED );
	}
	
	@Override
	public Integer getTeamPreference() {
		return get( P_TEAM_PREFERENCE_TEAM );
	}
	
	@Override
	public Boolean getTestAuto() {
		return get( F_TEST_AUTO );
	}
	
	@Override
	public Boolean getTestMap() {
		return get( F_TEST_MAP );
	}
	
	@Override
	public League getHighestLeague() {
		if ( highestLeague == null ) {
			final Integer highestLeagueId = getHighestLeagueId();
			highestLeague = highestLeagueId == null ? League.UNKNOWN : League.VALUES[ highestLeagueId ];
		}
		
		return highestLeague;
	}
	
}
