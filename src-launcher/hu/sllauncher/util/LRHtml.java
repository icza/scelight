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

import hu.scelightapibase.util.IRHtml;
import hu.sllauncher.LConsts;
import hu.sllauncher.r.LR;
import hu.sllauncher.service.env.LEnv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
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
 * <br>
 * <p>
 * It is also possible to include the content of another HTML template if that other template is <b>already cached</b>.<br>
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
 * this possibility to the template that includes it. <br>
 * <br>
 * <b>Note:</b> This is a simplification and would not work properly in cases where one fully-qualified package is the tail of another one (e.g.
 * <code>/a/b/c</code> and <code>/q/a/b/c</code>) because an HTML resource is accepted if its URL ends with the fully qualified HTML resource name.
 * </p>
 * 
 * @author Andras Belicza
 */
public class LRHtml extends ResourceHolder implements IRHtml {
	
	/** Highlighted CSS style class definition, used for keywords or important words. */
	public static final String                 STYLE_HIGHLIGHTED      = "font-style:italic;color:#b02000";
	
	/** Stressed CSS style class definition, used for a term or name from the application. */
	public static final String                 STYLE_STRESSED         = "font-weight:bold;color:#3030ff";
	
	/** Key CSS style class definition, used for marking a key or a key combo. */
	public static final String                 STYLE_KEY              = "font-family:monospace;background:#777777;color:#ffffff";
	
	/** Not important CSS style class definition, used for marking words unimportant and less visible. */
	public static final String                 STYLE_NOT_IMPORTANT    = "color:#a0a0a0";
	
	/** Not important CSS style class definition, used for marking words unimportant and less visible. */
	public static final String                 STYLE_ADVANCED_DETAILS = "font-size: 95%; padding-top: 7px; color: #333333";
	
	
	/**
	 * General CSS (styling, formatting).<br>
	 * 
	 * Defined CSS classes:
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
	 */
	public static final String                 GENERAL_CSS            = "<style>.h{"
	                                                                          + STYLE_HIGHLIGHTED
	                                                                          + "}.s{"
	                                                                          + STYLE_STRESSED
	                                                                          + "}.k{"
	                                                                          + STYLE_KEY
	                                                                          + "}.n{"
	                                                                          + STYLE_NOT_IMPORTANT
	                                                                          + "}.a{"
	                                                                          + STYLE_ADVANCED_DETAILS
	                                                                          + "}.D, .N, .T, .E, .W{padding:3px 0px 4px 5px;border-left-style:solid;border-left-width:6px}"
	                                                                          + ".D{border-left-color:#309930;background:#bbeebb}"
	                                                                          + ".N{border-left-color:#505050;background:#c8c8c8}"
	                                                                          + ".T{border-left-color:#bbbb00;background:#eeee80}"
	                                                                          + ".E{border-left-color:#8080ff;background:#ddddff}"
	                                                                          + ".W{border-left-color:#ff0000;background:#ffb0b0}"
	                                                                          + "ol, ul{margin-left:0px;padding-left:20px;margin-top:2px}</style>";
	
	
	/** General, common params which can be used without specifying. */
	protected final static String[]            GENERAL_PARAMS         = { "${generalCss}", GENERAL_CSS, "${appName}", LConsts.APP_NAME, "${appOpName}",
	        LConsts.APP_OPERATOR_NAME                                };
	
	
	/** A map of HTML resources mapped from their URL string, required to process inclusion of other templates. */
	private static final Map< String, LRHtml > URL_RHTML_MAP          = new HashMap<>();
	
	
	/** Title of the HTML template. */
	public final String                        title;
	
	/** Param name - param value pairs to be substituted in the HTML template. */
	protected final String[]                   params;
	
	
	/**
	 * Creates a new {@link LRHtml}.
	 * <p>
	 * The resource locator is acquired by calling {@link LR#get(String)}.
	 * </p>
	 * 
	 * @param title title of the HTML template
	 * @param resource HTML template resource
	 * @param params param name - param value pairs to be substituted in the HTML template
	 */
	public LRHtml( final String title, final String resource, final String... params ) {
		this( title, resource, null, params );
	}
	
	/**
	 * Creates a new {@link LRHtml}.
	 * <p>
	 * The resource locator is acquired by calling {@link LR#get(String)}.
	 * </p>
	 * 
	 * @param title title of the HTML template
	 * @param resource HTML template resource
	 * @param htmlParams collection of HTML param names, values of these parameters are treated as HTML and are not escaped
	 * @param params param name - param value pairs to be substituted in the HTML template
	 */
	public LRHtml( final String title, final String resource, final Set< String > htmlParams, final String... params ) {
		this( title, LR.get( resource ), htmlParams, params );
	}
	
