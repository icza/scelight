/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllauncher.gui.comp;

import hu.scelightapibase.gui.comp.IToolBarForTable;
import hu.scelightapibase.gui.comp.table.ITable;
import hu.sllauncher.gui.comp.table.XTable;
import hu.sllauncher.util.gui.adapter.ListSelectionAdapter;

import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * A {@link SelectionListenerToolBar} which targets an {@link XTable} and listens for its row selection changes.
 * 
 * <p>
 * The {@link #finalizeLayout()} method must be called in order to install the {@link ListSelectionListener}.
 * </p>
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class ToolBarForTable extends SelectionListenerToolBar implements IToolBarForTable {
	
	/** The targeted table. */
	private final JTable table;
	
	/**
	 * Creates a new {@link ToolBarForTable}.
	 * 
	 * @param table targeted table whose selection changes to listen
	 */
	public ToolBarForTable( final ITable table ) {
		this.table = table.asTable();
	}
	
	@Override
	public void finalizeLayout() {
		super.finalizeLayout();
		
		table.getSelectionModel().addListSelectionListener( new ListSelectionAdapter( true ) {
			@Override
			public void valueChanged( final ListSelectionEvent event ) {
				if ( event != null && event.getValueIsAdjusting() )
					return;
				
				selectionChanged( table.getSelectedRow() >= 0 );
			}
		} );
	}
	
}
