/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapi.sc2.rep.model.messageevents;

import hu.scelightapi.sc2.rep.model.IEvent;

/**
 * StarCraft II Replay message events.
 * 
 * @author Andras Belicza
 */
public interface IMessageEvents {
	
	/** Id of the Chat message event. */
	int ID_CHAT             = 0;
	
	/** Id of the Ping message event. */
	int ID_PING             = 1;
	
	/** Id of the Loading Progress message event. */
	int ID_LOADING_PROGRESS = 2;
	
	/** Id of the Server Ping message event. */
	int ID_SERVER_PING      = 3;
	
	
	/**
	 * Returns the array of message events.
	 * 
	 * @return the array of message events
	 */
	IEvent[] getEvents();
	
}
