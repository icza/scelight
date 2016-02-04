/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.sc2.rep.model.initdata.lobbystate;

import hu.belicza.andras.util.StructView;
import hu.scelightapi.sc2.rep.model.initdata.lobbystate.ILobbyState;

import java.util.Map;

/**
 * Lobby state.
 * 
 * @author Andras Belicza
 */
public class LobbyState extends StructView implements ILobbyState {
	
	// Eagerly initialized "cached" values
	
	
	// Lazily initialized "cached" values
	
	/** Slots. */
	private Slot[] slots;
	
	
	/**
	 * Creates a new {@link LobbyState}.
	 * 
	 * @param struct lobby state data structure
	 */
	public LobbyState( final Map< String, Object > struct ) {
		super( struct );
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
	public Integer getGameDuration() {
		return get( F_GAME_DURATION );
	}
	
	@Override
	public Integer getHostUserId() {
		return get( F_HOST_USER_ID );
	}
	
	@Override
	public Boolean getIsSinglePlayer() {
		return get( F_IS_SINGLE_PLAYER );
	}
	
	@Override
	public Integer getMaxObservers() {
		return get( F_MAX_OBSERVERS );
	}
	
	@Override
	public Integer getMaxUsers() {
		return get( F_MAX_USERS );
	}
	
	@Override
	public Integer getPhase() {
		return get( F_PHASE );
	}
	
	@Override
	public Integer getRandomSeed() {
		return get( F_RANDOM_SEED );
	}
	
	@Override
	public Slot[] getSlots() {
		if ( slots == null ) {
			final Map< String, Object >[] array = get( F_SLOTS );
			
			slots = new Slot[ array.length ];
			for ( int i = array.length - 1; i >= 0; i-- )
				slots[ i ] = new Slot( array[ i ] );
		}
		
		return slots;
	}
	
}
