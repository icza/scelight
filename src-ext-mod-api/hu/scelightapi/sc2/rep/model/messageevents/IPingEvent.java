/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapi.sc2.rep.model.messageevents;

/**
 * Minimap ping message event.
 * 
 * @author Andras Belicza
 */
public interface IPingEvent extends IMsgEvent {
	
	/** Point x event field path. */
	String[] P_POINT_X = { "point", "x" };
	
	/** Point y field path. */
	String[] P_POINT_Y = { "point", "y" };
	
	
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
	Float getXFloat();
	
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
	Float getYFloat();
	
}
