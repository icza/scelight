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

import hu.scelightapibase.gui.comp.IIndicatorComp;
import hu.scelightapibase.gui.comp.IIndicatorTextField;
import hu.scelightapibase.gui.comp.ITextField;

import javax.swing.ToolTipManager;

/**
 * An {@link IIndicatorComp} whose component is an {@link ITextField}.
 * 
 * <p>
 * The actual content of the text field is used as the tool tip.
 * </p>
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class IndicatorTextField extends IndicatorComp implements IIndicatorTextField {
	
	/** Text field. */
	public final XTextField textField;
	
	/**
	 * Creates a new {@link IndicatorTextField}.
	 */
	public IndicatorTextField() {
		this( null );
	}
	
	/**
	 * Creates a new {@link IndicatorTextField}.
	 * 
	 * @param text initial text to be set
	 */
	public IndicatorTextField( final String text ) {
		// Create a text field which shows the complete path in the tool tip.
		textField = new XTextField() {
			// Register this at the Tool tip manager.
			// Simply calling setToolTipText( "" ) does not work because my getToolTipText() always returns a non-null text,
			// and setToolTipText( "" ) only registers at the tool tip manager if previous tool tip is not null!
			{
				ToolTipManager.sharedInstance().registerComponent( this );
			}
			
			@Override
			public String getToolTipText() {
				return getText().isEmpty() ? null : getText();
			}
		};
		
		if ( text != null )
			textField.setText( text );
		addCenter( textField );
	}
	
	@Override
	public XTextField getTextField() {
		return textField;
	}
	
	@Override
	public String getText() {
		return textField.getText();
	}
	
	@Override
	public void setText( final String text ) {
		textField.setText( text );
	}
	
}
