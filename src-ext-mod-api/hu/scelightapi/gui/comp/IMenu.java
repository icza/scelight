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

import javax.swing.JMenu;

/**
 * An extended {@link JMenu}.
 * 
 * <p>
 * Menu texts might also have a mnemonic character as described at {@link IMenuItem}.
 * </p>
 * 
 * @author Andras Belicza
 * 
 * @see IMenuItem
 * @see IGuiFactory#newMenu()
 * @see IGuiFactory#newMenu(String)
 * @see IGuiFactory#newMenu(javax.swing.Icon)
 * @see IGuiFactory#newMenu(String, javax.swing.Icon)
 */
public interface IMenu extends IMenuItem {
	
	/**
	 * Casts this instance to {@link JMenu}.
	 * 
	 * @return <code>this</code> as a {@link JMenu}
	 */
	JMenu asMenu();
	
}
