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

/**
 * HTML template resource.
 * 
 * <p>
 * HTML templates may contain parameters which have the following syntax: <code>${paramName}</code>. The values of the parameters will be provided by the code
 * and will be replaced properly.
 * </p>
 * 
 * <p>
 * There are general, pre-defined parameters which can be used without declaration (and they cannot be overridden):
 * <ul>
 * <li><code>${generalCss}</code>: general CSS in the form of an HTML <code>&lt;style&gt;</code> tag which can be put in the <code>&lt;head&gt;</code> section
 * of HTML documents.
 * <li><code>${appName}</code>: name of the application
 * <li> <code>${appOpName}</code>: name of the application operator
 * </ul>
 * </p>
 * 
 * <p>
 * The general CSS parameter defines the following CSS classes (which can be used in HTML templates if the <code>${generalCss}</code> parameter is inserted in
 * the <code>&lt;head&gt;</code> section):
 * <ul>
 * <li><code>h</code> - Highlighted (used for keywords or important words)
 * <li><code>s</code> - Stressed or Strong (used for a term or name from the application)
 * <li><code>k</code> - Key (used for marking a key or key combo)
 * <li><code>n</code> - Not important (used for marking words unimportant and less visible)
 * <li><code>a</code> - Advanced details (content intended for advanced users)
 * <li><code>D</code> - Definition block
 * <li><code>N</code> - Note block
 * <li><code>T</code> - Tip block
 * <li><code>E</code> - Example block
 * <li><code>W</code> - Warning or very important block
 * </ul>
 * </p>
 * 
 * <br>
 * <p>
 * It is also possible to include the content of another HTML template if that other template is <b>already cached</b>. The factory methods
 * {@link hu.scelightapi.service.IGuiFactory#newRHtml(String, java.net.URL, String...)} and
 * {@link hu.scelightapi.service.IGuiFactory#newRHtml(String, java.net.URL, java.util.Set, String...)} cache the returned HTML resources, so they can be
 * referenced properly.<br>
 * <br>
 * The inclusion has the following syntax:
 * 
 * <pre>
 * ${#&lt;fully-qualified-html-resource-name&gt;}
 * </pre>
 * 
 * The inclusion must be in a new line and must not start with white spaces. If there is a cached HTML template associated with the specified resource name, its
 * content will be included.<br>
 * The inclusion happens before replacing the parameters, so it is possible for the referenced template to have parameters and not specify their values leaving
 * this possibility to the template that includes it.
 * </p>
 * 
 * @author Andras Belicza
 * 
 * @see hu.scelightapi.service.IGuiFactory#newRHtml(String, java.net.URL, String...)
 * @see hu.scelightapi.service.IGuiFactory#newRHtml(String, java.net.URL, java.util.Set, String...)
 */
public interface IRHtml extends HasResource {
	
	/**
	 * Returns the title of the HTML template.
	 * 
	 * @return the title of the HTML template
	 */
	String getTitle();
	
	/**
	 * Returns the HTML content.
	 * 
	 * <p>
	 * The content is read using UTF-8 charset. All parameter values will be substituted with their values.
	 * </p>
	 * 
	 * <p>
	 * If reading the resource throws an exception, an HTML error message will be returned indicating that content could not be loaded.
	 * </p>
	 * 
	 * @return the HTML content
	 */
	String get();
	
}
