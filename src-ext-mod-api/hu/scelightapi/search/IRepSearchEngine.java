/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapi.search;

import hu.scelightapi.bean.repfilters.IRepFilterBean;
import hu.scelightapi.bean.repfilters.IRepFiltersBean;
import hu.scelightapi.gui.dialog.IRepFiltersEditorDialog;
import hu.scelightapi.sc2.rep.repproc.IRepProcessor;
import hu.scelightapi.service.IFactory;

/**
 * Replay search engine interface.
 * 
 * <p>
 * Implementation is thread safe, the {@link #isIncluded(IRepProcessor)} can be called parallel from multiple threads.
 * </p>
 * 
 * @author Andras Belicza
 * 
 * @see IFactory#newRepSearchEngine(IRepFiltersBean)
 * @see IRepFiltersBean
 * @see IRepFilterBean
 * @see IRepFiltersEditorDialog
 */
public interface IRepSearchEngine {
	
	/**
	 * Returns the number of filters (the number of active filters).
	 * 
	 * @return the number of filters (the number of active filters)
	 */
	int getFiltersCount();
	
	/**
	 * Tells if the specified replay processor is included by the engine's replay filters.
	 * 
	 * @param repProc replay processor to test
	 * @return true if the specified replay processor is included by the engine's replay filters; false otherwise
	 */
	boolean isIncluded( IRepProcessor repProc );
	
	/**
	 * Builds an HTML table from the structure of the filters.
	 * 
	 * <p>
	 * This method builds a tree-like formatted HTML table describing the structure of the filters or a constant <code>"&lt;No active filters&gt;"</code> string
	 * if no filters are present. Wrapping <code>&lt;html&gt;</code> tags are not added, it is the responsibility of the caller.
	 * </p>
	 * 
	 * <p>
	 * The following CSS style classes are used to format the elements of the filters (these may/must be defined in the wrapping HTML):
	 * </p>
	 * <ul>
	 * <li><code>"conn"</code>: applied on table cells (<code>&lt;td&gt;</code>) holding the logical connection</li>
	 * <li><code>"fbg"</code>: applied on the text of the filter by groups
	 * <li><code>"fby"</code>: applied on the text of the filter by's
	 * <li><code>"op"</code>: applied on the text of the filter operators
	 * <li><code>"fval"</code>: applied on the text of the filter values
	 * </ul>
	 * 
	 * @param sb string builder to append the structure HTML table
	 */
	void getStructure( StringBuilder sb );
	
}
