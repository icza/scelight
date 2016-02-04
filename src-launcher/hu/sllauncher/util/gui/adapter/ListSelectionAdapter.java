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

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * An adapter-like class for the {@link ListSelectionListener} interface.
 * 
 * @author Andras Belicza
 */
public abstract class ListSelectionAdapter implements ListSelectionListener {
	
	/**
	 * Tells if we're during initialization.<br>
	 * Can be used to detect the first {@link #valueChanged(ListSelectionEvent)} call from the {@link #ListSelectionAdapter(boolean)} constructor.
	 */
	protected boolean duringInit;
	
	/**
	 * Creates a new {@link ListSelectionAdapter}.
	 */
	public ListSelectionAdapter() {
		this( false );
	}
	
	/**
	 * Creates a new {@link ListSelectionAdapter}.
	 * 
	 * @param init tells if {@link #valueChanged(ListSelectionEvent)} is to be called now (with a <code>null</code> event)
	 */
	public ListSelectionAdapter( final boolean init ) {
		if ( duringInit = init ) {
			valueChanged( null );
			duringInit = false;
		}
	}
	
}
