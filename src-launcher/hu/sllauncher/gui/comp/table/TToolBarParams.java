/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllauncher.gui.comp.table;

import hu.scelightapibase.gui.comp.table.ITToolBarParams;
import hu.scelightapibase.gui.comp.table.ITable;

import javax.swing.JComponent;

/**
 * Wrapper class for the {@link XTableToolBar} parameters.
 * 
 * @author Andras Belicza
 */
public class TToolBarParams implements ITToolBarParams {
	
	/** Table the tool bar is created for. */
	public final XTable     table;
	
	/** root component to register key strokes at. */
	public final JComponent rootComponent;
	
	/**
	 * Creates a new {@link TToolBarParams}.
	 * 
	 * @param table table the tool bar is created for.
	 * @param rootComponent root component to register key strokes at
	 */
	public TToolBarParams( final XTable table, final JComponent rootComponent ) {
		this.table = table;
		this.rootComponent = rootComponent;
	}
	
	@Override
	public ITable getTable() {
		return table;
	}
	
	@Override
	public JComponent getRootComponent() {
		return rootComponent;
	}
	
}
