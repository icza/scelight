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
import hu.scelight.sc2.rep.repproc.Format;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;

/**
 * Format component.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class FormatComp extends GeneralTableStatsComp< Format, Format > {
	
	/**
	 * Creates a new {@link FormatComp}.
	 * 
	 * @param displayName display name to be used when replays of stats rows are opened
	 * @param playerStats player stats to show game format stats for
	 */
	public FormatComp( final String displayName, final PlayerStats playerStats ) {
		super( displayName );
		
		buildGui( playerStats, "<html>Game<br>Format</html>", Format.class );
	}
	
	@Override
	protected Collection< GeneralStats< Format > > calculateStats( final PlayerStats playerStats ) {
		// Calculate stats
		final Map< Format, GeneralStats< Format > > generalStatsMap = new EnumMap<>( Format.class );
		for ( final Game g : playerStats.gameList )
			for ( final Part refPart : g.parts ) { // Have to go through all participants as the zero-toon might be present multiple times
				if ( !playerStats.obj.equals( refPart.toon ) )
					continue;
				
				GeneralStats< Format > gs = generalStatsMap.get( g.format );
				if ( gs == null )
					generalStatsMap.put( g.format, gs = new GeneralStats<>( g.format ) );
				gs.updateWithPartGame( refPart, g );
			}
		
		return generalStatsMap.values();
	}
	
}
