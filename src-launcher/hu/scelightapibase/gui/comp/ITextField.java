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

import java.awt.Color;

import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

/**
 * An extended {@link JTextField}.
 * 
 * @author Andras Belicza
 * 
 * @see hu.scelightapi.service.IGuiFactory#newTextField()
 * @see hu.scelightapi.service.IGuiFactory#newTextField(boolean)
 * @see hu.scelightapi.service.IGuiFactory#newTextField(String)
 * @see hu.scelightapi.service.IGuiFactory#newTextField(int)
 * @see hu.scelightapi.service.IGuiFactory#newTextField(String, int)
 * @see hu.scelightapi.service.IGuiFactory#newTextField(String, int, boolean)
 * @see IIndicatorTextField
 */
public interface ITextField {
	
	/** Error background color to be set if a validation fails. */
	final Color ERROR_COLOR = new Color( 255, 150, 150 );
	
	
	/**
	 * Simple validator.
	 * 
	 * @author Andras Belicza
	 */
	interface IValidator {
		/**
		 * Validates the entered text.
		 * 
		 * @param text current text value of the text field
		 * @return true if the text is valid; false otherwise
		 */
		boolean validate( String text );
	}
	
	
	/**
	 * Casts this instance to {@link JTextField}.
	 * 
	 * @return <code>this</code> as a {@link JTextField}
	 */
	JTextField asTextField();
	
	/**
	 * Sets the specified text if it's different from the current text.
	 * 
	 * @param text text to be set
	 */
	void setText( String text );
	
	/**
	 * Registers the specified key stroke at the specified root component to focus the text field when the key stroke is pressed.
	 * 
	 * <p>
	 * If the text field already has an explicitly set tool tip (set by {@link JComponent#setToolTipText(String)}), it will be extended with a string
	 * representation of the key stroke.<br>
	 * If the explicitly set tool tip is an HTML tool tip and ends with <code>"&lt;/html&gt;</code>, the key stroke string will be inserted before the closing
	 * HTML tag.
	 * </p>
	 * 
	 * @param rootComponent root component to register the specified stroke at
	 * @param keyStroke key stroke to be registered to focus the text field when pressed
	 */
	void registerFocusHotkey( JComponent rootComponent, KeyStroke keyStroke );
	
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
	 */
	void setValidator( IValidator validator );
	
	/**
	 * Returns the validator of the text field.
	 * 
	 * @return the validator of the text field
	 */
	IValidator getValidator();
	
}
