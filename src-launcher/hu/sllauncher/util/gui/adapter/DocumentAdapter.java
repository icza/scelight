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

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * An adapter-like class for the {@link DocumentListener} interface.
 * 
 * <p>
 * This utility class directs all event handler methods to the {@link #handleEvent(DocumentEvent)} method, so clients have to implement only this method.
 * </p>
 * 
 * @author Andras Belicza
 */
public abstract class DocumentAdapter implements DocumentListener {
	
	/**
	 * Tells if we're during initialization.<br>
	 * Can be used to detect the first {@link #handleEvent(DocumentEvent)} call from the {@link #DocumentAdapter(boolean)} constructor.
	 */
	protected boolean duringInit;
	
	/**
	 * Creates a new {@link DocumentAdapter}.
	 */
	public DocumentAdapter() {
		this( false );
	}
	
	/**
	 * Creates a new {@link DocumentAdapter}.
	 * 
	 * @param init tells if {@link #handleEvent(DocumentEvent)} is to be called now (with a <code>null</code> event)
	 */
	public DocumentAdapter( final boolean init ) {
		if ( duringInit = init ) {
			handleEvent( null );
			duringInit = false;
		}
	}
	
	@Override
	public void insertUpdate( final DocumentEvent event ) {
		handleEvent( event );
	}
	
	@Override
	public void removeUpdate( final DocumentEvent event ) {
		handleEvent( event );
	}
	
	@Override
	public void changedUpdate( final DocumentEvent event ) {
		handleEvent( event );
	}
	
	/**
	 * Method that is called when a document event is fired.
	 * 
	 * @param event the document event
	 */
	public abstract void handleEvent( DocumentEvent event );
	
}
