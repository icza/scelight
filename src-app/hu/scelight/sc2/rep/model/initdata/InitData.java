/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.sc2.rep.model.initdata;

import hu.belicza.andras.util.StructView;
import hu.scelight.sc2.rep.model.initdata.gamedesc.GameDescription;
import hu.scelight.sc2.rep.model.initdata.lobbystate.Controller;
import hu.scelight.sc2.rep.model.initdata.lobbystate.LobbyState;
import hu.scelight.sc2.rep.model.initdata.lobbystate.Slot;
import hu.scelight.sc2.rep.model.initdata.userinitdata.UserInitData;
import hu.scelightapi.sc2.rep.model.initdata.IInitData;

import java.util.Map;

/**
 * StarCraft II Replay init data.
 * 
 * @author Andras Belicza
 */
public class InitData extends StructView implements IInitData {
	
	/** Sync lobby state field name. */
	private static final String F_SYNC_LOBBY_STATE = "syncLobbyState";
	
	
	// Eagerly initialized "cached" values
	
	
	// Lazily initialized "cached" values
	
	/** User init datas. */
	private UserInitData[]      userInitDatas;
	
	/** Game description. */
	private GameDescription     gameDescription;
	
	/** Lobby state. */
	private LobbyState          lobbyState;
	
	
	/**
	 * Creates a new {@link InitData}.
	 * 
	 * @param struct init data data structure
	 */
	@SuppressWarnings( "unchecked" )
	public InitData( final Map< String, Object > struct ) {
		// Init data is a structure with 1 field only which is a structure. Use that as the root structure.
		super( (Map< String, Object >) struct.get( F_SYNC_LOBBY_STATE ) );
	}
	
	@Override
	public UserInitData[] getUserInitDatas() {
		if ( userInitDatas == null ) {
			final Map< String, Object >[] array = get( F_USER_INIT_DATA );
			
			userInitDatas = new UserInitData[ array.length ];
			
			// Manually determine playerIds for the slots
			final Slot[] slots = getLobbyState().getSlots();
			int playerId = 0;
			for ( int i = 0; i < array.length; i++ ) {
				final Controller controller = i < slots.length ? slots[ i ].getController() : Controller.CLOSED;
				userInitDatas[ i ] = new UserInitData( array[ i ], controller == Controller.HUMAN || controller == Controller.COMPUTER ? playerId++ : -1 );
			}
		}
		
		return userInitDatas;
	}
	
	@Override
	public GameDescription getGameDescription() {
		if ( gameDescription == null ) {
			gameDescription = new GameDescription( this.< Map< String, Object > > get( F_GAME_DESCRIPTION ) );
		}
		
		return gameDescription;
	}
	
	@Override
	public LobbyState getLobbyState() {
		if ( lobbyState == null ) {
			lobbyState = new LobbyState( this.< Map< String, Object > > get( F_LOBBY_STATE ) );
		}
		
		return lobbyState;
	}
	
}
