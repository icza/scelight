/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapibase.gui.comp;

import javax.swing.JEditorPane;

/**
 * A browser component based on {@link JEditorPane}.
 * 
 * <p>
 * By default editing is disabled, and handles hyperlinks (shows proper tool tip text and opens them in the default external browser).
 * </p>
 * 
 * @author Andras Belicza
 * 
 * @see hu.scelightapi.service.IGuiFactory#newBrowser()
 * @see hu.scelightapi.service.IGuiFactory#newBrowserPage(String, hu.scelightapibase.gui.icon.IRIcon, hu.scelightapibase.util.IRHtml)
 */
public interface IBrowser {
	
	/**
	 * Casts this instance to {@link JEditorPane}.
	 * 
	 * @return <code>this</code> as a {@link JEditorPane}
	 */
	JEditorPane asEditorPanel();
	
	/**
	 * Sets the specified message.
	 * 
	 * <p>
	 * The message must be a plain text message.
	 * </p>
	 * 
	 * @param msg error message to be set
	 */
	void setMessage( String msg );
	
	/**
	 * Sets the specified (error) message.
	 * 
	 * <p>
	 * The message must be a plain text message.
	 * </p>
	 * 
	 * @param msg (error) message to be set
	 * @param error tells if the message is an error message
	 */
	void setMessage( String msg, boolean error );
	
}
