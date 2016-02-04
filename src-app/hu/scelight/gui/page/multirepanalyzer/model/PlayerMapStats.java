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

import hu.scelight.sc2.rep.repproc.Format;

/**
 * Describes / models a Map. Contains aggregate stats.
 * 
 * @author Andras Belicza
 */
public class PlayerMapStats extends MapStats {
	
	/** Protoss record on this map. Mirror matchup (PvP) is excluded. */
	public final Record record = new Record();
	
	/**
	 * Creates a new {@link PlayerMapStats}.
	 * 
	 * @param game game to init from
	 */
	public PlayerMapStats( final Game game ) {
		super( game );
	}
	
	
	/**
	 * Updates the player stats with the specified {@link Part} and {@link Game}.
	 * 
	 * @param refPart reference participant
	 * @param game game
	 */
	public void updateWithPartGame( final Part refPart, final Game game ) {
		// Different logic, do not call super implementation!
		
		// Time played on the map by the player:
		updateWithGame( game, refPart.lengthMs );
		
		record.updateWithResult( refPart.result );
		
		if ( game.format == Format.ONE_VS_ONE )
			switch ( refPart.race ) {
				case PROTOSS :
					precord.updateWithResult( refPart.result );
					break;
				case TERRAN :
					trecord.updateWithResult( refPart.result );
					break;
				case ZERG :
					zrecord.updateWithResult( refPart.result );
					break;
				default :
					break;
			}
	}
	
}
