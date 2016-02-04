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

import hu.sllauncher.gui.comp.XCheckBox;
import hu.sllauncher.gui.icon.LIcons;

import javax.swing.DefaultCellEditor;
import javax.swing.table.TableCellEditor;

/**
 * {@link TableCellEditor} for {@link Boolean} type.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class BoolCellEditor extends DefaultCellEditor {
	
	/** A cached instance. */
	public static final BoolCellEditor INSTANCE = new BoolCellEditor();
	
	/**
	 * Creates a new {@link BoolCellEditor}.
	 */
	private BoolCellEditor() {
		this( new XCheckBox() );
	}
	
	/**
	 * Creates a new {@link BoolCellEditor}.
	 * 
	 * @param checkBox check box editor component
	 */
	private BoolCellEditor( final XCheckBox checkBox ) {
		super( checkBox );
		
		checkBox.setIcon( LIcons.F_CROSS.get() );
		checkBox.setSelectedIcon( LIcons.F_TICK.get() );
		
		setClickCountToStart( 2 );
	}
	
}
