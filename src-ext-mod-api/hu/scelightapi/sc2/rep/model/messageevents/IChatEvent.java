/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapi.sc2.rep.model.messageevents;

/**
 * Chat message event.
 * 
 * @author Andras Belicza
 */
public interface IChatEvent extends IMsgEvent {
	
	/** Chat string event field name. */
	String F_STRING = "string";
	
	
	/**
	 * Returns the chat text.
	 * 
	 * @return the chat text
	 */
	String getText();
	
}
