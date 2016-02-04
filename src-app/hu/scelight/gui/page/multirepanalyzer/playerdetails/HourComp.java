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
import hu.sllauncher.util.Int;

import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Hour component.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class HourComp extends GeneralTableStatsComp< Integer, Int > {
	
	/**
	 * Creates a new {@link HourComp}.
	 * 
	 * @param displayName display name to be used when replays of stats rows are opened
	 * @param playerStats player stats to show hour stats for
	 */
	public HourComp( final String displayName, final PlayerStats playerStats ) {
		super( displayName );
		
		buildGui( playerStats, "<html>Hour<br>of day</html>", Int.class );
	}
	
	@Override
	protected Collection< GeneralStats< Int > > calculateStats( final PlayerStats playerStats ) {
		final Calendar cal = Calendar.getInstance();
		
		// Calculate stats
		final Map< Integer, GeneralStats< Int > > generalStatsMap = new HashMap<>();
		for ( final Game g : playerStats.gameList )
			for ( final Part refPart : g.parts ) { // Have to go through all participants as the zero-toon might be present multiple times
				if ( !playerStats.obj.equals( refPart.toon ) )
					continue;
				
				cal.setTime( g.date );
				final int hour = cal.get( Calendar.HOUR_OF_DAY );
				GeneralStats< Int > gs = generalStatsMap.get( hour );
				if ( gs == null )
					generalStatsMap.put( hour, gs = new GeneralStats< Int >( new Int( hour ) {
						final String stringValue = hour < 10 ? "0" + hour : Integer.toString( hour );
						
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
