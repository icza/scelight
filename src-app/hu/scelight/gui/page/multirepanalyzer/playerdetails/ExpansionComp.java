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
import hu.scelight.sc2.rep.model.initdata.gamedesc.ExpansionLevel;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;

/**
 * Expansion component.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class ExpansionComp extends GeneralTableStatsComp< ExpansionLevel, ExpansionLevel > {
	
	/**
	 * Creates a new {@link ExpansionComp}.
	 * 
	 * @param displayName display name to be used when replays of stats rows are opened
	 * @param playerStats player stats to show expansion stats for
	 */
	public ExpansionComp( final String displayName, final PlayerStats playerStats ) {
		super( displayName );
		
		buildGui( playerStats, "<html>Expansion<br>Level</html>", ExpansionLevel.class );
	}
	
	@Override
	protected Collection< GeneralStats< ExpansionLevel > > calculateStats( final PlayerStats playerStats ) {
		// Calculate stats
		final Map< ExpansionLevel, GeneralStats< ExpansionLevel > > generalStatsMap = new EnumMap<>( ExpansionLevel.class );
		for ( final Game g : playerStats.gameList )
			for ( final Part refPart : g.parts ) { // Have to go through all participants as the zero-toon might be present multiple times
				if ( !playerStats.obj.equals( refPart.toon ) )
					continue;
				
				GeneralStats< ExpansionLevel > gs = generalStatsMap.get( g.expansion );
				if ( gs == null )
					generalStatsMap.put( g.expansion, gs = new GeneralStats<>( g.expansion ) );
				gs.updateWithPartGame( refPart, g );
			}
		
		return generalStatsMap.values();
	}
	
}
