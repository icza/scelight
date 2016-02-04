/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapi.sc2.balancedata.model;

/**
 * Describes a StarCraft II unit.
 * 
 * @author Andras Belicza
 * 
 * @see IEntity
 */
public interface IUnit extends IEntity {
	
	/**
	 * Returns the radius of the unit.
	 * 
	 * @return the radius of the unit
	 */
	float getRadius();
	
}
