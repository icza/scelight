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

import java.awt.Color;
import java.awt.Component;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * A bar code ({@link IBarCodeView}) renderer component.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
class BarCodeCellRenderer extends DefaultTableCellRenderer {
	
	/** Reusable, shared insets object (Swing is single-threaded). */
	private static final Insets INSETS = new Insets( 0, 0, 0, 0 );
	
	/** Bar code view to be rendered. */
	private IBarCodeView        barCodeView;
	
	/** Tells if the cell is selected. */
	private boolean             selected;
	
	
	/**
	 * Creates a new {@link BarCodeCellRenderer}.
	 */
	public BarCodeCellRenderer() {
		setHorizontalAlignment( CENTER );
		setOpaque( false );
	}
	
	@Override
	public Component getTableCellRendererComponent( final JTable table, Object value, final boolean isSelected, final boolean hasFocus, final int row,
	        final int column ) {
		
		super.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, column );
		
		this.barCodeView = value instanceof IBarCodeView ? (IBarCodeView) value : null;
		selected = isSelected;
		
		return this;
	}
	
	@Override
	protected void paintComponent( final Graphics g ) {
		if ( barCodeView == null ) {
			super.paintComponent( g );
			return;
		}
		
		// First draw the bar code background
		final int[] values = barCodeView.getValues();
		// Work with long: 100_000 games is real, 1000 pixel width is real, when multiplied: 100_000_000 which is order of magnitude of MAX_INT.
		long sum = 0;
		for ( final int value : values )
			sum += value;
		
		final Graphics2D g2 = (Graphics2D) g;
		
		getInsets( INSETS );
		final int width = getWidth() - INSETS.left - INSETS.right; // Useful width
		final int height = getHeight() - INSETS.top - INSETS.bottom; // Useful height
		final int cy = INSETS.top + height / 2;
		final int gradientY1 = selected ? cy - 3 * height / 4 : cy + 3 * height / 4;
		
		final Color[] colors = selected ? barCodeView.getSelectedColors() : barCodeView.getColors();
		
		// Arbitrary decision:
		// If sum is 0, I will paint the whole background with the first color.
		if ( sum == 0 ) {
			g2.setPaint( new GradientPaint( 0, gradientY1, colors[ 0 ].darker(), 0, cy, colors[ 0 ], false ) );
			g.fillRect( INSETS.left, INSETS.top, width, height );
		} else {
			int nextX = INSETS.left;
			long partialSum = 0;
			for ( int i = 0; i < values.length; i++ ) {
				final int value = values[ i ];
				if ( value == 0 )
					continue;
				
				final int x = nextX;
				partialSum += value;
				nextX = INSETS.left + (int) ( partialSum * width / sum );
				
				g2.setPaint( new GradientPaint( 0, gradientY1, colors[ i ].darker(), 0, cy, colors[ i ], false ) );
				g.fillRect( x, INSETS.top, nextX - x, height ); // nextX position is excluded
			}
		}
		
		// And now draw the text by calling the super implementation
		super.paintComponent( g );
	}
	
}
