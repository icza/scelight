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

import hu.scelightapibase.gui.comp.ITextField.IValidator;

/**
 * An {@link IIndicatorComp} whose component is an {@link ITextField}.
 * 
 * <p>
 * The actual content of the text field is used as the tool tip.
 * </p>
 * 
 * <p>
 * Usually the {@link IIndicatorTextField} is used with an {@link IValidator} installed on the text field of the {@link IIndicatorTextField}. The
 * {@link IValidator} implementation can take advantage on the status this indicator text field can display.
 * </p>
 * 
 * @author Andras Belicza
 * 
 * @see hu.scelightapi.service.IGuiFactory#newIndicatorTextField()
 * @see hu.scelightapi.service.IGuiFactory#newIndicatorTextField(String)
 * @see hu.scelightapi.gui.comp.IIndicatorTextArea
 */
public interface IIndicatorTextField extends IIndicatorComp {
	
	/**
	 * Returns the text field.
	 * 
	 * @return the text field
	 */
	ITextField getTextField();
	
	/**
	 * Returns the current text.
	 * 
	 * @return the current text
	 */
	String getText();
	
	/**
	 * Sets the specified text.
	 * 
	 * @param text text to be set
	 */
	void setText( String text );
	
}
