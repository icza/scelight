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
import hu.sllauncher.action.UrlAction;
import hu.sllauncher.action.XAction;
import hu.sllauncher.gui.comp.BorderPanel;
import hu.sllauncher.gui.comp.Browser;
import hu.sllauncher.gui.comp.XButton;
import hu.sllauncher.gui.comp.XScrollPane;
import hu.sllauncher.gui.comp.XToolBar;
import hu.sllauncher.gui.comp.multipage.BasePage;
import hu.sllauncher.gui.icon.LIcons;
import hu.sllauncher.service.env.LEnv;
import hu.sllauncher.util.ControlledThread;
import hu.sllauncher.util.UrlBuilder;
import hu.sllauncher.util.gui.LGuiUtils;

import java.awt.event.ActionEvent;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.zip.GZIPInputStream;

import javax.swing.JComponent;

/**
 * News page displaying latest application news.
 * 
 * @author Andras Belicza
 */
public class NewsPage extends BasePage< JComponent > {
	
	/**
	 * Creates a new {@link NewsPage}.
	 */
	public NewsPage() {
		super( "News", LIcons.F_NEWSPAPER );
	}
	
	@Override
	public JComponent createPageComp() {
		final BorderPanel p = new BorderPanel();
		
		final Browser newsBrowser = new Browser();
		
		@SuppressWarnings( "serial" )
		final XAction refreshAction = new XAction( LIcons.F_ARROW_CIRCLE_315, "Refresh", p ) {
			// We're assigning to the local variable so it is not yet available for referencing. Create a copy.
			private final XAction self = this;
			
			@Override
			public void actionPerformed( final ActionEvent event ) {
				self.setEnabled( false );
				newsBrowser.setMessage( "Refreshing..." );
				
				// Refresh in a new thread to not block the EDT!
				new ControlledThread( "News Refresher" ) {
					@Override
					public void customRun() {
						refresh();
						self.setEnabled( true );
					}
				}.start();
			}
			
			private void refresh() {
				// Refreshes the content of the displayed latest news.
				try {
					if ( LEnv.ECLIPSE_MODE ) {
						try ( final InputStream in = Files.newInputStream( Paths.get( "../war/news/news.html" ) ) ) {
							newsBrowser.read( in, null );
						}
					} else {
						try ( final InputStream in = new GZIPInputStream( new UrlBuilder( LEnv.URL_NEWS_GZ ).addTimestamp().toUrl().openStream() ) ) {
							newsBrowser.read( in, null );
						}
					}
				} catch ( final Exception ie ) {
					LEnv.LOGGER.error( "Failed to retrieve News.", ie );
					newsBrowser.setMessage( "Failed to retrieve News! Please try again later.", true );
				}
			}
		};
		
		// Tool bar
		final XToolBar toolBar = new XToolBar();
		final XButton refreshButton = new XButton( refreshAction );
		toolBar.add( refreshButton );
		LGuiUtils.autoCreateDisabledImage( refreshButton );
		toolBar.addSeparator();
		toolBar.add( new XButton( new UrlAction( "Visit home page", LConsts.URL_HOME_PAGE, p ) ) );
		toolBar.finalizeLayout();
		p.addNorth( toolBar );
		
		// News browser
		p.addCenter( new XScrollPane( newsBrowser ) );
		
		refreshAction.actionPerformed( null );
		
		return p;
	}
	
}
