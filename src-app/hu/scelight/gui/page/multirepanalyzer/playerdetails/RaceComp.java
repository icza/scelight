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
import hu.scelight.sc2.rep.model.details.Race;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;

/**
 * Race component.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class RaceComp extends GeneralTableStatsComp< Race, Race > {
	
	/**
	 * Creates a new {@link RaceComp}.
	 * 
	 * @param displayName display name to be used when replays of stats rows are opened
	 * @param playerStats player stats to show race stats for
	 */
	public RaceComp( final String displayName, final PlayerStats playerStats ) {
		super( displayName );
		
		buildGui( playerStats, "<html>Player<br>Race</html>", Race.class );
	}
	
	@Override
	protected Collection< GeneralStats< Race > > calculateStats( final PlayerStats playerStats ) {
		// Calculate stats
		final Map< Race, GeneralStats< Race > > generalStatsMap = new EnumMap<>( Race.class );
		for ( final Game g : playerStats.gameList )
			for ( final Part refPart : g.parts ) { // Have to go through all participants as the zero-toon might be present multiple times
				if ( !playerStats.obj.equals( refPart.toon ) )
					continue;
				
				GeneralStats< Race > gs = generalStatsMap.get( refPart.race );
				if ( gs == null )
					generalStatsMap.put( refPart.race, gs = new GeneralStats<>( refPart.race ) );
				gs.updateWithPartGame( refPart, g );
			}
		
		return generalStatsMap.values();
	}
	
}
