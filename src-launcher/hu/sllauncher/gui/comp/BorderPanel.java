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

import hu.scelightapibase.gui.comp.IBorderPanel;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * A {@link JPanel} with {@link BorderLayout} and with some helper methods.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class BorderPanel extends JPanel implements IBorderPanel {
	
	/**
	 * Creates a new {@link BorderPanel}.
	 */
	public BorderPanel() {
		super( new BorderLayout() );
	}
	
	/**
	 * Creates a new {@link BorderPanel} and adds the specified component to the center.
	 * 
	 * @param centerComp component to be added to the center
	 */
	public BorderPanel( final JComponent centerComp ) {
		this();
		
		addCenter( centerComp );
	}
	
	@Override
	public BorderPanel asPanel() {
		return this;
	}
	
	@Override
	public void addCenter( final JComponent c ) {
		add( c, BorderLayout.CENTER );
	}
	
	@Override
	public void addNorth( final JComponent c ) {
		add( c, BorderLayout.NORTH );
	}
	
	@Override
	public void addSouth( final JComponent c ) {
		add( c, BorderLayout.SOUTH );
	}
	
	@Override
	public void addEast( final JComponent c ) {
		add( c, BorderLayout.EAST );
	}
	
	@Override
	public void addWest( final JComponent c ) {
		add( c, BorderLayout.WEST );
	}
	
}
