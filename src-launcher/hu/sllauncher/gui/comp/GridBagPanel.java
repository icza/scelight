/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllauncher.gui.comp;

import hu.scelightapibase.gui.comp.IGridBagPanel;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;

/**
 * A {@link JPanel} with {@link GridBagLayout} and with some helper methods.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class GridBagPanel extends JPanel implements IGridBagPanel {
	
	/** Grid bag constraints used when adding components. */
	public final GridBagConstraints c = new GridBagConstraints();
	
	/**
	 * Creates a new {@link GridBagPanel}.
	 */
	public GridBagPanel() {
		super( new GridBagLayout() );
		
		defaultConstraints();
		
		// Initial grid y (adding to the first line must start with incrementing this)
		c.gridy = -1;
	}
	
	@Override
	public GridBagPanel asPanel() {
		return this;
	}
	
	@Override
	public GridBagConstraints c() {
		return c;
	}
	
	@Override
	public void defaultConstraints() {
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets.set( 2, 1, 2, 1 );
	}
	
	@Override
	public void nextRow() {
		c.gridy++;
	}
	
	@Override
	public void addSingle( final Component component ) {
		add( component, c );
	}
	
	@Override
	public void addDouble( final Component component ) {
		c.gridwidth = 2;
		add( component, c );
		c.gridwidth = 1;
	}
	
	@Override
	public void addTriple( final Component component ) {
		c.gridwidth = 3;
		add( component, c );
		c.gridwidth = 1;
	}
	
	@Override
	public void addRemainder( final Component component ) {
		c.gridwidth = GridBagConstraints.REMAINDER;
		add( component, c );
		c.gridwidth = 1;
	}
	
}
