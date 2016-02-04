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
import hu.scelightapi.sc2.rep.model.trackerevents.IUnitDoneEvent;

import java.util.Map;

/**
 * Unit done tracker event: see {@link UnitInitEvent}.
 * 
 * @author Andras Belicza
 * 
 * @see UnitInitEvent
 */
public class UnitDoneEvent extends Event implements IUnitDoneEvent {
	
	// TODO
	
	/**
	 * Creates a new {@link UnitDoneEvent}.
	 * 
	 * @param struct event data structure
	 * @param id id of the event
	 * @param name type name of the event
	 * @param loop game loop when the event occurred
	 * @param userId user id causing the event
	 */
	public UnitDoneEvent( final Map< String, Object > struct, final int id, final String name, final int loop, final int userId ) {
		super( struct, id, name, loop, userId );
	}
	
}
