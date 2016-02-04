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

import hu.scelight.sc2.textures.Textures;
import hu.scelightapi.sc2.balancedata.model.IEntity;
import hu.scelightapibase.gui.icon.IRIcon;

/**
 * Describes a StarCraft II base entity. Holds the common subset of attributes of different entities like units, commands etc.
 * 
 * @author Andras Belicza
 */
public class Entity implements IEntity {
	
	/** String id of the entity. */
	public String id;
	
	/** Displayable name of the entity. */
	public String text;
	
	/** Name of the entity's icon. */
	public String icon;
	
	/** Time it costs to build / complete / finish the entity (in-game seconds). */
	public float  costTime;
	
	
	@Override
	public String getId() {
		return id;
	}
	
	@Override
	public String getText() {
		return text;
	}
	
	@Override
	public IRIcon getRicon() {
		return Textures.getIcon( icon );
	}
	
	@Override
	public float getCostTime() {
		return costTime;
	}
	
}
