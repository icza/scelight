/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.sc2.map.model;

import hu.scelightapi.sc2.map.IObjectUnit;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;

/**
 * A unit object on the map.
 * 
 * @author Andras Belicza
 */
public class ObjectUnit implements IObjectUnit {
	
	/** Type of the unit; it is an interned string ({@link String#intern()}). */
	public String        unitType;
	
	/** Position of the object. */
	public Point2D.Float pos;
	
	@Override
	public String getUnitType() {
		return unitType;
	}
	
	@Override
	public Float getPos() {
		return pos;
	}
	
}
