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
import hu.scelightapibase.gui.comp.ILabel;

import javax.swing.JPopupMenu;

/**
 * An extended {@link JPopupMenu}.
 * 
 * @author Andras Belicza
 * 
 * @see IGuiFactory#newPopupMenu()
 * @see IGuiFactory#newPopupMenu(String)
 * @see IGuiFactory#newPopupMenu(String, javax.swing.Icon)
 * @see IGuiFactory#newPopupMenu(ILabel)
 */
public interface IPopupMenu {
	
	/**
	 * Casts this instance to {@link JPopupMenu}.
	 * 
	 * @return <code>this</code> as a {@link JPopupMenu}
	 */
	JPopupMenu asPopupMenu();
	
}
