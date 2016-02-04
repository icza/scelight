/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.gui.page.repanalyzer;

import hu.scelight.service.env.Env;
import hu.scelight.service.settings.Settings;
import hu.sllauncher.gui.comp.table.XTable;
import hu.sllauncher.gui.comp.table.XTableModel;
import hu.sllauncher.gui.comp.table.renderer.XTableCellRenderer;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * A table cell renderer which colors rows based on user color.
 * 
 * <p>
 * The column model index holding the user color (type of {@link Color}) must be specified as a constructor parameter of type which will be used to color the
 * foreground and background.
 * </p>
 * 
 * @author Andras Belicza
 */
public class UserColoredTableRenderer implements TableCellRenderer {
	
	/** A darker version of the color white. */
	private static final Color       DARK_WHITE = new Color( 240, 240, 240 );
	
	/** Column model index holding the user color (type of {@link Color}). */
	private final int                colorColumnIdx;
	
	/** Reference to the internal renderer of the improved table. */
	private final XTableCellRenderer xTableCellRenderer;
	
	/** Reference to the model of the improved table. */
	private final XTableModel        model;
	
	
	/**
	 * Creates a new {@link UserColoredTableRenderer}.
	 * 
	 * @param table table
	 * @param colorColumnIdx column model index holding the user color (type of {@link Color})
	 */
	public UserColoredTableRenderer( final XTable table, final int colorColumnIdx ) {
		xTableCellRenderer = table.getXTableCellRenderer();
		model = table.getXTableModel();
		
		this.colorColumnIdx = colorColumnIdx;
	}
	
	@Override
	public Component getTableCellRendererComponent( final JTable table, Object value, final boolean isSelected, final boolean hasFocus, final int row,
	        int column ) {
		
		final Component result = xTableCellRenderer.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, column );
		
		// If the returned renderer component is not the improved renderer itself, it is a custom which we respectfully return and not alter further.
		if ( result != xTableCellRenderer )
			return result;
		
		final Color c = (Color) model.getValueAt( table.convertRowIndexToModel( row ), colorColumnIdx );
		if ( c == null ) {
			if ( !isSelected ) { // If selected, these colors are "overridden" (set)
				xTableCellRenderer.setBackground( null );
				xTableCellRenderer.setForeground( null );
			}
		}
		if ( c != null ) {
			final boolean darkBackground = Env.APP_SETTINGS.get( Settings.INVERT_COLORS_IN_USER_TABLES ) ^ isSelected;
			xTableCellRenderer.setBackground( darkBackground ? c : DARK_WHITE );
			xTableCellRenderer.setForeground( darkBackground ? DARK_WHITE : c );
		}
		
		return xTableCellRenderer;
	}
	
}
