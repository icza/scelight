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
import hu.scelight.gui.icon.Icons;
import hu.scelight.gui.page.multirepanalyzer.TableStatsComp;
import hu.scelight.gui.page.multirepanalyzer.model.Game;
import hu.scelight.gui.page.multirepanalyzer.model.Part;
import hu.scelight.gui.page.multirepanalyzer.model.PlayerMapStats;
import hu.scelight.gui.page.multirepanalyzer.model.PlayerStats;
import hu.scelight.gui.page.multirepanalyzer.model.Stats;
import hu.scelight.service.env.Env;
import hu.scelight.service.settings.Settings;
import hu.sllauncher.gui.comp.table.renderer.ProgressBarView;
import hu.sllauncher.util.DateValue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.RowSorter.SortKey;
import javax.swing.SortOrder;

/**
 * Player Map component.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class PlayerMapComp extends TableStatsComp {
	
	/**
	 * Creates a new {@link PlayerMapComp}.
	 * 
	 * @param displayName display name to be used when replays of stats rows are opened
	 * @param playerStats player stats to show map stats for
	 */
	public PlayerMapComp( final String displayName, final PlayerStats playerStats ) {
		super( displayName );
		
		toolBar.finalizeLayout();
		
		// Calculate stats
		final Map< String, PlayerMapStats > playerMapStatsMap = new HashMap<>();
		for ( final Game g : playerStats.gameList )
			for ( final Part refPart : g.parts ) { // Have to go through all participants as the zero-toon might be present multiple times
				if ( !playerStats.obj.equals( refPart.toon ) )
					continue;
				
				PlayerMapStats ms = playerMapStatsMap.get( g.map );
				if ( ms == null )
					playerMapStatsMap.put( g.map, ms = new PlayerMapStats( g ) );
				ms.updateWithPartGame( refPart, g );
			}
		
		// Build table data
		statsColIdx = addColumn( "Stats", Stats.class );
		addColumn( "<html>Map<br>size</html>", ProgressBarView.class, true );
		final int mapImageColIdx = addColumn( "<html>Map<br>Image</html>", Icon.class );
		objColIdx = addColumn( "Name", String.class );
		final int playsColIdx = addColumn( "Plays", ProgressBarView.class, true );
		addColumn( "<html>Plays<br>%</html>", NullAwareComparable.class, true );
		addColumn( "<html>Record<br>V-D-O</html>", ProgressBarView.class, true );
		addColumn( "Win %", ProgressBarView.class, true );
		addColumn( "<html>1v1<br>P win %</html>", ProgressBarView.class, true );
		addColumn( "<html>1v1<br>T win %</html>", ProgressBarView.class, true );
		addColumn( "<html>1v1<br>Z win %</html>", ProgressBarView.class, true );
		addColumn( "<html>Time spent<br>playing</html>", ProgressBarView.class, true );
		addColumn( "<html>Avg play<br>length</html>", ProgressBarView.class, true );
		addColumn( "Presence", ProgressBarView.class, true );
		addColumn( "First play", DateValue.class );
		addColumn( "Last play", DateValue.class, true );
		
		setDefaultDescSortingColumns();
		model.setColumnClasses( columnClasses );
		
		final int rowHeight = Env.APP_SETTINGS.get( Settings.MULTI_REP_MAPS_ROW_HEIGHT );
		final int imageHeight = rowHeight * Env.APP_SETTINGS.get( Settings.MULTI_REP_MAPS_IMAGE_ZOOM ) / 100;
		
		int maxMapArea = 0;
		int maxPlays = 0;
		int minPoints = Integer.MAX_VALUE;
		int maxPoints = Integer.MIN_VALUE;
		double maxWinRatio = 0;
		double maxPWinRatio = 0;
		double maxTWinRatio = 0;
		double maxZWinRatio = 0;
		long maxTimePlayedMs = 0;
		long maxLength = 0;
		long maxPresenceMs = 0;
		for ( final PlayerMapStats ms : playerMapStatsMap.values() ) {
			maxMapArea = Math.max( maxMapArea, ms.mapSize.area );
			maxPlays = Math.max( maxPlays, ms.plays );
			minPoints = Math.min( minPoints, ms.record.getPoints() );
			maxPoints = Math.max( maxPoints, ms.record.getPoints() );
			maxWinRatio = Math.max( maxWinRatio, ms.record.getWinRatio() );
			maxPWinRatio = Math.max( maxPWinRatio, ms.precord.getWinRatio() );
			maxTWinRatio = Math.max( maxTWinRatio, ms.trecord.getWinRatio() );
			maxZWinRatio = Math.max( maxZWinRatio, ms.zrecord.getWinRatio() );
			maxTimePlayedMs = Math.max( maxTimePlayedMs, ms.totalTimePlayedMs );
			maxLength = Math.max( maxLength, ms.getAvgLengthMs() );
			maxPresenceMs = Math.max( maxPresenceMs, ms.getPresenceMs() );
		}
		
		final Vector< Vector< Object > > data = new Vector<>( playerMapStatsMap.size() );
		for ( final PlayerMapStats ms : playerMapStatsMap.values() ) {
			final Vector< Object > row = new Vector<>( columns.size() );
			row.add( ms );
			row.add( ms.mapSize.getSizeBar( maxMapArea ) );
			row.add( ms.ricon == null ? Icons.F_HOURGLASS.size( rowHeight ) : ms.ricon.size( imageHeight ) );
			row.add( ms.gameList.get( 0 ).map ); // Game list is not empty if map stats exists
			row.add( ms.getPlaysBar( maxPlays ) );
			row.add( NullAwareComparable.getPercent( ms.plays * 100.0 / playerStats.plays ) );
			row.add( ms.record.getPointsBar( minPoints, maxPoints ) );
			row.add( ms.record.getWinPercentBar( maxWinRatio ) );
			row.add( ms.precord.getWinPercentBar( maxPWinRatio ) );
			row.add( ms.trecord.getWinPercentBar( maxTWinRatio ) );
			row.add( ms.zrecord.getWinPercentBar( maxZWinRatio ) );
			row.add( ms.getTotalTimePlayedBar( maxTimePlayedMs ) );
			row.add( ms.getAvgLengthBar( maxLength ) );
			row.add( ms.getPresenceBar( maxPresenceMs ) );
			row.add( ms.getFirstDate() );
			row.add( ms.getLastDate() );
			data.add( row );
		}
		model.setDataVector( data, columns );
		
		table.getRowSorter().setSortKeys( Arrays.asList( new SortKey( playsColIdx, SortOrder.DESCENDING ) ) );
		
		table.setRowHeight( rowHeight );
		
		// Make the image column just wide enough, distribute remaining space between the rest of the columns.
		table.packColumns( mapImageColIdx );
		
		table.getColumnModel().removeColumn( table.getColumnModel().getColumn( statsColIdx ) );
		
		table.pack();
	}
	
}
