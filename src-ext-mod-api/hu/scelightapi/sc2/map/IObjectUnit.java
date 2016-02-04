/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapi.sc2.map;

import java.awt.geom.Point2D;

/**
 * A unit object on the map.
 * 
 * @author Andras Belicza
 */
public interface IObjectUnit {
	
	/**
	 * Returns the type of the unit; it is an interned string ({@link String#intern()}).
	 * 
	 * @return the type of the unit; it is an interned string ({@link String#intern()})
	 */
	String getUnitType();
	
	/**
	 * Returns the position of the object.
	 * 
	 * @return the position of the object
	 */
	Point2D.Float getPos();
	
}
