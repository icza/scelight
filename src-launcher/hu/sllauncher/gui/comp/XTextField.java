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

import hu.scelightapibase.gui.comp.ITextField;
import hu.sllauncher.util.gui.LGuiUtils;
import hu.sllauncher.util.gui.adapter.DocumentAdapter;

import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * An extended {@link JTextField}.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class XTextField extends JTextField implements ITextField {
	
	/** Default value of columns if not specified as a constructor argument. */
	public static final int DEFAULT_COLUMNS = 10;
	
	
	/**
	 * Creates a new {@link XTextField} with an initial columns of {@value #DEFAULT_COLUMNS}.
	 */
	public XTextField() {
		this( null );
	}
	
	/**
	 * Creates a new {@link XTextField} with an initial columns of {@value #DEFAULT_COLUMNS}.
	 * 
	 * @param clearByEsc tells if text field should be cleared when the ESC key is pressed
	 */
	public XTextField( final boolean clearByEsc ) {
		this( null, DEFAULT_COLUMNS, clearByEsc );
	}
	
	/**
	 * Creates a new {@link XTextField} with an initial columns of {@value #DEFAULT_COLUMNS}.
	 * 
	 * @param text initial text of the text field
	 */
	public XTextField( final String text ) {
		this( text, DEFAULT_COLUMNS, false );
	}
	
	/**
	 * Creates a new {@link XTextField}.
	 * 
	 * @param columns the number of columns to use to calculate the preferred width; if columns is set to zero, the preferred width will be whatever naturally
	 *            results from the component implementation
	 */
	public XTextField( final int columns ) {
		this( null, columns, false );
	}
	
	/**
	 * Creates a new {@link XTextField}.
	 * 
	 * @param text initial text of the text field
	 * @param columns the number of columns to use to calculate the preferred width; if columns is set to zero, the preferred width will be whatever naturally
	 *            results from the component implementation
	 */
	public XTextField( final String text, final int columns ) {
		this( text, columns, false );
	}
	
	/**
	 * Creates a new {@link XTextField}.
	 * 
	 * @param text initial text of the text field
	 * @param columns the number of columns to use to calculate the preferred width; if columns is set to zero, the preferred width will be whatever naturally
	 *            results from the component implementation
	 * @param clearByEsc tells if text field should be cleared when the ESC key is pressed
	 */
	public XTextField( final String text, final int columns, final boolean clearByEsc ) {
		super( text, columns );
		
		if ( clearByEsc )
			addKeyListener( new KeyAdapter() {
				@Override
				public void keyPressed( final KeyEvent event ) {
					if ( event.getKeyCode() == KeyEvent.VK_ESCAPE ) {
						if ( !getText().isEmpty() ) {
							setText( null );
							// ESC is also used to hide (close) dialogs, so consume event if we did clear search term
							event.consume();
						}
					}
				}
			} );
	}
	
	
	@Override
	public JTextField asTextField() {
		return this;
	}
	
	/**
	 * Sets the specified text if it's different from the current text.
	 * 
	 * @param text text to be set
	 */
	@Override
	public void setText( final String text ) {
		// If the same string is to be set, do not set it because setting the same text in a text field is implemented in 2
		// steps:
		// 1) clear the text field
		// 2) Set the text
		// ...and these 2 steps trigger 2 document change events (for nothing...)
		// If the text field is a settign text field causing 2 "real" setting change (and resulting in marking the setting dirty)
		if ( getText().equals( text ) )
			return;
		
		super.setText( text );
	}
	
	@Override
	public void registerFocusHotkey( final JComponent rootComponent, final KeyStroke keyStroke ) {
		final Object actionKey = new Object();
		
		final String toolTipText = getToolTipText();
		if ( toolTipText != null ) {
			if ( toolTipText.endsWith( "</html>" ) )
				setToolTipText( toolTipText.substring( 0, toolTipText.length() - 7 ) + LGuiUtils.keyStrokeToString( keyStroke ) + "</html>" );
			else
				setToolTipText( toolTipText + LGuiUtils.keyStrokeToString( keyStroke ) );
		}
		
		rootComponent.getInputMap( JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT ).put( keyStroke, actionKey );
		rootComponent.getActionMap().put( actionKey, new AbstractAction() {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				requestFocusInWindow();
			}
		} );
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
					setBackground( validator.validate( getText() ) ? null : ERROR_COLOR );
				}
			} );
	}
	
	@Override
	public IValidator getValidator() {
		return validator;
	}
	
}