	/**
	 * Creates a new {@link LRHtml}.
	 * 
	 * @param title title of the HTML template
	 * @param resource HTML template resource
	 * @param params param name - param value pairs to be substituted in the HTML template;<br>
	 *            param names must be in <i>raw</i> format, they will be prepended with <code>"${"</code> and postpended with <code>"}"</code>
	 */
	public LRHtml( final String title, final URL resource, final String... params ) {
		this( title, resource, null, params );
	}
	
	/**
	 * Creates a new {@link LRHtml}.
	 * 
	 * @param title title of the HTML template
	 * @param resource HTML template resource
	 * @param htmlParams collection of HTML param names, values of these parameters are treated as HTML and are not escaped
	 * @param params param name - param value pairs to be substituted in the HTML template;<br>
	 *            param names must be in <i>raw</i> format, they will be prepended with <code>"${"</code> and postpended with <code>"}"</code>
	 */
	public LRHtml( final String title, final URL resource, final Set< String > htmlParams, final String... params ) {
		super( resource );
		
		if ( ( params.length & 0x01 ) != 0 )
			throw new RuntimeException( "Params length must be even!" );
		
		this.title = title;
		
		this.params = params;
		for ( int i = 0; i < params.length; i += 2 ) {
			final String paramName = params[ i ];
			params[ i ] = "${" + paramName + '}';
			if ( htmlParams == null || !htmlParams.contains( paramName ) )
				params[ i + 1 ] = LUtils.safeForHtml( params[ i + 1 ] );
		}
		
		synchronized ( URL_RHTML_MAP ) {
			URL_RHTML_MAP.put( resource.toString(), this );
		}
	}
	
	@Override
	public String getTitle() {
		return title;
	}
	
	@Override
	public String get() {
		try ( final BufferedReader in = new BufferedReader( new InputStreamReader( resource.openStream(), LEnv.UTF8 ) ) ) {
			final StringBuilder sb = new StringBuilder( 256 );
			
			String line;
			while ( ( line = in.readLine() ) != null ) {
				// Replace parameters (it's better to do it with small strings than on the whole,
				// first we also check if parameters are present and only proceed to replace if so).
				if ( line.indexOf( '$' ) >= 0 ) { // A quick check if there is chance the line contains parameters:
					// First process inclusions.
					// So it wis possible for the referenced template to have parameters and not specify their values leaving
					// this possibility for the template that includes it.
					line = getInclusionContent( line );
					
					line = substParams( line, GENERAL_PARAMS );
					line = substParams( line, params );
				}
				
				// Also append a space because readLine() excludes line separator, but in HTML it is interpreted as a space.
				sb.append( line ).append( ' ' );
			}
			
			return sb.toString();
		} catch ( final IOException ie ) {
			LEnv.LOGGER.error( "Failed to load HTML template: " + title, ie );
			return "<font color='red'><i>Failed to load HTML template: <b>" + LUtils.safeForHtml( title ) + "</b></i></font>";
		}
	}
	
	/**
	 * Checks if the specified line specifies an inclusion of another template and if so, the referenced content will be returned.
	 * 
	 * <p>
	 * The inclusion has the following syntax:
	 * 
	 * <pre>
	 * ${#&lt;fully-qualified-html-resource-name&gt;}
	 * </pre>
	 * 
	 * The inclusion must be in a new line and must not start with white spaces. If there is a cached HTML template associated with the URL constructed by
	 * {@link Class#getResource(String)}, its content will be included.
	 * </p>
	 * 
	 * @param line optional specification of an inclusion
	 * @return the line itself if it is not an inclusion, the referenced content otherwise
	 */
	private static String getInclusionContent( final String line ) {
		if ( !line.startsWith( "${#" ) || line.charAt( line.length() - 1 ) != '}' )
			return line;
		
		final String urlString = line.substring( 3, line.length() - 1 );
		synchronized ( URL_RHTML_MAP ) {
			for ( final Entry< String, LRHtml > entry : URL_RHTML_MAP.entrySet() ) {
				if ( entry.getKey().endsWith( urlString ) )
					return entry.getValue().get();
			}
		}
		
		LEnv.LOGGER.error( "Referenced HTML resource not found: " + line );
		return line;
	}
	
	/**
	 * Substitutes the specified params in the specified text.
	 * 
	 * @param text text to Substitute the params in
	 * @param params params to be applied (replaced)
	 * @return the text where the specified params have been substituted
	 */
	private static String substParams( String text, final String[] params ) {
		for ( int i = 0; i < params.length; i += 2 )
			if ( text.indexOf( params[ i ] ) >= 0 )
				text = text.replace( params[ i ], params[ i + 1 ] );
		
		return text;
	}
	
}
