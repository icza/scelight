/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.gui.comp;

import hu.scelightapi.gui.comp.IList;
import hu.scelightapi.gui.comp.IToolBarForList;
import hu.sllauncher.gui.comp.SelectionListenerToolBar;
import hu.sllauncher.util.gui.adapter.ListSelectionAdapter;

import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * A {@link SelectionListenerToolBar} which targets an {@link XList} and listens for its item selection changes.
 * 
 * <p>
 * The {@link #finalizeLayout()} method must be called in order to install the {@link ListSelectionListener}.
 * </p>
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class ToolBarForList extends SelectionListenerToolBar implements IToolBarForList {
	
	/** The targeted list. */
	private final JList< ? > list;
	
	/**
	 * Creates a new {@link ToolBarForList}.
	 * 
	 * @param list targeted list whose selection changes to listen
	 */
	public ToolBarForList( final IList< ? > list ) {
		this.list = list.asList();
	}
	
	/**
	 * Overridden to register {@link ListSelectionListener} to the targeted list and also to invoke it.
	 */
	@Override
	public void finalizeLayout() {
		super.finalizeLayout();
		
		list.getSelectionModel().addListSelectionListener( new ListSelectionAdapter( true ) {
			@Override
			public void valueChanged( final ListSelectionEvent event ) {
				if ( event != null && event.getValueIsAdjusting() )
					return;
				
				selectionChanged( list.getSelectedIndex() >= 0 );
			}
		} );
	}
	
}
