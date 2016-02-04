/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.gui.page.multirepanalyzer.model;

import hu.scelight.sc2.rep.model.details.Toon;
import hu.scelight.util.Utils;
import hu.scelightapi.sc2.rep.model.details.IToon;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Describes / models stats of a Player. Contains aggregate stats.
 * 
 * @author Andras Belicza
 */
public class PlayerStats extends GeneralStats< IToon > {
	
	/** Full name of the player. */
	public String            name;
	
	/** Date when the current name comes from. */
	public Date              nameDate;
	
	
	/**
	 * Set of all names. Normally these are the name changes (and clan changes), but in case of the zero-toon, these are the different computer names and names
	 * used offline.<br>
	 * Initialized lazily.
	 */
	public Set< String >     allNameSet;
	
	
	/** Merged toons. */
	public final Set< Toon > mergedToonSet;
	
	/**
	 * Name with a date (when the name comes from).
	 * 
	 * @author Andras Belicza
	 */
	public static class NameWithDate {
		/** The name. */
		public String name;
		
		/** The date when the name comes from. */
		public Date   date;
		
		/**
		 * Creates a new {@link NameWithDate}.
		 * 
		 * @param name initial name
		 * @param date initial date
		 */
		public NameWithDate( final String name, final Date date ) {
			this.name = name;
			this.date = date;
		}
	}
	
	/** Latest / newest names found for the merged toons (date is the date when the name comes from). */
	public final Map< Toon, NameWithDate > mergedToonNameMap;
	
	
	/**
	 * Creates a new {@link PlayerStats}.
	 * 
	 * @param part participant to init from
	 */
	public PlayerStats( final Part part ) {
		super( part.toon );
		
		mergedToonSet = part.merged ? new HashSet< Toon >() : null;
		mergedToonNameMap = part.merged ? new HashMap< Toon, NameWithDate >() : null;
		
		if ( part.toon.isZero() ) {
			name = "COMPUTER / OFFLINE";
			if ( mergedToonNameMap != null )
				mergedToonNameMap.put( part.toon, new NameWithDate( name, null ) );
		}
	}
	
	/**
	 * Updates the player name with the specified {@link Part}.
	 * 
	 * @param part participant
	 * @param game game
	 */
	public void updateName( final Part part, final Game game ) {
		// Update name
		if ( obj.isZero() ) {
			// Zero-toon, store all names
			if ( allNameSet == null )
				allNameSet = new HashSet<>();
			allNameSet.add( part.name );
		} else {
			if ( name != null && !name.equals( part.name ) ) {
				if ( allNameSet == null )
					allNameSet = new HashSet<>();
				allNameSet.add( name );
				allNameSet.add( part.name );
			}
			
			// Use the latest name
			if ( nameDate == null || nameDate.before( game.date ) ) {
				name = part.name;
				nameDate = game.date;
			}
		}
		
		if ( mergedToonSet != null )
			mergedToonSet.add( part.originalToon );
		
		if ( mergedToonNameMap != null ) {
			// Use the latest name
			final NameWithDate nameWithDate = mergedToonNameMap.get( part.originalToon );
			if ( nameWithDate == null )
				mergedToonNameMap.put( part.originalToon, new NameWithDate( part.name, game.date ) );
			else if ( nameWithDate.date.before( game.date ) ) {
				nameWithDate.name = part.name;
				nameWithDate.date = game.date;
			}
		}
	}
	
	/**
	 * Returns the name to be displayed in the table.
	 * 
	 * @return the name to be displayed in the table
	 */
	public String getName() {
		if ( allNameSet == null )
			return mergedToonSet == null ? name : name + " (Merged:" + mergedToonSet.size() + ")";
		
		return name + "  (+" + ( obj.isZero() ? allNameSet.size() : allNameSet.size() - 1 )
		        + ( mergedToonSet == null ? ")" : ") (Merged:" + +mergedToonSet.size() + ")" );
	}
	
	/**
	 * Returns a comma separated list of all names.
	 * 
	 * @return a comma separated list of all names
	 */
	public String getAllNames() {
		return allNameSet == null ? null : Utils.concatenate( allNameSet );
	}
	
}
