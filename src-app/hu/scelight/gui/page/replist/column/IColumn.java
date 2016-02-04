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

import hu.scelight.sc2.rep.repproc.RepProcessor;
import hu.scelightapibase.gui.icon.IRIcon;

import java.util.Comparator;

/**
 * Column interface of the replay list table.
 * 
 * <p>
 * By default column instances are cached and shared.<br>
 * If the {@link #getData(RepProcessor)} method implementation of an {@link IColumn} implementation <i>depends</i> on the environment or settings to calculate
 * and return the column data (not just on the {@link RepProcessor} parameter), the implementor class must be annotated with the {@link Dependent} annotation to
 * disallow caching optimizations.
 * </p>
 * 
 * <p>
 * <b>Implementation requirements:</b>
 * </p>
 * <ul>
 * <li>Implementations must have a no-arg constructor.
 * <li>Implementations must be thread-safe: the {@link #getData(RepProcessor)} method might be called concurrently from multiple threads.
 * </ul>
 * 
 * @param <T> type of the data of the column; it is recommended for <code>T</code> to implement {@link Comparable} so the table can be sorted by the column
 * 
 * @author Andras Belicza
 * 
 * @see RepListColumnRegistry
 * @see Dependent
 */
public interface IColumn< T > {
	
	/**
	 * Returns the display name of the column. This will appear in the table header.
	 * 
	 * @return the display name of the column
	 */
	String getDisplayName();
	
	/**
	 * Returns the ricon of the column.
	 * 
	 * @return the ricon name of the column
	 */
	IRIcon getRicon();
	
	/**
	 * Returns a short description of the column.
	 * 
	 * @return a short description of the column
	 */
	String getDescription();
	
	/**
	 * Returns the class of the column data.
	 * 
	 * @return the class of the column data
	 */
	Class< T > getColumnClass();
	
	/**
	 * Tells if the column is to be sorted descending order by default.
	 * 
	 * @return true if the column is to be sorted descending order by default; false otherwise
	 */
	boolean isDefaultDesc();
	
	/**
	 * Returns a comparator for the column.
	 * 
	 * <p>
	 * No comparator is required if the data type implements the {@link Comparator} which defines the intended sort order.
	 * </p>
	 * 
	 * @return a comparator for the column
	 */
	Comparator< T > getComparator();
	
	/**
	 * Returns the column data for the specified replay processor.
	 * 
	 * @param repProc replay processor of the replay to return the data for
	 * @return the column data for the specified replay processor
	 */
	T getData( RepProcessor repProc );
	
}
