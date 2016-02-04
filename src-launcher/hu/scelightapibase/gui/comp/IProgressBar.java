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

import hu.scelightapibase.gui.comp.table.ITable;
import hu.scelightapibase.gui.comp.table.renderer.IProgressBarView;

import javax.swing.JProgressBar;

/**
 * An extended {@link JProgressBar}.
 * 
 * <p>
 * Implements {@link Comparable} based on the current value of the progress bar so if displayed in a table (used as a renderer), they can be sorted properly.
 * </p>
 * 
 * @author Andras Belicza
 * 
 * @see hu.scelightapi.service.IGuiFactory#newProgressBar()
 * @see IProgressBarView
 * @see ITable#setRowHeightForProgressBar()
 */
public interface IProgressBar extends Comparable< IProgressBar > {
	
	/**
	 * Casts this instance to {@link JProgressBar}.
	 * 
	 * @return <code>this</code> as a {@link JProgressBar}
	 */
	JProgressBar asProgressBar();
	
	/**
	 * Displays an aborted status.
	 */
	void setAborted();
	
	/**
	 * Overrides {@link Object#toString()} to return the painted string.
	 */
	@Override
	String toString();
	
	/**
	 * Returns the progress bar's current value. See {@link JProgressBar#getValue()} for details.
	 * 
	 * @return the progress bar's current value
	 */
	int getValue();
	
	/**
	 * Defines an order based on the progress bar's current value.
	 */
	@Override
	int compareTo( IProgressBar pb );
	
}
