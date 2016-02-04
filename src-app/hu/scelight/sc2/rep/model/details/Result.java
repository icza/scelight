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
import hu.scelight.util.gui.TableIcon;
import hu.scelightapi.sc2.rep.model.details.IResult;
import hu.sllauncher.gui.icon.LRIcon;

/**
 * Match result of a player.
 * 
 * @author Andras Belicza
 */
public enum Result implements IResult {
	
	/** Unknown. */
	UNKNOWN( "Unknown", Icons.MY_EMPTY ),
	
	/** Victory. */
	VICTORY( "Victory", Icons.SC2_VICTORY ),
	
	/** Defeat. */
	DEFEAT( "Defeat", Icons.SC2_DEFEAT ),
	
	/** Tie. */
	TIE( "Tie", Icons.SC2_TIE );
	
	
	/** Text value of the result. */
	public final String              text;
	
	/** Ricon of the result. */
	public final LRIcon              ricon;
	
	/** Result letter (first character of the English name). */
	public final char                letter;
	
	/** Icon for tables. */
	public final TableIcon< Result > tableIcon;
	
	
	/**
	 * Creates a new {@link Result}.
	 * 
	 * @param text text value
	 * @param ricon ricon of the league
	 */
	private Result( final String text, final LRIcon ricon ) {
		this.text = text;
		this.ricon = ricon;
		letter = "UNKNOWN".equals( name() ) ? '-' : Character.toUpperCase( text.charAt( 0 ) );
		
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
	public TableIcon< Result > getTableIcon() {
		return tableIcon;
	}
	
	
	/** Ricon representing this entity. */
	public static final LRIcon   RICON  = Icons.MY_RESULTS;
	
	/** Cache of the values array. */
	public static final Result[] VALUES = values();
	
}
