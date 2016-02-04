/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.sc2.rep.model.messageevents;

import hu.scelight.sc2.rep.s2prot.Event;
import hu.scelightapi.sc2.rep.model.messageevents.IMessageEvents;

import java.util.List;

/**
 * StarCraft II Replay message events.
 * 
 * @author Andras Belicza
 */
public class MessageEvents implements IMessageEvents {
	
	/** Message event list. */
	public final Event[] events;
	
	
	/**
	 * Creates a new {@link MessageEvents}.
	 * 
	 * @param eventList message event list
	 */
	public MessageEvents( final List< Event > eventList ) {
		this.events = eventList.toArray( new Event[ eventList.size() ] );
	}
	
	@Override
	public Event[] getEvents() {
		return events;
	}
	
}
