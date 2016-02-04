/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllauncher.gui.comp;

import hu.scelightapibase.gui.comp.ITextArea;
import hu.scelightapibase.gui.comp.ITextField;
import hu.scelightapibase.gui.comp.ITextField.IValidator;
import hu.sllauncher.util.gui.adapter.DocumentAdapter;

import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * An extended {@link JTextArea}.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class XTextArea extends JTextArea implements ITextArea {
	
	/** Default value of columns if not specified as a constructor argument. */
	public static final int DEFAULT_COLUMNS = 20;
	
	/** Default value of rows if not specified as a constructor argument. */
	public static final int DEFAULT_ROWS    = 3;
	
	
	/**
	 * Creates a new {@link XTextArea}.
	 */
	public XTextArea() {
		this( null );
	}
	
	/**
	 * Creates a new {@link XTextArea}.
	 * 
	 * @param text initial text of the text area
	 */
	public XTextArea( final String text ) {
		this( text, DEFAULT_ROWS, DEFAULT_COLUMNS );
	}
	
	/**
	 * Creates a new {@link XTextArea}.
	 * 
	 * @param rows the number of rows to use to calculate the preferred height
	 * @param columns the number of columns to use to calculate the preferred width
	 */
	public XTextArea( final int rows, final int columns ) {
		this( null, rows, columns );
	}
	
	/**
	 * Creates a new {@link XTextArea}.
	 * 
	 * @param text initial text of the text area
	 * @param rows the number of rows to use to calculate the preferred height
	 * @param columns the number of columns to use to calculate the preferred width
	 */
	public XTextArea( final String text, final int rows, final int columns ) {
		super( text, rows, columns );
	}
	
	
	@Override
	public JTextArea asTextArea() {
		return this;
	}
	
	/**
	 * Sets the specified text if it's different from the current text.
	 * 
	 * @param text text to be set
	 */
	@Override
	public void setText( final String text ) {
		// If the same string is to be set, do not set it because setting the same text in a text field is implemented in 2 steps:
		// 1) clear the text field
		// 2) Set the text
		// ...and these 2 steps trigger 2 document change events (for nothing...)
		// If the text area is a setting text area causing 2 "real" setting change (and resulting in marking the setting dirty)
		if ( getText().equals( text ) )
			return;
		
		super.setText( text );
	}
	
	
	/** Validator of the text field. */
	private IValidator       validator;
	
	/** {@link DocumentListener} registered to intercept document changes in order to call the validator. */
	private DocumentListener validatorDocListener;
	
	
	@Override
	public void setValidator( final IValidator validator ) {
		if ( this.validator != null )
			getDocument().removeDocumentListener( validatorDocListener );
		
		this.validator = validator;
		if ( validator != null )
			getDocument().addDocumentListener( validatorDocListener = new DocumentAdapter( true ) {
				@Override
				public void handleEvent( final DocumentEvent event ) {
					setBackground( validator.validate( getText() ) ? null : ITextField.ERROR_COLOR );
				}
			} );
	}
	
	@Override
	public IValidator getValidator() {
		return validator;
	}
	
}
