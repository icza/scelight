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
import hu.sllauncher.gui.icon.LRIcon;

/**
 * Describes / models a Map. Contains aggregate stats.
 * 
 * @author Andras Belicza
 */
public class MapStats extends Stats {
	
	/** Ricon of the map (for visualizing in tables). */
	public final LRIcon  ricon;
	
	/** Map size. */
	public final MapSize mapSize;
	
	/** Protoss record on this map. Mirror matchup (PvP) is excluded. */
	public final Record  precord = new Record();
	
	/** Terran record on this map. Mirror matchup (TvT) is excluded. */
	public final Record  trecord = new Record();
	
	/** Zerg record on this map. Mirror matchup (ZvZ) is excluded. */
	public final Record  zrecord = new Record();
	
	
	/**
	 * Creates a new {@link MapStats}.
	 * 
	 * @param game game to init from
	 */
	public MapStats( final Game game ) {
		ricon = game.ricon;
		mapSize = game.mapSize;
	}
	
	
	/**
	 * Updates the map stats with the specified {@link Game}.
	 * 
	 * @param game game to be included
	 */
	public void updateWithGame( final Game game ) {
		// Time played on the map:
		updateWithGame( game, game.lengthMs );
		
		if ( game.format == Format.ONE_VS_ONE && game.parts[ 0 ].race != game.parts[ 1 ].race ) { // Exclude mirror matchups
			for ( final Part part : game.parts )
				switch ( part.race ) {
					case PROTOSS :
						precord.updateWithResult( part.result );
						break;
					case TERRAN :
						trecord.updateWithResult( part.result );
						break;
					case ZERG :
						zrecord.updateWithResult( part.result );
						break;
					default :
						break;
				}
		}
	}
	
}
