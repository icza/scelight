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

import hu.belicza.andras.util.NullAwareComparable;
import hu.scelight.gui.icon.Icons;
import hu.scelight.gui.page.multirepanalyzer.model.Game;
import hu.scelight.gui.page.multirepanalyzer.model.MapStats;
import hu.scelight.gui.page.multirepanalyzer.model.Stats;
import hu.scelight.service.env.Env;
import hu.scelight.service.settings.Settings;
import hu.sllauncher.gui.comp.table.renderer.ProgressBarView;
import hu.sllauncher.util.DateValue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.RowSorter.SortKey;
import javax.swing.SortOrder;

/**
 * Map component.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class MapComp extends TableStatsComp {
	
	/**
	 * Creates a new {@link MapComp}.
	 * 
	 * @param displayName display name to be used when replays of stats rows are opened
	 * @param gameList game list
	 */
	public MapComp( final String displayName, final List< Game > gameList ) {
		super( displayName );
		
		toolBar.finalizeLayout();
		
		// Calculate stats
		final Map< String, MapStats > mapStatsMap = new HashMap<>();
		for ( final Game g : gameList ) {
			MapStats ms = mapStatsMap.get( g.map );
			if ( ms == null )
				mapStatsMap.put( g.map, ms = new MapStats( g ) );
			ms.updateWithGame( g );
		}
		
		// Build table data
		statsColIdx = addColumn( "Stats", Stats.class );
		addColumn( "<html>Map<br>size</html>", ProgressBarView.class, true );
		final int mapImageColIdx = addColumn( "<html>Map<br>Image</html>", Icon.class );
		objColIdx = addColumn( "Name", String.class );
		final int gamesColIdx = addColumn( "Games", ProgressBarView.class, true );
		addColumn( "<html>Games<br>%</html>", NullAwareComparable.class, true );
		addColumn( "<html>1v1 P win %<br>PvP excluded</html>", ProgressBarView.class, true );
		addColumn( "<html>1v1 T win %<br>TvT excluded</html>", ProgressBarView.class, true );
		addColumn( "<html>1v1 Z win %<br>ZvZ excluded</html>", ProgressBarView.class, true );
		addColumn( "<html>Sum game<br>length</html>", ProgressBarView.class, true );
		addColumn( "<html>Avg game<br>length</html>", ProgressBarView.class, true );
		addColumn( "Presence", ProgressBarView.class, true );
		addColumn( "First game", DateValue.class );
		addColumn( "Last game", DateValue.class, true );
		
		setDefaultDescSortingColumns();
		model.setColumnClasses( columnClasses );
		
		final int rowHeight = Env.APP_SETTINGS.get( Settings.MULTI_REP_MAPS_ROW_HEIGHT );
		final int imageHeight = rowHeight * Env.APP_SETTINGS.get( Settings.MULTI_REP_MAPS_IMAGE_ZOOM ) / 100;
		
		int maxMapArea = 0;
		int maxGames = 0;
		double maxPWinRatio = 0;
		double maxTWinRatio = 0;
		double maxZWinRatio = 0;
		long maxTimePlayedMs = 0;
		long maxLength = 0;
		long maxPresenceMs = 0;
		for ( final MapStats ms : mapStatsMap.values() ) {
			maxMapArea = Math.max( maxMapArea, ms.mapSize.area );
			maxGames = Math.max( maxGames, ms.gameList.size() );
			maxPWinRatio = Math.max( maxPWinRatio, ms.precord.getWinRatio() );
			maxTWinRatio = Math.max( maxTWinRatio, ms.trecord.getWinRatio() );
			maxZWinRatio = Math.max( maxZWinRatio, ms.zrecord.getWinRatio() );
			maxTimePlayedMs = Math.max( maxTimePlayedMs, ms.totalTimePlayedMs );
			maxLength = Math.max( maxLength, ms.getAvgLengthMs() );
			maxPresenceMs = Math.max( maxPresenceMs, ms.getPresenceMs() );
		}
		
		final Vector< Vector< Object > > data = new Vector<>( mapStatsMap.size() );
		for ( final MapStats ms : mapStatsMap.values() ) {
			final Vector< Object > row = new Vector<>( columns.size() );
			row.add( ms );
			row.add( ms.mapSize.getSizeBar( maxMapArea ) );
			row.add( ms.ricon == null ? Icons.F_HOURGLASS.size( rowHeight ) : ms.ricon.size( imageHeight ) );
			row.add( ms.gameList.get( 0 ).map ); // Game list is not empty if map stats exists
			row.add( ms.getGamesBar( maxGames ) );
			row.add( ms.getPlaySharePercent( gameList.size() ) ); // games count = plays count
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
		
		table.getRowSorter().setSortKeys( Arrays.asList( new SortKey( gamesColIdx, SortOrder.DESCENDING ) ) );
		
		table.setRowHeight( rowHeight );
		
		// Make the image column just wide enough, distribute remaining space between the rest of the columns.
		table.packColumns( mapImageColIdx );
		
		table.getColumnModel().removeColumn( table.getColumnModel().getColumn( statsColIdx ) );
		
		table.pack();
	}
	
}
