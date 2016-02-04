/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapi.bean.repfilters;

import hu.scelightapi.search.IConnection;
import hu.scelightapi.search.IFilterBy;
import hu.scelightapi.search.IFilterByGroup;
import hu.scelightapi.search.IOperator;
import hu.scelightapi.service.IFactory;
import hu.scelightapibase.bean.IBean;
import hu.scelightapibase.bean.IIdedBean;

/**
 * Replay filter bean.
 * 
 * @author Andras Belicza
 * 
 * @see IFactory#newRepFilterBean()
 * @see IRepFiltersBean
 * @see hu.scelightapi.search.IRepSearchEngine
 * @see IIdedBean
 * @see IBean
 */
public interface IRepFilterBean extends IIdedBean {
	
	/**
	 * Tells if this filter is active.<br>
	 * A filter is active if it's enabled and is valid ({@link #isValid()} returns <code>true</code>).
	 * 
	 * @return true if this filter is active; false otherwise
	 */
	boolean isActive();
	
	/**
	 * Tells if this filter is valid.<br>
	 * A filter is valid if:
	 * <ul>
	 * <li>all its <i>essential</i> properties are specified (not <code>null</code>)</li>
	 * <li>its filter by is allowed by its filter by group</li>
	 * <li>its operator is allowed by its filter by</li>
	 * </ul>
	 * 
	 * @return true if this filter is valid; false otherwise
	 */
	boolean isValid();
	
	/**
	 * Returns if the filter is enabled.
	 * 
	 * @return if the filter is enabled
	 */
	Boolean getEnabled();
	
	/**
	 * Sets if the filter is enabled.
	 * 
	 * @param enabled true if the filter is enabled
	 */
	void setEnabled( Boolean enabled );
	
	/**
	 * Returns the logical connection of this filter.
	 * 
	 * @return the logical connection of this filter
	 */
	IConnection getConnection();
	
	/**
	 * Sets the logical connection of this filter.
	 * 
	 * @param connection logical connection of this filter to be set
	 */
	void setConnection( IConnection connection );
	
	/**
	 * Returns the filter by group.
	 * 
	 * @return the filter by group
	 */
	IFilterByGroup getFilterByGroup();
	
	/**
	 * Sets the filter by group.
	 * 
	 * @param filterByGroup filter by group to be set
	 */
	void setFilterByGroup( IFilterByGroup filterByGroup );
	
	/**
	 * Returns the filter by.
	 * 
	 * @return the filter by
	 */
	IFilterBy getFilterBy();
	
	/**
	 * Sets the filter by.
	 * 
	 * @param filterBy filter by to be set
	 */
	void setFilterBy( IFilterBy filterBy );
	
	/**
	 * Returns the operator.
	 * 
	 * @return the operator
	 */
	IOperator getOperator();
	
	/**
	 * Sets the operator.
	 * 
	 * @param operator operator to be set
	 */
	void setOperator( IOperator operator );
	
	/**
	 * Returns the typed object value of the filter.
	 * 
	 * @return the typed object value of the filter
	 */
	Object getValue();
	
	/**
	 * Sets the typed object value of the filter.
	 * 
	 * @param value typed object value of the filter to be set
	 */
	void setValue( Object value );
	
	/**
	 * Returns the comment of the filter.
	 * 
	 * @return the comment of the filter
	 */
	String getComment();
	
	/**
	 * Sets the comment of the filter.
	 * 
	 * @param comment comment of the filter to be set
	 */
	void setComment( String comment );
	
}
