/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllauncher.gui.comp.combobox;

import hu.scelightapibase.gui.comp.combobox.IComboBoxModel;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;

/**
 * An improved combo box model which allows marking items disabled (that can't be selected).
 * 
 * @param <E> type of the elements added to the model
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class XComboBoxModel< E > extends DefaultComboBoxModel< E > implements IComboBoxModel< E > {
	
	/** List of disabled items (these can't be selected). */
	private Set< E > disabledItemSet;
	
	/**
	 * Creates a new {@link XComboBoxModel}.
	 * 
	 * @param items initial items to be added to the model
	 */
	public XComboBoxModel( final Vector< E > items ) {
		super( items );
	}
	
	@Override
	public void markDisabledItems( final Collection< ? extends E > items ) {
		if ( disabledItemSet == null )
			disabledItemSet = new HashSet<>();
		
		disabledItemSet.addAll( items );
	}
	
	@Override
	public void markDisabledItems( @SuppressWarnings( "unchecked" ) final E... items ) {
		if ( disabledItemSet == null )
			disabledItemSet = new HashSet<>();
		
		Collections.addAll( disabledItemSet, items );
	}
	
	@Override
	public boolean isItemDisabled( final Object item ) {
		return disabledItemSet != null && disabledItemSet.contains( item );
	}
	
	@Override
	public void setSelectedItem( final Object item ) {
		if ( disabledItemSet != null && disabledItemSet.contains( item ) )
			return;
		super.setSelectedItem( item );
	};
	
}
