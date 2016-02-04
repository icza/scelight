/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.sc2.rep.model.initdata.userinitdata;

import hu.scelight.gui.icon.Icons;
import hu.scelight.util.gui.TableIcon;
import hu.scelightapi.sc2.rep.model.initdata.userinitdata.ILeague;
import hu.scelightapi.util.gui.ITableIcon;
import hu.sllauncher.gui.icon.LRIcon;

import java.util.Comparator;

/**
 * Battle.net league.
 * 
 * @author Andras Belicza
 */
public enum League implements ILeague {
	
	/** Unknown. */
	UNKNOWN( "Unknown", Icons.MY_EMPTY ),
	
	/** Bronze. */
	BRONZE( "Bronze", Icons.SC2_BRONZE ),
	
	/** Silver. */
	SILVER( "Silver", Icons.SC2_SILVER ),
	
	/** Gold. */
	GOLD( "Gold", Icons.SC2_GOLD ),
	
	/** Platinum. */
	PLATINUM( "Platinum", Icons.SC2_PLATINUM ),
	
	/** Diamond. */
	DIAMOND( "Diamond", Icons.SC2_DIAMOND ),
	
	/** Master. */
	MASTER( "Master", Icons.SC2_MASTER ),
	
	/** Grandmaster. */
	GRANDMASTER( "Grandmaster", Icons.SC2_GRANDMASTER ),
	
	/** Unranked. */
	UNRANKED( "Unranked", Icons.SC2_UNRANKED );
	
	
	/** Text value of the league. */
	public final String              text;
	
	/** Ricon of the league. */
	public final LRIcon              ricon;
	
	/**
	 * League letter (first character of the English name except <code>'R'</code> for {@link #GRANDMASTER} and <code>'-'</code> for {@link #UNKNOWN}).
	 */
	public final char                letter;
	
	/** Icon for tables. */
	public final TableIcon< League > tableIcon;
	
	
	/**
	 * Creates a new {@link League}.
	 * 
	 * @param text text value
	 * @param ricon ricon of the league
	 */
	private League( final String text, final LRIcon ricon ) {
		this.text = text;
		this.ricon = ricon;
		letter = "UNKNOWN".equals( name() ) ? '-' : Character.toUpperCase( "GRANDMASTER".equals( name() ) ? 'R' : text.charAt( 0 ) );
		
		tableIcon = new TableIcon<>( this );
	}
	
	@Override
	public String toString() {
		return text;
	}
	
	@Override
	public char getLetter() {
		return letter;
	}
	
	@Override
	public LRIcon getRicon() {
		return ricon;
	}
	
	@Override
	@SuppressWarnings( "unchecked" )
	public TableIcon< League > getTableIcon() {
		return tableIcon;
	}
	
	
	/** Ricon representing this entity. */
	public static final LRIcon                                        RICON                  = Icons.MY_LEAGUES;
	
	/** Cache of the values array. */
	public static final League[]                                      VALUES                 = values();
	
	/** A comparator which returns a more meaningful league order. */
	public static final Comparator< ILeague >                         COMPARATOR             = new Comparator< ILeague >() {
		                                                                                         @Override
		                                                                                         public int compare( final ILeague l1, final ILeague l2 ) {
			                                                                                         if ( l1 == l2 )
				                                                                                         return 0;
			                                                                                         
			                                                                                         // Make unknown the least
			                                                                                         if ( l1 == UNKNOWN )
				                                                                                         return -1;
			                                                                                         if ( l2 == UNKNOWN )
				                                                                                         return 1;
			                                                                                         
			                                                                                         // Then comes unranked
			                                                                                         if ( l1 == UNRANKED )
				                                                                                         return -1;
			                                                                                         if ( l2 == UNRANKED )
				                                                                                         return 1;
			                                                                                         
			                                                                                         // Rest is good in their natural order
			                                                                                         return l1.ordinal() - l2.ordinal();
		                                                                                         }
	                                                                                         };
	
	/** A comparator of {@link TableIcon}s wrapping a {@link League}, and using the order defined by {@link #COMPARATOR}. */
	public static final Comparator< TableIcon< League > >             TABLE_ICON_COMPARATOR  = new Comparator< TableIcon< League > >() {
		                                                                                         @Override
		                                                                                         public int compare( final TableIcon< League > i1,
		                                                                                                 final TableIcon< League > i2 ) {
			                                                                                         return COMPARATOR.compare( i1.represented, i2.represented );
		                                                                                         }
	                                                                                         };
	
	/** A comparator of {@link ITableIcon}s wrapping an {@link ILeague}, and using the order defined by {@link #COMPARATOR}. */
	public static final Comparator< ITableIcon< ? extends ILeague > > ITABLE_ICON_COMPARATOR = new Comparator< ITableIcon< ? extends ILeague > >() {
		                                                                                         @Override
		                                                                                         public int compare( final ITableIcon< ? extends ILeague > i1,
		                                                                                                 final ITableIcon< ? extends ILeague > i2 ) {
			                                                                                         return COMPARATOR.compare( i1.getRepresented(),
			                                                                                                 i2.getRepresented() );
		                                                                                         }
	                                                                                         };
	
}
