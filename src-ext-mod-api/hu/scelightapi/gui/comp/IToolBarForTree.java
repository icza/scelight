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
import hu.scelightapibase.gui.comp.ITree;

import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionListener;

/**
 * An {@link ISelectionListenerToolBar} which targets an {@link ITree} and listens for its selection changes.
 * 
 * <p>
 * The {@link #finalizeLayout()} method must be called in order to install the {@link TreeSelectionListener}.
 * </p>
 * 
 * @author Andras Belicza
 * 
 * @see IGuiFactory#newToolBarForTree(ITree)
 */
public interface IToolBarForTree extends ISelectionListenerToolBar {
	
	/**
	 * Overridden to also register {@link ListSelectionListener} to the targeted tree and also to invoke it.
	 */
	@Override
	void finalizeLayout();
	
}
