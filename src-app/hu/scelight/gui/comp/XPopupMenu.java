/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.gui.comp;

import hu.scelightapi.gui.comp.IPopupMenu;
import hu.scelightapibase.gui.comp.ILabel;
import hu.sllauncher.gui.comp.BorderPanel;
import hu.sllauncher.gui.comp.XLabel;

import javax.swing.Icon;
import javax.swing.JPopupMenu;

/**
 * An extended {@link JPopupMenu}.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class XPopupMenu extends JPopupMenu implements IPopupMenu {
	
	/**
	 * Creates a new {@link XPopupMenu}.
	 */
	public XPopupMenu() {
		this( (XLabel) null );
	}
	
	/**
	 * Creates a new {@link XPopupMenu}.
	 * 
	 * @param text text of the popup title
	 */
	public XPopupMenu( final String text ) {
		this( text, null );
	}
	
	/**
	 * Creates a new {@link XPopupMenu}.
	 * 
	 * @param text text of the popup title
	 * @param icon icon of the popup title
	 */
	public XPopupMenu( final String text, final Icon icon ) {
		this( createTitleLabel( text, icon ) );
	}
	
	/**
	 * Creates a new {@link XPopupMenu}.
	 * 
	 * @param titleLabel title label of the popup
	 */
	public XPopupMenu( final ILabel titleLabel ) {
		if ( titleLabel != null ) {
			// I wrap title label in a border panel because if a menu item has an HTML text,
			// title gets shifted (screwed up).
			final BorderPanel bp;
			add( bp = new BorderPanel( titleLabel.asLabel() ) );
			bp.setOpaque( false );
			addSeparator();
		}
	}
	
	@Override
	public XPopupMenu asPopupMenu() {
		return this;
	}
	
	/**
	 * Creates a title label from the specified text and icon.
	 * 
	 * @param text text of the title
	 * @param icon icon of the title
	 * @return a title label from the specified text and icon
	 */
	private static XLabel createTitleLabel( final String text, final Icon icon ) {
		return new XLabel( text, icon ).boldFont().leftBorder( 15 );
	}
	
}
