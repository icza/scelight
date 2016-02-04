/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapibase.gui.comp;

import java.awt.Color;

import javax.swing.Icon;

/**
 * An {@link Icon} which is filled with a constant color.
 * 
 * @author Andras Belicza
 * 
 * @see hu.scelightapi.service.IGuiFactory#newColorIcon(Color)
 * @see hu.scelightapi.service.IGuiFactory#newColorIcon(Color, String)
 * @see hu.scelightapi.service.IGuiFactory#newColorIcon(Color, int, int)
 * @see hu.scelightapi.service.IGuiFactory#newColorIcon(Color, int, int, String)
 */
public interface IColorIcon extends Icon {
	
	/**
	 * Returns the color of the icon.
	 * 
	 * @return the color of the icon
	 */
	Color getColor();
	
	/**
	 * Returns the text value to be returned by the {@link #toString()} method. Useful if this icon is rendered in a table, and users might copy data from the
	 * table (because this text will be copied).
	 * 
	 * @return the text value to be returned by the {@link #toString()} method
	 */
	String getText();
	
	/**
	 * Sets the text value to be returned by the {@link #toString()} method. Useful if this icon is rendered in a table, and users might copy data from the
	 * table (because this text will be copied).
	 * 
	 * @param text text to be set
	 */
	void setText( String text );
	
	/**
	 * Overrides {@link Object#toString()} to return the text specified by {@link #getText()} so that if this icon is used in a table, copying table will result
	 * in a meaningful text.
	 */
	@Override
	public String toString();
	
}
