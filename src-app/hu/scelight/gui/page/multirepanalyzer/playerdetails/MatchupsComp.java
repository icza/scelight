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

import hu.scelight.gui.comp.XTabbedPane;
import hu.scelight.gui.icon.Icons;
import hu.scelight.gui.page.multirepanalyzer.model.Game;
import hu.scelight.gui.page.multirepanalyzer.model.GeneralStats;
import hu.scelight.gui.page.multirepanalyzer.model.Part;
import hu.scelight.gui.page.multirepanalyzer.model.PlayerStats;
import hu.scelight.sc2.rep.repproc.Format;
import hu.scelightapibase.util.iface.Producer;
import hu.sllauncher.gui.comp.BorderPanel;

import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;

/**
 * Matchup details component (either race or league matchups).
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class MatchupsComp extends BorderPanel {
	
	/**
	 * Creates a new {@link MatchupsComp}.
	 * 
	 * @param displayName display name to be used when replays of stats rows are opened
	 * @param playerStats player stats to show matchup details for
	 * @param leagueMatchups tells if league matchups are to be shown; else race league matchups
	 */
	public MatchupsComp( final String displayName, final PlayerStats playerStats, final boolean leagueMatchups ) {
		// Calculate stats
		// Gather / calculate matchup stats separated by format
		final Map< Format, Map< String, GeneralStats< String > > > formatGeneralStatsMapMap = new EnumMap<>( Format.class );
		
		final StringBuilder sb = new StringBuilder();
		for ( final Game g : playerStats.gameList ) {
			if ( leagueMatchups && !g.hasLeagues )
				continue;
			
			for ( final Part refPart : g.parts ) { // Have to go through all participants as the zero-toon might be present multiple times
				if ( !playerStats.obj.equals( refPart.toon ) )
					continue;
				
				// Matchup stats by format
				Map< String, GeneralStats< String > > generalStatsMap = formatGeneralStatsMapMap.get( g.format );
				if ( generalStatsMap == null )
					formatGeneralStatsMapMap.put( g.format, generalStatsMap = new HashMap<>() );
				
				final String teamSeparator = g.format == Format.ONE_VS_ONE || g.format == Format.FFA ? "v" : " vs ";
				final String[] teamLetterss = leagueMatchups ? g.getTeamLeaguess( refPart ) : g.getTeamRacess( refPart );
				
				if ( g.format == Format.FFA || g.format == Format.CUSTOM ) {
					// 2 variations: normal matchup, "home" team vs *
					for ( int variation = 0; variation < 2; variation++ ) {
						sb.setLength( 0 );
						final String matchup;
						if ( variation == 0 ) {
							// Normal matchup
							for ( final String teamLetters : teamLetterss ) {
								if ( sb.length() > 0 )
									sb.append( teamSeparator );
								sb.append( teamLetters );
							}
							matchup = sb.toString();
						} else if ( variation == 1 ) {
							// "Home" team vs *
							matchup = teamLetterss[ 0 ] + teamSeparator + '*';
						} else {
							throw new RuntimeException( "Fix the variation cycle!" );
						}
						
						GeneralStats< String > gs = generalStatsMap.get( matchup );
						if ( gs == null )
							generalStatsMap.put( matchup, gs = new GeneralStats<>( matchup ) );
						gs.updateWithPartGame( refPart, g );
					}
				} else {
					// 3 variations: normal match-up, "home" team vs *, * vs "away" team
					// 4th : player letter + * vs * (which is for example: P+* vs *, T+* vs *, Z+* vs *)
					final int cyclesCount = g.format == Format.ONE_VS_ONE ? 3 : 4;
					for ( int i = 0; i < cyclesCount; i++ ) {
						sb.setLength( 0 );
						final String matchup;
						switch ( i ) {
							case 0 : // Normal matchup
								matchup = teamLetterss[ 0 ] + teamSeparator + teamLetterss[ 1 ];
								break;
							case 1 : // "home" team vs *
								matchup = teamLetterss[ 0 ] + teamSeparator + '*';
								break;
							case 2 : // * vs "away" team
								matchup = '*' + teamSeparator + teamLetterss[ 1 ];
								break;
							case 3 : // player letter + * vs "away" team
								if ( teamLetterss[ 0 ].length() < 3 || teamLetterss[ 0 ].charAt( 1 ) != '+' )
									continue;
								matchup = teamLetterss[ 0 ].charAt( 0 ) + "+*" + teamSeparator + '*';
								break;
							default :
								throw new RuntimeException( "Fix the variation cycle!" );
						}
						
						GeneralStats< String > gs = generalStatsMap.get( matchup );
						if ( gs == null )
							generalStatsMap.put( matchup, gs = new GeneralStats<>( matchup ) );
						gs.updateWithPartGame( refPart, g );
					}
				}
			}
		}
		
		
		// Now build GUI
		final XTabbedPane tp = new XTabbedPane();
		
		// Add Sum tab
		tp.addTab( "Sum", Icons.F_SUM, leagueMatchups ? new LeagueComp( displayName + " \u00d7 Sum", playerStats ) : new RaceComp( displayName + " \u00d7 Sum",
		        playerStats ), false );
		
		// Add Format sub-tabs in order
		for ( final Format format : Format.VALUES ) {
			final Map< String, GeneralStats< String > > generalStatsMap = formatGeneralStatsMapMap.get( format );
			if ( generalStatsMap == null )
				continue;
			
			// Non-first tab
			tp.addTab( format.text, null, new Producer< JComponent >() {
				@Override
				public JComponent produce() {
					return new GeneralTableStatsComp< String, String >( displayName + " \u00d7 " + format ) {
						{
							buildGui( playerStats, leagueMatchups ? "<html>League<br>Matchup</html>" : "<html>Race<br>Matchup</html>", String.class );
						}
						
						@Override
						protected Collection< GeneralStats< String > > calculateStats( final PlayerStats playerStats ) {
							return generalStatsMap.values();
						}
					};
				}
			}, false );
		}
		
		addCenter( tp );
	}
	
}
