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

import hu.belicza.andras.util.NullAwareComparable;
import hu.scelight.gui.page.multirepanalyzer.TableStatsComp;
import hu.scelight.gui.page.multirepanalyzer.model.GeneralStats;
import hu.scelight.gui.page.multirepanalyzer.model.PlayerStats;
import hu.scelight.gui.page.multirepanalyzer.model.Stats;
import hu.scelightapibase.gui.comp.table.renderer.IProgressBarView;
import hu.sllauncher.gui.comp.table.renderer.BarCodeView;
import hu.sllauncher.gui.comp.table.renderer.ProgressBarView;
import hu.sllauncher.util.DateValue;

import java.util.Arrays;
import java.util.Collection;
import java.util.Vector;

import javax.swing.RowSorter.SortKey;
import javax.swing.SortOrder;

/**
 * Play length component.
 * 
 * @param <K> key type of the general stats map
 * @param <T> type of the general object
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public abstract class GeneralTableStatsComp< K, T extends Comparable< T > > extends TableStatsComp {
	
	/**
	 * Creates a new {@link GeneralTableStatsComp}.
	 * 
	 * @param displayName display name to be used when replays of stats rows are opened
	 */
	public GeneralTableStatsComp( final String displayName ) {
		super( displayName );
	}
	
	/**
	 * Builds the GUI of the general table stats comp.<br>
	 * Also finalizes the tool bar, so if any custom buttons are to be added, they should be added before calling this.
	 * 
	 * @param playerStats player stats to show stats for
	 * @param objName name of the general object
	 * @param objClass class of the general object
	 */
	protected void buildGui( final PlayerStats playerStats, final String objName, final Class< T > objClass ) {
		toolBar.finalizeLayout();
		
		table.setRowHeightForProgressBar();
		
		// Build table data
		statsColIdx = addColumn( "Stats", Stats.class );
		objColIdx = addColumn( objName, objClass );
		addColumn( "Plays", IProgressBarView.class, true );
		addColumn( "<html>Plays<br>%</html>", NullAwareComparable.class, true );
		addColumn( "<html>Avg<br>APM</html>", ProgressBarView.class, true );
		addColumn( "<html>Avg<br>SPM</html>", ProgressBarView.class, true );
		addColumn( "<html>Avg<br>SQ</html>", ProgressBarView.class, true );
		addColumn( "<html>Avg<br>SC %</html>", ProgressBarView.class, true );
		addColumn( "<html>Record<br>V-D-O</html>", ProgressBarView.class, true );
		addColumn( "Win %", ProgressBarView.class, true );
		addColumn( "<html>Races<br>played %</html>", BarCodeView.class );
		addColumn( "<html>Time spent<br>playing</html>", ProgressBarView.class, true );
		addColumn( "<html>Avg play<br>length</html>", ProgressBarView.class, true );
		addColumn( "Presence", ProgressBarView.class, true );
		addColumn( "<html>Avg plays<br>per day</html>", ProgressBarView.class, true );
		addColumn( "First play", DateValue.class );
		addColumn( "Last play", DateValue.class, true );
		
		setDefaultDescSortingColumns();
		model.setColumnClasses( columnClasses );
		
		final Collection< GeneralStats< T > > generalStats = calculateStats( playerStats );
		
		int maxPlays = 0;
		int maxApm = 0;
		double maxSpm = 0;
		int maxSq = 0;
		double maxScp = 0;
		int minPoints = Integer.MAX_VALUE;
		int maxPoints = Integer.MIN_VALUE;
		double maxWinRatio = 0;
		long maxTimePlayedMs = 0;
		long maxLength = 0;
		long maxPresenceMs = 0;
		float maxPlaysPerDay = 0;
		for ( final GeneralStats< T > gs : generalStats ) {
			maxPlays = Math.max( maxPlays, gs.plays );
			maxApm = Math.max( maxApm, gs.getAvgApm() );
			maxSpm = Math.max( maxSpm, gs.getAvgSpm() );
			maxSq = Math.max( maxSq, gs.getAvgSq() );
			maxScp = Math.max( maxScp, gs.getAvgSupplyCappedPercent() );
			minPoints = Math.min( minPoints, gs.record.getPoints() );
			maxPoints = Math.max( maxPoints, gs.record.getPoints() );
			maxWinRatio = Math.max( maxWinRatio, gs.record.getWinRatio() );
			maxTimePlayedMs = Math.max( maxTimePlayedMs, gs.totalTimePlayedMs );
			maxLength = Math.max( maxLength, gs.getAvgLengthMs() );
			maxPresenceMs = Math.max( maxPresenceMs, gs.getPresenceMs() );
			maxPlaysPerDay = Math.max( maxPlaysPerDay, gs.getAvgPlaysPerDay() );
		}
		
		final Vector< Vector< Object > > data = new Vector<>( generalStats.size() );
		for ( final GeneralStats< T > gs : generalStats ) {
			final Vector< Object > row = new Vector<>( columns.size() );
			row.add( gs );
			row.add( gs.obj );
			row.add( gs.getPlaysBar( maxPlays ) );
			row.add( gs.getPlaySharePercent( playerStats.plays ) );
			row.add( gs.getAvgApmBar( maxApm ) );
			row.add( gs.getAvgSpmBar( maxSpm ) );
			row.add( gs.getAvgSqBar( maxSq ) );
			row.add( gs.getAvgSupplyCappedPercentBar( maxScp ) );
			row.add( gs.record.getPointsBar( minPoints, maxPoints ) );
			row.add( gs.record.getWinPercentBar( maxWinRatio ) );
			row.add( gs.getRacesBarCode() );
			row.add( gs.getTotalTimePlayedBar( maxTimePlayedMs ) );
			row.add( gs.getAvgLengthBar( maxLength ) );
			row.add( gs.getPresenceBar( maxPresenceMs ) );
			row.add( gs.getAvgPlaysPerDayBar( maxPlaysPerDay ) );
			row.add( gs.getFirstDate() );
			row.add( gs.getLastDate() );
			data.add( row );
		}
		model.setDataVector( data, columns );
		
		table.getRowSorter().setSortKeys( Arrays.asList( new SortKey( objColIdx, SortOrder.ASCENDING ) ) );
		
		table.getColumnModel().removeColumn( table.getColumnModel().getColumn( statsColIdx ) );
		
		table.pack();
	}
	
	/**
	 * Calculates the statistics.
	 * 
	 * @param playerStats player stats to show stats for
	 * @return the calculated statistics
	 */
	protected abstract Collection< GeneralStats< T > > calculateStats( PlayerStats playerStats );
	
}
