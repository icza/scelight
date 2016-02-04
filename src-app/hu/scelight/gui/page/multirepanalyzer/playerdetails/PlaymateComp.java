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

import hu.scelight.gui.page.multirepanalyzer.MultiRepAnalyzerComp;
import hu.scelight.gui.page.multirepanalyzer.PlayerTableStatsComp;
import hu.scelight.gui.page.multirepanalyzer.model.Game;
import hu.scelight.gui.page.multirepanalyzer.model.Part;
import hu.scelight.gui.page.multirepanalyzer.model.PlayerStats;
import hu.scelight.gui.page.multirepanalyzer.model.PlaymateStats;
import hu.scelight.gui.page.multirepanalyzer.model.Record;
import hu.scelight.gui.page.multirepanalyzer.model.Stats;
import hu.scelight.sc2.rep.model.details.Toon;
import hu.sllauncher.gui.comp.table.renderer.ProgressBarView;
import hu.sllauncher.util.DateValue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.RowSorter.SortKey;
import javax.swing.SortOrder;

/**
 * Playmate of a player component.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class PlaymateComp extends PlayerTableStatsComp {
	
	/**
	 * Creates a new {@link PlaymateComp}.
	 * 
	 * @param displayName display name to be used when replays of stats rows are opened
	 * @param multiRepAnalyzerComp reference to the Multi-replay Analyzer component
	 * @param playerStats player stats to show playmate stats for
	 * @param playerStatsMap reference to the map of all player stats; required when the details of a playmate is to be opened
	 */
	public PlaymateComp( final String displayName, final MultiRepAnalyzerComp multiRepAnalyzerComp, final PlayerStats playerStats,
	        final Map< Toon, PlayerStats > playerStatsMap ) {
		super( displayName );
		
		table.setRowHeightForProgressBar();
		
		// Calculate stats
		final Map< Toon, PlaymateStats > playmateStatsMap = new HashMap<>();
		for ( final Game g : playerStats.gameList )
			for ( final Part refPart : g.parts ) { // Have to go through all participants as the zero-toon might be present multiple times
				if ( !playerStats.obj.equals( refPart.toon ) )
					continue;
				for ( final Part playmatePart : g.parts ) {
					if ( playmatePart == refPart )
						continue;
					PlaymateStats ps = playmateStatsMap.get( playmatePart.toon );
					if ( ps == null )
						playmateStatsMap.put( playmatePart.toon, ps = new PlaymateStats( playmatePart ) );
					ps.updateWithPartGame( refPart, playmatePart, g );
					ps.updateName( playmatePart, g );
				}
			}
		
		// Build table data
		statsColIdx = addColumn( "Stats", Stats.class );
		objColIdx = addColumn( "<html>Name<br>(Newest)</html>", String.class );
		final int allNamesColIdx = addColumn( "<html>All<br>Names</html>", String.class, true );
		final int commonPlaysColIdx = addColumn( "<html>Common<br>Plays</html>", ProgressBarView.class, true );
		addColumn( "<html>Plays<br>With</html>", ProgressBarView.class, true );
		addColumn( "<html>Plays<br>VS</html>", ProgressBarView.class, true );
		addColumn( "<html>Record<br>With</html>", Record.class, true );
		addColumn( "<html>Win %<br>With</html>", ProgressBarView.class, true );
		addColumn( "<html>Record<br>VS</html>", Record.class, true );
		addColumn( "<html>Win %<br>VS</html>", ProgressBarView.class, true );
		addColumn( "<html>Time played<br>together</html>", ProgressBarView.class, true );
		addColumn( "Presence", ProgressBarView.class, true );
		addColumn( "<html>First play<br>together</html>", DateValue.class );
		addColumn( "<html>Last play<br>together</html>", DateValue.class, true );
		addColumn( "Toon", Toon.class );
		
		setDefaultDescSortingColumns();
		model.setColumnClasses( columnClasses );
		
		int maxCommonPlays = 0;
		int maxPlaysWith = 0;
		int maxPlaysVs = 0;
		int minPointsWith = Integer.MAX_VALUE;
		int maxPointsWith = Integer.MIN_VALUE;
		double maxWinRatioWith = 0;
		int minPointsVs = Integer.MAX_VALUE;
		int maxPointsVs = Integer.MIN_VALUE;
		double maxWinRatioVs = 0;
		long maxTimePlayedMs = 0;
		long maxPresenceMs = 0;
		for ( final PlaymateStats ps : playmateStatsMap.values() ) {
			maxCommonPlays = Math.max( maxCommonPlays, ps.plays );
			maxPlaysWith = Math.max( maxPlaysWith, ps.recordWith.getAll() );
			maxPlaysVs = Math.max( maxPlaysVs, ps.recordVs.getAll() );
			minPointsWith = Math.min( minPointsWith, ps.recordWith.getPoints() );
			maxPointsWith = Math.max( maxPointsWith, ps.recordWith.getPoints() );
			maxWinRatioWith = Math.max( maxWinRatioWith, ps.recordWith.getWinRatio() );
			minPointsVs = Math.min( minPointsVs, ps.recordVs.getPoints() );
			maxPointsVs = Math.max( maxPointsVs, ps.recordVs.getPoints() );
			maxWinRatioVs = Math.max( maxWinRatioVs, ps.recordVs.getWinRatio() );
			maxTimePlayedMs = Math.max( maxTimePlayedMs, ps.totalTimePlayedMs );
			maxPresenceMs = Math.max( maxPresenceMs, ps.getPresenceMs() );
		}
		
		final Vector< Vector< Object > > data = new Vector<>( playmateStatsMap.size() );
		for ( final PlaymateStats ps : playmateStatsMap.values() ) {
			final Vector< Object > row = new Vector<>( columns.size() );
			row.add( ps );
			row.add( ps.getName() );
			row.add( ps.getAllNames() );
			row.add( ps.getPlaysBar( maxCommonPlays ) );
			row.add( ps.getPlaysWithBar( maxPlaysWith ) );
			row.add( ps.getPlaysVsBar( maxPlaysVs ) );
			row.add( ps.recordWith.getPointsBar( minPointsWith, maxPointsWith ) );
			row.add( ps.recordWith.getWinPercentBar( maxWinRatioWith ) );
			row.add( ps.recordVs.getPointsBar( minPointsVs, maxPointsVs ) );
			row.add( ps.recordVs.getWinPercentBar( maxWinRatioVs ) );
			row.add( ps.getTotalTimePlayedBar( maxTimePlayedMs ) );
			row.add( ps.getPresenceBar( maxPresenceMs ) );
			row.add( ps.getFirstDate() );
			row.add( ps.getLastDate() );
			row.add( ps.obj );
			data.add( row );
		}
		model.setDataVector( data, columns );
		
		table.getRowSorter().setSortKeys( Arrays.asList( new SortKey( commonPlaysColIdx, SortOrder.DESCENDING ) ) );
		
		// Make the all names column fixed-width.
		final int i = 70;
		table.getColumnModel().getColumn( allNamesColIdx ).setPreferredWidth( i );
		table.getColumnModel().getColumn( allNamesColIdx ).setMaxWidth( i );
		
		table.getColumnModel().removeColumn( table.getColumnModel().getColumn( statsColIdx ) );
		
		table.pack();
		
		buildToolBar( multiRepAnalyzerComp, playerStatsMap );
	}
	
}
