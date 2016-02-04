/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapibase.gui.comp.table;

import hu.scelightapibase.gui.comp.IToolBar;

import javax.swing.JComponent;

/**
 * Interface for the table {@link IToolBar} parameters.
 * 
 * @author Andras Belicza
 * 
 * @see ITable#createToolBarParams(JComponent)
 */
public interface ITToolBarParams {
	
	/**
	 * Returns the table the tool bar is created for.
	 * 
	 * @return the table the tool bar is created for
	 */
	ITable getTable();
	
	/**
	 * Returns the root component to register key strokes at.
	 * 
	 * @return the root component to register key strokes at
	 */
	JComponent getRootComponent();
	
}
