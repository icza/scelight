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

import hu.scelight.sc2.rep.s2prot.Event;
import hu.scelightapi.sc2.rep.model.gameevents.IGameEvents;

import java.util.List;

/**
 * StarCraft II Replay game events.
 * 
 * @author Andras Belicza
 */
public class GameEvents implements IGameEvents {
	
	/** Game event list. */
	public final Event[] events;
	
	/**
	 * Creates a new {@link GameEvents}.
	 * 
	 * @param eventList game event list
	 */
	public GameEvents( final List< Event > eventList ) {
		this.events = eventList.toArray( new Event[ eventList.size() ] );
	}
	
	@Override
	public Event[] getEvents() {
		return events;
	}
	
}
