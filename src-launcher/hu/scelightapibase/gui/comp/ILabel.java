/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapibase.gui.comp;

import java.awt.Color;

import javax.swing.JLabel;

/**
 * An extended {@link JLabel}.
 * 
 * <p>
 * Provides numerous builder-style utility methods and overrides tool tip creation and placement to return the label's text as tool tip if it's truncated (due
 * to not fitting into its size). Positions tool tips exactly over the label text, uses the label's font for tool tips and also avoids showing empty tool tips.
 * </p>
 * 
 * @author Andras Belicza
 * 
 * @see hu.scelightapi.service.IGuiFactory#newLabel()
 * @see hu.scelightapi.service.IGuiFactory#newLabel(String)
 * @see hu.scelightapi.service.IGuiFactory#newLabel(String, int)
 * @see hu.scelightapi.service.IGuiFactory#newLabel(javax.swing.Icon)
 * @see hu.scelightapi.service.IGuiFactory#newLabel(String, javax.swing.Icon)
 * @see hu.scelightapi.service.IGuiFactory#newLabel(String, javax.swing.Icon, int)
 */
public interface ILabel {
	
	/**
	 * Casts this instance to {@link JLabel}.
	 * 
	 * @return <code>this</code> as a {@link JLabel}
	 */
	JLabel asLabel();
	
	/**
	 * Sets an empty border with the specified width of all sides and returns the label.
	 * 
	 * @param width width of all sides
	 * 
	 * @return <code>this</code> for chaining
	 */
	ILabel allBorder( int width );
	
	/**
	 * Sets an empty border with the specified width of the left and returns the label.
	 * 
	 * @param left width of the left
	 * 
	 * @return <code>this</code> for chaining
	 */
	ILabel leftBorder( int left );
	
	/**
	 * Sets an empty border with the specified width of the right and returns the label.
	 * 
	 * @param right width of the right
	 * 
	 * @return <code>this</code> for chaining
	 */
	ILabel rightBorder( int right );
	
	/**
	 * Sets an empty border with the specified width of the top and returns the label.
	 * 
	 * @param top width of the top
	 * 
	 * @return <code>this</code> for chaining
	 */
	ILabel topBorder( int top );
	
	/**
	 * Sets an empty border with the specified width of the bottom and returns the label.
	 * 
	 * @param bottom width of the bottom
	 * 
	 * @return <code>this</code> for chaining
	 */
	ILabel bottomBorder( final int bottom );
	
	/**
	 * Sets an empty border with the specified widths on all sides and returns the label.
	 * 
	 * @param top width of the top
	 * @param left width of the left
	 * @param bottom width of the bottom
	 * @param right width of the right
	 * 
	 * @return <code>this</code> for chaining
	 */
	ILabel allBorder( int top, int left, int bottom, int right );
	
	/**
	 * Sets an empty border with the specified width of the bottom and top and returns the label.
	 * 
	 * @param topBottom width of the top and bottom
	 * 
	 * @return <code>this</code> for chaining
	 */
	ILabel verticalBorder( int topBottom );
	
	/**
	 * Sets an empty border with the specified width of the left and right and returns the label.
	 * 
	 * @param leftRight width of the left and right
	 * 
	 * @return <code>this</code> for chaining
	 */
	ILabel horizontalBorder( int leftRight );
	
	/**
	 * Sets the specified color as the foreground color and returns the label.
	 * 
	 * @param color color to be set as the foreground color
	 * 
	 * @return <code>this</code> for chaining
	 */
	ILabel color( Color color );
	
	/**
	 * Changes the label's font to bold and returns the label.
	 * 
	 * @return <code>this</code> for chaining
	 */
	ILabel boldFont();
	
	/**
	 * Changes the label's font to italic and returns the label.
	 * 
	 * @return <code>this</code> for chaining
	 */
	ILabel italicFont();
	
}
