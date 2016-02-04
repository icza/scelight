/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllauncher.util;

import hu.scelightapibase.util.IHolder;

/**
 * A generic reference holder object whose reference can be changed.
 * 
 * @author Andras Belicza
 * 
 * @param <T> type of the held object
 */
public class Holder< T > implements IHolder< T > {
	
	/** Reference of the held object */
	public T value;
	
	/**
	 * Creates a new Holder.
	 */
	public Holder() {
	}
	
	/**
	 * Creates a new Holder.
	 * 
	 * @param value reference to the held object
	 */
	public Holder( final T value ) {
		this.value = value;
	}
	
	@Override
	public T get() {
		return value;
	}
	
	@Override
	public void set( final T value ) {
		this.value = value;
	}
	
}
