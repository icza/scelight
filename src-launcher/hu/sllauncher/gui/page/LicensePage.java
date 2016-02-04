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
import hu.sllauncher.service.env.LEnv;
import hu.sllauncher.util.LRHtml;

/**
 * License page.
 * 
 * @author Andras Belicza
 */
public class LicensePage extends BrowserPage {
	
	/**
	 * Creates a new {@link LicensePage}.
	 */
	public LicensePage() {
		super( "License", LIcons.F_SCRIPT_TEXT, new LRHtml( "LICENSE page", "html/LICENSE.html", "copyrightYears", LEnv.COPYRIGHT_YEARS, "urlHomePage",
		        LConsts.URL_HOME_PAGE.toString(), "authorName", LConsts.APP_AUTHOR.getPersonName().toString(), "authorEmail", LConsts.APP_AUTHOR.getContact()
		                .getEmail(), "urlReginfoPage", LConsts.URL_REGINFO_PAGE.toString() ) );
	}
	
}
