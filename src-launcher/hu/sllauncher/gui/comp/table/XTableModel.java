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

import hu.scelightapibase.gui.comp.table.ITableModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An improved table model which allows setting the class of columns.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class XTableModel extends ITableModel {
	
	/** Explicitly set column classes (mapped from column index). */
	private Map< Integer, Class< ? > > columnClassMap;
	
	@Override
	public void setColumnClasses( final Class< ? >... columnClasses ) {
		for ( int i = columnClasses.length - 1; i >= 0; i-- )
			setColumnClass( i, columnClasses[ i ] );
	}
	
	@Override
	public void setColumnClasses( final List< Class< ? > > columnClassList ) {
		for ( int i = columnClassList.size() - 1; i >= 0; i-- )
			setColumnClass( i, columnClassList.get( i ) );
	}
	
	@Override
	public void setColumnClass( final int columnIndex, final Class< ? > columnClass ) {
		if ( columnClassMap == null )
			columnClassMap = new HashMap<>();
		
		columnClassMap.put( columnIndex, columnClass );
	}
	
	@Override
	public Class< ? > getColumnClass( final int columnIndex ) {
		final Class< ? > columnClass = columnClassMap == null ? null : columnClassMap.get( columnIndex );
		
		return columnClass == null ? super.getColumnClass( columnIndex ) : columnClass;
	}
	
}
