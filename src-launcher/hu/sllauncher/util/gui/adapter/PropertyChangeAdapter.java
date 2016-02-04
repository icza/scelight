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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * An adapter-like class for the {@link PropertyChangeListener} interface.
 * 
 * @author Andras Belicza
 */
public abstract class PropertyChangeAdapter implements PropertyChangeListener {
	
	/**
	 * Tells if we're during initialization.<br>
	 * Can be used to detect the first {@link #propertyChange(PropertyChangeEvent)} call from the {@link #PropertyChangeAdapter(boolean)} constructor.
	 */
	protected boolean duringInit;
	
	/**
	 * Creates a new {@link PropertyChangeAdapter}.
	 */
	public PropertyChangeAdapter() {
		this( false );
	}
	
	/**
	 * Creates a new {@link PropertyChangeAdapter}.
	 * 
	 * @param init tells if {@link #propertyChange(PropertyChangeEvent)} is to be called now (with a <code>null</code> event)
	 */
	public PropertyChangeAdapter( final boolean init ) {
		if ( duringInit = init ) {
			propertyChange( null );
			duringInit = false;
		}
	}
	
}
