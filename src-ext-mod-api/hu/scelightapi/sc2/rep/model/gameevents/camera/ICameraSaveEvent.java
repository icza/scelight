/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapi.sc2.rep.model.gameevents.camera;

import hu.scelightapi.sc2.rep.model.IEvent;

import java.util.Map;

/**
 * Camera save event.
 * 
 * @author Andras Belicza
 */
public interface ICameraSaveEvent extends IEvent {
	
	/** Which field name. */
	String F_WHICH  = "which";
	
	/** Target event field name. */
	String F_TARGET = "target";
	
	
	/**
	 * Returns which camera hotkey is being used.
	 * 
	 * @return which camera hotkey is being used
	 */
	Integer getWhich();
	
	/**
	 * Returns the target point structure.
	 * 
	 * @return the target point structure
	 * 
	 * @see #getTargetPoint()
	 */
	Map< String, Object > getTarget();
	
	/**
	 * Returns the target point of the camera.
	 * 
	 * @return the target point of the camera
	 */
	ITargetPoint getTargetPoint();
	
}
