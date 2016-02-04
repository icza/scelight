/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllauncher.util.gui.adapter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * An adapter-like class for the {@link ActionListener} interface.
 * 
 * @author Andras Belicza
 */
public abstract class ActionAdapter implements ActionListener {
	
	/**
	 * Tells if we're during initialization.<br>
	 * Can be used to detect the first {@link #actionPerformed(ActionEvent)} call from the {@link #ActionAdapter(boolean)} constructor.
	 */
	protected boolean duringInit;
	
	/**
	 * Creates a new {@link ActionAdapter}.
	 */
	public ActionAdapter() {
		this( false );
	}
	
	/**
	 * Creates a new {@link ActionAdapter}.
	 * 
	 * @param init tells if {@link #actionPerformed(ActionEvent)} is to be called now (with a <code>null</code> event)
	 */
	public ActionAdapter( final boolean init ) {
		if ( duringInit = init ) {
			actionPerformed( null );
			duringInit = false;
		}
	}
	
}
