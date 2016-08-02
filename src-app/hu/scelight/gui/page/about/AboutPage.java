/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.gui.page.about;

import hu.scelight.Consts;
import hu.scelight.gui.icon.Icons;
import hu.scelight.service.env.Env;
import hu.scelight.util.RHtml;
import hu.scelightapibase.gui.comp.multipage.IPageDisposedListener;
import hu.sllauncher.gui.comp.BorderPanel;
import hu.sllauncher.gui.comp.Browser;
import hu.sllauncher.gui.comp.XScrollPane;
import hu.sllauncher.gui.comp.logo.Logo;
import hu.sllauncher.gui.comp.multipage.BasePage;

/**
 * About page displaying general / summary info about the application.
 * 
 * @author Andras Belicza
 */
public class AboutPage extends BasePage< AboutPage.AboutPageComp > {
	
	/**
	 * Logs page component.
	 * 
	 * @author Andras Belicza
	 */
	@SuppressWarnings( "serial" )
	public static class AboutPageComp extends BorderPanel implements IPageDisposedListener {
		
		/** Application logo. */
		private final Logo logo = new Logo();
		
		/**
		 * Creates a new {@link AboutPageComp}.
		 */
		public AboutPageComp() {
			buildGui();
		}
		
		/**
		 * Builds the GUI of the logs page component.
		 */
		private void buildGui() {
			addNorth( logo );
			
			final Browser browser = new Browser();
			browser.setText( new RHtml( "About page", "html/about.html", "appVersion", Consts.APP_VERSION.toString(), "appBuildNumber", Env.APP_BUILD_INFO
			        .getBuildNumber().toString(), "copyrightYears", Env.COPYRIGHT_YEARS, "authorName", Consts.APP_AUTHOR.getPersonName().toString(),
			        "authorEmail", Consts.APP_AUTHOR.getContact().getEmail(), "urlHomePage", Consts.URL_HOME_PAGE.toString(),
			        "urlProjectPage", Consts.URL_PROJECT_PAGE.toString()).get() );
			addCenter( new XScrollPane( browser ) );
		}
		
		@Override
		public void pageDisposed() {
			logo.stop();
		}
		
	}
	
	/**
	 * Creates a new {@link AboutPage}.
	 */
	public AboutPage() {
		super( "About " + Consts.APP_NAME, Icons.MY_APP_ICON );
	}
	
	@Override
	public AboutPageComp createPageComp() {
		return new AboutPageComp();
	}
	
}
