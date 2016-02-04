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
 * General Tips page.
 * 
 * @author Andras Belicza
 */
public class TipsPage extends BrowserPage {
	
	/**
	 * Creates a new {@link TipsPage}.
	 */
	public TipsPage() {
		super( "Tips", LIcons.F_LIGHT_BULB, new LRHtml( "Tips page", "html/tips.html", "urlDownloadsPage", LConsts.URL_DOWNLOADS_PAGE.toString(),
		        "urlReginfoPage", LConsts.URL_REGINFO_PAGE.toString() ) );
	}
	
}
