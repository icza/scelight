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

import hu.scelightapi.sc2.rep.model.trackerevents.IUnitInitEvent;

import java.util.Map;

/**
 * Unit init tracker event: appears for units under construction. When complete a {@link UnitDoneEvent} appears with the same unit tag.
 * 
 * @author Andras Belicza
 * 
 * @see UnitDoneEvent
 */
public class UnitInitEvent extends BaseUnitEvent implements IUnitInitEvent {
	
	/**
	 * Creates a new {@link UnitInitEvent}.
	 * 
	 * @param struct event data structure
	 * @param id id of the event
	 * @param name type name of the event
	 * @param loop game loop when the event occurred
	 * @param userId user id causing the event
	 * @param baseBuild base build of the replay being parsed, there are resolution differences in tracker positions in different versions
	 */
	public UnitInitEvent( final Map< String, Object > struct, final int id, final String name, final int loop, final int userId, final int baseBuild ) {
		super( struct, id, name, loop, userId, baseBuild );
	}
	
}
