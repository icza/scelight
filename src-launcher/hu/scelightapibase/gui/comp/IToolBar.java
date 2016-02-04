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

import javax.swing.JToolBar;

/**
 * An extended {@link JToolBar}.
 * 
 * @author Andras Belicza
 * 
 * @see hu.scelightapi.service.IGuiFactory#newToolBar()
 * @see hu.scelightapi.service.IGuiFactory#newToolBar(boolean)
 */
public interface IToolBar {
	
	/**
	 * Casts this instance to {@link JToolBar}.
	 * 
	 * @return <code>this</code> as a {@link JToolBar}
	 */
	JToolBar asToolBar();
	
	/**
	 * Does some finalization after content has been added to the tool bar.
	 */
	void finalizeLayout();
	
}
