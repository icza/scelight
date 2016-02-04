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

import hu.scelightapibase.util.gui.HasRIcon;

/**
 * Describes a StarCraft II base entity. Holds the common characteristics of different entities like units, commands etc.
 * 
 * @author Andras Belicza
 */
public interface IEntity extends HasRIcon {
	
	/**
	 * Returns the string id of the entity.
	 * 
	 * @return the string id of the entity
	 */
	String getId();
	
	/**
	 * Returns the displayable name of the entity.
	 * 
	 * @return the displayable name of the entity
	 */
	String getText();
	
	/**
	 * Returns the time it costs to build / complete / finish the entity (in-game seconds).
	 * 
	 * @return the time it costs to build / complete / finish the entity (in-game seconds)
	 */
	float getCostTime();
	
}
