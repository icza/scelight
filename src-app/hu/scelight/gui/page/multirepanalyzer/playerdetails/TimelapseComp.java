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
import hu.scelight.gui.help.Helps;
import hu.scelight.gui.icon.Icons;
import hu.scelight.gui.page.multirepanalyzer.MultiRepAnalyzerComp;
import hu.scelight.gui.page.multirepanalyzer.model.Game;
import hu.scelight.gui.page.multirepanalyzer.model.GeneralStats;
import hu.scelight.gui.page.multirepanalyzer.model.Part;
import hu.scelight.gui.page.multirepanalyzer.model.PlayerStats;
import hu.scelight.service.env.Env;
import hu.scelight.service.settings.Settings;
import hu.scelight.util.Utils;
import hu.scelightapibase.util.iface.Producer;
import hu.sllauncher.gui.comp.BorderPanel;
import hu.sllauncher.gui.comp.HelpIcon;
import hu.sllauncher.gui.icon.LRIcon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.RowSorter.SortKey;
import javax.swing.SortOrder;

/**
 * Timelapse details component.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class TimelapseComp extends BorderPanel {
	
	/**
	 * Timelapse granularity.
	 * 
	 * @author Andras Belicza
	 */
	private enum Granularity {
	    
	    /** Session. */
		SESSION( "Session", Icons.F_CHAIN, Integer.class ),
		
		/** Year. */
		YEAR( "Year", Icons.F_CALENDAR, String.class ), // I use String to avoid automatic number formatting which is undesired in case of a year.
		
		/** Month. */
		MONTH( "Month", Icons.F_CALENDAR_SELECT_MONTH, String.class ),
		
		/** Week. */
		WEEK( "Week", Icons.F_CALENDAR_SELECT_WEEK, String.class ),
		
		/** Day. */
		DAY( "Day", Icons.F_CALENDAR_SELECT, String.class ),
		
		/** Play (each play individually). */
		PLAY( "Play", Icons.SC2_REPLAY, String.class );
		
		
		/** Name of the granularity. */
		public final String		name;
								
		/** Ricon of the granularity. */
		public final LRIcon		ricon;
								
		/** Stat object class of the granularity. */
		public final Class< ? >	objClass;
								
								
		/**
		 * Creates a new {@link Granularity}.
		 * 
		 * @param name name of the granularity
		 * @param ricon ricon of the granularity
		 * @param objClass stat object class of the granularity
		 */
		private Granularity( final String name, final LRIcon ricon, final Class< ? > objClass ) {
			this.name = name;
			this.ricon = ricon;
			this.objClass = objClass;
		}
		
		/**
		 * Returns the stats object for a play happened at the time specified by a calendar.
		 * 
		 * @param <T> type of the stat object
		 * @param cal calendar holding the time value of the play
		 * @param sb {@link StringBuilder} that can be used to assemble the stats name
		 * @return stats object for the play
		 */
		@SuppressWarnings( "unchecked" )
		public < T extends Comparable< T > > T getStatName( final Calendar cal, final StringBuilder sb ) {
			sb.setLength( 0 );
			switch ( this ) {
				case YEAR :
					return (T) Utils.append( sb, cal, Calendar.YEAR ).toString();
				case MONTH :
					sb.append( cal.get( Calendar.YEAR ) ).append( '-' );
					return (T) Utils.append( sb, cal, Calendar.MONTH, 1 ).toString();
				case WEEK :
					// Note: if last week of year ends in the next year, WEEK_OF_YEAR returns 1!
					// So year+1 should be used!
					int year = cal.get( Calendar.YEAR );
					final int woy = cal.get( Calendar.WEEK_OF_YEAR );
					if ( woy == 1 && cal.get( Calendar.MONTH ) == Calendar.DECEMBER )
						year++;
					sb.append( year ).append( " W: #" );
					return (T) Utils.append( sb, cal, Calendar.WEEK_OF_YEAR ).toString();
				case DAY :
					sb.append( cal.get( Calendar.YEAR ) ).append( '-' );
					Utils.append( sb, cal, Calendar.MONTH, 1 ).append( '-' );
					return (T) Utils.append( sb, cal, Calendar.DATE ).toString();
				case PLAY :
					sb.append( cal.get( Calendar.YEAR ) ).append( '-' );
					Utils.append( sb, cal, Calendar.MONTH, 1 ).append( '-' );
					Utils.append( sb, cal, Calendar.DATE ).append( ' ' );
					Utils.append( sb, cal, Calendar.HOUR_OF_DAY ).append( ':' );
					Utils.append( sb, cal, Calendar.MINUTE ).append( ':' );
					return (T) Utils.append( sb, cal, Calendar.SECOND ).toString();
				default :
					throw new RuntimeException( "Unhandled granularity: " + name );
			}
		}
		
	}
	
	
	/** Reference to the Multi-replay Analyzer component. */
	private final MultiRepAnalyzerComp multiRepAnalyzerComp;
	
	/**
	 * Creates a new {@link TimelapseComp}.
	 * 
	 * @param displayName display name to be used when replays of stats rows are opened
	 * @param multiRepAnalyzerComp reference to the Multi-replay Analyzer component
	 * @param playerStats player stats to show timelapse details for
	 */
	public TimelapseComp( final String displayName, final MultiRepAnalyzerComp multiRepAnalyzerComp, final PlayerStats playerStats ) {
		this.multiRepAnalyzerComp = multiRepAnalyzerComp;
		
		// Now build GUI
		final XTabbedPane tp = new XTabbedPane();
		
		// First tab
		final Granularity firstGr = Granularity.SESSION;
		tp.addTab( firstGr.name, firstGr.ricon, createTimelapseStatsComp( displayName, playerStats, firstGr ), false );
		
		// Other tabs
		for ( final Granularity gr : Granularity.values() ) {
			if ( gr == firstGr )
				continue;
				
			tp.addTab( gr.name, gr.ricon, new Producer< JComponent >() {
				@Override
				public JComponent produce() {
					return createTimelapseStatsComp( displayName, playerStats, gr );
				}
			}, true );
		}
		
		addCenter( tp );
	}
	
	/**
	 * Creates a {@link GeneralTableStatsComp} for the specified timelapse granularity.
	 * 
	 * @param <T> type of trend, the base of aggregation
	 * @param displayName display name for the timelapse tab
	 * @param playerStats player stats to show timelapse details for
	 * @param granularity timelapse granularity, this is the base of aggregation
	 * @return a {@link GeneralTableStatsComp} for the specified timelapse granularity
	 */
	private < T extends Comparable< T > > GeneralTableStatsComp< T, T > createTimelapseStatsComp( final String displayName, final PlayerStats playerStats,
	        final Granularity granularity ) {
		return new GeneralTableStatsComp< T, T >( displayName + " \u00d7 " + granularity.name) {
			{
				if ( granularity == Granularity.SESSION ) {
					toolBar.addSeparator();
					toolBar.add( new HelpIcon( Helps.TIMELAPSE_PLAYING_SESSION ) );
				}
				
				@SuppressWarnings( "unchecked" )
				final Class< T > objClass = (Class< T >) granularity.objClass;
				buildGui( playerStats,
		                granularity == Granularity.SESSION ? "<html>Session id<br>(counter)</html>" : "<html><br>" + granularity.name + "</html>", objClass );
						
				// Timelapse tab: default descendant sorting by the stat object (it is game time related)
				table.getXTableRowSorter().setColumnDefaultDesc( objColIdx, true );
				table.getRowSorter().setSortKeys( Arrays.asList( new SortKey( objColIdx, SortOrder.DESCENDING ) ) );
			}
			
			@Override
			protected Collection< GeneralStats< T > > calculateStats( final PlayerStats playerStats ) {
				// Session granularity is special, it operates on relation between subsequent plays, not on the play date.
				if ( granularity == Granularity.SESSION ) {
					@SuppressWarnings( "unchecked" )
					final Collection< GeneralStats< T > > statsColl = (Collection< GeneralStats< T > >) (Object) calculateSessionStats( playerStats );
					return statsColl;
				}
				
				final Calendar cal = Calendar.getInstance();
				
				// Calculate stats
				final Map< T, GeneralStats< T > > generalStatsMap = new HashMap< >();
				final StringBuilder sb = new StringBuilder();
				for ( final Game g : playerStats.gameList )
					for ( final Part refPart : g.parts ) { // Have to go through all participants as the zero-toon might be present multiple times
						if ( !playerStats.obj.equals( refPart.toon ) )
							continue;
							
						cal.setTime( g.date );
						final T statObj = granularity.getStatName( cal, sb );
						
						GeneralStats< T > gs = generalStatsMap.get( statObj );
						if ( gs == null )
							generalStatsMap.put( statObj, gs = new GeneralStats< >( statObj ) );
						gs.updateWithPartGame( refPart, g );
					}
					
				return generalStatsMap.values();
			}
			
			private Collection< GeneralStats< Integer > > calculateSessionStats( final PlayerStats playerStats ) {
				final long maxBreakMs = Env.APP_SETTINGS.get( Settings.MULTI_REP_MAX_SESSION_BREAK ) * Utils.MS_IN_MIN;
				
				// Game list must be sorted by play time
				final List< Game > gameList = new ArrayList< >( playerStats.gameList );
				Collections.sort( gameList );
				
				Integer sessionCounter = 0;
				Game lastGame = null;
				
				final Map< Integer, GeneralStats< Integer > > generalStatsMap = new HashMap< >();
				
				for ( final Game g : gameList ) {
					// Session counter and last game must be updated outside the participant cycle!
					boolean newSession = false;
					if ( lastGame == null )
						newSession = true;
					else {
						final long realLengthMs = multiRepAnalyzerComp.useRealTime ? g.lengthMs : g.gameSpeed.convertToRealTime( g.lengthMs );
						
						if ( g.date.getTime() - realLengthMs - lastGame.date.getTime() > maxBreakMs ) {
							// Break between subsequent plays is greater than the max session break: start a new session
							newSession = true;
						}
					}
					if ( newSession )
						sessionCounter++;
						
					for ( final Part refPart : g.parts ) { // Have to go through all participants as the zero-toon might be present multiple times
						if ( !playerStats.obj.equals( refPart.toon ) )
							continue;
							
						GeneralStats< Integer > gs = generalStatsMap.get( sessionCounter );
						if ( gs == null )
							generalStatsMap.put( sessionCounter, gs = new GeneralStats< >( sessionCounter ) );
						gs.updateWithPartGame( refPart, g );
					}
					
					lastGame = g;
				}
				
				return generalStatsMap.values( );
			}
		};
	}
	
}
