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
 * Resource trade event (a player sending resources to another).
 * 
 * @author Andras Belicza
 */
public interface IResourceTradeEvent extends IEvent {
	
	/** Recipient id field name. */
	String F_RECIPIENT_ID = "recipientId";
	
	/** Resources field name. */
	String F_RESOURCES    = "resources";
	
	
	/**
	 * Returns the recipient id.
	 * 
	 * @return the recipient id
	 */
	Integer getRecipientId();
	
	/**
	 * Returns the traded resources. First element is the minerals, second element is the vespene.
	 * 
	 * @return the resources
	 */
	Integer[] getResources();
	
}
