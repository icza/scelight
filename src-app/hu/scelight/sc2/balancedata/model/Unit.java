/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.sc2.balancedata.model;

import hu.scelightapi.sc2.balancedata.model.IUnit;

/**
 * Describes a StarCraft II unit.
 * 
 * @author Andras Belicza
 */
public class Unit extends Entity implements IUnit {
	
	/** Radius of the unit. */
	public float radius;
	
	
	@Override
	public float getRadius() {
		return radius;
	}
	
}
