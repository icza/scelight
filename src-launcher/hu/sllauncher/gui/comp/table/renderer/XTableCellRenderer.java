/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllauncher.gui.comp.table.renderer;

import hu.scelightapibase.gui.comp.table.renderer.IBarCodeView;
import hu.scelightapibase.gui.comp.table.renderer.IProgressBarView;
import hu.sllauncher.util.gui.ToLabelRenderer;

import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * An improved table cell renderer with the unified {@link ToLabelRenderer} logic incorporated.
 * 
 * <p>
 * Additionally the following types are handled:
 * </p>
 * <ul>
 * <li>If a cell value is an {@link IBarCodeView}, the cell will be rendered as a bar code.</li>
 * <li>If a cell value is a {@link JComponent}, it will be returned as the renderer component.</li>
 * </ul>
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class XTableCellRenderer extends DefaultTableCellRenderer {
	
	/** A bar code cell renderer to render {@link IBarCodeView} values (including {@link IProgressBarView}s). */
	private static final BarCodeCellRenderer BAR_CODE_CELL_RENDERER = new BarCodeCellRenderer();
	
	@Override
	public Component getTableCellRendererComponent( final JTable table, Object value, final boolean isSelected, final boolean hasFocus, final int row,
	        final int column ) {
		
		if ( value instanceof IBarCodeView )
			return BAR_CODE_CELL_RENDERER.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, column );
		
		if ( value instanceof JComponent )
			return (JComponent) value;
		
		super.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, column );
		
		return ToLabelRenderer.render( this, value, isSelected, hasFocus );
	}
	
}
