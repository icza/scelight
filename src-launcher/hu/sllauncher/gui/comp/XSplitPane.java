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

import hu.scelightapibase.gui.comp.ISplitPane;

import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;

/**
 * An extended {@link JSplitPane}.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class XSplitPane extends JSplitPane implements ISplitPane {
	
	/**
	 * Creates a new {@link XSplitPane} using {@link JSplitPane#HORIZONTAL_SPLIT} orientation (the splitter will be vertical).
	 */
	public XSplitPane() {
		this( JSplitPane.HORIZONTAL_SPLIT );
	}
	
	/**
	 * Creates a new {@link XSplitPane}.
	 * 
	 * @param orientation orientation ({@link JSplitPane#HORIZONTAL_SPLIT} or {@link JSplitPane#VERTICAL_SPLIT})
	 */
	public XSplitPane( final int orientation ) {
		super( orientation );
		
		setOneTouchExpandable( true );
	}
	
	@Override
	public XSplitPane asSplitPane() {
		return this;
	}
	
	@Override
	public void setDividerLocationLater( final double proportionalLocation ) {
		SwingUtilities.invokeLater( new Runnable() {
			@Override
			public void run() {
				setDividerLocation( proportionalLocation );
			}
		} );
	}
	
}
