/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllauncher.gui.comp;

import hu.scelightapibase.gui.comp.IBrowser;
import hu.sllauncher.service.env.LEnv;
import hu.sllauncher.util.LUtils;

import javax.swing.JEditorPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

/**
 * A browser component based on {@link JEditorPane}.
 * 
 * <p>
 * By default editing is disabled, and handles hyperlinks (shows proper tool tip text and opens them in the default external browser).
 * </p>
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class Browser extends JEditorPane implements IBrowser {
	
	/**
	 * Creates a new {@link Browser}.
	 */
	public Browser() {
		setEditable( false );
		setContentType( "text/html" );
		
		addHyperlinkListener( new HyperlinkListener() {
			@Override
			public void hyperlinkUpdate( final HyperlinkEvent event ) {
				if ( event.getEventType() == HyperlinkEvent.EventType.ACTIVATED ) {
					if ( event.getURL() != null )
						LEnv.UTILS_IMPL.get().showURLInBrowser( event.getURL() );
				} else if ( event.getEventType() == HyperlinkEvent.EventType.ENTERED ) {
					if ( event.getURL() != null )
						setToolTipText( LUtils.urlToolTip( event.getURL() ) );
				} else if ( event.getEventType() == HyperlinkEvent.EventType.EXITED )
					setToolTipText( null );
			}
		} );
	}
	
	@Override
	public JEditorPane asEditorPanel() {
		return this;
	}
	
	@Override
	public void setMessage( final String msg ) {
		setMessage( msg, false );
	}
	
	@Override
	public void setMessage( final String msg, final boolean error ) {
		setContentType( "text/html" );
		setText( "<html><body style='font-family:arial;font-style:italic;background:#ffffff;" + ( error ? "color:red;" : "" ) + "'><p>"
		        + LUtils.safeForHtml( msg ) + "</p></body></html>" );
	}
	
}
