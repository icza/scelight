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

import hu.scelightapibase.action.IAction;

import javax.swing.JButton;

/**
 * An extended {@link IToolBar} which listens the selection changes of a component, and can have buttons and an info text which gets enabled / disabled based on
 * the selection.
 * 
 * @author Andras Belicza
 */
public interface ISelectionListenerToolBar extends IToolBar {
	
	/**
	 * Adds a new info label with the specified text which will be automatically disabled if there are selected rows and enabled if nothing is selected in the
	 * targeted table.<br>
	 * Also adds a separator after the label.
	 * 
	 * @param text info text to create and add a label for
	 * @return the added label
	 */
	ILabel addSelectInfoLabel( String text );
	
	/**
	 * Adds a new button associated with the specified action.<br>
	 * The newly added button will be automatically enabled/disabled when the listened component's selection changes.
	 * 
	 * @param action action to be associated with the button
	 * @return the added button
	 */
	JButton addSelEnabledButton( IAction action );
	
}
