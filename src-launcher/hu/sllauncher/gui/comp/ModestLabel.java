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

import hu.scelightapibase.gui.comp.IModestLabel;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * A modest {@link XLabel} which is disabled by default, but becomes enabled when/while mouse is over.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class ModestLabel extends XLabel implements IModestLabel {
	
	/**
	 * Creates a new {@link ModestLabel}.
	 */
	public ModestLabel() {
		this( null );
	}
	
	/**
	 * Creates a new {@link ModestLabel}.
	 * 
	 * @param text text of the label
	 */
	public ModestLabel( final String text ) {
		super( text );
		
		setEnabled( false );
		
		addMouseListener( new MouseAdapter() {
			@Override
			public void mouseEntered( final MouseEvent event ) {
				setEnabled( true );
			}
			
			@Override
			public void mouseExited( final MouseEvent event ) {
				setEnabled( false );
			}
		} );
	}
	
}
