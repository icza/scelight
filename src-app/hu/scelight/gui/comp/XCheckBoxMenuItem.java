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

import hu.scelightapi.gui.comp.ICheckBoxMenuItem;
import hu.sllauncher.util.gui.TextWithMnemonic;

import javax.swing.Icon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;

/**
 * An extended {@link JCheckBoxMenuItem}.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class XCheckBoxMenuItem extends JCheckBoxMenuItem implements ICheckBoxMenuItem {
	
	/**
	 * Creates a new {@link XCheckBoxMenuItem}.
	 */
	public XCheckBoxMenuItem() {
		this( null, null );
	}
	
	/**
	 * Creates a new {@link XCheckBoxMenuItem}.
	 * 
	 * @param text text of the check box menu item
	 */
	public XCheckBoxMenuItem( final String text ) {
		this( text, null );
	}
	
	/**
	 * Creates a new {@link XCheckBoxMenuItem}.
	 * 
	 * @param icon icon of the check box menu item
	 */
	public XCheckBoxMenuItem( final Icon icon ) {
		this( null, icon );
	}
	
	/**
	 * Creates a new {@link XCheckBoxMenuItem}.
	 * 
	 * @param text text of the check box menu item
	 * @param icon icon of the check box menu item
	 */
	public XCheckBoxMenuItem( final String text, final Icon icon ) {
		super( text );
		setIcon( icon );
		
		// Mnemonic is somehow not set by the super call, so take care of it:
		setText( text );
	}
	
	@Override
	public JMenuItem asMenuItem() {
		return this;
	}
	
	@Override
	public XCheckBoxMenuItem asCheckBoxMenuItem() {
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
