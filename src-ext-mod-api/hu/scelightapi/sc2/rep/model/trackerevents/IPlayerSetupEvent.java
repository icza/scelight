/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapi.sc2.rep.model.trackerevents;

import hu.scelightapi.sc2.rep.model.IEvent;

/**
 * Player setup tracker event.
 * 
 * <p>
 * Present since base build 27950 (2.1 PTR).
 * </p>
 * 
 * @author Andras Belicza
 */
public interface IPlayerSetupEvent extends IEvent {
	
	/** Type field name. */
	String F_TYPE    = "type";
	
	/** Slot id field name. */
	String F_SLOT_ID = "slotId";
	
	
	/**
	 * Returns the type.<br>
	 * 1=human, 2=cpu, 3=neutral, 4=hostile
	 * 
	 * @return the type
	 */
	Integer getType();
	
	/**
	 * Returns the setup user id for this player, only if human.
	 * 
	 * @return the setup user id for this player
	 */
	Integer getSetupUserId();
	
	/**
	 * Returns the setup slot id for this player, only if human or cpu.
	 * 
	 * @return the setup slot id for this player
	 */
	Integer getSetupSlotId();
	
}
