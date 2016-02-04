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

import javax.swing.JScrollPane;

/**
 * An improved {@link JScrollPane}.
 * 
 * @author Andras Belicza
 * 
 * @see hu.scelightapi.service.IGuiFactory#newScrollPane()
 * @see hu.scelightapi.service.IGuiFactory#newScrollPane(java.awt.Component)
 * @see hu.scelightapi.service.IGuiFactory#newScrollPane(java.awt.Component, boolean)
 * @see hu.scelightapi.service.IGuiFactory#newScrollPane(java.awt.Component, boolean, boolean)
 */
public interface IScrollPane {
	
	/**
	 * Casts this instance to {@link JScrollPane}.
	 * 
	 * @return <code>this</code> as a {@link JScrollPane}
	 */
	JScrollPane asScrollPane();
	
}
