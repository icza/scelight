/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.gui.page.repanalyzer.charts.chartfactory;

import java.util.ArrayList;
import java.util.List;

import hu.scelight.gui.page.repanalyzer.charts.ChartsComp;
import hu.scelight.gui.page.repanalyzer.charts.chartscanvas.chart.Chart;
import hu.scelight.gui.page.repanalyzer.charts.chartscanvas.chart.model.DataModel;
import hu.scelight.gui.page.repanalyzer.charts.chartscanvas.chart.model.DataSet;
import hu.scelight.sc2.rep.repproc.User;

/**
 * Base chart factory with utilities so descendants can focus on creating and calculating data sets.
 * 
 * @param <T> type of the data set used by the charts this factory creates
 * 		   
 * @author Andras Belicza
 */
public abstract class BaseChartFactory< T extends DataSet > extends ChartFactory {
	
	/**
	 * Creates a new {@link BaseChartFactory}.
	 * 
	 * @param chartsComp reference to the charts component that created us
	 */
	public BaseChartFactory( final ChartsComp chartsComp ) {
		super( chartsComp );
	}
	
	
	/** List of charts created. */
	protected List< Chart< T > > chartList;
								 
	/** Chart data models indexed by user id. */
	protected DataModel< T >[]	 modelByUserIds;
								 
	/** Chart data models indexed by player id. */
	protected DataModel< T >[]	 modelByPlayerIds;
								 
	/**
	 * Creates the {@link #modelByUserIds} array and also populates the {@link #chartList} from the created models.
	 */
	protected void createModelByUserIds() {
		createModelByIds( true );
	}
	
	/**
	 * Creates the {@link #modelByPlayerIds} array and also populates the {@link #chartList} from the created models.
	 */
	protected void createModelByPlayerIds() {
		createModelByIds( false );
	}
	
