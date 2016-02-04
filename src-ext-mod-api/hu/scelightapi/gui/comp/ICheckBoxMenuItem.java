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

import javax.swing.JCheckBoxMenuItem;

/**
 * An extended {@link JCheckBoxMenuItem}.
 * 
 * <p>
 * Check box menu texts might also have a mnemonic character as described at {@link IMenuItem}.
 * </p>
 * 
 * @author Andras Belicza
 * 
 * @see IMenu
 * @see IGuiFactory#newCheckBoxMenuItem()
 * @see IGuiFactory#newCheckBoxMenuItem(String)
 * @see IGuiFactory#newCheckBoxMenuItem(javax.swing.Icon)
 * @see IGuiFactory#newCheckBoxMenuItem(String, javax.swing.Icon)
 */
public interface ICheckBoxMenuItem extends IMenuItem {
	
	/**
	 * Casts this instance to {@link JCheckBoxMenuItem}.
	 * 
	 * @return <code>this</code> as a {@link JCheckBoxMenuItem}
	 */
	JCheckBoxMenuItem asCheckBoxMenuItem();
	
}
