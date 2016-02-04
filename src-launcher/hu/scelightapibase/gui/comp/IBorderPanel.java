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

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * A {@link JPanel} with {@link BorderLayout} and with some helper methods.
 * 
 * @author Andras Belicza
 * 
 * @see hu.scelightapi.service.IGuiFactory#newBorderPanel()
 * @see hu.scelightapi.service.IGuiFactory#newBorderPanel(JComponent)
 */
public interface IBorderPanel {
	
	/**
	 * Casts this instance to {@link JPanel}.
	 * 
	 * @return <code>this</code> as a {@link JPanel}
	 */
	JPanel asPanel();
	
	/**
	 * Adds the specified component to the center.
	 * 
	 * @param c component to be added to the center
	 */
	void addCenter( JComponent c );
	
	/**
	 * Adds the specified component to the north.
	 * 
	 * @param c component to be added to the north
	 */
	void addNorth( JComponent c );
	
	/**
	 * Adds the specified component to the south.
	 * 
	 * @param c component to be added to the south
	 */
	void addSouth( JComponent c );
	
	/**
	 * Adds the specified component to the east.
	 * 
	 * @param c component to be added to the east
	 */
	void addEast( JComponent c );
	
	/**
	 * Adds the specified component to the west.
	 * 
	 * @param c component to be added to the west
	 */
	void addWest( JComponent c );
	
}
