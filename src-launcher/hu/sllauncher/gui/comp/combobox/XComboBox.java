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

import hu.scelightapibase.gui.comp.combobox.IComboBox;
import hu.sllauncher.util.LUtils;
import hu.sllauncher.util.gui.ToLabelRenderer;

import java.awt.Color;
import java.awt.Component;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.border.Border;

/**
 * An extended {@link JComboBox} with a custom renderer having the unified {@link ToLabelRenderer} logic incorporated.
 * 
 * <p>
 * Supports marking items that will be followed by a separator line in the popup list and marking disabled items which cannot be selected.
 * </p>
 * 
 * @param <E> type of the elements added to the combo box
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class XComboBox< E > extends JComboBox< E > implements IComboBox< E > {
	
	/** Border to be used to "imitate" a separator after an element. */
	private static final Border       SEPARATOR_BORDER = BorderFactory.createCompoundBorder(
	                                                           BorderFactory.createCompoundBorder( BorderFactory.createEmptyBorder( 1, 1, 0, 1 ),
	                                                                   BorderFactory.createMatteBorder( 0, 0, 1, 0, Color.GRAY ) ),
	                                                           new DefaultListCellRenderer().getBorder() );
	
	
	/** Improved combo box model . */
	private final XComboBoxModel< E > comboBoxModel;
	
	/** List of items that will be followed by a separator in the popup list. */
	private Set< E >                  separatedItemSet;
	
	
	/**
	 * Creates a new {@link XComboBox}.
	 */
	public XComboBox() {
		this( LUtils.< E > vectort() );
	}
	
	/**
	 * Creates a new {@link XComboBox}.
	 * 
	 * @param items initial items to be added to the combo box
	 */
	public XComboBox( final E[] items ) {
		this( LUtils.vectort( items ) );
	}
	
	/**
	 * Creates a new {@link XComboBox}.
	 * 
	 * @param items initial items to be added to the combo box
	 */
	public XComboBox( final Vector< E > items ) {
		super( new XComboBoxModel<>( items ) );
		
		comboBoxModel = (XComboBoxModel< E >) getModel();
		
		setMaximumRowCount( 14 );
		
		setRenderer( new DefaultListCellRenderer() {
			@Override
			public Component getListCellRendererComponent( final JList< ? > list, final Object value, final int index, final boolean isSelected,
			        final boolean cellHasFocus ) {
				if ( value instanceof JComponent )
					return (JComponent) value;
				
				final boolean itemEnabled = !comboBoxModel.isItemDisabled( value );
				
				super.getListCellRendererComponent( list, value, index, isSelected && itemEnabled, cellHasFocus );
				
				ToLabelRenderer.render( this, value, isSelected, cellHasFocus );
				
				// Set cell value as tool tip if it's truncated (due to not fitting into into the cell's area).
				final int comboWidth = XComboBox.this.getWidth();
				if ( getToolTipText() == null && comboWidth > 0 && getPreferredSize().width > comboWidth )
					setToolTipText( value.toString() );
				
				if ( separatedItemSet != null && separatedItemSet.contains( value ) )
					setBorder( SEPARATOR_BORDER );
				
				setEnabled( itemEnabled );
				
				return this;
			}
		} );
	}
	
	@Override
	public JComboBox< E > asComboBox() {
		return this;
	}
	
	@Override
	public XComboBoxModel< E > getXComboBoxModel() {
		return comboBoxModel;
	}
	
	@Override
	@SuppressWarnings( "unchecked" )
	public E getSelectedItem() {
		return (E) super.getSelectedItem();
	}
	
	@Override
	public void markSeparatedItems( final Collection< ? extends E > items ) {
		if ( separatedItemSet == null )
			separatedItemSet = new HashSet<>();
		
		separatedItemSet.addAll( items );
	}
	
	@Override
	public void markSeparatedItems( @SuppressWarnings( "unchecked" ) final E... items ) {
		if ( separatedItemSet == null )
			separatedItemSet = new HashSet<>();
		
		Collections.addAll( separatedItemSet, items );
	}
	
	@Override
	public void markDisabledItems( final Collection< ? extends E > items ) {
		comboBoxModel.markDisabledItems( items );
	}
	
	@Override
	public void markDisabledItems( @SuppressWarnings( "unchecked" ) final E... items ) {
		comboBoxModel.markDisabledItems( items );
	}
	
}
