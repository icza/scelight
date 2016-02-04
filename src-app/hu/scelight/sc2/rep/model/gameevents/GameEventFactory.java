/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.sc2.rep.model.gameevents;

import static hu.scelightapi.sc2.rep.model.gameevents.IGameEvents.ID_CAMERA_SAVE;
import static hu.scelightapi.sc2.rep.model.gameevents.IGameEvents.ID_CAMERA_UPDATE;
import static hu.scelightapi.sc2.rep.model.gameevents.IGameEvents.ID_CMD;
import static hu.scelightapi.sc2.rep.model.gameevents.IGameEvents.ID_CONTROL_GROUP_UPDATE;
import static hu.scelightapi.sc2.rep.model.gameevents.IGameEvents.ID_GAME_USER_LEAVE;
import static hu.scelightapi.sc2.rep.model.gameevents.IGameEvents.ID_PLAYER_LEAVE;
import static hu.scelightapi.sc2.rep.model.gameevents.IGameEvents.ID_RESOURCE_REQUEST;
import static hu.scelightapi.sc2.rep.model.gameevents.IGameEvents.ID_RESOURCE_REQUEST_FULFILL;
import static hu.scelightapi.sc2.rep.model.gameevents.IGameEvents.ID_RESOURCE_TRADE;
import static hu.scelightapi.sc2.rep.model.gameevents.IGameEvents.ID_SELECTION_DELTA;
import hu.scelight.sc2.balancedata.BalanceData;
import hu.scelight.sc2.rep.model.Replay;
import hu.scelight.sc2.rep.model.gameevents.camera.CameraSaveEvent;
import hu.scelight.sc2.rep.model.gameevents.camera.CameraUpdateEvent;
import hu.scelight.sc2.rep.model.gameevents.cmd.CmdEvent;
import hu.scelight.sc2.rep.model.gameevents.selectiondelta.SelectionDeltaEvent;
import hu.scelight.sc2.rep.s2prot.Event;
import hu.scelight.sc2.rep.s2prot.EventFactory;

import java.util.Map;

/**
 * Event factory that produces events from the game events stream data structures.
 * 
 * @author Andras Belicza
 */
public class GameEventFactory extends EventFactory {
	
	/** Balance data of the game / replay; required to interpret cmd events. */
	private final BalanceData balanceData;
	
	/** Base build of the replay being parsed. */
	private final int         baseBuild;
	
	/**
	 * Creates a new {@link GameEventFactory}.
	 * 
	 * @param replay reference to the {@link Replay} being parsed, source for optionally required more information
	 */
	public GameEventFactory( final Replay replay ) {
		this.balanceData = replay.getBalanceData();
		baseBuild = replay.header.baseBuild;
	}
	
	
	@Override
	public Event create( final Map< String, Object > struct, final int id, final String name, final int loop, final int userId ) {
		switch ( id ) {
			case ID_CAMERA_UPDATE :
				return new CameraUpdateEvent( struct, id, name, loop, userId );
			case ID_SELECTION_DELTA :
				return new SelectionDeltaEvent( struct, id, name, loop, userId, baseBuild );
			case ID_CMD :
				return new CmdEvent( struct, id, name, loop, userId, baseBuild, balanceData );
			case ID_CONTROL_GROUP_UPDATE :
				return new ControlGroupUpdateEvent( struct, id, name, loop, userId );
			case ID_CAMERA_SAVE :
				return new CameraSaveEvent( struct, id, name, loop, userId );
			case ID_RESOURCE_TRADE :
				return new ResourceTradeEvent( struct, id, name, loop, userId );
			case ID_RESOURCE_REQUEST :
				return new ResourceRequestEvent( struct, id, name, loop, userId );
			case ID_RESOURCE_REQUEST_FULFILL :
				return new ResourceRequestFulfillEvent( struct, id, name, loop, userId );
			case ID_GAME_USER_LEAVE :
				return new GameUserLeaveEvent( struct, id, name, loop, userId );
			case ID_PLAYER_LEAVE :
				return new PlayerLeaveEvent( struct, id, name, loop, userId );
		}
		
		return super.create( struct, id, name, loop, userId );
	}
	
}
