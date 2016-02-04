/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllauncher.gui.comp.table.renderer;

import hu.scelightapibase.gui.comp.table.renderer.IBarCodeView;
import hu.sllauncher.gui.comp.table.XTable;
import hu.sllauncher.util.LUtils;

import java.awt.Color;

/**
 * An object that will be rendered as a bar code in the {@link XTable}.
 * 
 * @author Andras Belicza
 */
public class BarCodeView implements IBarCodeView {
	
	/** Values of the parts of the bar code. */
	public final int[]   values;
	
	/** Colors of the parts of the bar code. */
	public final Color[] colors;
	
	/** Colors of the parts of the bar code when the cell is selected. */
	public final Color[] selColors;
	
	/** String value to be displayed on the bar code. */
	public final String  stringValue;
	
	
	/**
	 * Creates a new {@link BarCodeView}.
	 * 
	 * @param values values of the parts of the bar code
	 * @param colors colors of the parts of the bar code
	 * @param selColors colors of the parts of the bar code when the cell is selected
	 * @throws IllegalArgumentException if the number of values, colors and selected colors are not the same
	 */
	public BarCodeView( final int[] values, final Color[] colors, final Color[] selColors ) {
		this( values, colors, selColors, LUtils.concatenate( values ) );
	}
	
	/**
	 * Creates a new {@link BarCodeView}.
	 * 
	 * @param values values of the parts of the bar code
	 * @param colors colors of the parts of the bar code
	 * @param selColors colors of the parts of the bar code when the cell is selected
	 * @param stringValue string value to be displayed
	 * @throws IllegalArgumentException if the number of values, colors and selected colors are not the same
	 */
	public BarCodeView( final int[] values, final Color[] colors, final Color[] selColors, final String stringValue ) throws IllegalArgumentException {
		if ( values.length != colors.length || values.length != selColors.length )
			throw new IllegalArgumentException( "The number of values, colors and selected colors must be the same!" );
		
		this.values = values;
		this.colors = colors;
		this.selColors = selColors;
		this.stringValue = stringValue;
	}
	
	@Override
	public int[] getValues() {
		return values;
	}
	
	@Override
	public Color[] getColors() {
		return colors;
	}
	
	@Override
	public Color[] getSelectedColors() {
		return selColors;
	}
	
	@Override
	public String toString() {
		return stringValue;
	}
	
}
