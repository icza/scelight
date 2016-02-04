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
import hu.scelight.sc2.rep.model.initdata.userinitdata.League;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;

/**
 * League component.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class LeagueComp extends GeneralTableStatsComp< League, League > {
	
	/**
	 * Creates a new {@link LeagueComp}.
	 * 
	 * @param displayName display name to be used when replays of stats rows are opened
	 * @param playerStats player stats to show league stats for
	 */
	public LeagueComp( final String displayName, final PlayerStats playerStats ) {
		super( displayName );
		
		buildGui( playerStats, "<html>Player<br>League</html>", League.class );
	}
	
	@Override
	protected Collection< GeneralStats< League > > calculateStats( final PlayerStats playerStats ) {
		// Calculate stats
		final Map< League, GeneralStats< League > > generalStatsMap = new EnumMap<>( League.class );
		for ( final Game g : playerStats.gameList ) {
			if ( !g.hasLeagues )
				continue;
			
			for ( final Part refPart : g.parts ) { // Have to go through all participants as the zero-toon might be present multiple times
				if ( refPart.league == null )
					continue;
				if ( !playerStats.obj.equals( refPart.toon ) )
					continue;
				
				GeneralStats< League > gs = generalStatsMap.get( refPart.league );
				if ( gs == null )
					generalStatsMap.put( refPart.league, gs = new GeneralStats<>( refPart.league ) );
				gs.updateWithPartGame( refPart, g );
			}
		}
		
		return generalStatsMap.values();
	}
	
}
