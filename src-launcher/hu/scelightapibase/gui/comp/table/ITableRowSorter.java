/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapibase.gui.comp.table;

import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 * An improved {@link TableRowSorter} which allows setting default sorting order of columns.
 * 
 * @author Andras Belicza
 * 
 * @see ITable#getXTableRowSorter()
 */
public abstract class ITableRowSorter extends TableRowSorter< TableModel > {
	
	/**
	 * Creates a new {@link ITableRowSorter}.
	 * 
	 * @param model reference to the table model
	 */
	public ITableRowSorter( final TableModel model ) {
		super( model );
	}
	
	/**
	 * Sets whether the specified columns are to be sorted descendant by default.
	 * 
	 * @param defaultDesc if true, the columns will be sorted descending by default; else the columns will be sorted ascending
	 * @param columns columns whose sorting property to be set
	 */
	public abstract void setColumnDefaultDescs( boolean defaultDesc, int... columns );
	
	/**
	 * Sets whether the specified column is to be sorted descendant by default.
	 * 
	 * @param column column whose sorting property to be set
	 * @param defaultDesc if true, the column will be sorted descending by default; else the column will be sorted ascending
	 */
	public abstract void setColumnDefaultDesc( int column, boolean defaultDesc );
	
}
