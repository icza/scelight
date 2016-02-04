/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.gui.page.replist.column;

import hu.scelightapibase.gui.icon.IRIcon;
import hu.sllauncher.gui.icon.LRIcon;

import java.util.Comparator;

/**
 * Base {@link IColumn} implementation to help implementing columns.
 * 
 * @param <T> type of the data of the column; it is recommended for <code>T</code> to implement {@link Comparable} so the table can be sorted by the column
 * 
 * @author Andras Belicza
 */
public abstract class BaseColumn< T > implements IColumn< T > {
	
	/** Display name. */
	protected final String          displayName;
	
	/** Ricon. */
	protected final LRIcon          ricon;
	
	/** Description. */
	protected final String          description;
	
	/** Class of the column data. */
	protected final Class< T >      columnClass;
	
	/** Tells if default sort order of the column is descending. */
	protected final boolean         defaultdesc;
	
	/** Optional comparator for the column. */
	protected final Comparator< T > comparator;
	
	
	/**
	 * Creates a new {@link BaseColumn}.
	 * 
	 * @param displayName display name of the column
	 * @param ricon ricon of the column
	 * @param description short description of the column
	 * @param columnClass class of the column data
	 * @param defaultdesc tells if default sort order of the column is descending
	 */
	public BaseColumn( final String displayName, final LRIcon ricon, final String description, final Class< T > columnClass, final boolean defaultdesc ) {
		this( displayName, ricon, description, columnClass, defaultdesc, null );
	}
	
	/**
	 * Creates a new {@link BaseColumn}.
	 * 
	 * @param displayName display name of the column
	 * @param ricon ricon of the column
	 * @param description short description of the column
	 * @param columnClass class of the column data
	 * @param defaultdesc tells if default sort order of the column is descending
	 * @param comparator optional comparator for the column
	 */
	public BaseColumn( final String displayName, final LRIcon ricon, final String description, final Class< T > columnClass, final boolean defaultdesc,
	        final Comparator< T > comparator ) {
		this.displayName = displayName;
		this.ricon = ricon;
		this.description = description;
		this.columnClass = columnClass;
		this.defaultdesc = defaultdesc;
		this.comparator = comparator;
	}
	
	
	@Override
	public String getDisplayName() {
		return displayName;
	}
	
	@Override
	public IRIcon getRicon() {
		return ricon;
	}
	
	@Override
	public String getDescription() {
		return description;
	}
	
	@Override
	public Class< T > getColumnClass() {
		return columnClass;
	}
	
	@Override
	public boolean isDefaultDesc() {
		return defaultdesc;
	}
	
	@Override
	public Comparator< T > getComparator() {
		return comparator;
	}
	
	@Override
	public String toString() {
		return displayName;
	}
	
}
