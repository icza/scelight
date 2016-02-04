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
import hu.scelight.sc2.rep.model.details.Result;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;

/**
 * Result component.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class ResultComp extends GeneralTableStatsComp< Result, Result > {
	
	/**
	 * Creates a new {@link ResultComp}.
	 * 
	 * @param displayName display name to be used when replays of stats rows are opened
	 * @param playerStats player stats to show result stats for
	 */
	public ResultComp( final String displayName, final PlayerStats playerStats ) {
		super( displayName );
		
		buildGui( playerStats, "<html>Player<br>Result</html>", Result.class );
	}
	
	@Override
	protected Collection< GeneralStats< Result > > calculateStats( final PlayerStats playerStats ) {
		// Calculate stats
		final Map< Result, GeneralStats< Result > > generalStatsMap = new EnumMap<>( Result.class );
		for ( final Game g : playerStats.gameList )
			for ( final Part refPart : g.parts ) { // Have to go through all participants as the zero-toon might be present multiple times
				if ( !playerStats.obj.equals( refPart.toon ) )
					continue;
				
				GeneralStats< Result > gs = generalStatsMap.get( refPart.result );
				if ( gs == null )
					generalStatsMap.put( refPart.result, gs = new GeneralStats<>( refPart.result ) );
				gs.updateWithPartGame( refPart, g );
			}
		
		return generalStatsMap.values();
	}
	
}
