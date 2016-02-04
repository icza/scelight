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

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;

/**
 * A {@link JPanel} with {@link GridBagLayout} and with some helper methods.
 * 
 * @author Andras Belicza
 * 
 * @see hu.scelightapi.service.IGuiFactory#newGridBagPanel()
 */
public interface IGridBagPanel {
	
	/**
	 * Casts this instance to {@link JPanel}.
	 * 
	 * @return <code>this</code> as a {@link JPanel}
	 */
	JPanel asPanel();
	
	/**
	 * Returns the grid bag constraints used when adding components.
	 * 
	 * @return the grid bag constraints used when adding components
	 */
	GridBagConstraints c();
	
	/**
	 * Restores the default constraints.
	 */
	void defaultConstraints();
	
	/**
	 * Configures the constraints for the next row (increments <code>gridy</code>).
	 */
	void nextRow();
	
	/**
	 * Adds the specified component to this panel using our constraints.
	 * 
	 * @param component component to be added
	 */
	void addSingle( Component component );
	
	/**
	 * Adds the specified component to this panel using our constraints but modified to take 2 horizontal cells ( <code>gridwidth = 2</code> which is restored
	 * after).
	 * 
	 * @param component component to be added
	 */
	void addDouble( Component component );
	
	/**
	 * Adds the specified component to this panel using our constraints but modified to take 3 horizontal cells ( <code>gridwidth = 3</code> which is restored
	 * after).
	 * 
	 * @param component component to be added
	 */
	void addTriple( Component component );
	
	/**
	 * Adds the specified component to this panel using our constraints but modified to be the last in the row (<code>gridwidth =
	 * {@link GridBagConstraints#REMAINDER}</code> which is restored after).
	 * 
	 * @param component component to be added
	 */
	void addRemainder( Component component );
	
}
