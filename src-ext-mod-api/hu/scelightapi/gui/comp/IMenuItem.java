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

import javax.swing.JMenuItem;

/**
 * An extended {@link JMenuItem}.
 * 
 * <p>
 * Menu item texts might have a mnemonic character indicated with the underscore (<code>'_'</code>). If such indicator is detected, it will be removed from the
 * visible text and the character following the underscore will be set as the button's mnemonic.
 * </p>
 * 
 * @author Andras Belicza
 * 
 * @see IMenu
 * @see IGuiFactory#newMenuItem()
 * @see IGuiFactory#newMenuItem(String)
 * @see IGuiFactory#newMenuItem(javax.swing.Icon)
 * @see IGuiFactory#newMenuItem(String, javax.swing.Icon)
 */
public interface IMenuItem {
	
	/**
	 * Casts this instance to {@link JMenuItem}.
	 * 
	 * @return <code>this</code> as a {@link JMenuItem}
	 */
	JMenuItem asMenuItem();
	
}
