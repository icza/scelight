/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapibase.gui.comp.combobox;

import java.util.Collection;

import javax.swing.ComboBoxModel;

/**
 * An improved combo box model which allows marking items disabled (that can't be selected).
 * 
 * @param <E> type of the elements added to the model
 * 
 * @author Andras Belicza
 * 
 * @see IComboBox
 */
public interface IComboBoxModel< E > extends ComboBoxModel< E > {
	
	/**
	 * Makes the specified items disabled (can't be selected).
	 * 
	 * @param items items to be marked disabled
	 * 
	 * @see #markDisabledItems(Object...)
	 */
	void markDisabledItems( Collection< ? extends E > items );
	
	/**
	 * Makes the specified items disabled (can't be selected).
	 * 
	 * @param items items to be marked disabled
	 * 
	 * @see #markDisabledItems(Collection)
	 */
	void markDisabledItems( @SuppressWarnings( "unchecked" ) final E... items );
	
	/**
	 * Tells if the specified item is marked disabled.
	 * 
	 * @param item item to be checked
	 * @return true if the specified item is marked disabled; false otherwise
	 */
	boolean isItemDisabled( final Object item );
	
}
