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

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

/**
 * An adapter-like class for the {@link TreeSelectionListener} interface.
 * 
 * @author Andras Belicza
 */
public abstract class TreeSelectionAdapter implements TreeSelectionListener {
	
	/**
	 * Tells if we're during initialization.<br>
	 * Can be used to detect the first {@link #valueChanged(TreeSelectionEvent)} call from the {@link #TreeSelectionAdapter(boolean)} constructor.
	 */
	protected boolean duringInit;
	
	/**
	 * Creates a new {@link TreeSelectionAdapter}.
	 */
	public TreeSelectionAdapter() {
		this( false );
	}
	
	/**
	 * Creates a new {@link TreeSelectionAdapter}.
	 * 
	 * @param init tells if {@link #valueChanged(TreeSelectionEvent)} is to be called now (with a <code>null</code> event)
	 */
	public TreeSelectionAdapter( final boolean init ) {
		if ( duringInit = init ) {
			valueChanged( null );
			duringInit = false;
		}
	}
	
}
