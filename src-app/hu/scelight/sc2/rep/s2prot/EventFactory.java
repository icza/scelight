/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.sc2.rep.s2prot;

import java.util.Map;

/**
 * Base event factory.
 * 
 * @author Andras Belicza
 */
public class EventFactory {
	
	/**
	 * Creates a new {@link Event}.
	 * <p>
	 * This base implementation simply returns a new {@link Event}. Specific event factories should override this method and call this implementation as a
	 * fall-back if they can't / don't want to create a special event instance.
	 * </p>
	 * 
	 * @param struct event data structure
	 * @param id id of the event
	 * @param name type name of the event
	 * @param loop game loop when the event occurred
	 * @param userId user id causing the event
	 * @return the created event
	 */
	public Event create( final Map< String, Object > struct, final int id, final String name, final int loop, final int userId ) {
		return new Event( struct, id, name, loop, userId );
	}
	
}
