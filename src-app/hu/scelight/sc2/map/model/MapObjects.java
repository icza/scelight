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

import hu.scelightapi.sc2.map.IMapObjects;
import hu.scelightapi.sc2.map.IObjectUnit;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;
import java.util.List;

/**
 * Class describing map objects.
 * 
 * @author Andras Belicza
 */
public class MapObjects implements IMapObjects {
	
	/** Map unit objects. */
	public List< ObjectUnit >    unitList;
	
	/** Start locations on the map. */
	public List< Point2D.Float > startLocationList;
	
	@Override
	public List< ? extends IObjectUnit > getUnitList() {
		return unitList;
	}
	
	@Override
	public List< Float > getStartLocationList() {
		return startLocationList;
	}
	
}
