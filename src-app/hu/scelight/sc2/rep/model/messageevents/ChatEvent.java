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
import hu.scelightapi.sc2.rep.model.messageevents.IChatEvent;
import hu.sllauncher.gui.icon.LRIcon;

import java.util.Map;

/**
 * Chat message event.
 * 
 * @author Andras Belicza
 */
public class ChatEvent extends MsgEvent implements IChatEvent {
	
	/** RIcon of the chat event. */
	public static final LRIcon RICON = Icons.F_BALLOON;
	
	
	/**
	 * Creates a new {@link ChatEvent}.
	 * 
	 * @param struct event data structure
	 * @param id id of the event
	 * @param name type name of the event
	 * @param loop game loop when the event occurred
	 * @param userId user id causing the event
	 */
	public ChatEvent( final Map< String, Object > struct, final int id, final String name, final int loop, final int userId ) {
		super( struct, id, name, loop, userId );
	}
	
	@Override
	public String getText() {
		return get( F_STRING ).toString();
	}
	
	@Override
	public LRIcon getRicon() {
		return RICON;
	}
	
}
