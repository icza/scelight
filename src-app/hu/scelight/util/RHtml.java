/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.util;

import hu.scelight.r.R;
import hu.sllauncher.util.LRHtml;

import java.net.URL;
import java.util.Set;

/**
 * HTML template resource.
 * 
 * <p>
 * HTML templates may contain parameters which have the following syntax: <code>${paramName}</code>. The values of the parameters will be provided by the code
 * and will be substituted.<br>
 * There are general, pre-defined parameters which can be used without declaration (and they cannot be overridden): {@link #GENERAL_PARAMS}.
 * </p>
 * 
 * @author Andras Belicza
 */
public class RHtml extends LRHtml {
	
	/**
	 * Creates a new {@link RHtml}.
	 * <p>
	 * The resource locator is acquired by calling {@link R#get(String)}.
	 * </p>
	 * 
	 * @param title title of the HTML template
	 * @param resource HTML template resource
	 * @param params param name - param value pairs to be substituted in the HTML template
	 */
	public RHtml( final String title, final String resource, final String... params ) {
		this( title, resource, null, params );
	}
	
	/**
	 * Creates a new {@link RHtml}.
	 * <p>
	 * The resource locator is acquired by calling {@link R#get(String)}.
	 * </p>
	 * 
	 * @param title title of the HTML template
	 * @param resource HTML template resource
	 * @param htmlParams collection of HTML param names, values of these parameters are treated as HTML and are not escaped
	 * @param params param name - param value pairs to be substituted in the HTML template
	 */
	public RHtml( final String title, final String resource, final Set< String > htmlParams, final String... params ) {
		this( title, R.get( resource ), htmlParams, params );
	}
	
	/**
	 * Creates a new {@link RHtml}.
	 * 
	 * @param title title of the HTML template
	 * @param resource HTML template resource
	 * @param params param name - param value pairs to be substituted in the HTML template;<br>
	 *            param names must be in <i>raw</i> format, they will be prepended with <code>${</code> and postpended with <code>}</code>
	 */
	public RHtml( final String title, final URL resource, final String... params ) {
		super( title, resource, null, params );
	}
	
	/**
	 * Creates a new {@link RHtml}.
	 * 
	 * @param title title of the HTML template
	 * @param resource HTML template resource
	 * @param htmlParams collection of HTML param names, values of these parameters are treated as HTML and are not escaped
	 * @param params param name - param value pairs to be substituted in the HTML template;<br>
	 *            param names must be in <i>raw</i> format, they will be prepended with <code>${</code> and postpended with <code>}</code>
	 */
	public RHtml( final String title, final URL resource, final Set< String > htmlParams, final String... params ) {
		super( title, resource, htmlParams, params );
	}
	
}
