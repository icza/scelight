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

import hu.scelightapibase.gui.comp.ILink;
import hu.scelightapibase.util.iface.Consumer;
import hu.sllauncher.gui.icon.LRIcon;
import hu.sllauncher.service.env.LEnv;
import hu.sllauncher.util.LUtils;

import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

import javax.swing.ToolTipManager;

/**
 * A clickable, link-styled {@link XLabel}.
 * 
 * <p>
 * The link text is interpreted as a pre-formatted text and is properly encoded if contains HTML unsafe characters.<br>
 * A link may have a target URL or a {@link Consumer} defining an action to be called when clicked.
 * </p>
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class Link extends XLabel implements ILink {
	
	/** {@link URL} to be opened when clicked. */
	private URL                    url;
	
	/** Consumer to be called when clicked. */
	private Consumer< MouseEvent > consumer;
	
	/**
	 * Creates a new {@link Link} with no URL and no target action.
	 */
	public Link() {
		this( null, null, null );
	}
	
	/**
	 * Creates a new {@link Link} with no URL or target action.
	 * <p>
	 * Basically this is a link-styled {@link XLabel}.
	 * </p>
	 * 
	 * @param text text of the link
	 */
	public Link( final String text ) {
		this( text, null, null );
	}
	
	/**
	 * Creates a new {@link Link} which opens the specified URL in the user's default browser when clicked.
	 * 
	 * @param text text of the link
	 * @param url {@link URL} to be opened when clicked
	 */
	public Link( final String text, final URL url ) {
		this( text, url, null );
	}
	
	/**
	 * Creates a new {@link Link} which forwards mouse click events to the specified {@link Consumer}.
	 * 
	 * @param text text of the link
	 * @param consumer consumer to be called when clicked
	 */
	public Link( final String text, final Consumer< MouseEvent > consumer ) {
		this( text, null, consumer );
	}
	
	/**
	 * Creates a new {@link Link} which forwards mouse click events to the specified {@link Consumer} and opens the specified URL in the user's default browser
	 * when clicked.
	 * 
	 * @param text text of the link
	 * @param url {@link URL} to be opened when clicked
	 * @param consumer consumer to be called when clicked
	 */
	public Link( final String text, final URL url, final Consumer< MouseEvent > consumer ) {
		setText( text );
		setUrl( url );
		
		this.consumer = consumer;
		
		setCursor( Cursor.getPredefinedCursor( Cursor.HAND_CURSOR ) );
		
		addMouseListener( new MouseAdapter() {
			@Override
			public void mousePressed( final MouseEvent event ) {
				if ( !isEnabled() )
					return;
				
				// Note: Link.this.consumer and Link.this.url is used
				// because these can be changed later (so we definitely don't want to refer to the local variable here!)
				if ( Link.this.consumer != null )
					Link.this.consumer.consume( event );
				if ( Link.this.url != null )
					LEnv.UTILS_IMPL.get().showURLInBrowser( Link.this.url );
			}
		} );
		
		// Register this at the Tool tip manager.
		// Simply calling setToolTipText( "" ) does not work because my getToolTipText() always returns a non-null text,
		// and setToolTipText( "" ) only registers at the tool tip manager if previous tool tip is not null!
		ToolTipManager.sharedInstance().registerComponent( this );
	}
	
	@Override
	public void setText( final String text ) {
		super.setText( LUtils.htmlLinkText( text ) );
	}
	
	@Override
	public URL getUrl() {
		return url;
	}
	
	@Override
	public void setUrl( final URL url ) {
		this.url = url;
		final LRIcon ricon = LUtils.urlIcon( url );
		setIcon( ricon == null ? null : ricon.get() );
	}
	
	@Override
	public String getToolTipText() {
		// If a tool tip is set explicitly, return that, else the URL
		final String toolTipText = super.getToolTipText();
		return toolTipText == null ? LUtils.urlToolTip( url ) : toolTipText;
	}
	
	@Override
	public Consumer< MouseEvent > getConsumer() {
		return consumer;
	}
	
	@Override
	public void setConsumer( final Consumer< MouseEvent > consumer ) {
		this.consumer = consumer;
	}
	
}
