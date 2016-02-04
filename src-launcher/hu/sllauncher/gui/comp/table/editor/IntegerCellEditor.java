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

import hu.scelightapibase.bean.settings.ISettingChangeListener;
import hu.sllauncher.gui.comp.XSpinner;
import hu.sllauncher.gui.setting.LSettingsGui;

import java.awt.Component;

import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.TableCellEditor;

/**
 * {@link TableCellEditor} for {@link Integer} type.
 * 
 * <p>
 * <i>WARNING!</i> The {@link #dispose()} method must be called when the editor is no longer in use.<br>
 * This is because the editor uses an {@link XSpinner} which registers an {@link ISettingChangeListener} to follow the user's number format preference.
 * </p>
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class IntegerCellEditor extends BaseCellEditor {
	
	/** A cached instance. */
	public static final IntegerCellEditor INSTANCE = new IntegerCellEditor();
	
	/** {@link XSpinner} editor component. */
	private final XSpinner                spinner  = new XSpinner();
	
	@Override
	public Object getCellEditorValue() {
		return spinner.getValue();
	}
	
	@Override
	public Component getTableCellEditorComponent( final JTable table, final Object value, final boolean isSelected, final int row, final int column ) {
		spinner.setValue( value == null ? 0 : value );
		
		SwingUtilities.invokeLater( new Runnable() {
			@Override
			public void run() {
				if ( spinner.getEditor() instanceof JSpinner.NumberEditor )
					( (JSpinner.NumberEditor) spinner.getEditor() ).getTextField().requestFocusInWindow();
			}
		} );
		
		return spinner;
	}
	
	/**
	 * Disposes this editor: removes registered setting change listeners.
	 */
	public void dispose() {
		LSettingsGui.removeAllBoundedScl( spinner );
	}
	
}
