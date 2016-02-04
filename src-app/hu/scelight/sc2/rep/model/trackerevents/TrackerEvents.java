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

import hu.scelight.sc2.rep.s2prot.Event;
import hu.scelightapi.sc2.rep.model.IEvent;
import hu.scelightapi.sc2.rep.model.trackerevents.ITrackerEvents;

import java.util.List;

/**
 * StarCraft II Replay tracker events.
 * 
 * @author Andras Belicza
 */
public class TrackerEvents implements ITrackerEvents {
	
	/** Tracker event list. */
	public final Event[] events;
	
	/**
	 * Creates a new {@link TrackerEvents}.
	 * 
	 * @param eventList tracker event list
	 */
	public TrackerEvents( final List< Event > eventList ) {
		this.events = eventList.toArray( new Event[ eventList.size() ] );
	}
	
	@Override
	public IEvent[] getEvents() {
		return events;
	}
	
}
