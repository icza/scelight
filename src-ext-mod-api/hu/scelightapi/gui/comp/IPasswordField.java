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

import javax.swing.JPasswordField;

/**
 * An extended {@link JPasswordField}.
 * 
 * @author Andras Belicza
 * 
 * @see IGuiFactory#newPasswordField()
 * @see IGuiFactory#newPasswordField(String)
 * @see IGuiFactory#newPasswordField(int)
 * @see IGuiFactory#newPasswordField(String, int)
 */
public interface IPasswordField {
	
	/**
	 * Casts this instance to {@link JPasswordField}.
	 * 
	 * @return <code>this</code> as a {@link JPasswordField}
	 */
	JPasswordField asPasswordField();
	
}
