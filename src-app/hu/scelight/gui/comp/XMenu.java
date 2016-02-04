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

import hu.scelightapi.gui.comp.IMenu;
import hu.sllauncher.util.gui.TextWithMnemonic;

import javax.swing.Icon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

/**
 * An extended {@link JMenu}.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class XMenu extends JMenu implements IMenu {
	
	/**
	 * Creates a new {@link XMenu}.
	 */
	public XMenu() {
		this( null, null );
	}
	
	/**
	 * Creates a new {@link XMenu}.
	 * 
	 * @param text text of the menu
	 */
	public XMenu( final String text ) {
		this( text, null );
	}
	
	/**
	 * Creates a new {@link XMenu}.
	 * 
	 * @param icon icon of the menu
	 */
	public XMenu( final Icon icon ) {
		this( null, icon );
	}
	
	/**
	 * Creates a new {@link XMenu}.
	 * 
	 * @param text text of the menu
	 * @param icon icon of the menu
	 */
	public XMenu( final String text, final Icon icon ) {
		super( text );
		setIcon( icon );
	}
	
	@Override
	public JMenuItem asMenuItem() {
		return this;
	}
	
	@Override
	public XMenu asMenu() {
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
	
}
