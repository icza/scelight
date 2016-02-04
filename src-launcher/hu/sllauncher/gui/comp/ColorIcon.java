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

import hu.scelightapibase.gui.comp.IColorIcon;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;

/**
 * An {@link Icon} which is filled with a constant color.
 * 
 * @author Andras Belicza
 */
public class ColorIcon implements IColorIcon {
	
	/** Color of the icon. */
	public final Color color;
	
	/** Width of the icon. */
	public final int   width;
	
	/** Height of the icon. */
	public final int   height;
	
	/**
	 * Text value to be returned by the {@link #toString()} method. Useful if this icon is rendered in a table, and users might copy data from the table
	 * (because this text will be copied).
	 */
	private String     text;
	
	
	/**
	 * Creates a new {@link ColorIcon} with a default size of 16x16.
	 * 
	 * @param color color of the icon.
	 */
	public ColorIcon( final Color color ) {
		this( color, 16, 16 );
	}
	
	/**
	 * Creates a new {@link ColorIcon} with a default size of 16x16.
	 * 
	 * @param color color of the icon.
	 * @param text text to be returned by the {@link #toString()} method
	 */
	public ColorIcon( final Color color, final String text ) {
		this( color, 16, 16, text );
	}
	
	/**
	 * Creates a new {@link ColorIcon}.
	 * 
	 * @param color color of the icon.
	 * @param width width of the icon
	 * @param height height of the icon
	 */
	public ColorIcon( final Color color, final int width, final int height ) {
		this( color, width, height, "I" );
	}
	
	/**
	 * Creates a new {@link ColorIcon}.
	 * 
	 * @param color color of the icon.
	 * @param width width of the icon
	 * @param height height of the icon
	 * @param text text to be returned by the {@link #toString()} method
	 */
	public ColorIcon( final Color color, final int width, final int height, final String text ) {
		this.color = color;
		this.width = width;
		this.height = height;
		this.text = text;
	}
	
	@Override
	public void paintIcon( final Component c, final Graphics g, final int x, final int y ) {
		g.setColor( color );
		g.fillRect( x, y, width, height );
	}
	
	@Override
	public int getIconWidth() {
		return width;
	}
	
	@Override
	public int getIconHeight() {
		return height;
	}
	
	@Override
	public Color getColor() {
		return color;
	}
	
	@Override
	public String getText() {
		return text;
	}
	
	@Override
	public void setText( final String text ) {
		this.text = text;
	}
	
	@Override
	public String toString() {
		return text;
	}
	
}
