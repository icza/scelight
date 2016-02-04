/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.gui.page.multirepanalyzer;

import hu.scelight.gui.page.multirepanalyzer.model.Game;
import hu.scelight.gui.page.multirepanalyzer.model.Part;
import hu.scelight.gui.page.multirepanalyzer.model.PlayerStats;
import hu.scelight.gui.page.multirepanalyzer.model.PlayerStats.NameWithDate;
import hu.scelight.gui.page.multirepanalyzer.model.Stats;
import hu.scelight.sc2.rep.model.details.Toon;
import hu.scelight.service.env.Env;
import hu.scelight.service.settings.Settings;
import hu.sllauncher.gui.comp.table.renderer.BarCodeView;
import hu.sllauncher.gui.comp.table.renderer.ProgressBarView;
import hu.sllauncher.util.DateValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.RowSorter.SortKey;
import javax.swing.SortOrder;
import javax.swing.SwingUtilities;

/**
 * Player component.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class PlayerComp extends PlayerTableStatsComp {
	
	/**
	 * Creates a new {@link PlayerComp}.
	 * 
	 * @param displayName display name to be used when replays of stats rows are opened
	 * @param multiRepAnalyzerComp reference to the Multi-replay Analyzer component
	 * @param gameList game list
	 */
	public PlayerComp( final String displayName, final MultiRepAnalyzerComp multiRepAnalyzerComp, final List< Game > gameList ) {
		super( displayName );
		
		table.setRowHeightForProgressBar();
		
		// Calculate stats
		final Map< Toon, PlayerStats > playerStatsMap = new HashMap<>();
		final List< PlayerStats > mergedPlayerStats = multiRepAnalyzerComp.mergedAccountsMap.isEmpty() ? null : new ArrayList< PlayerStats >();
		
		for ( final Game g : gameList ) {
			for ( final Part part : g.parts ) {
				PlayerStats ps = playerStatsMap.get( part.toon );
				if ( ps == null ) {
					playerStatsMap.put( part.toon, ps = new PlayerStats( part ) );
					if ( ps.mergedToonNameMap != null )
						mergedPlayerStats.add( ps );
				}
				ps.updateWithPartGame( part, g );
				ps.updateName( part, g );
			}
		}
		
		// Choose proper name for merged accounts.
		if ( mergedPlayerStats != null ) {
			// If main toon was found, use its latest name.
			// Else use the latest name.
			for ( final PlayerStats ps : mergedPlayerStats ) {
				final NameWithDate nameWithDate = ps.mergedToonNameMap.get( ps.obj );
				if ( nameWithDate != null )
					ps.name = nameWithDate.name;
			}
		}
		
		// Build table data
		statsColIdx = addColumn( "Stats", Stats.class );
		objColIdx = addColumn( "<html>Name<br>(Newest)</html>", String.class );
		final int allNamesColIdx = addColumn( "<html>All<br>Names</html>", String.class, true );
		final int playsColIdx = addColumn( "Plays", ProgressBarView.class, true );
		addColumn( "<html>Avg<br>APM</html>", ProgressBarView.class, true );
		addColumn( "<html>Avg<br>SPM</html>", ProgressBarView.class, true );
		addColumn( "<html>Avg<br>SQ</html>", ProgressBarView.class, true );
		addColumn( "<html>Avg<br>SC %</html>", ProgressBarView.class, true );
		addColumn( "<html>Record<br>V-D-O</html>", ProgressBarView.class, true );
		addColumn( "Win %", ProgressBarView.class, true );
		addColumn( "<html>Races<br>played %</html>", BarCodeView.class );
		addColumn( "<html>Time spent<br>playing</html>", ProgressBarView.class, true );
		addColumn( "Presence", ProgressBarView.class, true );
		addColumn( "<html>Avg plays<br>per day</html>", ProgressBarView.class, true );
		addColumn( "First play", DateValue.class );
		addColumn( "Last play", DateValue.class, true );
		final int toonColIdx = addColumn( "Toon", Toon.class );
		
		setDefaultDescSortingColumns();
		model.setColumnClasses( columnClasses );
		
		int maxPlays = 0;
		int maxApm = 0;
		double maxSpm = 0;
		int maxSq = 0;
		double maxScp = 0;
		int minPoints = Integer.MAX_VALUE;
		int maxPoints = Integer.MIN_VALUE;
		double maxWinRatio = 0;
		long maxTimePlayedMs = 0;
		long maxPresenceMs = 0;
		float maxPlaysPerDay = 0;
		for ( final PlayerStats ps : playerStatsMap.values() ) {
			maxPlays = Math.max( maxPlays, ps.plays );
			maxApm = Math.max( maxApm, ps.getAvgApm() );
			maxSpm = Math.max( maxSpm, ps.getAvgSpm() );
			maxSq = Math.max( maxSq, ps.getAvgSq() );
			maxScp = Math.max( maxScp, ps.getAvgSupplyCappedPercent() );
			minPoints = Math.min( minPoints, ps.record.getPoints() );
			maxPoints = Math.max( maxPoints, ps.record.getPoints() );
			maxWinRatio = Math.max( maxWinRatio, ps.record.getWinRatio() );
			maxTimePlayedMs = Math.max( maxTimePlayedMs, ps.totalTimePlayedMs );
			maxPresenceMs = Math.max( maxPresenceMs, ps.getPresenceMs() );
			maxPlaysPerDay = Math.max( maxPlaysPerDay, ps.getAvgPlaysPerDay() );
		}
		
		final Vector< Vector< Object > > data = new Vector<>( playerStatsMap.size() );
		for ( final PlayerStats ps : playerStatsMap.values() ) {
			final Vector< Object > row = new Vector<>( columns.size() );
			row.add( ps );
			row.add( ps.getName() );
			row.add( ps.getAllNames() );
			row.add( ps.getPlaysBar( maxPlays ) );
			row.add( ps.getAvgApmBar( maxApm ) );
			row.add( ps.getAvgSpmBar( maxSpm ) );
			row.add( ps.getAvgSqBar( maxSq ) );
			row.add( ps.getAvgSupplyCappedPercentBar( maxScp ) );
			row.add( ps.record.getPointsBar( minPoints, maxPoints ) );
			row.add( ps.record.getWinPercentBar( maxWinRatio ) );
			row.add( ps.getRacesBarCode() );
			row.add( ps.getTotalTimePlayedBar( maxTimePlayedMs ) );
			row.add( ps.getPresenceBar( maxPresenceMs ) );
			row.add( ps.getAvgPlaysPerDayBar( maxPlaysPerDay ) );
			row.add( ps.getFirstDate() );
			row.add( ps.getLastDate() );
			row.add( ps.obj );
			data.add( row );
		}
		model.setDataVector( data, columns );
		
		table.getRowSorter().setSortKeys( Arrays.asList( new SortKey( playsColIdx, SortOrder.DESCENDING ) ) );
		
		// Make the all names column fixed-width.
		final int i = 70;
		table.getColumnModel().getColumn( allNamesColIdx ).setPreferredWidth( i );
		table.getColumnModel().getColumn( allNamesColIdx ).setMaxWidth( i );
		
		table.getColumnModel().removeColumn( table.getColumnModel().getColumn( statsColIdx ) );
		
		table.pack();
		
		buildToolBar( multiRepAnalyzerComp, playerStatsMap );
		
		if ( Env.APP_SETTINGS.get( Settings.MULTI_REP_AUTO_OPEN_FIRST_PLAYER ) && model.getRowCount() > 0 ) {
			// Invoke later else the first player's details tab would be the first (Global stats tab is not yet added).
			SwingUtilities.invokeLater( new Runnable() {
				@Override
				public void run() {
					// Query toon from the table instead of the model:
					// Table uses view index and is sorted by plays by default, so the first is with the most plays
					// while model's first is not what we want (model's first is undetermined).
					multiRepAnalyzerComp.addPlayerDetailsTab( playerStatsMap.get( table.getValueAt( 0, table.convertColumnIndexToView( toonColIdx ) ) ),
					        playerStatsMap );
				}
			} );
		}
	}
	
}
