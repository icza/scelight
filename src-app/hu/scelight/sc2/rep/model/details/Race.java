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

import hu.scelight.gui.icon.Icons;
import hu.scelight.util.Utils;
import hu.scelightapi.sc2.rep.model.details.IRace;
import hu.sllauncher.gui.icon.LRIcon;

import java.awt.Color;
import java.util.Set;

/**
 * SC2 Race.
 * 
 * @author Andras Belicza
 */
public enum Race implements IRace {
	
	/**
	 * Terran.<br>
	 * English, German, Portuguese, Korean, Chinese, Russian, Polish, Mandarin (Chinese)
	 */
	TERRAN( "Terran", Icons.SC2_TERRAN, new Color( 200, 255, 200 ), new Color( 117, 160, 117 ), Utils.asNewSet( "Terran", "Terraner", "Terrano", "테란", "人類",
	        "Терран", "Terrani", "人类" ) ),
	
	/**
	 * Zerg.<br>
	 * English, Korean, Chinese, Russian, Polish, Mandarin (Chinese)
	 */
	ZERG( "Zerg", Icons.SC2_ZERG, new Color( 255, 200, 200 ), new Color( 180, 125, 125 ), Utils.asNewSet( "Zerg", "저그", "蟲族", "Зерг", "Zergi", "异虫" ) ),
	
	/**
	 * Protoss.<br>
	 * English, Korean, Chinese, Russian, Polish, Mandarin (Chinese)
	 */
	PROTOSS( "Protoss", Icons.SC2_PROTOSS, new Color( 200, 200, 255 ), new Color( 125, 125, 180 ), Utils.asNewSet( "Protoss", "프로토스", "神族", "Протосс",
	        "Protosi", "星灵" ) ),
	
	/** Random. */
	RANDOM( "Random", Icons.MY_EMPTY, null, null, null ),
	
	/** Unknown. */
	UNKNOWN( "Unknown", Icons.MY_EMPTY, null, null, null );
	
	
	
	/** Text value of the race. */
	public final String         text;
	
	/** Race letter (first character of the English name). */
	public final char           letter;
	
	/** Ricon of the race. */
	public final LRIcon         ricon;
	
	/** Color representing this race. */
	public final Color          color;
	
	/** Brighter color representing this race. */
	public final Color          darkerColor;
	
	/** Localized names of the race. */
	private final Set< String > localizedNameSet;
	
	/**
	 * Creates a new {@link Race}.
	 * 
	 * @param text text value
	 * @param ricon ricon of the race
	 * @param color color representing this race
	 * @param darkerColor darker color representing this race
	 * @param localizedNameSet localized name set
	 */
	private Race( final String text, final LRIcon ricon, final Color color, final Color darkerColor, final Set< String > localizedNameSet ) {
		this.text = text;
		letter = "UNKNOWN".equals( name() ) ? '-' : Character.toUpperCase( text.charAt( 0 ) );
		this.ricon = ricon;
		this.color = color;
		this.darkerColor = darkerColor;
		this.localizedNameSet = localizedNameSet;
	}
	
	/**
	 * Returns the race specified by its localized name.
	 * 
	 * @param localizedName the localized name of the race
	 * @return the race specified by its localized name; or {@link #UNKNOWN} if the localized value was not recognized
	 */
	public static Race fromLocalizedValue( final String localizedName ) {
		// Protoss and Zerg are the most common name in localized names, so first try those; "Zerg" is shorter, so start with that
		if ( ZERG.localizedNameSet.contains( localizedName ) )
			return ZERG;
		if ( PROTOSS.localizedNameSet.contains( localizedName ) )
			return PROTOSS;
		if ( TERRAN.localizedNameSet.contains( localizedName ) )
			return TERRAN;
		
		// Could not find the localized value, let's try to find out
		if ( localizedName.startsWith( "Pr" ) )
			return PROTOSS;
		else if ( localizedName.startsWith( "Te" ) )
			return TERRAN;
		else if ( localizedName.startsWith( "Ze" ) )
			return ZERG;
		
		return UNKNOWN;
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
	public Color getColor() {
		return color;
	}
	
	@Override
	public Color getDarkerColor() {
		return darkerColor;
	}
	
	
	/** Ricon representing this entity. */
	public static final LRIcon RICON  = Icons.MY_RACES;
	
	/** Cache of the values array. */
	public static final Race[] VALUES = values();
	
}
