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

import hu.sllauncher.gui.comp.PathField;

import java.awt.Color;
import java.awt.Component;
import java.nio.file.Path;

import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.TableCellEditor;

/**
 * {@link TableCellEditor} for {@link Path} type.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class PathCellEditor extends BaseCellEditor {
	
	/** A cached instance. */
	public static final PathCellEditor INSTANCE = new PathCellEditor();
	
	/** {@link PathField} editor component. */
	private final PathField            pc       = new PathField();
	
	/**
	 * Creates a new {@link PathCellEditor}.
	 */
	public PathCellEditor() {
		pc.textField.setBorder( BorderFactory.createLineBorder( Color.BLACK ) );
		pc.button.setBorder( null );
	}
	
	@Override
	public Object getCellEditorValue() {
		return pc.getPath();
	}
	
	@Override
	public Component getTableCellEditorComponent( final JTable table, final Object value, final boolean isSelected, final int row, final int column ) {
		pc.setPath( (Path) value );
		
		SwingUtilities.invokeLater( new Runnable() {
			@Override
			public void run() {
				pc.textField.requestFocusInWindow();
			}
		} );
		
		return pc;
	}
	
}
