/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.belicza.andras.util;

import java.util.ArrayList;
import java.util.List;

/**
 * A general, generic listener registry which supports listener registration and event firing / dispatching.
 * 
 * <p>
 * Implementation is thread safe.
 * </p>
 * 
 * @param <T> listener type
 * @param <E> event type that can be fired to the registered listeners
 * 
 * @author Andras Belicza
 */
public abstract class ListenerRegistry< T, E > {
	
	/** List of registered listeners. */
	private final ArrayList< T > listenerList = new ArrayList<>();
	
	/**
	 * Adds the specified listener.
	 * 
	 * @param listener listener to be added
	 */
	public synchronized void addListener( final T listener ) {
		listenerList.add( listener );
	}
	
	/**
	 * Removes the specified listener.
	 * 
	 * @param listener listener to be removed
	 */
	public synchronized void removeListener( final T listener ) {
		listenerList.remove( listener );
	}
	
	/**
	 * Fires the specified event to the registered listeners
	 * 
	 * @param event event to be fired
	 */
	@SuppressWarnings( "unchecked" )
	public void fireEvent( final E event ) {
		// Clone the list so we don't have to block while listeners are being executed
		final List< T > listenerList;
		synchronized ( this ) {
			// I use ArrayList's clone() method because it is more efficient than the constructor that takes a collection.
			listenerList = (List< T >) this.listenerList.clone();
		}
		
		for ( final T listener : listenerList )
			notify( listener, event );
	}
	
	/**
	 * Notifies the specified listener with the specified event.
	 * 
	 * @param listener listener to be notified
	 * @param event event to be passed
	 */
	protected abstract void notify( T listener, E event );
	
}
