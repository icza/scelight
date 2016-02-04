/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllauncher.gui.comp.table.editor;

import java.awt.event.MouseEvent;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.table.TableCellEditor;

/**
 * Abstract base of custom {@link TableCellEditor}s. Contains utilities to specify the required mouse click count to start editing.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public abstract class BaseCellEditor extends AbstractCellEditor implements TableCellEditor {
	
	/** Number of mouse clicks required to start editing. */
	protected int clickCountToStart = 1;
	
	/**
	 * Sets the number of clicks required to start editing.
	 * 
	 * @param clickCountToStart the number of clicks to be set required to start editing
	 */
	public void setClickCountToStart( final int clickCountToStart ) {
		this.clickCountToStart = clickCountToStart;
	}
	
	/**
	 * Returns the number of clicks required to start editing
	 * 
	 * @return the number of clicks required to start editing
	 */
	public int getClickCountToStart() {
		return clickCountToStart;
	}
	
	@Override
	public boolean isCellEditable( final EventObject event ) {
		if ( event instanceof MouseEvent )
			return ( (MouseEvent) event ).getClickCount() >= clickCountToStart;
		
		return super.isCellEditable( event );
	}
	
}
