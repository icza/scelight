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

import hu.scelightapibase.gui.comp.IProgressBar;
import hu.scelightapibase.gui.comp.table.ITable;

/**
 * An interface which defines an object that will be rendered as a progress bar in the {@link ITable}.
 * 
 * <p>
 * Implements {@link Comparable} based on the value of the progress bar so if displayed in a table (used as a cell value), they can be sorted properly.
 * </p>
 * 
 * @since 1.2
 * 
 * @author Andras Belicza
 * 
 * @see hu.scelightapi.service.IGuiFactory#newProgressBarView(int, int)
 * @see hu.scelightapi.service.IGuiFactory#newProgressBarView(int, int, String)
 * @see IBarCodeView
 * @see IProgressBar
 * @see ITable#setRowHeightForProgressBar()
 */
public interface IProgressBarView extends Comparable< IProgressBarView >, IBarCodeView {
	
	/**
	 * Returns the value of the progress bar.
	 * 
	 * @return the value of the progress bar
	 */
	int getValue();
	
	/**
	 * Returns the maximum value of the progress bar.
	 * 
	 * @return the maximum value of the progress bar
	 */
	int getMaximum();
	
	/**
	 * Defines an order based on the progress bar's value.
	 */
	@Override
	int compareTo( IProgressBarView pbv );
	
}
