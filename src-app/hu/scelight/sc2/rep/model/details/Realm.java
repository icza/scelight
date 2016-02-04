/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.sc2.rep.model.details;

import hu.scelight.sc2.rep.model.initdata.gamedesc.Region;
import hu.scelightapi.sc2.rep.model.details.IRealm;

/**
 * SC2 realm (sub-region).
 * 
 * @author Andras Belicza
 * 
 * @see Region
 */
public enum Realm implements IRealm {
	
	/** North America. */
	NORTH_AMERICA( "North America" ),
	
	/** Latin America. */
	LATIN_AMERICA( "Latin America" ),
	
	/** China. */
	CHINA( "China" ),
	
	/** Europe. */
	EUROPE( "Europe" ),
	
	/** Russia. */
	RUSSIA( "Russia" ),
	
	/** Korea. */
	KOREA( "Korea" ),
	
	/** Taiwan. */
	TAIWAN( "Taiwan" ),
	
	/** South-East Asia. */
	SEA( "SEA" ),
	
	/** Unknown. */
	UNKNOWN( "Unknown" );
	
	
	/** Text value of the realm. */
	public final String text;
	
	
	/**
	 * Creates a new {@link Realm}.
	 * 
	 * @param text text value
	 */
	private Realm( final String text ) {
		this.text = text;
	}
	
	@Override
	public String toString() {
		return text;
	}
	
	
	/** Cache of the values array. */
	public static final Realm[] VALUES = values();
	
}
