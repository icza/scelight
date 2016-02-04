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

import javax.swing.event.ListSelectionListener;

/**
 * An {@link ISelectionListenerToolBar} which targets an {@link ITable} and listens for its row selection changes.
 * 
 * <p>
 * The {@link #finalizeLayout()} method must be called in order to install the {@link ListSelectionListener}.
 * </p>
 * 
 * @author Andras Belicza
 * 
 * @see hu.scelightapi.service.IGuiFactory#newToolBarForTable(ITable)
 */
public interface IToolBarForTable extends ISelectionListenerToolBar {
	
	/**
	 * Overridden to also register {@link ListSelectionListener} to the targeted table and also to invoke it.
	 */
	@Override
	void finalizeLayout();
	
}
