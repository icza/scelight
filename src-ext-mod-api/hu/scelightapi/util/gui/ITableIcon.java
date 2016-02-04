/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapi.util.gui;

import hu.scelightapibase.util.gui.HasRIcon;

import javax.swing.Icon;

/**
 * Icon for tables which overrides {@link Object#toString()} for copying and table search / filter operations, and which implements {@link Comparable} for
 * sorting.
 * 
 * @param <T> type of the represented object
 * 
 * @author Andras Belicza
 */
public interface ITableIcon< T extends HasRIcon & Comparable< T > > extends Icon, Comparable< ITableIcon< T > > {
	
	/**
	 * Returns the represented object.
	 * 
	 * @return the represented object
	 */
	T getRepresented();
	
	/**
	 * Returns the icon.
	 * 
	 * @return the icon
	 */
	Icon getIcon();
	
}
