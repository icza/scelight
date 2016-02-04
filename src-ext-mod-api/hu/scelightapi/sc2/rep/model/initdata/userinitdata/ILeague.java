/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapi.sc2.rep.model.initdata.userinitdata;

import hu.scelight.sc2.rep.model.initdata.userinitdata.League;
import hu.scelightapi.util.gui.ITableIcon;
import hu.scelightapibase.util.IEnum;
import hu.scelightapibase.util.gui.HasRIcon;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Battle.net league.
 * 
 * @author Andras Belicza
 * 
 * @see IEnum
 */
public interface ILeague extends HasRIcon, IEnum {
	
	/** Unknown. */
	ILeague                                       UNKNOWN                = League.UNKNOWN;
	
	/** Bronze. */
	ILeague                                       BRONZE                 = League.BRONZE;
	
	/** Silver. */
	ILeague                                       SILVER                 = League.SILVER;
	
	/** Gold. */
	ILeague                                       GOLD                   = League.GOLD;
	
	/** Platinum. */
	ILeague                                       PLATINUM               = League.PLATINUM;
	
	/** Diamond. */
	ILeague                                       DIAMOND                = League.DIAMOND;
	
	/** Master. */
	ILeague                                       MASTER                 = League.MASTER;
	
	/** Grandmaster. */
	ILeague                                       GRANDMASTER            = League.GRANDMASTER;
	
	/** Unranked. */
	ILeague                                       UNRANKED               = League.UNRANKED;
	
	
	/** An unmodifiable list of all the leagues. */
	List< ILeague >                               VALUE_LIST             = Collections.unmodifiableList( Arrays.< ILeague > asList( League.VALUES ) );
	
	/** A comparator which returns a more meaningful league order than the natural order (defined by {@link IEnum#ordinal()}). */
	Comparator< ILeague >                         COMPARATOR             = League.COMPARATOR;
	
	/** A comparator of {@link ITableIcon}s wrapping an {@link ILeague}, and using the order defined by {@link #COMPARATOR}. */
	Comparator< ITableIcon< ? extends ILeague > > ITABLE_ICON_COMPARATOR = League.ITABLE_ICON_COMPARATOR;
	
	/**
	 * Returns the league letter (first character of the English name except <code>'R'</code> for {@link #GRANDMASTER} and <code>'-'</code> for {@link #UNKNOWN}
	 * ).
	 * 
	 * @return the league letter (first character of the English name except <code>'R'</code> for {@link #GRANDMASTER} and <code>'-'</code> for {@link #UNKNOWN}
	 *         )
	 */
	char getLetter();
	
	/**
	 * Returns an icon for tables.
	 * 
	 * @param <T> type parameter specifying the requirements for {@link ITableIcon}
	 * 
	 * @return an icon for tables
	 */
	< T extends ILeague & Comparable< T > > ITableIcon< T > getTableIcon();
	
}
