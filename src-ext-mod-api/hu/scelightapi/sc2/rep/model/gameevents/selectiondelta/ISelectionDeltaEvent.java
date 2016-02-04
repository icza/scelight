/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapi.sc2.rep.model.gameevents.selectiondelta;

import hu.scelightapi.sc2.rep.model.IEvent;

/**
 * Camera update game event.
 * 
 * @author Andras Belicza
 */
public interface ISelectionDeltaEvent extends IEvent {
	
	/** Control group id field name. */
	String F_GROUP_INDEX = "controlGroupId";
	
	/** Delta field name. */
	String F_DELTA       = "delta";
	
	
	/**
	 * Returns the control group id.
	 * 
	 * @return the control group id
	 */
	Integer getControlGroupId();
	
	/**
	 * Returns the selection delta.
	 * 
	 * @return the selection delta
	 */
	IDelta getDelta();
	
}
