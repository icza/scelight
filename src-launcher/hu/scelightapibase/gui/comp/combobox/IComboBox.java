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

import javax.swing.JComboBox;

/**
 * An extended {@link JComboBox} with a custom renderer having the unified rendering logic incorporated (
 * {@link hu.scelightapi.util.gui.IGuiUtils#renderToLabel(javax.swing.JLabel, Object, boolean, boolean)}).
 * 
 * <p>
 * Supports marking items that will be followed by a separator line in the popup list and marking disabled items which cannot be selected.
 * </p>
 * 
 * @param <E> type of the elements added to the combo box
 * 
 * @author Andras Belicza
 * 
 * @see IComboBoxModel
 * @see hu.scelightapi.service.IGuiFactory#newComboBox()
 * @see hu.scelightapi.service.IGuiFactory#newComboBox(Object[])
 * @see hu.scelightapi.service.IGuiFactory#newComboBox(java.util.Vector)
 */
public interface IComboBox< E > {
	
	/**
	 * Casts this instance to {@link JComboBox}.
	 * 
	 * @return <code>this</code> as a {@link JComboBox}
	 */
	JComboBox< E > asComboBox();
	
	/**
	 * Returns the improved combo box model of this improved combo box.
	 * 
	 * @return the improved combo box model of this improved combo box
	 */
	IComboBoxModel< E > getXComboBoxModel();
	
	/**
	 * Overrides {@link JComboBox#getSelectedItem()} to return the selected value as the type specified by the element type parameter
	 * 
	 * @return the selected item
	 */
	E getSelectedItem();
	
	/**
	 * Marks items that will be followed by a separator in the popup list.
	 * 
	 * @param items items to be marked that will be followed by a separator in the popup list
	 * 
	 * @see #markSeparatedItems(Object...)
	 */
	void markSeparatedItems( Collection< ? extends E > items );
	
	/**
	 * Marks items that will be followed by a separator in the popup list.
	 * 
	 * @param items items to be marked that will be followed by a separator in the popup list
	 * 
	 * @see #markSeparatedItems(Collection)
	 */
	void markSeparatedItems( @SuppressWarnings( "unchecked" ) E... items );
	
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
	void markDisabledItems( @SuppressWarnings( "unchecked" ) E... items );
	
}
