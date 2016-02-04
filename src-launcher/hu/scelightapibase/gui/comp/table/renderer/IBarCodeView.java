/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapibase.gui.comp.table.renderer;

import hu.scelightapibase.gui.comp.table.ITable;

import java.awt.Color;

/**
 * An interface which defines an object that will be rendered as a bar code in the {@link ITable}.
 * 
 * @since 1.2
 * 
 * @author Andras Belicza
 * 
 * @see hu.scelightapi.service.IGuiFactory#newBarCodeView(int[], Color[], Color[])
 * @see hu.scelightapi.service.IGuiFactory#newBarCodeView(int[], Color[], Color[], String)
 * @see IProgressBarView
 */
public interface IBarCodeView {
	
	/**
	 * Returns the values of the parts of the bar code.
	 * 
	 * @return the values of the parts of the bar code.
	 */
	int[] getValues();
	
	/**
	 * Returns the colors of the parts of the bar code.
	 * 
	 * @return the colors of the parts of the bar coded
	 * 
	 * @see #getSelectedColors()
	 */
	Color[] getColors();
	
	/**
	 * Returns the colors of the parts of the bar code when the cell is selected.<br>
	 * These should be darker versions of the normal colors ({@link #getColors()}).
	 * 
	 * @return the colors of the parts of the bar code when the cell is selected
	 * 
	 * @see #getColors()
	 */
	Color[] getSelectedColors();
	
	/**
	 * The value returned by this will be rendered on the bar code, also this value will be copied to the clipboard if the cell is copied.
	 */
	@Override
	String toString();
	
}
