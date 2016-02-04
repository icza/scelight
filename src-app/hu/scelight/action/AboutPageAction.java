/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.action;

import hu.scelight.gui.dialog.AboutDialog;
import hu.scelightapibase.gui.comp.multipage.IPage;
import hu.sllauncher.action.XAction;

import java.awt.event.ActionEvent;

import javax.swing.KeyStroke;

/**
 * An action which opens the About dialog having the specified page selected.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class AboutPageAction extends XAction {
	
	/** Class of the page to be selected. */
	private final Class< ? extends IPage< ? > > pageClass;
	
	/**
	 * Creates a new {@link AboutPageAction}.
	 * 
	 * @param page page to be selected
	 */
	public AboutPageAction( final IPage< ? > page ) {
		this( null, page );
	}
	
	/**
	 * Creates a new {@link AboutPageAction}.
	 * 
	 * @param keyStroke optional hotkey of the action
	 * @param page page to be selected
	 */
	@SuppressWarnings( "unchecked" )
	public AboutPageAction( final KeyStroke keyStroke, final IPage< ? > page ) {
		super( keyStroke, page.getRicon(), page.getDisplayName() + "..." );
		
		this.pageClass = (Class< ? extends IPage< ? > >) page.getClass();
	}
	
	@Override
	public void actionPerformed( final ActionEvent event ) {
		new AboutDialog( pageClass );
	}
	
}
