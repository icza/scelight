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

import javax.swing.JSpinner;

/**
 * An extended {@link JSpinner}.
 * 
 * <p>
 * Sets / applies the number and date formats of the settings.
 * </p>
 * 
 * @author Andras Belicza
 * 
 * @see hu.scelightapi.service.IGuiFactory#newSpinner()
 * @see hu.scelightapi.service.IGuiFactory#newSpinner(javax.swing.SpinnerModel)
 */
public interface ISpinner {
	
	/**
	 * Casts this instance to {@link JSpinner}.
	 * 
	 * @return <code>this</code> as a {@link JSpinner}
	 */
	JSpinner asSpinner();
	
}
