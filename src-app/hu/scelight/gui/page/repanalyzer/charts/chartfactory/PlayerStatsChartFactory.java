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

import hu.scelight.gui.help.Helps;
import hu.scelight.gui.page.repanalyzer.charts.ChartType;
import hu.scelight.gui.page.repanalyzer.charts.ChartsComp;
import hu.scelight.gui.page.repanalyzer.charts.chartscanvas.chart.Chart;
import hu.scelight.gui.page.repanalyzer.charts.chartscanvas.chart.LineTimeChart;
import hu.scelight.gui.page.repanalyzer.charts.chartscanvas.chart.LineTimeChart.Presentation;
import hu.scelight.gui.page.repanalyzer.charts.chartscanvas.chart.model.DataModel;
import hu.scelight.gui.page.repanalyzer.charts.chartscanvas.chart.model.LineChartDataSet;
import hu.scelight.gui.setting.SettingsGui;
import hu.scelight.sc2.rep.model.trackerevents.PlayerStatsEvent;
import hu.scelight.sc2.rep.model.trackerevents.TrackerEvents;
import hu.scelight.sc2.rep.repproc.RepProcessor;
import hu.scelight.sc2.rep.repproc.User;
import hu.scelight.sc2.rep.s2prot.Event;
import hu.scelight.service.env.Env;
import hu.scelight.service.settings.Settings;
import hu.scelightapi.sc2.rep.model.gameevents.IGameEvents;
import hu.scelightapi.sc2.rep.model.trackerevents.ITrackerEvents;
import hu.sllauncher.gui.comp.HelpIcon;
import hu.sllauncher.gui.comp.XCheckBox;
import hu.sllauncher.gui.comp.XLabel;
import hu.sllauncher.gui.comp.XToolBar;
import hu.sllauncher.gui.comp.combobox.XComboBox;
import hu.sllauncher.util.Int;
import hu.sllauncher.util.gui.adapter.ActionAdapter;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * {@link ChartType#PLAYER_STATS} chart factory.
 * 
 * @author Andras Belicza
 */
public class PlayerStatsChartFactory extends BaseChartFactory< LineChartDataSet > {
	
	/**
	 * Player stat.
	 * 
	 * @author Andras Belicza
	 */
	public enum Stat {
		
		/** Resources Current (min+gas). */
		RES_CURRENT( "Resources Current", true, PlayerStatsEvent.F_MINS_CURRENT, PlayerStatsEvent.F_GAS_CURRENT ),
		
		/** Resource Collection Rate (min+gas). */
		RES_COLL_RATE( "Resource Collection Rate", true, PlayerStatsEvent.F_MINS_COLL_RATE, PlayerStatsEvent.F_GAS_COLL_RATE ),
		
		/** Spending Quotient (SQ) (min+gas). */
		SPENDING_QUOTIENT( "Spending Quotient (SQ)", true, "minsDummy", "gasDummy" ),
		
		/** Workers Active. */
		WORKERS_ACTIVE( "Workers Active", false, PlayerStatsEvent.F_WORKERS_ACTIVE_COUNT ),
		
		/** Food Used. */
		FOOD_USED( "Food Used", false, PlayerStatsEvent.F_FOOD_USED ),
		
		/** Food Used. */
		FOOD_MADE( "Food Made", false, PlayerStatsEvent.F_FOOD_MADE ),
		
		/** Food Made+Used. */
		FOOD_MADE_USED( "Food Made+Used", false, "scoreValueFoodDummy" ),
		
		
		/** Army Resources Current. */
		ARMY_RES_CURRENT( "Army Resources Current", true, PlayerStatsEvent.F_MINS_USED_IN_CURRENT_ARMY, PlayerStatsEvent.F_GAS_USED_IN_CURRENT_ARMY ),
		
		/** Army Resources Current. */
		ARMY_RES_LOST( "Army Resources Lost", true, PlayerStatsEvent.F_MINS_LOST_ARMY, PlayerStatsEvent.F_GAS_LOST_ARMY ),
		
		/** Army Resources Killed. */
		ARMY_RES_KILLED( "Army Resources Killed", true, PlayerStatsEvent.F_MINS_KILLED_ARMY, PlayerStatsEvent.F_GAS_KILLED_ARMY ),
		
		/** Army Resources In Progress. */
		ARMY_RES_IN_PROGRESS( "Army Resources In Progress", true, PlayerStatsEvent.F_MINS_USED_IN_PROGRESS_ARMY, PlayerStatsEvent.F_GAS_USED_IN_PROGRESS_ARMY ),
		
		
		/** Economy Resources Current. */
		ECON_RES_CURRENT( "Economy Resources Current", true, PlayerStatsEvent.F_MINS_USED_IN_CURRENT_ECON, PlayerStatsEvent.F_GAS_USED_IN_CURRENT_ECON ),
		
		/** Economy Resources Current. */
		ECON_RES_LOST( "Economy Resources Lost", true, PlayerStatsEvent.F_MINS_LOST_ECON, PlayerStatsEvent.F_GAS_LOST_ECON ),
		
		/** Economy Resources Killed. */
		ECON_RES_KILLED( "Economy Resources Killed", true, PlayerStatsEvent.F_MINS_KILLED_ECON, PlayerStatsEvent.F_GAS_KILLED_ECON ),
		
		/** Economy Resources In Progress. */
		ECON_RES_IN_PROGRESS(
		        "Economy Resources In Progress",
		        true,
		        PlayerStatsEvent.F_MINS_USED_IN_PROGRESS_ECON,
		        PlayerStatsEvent.F_GAS_USED_IN_PROGRESS_ECON ),
		
		
		/** Technology Resources Current. */
		TECH_RES_CURRENT( "Technology Resources Current", true, PlayerStatsEvent.F_MINS_USED_IN_CURRENT_TECH, PlayerStatsEvent.F_GAS_USED_IN_CURRENT_TECH ),
		
		/** Technology Resources Current. */
		TECH_RES_LOST( "Technology Resources Lost", true, PlayerStatsEvent.F_MINS_LOST_TECH, PlayerStatsEvent.F_GAS_LOST_TECH ),
		
		/** Technology Resources Killed. */
		TECH_RES_KILLED( "Technology Resources Killed", true, PlayerStatsEvent.F_MINS_KILLED_TECH, PlayerStatsEvent.F_GAS_KILLED_TECH ),
		
		/** Technology Resources In Progress. */
		TECH_RES_IN_PROGRESS(
		        "Technology Resources In Progress",
		        true,
		        PlayerStatsEvent.F_MINS_USED_IN_PROGRESS_TECH,
		        PlayerStatsEvent.F_GAS_USED_IN_PROGRESS_TECH );
		
		
		
		/** Text value of the player controller. */
		public final String   text;
		
		/** Tells if the stat includes minerals and vespene. */
		public final boolean  hasMinGas;
		
		/**
		 * Event fields associated with this stat.<br>
		 * If stat is minerals and vespene, it contains 2 values for minerals and vespene.
		 */
		public final String[] eventFields;
		
		/**
		 * Creates a new {@link Stat}.
		 * 
		 * @param text text of the presentation
		 * @param hasMinGas tells if the stat includes minerals and vespene
		 * @param eventFields event fields associated with this stat
		 */
		private Stat( final String text, final boolean hasMinGas, final String... eventFields ) {
			this.text = text;
			this.hasMinGas = hasMinGas;
			this.eventFields = eventFields;
			
			if ( Env.DEV_MODE )
				if ( hasMinGas && eventFields.length != 2 )
					throw new RuntimeException( "Stats that has minerals and vespene must have exactly 2 event fields!" );
		}
		
		@Override
		public String toString() {
			return text;
		}
		
		
		/** Cache of the values array. */
		public static final Stat[] VALUES = values();
		
	}
	
	
	/** Combo box to select the presentation. */
	private final XComboBox< Stat >         statComboBox            = SettingsGui.createSettingComboBox( Settings.PLAYER_STATS_STAT, Env.APP_SETTINGS, null );
	
	/** Combo box to select the presentation. */
	private final XComboBox< Presentation > presentationComboBox    = SettingsGui.createSettingComboBox( Settings.PLAYER_STATS_PRESENTATION, Env.APP_SETTINGS,
	                                                                        null );
	
	/** Check box to display / include minerals. */
	private final XCheckBox                 minsCheckBox            = SettingsGui.createSettingCheckBox( Settings.PLAYER_STATS_INCLUDE_MINS, Env.APP_SETTINGS,
	                                                                        null );
	
	/** Check box to display / include vespene. */
	private final XCheckBox                 gasCheckBox             = SettingsGui.createSettingCheckBox( Settings.PLAYER_STATS_INCLUDE_GAS, Env.APP_SETTINGS,
	                                                                        null );
	
	/** Check box to separate minerals and vespene. */
	private final XCheckBox                 separateMinsGasCheckBox = SettingsGui.createSettingCheckBox( Settings.PLAYER_STATS_SEPARATE_MINS_GAS,
	                                                                        Env.APP_SETTINGS, null );
	
	
	/**
	 * Creates a new {@link PlayerStatsChartFactory}.
	 * 
	 * @param chartsComp reference to the charts component that created us
	 */
	public PlayerStatsChartFactory( final ChartsComp chartsComp ) {
		super( chartsComp );
		
		statComboBox.addActionListener( chartsComp.chartsRebuilder );
		statComboBox.addActionListener( new ActionAdapter( true ) {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				final boolean hasMinGas = statComboBox.getSelectedItem().hasMinGas;
				minsCheckBox.setVisible( hasMinGas );
				gasCheckBox.setVisible( hasMinGas );
				separateMinsGasCheckBox.setVisible( hasMinGas );
			}
		} );
		statComboBox.markSeparatedItems( Stat.FOOD_MADE_USED, Stat.ARMY_RES_IN_PROGRESS, Stat.ECON_RES_IN_PROGRESS );
		statComboBox.setMaximumRowCount( statComboBox.getItemCount() );
		
		presentationComboBox.addActionListener( chartsComp.chartsReconfigurer );
		
		minsCheckBox.setText( "Minerals" );
		minsCheckBox.setToolTipText( Settings.PLAYER_STATS_INCLUDE_MINS.name );
		minsCheckBox.addActionListener( chartsComp.chartsRebuilder );
		
		gasCheckBox.setText( "Gas" );
		gasCheckBox.setToolTipText( Settings.PLAYER_STATS_INCLUDE_GAS.name );
		gasCheckBox.addActionListener( chartsComp.chartsRebuilder );
		
		separateMinsGasCheckBox.setText( "Separate Mins/Gas" );
		separateMinsGasCheckBox.setToolTipText( Settings.PLAYER_STATS_SEPARATE_MINS_GAS.name );
		separateMinsGasCheckBox.addActionListener( chartsComp.chartsRebuilder );
	}
	
	@Override
	public void addChartOptions( final XToolBar toolBar ) {
		toolBar.add( new HelpIcon( Helps.PLAYER_STATS_CHART, true ).rightBorder( 5 ) );
		
		toolBar.add( new XLabel( Settings.PLAYER_STATS_STAT.name + ":" ) );
		toolBar.add( statComboBox );
		
		toolBar.add( new XLabel( Settings.PLAYER_STATS_PRESENTATION.name + ":" ) );
		toolBar.add( presentationComboBox );
		
		toolBar.add( minsCheckBox );
		
		toolBar.add( gasCheckBox );
		
		toolBar.add( separateMinsGasCheckBox );
	}
	
	@Override
	public List< Chart< LineChartDataSet > > createCharts() {
		chartList = new ArrayList<>();
		
		final TrackerEvents trackerEvents = repProc.replay.trackerEvents;
		if ( trackerEvents == null ) {
			chartsComp.getChartsCanvas().setMessage(
			        "This chart is available only from replay version 2.0.8. This replay has version " + repProc.replay.header.versionString( false ) + "." );
			return chartList;
		}
		
		createModelByPlayerIds();
		
		switch ( statComboBox.getSelectedItem() ) {
			case SPENDING_QUOTIENT :
				// Spending Quotient is different than the others: it is not stored directly in the tracker events stream
				calculateSQDataSets();
				break;
			case FOOD_MADE_USED :
				// Food Made+Used is showing 2 player stats at once: Food Made and Food Used
				calculateFoodMadeUsedDataSets();
				break;
			default :
				calculateDataSets();
				break;
		}
		
		return chartList;
	}
	
	@Override
	protected LineTimeChart createChart() {
		return new LineTimeChart( repProc );
	}
	
	@Override
	protected DataModel< LineChartDataSet > createChartModel( final User user, final String modelTitle ) {
		final DataModel< LineChartDataSet > model = new DataModel<>( modelTitle, user.getPlayerColor().brighterColor, user );
		
		final Stat stat = statComboBox.getSelectedItem();
		
		int dsCount = 0;
		boolean gasOnly = false;
		if ( stat.hasMinGas ) {
			if ( separateMinsGasCheckBox.isSelected() ) {
				if ( minsCheckBox.isSelected() )
					dsCount++;
				else
					gasOnly = true;
				if ( gasCheckBox.isSelected() )
					dsCount++;
			} else
				dsCount = minsCheckBox.isSelected() || gasCheckBox.isSelected() ? 1 : 0;
		} else
			dsCount = stat.eventFields.length;
		
		for ( int i = 0; i < dsCount; i++ ) {
			// Stroke will be set during reconfiguration
			final LineChartDataSet ds = new LineChartDataSet( i > 0 || gasOnly ? user.getPlayerColor().brighterColor : user.getPlayerColor().color,
			        null, new int[ 0 ], new int[ 0 ] );
			model.addDataSet( ds );
		}
		
		return model;
	}
	
	/**
	 * Calculates the data sets.
	 */
	private void calculateDataSets() {
		calculateDataSets( statComboBox.getSelectedItem() );
	}
	
	/**
	 * Calculates the Spending Quotient data sets.
	 */
	private void calculateSQDataSets() {
		// Spending Quotient is a function of 2 other Stats: SQ = f( RES_CURRENT, RES_COLL_RATE )
		
		// First calculate RES_CURRENT data set...
		calculateDataSets( Stat.RES_CURRENT );
		// ...and clone it because we'll need it in a moment
		final List< Chart< LineChartDataSet > > clonedChartList = new ArrayList<>( chartList.size() );
		for ( final Chart< LineChartDataSet > chart : chartList ) {
			final Chart< LineChartDataSet > clonedChart = createChart();
			
			for ( final DataModel< LineChartDataSet > model : chart.getDataModelList() ) {
				final DataModel< LineChartDataSet > clonedModel = new DataModel<>( null, null );
				
				for ( final LineChartDataSet dataSet : model.getDataSetList() )
					clonedModel.addDataSet( new LineChartDataSet( null, null, dataSet.getLoops(), dataSet.getValues() ) );
				
				clonedChart.addModel( clonedModel );
			}
			
			clonedChartList.add( clonedChart );
		}
		
		// Cloning done, let's roll on to calculating the RES_COLL_RATE data set...
		calculateDataSets( Stat.RES_COLL_RATE );
		
		// We'll need the last command game event loops indexed by data model (last cmd loop is the same for all data sets in a
		// model)
		final Map< DataModel< LineChartDataSet >, Int > modelLastCmdLoopsMap = new HashMap<>();
		for ( final Event event : repProc.replay.gameEvents.events ) {
			if ( event.id != IGameEvents.ID_CMD )
				continue;
			
			// Player stats go by tracker events which go by player id. First check if there is player for the user!
			final int playerId = repProc.usersByUserId[ event.userId ].playerId;
			if ( playerId <= 0 )
				continue;
			
			final DataModel< LineChartDataSet > model = modelByPlayerIds[ playerId ];
			if ( model == null )
				continue;
			
			// Update last command loop per model
			Int lastCmdLoop = modelLastCmdLoopsMap.get( model );
			if ( lastCmdLoop == null )
				modelLastCmdLoopsMap.put( model, lastCmdLoop = new Int() );
			lastCmdLoop.value = event.loop;
		}
		
		// And the final step: "merge" the 2 data sets, the result being the SQ
		for ( int chartIdx = 0; chartIdx < chartList.size(); chartIdx++ ) {
			final Chart< LineChartDataSet > chart = chartList.get( chartIdx );
			final Chart< LineChartDataSet > clonedChart = clonedChartList.get( chartIdx );
			
			for ( int modelIdx = 0; modelIdx < chart.getDataModelList().size(); modelIdx++ ) {
				final DataModel< LineChartDataSet > model = chart.getDataModelList().get( modelIdx );
				final DataModel< LineChartDataSet > clonedModel = clonedChart.getDataModelList().get( modelIdx );
				
				final int lastCmdLoop = modelLastCmdLoopsMap.get( model ) == null ? 0 : modelLastCmdLoopsMap.get( model ).value;
				
				for ( int dataSetIdx = 0; dataSetIdx < model.getDataSetList().size(); dataSetIdx++ ) {
					final LineChartDataSet dataSet = model.getDataSetList().get( dataSetIdx );
					final LineChartDataSet clonedDataSet = clonedModel.getDataSetList().get( dataSetIdx );
					
					// Only include values in total before the last command loop! And of course only count samples up to that.
					
					final int[] loops = dataSet.getLoops();
					final int[] values = dataSet.getValues();
					final int[] clonedValues = clonedDataSet.getValues();
					
					long totalValues = 0, totalClonedValues = 0;
					int totalCount = 0;
					
					for ( int valueIdx = 0; valueIdx < values.length; valueIdx++ ) {
						if ( loops[ valueIdx ] <= lastCmdLoop ) {
							totalClonedValues += clonedValues[ valueIdx ];
							totalValues += values[ valueIdx ];
							totalCount++;
						}
						values[ valueIdx ] = RepProcessor.calculateSQImpl( clonedValues[ valueIdx ], values[ valueIdx ] );
					}
					
					// Calculate average SQ for the data set, put in data set title!
					final int avgSQ = totalCount == 0 ? 0 : RepProcessor.calculateSQImpl( (int) ( totalClonedValues / totalCount ),
					        (int) ( totalValues / totalCount ) );
					dataSet.setTitle( "SQ: " + avgSQ );
					dataSet.calculateValueMax();
				}
			}
		}
	}
	
	/**
	 * Calculates the Food Made+Used data sets.
	 */
	private void calculateFoodMadeUsedDataSets() {
		// Food Made+Used is showing 2 player stats at once: Food Made and Food Used
		
		// First calculate FOOD_USED data set...
		calculateDataSets( Stat.FOOD_USED );
		// ...and clone it because we'll need it in a moment
		final List< Chart< LineChartDataSet > > clonedChartList = new ArrayList<>( chartList.size() );
		for ( final Chart< LineChartDataSet > chart : chartList ) {
			final Chart< LineChartDataSet > clonedChart = createChart();
			
			for ( final DataModel< LineChartDataSet > model : chart.getDataModelList() ) {
				final DataModel< LineChartDataSet > clonedModel = new DataModel<>( model.getTitle(), model.getColor() );
				
				for ( final LineChartDataSet dataSet : model.getDataSetList() ) {
					// Use brighter color as this will be the 2nd dataset
					final Color color = ( (User) model.getUserobObject() ).getPlayerColor().brighterColor;
					clonedModel.addDataSet( new LineChartDataSet( color, dataSet.getStroke(), dataSet.getLoops(), dataSet.getValues() ) );
				}
				
				clonedChart.addModel( clonedModel );
			}
			
			clonedChartList.add( clonedChart );
		}
		
		// Cloning done, let's roll on to calculating the FOOD_MADE data set...
		calculateDataSets( Stat.FOOD_MADE );
		
		// And the final step: "merge" the 2 data sets into one, simply add the datasets...
		for ( int chartIdx = 0; chartIdx < chartList.size(); chartIdx++ ) {
			final Chart< LineChartDataSet > chart = chartList.get( chartIdx );
			final Chart< LineChartDataSet > clonedChart = clonedChartList.get( chartIdx );
			
			for ( int modelIdx = 0; modelIdx < chart.getDataModelList().size(); modelIdx++ ) {
				final DataModel< LineChartDataSet > model = chart.getDataModelList().get( modelIdx );
				final DataModel< LineChartDataSet > clonedModel = clonedChart.getDataModelList().get( modelIdx );
				
				model.getDataSetList().addAll( clonedModel.getDataSetList() );
			}
		}
	}
	
	/**
	 * Calculates the data sets for the specified {@link Stat}.
	 * 
	 * @param stat stat to calculate the data sets for
	 */
	private void calculateDataSets( final Stat stat ) {
		// Data points for each data set, inside being mapped from loop to value.
		final Map< LineChartDataSet, Map< Integer, Int > > dataSetPointsMap = new HashMap<>();
		
		final boolean mergeMinGas = stat.hasMinGas && !separateMinsGasCheckBox.isSelected();
		
		final String[] eventFields; // Event fields to query eventually
		if ( stat.hasMinGas ) {
			final boolean includeMins = minsCheckBox.isSelected();
			final boolean includeGas = gasCheckBox.isSelected();
			if ( includeMins && includeGas )
				eventFields = stat.eventFields;
			else if ( includeMins )
				eventFields = new String[] { stat.eventFields[ 0 ] };
			else if ( includeGas )
				eventFields = new String[] { stat.eventFields[ 1 ] };
			else
				eventFields = new String[ 0 ];
		} else
			eventFields = stat.eventFields;
		
		// Calculate data points maps
		for ( final Event event : repProc.replay.trackerEvents.events ) {
			if ( event.id != ITrackerEvents.ID_PLAYER_STATS )
				continue;
			
			final DataModel< LineChartDataSet > model = modelByPlayerIds[ event.getPlayerId() ];
			if ( model == null )
				continue;
			
			// One extra player stat event is recorded when a user leaves (at the same loop) even if at the same loop there was a
			// scheduled player stat event. So we have to exclude the last player stat event (to avoid the chart getting
			// "screwed"). (This for example contains mineralsCurrent = 0.)
			// Note: if there's a scheduled stat event when the player left, this will be excluded wrongly,
			// but I don't care, it won't make any noticable difference...
			if ( event.loop == repProc.usersByPlayerId[ event.getPlayerId() ].leaveLoop )
				continue;
			
			// Data sets per event fields
			for ( int i = 0; i < eventFields.length; i++ ) {
				final String field = eventFields[ i ];
				final LineChartDataSet dataSet = model.getDataSetList().get( mergeMinGas ? 0 : i );
				
				Map< Integer, Int > pointsMap = dataSetPointsMap.get( dataSet );
				if ( pointsMap == null )
					dataSetPointsMap.put( dataSet, pointsMap = new HashMap<>() );
				final Integer loop = Integer.valueOf( event.loop );
				Int point = pointsMap.get( loop );
				if ( point == null ) {
					pointsMap.put( loop, point = new Int() );
				}
				int value = ( (PlayerStatsEvent) event ).stats.< Integer > get( field );
				if ( field == PlayerStatsEvent.F_FOOD_MADE || field == PlayerStatsEvent.F_FOOD_USED )
					value >>= 12;
				point.value += value;
			}
		}
		
		// Convert data points maps to arrays
		for ( final Chart< LineChartDataSet > chart : chartList ) {
			for ( final DataModel< LineChartDataSet > model : chart.getDataModelList() ) {
				for ( final LineChartDataSet dataSet : model.getDataSetList() ) {
					final Map< Integer, Int > pointsMap = dataSetPointsMap.get( dataSet );
					if ( pointsMap == null )
						continue;
					
					// Data points are needed sorted by loop
					final List< Entry< Integer, Int > > dataPointList = new ArrayList<>( pointsMap.entrySet() );
					Collections.sort( dataPointList, new Comparator< Entry< Integer, Int > >() {
						@Override
						public int compare( Entry< Integer, Int > o1, Entry< Integer, Int > o2 ) {
							return o1.getKey().compareTo( o2.getKey() );
						}
					} );
					
					int i = 0;
					final int[] loops = new int[ pointsMap.size() ];
					final int[] values = new int[ pointsMap.size() ];
					for ( final Entry< Integer, Int > dataPoint : dataPointList ) {
						loops[ i ] = dataPoint.getKey();
						values[ i ] = dataPoint.getValue().value;
						i++;
					}
					
					dataSet.setLoops( loops );
					dataSet.setValues( values );
					dataSet.calculateValueMax();
				}
			}
		}
	}
	
	@Override
	public void reconfigureChartCanvas() {
		final boolean gasOnly = statComboBox.getSelectedItem().hasMinGas && separateMinsGasCheckBox.isSelected() && !minsCheckBox.isSelected();
		
		final Presentation p = presentationComboBox.getSelectedItem();
		
		for ( final Chart< ? > chart : chartsComp.getChartsCanvas().getChartList() ) {
			if ( chart instanceof LineTimeChart ) {
				final LineTimeChart lc = (LineTimeChart) chart;
				lc.setPresentation( p );
				
				// Stroke depends on the presentation:
				for ( final DataModel< LineChartDataSet > model : lc.getDataModelList() ) {
					int i = 0;
					for ( final LineChartDataSet dataSet : model.getDataSetList() )
						dataSet.setStroke( i++ > 0 || gasOnly ? p.storke : p.storkeDouble );
				}
			}
		}
	}
	
}
