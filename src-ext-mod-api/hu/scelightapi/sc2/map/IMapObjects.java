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
import java.util.List;

/**
 * Map objects.
 * 
 * @author Andras Belicza
 */
public interface IMapObjects {
	
	/**
	 * Returns the list of map unit objects.
	 * 
	 * @return the list of map unit objects
	 */
	List< ? extends IObjectUnit > getUnitList();
	
	/**
	 * Returns the start locations on the map.
	 * 
	 * @return the start locations on the map
	 */
	List< Point2D.Float > getStartLocationList();
	
}
