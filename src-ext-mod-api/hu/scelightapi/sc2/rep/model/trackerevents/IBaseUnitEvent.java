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
import hu.scelightapi.util.type.IXString;

/**
 * Base Unit tracker event.
 * 
 * @author Andras Belicza
 */
public interface IBaseUnitEvent extends IEvent {
	
	/** Unit tag index field name. */
	String F_UNIT_TAG_INDEX    = "unitTagIndex";
	
	/** Unit tag recycle field name. */
	String F_UNIT_TAG_RECYCLE  = "unitTagRecycle";
	
	/** Unit type name field name. */
	String F_UNIT_TYPE_NAME    = "unitTypeName";
	
	/** Control player id field name. */
	String F_CONTROL_PLAYER_ID = "controlPlayerId";
	
	/** Upkeep player id field name. */
	String F_UPKEEP_PLAYER_ID  = "upkeepPlayerId";
	
	/** X field name. */
	String F_X                 = "x";
	
	/** Y field name. */
	String F_Y                 = "y";
	
	
	/**
	 * Returns the unitTagIndex.
	 * 
	 * @return the unitTagIndex
	 */
	Integer getUnitTagIndex();
	
	/**
	 * Returns the unitTagRecycle.
	 * 
	 * @return the unitTagRecycle
	 */
	Integer getUnitTagRecycle();
	
	/**
	 * Returns the unitTypeName.
	 * 
	 * @return the unitTypeName
	 */
	IXString getUnitTypeName();
	
	/**
	 * Returns the controlPlayerId.
	 * 
	 * @return the controlPlayerId
	 */
	Integer getControlPlayerId();
	
	/**
	 * Returns the upkeepPlayerId.
	 * 
	 * @return the upkeepPlayerId
	 */
	Integer getUpkeepPlayerId();
	
	/**
	 * Returns the x.
	 * 
	 * @return the x
	 * @see #getXCoord()
	 */
	Integer getX();
	
	/**
	 * Returns the y.
	 * 
	 * @return the y
	 * @see #getYCoord()
	 */
	Integer getY();
	
	/**
	 * Returns the x converted to map coordinate.
	 * 
	 * @return the x converted to map coordinate
	 * @see #getX()
	 */
	Integer getXCoord();
	
	/**
	 * Returns the y converted to map coordinate.
	 * 
	 * @return the y converted to map coordinate
	 * @see #getY()
	 */
	Integer getYCoord();
	
}