	/**
	 * Creates either the {@link #modelByUserIds} or {@link #modelByPlayerIds} array based on the parameter and also populates the {@link #chartList} from the
	 * created models.
	 * 
	 * @param byUserId if true, {@link #modelByUserIds} array will be created; else {@link #modelByPlayerIds} array will be created
	 */
	private void createModelByIds( final boolean byUserId ) {
		final boolean[] byIdEnableds = byUserId ? chartsComp.getUserByUserIdEnableds() : chartsComp.getUserByPlayerIdEnableds();
		final boolean teamsAsOne = chartsComp.getTeamsAsOne();
		final boolean allOnOne = chartsComp.getAllOnOneChart();
		
		@SuppressWarnings( "unchecked" )
		final DataModel< T >[] modelByIds = new DataModel[ byIdEnableds.length ];
		if ( byUserId )
			modelByUserIds = modelByIds;
		else
			modelByPlayerIds = modelByIds;
			
		// Only PlayerStatsChartFactory uses byPlayerIds (byUserId=false)
		// In case of Archon mode replay and PlayerStatsChartFactory, only teamsAsOne=true makes sense
		// Also in Archon mode replay tracker events only contain playerId=1 and playerId=2 for the TEAMS, not for player #1 and player #2!
		
		if ( !byUserId && repProc.isArchon() ) {
			
			// Archon mode and PlayerStatsChartFactory: One data model per team
			
			// Remember team order:
			final List< Integer > teamIdList = new ArrayList< >( 2 );
			final StringBuilder[] teamNameBuilders = new StringBuilder[ byIdEnableds.length ];
			final User[] teamLeaders = new User[ byIdEnableds.length ];
			
			// First "gather" teams
			for ( int i = 0; i < repProc.users.length; i++ ) {
				final User u = repProc.users[ i ];
				
				if ( u.player == null )
					continue;
					
				final int team = u.slot.teamId;
				
				// Always need full team list (because the whole team controls 1 player):
				if ( teamNameBuilders[ team ] == null ) {
					teamNameBuilders[ team ] = new StringBuilder( "TEAM " ).append( team + 1 ).append( " <" );
					teamLeaders[ team ] = u.slot.getTandemLeaderUserId() == null ? u : repProc.usersByUserId[ u.slot.getTandemLeaderUserId() ];
				} else
					teamNameBuilders[ team ].append( ", " );
				teamNameBuilders[ team ].append( u.fullName );
				teamNameBuilders[ team ].append( " (" ).append( u.player.race.letter ).append( ')' );
				
				if ( !byIdEnableds[ u.playerId ] )
					continue;
					
				if ( !teamIdList.contains( u.slot.teamId ) )
					teamIdList.add( u.slot.teamId );
			}
			
			// Now process teams (in proper order)
			for ( final int team : teamIdList ) {
				final String title = teamNameBuilders[ team ].append( '>' ).toString();
				final DataModel< T > model = createChartModel( teamLeaders[ team ], title );
				// Set this model to each team member
				for ( int i = 0; i < repProc.users.length; i++ ) {
					final User u = repProc.users[ i ];
					if ( u.player != null && team == u.slot.teamId )
						modelByIds[ u.playerId - 1 ] = model;
				}
				
				final Chart< T > chart;
				if ( allOnOne && !chartList.isEmpty() )
					chart = chartList.get( 0 );
				else
					chartList.add( chart = createChart() );
				chart.addModel( model );
			}
			
		} else if ( teamsAsOne ) {
			
			// One data model per team
			
			// Remember team order:
			final List< Integer > teamIdList = new ArrayList< >( 4 );
			final StringBuilder[] teamNameBuilders = new StringBuilder[ byIdEnableds.length ];
			final User[] teamLeaders = new User[ byIdEnableds.length ];
			
			// First "gather" teams
			for ( int i = 0; i < repProc.users.length; i++ ) {
				final User u = repProc.users[ i ];
				if ( byUserId ) {
					if ( u.slot.userId == null || !byIdEnableds[ u.slot.userId ] )
						continue;
				} else {
					if ( u.player == null || !byIdEnableds[ u.playerId ] )
						continue;
				}
				
				final int team = u.slot.teamId;
				if ( !teamIdList.contains( u.slot.teamId ) ) {
					teamIdList.add( u.slot.teamId );
					teamNameBuilders[ team ] = new StringBuilder( "TEAM " ).append( team + 1 ).append( " <" );
					teamLeaders[ team ] = u;
				} else
					teamNameBuilders[ team ].append( ", " );
				teamNameBuilders[ team ].append( u.fullName );
				if ( u.player != null )
					teamNameBuilders[ team ].append( " (" ).append( u.player.race.letter ).append( ')' );
			}
			
			// Now process teams (in proper order)
			for ( final int team : teamIdList ) {
				final String title = teamNameBuilders[ team ].append( '>' ).toString();
				final DataModel< T > model = createChartModel( teamLeaders[ team ], title );
				// Set this model to each team member
				for ( int i = 0; i < repProc.users.length; i++ ) {
					final User u = repProc.users[ i ];
					if ( byUserId ) {
						if ( u.slot.userId != null && byIdEnableds[ u.slot.userId ] && team == u.slot.teamId )
							modelByIds[ u.slot.userId ] = model;
					} else {
						if ( u.player != null && byIdEnableds[ u.playerId ] && team == u.slot.teamId )
							modelByIds[ u.playerId ] = model;
					}
				}
				
				final Chart< T > chart;
				if ( allOnOne && !chartList.isEmpty() )
					chart = chartList.get( 0 );
				else
					chartList.add( chart = createChart() );
				chart.addModel( model );
			}
			
		} else {
			
			// One data model per user
			
			for ( final User u : repProc.users ) {
				if ( byUserId ) {
					if ( u.slot.userId == null || !byIdEnableds[ u.slot.userId ] )
						continue;
				} else {
					if ( u.player == null || !byIdEnableds[ u.playerId ] )
						continue;
				}
				
				final String title = u.fullName + ( u.player == null ? "" : " (" + u.player.race.letter + ")" );
				final DataModel< T > model = createChartModel( u, title );
				if ( byUserId )
					modelByIds[ u.slot.userId ] = model;
				else
					modelByIds[ u.playerId ] = model;
					
				final Chart< T > chart;
				if ( allOnOne && !chartList.isEmpty() )
					chart = chartList.get( 0 );
				else
					chartList.add( chart = createChart() );
				chart.addModel( model );
			}
			
		}
	}
	
	/**
	 * Creates a new, empty chart.
	 * 
	 * @return the created chart
	 */
	protected abstract Chart< T > createChart();
	
	/**
	 * Creates a new chart data model.
	 * 
	 * @param user user the data model is created for (in case of teams it's the team leader)
	 * @param modelTitle suggested data model title
	 * @return the created chart data model
	 */
	protected abstract DataModel< T > createChartModel( final User user, final String modelTitle );
	
}
