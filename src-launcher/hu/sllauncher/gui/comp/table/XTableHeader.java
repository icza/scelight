/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllauncher.gui.comp.table;

import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 * An improved table header.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class XTableHeader extends JTableHeader {
	
	/**
	 * Creates a new {@link XTableHeader}.
	 * 
	 * @param columnModel reference to the table column model
	 */
	public XTableHeader( final TableColumnModel columnModel ) {
		super( columnModel );
	}
	
	/**
	 * Overridden to return header cell value as tool tip if it's truncated (due to not fitting into its cell's area).
	 */
	@Override
	public String getToolTipText( final MouseEvent event ) {
		// First check if super implementation returns a tool tip:
		// (super implementation checks if a tool tip is set on the renderer for the cell,
		// and if no, also checks if a tool tip has been set on the table itself)
		final String tip = super.getToolTipText( event );
		if ( tip != null )
			return tip;
		
		// No tool tip from the renderer, and no own tool tip has been set.
		// So let's check if value doesn't fit into the cell, and if so provide it's full text as the tool tip
		final Point point = event.getPoint();
		
		final int column = columnAtPoint( point );
		
		// Check if value is truncated (doesn't fit into the cell):
		if ( column < 0 ) // event is not on a cell
			return null;
		
		// Check if value is truncated (doesn't fit into the cell):
		final TableColumn tableColumn = getColumnModel().getColumn( column );
		TableCellRenderer renderer = tableColumn.getHeaderRenderer();
		if ( renderer == null )
			renderer = getDefaultRenderer();
		final Object value = tableColumn.getHeaderValue();
		if ( value == null )
			return null;
		if ( getHeaderRect( column ).width >= renderer.getTableCellRendererComponent( getTable(), value, false, false, 0, column ).getPreferredSize().width )
			return null; // Value fits in the cell, no need to show tool tip
			
		// At this point no one provided tool tip, and value is rendered truncated. Provide its full text as the tool tip.
		return value.toString();
	}
	
	/**
	 * Positions tool tips exactly over the header cell they belong to, also avoids showing empty tool tips (by returning <code>null</code>).
	 */
	@Override
	public Point getToolTipLocation( final MouseEvent event ) {
		// If tool tip is provided by the renderer or the table itself, use default location:
		if ( super.getToolTipText( event ) != null )
			return super.getToolTipLocation( event );
		
		// If no tool tip, return null to prevent displaying an empty tool tip
		if ( getToolTipText( event ) == null )
			return null;
		
		// Else align to the cell:
		final Point point = event.getPoint();
		
		final int column = columnAtPoint( point );
		
		if ( column < 0 ) // event is not on a cell
			return null;
		
		final Point location = getHeaderRect( column ).getLocation();
		// Adjust due to the tool tip border and padding:
		location.x += 1;
		location.y -= 2;
		
		return location;
	}
	
}
