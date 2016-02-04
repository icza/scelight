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

import hu.scelightapibase.gui.comp.ICheckBox;

import javax.swing.JCheckBox;

/**
 * An extended {@link JCheckBox}.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class XCheckBox extends JCheckBox implements ICheckBox {
	
	/**
	 * Creates a new {@link XCheckBox}.
	 */
	public XCheckBox() {
		this( null, false );
	}
	
	/**
	 * Creates a new {@link XCheckBox}.
	 * 
	 * @param text text of the check box
	 */
	public XCheckBox( final String text ) {
		this( text, false );
	}
	
	/**
	 * Creates a new {@link XCheckBox}.
	 * 
	 * @param text text of the check box
	 * @param selected initial selected state
	 */
	public XCheckBox( final String text, final boolean selected ) {
		super( text, selected );
	}
	
	@Override
	public XCheckBox asCheckBox() {
		return this;
	}
	
}
