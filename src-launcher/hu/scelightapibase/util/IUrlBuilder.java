/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapibase.util;

import java.net.URL;

/**
 * Helper class to build {@link URL}s.
 * 
 * <p>
 * Main goal of this class is to help adding query parameters to a URL.
 * </p>
 * 
 * @author Andras Belicza
 * 
 * @see hu.scelightapi.util.httppost.IHttpPost
 */
public interface IUrlBuilder {
	
	/**
	 * Adds a new query parameter.
	 * 
	 * @param name name of the parameter to be added
	 * @param value value of the parameter to be added (<code>value.toString()</code> will be used)
	 * @return <code>this</code> for chaining
	 */
	IUrlBuilder addParam( String name, Object value );
	
	/**
	 * Adds a timestamp query parameter ensuring that the URL will be unique and therefore its response won't be cached.
	 * 
	 * <p>
	 * The appended timestamp parameter name is <code>t</code> and its value is the value returned by {@link System#currentTimeMillis()} in radix 64, reversed.
	 * </p>
	 * 
	 * @return <code>this</code> for chaining
	 */
	IUrlBuilder addTimestamp();
	
	/**
	 * Builds and returns the {@link URL}.
	 * 
	 * @return the built {@link URL}
	 */
	URL toUrl();
	
}
