/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapi.gui.comp;

import hu.scelightapi.service.IGuiFactory;
import hu.scelightapibase.gui.comp.ISelectionListenerToolBar;

import javax.swing.event.ListSelectionListener;

/**
 * An {@link ISelectionListenerToolBar} which targets an {@link IList} and listens for its item selection changes.
 * 
 * <p>
 * The {@link #finalizeLayout()} method must be called in order to install the {@link ListSelectionListener}.
 * </p>
 * 
 * @author Andras Belicza
 * 
 * @see IGuiFactory#newToolBarForList(IList)
 */
public interface IToolBarForList extends ISelectionListenerToolBar {
	
	/**
	 * Overridden to also register {@link ListSelectionListener} to the targeted list and also to invoke it.
	 */
	@Override
	void finalizeLayout();
	
}
