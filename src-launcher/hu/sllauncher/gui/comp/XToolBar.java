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

import hu.scelightapibase.gui.comp.IToolBar;

import javax.swing.Box;
import javax.swing.JToolBar;

/**
 * An extended {@link JToolBar}.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class XToolBar extends JToolBar implements IToolBar {
	
	/**
	 * Creates a new {@link XToolBar}.
	 */
	public XToolBar() {
		this( true );
	}
	
	/**
	 * Creates a new {@link XToolBar}.
	 * 
	 * @param initialSpace tells if initial space component is to be added
	 */
	public XToolBar( final boolean initialSpace ) {
		setFloatable( false );
		
		if ( initialSpace )
			add( Box.createHorizontalStrut( 5 ) );
	}
	
	@Override
	public XToolBar asToolBar() {
		return this;
	}
	
	@Override
	public void finalizeLayout() {
		addSeparator();
		add( new BorderPanel() ); // To consume remaining space, (else Metal LaF stretches content...)
	}
	
}
