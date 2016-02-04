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
 * StarCraft II Replay tracker events.
 * 
 * @author Andras Belicza
 */
public interface ITrackerEvents {
	
	/** Id of the PlayerStats tracker event. */
	int ID_PLAYER_STATS      = 0;
	
	/** Id of the UnitBorn tracker event. */
	int ID_UNIT_BORN         = 1;
	
	/** Id of the UnitDied tracker event. */
	int ID_UNIT_DIED         = 2;
	
	/** Id of the UnitOwnerChange tracker event. */
	int ID_UNIT_OWNER_CHANGE = 3;
	
	/** Id of the UnitType tracker event. */
	int ID_UNIT_TYPE_CHANGE  = 4;
	
	/** Id of the Upgrade tracker event. */
	int ID_UPGRADE           = 5;
	
	/** Id of the UnitInit tracker event. */
	int ID_UNIT_INIT         = 6;
	
	/** Id of the UnitDone tracker event. */
	int ID_UNIT_DONE         = 7;
	
	/** Id of the UnitPositions tracker event. */
	int ID_UNIT_POSITIONS    = 8;
	
	/** Id of the PlayerSetup tracker event. */
	int ID_PLAYER_SETUP      = 9;
	
	
	/**
	 * Returns the array of tracker events.
	 * 
	 * @return the array of tracker events
	 */
	IEvent[] getEvents();
	
}
