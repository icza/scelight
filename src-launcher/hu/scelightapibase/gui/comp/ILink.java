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

import hu.scelightapibase.util.iface.Consumer;

import java.awt.event.MouseEvent;
import java.net.URL;

/**
 * A clickable, link-styled {@link ILabel}.
 * 
 * <p>
 * The link text is interpreted as a pre-formatted text and is properly encoded if contains HTML unsafe characters.<br>
 * A link may have a target URL or a {@link Consumer} defining an action to be called when clicked.
 * </p>
 * 
 * <p>
 * If a target {@link URL} is set, it will be returned as the default tool tip.
 * </p>
 * 
 * @author Andras Belicza
 * 
 * @see hu.scelightapi.service.IGuiFactory#newLink()
 * @see hu.scelightapi.service.IGuiFactory#newLink(String)
 * @see hu.scelightapi.service.IGuiFactory#newLink(String, URL)
 * @see hu.scelightapi.service.IGuiFactory#newLink(String, Consumer)
 * @see hu.scelightapi.service.IGuiFactory#newLink(String, URL, Consumer)
 * @see hu.scelightapi.util.gui.IGuiUtils#linkForAction(String, hu.scelightapibase.action.IAction)
 */
public interface ILink extends ILabel {
	
	/**
	 * Returns the {@link URL} to be opened when clicked.
	 * 
	 * @return the {@link URL} to be opened when clicked
	 */
	URL getUrl();
	
	/**
	 * Sets the the {@link URL} to be opened when clicked.
	 * 
	 * @param url the {@link URL} to be opened when clicked
	 */
	void setUrl( URL url );
	
	/**
	 * Returns the consumer to be called when clicked.
	 * 
	 * @return the consumer to be called when clicked
	 */
	Consumer< MouseEvent > getConsumer();
	
	/**
	 * Sets the consumer to be called when clicked.
	 * 
	 * @param consumer the consumer to be called when clicked
	 */
	void setConsumer( Consumer< MouseEvent > consumer );
	
}
