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

import hu.scelight.gui.icon.Icons;
import hu.scelight.gui.page.multirepanalyzer.model.Game;
import hu.scelight.gui.page.multirepanalyzer.model.GeneralStats;
import hu.scelight.gui.page.multirepanalyzer.model.Part;
import hu.scelight.gui.page.multirepanalyzer.model.PlayerStats;
import hu.scelight.gui.page.multirepanalyzer.model.Stats;
import hu.scelight.gui.page.replist.RepListPage;
import hu.scelight.gui.setting.SettingsGui;
import hu.scelight.sc2.rep.model.initdata.gamedesc.GameSpeed;
import hu.scelight.service.env.Env;
import hu.scelight.service.settings.Settings;
import hu.scelight.util.Utils;
import hu.scelightapibase.bean.settings.ISettingChangeEvent;
import hu.scelightapibase.bean.settings.ISettingChangeListener;
import hu.scelightapibase.gui.comp.multipage.IPage;
import hu.sllauncher.action.XAction;
import hu.sllauncher.gui.comp.BorderPanel;
import hu.sllauncher.gui.comp.ToolBarForTable;
import hu.sllauncher.gui.comp.table.XTable;
import hu.sllauncher.gui.comp.table.XTableModel;
import hu.sllauncher.gui.comp.table.renderer.XTableCellRenderer;
import hu.sllauncher.service.lang.Month;
import hu.sllauncher.util.Pair;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * Calendar component. Handles both the global and player calendar.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class CalendarComp extends BorderPanel {
	
	/** Color for days with no plays / games. */
	private static final Color   RESTED_COLOR  = new Color( 225, 225, 245 );
	
	/** Played colors count. */
	private static final int     PCC           = 48;                        // Must be even!
	                                                                         
	/** Color for days with plays / games. */
	private static final Color[] PLAYED_COLORS = new Color[ PCC ];
	{
		final int minR = 225, minG = 255, minB = 225;
		final int midR = 112, midG = 128, midB = 112;
		final int maxR = 150, maxG = 90, maxB = 49;
		
		final int half = PCC / 2;
		for ( int i = 0; i < half; i++ ) {
			PLAYED_COLORS[ i ] = new Color( minR + ( midR - minR ) * i / half, minG + ( midG - minG ) * i / half, minB + ( midB - minB ) * i / half );
			PLAYED_COLORS[ half + i ] = new Color( midR + ( maxR - midR ) * i / half, midG + ( maxG - midG ) * i / half, midB + ( maxB - midB ) * i / half );
		}
	}
	
	
	/** Table displaying the stats. */
	private final XTable         table         = new XTable();
	
	/**
	 * Creates a new {@link CalendarComp}.
	 * 
	 * @param displayName display name to be used when replays of stats rows are opened
	 * @param multiRepAnalyzerComp reference to the Multi-replay Analyzer component
	 * @param gameList game list
	 */
	public CalendarComp( final String displayName, final MultiRepAnalyzerComp multiRepAnalyzerComp, final List< Game > gameList ) {
		this( displayName, multiRepAnalyzerComp, gameList, null );
	}
	
	/**
	 * Creates a new {@link CalendarComp}.
	 * 
	 * @param displayName display name to be used when replays of stats rows are opened
	 * @param multiRepAnalyzerComp reference to the Multi-replay Analyzer component
	 * @param playerStats player stats to show calendar for
	 */
	public CalendarComp( final String displayName, final MultiRepAnalyzerComp multiRepAnalyzerComp, final PlayerStats playerStats ) {
		this( displayName, multiRepAnalyzerComp, playerStats.gameList, playerStats );
	}
	
	/**
	 * Creates a new {@link CalendarComp}.
	 * 
	 * @param displayName display name to be used when replays of stats rows are opened
	 * @param multiRepAnalyzerComp reference to the Multi-replay Analyzer component
	 * @param gameList game list
	 * @param playerStats player stats to show calendar for
	 */
	private CalendarComp( final String displayName, final MultiRepAnalyzerComp multiRepAnalyzerComp, final List< Game > gameList, final PlayerStats playerStats ) {
		// Listen setting changes
		final ISettingChangeListener scl = new ISettingChangeListener() {
			@Override
			public void valuesChanged( final ISettingChangeEvent event ) {
				if ( event.affected( Settings.MULTI_REP_STRETCH_TABLES ) )
					table.setAutoResizeMode( event.get( Settings.MULTI_REP_STRETCH_TABLES ) ? JTable.AUTO_RESIZE_ALL_COLUMNS : JTable.AUTO_RESIZE_OFF );
			}
		};
		SettingsGui.addBindExecuteScl( scl, Env.APP_SETTINGS, Settings.MULTI_REP_STRETCH_TABLES.SELF_SET, table );
		
		// Calculate stats
		// To track first-last dates:
		Date firstDate = null;
		Date lastDate = null;
		
		final Map< Date, Stats > calStatsMap = new HashMap<>();
		
		final Calendar cal = Calendar.getInstance();
		
		for ( final Game g : gameList ) {
			// Track first-last dates:
			if ( firstDate == null || firstDate.after( g.date ) )
				firstDate = g.date;
			if ( lastDate == null || lastDate.before( g.date ) )
				lastDate = g.date;
			
			setDay( cal, g.date );
			final Date date = cal.getTime();
			
			if ( playerStats == null ) {
				// Global stats
				Stats cs = calStatsMap.get( date );
				if ( cs == null )
					calStatsMap.put( date, cs = new Stats() );
				// Length of the game is the play time
				cs.updateWithGame( g, g.lengthMs );
			} else {
				// Player stats
				for ( final Part refPart : g.parts ) { // Have to go through all participants as the zero-toon might be present multiple times
					if ( !playerStats.obj.equals( refPart.toon ) )
						continue;
					@SuppressWarnings( "unchecked" )
					GeneralStats< Date > cs = (GeneralStats< Date >) calStatsMap.get( date );
					if ( cs == null )
						calStatsMap.put( date, cs = new GeneralStats<>( date ) );
					// Time played on this day
					cs.updateWithPartGame( refPart, g );
				}
			}
		}
		
		final Date lastDay;
		if ( calStatsMap.isEmpty() ) {
			// No games, make last day smaller than the first day to avoid going into the loop
			lastDay = cal.getTime();
			cal.add( Calendar.DATE, 1 );
		} else {
			// Determine last month to show
			setDay( cal, lastDate );
			cal.set( Calendar.DAY_OF_MONTH, cal.getActualMaximum( Calendar.DAY_OF_MONTH ) );
			lastDay = cal.getTime();
			
			// Determine first month to show
			setDay( cal, firstDate );
			cal.set( Calendar.DAY_OF_MONTH, 1 );
		}
		
		final Vector< Vector< Object > > data = new Vector<>( calStatsMap.size() );
		Vector< Object > row = null;
		
		// Store the stats object associated with the cells.
		final Map< Pair< Integer, Integer >, Stats > cellStatsMap = new HashMap<>();
		
		final XTableModel model = table.getXTableModel();
		
		// Custom renderer for background setup.
		table.setTableCellRenderer( new TableCellRenderer() {
			private final XTableCellRenderer xtcrenderer = table.getXTableCellRenderer();
			
			/**
			 * Milliseconds in a day.
			 * <p>
			 * Note: if game-time is used, total real-time played time should also be calculated in the background and used with the real MS_IN_DAY value!<br>
			 * This simplification assumes that all games were played on {@link GameSpeed#FASTER} game speed which is a good and simple estimation.
			 * </p>
			 */
			private final long               MS_IN_DAY   = multiRepAnalyzerComp.useRealTime ? Utils.MS_IN_DAY : GameSpeed.FASTER
			                                                     .convertToGameTime( Utils.MS_IN_DAY );
			
			@Override
			public Component getTableCellRendererComponent( final JTable table, final Object value, final boolean isSelected, final boolean hasFocus,
			        final int row, final int column ) {
				xtcrenderer.setBackground( null );
				xtcrenderer.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, column );
				
				if ( value != null && !( (String) value ).contains( " class='MONTH' " ) ) { // A normal day cell
					xtcrenderer.setBorder( BorderFactory.createRaisedSoftBevelBorder() );
					if ( !isSelected ) {
						// Determine cell color
						final Stats stats = cellStatsMap.get( new Pair<>( table.convertRowIndexToModel( row ), table.convertColumnIndexToModel( column ) ) );
						if ( stats == null ) {
							xtcrenderer.setBackground( RESTED_COLOR );
						} else {
							// Pick a color based on the played hours.
							// We're showing darkest color if played at least 12 hours, that's why the "2 *" (12*2 = 24 = 1 day)
							final int colorIdx = (int) ( playerStats == null ? 0 : 2 * stats.totalTimePlayedMs * PCC / MS_IN_DAY );
							xtcrenderer.setBackground( PLAYED_COLORS[ Math.max( 0, Math.min( PCC - 1, colorIdx ) ) ] );
						}
					}
				} else if ( !isSelected ) {
					// Empty cell
					xtcrenderer.setBackground( Color.WHITE );
				}
				
				return xtcrenderer;
			}
		} );
		
		// Build table data
		final Vector< String > columns = Utils.asNewVector( "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday" );
		
		// Calendar holds the first day (1st of the month of the first game)
		for ( Date day = cal.getTime(); !day.after( lastDay ); ) {
			if ( cal.get( Calendar.DAY_OF_MONTH ) == 1 ) {
				// Start month
				row = Utils.vector( null, null, null,
				        "<html><span class='MONTH' style='font-weight:bold;font-size:170%'><span style='color:#999999'>" + cal.get( Calendar.YEAR )
				                + "</span>&nbsp;" + Month.VALUES[ cal.get( Calendar.MONTH ) ] + "</span></html>" );
				data.add( row );
				
				row = new Vector<>( 7 );
				data.add( row );
				final int dayOfWeek = cal.get( Calendar.DAY_OF_WEEK );
				for ( int i = dayOfWeek == Calendar.SUNDAY ? 6 : dayOfWeek - 2; i > 0; i-- )
					row.add( null );
			} else if ( cal.get( Calendar.DAY_OF_WEEK ) == Calendar.MONDAY ) {
				// Start week
				row = new Vector<>( 7 );
				data.add( row );
			}
			
			final StringBuilder sb = new StringBuilder( "<html><span color='#b02000'>" );
			sb.append( cal.get( Calendar.DAY_OF_MONTH ) ).append( "</span>" );
			final Stats cs = calStatsMap.get( day );
			if ( cs == null )
				sb.append( "<br><br>&nbsp;" );
			if ( cs != null ) {
				cellStatsMap.put( new Pair<>( data.size() - 1, row.size() ), cs ); // row is already added, but cell is not yet
				if ( playerStats == null ) {
					// Global stats
					sb.append( "<br>&nbsp;&nbsp;<b>" ).append( cs.gameList.size() ).append( cs.gameList.size() == 1 ? " game" : " games" );
				} else {
					// Player stats
					sb.append( "<br>&nbsp;&nbsp;<b>" ).append( cs.plays ).append( cs.plays == 1 ? " play" : " plays" );
					sb.append( " (" ).append( ( (GeneralStats< ? >) cs ).record ).append( ')' );
				}
				sb.append( "</b><br>&nbsp;&nbsp;Total: " ).append( cs.getTotalTimePlayed() );
			}
			row.add( sb.toString() );
			
			// Increment
			cal.add( Calendar.DATE, 1 );
			day = cal.getTime();
		}
		
		model.setDataVector( data, columns );
		
		table.setSortable( false );
		table.getTableHeader().setReorderingAllowed( false );
		table.setRowHeight( 50 );
		table.setColumnSelectionAllowed( true );
		
		// Build GUI
		final ToolBarForTable toolBar = new ToolBarForTable( table );
		addNorth( toolBar );
		toolBar.addSelectInfoLabel( "Select a Day." );
		final XAction listRepsAction = new XAction( Icons.F_BLUE_FOLDER_OPEN_TABLE, "List Replays of the Selected Day (Shift+Enter)", this ) {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				int count = Env.APP_SETTINGS.get( Settings.MULTI_REP_MAX_STATS_TO_OPEN_TOGETHER );
				IPage< ? > firstPage = null;
				
				outerCycle:
				for ( final int row : table.getSelectedModelRows() ) {
					for ( int col = 0; col < columns.size(); col++ ) {
						if ( !table.isCellSelected( table.convertRowIndexToView( row ), table.convertColumnIndexToView( col ) ) )
							continue;
						final Stats stats = cellStatsMap.get( new Pair<>( row, col ) );
						if ( stats == null )
							continue;
						
						final IPage< ? > page = new RepListPage( displayName + " \u00d7 " + Env.LANG.formatDate( stats.firstDate ), stats.getRepFiles() );
						if ( firstPage == null )
							firstPage = page;
						Env.MAIN_FRAME.getRepFoldersPage().addChild( page );
						
						if ( --count <= 0 )
							break outerCycle;
					}
				}
				
				if ( firstPage != null ) {
					Env.MAIN_FRAME.rebuildMainPageTree();
					Env.MAIN_FRAME.multiPageComp.selectPage( firstPage );
				}
			}
		};
		toolBar.addSelEnabledButton( listRepsAction );
		final XAction analyzeRepsAction = new XAction( Icons.F_CHART_UP_COLOR, "Multi-Replay Analyze Replays of the Selected Row (Ctrl+Enter)", this ) {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				int count = Env.APP_SETTINGS.get( Settings.MULTI_REP_MAX_STATS_TO_OPEN_TOGETHER );
				IPage< ? > firstPage = null;
				
				outerCycle:
				for ( final int row : table.getSelectedModelRows() ) {
					for ( int col = 0; col < columns.size(); col++ ) {
						if ( !table.isCellSelected( table.convertRowIndexToView( row ), table.convertColumnIndexToView( col ) ) )
							continue;
						final Stats stats = cellStatsMap.get( new Pair<>( row, col ) );
						if ( stats == null )
							continue;
						
						final IPage< ? > page = new MultiRepAnalyzerPage( stats.getRepFiles() );
						if ( firstPage == null )
							firstPage = page;
						Env.MAIN_FRAME.getMultiRepAnalyzersPage().addChild( page );
						
						if ( --count <= 0 )
							break outerCycle;
					}
				}
				
				if ( firstPage != null ) {
					Env.MAIN_FRAME.rebuildMainPageTree();
					Env.MAIN_FRAME.multiPageComp.selectPage( firstPage );
				}
			}
		};
		toolBar.addSelEnabledButton( analyzeRepsAction );
		toolBar.finalizeLayout();
		table.setShiftOpenAction( listRepsAction );
		table.setCtrlOpenAction( analyzeRepsAction );
		addCenter( table.createWrapperBox( true, table.createToolBarParams( this ) ) );
		
		// Scroll to bottom
		table.scrollRectToVisible( table.getCellRect( data.size() - 1, 0, true ) );
	}
	
	/**
	 * Sets only the day part of the calendar, resets the time part.
	 * 
	 * @param cal calendar whose day part to be set
	 * @param date date specifing the day
	 */
	private static void setDay( final Calendar cal, final Date date ) {
		cal.setTime( date );
		cal.set( Calendar.HOUR_OF_DAY, 0 );
		cal.set( Calendar.MINUTE, 0 );
		cal.set( Calendar.SECOND, 0 );
		cal.set( Calendar.MILLISECOND, 0 );
	}
	
}
