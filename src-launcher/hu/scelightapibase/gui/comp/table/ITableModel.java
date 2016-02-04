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

import java.util.List;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 * An improved {@link TableModel} which allows setting the class of columns.
 * 
 * @author Andras Belicza
 * 
 * @see ITable#getXTableModel()
 */
@SuppressWarnings( "serial" )
public abstract class ITableModel extends DefaultTableModel {
	
	/**
	 * Sets the classes of multiple columns to be returned by {@link #getColumnClass(int)}.
	 * <p>
	 * The passed column classes will be associated with the columns having a column index equal to the argument index.
	 * </p>
	 * 
	 * @param columnClasses column class to be set
	 */
	public abstract void setColumnClasses( Class< ? >... columnClasses );
	
	/**
	 * Sets the classes of multiple columns to be returned by {@link #getColumnClass(int)}.
	 * <p>
	 * The passed column classes will be associated with the columns having a column index equal to the column class index.
	 * </p>
	 * 
	 * @param columnClassList list of column classes to be set
	 */
	public abstract void setColumnClasses( List< Class< ? > > columnClassList );
	
	/**
	 * Sets the class of a column to be returned by {@link #getColumnClass(int)}.
	 * 
	 * @param columnIndex column index whose class to be set
	 * @param columnClass column class to be set; you can pass a <code>null</code> value to clear the column class
	 */
	public abstract void setColumnClass( final int columnIndex, final Class< ? > columnClass );
	
}
