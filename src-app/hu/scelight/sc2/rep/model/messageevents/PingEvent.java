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

import hu.scelight.gui.icon.Icons;
import hu.scelightapi.sc2.rep.model.messageevents.IPingEvent;
import hu.sllauncher.gui.icon.LRIcon;

import java.util.Map;

/**
 * Minimap ping message event.
 * 
 * @author Andras Belicza
 */
public class PingEvent extends MsgEvent implements IPingEvent {
	
	/** RIcon of the ping event. */
	public static final LRIcon RICON = Icons.F_MAP_PIN;
	
	
	/**
	 * Creates a new {@link PingEvent}.
	 * 
	 * @param struct event data structure
	 * @param id id of the event
	 * @param name type name of the event
	 * @param loop game loop when the event occurred
	 * @param userId user id causing the event
	 */
	public PingEvent( final Map< String, Object > struct, final int id, final String name, final int loop, final int userId ) {
		super( struct, id, name, loop, userId );
	}
	
	@Override
	public Integer getX() {
		return get( P_POINT_X );
	}
	
	@Override
	public Float getXFloat() {
		final Integer x = get( P_POINT_X );
		return x == null ? null : x / 8192.0f;
	}
	
	@Override
	public Integer getY() {
		return get( P_POINT_Y );
	}
	
	@Override
	public Float getYFloat() {
		final Integer y = get( P_POINT_Y );
		return y == null ? null : y / 8192.0f;
	}
	
	@Override
	public LRIcon getRicon() {
		return RICON;
	}
	
}
