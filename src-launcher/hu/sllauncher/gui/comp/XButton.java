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

import hu.scelightapibase.action.IAction;
import hu.scelightapibase.gui.comp.IButton;
import hu.sllauncher.util.gui.TextWithMnemonic;

import java.awt.Cursor;

import javax.swing.Icon;
import javax.swing.JButton;

/**
 * An extended {@link JButton}.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class XButton extends JButton implements IButton {
	
	/**
	 * Creates a new {@link XButton}.
	 */
	public XButton() {
		this( null, null );
	}
	
	/**
	 * Creates a new {@link XButton}.
	 * 
	 * @param text text of the button
	 */
	public XButton( final String text ) {
		this( text, null );
	}
	
	/**
	 * Creates a new {@link XButton}.
	 * 
	 * @param icon icon of the button
	 */
	public XButton( final Icon icon ) {
		this( null, icon );
	}
	
	/**
	 * Creates a new {@link XButton}.
	 * 
	 * @param text text of the button
	 * @param icon icon of the button
	 */
	public XButton( final String text, final Icon icon ) {
		super( text, icon );
	}
	
	/**
	 * Creates a new {@link XButton}.
	 * 
	 * @param action action to configure the button from
	 */
	public XButton( final IAction action ) {
		super( action );
	}
	
	@Override
	public XButton asButton() {
		return this;
	}
	
	@Override
	public void setText( String text ) {
		if ( text != null ) {
			final TextWithMnemonic twm = new TextWithMnemonic( text );
			if ( twm.hasMnemonic() ) {
				text = twm.text;
				setMnemonic( twm.mnemonic );
			}
		}
		super.setText( text );
	}
	
	@Override
	public void configureAsIconButton() {
		setContentAreaFilled( false );
		setFocusable( false );
		setBorder( null );
		setCursor( Cursor.getPredefinedCursor( Cursor.HAND_CURSOR ) );
	}
	
}
