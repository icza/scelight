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
import hu.scelight.gui.page.multirepanalyzer.CalendarComp;
import hu.scelight.gui.page.multirepanalyzer.MultiRepAnalyzerComp;
import hu.scelight.gui.page.multirepanalyzer.model.PlayerStats;
import hu.scelight.gui.page.multirepanalyzer.playerdetails.metrictrend.MetricTrendComp;
import hu.scelight.sc2.rep.model.attributesevents.GameMode;
import hu.scelight.sc2.rep.model.details.Race;
import hu.scelight.sc2.rep.model.details.Result;
import hu.scelight.sc2.rep.model.details.Toon;
import hu.scelight.sc2.rep.model.initdata.gamedesc.ExpansionLevel;
import hu.scelight.sc2.rep.model.initdata.gamedesc.Region;
import hu.scelight.sc2.rep.model.initdata.userinitdata.League;
import hu.scelight.sc2.rep.repproc.Format;
import hu.scelightapibase.util.iface.Producer;
import hu.sllauncher.gui.comp.BorderPanel;

import java.util.Map;

import javax.swing.JComponent;

/**
 * Player details component.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class PlayerDetailsComp extends BorderPanel {
	
	/**
	 * Creates a new {@link PlayerDetailsComp}.
	 * 
	 * @param displayName_ display name to be used when replays of stats rows are opened
	 * @param multiRepAnalyzerComp reference to the Multi-replay Analyzer component
	 * @param playerStats player stats to show details for
	 * @param playerStatsMap reference to the map of all player stats; required when the details of a playmate is to be opened
	 */
	public PlayerDetailsComp( final String displayName_, final MultiRepAnalyzerComp multiRepAnalyzerComp, final PlayerStats playerStats,
	        final Map< Toon, PlayerStats > playerStatsMap ) {
		
		final XTabbedPane tp = new XTabbedPane();
		
		final String displayName = displayName_ + " \u00d7 ";
		
		tp.addTab( "Playmate", Icons.F_USERS, new PlaymateComp( displayName + "Playmate", multiRepAnalyzerComp, playerStats, playerStatsMap ), true );
		tp.addTab( "Calendar", Icons.F_CALENDAR_BLUE, new Producer< JComponent >() {
			@Override
			public JComponent produce() {
				return new CalendarComp( displayName + "Calendar", multiRepAnalyzerComp, playerStats );
			}
		}, true );
		tp.addTab( "Map", Icons.F_MAPS_STACK, new Producer< JComponent >() {
			@Override
			public JComponent produce() {
				return new PlayerMapComp( displayName + "Map", playerStats );
			}
		}, true );
		tp.addTab( "Race", Race.RICON, new Producer< JComponent >() {
			@Override
			public JComponent produce() {
				return new MatchupsComp( displayName + "Race", playerStats, false );
			}
		}, true );
		tp.addTab( "League", League.RICON, new Producer< JComponent >() {
			@Override
			public JComponent produce() {
				return new MatchupsComp( displayName + "League", playerStats, true );
			}
		}, true );
		tp.addTab( "Length", Icons.F_CLOCK_SELECT, new Producer< JComponent >() {
			@Override
			public JComponent produce() {
				return new LengthComp( displayName + "Length", playerStats );
			}
		}, true );
		tp.addTab( "Mode", GameMode.RICON, new Producer< JComponent >() {
			@Override
			public JComponent produce() {
				return new ModeComp( displayName + "Mode", playerStats );
			}
		}, true );
		tp.addTab( "Expansion", ExpansionLevel.RICON, new Producer< JComponent >() {
			@Override
			public JComponent produce() {
				return new ExpansionComp( displayName + "Expansion", playerStats );
			}
		}, true );
		tp.addTab( "Format", Format.RICON, new Producer< JComponent >() {
			@Override
			public JComponent produce() {
				return new FormatComp( displayName + "Format", playerStats );
			}
		}, true );
		tp.addTab( "Result", Result.RICON, new Producer< JComponent >() {
			@Override
			public JComponent produce() {
				return new ResultComp( displayName + "Result", playerStats );
			}
		}, true );
		tp.addTab( "Time trend", Icons.F_CALENDAR_SELECT_WEEK, new Producer< JComponent >() {
			@Override
			public JComponent produce() {
				return new TimeTrendComp( displayName + "Time trend", multiRepAnalyzerComp, playerStats );
			}
		}, true );
		tp.addTab( "Timelapse", Icons.F_CALENDAR_RELATION, new Producer< JComponent >() {
			@Override
			public JComponent produce() {
				return new TimelapseComp( displayName + "Timelapse", multiRepAnalyzerComp, playerStats );
			}
		}, true );
		tp.addTab( "Metric trend", Icons.F_EDIT_MATHEMATICS, new Producer< JComponent >() {
			@Override
			public JComponent produce() {
				return new MetricTrendComp( displayName + "Metric trend", playerStats );
			}
		}, true );
		tp.addTab( "Region", Region.RICON, new Producer< JComponent >() {
			@Override
			public JComponent produce() {
				return new RegionComp( displayName + "Region", playerStats );
			}
		}, true );
		tp.addTab( "Account", Icons.F_TAGS, new Producer< JComponent >() {
			@Override
			public JComponent produce() {
				return new AccountComp( displayName + "Account", playerStats );
			}
		}, true );
		
		addCenter( tp );
	}
	
}
