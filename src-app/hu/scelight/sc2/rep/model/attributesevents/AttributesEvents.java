/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.sc2.rep.model.attributesevents;

import hu.belicza.andras.util.StructView;
import hu.scelight.sc2.rep.s2prot.type.Attribute;
import hu.scelight.util.Utils;
import hu.scelightapi.sc2.rep.model.attributesevents.IAttribute;
import hu.scelightapi.sc2.rep.model.attributesevents.IAttributesEvents;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * StarCraft II Replay attributes events.
 * 
 * @author Andras Belicza
 */
public class AttributesEvents extends StructView implements IAttributesEvents {
	
	/** Map holding attribute names mapped from their ids. */
	private static final HashMap< Integer, String >        SCOPE_NAME_MAP   = new HashMap<>();
	static {
		SCOPE_NAME_MAP.put( S_GLOBAL, "global" );
		for ( int i = 1; i < 16; i++ )
			SCOPE_NAME_MAP.put( i, "player" + i );
	}
	
	
	/** Map holding attribute names mapped from their ids. */
	private static final HashMap< Integer, String >        ATTR_ID_NAME_MAP = new HashMap<>();
	static {
		final HashMap< Integer, String > M = ATTR_ID_NAME_MAP;
		M.put( A_CONTROLLER, "controller" );
		
		M.put( A_RULES, "rules" );
		M.put( A_IS_PREMADE_GAME, "isPremadeGame" );
		M.put( A_PARTIES_PRIVATE, "partiesPrivate" );
		M.put( A_PARTIES_PREMADE, "partiesPremade" );
		M.put( A_GAME_SPEED, "gameSpeed" );
		M.put( A_LOBBY_DELAY, "lobbyDelay" );
		M.put( A_GAME_MODE, "gameMode" );
		M.put( A_PRIVACY_OPTION, "privacyOption" );
		M.put( A_LOCKED_ALLIANCES, "lockedAlliances" );
		M.put( A_PARTIES_PREMADE_1V1, "partiesPremade1v1" );
		M.put( A_PARTIES_PREMADE_2V2, "partiesPremade2v2" );
		M.put( A_PARTIES_PREMADE_3V3, "partiesPremade3v3" );
		M.put( A_PARTIES_PREMADE_4V4, "partiesPremade4v4" );
		M.put( A_PARTIES_PREMADE_FFA, "partiesPremadeFfa" );
		M.put( A_PARTIES_PREMADE_5V5, "partiesPremade5v5" );
		M.put( A_PARTIES_PREMADE_6V6, "partiesPremade6v6" );
		M.put( A_PARTIES_PRIVATE_ONE, "partiesPrivateOne" );
		M.put( A_PARTIES_PRIVATE_TWO, "partiesPrivateTwo" );
		M.put( A_PARTIES_PRIVATE_THREE, "partiesPrivateThree" );
		M.put( A_PARTIES_PRIVATE_FOUR, "partiesPrivateFour" );
		M.put( A_PARTIES_PRIVATE_FIVE, "partiesPrivateFive" );
		M.put( A_PARTIES_PRIVATE_SIX, "partiesPrivateSix" );
		M.put( A_PARTIES_PRIVATE_SEVEN, "partiesPrivateSeven" );
		M.put( A_PARTIES_PRIVATE_FFA, "partiesPrivateFfa" );
		M.put( A_PARTIES_PRIVATE_CUSTOM, "partiesPrivateCustom" );
		M.put( A_PARTIES_PRIVATE_EIGHT, "partiesPrivateEight" );
		M.put( A_PARTIES_PRIVATE_NINE, "partiesPrivateNine" );
		M.put( A_PARTIES_PRIVATE_TEN, "partiesPrivateTen" );
		M.put( A_PARTIES_PRIVATE_ELEVEN, "partiesPrivateEleven" );
		M.put( A_RACE, "race" );
		M.put( A_PARTY_COLOR, "partyColor" );
		M.put( A_HANDICAP, "handicap" );
		M.put( A_AI_SKILL, "aiSkill" );
		M.put( A_AI_RACE, "aiRace" );
		M.put( A_PARTICIPANT_ROLE, "participantRole" );
		M.put( A_WATCHER_TYPE, "watcherType" );
		for ( int i = A_AI_BUILD_FIRST; i < A_AI_BUILD_LAST; i++ )
			M.put( i, "aiBuild" + ( i - A_AI_BUILD_FIRST ) );
		M.put( A_USING_CUSTOM_OBSERVER_UI, "customObserverUi" );
	}
	
	
	// Eagerly initialized "cached" values
	
	/** Scopes. */
	public final Map< Integer, Map< Integer, Attribute > > scopes;
	
	
	// Lazily initialized "cached" values
	
	/** Game mode. */
	private GameMode                                       gameMode;
	
	
	// Other attributes.
	
	/** Cached converted, display friendly string-map source structure. */
	protected Map< String, Object >                        stringStruct;
	
	
	/**
	 * Creates a new {@link AttributesEvents}.
	 * 
	 * @param struct attributes events data structure
	 */
	public AttributesEvents( final Map< String, Object > struct ) {
		super( struct );
		
		scopes = get( F_SCOPES );
	}
	
	@Override
	public Integer getSource() {
		return get( F_SOURCE );
	}
	
	@Override
	public Integer getMapNamespace() {
		return get( F_MAP_NAMESPACE );
	}
	
	@Override
	@SuppressWarnings( "unchecked" )
	public Map< Integer, Map< Integer, ? extends IAttribute > > getScopes() {
		return (Map< Integer, Map< Integer, ? extends IAttribute > >) (Object) scopes;
	}
	
	@Override
	public GameMode getGameMode() {
		if ( gameMode == null ) {
			GameMode gameMode_ = GameMode.UNKNOWN;
			if ( scopes != null ) {
				final Map< Integer, Attribute > glScope = scopes.get( S_GLOBAL );
				if ( glScope != null ) {
					final Attribute attribute = scopes.get( S_GLOBAL ).get( A_GAME_MODE );
					if ( attribute != null )
						gameMode_ = GameMode.fromValue( attribute.value );
				}
			}
			gameMode = gameMode_;
		}
		
		return gameMode;
	}
	
	
	@Override
	public Map< String, Object > getStringStruct() {
		if ( stringStruct == null ) {
			stringStruct = new HashMap<>( struct );
			if ( scopes == null )
				return stringStruct;
			
			// Replace the scopes structure
			final Map< String, Map< String, Attribute > > stringScopes = Utils.newHashMap( scopes.size() );
			for ( final Entry< Integer, Map< Integer, Attribute > > scopeEntry : scopes.entrySet() ) {
				
				final Map< String, Attribute > stringScope = Utils.newHashMap( scopeEntry.getValue().size() );
				
				for ( final Entry< Integer, Attribute > attrEntry : scopeEntry.getValue().entrySet() ) {
					final String attrName = ATTR_ID_NAME_MAP.get( attrEntry.getKey() );
					stringScope.put( attrName == null ? Integer.toString( attrEntry.getKey() ) : attrName, attrEntry.getValue() );
				}
				
				final String scopeName = AttributesEvents.SCOPE_NAME_MAP.get( scopeEntry.getKey() );
				stringScopes.put( scopeName == null ? Integer.toString( scopeEntry.getKey() ) : scopeName, stringScope );
			}
			
			stringStruct.put( F_SCOPES, stringScopes );
		}
		
		return stringStruct;
	}
	
}
