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

import hu.scelightapibase.gui.comp.IProgressBar;

import javax.swing.JProgressBar;

/**
 * An extended {@link JProgressBar}.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class XProgressBar extends JProgressBar implements IProgressBar {
	
	/**
	 * Creates a new {@link XProgressBar}.
	 */
	public XProgressBar() {
	}
	
	@Override
	public XProgressBar asProgressBar() {
		return this;
	}
	
	@Override
	public void setAborted() {
		setString( "ABORTED!  [" + getString() + "]" );
	}
	
	@Override
	public String toString() {
		return getString();
	}
	
	@Override
	public int compareTo( final IProgressBar pb ) {
		return getValue() - pb.getValue();
	}
	
}
