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

import hu.scelightapibase.gui.comp.IPopupIcon;
import hu.scelightapibase.util.IRHtml;
import hu.sllauncher.gui.icon.LRIcon;
import hu.sllauncher.service.env.LEnv;
import hu.sllauncher.service.settings.LSettings;
import hu.sllauncher.util.LRHtml;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPopupMenu;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

/**
 * A clickable icon which when clicked displays the content of a {@link LRHtml} in a popup.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class PopupIcon extends XLabel implements IPopupIcon {
	
	/** Header (containing style) of the HTML content templates. */
	private static final String HEADER = "<html><head>" + LRHtml.GENERAL_CSS + "</head>" + "<body style='width:350px;'><h2 style='margin-top:1px'>";
	
	/** Ricon of the component. */
	private final LRIcon        ricon;
	
	/** HTML template resource of the content. */
	private final IRHtml        contentRhtml;
	
	/** Reference to the last showed popup. */
	private JPopupMenu          popup;
	
	/**
	 * Creates a new {@link PopupIcon}.
	 * 
	 * @param ricon ricon of the component
	 * @param contentRhtml html template resource of the content
	 */
	public PopupIcon( final LRIcon ricon, final IRHtml contentRhtml ) {
		super( ricon.get() );
		
		this.ricon = ricon;
		this.contentRhtml = contentRhtml;
		
		setCursor( Cursor.getPredefinedCursor( Cursor.HAND_CURSOR ) );
		
		addMouseListener( new MouseAdapter() {
			@Override
			public void mousePressed( final MouseEvent event ) {
				showPopup( event );
			}
			
			@Override
			public void mouseEntered( final MouseEvent event ) {
				if ( LEnv.LAUNCHER_SETTINGS.get( LSettings.SHOW_HELPS_TIPS_MOUSE_OVER ) )
					showPopup( event );
			}
			
			@Override
			public void mouseExited( final MouseEvent event ) {
				if ( LEnv.LAUNCHER_SETTINGS.get( LSettings.HIDE_HELPS_TIPS_MOUSE_OUT ) ) {
					hidePopup();
				}
			}
		} );
		
		// When the icon becomes invisible due to being removed, hide the popup if visible
		// This needed because if the icon's parent is removed, no mouseExited event is generated.
		addAncestorListener( new AncestorListener() {
			@Override
			public void ancestorRemoved( final AncestorEvent event ) {
				hidePopup();
			}
			
			@Override
			public void ancestorMoved( final AncestorEvent event ) {
				// We don't want to do anything here.
			}
			
			@Override
			public void ancestorAdded( final AncestorEvent event ) {
				// We don't want to do anything here.
			}
		} );
	}
	
	@Override
	public void showPopup( final MouseEvent event ) {
		popup = new JPopupMenu();
		
		final XLabel l = new XLabel( HEADER + contentRhtml.getTitle() + "</h2>" + contentRhtml.get() + "</body></html>", ricon.size( 32 ), LEFT ).allBorder( 7,
		        5, 7, 10 );
		l.setVerticalTextPosition( TOP );
		l.setIconTextGap( 6 );
		popup.add( l );
		
		popup.addMouseListener( new MouseAdapter() {
			@Override
			public void mousePressed( final MouseEvent event ) {
				hidePopup();
			}
		} );
		
		// If we would not specify precise popup location, default location could be a location where the popup
		// overlaps the icon causing a mouse exited event which could immediately trigger a popup hide
		// which could immediately trigger a mouse entered event which could immediately trigger a popup show...
		// (endless loop!).
		
		// Note: Screen insets are only arbitrary taken into consideration
		// (because popups can be displayed over the task bar for example).
		
		final Component comp = (Component) event.getSource();
		final GraphicsConfiguration gc = comp.getGraphicsConfiguration();
		final Rectangle screenBounds = gc.getBounds();
		final Insets insets = Toolkit.getDefaultToolkit().getScreenInsets( gc );
		final Dimension prefSize = popup.getPreferredSize();
		final Point compOnScreen = comp.getLocationOnScreen();
		
		final int SPACE = 3; // space to leave between the popup and the component
		
		// Preferably show popup below the component, but if no space, show above.
		int y = comp.getHeight() + SPACE; // Default location below
		if ( compOnScreen.y + y + prefSize.height > screenBounds.getMaxY() - insets.bottom )
			y = -prefSize.height - SPACE; // No space below, position above
			
		// Preferably show popup on the right, but if no space, show on the left.
		int x = comp.getWidth() + SPACE;
		if ( compOnScreen.x + x + prefSize.width > screenBounds.getMaxX() - insets.right )
			x = -prefSize.width - SPACE; // No space on the right, position on the left
			
		popup.show( comp, x, y );
	}
	
	@Override
	public void hidePopup() {
		if ( popup == null )
			return;
		
		popup.setVisible( false );
		popup = null;
	}
	
	/**
	 * Only returns the tool tip text if the content is to be shown only for click and not for mouse over and a popup is not currently visible.
	 */
	@Override
	public String getToolTipText() {
		if ( LEnv.LAUNCHER_SETTINGS.get( LSettings.SHOW_HELPS_TIPS_MOUSE_OVER ) || popup != null && popup.isVisible() )
			return null;
		else
			return super.getToolTipText();
	}
	
}
