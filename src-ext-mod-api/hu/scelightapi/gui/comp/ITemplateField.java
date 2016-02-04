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
import hu.scelightapi.template.ITemplateEngine;
import hu.scelightapibase.gui.comp.IButton;
import hu.scelightapibase.gui.comp.IIndicatorTextField;

/**
 * An {@link IIndicatorTextField} one-line name template field with an optional {@link IButton} which shows a template editor dialog when clicked.
 * 
 * @author Andras Belicza
 * 
 * @see IGuiFactory#newTemplateField()
 * @see IGuiFactory#newTemplateField(String)
 * @see IGuiFactory#newTemplateField(boolean)
 * @see IGuiFactory#newTemplateField(String, boolean)
 * @see ITemplateEngine
 */
public interface ITemplateField extends IIndicatorTextField {
	
	/**
	 * Returns the optional button to open a template editor dialog.
	 * 
	 * @return the optional button to open a template editor dialog
	 */
	IButton getButton();
	
	/**
	 * Returns the entered template.
	 * 
	 * @return the entered template
	 */
	String getTemplate();
	
	/**
	 * Sets the specified template.
	 * 
	 * @param template template to be set
	 */
	void setTemplate( String template );
	
	/**
	 * Returns the title of the template editor dialog.
	 * 
	 * @return the title of the template editor dialog
	 */
	String getDialogTitle();
	
	/**
	 * Sets the title of the template editor dialog.
	 * 
	 * @param dialogTitle title of the template editor dialog to be set
	 */
	void setDialogTitle( String dialogTitle );
	
}
