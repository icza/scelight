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

import hu.scelightapibase.gui.comp.table.renderer.IProgressBarView;
import hu.sllauncher.gui.comp.table.XTable;
import hu.sllauncher.service.env.LEnv;

import java.awt.Color;

/**
 * An object that will be rendered as a progress bar in the {@link XTable}.
 * 
 * @author Andras Belicza
 */
public class ProgressBarView extends BarCodeView implements IProgressBarView {
	
	/** Colors for unselected progress bar view. */
	private static final Color[] COLORS        = { new Color( 0xffc560 ), new Color( 0xf8f8f8 ) };
	
	/** Darker colors for selected progress bar view. */
	private static final Color[] DARKER_COLORS = { new Color( 0xa88340 ), new Color( 0x606060 ) };
	
	
	/**
	 * Creates a new {@link ProgressBarView} where the string value will be the properly formatted value.
	 * 
	 * @param value value for the progress bar
	 * @param maximum maximum value for the progress bar
	 */
	public ProgressBarView( final int value, final int maximum ) {
		this( value, maximum, LEnv.LANG.formatNumber( value ) );
	}
	
	/**
	 * Creates a new {@link ProgressBarView}.
	 * 
	 * @param value value for the progress bar
	 * @param maximum maximum value for the progress bar
	 * @param stringValue string value to be displayed
	 */
	public ProgressBarView( final int value, final int maximum, final String stringValue ) {
		super( new int[] { value, maximum - value }, COLORS, DARKER_COLORS, stringValue );
	}
	
	@Override
	public int getValue() {
		return values[ 0 ];
	}
	
	@Override
	public int getMaximum() {
		return values[ 0 ] + values[ 1 ];
	}
	
	@Override
	public int compareTo( final IProgressBarView pbv ) {
		return getValue() - pbv.getValue();
	}
	
}
