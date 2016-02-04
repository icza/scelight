/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllauncher.gui.comp.table;

import hu.scelightapibase.gui.comp.table.ITableRowSorter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.SortOrder;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 * An improved {@link TableRowSorter} which allows setting default sorting order of columns.
 * 
 * @author Andras Belicza
 */
public class XTableRowSorter extends ITableRowSorter {
	
	/** Set of columns which must be sorted descendant by default. */
	private Set< Integer > defaultDescColumnSet;
	
	/**
	 * Creates a new {@link XTableRowSorter}.
	 * 
	 * @param model reference to the table model
	 */
	public XTableRowSorter( final TableModel model ) {
		super( model );
	}
	
	@Override
	public void setColumnDefaultDescs( final boolean defaultDesc, final int... columns ) {
		for ( final int column : columns )
			setColumnDefaultDesc( column, defaultDesc );
	}
	
	@Override
	public void setColumnDefaultDesc( final int column, final boolean defaultDesc ) {
		if ( defaultDescColumnSet == null )
			defaultDescColumnSet = new HashSet<>();
		
		if ( defaultDesc )
			defaultDescColumnSet.add( column );
		else
			defaultDescColumnSet.remove( column );
	}
	
	@Override
	public void toggleSortOrder( final int column ) {
		if ( defaultDescColumnSet != null && defaultDescColumnSet.contains( column ) ) {
			// Column needs to be sorted descending by default
			final List< ? extends SortKey > sortKeys = getSortKeys();
			
			// Search for the column's sort key
			int idx;
			for ( idx = sortKeys.size() - 1; idx >= 0; idx-- )
				if ( sortKeys.get( idx ).getColumn() == column )
					break;
			
			if ( idx != 0 ) {
				// Currently column is not the primary sorting key but is about to become the primary,
				// which means the default descending sorting order has to be applied.
				final List< SortKey > sortKeyList = new ArrayList<>( getSortKeys() );
				if ( idx > 0 ) {
					// Column is part of the sorting strategy, move it to the front
					sortKeyList.remove( idx );
				}
				// Add sort key for the column as ASCENDING and super implementation will toggle it to descending!
				sortKeyList.add( 0, new SortKey( column, SortOrder.ASCENDING ) );
				setSortKeys( sortKeyList );
			}
		}
		
		super.toggleSortOrder( column );
	}
	
}
