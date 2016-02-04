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

import hu.scelightapibase.action.IAction;
import hu.scelightapibase.gui.comp.ISelectionListenerToolBar;
import hu.sllauncher.util.gui.LGuiUtils;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;

/**
 * An extended {@link XToolBar} which listens the selection changes of a component, and can have buttons and an info text which gets enabled / disabled based on
 * the selection.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public abstract class SelectionListenerToolBar extends XToolBar implements ISelectionListenerToolBar {
	
	/**
	 * List of buttons bounded to selection. These are to be enabled if there are selected items, and disabled if no item is selected in the listened component.
	 */
	private final List< JButton > selEnabledButtonList = new ArrayList<>();
	
	/**
	 * Select info label which is to be disabled if there are selected items and enabled if nothing is selected in the listened component.
	 */
	private XLabel                selectInfoLabel;
	
	/**
	 * Creates a new {@link SelectionListenerToolBar}.
	 * 
	 */
	public SelectionListenerToolBar() {
		super( true );
	}
	
	@Override
	public XLabel addSelectInfoLabel( final String text ) {
		selectInfoLabel = new XLabel( text );
		add( selectInfoLabel );
		addSeparator();
		
		return selectInfoLabel;
	}
	
	@Override
	public JButton addSelEnabledButton( final IAction action ) {
		final JButton button = add( action );
		selEnabledButtonList.add( button );
		LGuiUtils.autoCreateDisabledImage( button );
		
		return button;
	}
	
	/**
	 * Should be called when the listened component's selection changes. Properly enables / disables the buttons and info text based on the selection status.
	 * 
	 * @param hasSelection selection status, tells if there are selected items in the listened component
	 */
	protected void selectionChanged( final boolean hasSelection ) {
		for ( final JButton button : selEnabledButtonList )
			button.setEnabled( hasSelection );
		
		if ( selectInfoLabel != null )
			selectInfoLabel.setEnabled( !hasSelection );
	}
	
}
