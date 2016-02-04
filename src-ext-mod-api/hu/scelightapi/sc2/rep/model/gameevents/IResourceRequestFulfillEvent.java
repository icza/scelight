/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapi.sc2.rep.model.gameevents;

import hu.scelightapi.sc2.rep.model.IEvent;

/**
 * Resource request fulfill event (a player approving a resource request and sending the requested resources).
 * 
 * @author Andras Belicza
 */
public interface IResourceRequestFulfillEvent extends IEvent {
	
	/** Fulfill request id field name. */
	String F_FULFILL_REQUEST_ID = "fulfillRequestId";
	
	
	/**
	 * Returns the fulfill request id.
	 * 
	 * @return the fulfill request id
	 */
	Integer getFulfillRequestId();
	
}
