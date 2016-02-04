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
import hu.sllauncher.service.lang.Day;
import hu.sllauncher.service.lang.Month;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;

/**
 * Time trends details component.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class TimeTrendComp extends BorderPanel {
	
	/**
	 * Time trend group.
	 * 
	 * @author Andras Belicza
	 */
	private enum Group {
		
		/** Play # in session. */
		SESSION( "Session", Icons.F_CHAIN, "<html>Play #<br>in session</html>", Integer.class ),
		
		/** Hour of day. */
		HOUR( "Hour", Icons.F_CLOCK_MOON_PHASE, "<html>Hour<br>of day</html>", String.class ),
		
		/** Day of week. */
		DAY( "Day", Icons.F_CALENDAR_SELECT, "<html>Day<br>of week</html>", Day.class ),
		
		/** Day of month. */
		DATE( "Date", Icons.F_CALENDAR_SELECT, "<html>Day<br>of month</html>", String.class ),
		
		/** Month of year. */
		MONTH( "Month", Icons.F_CALENDAR_SELECT_MONTH, "<html>Month<br>of year</html>", Month.class );
		
		
		/** Name of the group. */
		public final String     name;
		
		/** Ricon of the group. */
		public final LRIcon     ricon;
		
		/** Column name for the group. */
		public final String     colName;
		
		/** Stat object class of the group. */
		public final Class< ? > objClass;
		
		
		/**
		 * Creates a new {@link Group}.
		 * 
		 * @param name name of the group
		 * @param ricon ricon of the group
		 * @param colName column name for the group
		 * @param objClass stat object class of the group
		 */
		private Group( final String name, final LRIcon ricon, final String colName, final Class< ? > objClass ) {
			this.name = name;
			this.ricon = ricon;
			this.colName = colName;
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
		public < T extends Comparable< T > > T getStatObj( final Calendar cal, final StringBuilder sb ) {
			switch ( this ) {
				case HOUR :
					sb.setLength( 0 );
					return (T) Utils.append( sb, cal, Calendar.HOUR_OF_DAY ).toString();
				case DAY :
					return (T) Day.VALUES[ ( cal.get( Calendar.DAY_OF_WEEK ) + 5 ) % 7 ];
				case DATE :
					sb.setLength( 0 );
					return (T) Utils.append( sb, cal, Calendar.DATE ).toString();
				case MONTH :
					return (T) Month.VALUES[ cal.get( Calendar.MONTH ) ];
				default :
					throw new RuntimeException( "Unhandled group: " + name );
			}
		}
		
	}
	
	
	/** Reference to the Multi-replay Analyzer component. */
	private final MultiRepAnalyzerComp multiRepAnalyzerComp;
	
	/**
	 * Creates a new {@link TimeTrendComp}.
	 * 
	 * @param displayName display name to be used when replays of stats rows are opened
	 * @param multiRepAnalyzerComp reference to the Multi-replay Analyzer component
	 * @param playerStats player stats to show timelapse details for
	 */
	public TimeTrendComp( final String displayName, final MultiRepAnalyzerComp multiRepAnalyzerComp, final PlayerStats playerStats ) {
		this.multiRepAnalyzerComp = multiRepAnalyzerComp;
		
		// Now build GUI
		final XTabbedPane tp = new XTabbedPane();
		
		// First tab
		final Group firstGr = Group.SESSION;
		tp.addTab( firstGr.name, firstGr.ricon, createTimeTrendStatsComp( displayName, playerStats, firstGr ), false );
		
		// Other tabs
		for ( final Group gr : Group.values() ) {
			if ( gr == firstGr )
				continue;
			
			tp.addTab( gr.name, gr.ricon, new Producer< JComponent >() {
				@Override
				public JComponent produce() {
					return createTimeTrendStatsComp( displayName, playerStats, gr );
				}
			}, true );
		}
		
		addCenter( tp );
	}
	
	/**
	 * Creates a {@link GeneralTableStatsComp} for the specified time trend group.
	 * 
	 * @param <T> type of trend, the base of aggregation
	 * @param displayName display name for the time trend group tab
	 * @param playerStats player stats to show time trend details for
	 * @param group time trend group, this is the base of aggregation
	 * @return a {@link GeneralTableStatsComp} for the specified time trend group
	 */
	private < T extends Comparable< T > > GeneralTableStatsComp< T, T > createTimeTrendStatsComp( final String displayName, final PlayerStats playerStats,
	        final Group group ) {
		return new GeneralTableStatsComp< T, T >( displayName + " \u00d7 " + group.name ) {
			{
				if ( group == Group.SESSION ) {
					toolBar.addSeparator();
					toolBar.add( new HelpIcon( Helps.TIME_TREND_PLAYING_SESSION ) );
				}
				
				@SuppressWarnings( "unchecked" )
				final Class< T > objClass = (Class< T >) group.objClass;
				buildGui( playerStats, group.colName, objClass );
			}
			
			@Override
			protected Collection< GeneralStats< T > > calculateStats( final PlayerStats playerStats ) {
				// Session granularity is special, it operates on relation between subsequent plays, not on the play date.
				if ( group == Group.SESSION ) {
					@SuppressWarnings( "unchecked" )
					final Collection< GeneralStats< T > > statsColl = (Collection< GeneralStats< T > >) (Object) calculateSessionStats( playerStats );
					return statsColl;
				}
				
				final Calendar cal = Calendar.getInstance();
				
				// Calculate stats
				// Note: could be optimized for day and month with EnumMap, but not worth the extra syntax...
				final Map< T, GeneralStats< T > > generalStatsMap = new HashMap<>();
				final StringBuilder sb = new StringBuilder();
				for ( final Game g : playerStats.gameList )
					for ( final Part refPart : g.parts ) { // Have to go through all participants as the zero-toon might be present multiple times
						if ( !playerStats.obj.equals( refPart.toon ) )
							continue;
						
						cal.setTime( g.date );
						final T statObj = group.getStatObj( cal, sb );
						GeneralStats< T > gs = generalStatsMap.get( statObj );
						if ( gs == null )
							generalStatsMap.put( statObj, gs = new GeneralStats<>( statObj ) );
						gs.updateWithPartGame( refPart, g );
					}
				
				return generalStatsMap.values();
			}
			
			private Collection< GeneralStats< Integer > > calculateSessionStats( final PlayerStats playerStats ) {
				final long maxBreakMs = Env.APP_SETTINGS.get( Settings.MULTI_REP_MAX_SESSION_BREAK ) * Utils.MS_IN_MIN;
				
				// Calculate stats
				
				// Game list must be sorted by play time
				final List< Game > gameList = new ArrayList<>( playerStats.gameList );
				Collections.sort( gameList );
				
				final Map< Integer, GeneralStats< Integer > > generalStatsMap = new HashMap<>();
				
				int playNumInSession = 1;
				Game lastGame = null;
				
				for ( final Game g : gameList ) {
					boolean newSession = false;
					if ( lastGame == null )
						newSession = true;
					else {
						// Note: this is a simplification because the refpart's play time should be used from the last game instead of the game length!
						final long realLengthMs = multiRepAnalyzerComp.useRealTime ? g.lengthMs : g.gameSpeed.convertToRealTime( g.lengthMs );
						
						if ( g.date.getTime() - realLengthMs - lastGame.date.getTime() > maxBreakMs ) {
							// Break between subsequent plays is greater than the max session break: start a new session
							newSession = true;
						}
					}
					if ( newSession )
						playNumInSession = 1;
					
					for ( final Part refPart : g.parts ) { // Have to go through all participants as the zero-toon might be present multiple times
						if ( !playerStats.obj.equals( refPart.toon ) )
							continue;
						
						GeneralStats< Integer > gs = generalStatsMap.get( playNumInSession );
						if ( gs == null )
							generalStatsMap.put( playNumInSession, gs = new GeneralStats<>( playNumInSession ) );
						gs.updateWithPartGame( refPart, g );
					}
					
					// Session number and last game must be updated outside the participant cycle!
					lastGame = g;
					playNumInSession++;
				}
				
				return generalStatsMap.values();
			}
		};
	}
	
}
