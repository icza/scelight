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

import hu.scelightapi.search.IRepSearchEngine;
import hu.scelightapi.service.IFactory;
import hu.scelightapibase.bean.IBean;

import java.util.List;

/**
 * Replay filters holder bean.
 * 
 * @author Andras Belicza
 * 
 * @see IFactory#newRepFiltersBean()
 * @see IRepFilterBean
 * @see IRepSearchEngine
 * @see IBean
 */
public interface IRepFiltersBean extends IBean {
	
	/**
	 * Returns the number of active filters.
	 * 
	 * @return the number of active filters
	 */
	int getActiveCount();
	
	/**
	 * Returns the non-null the replay filter bean list.
	 * 
	 * <p>
	 * If no replay filter bean list have been set previously, a new empty list will be created and returned.
	 * </p>
	 * 
	 * @return the replay filter bean list
	 */
	List< ? extends IRepFilterBean > getRepFilterBeanList();
	
	/**
	 * Sets the replay filter bean list.
	 * 
	 * @param repFilterBeanList replay filter bean list to be set
	 * 
	 * @throws IllegalArgumentException if this {@link IRepFiltersBean} instance is provided by the API but the specified list contains {@link IRepFilterBean}
	 *             elements which are not provided by the API
	 */
	void setRepFilterBeanList( List< ? extends IRepFilterBean > repFilterBeanList ) throws IllegalArgumentException;
	
}
