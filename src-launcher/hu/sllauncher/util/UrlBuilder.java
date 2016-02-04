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

import hu.scelightapibase.util.IUrlBuilder;
import hu.scelightopapibase.ScelightOpApiBase;
import hu.sllauncher.service.env.LEnv;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Helper class to build {@link URL}s.
 * 
 * <p>
 * Main goal of this class is to help adding query parameters to a URL.
 * </p>
 * 
 * @author Andras Belicza
 */
public class UrlBuilder implements IUrlBuilder {
	
	/** URL-safe (meaning no need to URL-encode) timestamp chars to use. */
	private static final char[]   TIMESTAMP_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz.-".toCharArray();
	
	
	/** String builder used to construct the string URL. */
	protected final StringBuilder sb;
	
	/** Tells if there are parameters added. */
	protected boolean             paramsAdded     = false;
	
	/**
	 * Creates a new {@link UrlBuilder}.
	 * 
	 * @param spec base URL specification, may contain a query part but must not contain a reference part
	 * @throws IllegalArgumentException if the spec is a malformed URL spec or it contains a reference part
	 */
	public UrlBuilder( final String spec ) throws IllegalArgumentException {
		this( LUtils.createUrl( spec ) );
	}
	
	/**
	 * Creates a new {@link UrlBuilder}.
	 * 
	 * @param url base URL to extend, may contain a query part but must not contain a reference part
	 * @throws IllegalArgumentException if the specified URL contains a reference part
	 */
	public UrlBuilder( final URL url ) throws IllegalArgumentException {
		if ( url.getRef() != null )
			throw new IllegalArgumentException( "URL contains a reference part: " + url );
		
		paramsAdded = url.getQuery() != null;
		
		sb = new StringBuilder( url.toString() );
	}
	
	@Override
	public UrlBuilder addParam( final String name, final Object value ) {
		prepareAddParam();
		
		try {
			sb.append( name ).append( '=' ).append( URLEncoder.encode( value == null ? "null" : value.toString(), "UTF-8" ) );
		} catch ( final UnsupportedEncodingException uee ) {
			// Never to happen
			LEnv.LOGGER.error( "Unsupported encoding exception!", uee );
		}
		
		return this;
	}
	
	/**
	 * Adds a timestamp query parameter ensuring that the URL will be unique and therefore its response won't be cached.
	 * 
	 * <p>
	 * The appended timestamp parameter name is {@link ScelightOpApiBase#PARAM_GENERAL_TIMESTAMP} and its value is the value returned by
	 * {@link System#currentTimeMillis()} in radix 64, reversed.
	 * </p>
	 * 
	 * @return <code>this</code> for chaining
	 */
	@Override
	public UrlBuilder addTimestamp() {
		prepareAddParam();
		
		sb.append( ScelightOpApiBase.PARAM_GENERAL_TIMESTAMP ).append( '=' );
		
		// The next cycle would not write anything in case of 0 time, but time is always greater than 0, so no need to check.
		for ( long t = System.currentTimeMillis(); t > 0; t >>= 6 )
			sb.append( TIMESTAMP_CHARS[ (int) ( t & 0x3f ) ] );
		
		return this;
	}
	
	/**
	 * Prepares adding a param, so after this param can simply be appended.
	 */
	private void prepareAddParam() {
		if ( paramsAdded )
			sb.append( '&' );
		else {
			sb.append( '?' );
			paramsAdded = true;
		}
	}
	
	@Override
	public URL toUrl() {
		try {
			return new URL( sb.toString() );
		} catch ( final MalformedURLException mue ) {
			// Never to happen since we check URL spec in the constructor and we only add query parameters, properly encoded
			throw new RuntimeException( "Malformed URL: " + sb.toString(), mue );
		}
	}
	
}
