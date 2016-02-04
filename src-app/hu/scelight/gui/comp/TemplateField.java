/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.gui.comp;

import hu.scelight.gui.dialog.TemplateEditorDialog;
import hu.scelight.gui.icon.Icons;
import hu.scelight.template.InvalidTemplateException;
import hu.scelight.template.TemplateEngine;
import hu.scelightapi.gui.comp.ITemplateField;
import hu.scelightapibase.gui.comp.ITextField.IValidator;
import hu.sllauncher.gui.comp.IndicatorTextField;
import hu.sllauncher.gui.comp.XButton;
import hu.sllauncher.util.gui.adapter.ActionAdapter;

import java.awt.event.ActionEvent;

/**
 * An {@link IndicatorTextField} one-line name template field with an optional {@link XButton} which shows a {@link TemplateEditorDialog} when clicked.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class TemplateField extends IndicatorTextField implements ITemplateField {
	
	/** Optional button to open a template editor dialog. */
	public final XButton button;
	
	/** Title of the template editor dialog. */
	private String       dialogTitle;
	
	/**
	 * Creates a new {@link TemplateField} with a template editor dialog opener button added.
	 */
	public TemplateField() {
		this( null, true );
	}
	
	/**
	 * Creates a new {@link TemplateField} with a template editor dialog opener button added.
	 * 
	 * @param template initial template to be set
	 */
	public TemplateField( final String template ) {
		this( template, true );
	}
	
	/**
	 * Creates a new {@link TemplateField}.
	 * 
	 * @param addDialogOpener tells if a template editor dialog opener button is to be added
	 */
	public TemplateField( final boolean addDialogOpener ) {
		this( null, addDialogOpener );
	}
	
	/**
	 * Creates a new {@link TemplateField}.
	 * 
	 * @param template initial template to be set
	 * @param addDialogOpener tells if a template editor dialog opener button is to be added
	 */
	public TemplateField( final String template, final boolean addDialogOpener ) {
		super( template );
		
		textField.setValidator( new IValidator() {
			@Override
			public boolean validate( final String text ) {
				if ( text.isEmpty() ) {
					setEmpty( "No template entered." );
					return true;
				}
				
				try {
					new TemplateEngine( text );
					// If no exception is thrown: template is valid.
					setAccepted( "Template is valid." );
					return true;
				} catch ( final InvalidTemplateException ite ) {
					setError( "<html>The entered template is invalid:<blockquote>" + ite.getMessage() + "</blockquote>"
					        + ( ite.position == null ? "" : "at position: " + ( ite.position + 1 ) ) + "</html>" );
					return false;
				}
			}
		} );
		
		addCenter( textField );
		
		if ( addDialogOpener ) {
			button = new XButton( Icons.F_PENCIL.get() );
			button.setToolTipText( "Open Template Editor..." );
			button.addActionListener( new ActionAdapter() {
				@Override
				public void actionPerformed( final ActionEvent event ) {
					new TemplateEditorDialog( dialogTitle, TemplateField.this );
				}
			} );
			indicatorBox.add( button );
		} else
			button = null;
	}
	
	@Override
	public XButton getButton() {
		return button;
	}
	
	@Override
	public String getTemplate() {
		return getText();
	}
	
	@Override
	public void setTemplate( final String template ) {
		setText( template );
	}
	
	@Override
	public String getDialogTitle() {
		return dialogTitle;
	}
	
	@Override
	public void setDialogTitle( final String dialogTitle ) {
		this.dialogTitle = dialogTitle;
	}
	
}
