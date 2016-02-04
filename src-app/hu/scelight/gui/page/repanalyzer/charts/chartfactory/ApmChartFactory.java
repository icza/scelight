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
import hu.scelight.sc2.rep.repproc.RepProcessor;
import hu.scelight.sc2.rep.repproc.User;
import hu.scelight.sc2.rep.s2prot.Event;
import hu.scelight.service.env.Env;
import hu.scelight.service.settings.Settings;
import hu.scelightapi.sc2.rep.model.gameevents.IGameEvents;
import hu.sllauncher.gui.comp.HelpIcon;
import hu.sllauncher.gui.comp.XLabel;
import hu.sllauncher.gui.comp.XSpinner;
import hu.sllauncher.gui.comp.XToolBar;
import hu.sllauncher.gui.comp.combobox.XComboBox;
import hu.sllauncher.util.Int;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * {@link ChartType#APM} chart factory.
 * 
 * @author Andras Belicza
 */
public class ApmChartFactory extends BaseChartFactory< LineChartDataSet > {
	
	/** Combo box to select the presentation. */
	private final XComboBox< Presentation > presentationComboBox = SettingsGui.createSettingComboBox( Settings.APM_PRESENTATION, Env.APP_SETTINGS, null );
	
	/** Spinner to specify granularity. */
	private final XSpinner                  granularitySpinner   = SettingsGui.createSettingSpinner( Settings.APM_GRANULARITY, Env.APP_SETTINGS, null );
	
	
	/**
	 * Creates a new {@link ApmChartFactory}.
	 * 
	 * @param chartsComp reference to the charts component that created us
	 */
	public ApmChartFactory( final ChartsComp chartsComp ) {
		super( chartsComp );
		
		presentationComboBox.addActionListener( chartsComp.chartsReconfigurer );
		granularitySpinner.addChangeListener( new ChangeListener() {
			@Override
			public void stateChanged( final ChangeEvent event ) {
				chartsComp.chartsRebuilder.actionPerformed( null );
			}
		} );
	}
	
	@Override
	public void addChartOptions( final XToolBar toolBar ) {
		toolBar.add( new HelpIcon( Helps.APM_CHART, true ).rightBorder( 5 ) );
		
		toolBar.add( new XLabel( Settings.APM_PRESENTATION.name + ":" ) );
		toolBar.add( presentationComboBox );
		
		toolBar.add( new XLabel( Settings.APM_GRANULARITY.name + ":" ) );
		toolBar.add( granularitySpinner );
		toolBar.add( new XLabel( Settings.APM_GRANULARITY.viewHints.getSubsequentText() ) );
	}
	
	/** Granularity in seconds. */
	private int   granularitySec;
	
	/** Granularity in loops (loop = sec * 16). */
	private int   granularity;
	
	/** Granularity of the last segment in loops which might be smaller than the others. */
	private int   lastGranularity;
	
	/** Number of data points. */
	private int   dataPoints;
	
	/** Shared loops array for all data sets. */
	private int[] loops;
	
	
	@Override
	public List< Chart< LineChartDataSet > > createCharts() {
		chartList = new ArrayList<>();
		
		// Preparation
		granularitySec = ( (Number) granularitySpinner.getValue() ).intValue();
		// Granularity in loop (loop = sec * 16)
		granularity = granularitySec * 16;
		
		lastGranularity = repProc.replay.header.getElapsedGameLoops() % granularity;
		if ( lastGranularity == 0 ) // Last segment is not smaller
			lastGranularity = granularity;
		
		dataPoints = repProc.replay.header.getElapsedGameLoops() / granularity + ( lastGranularity == granularity ? 0 : 1 );
		
		loops = new int[ dataPoints ];
		// Loop values are the center of the loop segments
		for ( int i = 0, loop = granularity / 2; i < loops.length; i++, loop += granularity )
			loops[ i ] = loop;
		// Length of last segment might be different (smaller)
		if ( lastGranularity != granularity && loops.length > 1 )
			loops[ loops.length - 1 ] = loops[ loops.length - 2 ] + ( granularity + lastGranularity ) / 2;
		
		createModelByUserIds();
		
		calculateDataSets();
		
		return chartList;
	}
	
	@Override
	protected LineTimeChart createChart() {
		return new LineTimeChart( repProc );
	}
	
	@Override
	protected DataModel< LineChartDataSet > createChartModel( final User user, final String modelTitle ) {
		final DataModel< LineChartDataSet > model = new DataModel<>( modelTitle, user.getPlayerColor().brighterColor );
		
		// (Normal) APM data set
		// Stroke will be set during reconfiguration
		final LineChartDataSet apmDs = new LineChartDataSet( user.getPlayerColor().color, null, loops, new int[ dataPoints ] );
		model.addDataSet( apmDs );
		
		// TODO other data sets (e.g. EAPM, micro APM, XAPM)
		
		return model;
	}
	
	/**
	 * Calculates the data sets.
	 */
	private void calculateDataSets() {
		// Initial time to exclude from APM calculation!
		final int initialPerMinCalcExclLoops = repProc.initialPerMinCalcExclTime * 16;
		
		// Initial excluded actions counts indexed by data set
		final Map< LineChartDataSet, Int > dataSetExcludedActionCountsMap = new HashMap<>();
		
		// Last command game event loops indexed by data model (last cmd loop is the same for all data sets in a model)
		final Map< DataModel< LineChartDataSet >, Int > modelLastCmdLoopsMap = new HashMap<>();
		
		
		// Calculate data sets
		for ( final Event event : repProc.replay.gameEvents.events ) {
			final DataModel< LineChartDataSet > model = modelByUserIds[ event.userId ];
			if ( model == null )
				continue;
			
			// Update last command loop per model
			if ( event.id == IGameEvents.ID_CMD ) {
				Int lastCmdLoop = modelLastCmdLoopsMap.get( model );
				if ( lastCmdLoop == null )
					modelLastCmdLoopsMap.put( model, lastCmdLoop = new Int() );
				lastCmdLoop.value = event.loop;
			}
			
			// APM data set
			if ( RepProcessor.APM_EVENT_ID_SET.contains( event.id ) ) {
				final LineChartDataSet dataSet = model.getDataSetList().get( 0 );
				dataSet.getValues()[ event.loop / granularity ]++;
				if ( event.loop < initialPerMinCalcExclLoops || event.loop > repProc.usersByUserId[ event.userId ].lastCmdLoop ) {
					Int excludedCount = dataSetExcludedActionCountsMap.get( dataSet );
					if ( excludedCount == null )
						dataSetExcludedActionCountsMap.put( dataSet, excludedCount = new Int() );
					excludedCount.value++;
				}
			}
		}
		
		// Data sets only contain actions count, divide by time to get APM values
		for ( final Chart< LineChartDataSet > chart : chartList ) {
			for ( final DataModel< LineChartDataSet > model : chart.getDataModelList() ) {
				final Int lastCmdLoop = modelLastCmdLoopsMap.get( model );
				
				for ( final LineChartDataSet dataSet : model.getDataSetList() ) {
					// Convert action counts to APM values, calculate average APM
					long total = 0;
					final Int excludedCount = dataSetExcludedActionCountsMap.get( dataSet );
					final int[] values = dataSet.getValues();
					
					// The last data segment might be shorter than the others, have to divide with a different time value:
					if ( values.length > 0 ) {
						total += values[ values.length - 1 ];
						values[ values.length - 1 ] = repProc.calculatePerMinute( values[ values.length - 1 ], lastGranularity );
					}
					
					// All other data segments:
					for ( int i = values.length - 2; i >= 0; i-- ) {
						total += values[ i ];
						values[ i ] = repProc.calculatePerMinute( values[ i ], granularity );
					}
					
					final int apmLoops = lastCmdLoop == null ? 0 : lastCmdLoop.value - initialPerMinCalcExclLoops;
					final long apmActions = excludedCount == null ? total : total - excludedCount.value;
					final int avgApm = repProc.calculatePerMinute( apmActions, apmLoops );
					dataSet.setTitle( "Actions: " + total + ",    APM: " + avgApm );
					dataSet.calculateValueMax();
				}
			}
		}
	}
	
	@Override
	public void reconfigureChartCanvas() {
		for ( final Chart< ? > chart : chartsComp.getChartsCanvas().getChartList() ) {
			if ( !( chart instanceof LineTimeChart ) )
				continue;
			
			final LineTimeChart lc = (LineTimeChart) chart;
			
			final Presentation p = presentationComboBox.getSelectedItem();
			lc.setPresentation( p );
			
			// Stroke depends on the presentation:
			for ( final DataModel< LineChartDataSet > model : lc.getDataModelList() ) {
				int i = 0;
				for ( final LineChartDataSet dataSet : model.getDataSetList() )
					dataSet.setStroke( i++ == 0 ? p.storkeDouble : p.storke );
			}
		}
	}
	
}
