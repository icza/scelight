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

import hu.scelightapi.gui.comp.IPasswordField;
import hu.sllauncher.gui.comp.XTextField;

import javax.swing.JPasswordField;

/**
 * An extended {@link JPasswordField}.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class XPasswordField extends JPasswordField implements IPasswordField {
	
	/** Default value of columns if not specified as a constructor argument. */
	public static final int DEFAULT_COLUMNS = 10;
	
	/**
	 * Creates a new {@link XPasswordField}.
	 */
	public XPasswordField() {
		this( null );
	}
	
	/**
	 * Creates a new {@link XTextField} with an initial columns of {@value #DEFAULT_COLUMNS}.
	 * 
	 * @param text initial text of the password field
	 */
	public XPasswordField( final String text ) {
		this( text, DEFAULT_COLUMNS );
	}
	
	/**
	 * Creates a new {@link XPasswordField}.
	 * 
	 * @param columns the number of columns to use to calculate the preferred width
	 */
	public XPasswordField( final int columns ) {
		this( null, columns );
	}
	
	/**
	 * Creates a new {@link XPasswordField}.
	 * 
	 * @param text initial text of the password field
	 * @param columns the number of columns to use to calculate the preferred width
	 */
	public XPasswordField( final String text, final int columns ) {
		super( text, columns );
	}
	
	@Override
	public XPasswordField asPasswordField() {
		return this;
	}
	
}
