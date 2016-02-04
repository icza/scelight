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

import hu.scelightapibase.util.HasResource;

import java.net.URL;

/**
 * Static resource holder.
 * 
 * <p>
 * Implementation is IMMUTABLE.
 * </p>
 * 
 * @author Andras Belicza
 */
public class ResourceHolder implements HasResource {
	
	/** Uniform resource locator. */
	public final URL resource;
	
	/**
	 * Creates a new {@link ResourceHolder}.
	 * 
	 * @param resource uniform resource locator
	 */
	public ResourceHolder( final URL resource ) {
		if ( resource == null )
			throw new RuntimeException( "Resource not found!" );
		
		this.resource = resource;
	}
	
	@Override
	public URL getResource() {
		return resource;
	}
	
}
