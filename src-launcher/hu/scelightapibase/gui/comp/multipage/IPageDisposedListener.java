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
 * Page disposed listener.
 * 
 * @author Andras Belicza
 */
public interface IPageDisposedListener {
	
	/**
	 * Called when the page component is disposed, no longer needed or referenced.<br>
	 * Resources that were allocated by the page can be freed.
	 */
	void pageDisposed();
	
}
