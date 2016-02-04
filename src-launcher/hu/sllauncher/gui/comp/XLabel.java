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

import hu.scelightapibase.gui.comp.ILabel;
import hu.sllauncher.util.gui.LGuiUtils;

import java.awt.Color;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JToolTip;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;

/**
 * An extended {@link JLabel}.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class XLabel extends JLabel implements ILabel {
	
	/**
	 * Creates a new {@link XLabel}.
	 */
	public XLabel() {
		this( null, null );
	}
	
	/**
	 * Creates a new {@link XLabel}.
	 * 
	 * @param text text of the label
	 */
	public XLabel( final String text ) {
		this( text, null );
	}
	
	/**
	 * Creates a new {@link XLabel}.
	 * 
	 * @param text text of the label
	 * @param halignment horizontal alignment; one of <code>SwingConstants: LEFT, CENTER, RIGHT, LEADING or TRAILING</code>
	 */
	public XLabel( final String text, final int halignment ) {
		this( text, null, halignment );
	}
	
	/**
	 * Creates a new {@link XLabel}.
	 * 
	 * @param icon icon of the label
	 */
	public XLabel( final Icon icon ) {
		this( null, icon, CENTER );
	}
	
	/**
	 * Creates a new {@link XLabel}.
	 * 
	 * @param text text of the label
	 * @param icon icon of the label
	 */
	public XLabel( final String text, final Icon icon ) {
		this( text, icon, LEADING );
	}
	
	/**
	 * Creates a new {@link XLabel}.
	 * 
	 * @param text text of the label
	 * @param icon icon of the label
	 * @param halignment horizontal alignment; one of <code>SwingConstants: LEFT, CENTER, RIGHT, LEADING or TRAILING</code>
	 */
	public XLabel( final String text, final Icon icon, final int halignment ) {
		super( text, icon, halignment );
		
		ToolTipManager.sharedInstance().registerComponent( this );
	}
	
	@Override
	public XLabel asLabel() {
		return this;
	}
	
	@Override
	public XLabel allBorder( final int width ) {
		setBorder( BorderFactory.createEmptyBorder( width, width, width, width ) );
		return this;
	}
	
	@Override
	public XLabel leftBorder( final int left ) {
		setBorder( BorderFactory.createEmptyBorder( 0, left, 0, 0 ) );
		return this;
	}
	
	@Override
	public XLabel rightBorder( final int right ) {
		setBorder( BorderFactory.createEmptyBorder( 0, 0, 0, right ) );
		return this;
	}
	
	@Override
	public XLabel topBorder( final int top ) {
		setBorder( BorderFactory.createEmptyBorder( top, 0, 0, 0 ) );
		return this;
	}
	
	@Override
	public XLabel bottomBorder( final int bottom ) {
		setBorder( BorderFactory.createEmptyBorder( 0, 0, bottom, 0 ) );
		return this;
	}
	
	@Override
	public XLabel allBorder( final int top, final int left, final int bottom, final int right ) {
		setBorder( BorderFactory.createEmptyBorder( top, left, bottom, right ) );
		return this;
	}
	
	@Override
	public XLabel verticalBorder( final int topBottom ) {
		setBorder( BorderFactory.createEmptyBorder( topBottom, 0, topBottom, 0 ) );
		return this;
	}
	
	@Override
	public XLabel horizontalBorder( final int leftRight ) {
		setBorder( BorderFactory.createEmptyBorder( 0, leftRight, 0, leftRight ) );
		return this;
	}
	
	@Override
	public XLabel color( final Color color ) {
		setForeground( color );
		return this;
	}
	
	@Override
	public XLabel boldFont() {
		return LGuiUtils.boldFont( this );
	}
	
	@Override
	public XLabel italicFont() {
		return LGuiUtils.italicFont( this );
	}
	
	/**
	 * Overridden to return label text as tool tip if it's truncated (due to not fitting into its size).
	 */
	@Override
	public String getToolTipText( final MouseEvent event ) {
		// First check if super implementation returns a tool tip (tool tip set on the label itself)
		final String tip = super.getToolTipText( event );
		if ( tip != null )
			return tip;
		
		if ( getVisibleRect().width >= getPreferredSize().width )
			return null;
		
		return getText();
	}
	
	/**
	 * Positions tool tips exactly over the label text, also avoids showing empty tool tips (by returning <code>null</code>).
	 */
	@Override
	public Point getToolTipLocation( MouseEvent event ) {
		// If tool tip is provided by the renderer or the table itself, use default location:
		if ( super.getToolTipText( event ) != null )
			return super.getToolTipLocation( event );
		
		// If no tool tip, return null to prevent displaying an empty tool tip
		if ( getToolTipText( event ) == null )
			return null;
		
		// Now we need the position of the rendered text. This depends on many things (e.g. space reserved for the label,
		// horizontal, vertical alignments, has icon, icon size, icon text gap etc.)
		// The calculation is not easy, but we have built-in help:
		final Point p = new Point( -4, -4 );
		final Rectangle textR = new Rectangle();
		SwingUtilities.layoutCompoundLabel( this, getFontMetrics( getFont() ), getText(), getIcon(), getVerticalAlignment(), getHorizontalAlignment(),
		        getVerticalTextPosition(), getHorizontalTextPosition(), getVisibleRect(), new Rectangle(), textR, getIconTextGap() );
		p.x += textR.x;
		p.y += textR.y;
		// We have to account for border ourselves
		if ( getBorder() != null ) {
			final Insets insets = getBorder().getBorderInsets( this );
			p.x += insets.left;
			p.y += ( insets.top - insets.bottom ) / 2; // Might be negative? (that's why I don't use shift)
		}
		
		return p;
	}
	
	/**
	 * Creates a tool tip with the label's font.
	 */
	@Override
	public JToolTip createToolTip() {
		final JToolTip tt = super.createToolTip();
		
		tt.setFont( getFont() );
		
		return tt;
	}
	
}
