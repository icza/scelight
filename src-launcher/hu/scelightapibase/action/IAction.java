/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapibase.action;

import hu.scelightapibase.util.gui.HasRIcon;

import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/**
 * Extended action interface.
 * 
 * <p>
 * Action texts might have a mnemonic character indicated with the underscore (<code>'_'</code>). If such indicator is detected, it will be removed from the
 * visible text and the character following the underscore will be set as the action's mnemonic.
 * </p>
 * 
 * <p>
 * If the action has a ricon, the action's large icon ({@link Action#LARGE_ICON_KEY}) will automatically be re-scaled when the setting defining the tool bar
 * icon size changes.
 * </p>
 * 
 * @author Andras Belicza
 * 
 * @see hu.scelightapi.service.IGuiFactory#newAction(hu.scelightapibase.gui.icon.IRIcon, String, java.awt.event.ActionListener)
 * @see hu.scelightapi.service.IGuiFactory#newAction(hu.scelightapibase.gui.icon.IRIcon, String, javax.swing.JComponent, java.awt.event.ActionListener)
 * @see hu.scelightapi.service.IGuiFactory#newAction(javax.swing.KeyStroke, hu.scelightapibase.gui.icon.IRIcon, String, java.awt.event.ActionListener)
 * @see hu.scelightapi.service.IGuiFactory#newAction(javax.swing.KeyStroke, hu.scelightapibase.gui.icon.IRIcon, String, javax.swing.JComponent,
 *      java.awt.event.ActionListener)
 * 
 */
public interface IAction extends Action, HasRIcon {
	
	/**
	 * Returns the action's name (the value associated with the {@link Action#NAME} key).
	 * 
	 * @return the action's name (the value associated with the {@link Action#NAME} key)
	 */
	String getName();
	
	/**
	 * Returns the action's short description (the value associated with the {@link Action#SHORT_DESCRIPTION} key).
	 * 
	 * @return the action's short description (the value associated with the {@link Action#SHORT_DESCRIPTION} key)
	 */
	String getShortDescription();
	
	/**
	 * Adds this action to the specified menu.
	 * 
	 * @param menu menu to add the action to
	 * @return the created and added menu item
	 * 
	 * @see #addToMenu(JPopupMenu)
	 */
	JMenuItem addToMenu( JMenu menu );
	
	/**
	 * Adds this action to the specified popup menu.
	 * 
	 * @param menu popup menu to add the action to
	 * @return the created and added menu item
	 * 
	 * @see #addToMenu(JMenu)
	 */
	JMenuItem addToMenu( JPopupMenu menu );
	
}
