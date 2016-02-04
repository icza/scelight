/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapi.sc2.rep.model.gameevents.cmd;

import hu.scelightapi.util.IStructView;

/**
 * Interface describing a cmd target point.
 * 
 * @author Andras Belicza
 */
public interface ITargetPoint extends IStructView {
	
	/** X event field name. */
	String F_X = "x";
	
	/** Y event field name. */
	String F_Y = "y";
	
	/** Z event field name. */
	String F_Z = "z";
	
	
	/**
	 * Returns the X coordinate (includes 13 fraction bits).
	 * 
	 * @return the X coordinate
	 * 
	 * @see #getXFloat()
	 */
	Integer getX();
	
	/**
	 * Returns the X coordinate as a float.
	 * 
	 * @return the X coordinate as a float
	 * 
	 * @see #getX()
	 */
	float getXFloat();
	
	/**
	 * Returns the Y coordinate (includes 13 fraction bits).
	 * 
	 * @return the Y coordinate
	 * 
	 * @see #getYFloat()
	 */
	Integer getY();
	
	/**
	 * Returns the Y coordinate as a float.
	 * 
	 * @return the Y coordinate as a float
	 * 
	 * @see #getY()
	 */
	float getYFloat();
	
	/**
	 * Returns the Z coordinate (includes 13 fraction bits).
	 * 
	 * @return the Z coordinate
	 * 
	 * @see #getZFloat()
	 */
	Integer getZ();
	
	/**
	 * Returns the Z coordinate as a float.
	 * 
	 * @return the Z coordinate as a float
	 * 
	 * @see #getZ()
	 */
	float getZFloat();
	
}
