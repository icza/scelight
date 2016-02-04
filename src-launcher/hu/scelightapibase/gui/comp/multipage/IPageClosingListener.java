/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapibase.gui.comp.multipage;

/**
 * Page closing listener.
 * 
 * @author Andras Belicza
 */
public interface IPageClosingListener {
	
	/**
	 * Called before the page is closed.
	 * 
	 * @return whether close is allowed; a return value of false vetoes the close
	 */
	boolean pageClosing();
	
}
