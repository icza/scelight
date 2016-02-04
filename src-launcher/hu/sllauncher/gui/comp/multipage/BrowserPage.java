/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllauncher.gui.comp.multipage;

import hu.scelightapibase.gui.icon.IRIcon;
import hu.scelightapibase.util.IRHtml;
import hu.sllauncher.gui.comp.Browser;

/**
 * A browser page which displays a static HTML resource.
 * 
 * @author Andras Belicza
 */
public class BrowserPage extends BasePage< Browser > {
	
	/** HTML template resource to display as content. */
	private final IRHtml rhtml;
	
	/**
	 * Creates a new {@link BrowserPage}.
	 * 
	 * @param displayName display name of the page
	 * @param ricon icon resource of the page
	 * @param rhtml HTML template resource to display as content
	 */
	public BrowserPage( final String displayName, final IRIcon ricon, final IRHtml rhtml ) {
		super( displayName, ricon );
		this.rhtml = rhtml;
	}
	
	@Override
	public Browser createPageComp() {
		final Browser browser = new Browser();
		
		browser.setText( rhtml.get() );
		// Sometimes content is scrolled to the end. Make sure it's not.
		browser.setCaretPosition( 0 );
		
		return browser;
	}
	
}
