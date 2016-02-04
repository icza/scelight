/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapibase.util.gui;

import javax.swing.Icon;

/**
 * Interface telling that an instance has an icon ({@link Icon}).
 * 
 * @author Andras Belicza
 */
public interface HasIcon {
	
	/**
	 * Returns the icon.
	 * 
	 * @return the icon
	 */
	Icon getIcon();
	
}
