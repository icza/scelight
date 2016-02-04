/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.gui.page.multirepanalyzer.playerdetails.metrictrend;

import hu.scelight.gui.page.multirepanalyzer.model.Game;
import hu.scelight.gui.page.multirepanalyzer.model.GeneralStats;
import hu.scelight.gui.page.multirepanalyzer.model.Part;
import hu.scelight.gui.page.multirepanalyzer.model.PlayerStats;
import hu.scelight.gui.page.multirepanalyzer.playerdetails.GeneralTableStatsComp;
import hu.scelight.service.env.Env;
import hu.scelight.service.settings.Settings;
import hu.sllauncher.util.Int;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * SPM component.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class SpmComp extends GeneralTableStatsComp< Integer, Int > {
	
	/**
	 * Creates a new {@link SpmComp}.
	 * 
	 * @param displayName display name to be used when replays of stats rows are opened
	 * @param playerStats player stats to show SPM stats for
	 */
	public SpmComp( final String displayName, final PlayerStats playerStats ) {
		super( displayName );
		
		buildGui( playerStats, "<html>SPM<br>range</html>", Int.class );
	}
	
	@Override
	protected Collection< GeneralStats< Int > > calculateStats( final PlayerStats playerStats ) {
		final int spmGranularity = Env.APP_SETTINGS.get( Settings.MULTI_REP_SPM_GRANULARITY );
		
		// Calculate stats
		final Map< Integer, GeneralStats< Int > > generalStatsMap = new HashMap<>();
		for ( final Game g : playerStats.gameList )
			for ( final Part refPart : g.parts ) { // Have to go through all participants as the zero-toon might be present multiple times
				if ( !playerStats.obj.equals( refPart.toon ) )
					continue;
				
				final int spm = ( (int) refPart.getSpm() / spmGranularity ) * spmGranularity;
				GeneralStats< Int > gs = generalStatsMap.get( spm );
				if ( gs == null )
					generalStatsMap.put( spm, gs = new GeneralStats< Int >( new Int( spm ) {
						final String stringValue = spm + "-" + ( spm + spmGranularity );
						
						@Override
						public String toString() {
							return stringValue;
						};
					} ) );
				gs.updateWithPartGame( refPart, g );
			}
		
		return generalStatsMap.values();
	}
	
}
