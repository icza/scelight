/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllauncher.gui.page;

import hu.sllauncher.LConsts;
import hu.sllauncher.gui.comp.multipage.BrowserPage;
import hu.sllauncher.gui.icon.LIcons;
import hu.sllauncher.util.LRHtml;

/**
 * Welcome page.
 * 
 * @author Andras Belicza
 */
public class WelcomePage extends BrowserPage {
	
	/**
	 * Creates a new {@link WelcomePage}.
	 */
	public WelcomePage() {
		super( "Welcome", LIcons.F_HOME, new LRHtml( "Welcome page", "html/welcome.html", "urlHomePage", LConsts.URL_HOME_PAGE.toString(), "urlDownloadsPage",
		        LConsts.URL_DOWNLOADS_PAGE.toString() ) );
	}
	
}
