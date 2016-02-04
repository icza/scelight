/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.sc2.rep.model.trackerevents;

import static hu.scelightapi.sc2.rep.model.trackerevents.ITrackerEvents.ID_PLAYER_SETUP;
import static hu.scelightapi.sc2.rep.model.trackerevents.ITrackerEvents.ID_PLAYER_STATS;
import static hu.scelightapi.sc2.rep.model.trackerevents.ITrackerEvents.ID_UNIT_BORN;
import static hu.scelightapi.sc2.rep.model.trackerevents.ITrackerEvents.ID_UNIT_DONE;
import static hu.scelightapi.sc2.rep.model.trackerevents.ITrackerEvents.ID_UNIT_INIT;
import static hu.scelightapi.sc2.rep.model.trackerevents.ITrackerEvents.ID_UPGRADE;
import hu.scelight.sc2.rep.model.Replay;
import hu.scelight.sc2.rep.s2prot.Event;
import hu.scelight.sc2.rep.s2prot.EventFactory;

import java.util.Map;

/**
 * Event factory that produces events from the tracker events stream data structures.
 * 
 * @author Andras Belicza
 */
public class TrackerEventFactory extends EventFactory {
	
	/** Base build of the replay being parsed. */
	private final int baseBuild;
	
	/**
	 * Creates a new {@link TrackerEventFactory}.
	 * 
	 * @param replay reference to the {@link Replay} being parsed, source for optionally required more information
	 */
	public TrackerEventFactory( final Replay replay ) {
		baseBuild = replay.header.baseBuild;
	}
	
	
	@Override
	public Event create( final Map< String, Object > struct, final int id, final String name, final int loop, final int userId ) {
		switch ( id ) {
			case ID_PLAYER_STATS :
				return new PlayerStatsEvent( struct, id, name, loop, userId );
			case ID_UNIT_BORN :
				return new UnitBornEvent( struct, id, name, loop, userId, baseBuild );
			case ID_UPGRADE :
				return new UpgradeEvent( struct, id, name, loop, userId );
			case ID_UNIT_INIT :
				return new UnitInitEvent( struct, id, name, loop, userId, baseBuild );
			case ID_UNIT_DONE :
				return new UnitDoneEvent( struct, id, name, loop, userId );
			case ID_PLAYER_SETUP :
				return new PlayerSetupEvent( struct, id, name, loop, userId );
		}
		
		return super.create( struct, id, name, loop, userId );
	}
	
}
