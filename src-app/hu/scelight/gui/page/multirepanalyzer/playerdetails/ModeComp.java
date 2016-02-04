/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.gui.page.multirepanalyzer.playerdetails;

import hu.scelight.gui.page.multirepanalyzer.model.Game;
import hu.scelight.gui.page.multirepanalyzer.model.GeneralStats;
import hu.scelight.gui.page.multirepanalyzer.model.Part;
import hu.scelight.gui.page.multirepanalyzer.model.PlayerStats;
import hu.scelight.sc2.rep.model.attributesevents.GameMode;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;

/**
 * Mode component.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class ModeComp extends GeneralTableStatsComp< GameMode, GameMode > {
	
	/**
	 * Creates a new {@link ModeComp}.
	 * 
	 * @param displayName display name to be used when replays of stats rows are opened
	 * @param playerStats player stats to show game mode stats for
	 */
	public ModeComp( final String displayName, final PlayerStats playerStats ) {
		super( displayName );
		
		buildGui( playerStats, "<html>Game<br>Mode</html>", GameMode.class );
	}
	
	@Override
	protected Collection< GeneralStats< GameMode > > calculateStats( final PlayerStats playerStats ) {
		// Calculate stats
		final Map< GameMode, GeneralStats< GameMode > > generalStatsMap = new EnumMap<>( GameMode.class );
		for ( final Game g : playerStats.gameList )
			for ( final Part refPart : g.parts ) { // Have to go through all participants as the zero-toon might be present multiple times
				if ( !playerStats.obj.equals( refPart.toon ) )
					continue;
				
				GeneralStats< GameMode > gs = generalStatsMap.get( g.gameMode );
				if ( gs == null )
					generalStatsMap.put( g.gameMode, gs = new GeneralStats<>( g.gameMode ) );
				gs.updateWithPartGame( refPart, g );
			}
		
		return generalStatsMap.values();
	}
	
}
