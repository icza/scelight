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

import javax.swing.JTextArea;

/**
 * An extended {@link JTextArea}.
 * 
 * @author Andras Belicza
 * 
 * @see hu.scelightapi.service.IGuiFactory#newTextArea()
 * @see hu.scelightapi.service.IGuiFactory#newTextArea(String)
 * @see hu.scelightapi.service.IGuiFactory#newTextArea(String, int, int)
 */
public interface ITextArea {
	
	/**
	 * Casts this instance to {@link JTextArea}.
	 * 
	 * @return <code>this</code> as a {@link JTextArea}
	 */
	JTextArea asTextArea();
	
	/**
	 * Sets the specified text if it's different from the current text.
	 * 
	 * @param text text to be set
	 */
	void setText( String text );
	
	/**
	 * Sets a simple real-time validator to the text field.
	 * 
	 * <p>
	 * The validator will be called each time the text of the text field changes, and if the validator returns <code>false</code>, red background will indicate
	 * the failure.
	 * </p>
	 * 
	 * <p>
	 * If a validator has been previously set, it will be removed first. You can set a <code>null</code> validator to remove the current validator.
	 * </p>
	 * 
	 * @param validator validator to be added, may be <code>null</code>
	 * 
	 * @since 1.4
	 * 
	 * @see ITextField.IValidator
	 */
	void setValidator( IValidator validator );
	
	/**
	 * Returns the validator of the text field.
	 * 
	 * @return the validator of the text field
	 * 
	 * @since 1.4
	 * 
	 * @see ITextField.IValidator
	 */
	IValidator getValidator();
	
}
