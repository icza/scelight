/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapi.sc2.rep.model.initdata.gamedesc;

import hu.scelight.sc2.rep.model.initdata.gamedesc.ExpansionLevel;
import hu.scelightapi.util.gui.ITableIcon;
import hu.scelightapibase.util.IEnum;
import hu.scelightapibase.util.gui.HasRIcon;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Expansion level.
 * 
 * @author Andras Belicza
 * 		
 * @see IEnum
 */
public interface IExpansionLevel extends HasRIcon, IEnum {
	
	/** Legacy of the Void. */
	IExpansionLevel	LOTV	= ExpansionLevel.LOTV;
	
	/** Heart of the Swarm. */
	IExpansionLevel	HOTS	= ExpansionLevel.HOTS;
	
	/** Wings of Liberty. */
	IExpansionLevel	WOL		= ExpansionLevel.WOL;
	
	/** Unknown. */
	IExpansionLevel	UNKNOWN	= ExpansionLevel.UNKNOWN;
	
	
	/** An unmodifiable list of all the expansion levels. */
	List< IExpansionLevel > VALUE_LIST = Collections.unmodifiableList( Arrays.< IExpansionLevel > asList( ExpansionLevel.VALUES ) );
	
	
	/**
	 * Returns the full (long) text value of the expansion level.
	 * 
	 * @return the full (long) text value of the expansion level
	 */
	String getFullText();
	
	/**
	 * Returns an icon for tables.
	 * 
	 * @param <T> type parameter specifying the requirements for {@link ITableIcon}
	 *            
	 * @return an icon for tables
	 */
	< T extends IExpansionLevel & Comparable< T > > ITableIcon< T > getTableIcon();
	
}
