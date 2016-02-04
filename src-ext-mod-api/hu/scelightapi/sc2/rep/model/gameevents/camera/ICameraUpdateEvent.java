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
 * Camera update game event.
 * 
 * @author Andras Belicza
 */
public interface ICameraUpdateEvent extends IEvent {
	
	/** Distance event field name. */
	String F_DISTANCE = "distance";
	
	/** Pitch event field name. */
	String F_PITCH    = "pitch";
	
	/** Yaw event field name. */
	String F_YAW      = "yaw";
	
	/** Target event field name. */
	String F_TARGET   = "target";
	
	/** Reason event field name. */
	String F_REASON   = "reason";
	
	
	/**
	 * Returns the distance (includes 8 fraction bits).
	 * 
	 * @return the distance (includes 8 fraction bits)
	 * 
	 * @see #getDistanceFloat()
	 */
	Integer getDistance();
	
	/**
	 * Returns the distance as a float.
	 * 
	 * @return the distance as a float
	 * 
	 * @see #getDistance()
	 */
	Float getDistanceFloat();
	
	/**
	 * Returns the pitch (angle in the vertical plane, vertical elevation).
	 * 
	 * @return the pitch (angle in the vertical plane, vertical elevation)
	 * 
	 * @see #getPitchDegree()
	 */
	Integer getPitch();
	
	/**
	 * Returns the pitch (angle in the vertical plane, vertical elevation) in degrees as float.
	 * 
	 * @return the pitch (angle in the vertical plane, vertical elevation) in degrees as float
	 * 
	 * @see #getPitch()
	 */
	Float getPitchDegree();
	
	/**
	 * Returns the yaw (angle in the horizontal plane).
	 * 
	 * @return the yaw (angle in the horizontal plane)
	 * 
	 * @see #getYawDegree()
	 */
	Integer getYaw();
	
	/**
	 * Returns the yaw (angle in the horizontal plane) in degrees as float.
	 * 
	 * @return the yaw (angle in the horizontal plane) in degrees as float
	 * 
	 * @see #getYaw()
	 */
	Float getYawDegree();
	
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
	
	/**
	 * @return the reason
	 */
	Integer getReason();
	
}
