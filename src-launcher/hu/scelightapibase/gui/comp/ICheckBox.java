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

import javax.swing.JCheckBox;

/**
 * An extended {@link JCheckBox}.
 * 
 * @author Andras Belicza
 * 
 * @see hu.scelightapi.service.IGuiFactory#newCheckBox()
 * @see hu.scelightapi.service.IGuiFactory#newCheckBox(String)
 * @see hu.scelightapi.service.IGuiFactory#newCheckBox(String, boolean)
 */
public interface ICheckBox {
	
	/**
	 * Casts this instance to {@link JCheckBox}.
	 * 
	 * @return <code>this</code> as a {@link JCheckBox}
	 */
	JCheckBox asCheckBox();
	
}
