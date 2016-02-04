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

import hu.scelightapi.util.IStructView;

/**
 * Interface describing a camera target point.
 * 
 * @author Andras Belicza
 */
public interface ITargetPoint extends IStructView {
	
	/** X event field name. */
	String F_X = "x";
	
	/** Y event field name. */
	String F_Y = "y";
	
	
	/**
	 * Returns the target X coordinate (includes 8 fraction bits).
	 * 
	 * @return the target X coordinate
	 * 
	 * @see #getXFloat()
	 */
	Integer getX();
	
	/**
	 * Returns the target X coordinate as a float.
	 * 
	 * @return the target X coordinate as a float
	 * 
	 * @see #getX()
	 */
	Float getXFloat();
	
	/**
	 * Returns the target Y coordinate (includes 8 fraction bits).
	 * 
	 * @return the target Y coordinate
	 * 
	 * @see #getYFloat()
	 */
	Integer getY();
	
	/**
	 * Returns the target Y coordinate as a float.
	 * 
	 * @return the target Y coordinate as a float
	 * 
	 * @see #getY()
	 */
	Float getYFloat();
	
}
