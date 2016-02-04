/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllauncher.action;

import hu.sllauncher.gui.icon.LRIcon;
import hu.sllauncher.service.env.LEnv;
import hu.sllauncher.util.LUtils;

import java.awt.event.ActionEvent;
import java.net.URL;

import javax.swing.JComponent;
import javax.swing.KeyStroke;

/**
 * An action which opens a {@link URL} in the default browser.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class UrlAction extends XAction {
	
	/** {@link URL} to be opened. */
	private final URL url;
	
	/**
	 * Creates a new {@link UrlAction}.
	 * 
	 * @param text text of the action
	 * @param url {@link URL} to be opened
	 */
	public UrlAction( final String text, final URL url ) {
		this( text, url, null );
	}
	
	/**
	 * Creates a new {@link UrlAction}.
	 * 
	 * @param text text of the action
	 * @param url {@link URL} to be opened
	 * @param comp component to bind the action's setting change listener to; basically in this case the lifetime of the scl will be bound to the specified
	 *            component's lifetime
	 */
	public UrlAction( final String text, final URL url, final JComponent comp ) {
		this( null, LUtils.urlIcon( url ), text, url );
	}
	
	/**
	 * Creates a new {@link UrlAction}.
	 * 
	 * @param ricon optional ricon of the action
	 * @param text text of the action
	 * @param url {@link URL} to be opened
	 */
	public UrlAction( final LRIcon ricon, final String text, final URL url ) {
		this( ricon, text, url, null );
	}
	
	/**
	 * Creates a new {@link UrlAction}.
	 * 
	 * @param ricon optional ricon of the action
	 * @param text text of the action
	 * @param url {@link URL} to be opened
	 * @param comp component to bind the action's setting change listener to; basically in this case the lifetime of the scl will be bound to the specified
	 *            component's lifetime
	 */
	public UrlAction( final LRIcon ricon, final String text, final URL url, final JComponent comp ) {
		this( null, ricon, text, url );
	}
	
	/**
	 * Creates a new {@link UrlAction}.
	 * 
	 * @param keyStroke optional hotkey of the action
	 * @param ricon optional ricon of the action
	 * @param text text of the action
	 * @param url {@link URL} to be opened
	 */
	public UrlAction( final KeyStroke keyStroke, final LRIcon ricon, final String text, final URL url ) {
		this( keyStroke, ricon, text, url, null );
	}
	
	/**
	 * Creates a new {@link UrlAction}.
	 * 
	 * @param keyStroke optional hotkey of the action
	 * @param ricon optional ricon of the action
	 * @param text text of the action
	 * @param url {@link URL} to be opened
	 * @param comp component to bind the action's setting change listener to; basically in this case the lifetime of the scl will be bound to the specified
	 *            component's lifetime
	 */
	public UrlAction( final KeyStroke keyStroke, final LRIcon ricon, final String text, final URL url, final JComponent comp ) {
		super( keyStroke, ricon, text, comp );
		
		this.url = url;
	}
	
	@Override
	public void actionPerformed( final ActionEvent event ) {
		LEnv.UTILS_IMPL.get().showURLInBrowser( url );
	}
	
}
