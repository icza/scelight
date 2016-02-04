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
import hu.scelight.gui.page.repanalyzer.charts.chartscanvas.chart.ControlGroupsChart;
import hu.scelight.gui.page.repanalyzer.charts.chartscanvas.chart.model.ControlGroupChartDataSet;
import hu.scelight.gui.page.repanalyzer.charts.chartscanvas.chart.model.DataModel;
import hu.scelight.gui.setting.SettingsGui;
import hu.scelight.sc2.rep.model.gameevents.ControlGroupUpdateEvent;
import hu.scelight.sc2.rep.repproc.User;
import hu.scelight.sc2.rep.s2prot.Event;
import hu.scelight.service.env.Env;
import hu.scelight.service.settings.Settings;
import hu.scelightapi.sc2.rep.model.gameevents.IGameEvents;
import hu.sllauncher.gui.comp.HelpIcon;
import hu.sllauncher.gui.comp.XCheckBox;
import hu.sllauncher.gui.comp.XToolBar;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link ChartType#CONTROL_GROUPS} chart factory.
 * 
 * @author Andras Belicza
 */
public class ControlGroupsChartFactory extends BaseChartFactory< ControlGroupChartDataSet > {
	
	/** Check box to show select groups. */
	private final XCheckBox showSelectCheckBox = SettingsGui.createSettingCheckBox( Settings.CONTROL_GROUPS_SHOW_SELECT, Env.APP_SETTINGS, null );
	
	/**
	 * Creates a new {@link ControlGroupsChartFactory}.
	 * 
	 * @param chartsComp reference to the charts component that created us
	 */
	public ControlGroupsChartFactory( final ChartsComp chartsComp ) {
		super( chartsComp );
		
		showSelectCheckBox.addActionListener( chartsComp.chartsRebuilder );
	}
	
	@Override
	public void addChartOptions( final XToolBar toolBar ) {
		toolBar.add( new HelpIcon( Helps.CONTROL_GROUPS_CHART, true ).rightBorder( 5 ) );
		
		toolBar.add( showSelectCheckBox );
	}
	
	@Override
	public List< Chart< ControlGroupChartDataSet > > createCharts() {
		chartList = new ArrayList<>();
		
		createModelByUserIds();
		
		calculateDataSets();
		
		return chartList;
	}
	
	@Override
	protected ControlGroupsChart createChart() {
		return new ControlGroupsChart( repProc );
	}
	
	@Override
	protected DataModel< ControlGroupChartDataSet > createChartModel( final User user, final String modelTitle ) {
		final DataModel< ControlGroupChartDataSet > model = new DataModel<>( modelTitle, user.getPlayerColor().brighterColor );
		
		// Control Group Assign data set
		final ControlGroupChartDataSet acgds = new ControlGroupChartDataSet( user.getPlayerColor().color );
		model.addDataSet( acgds );
		
		// Control Group Select data set
		if ( showSelectCheckBox.isSelected() ) {
			final ControlGroupChartDataSet scgds = new ControlGroupChartDataSet( user.getPlayerColor().brighterColor );
			model.addDataSet( scgds );
		}
		
		return model;
	}
	
	/**
	 * Calculates the data sets.
	 */
	private void calculateDataSets() {
		final boolean showSelect = showSelectCheckBox.isSelected();
		
		// Calculate data sets
		for ( final Event event : repProc.replay.gameEvents.events ) {
			if ( event.id != IGameEvents.ID_CONTROL_GROUP_UPDATE )
				continue;
			
			final DataModel< ControlGroupChartDataSet > model = modelByUserIds[ event.userId ];
			if ( model == null )
				continue;
			
			final ControlGroupUpdateEvent cgue = (ControlGroupUpdateEvent) event;
			if ( cgue.isAssign() )
				model.getDataSetList().get( 0 ).controlGroupEventLists[ cgue.getGroupIndex() ].add( cgue );
			else if ( showSelect )
				model.getDataSetList().get( 1 ).controlGroupEventLists[ cgue.getGroupIndex() ].add( cgue );
		}
		
		// Set data set titles
		for ( final Chart< ControlGroupChartDataSet > chart : chartList ) {
			for ( final DataModel< ControlGroupChartDataSet > model : chart.getDataModelList() ) {
				int i = 0;
				for ( final ControlGroupChartDataSet dataSet : model.getDataSetList() ) {
					int count = 0;
					for ( final List< ControlGroupUpdateEvent > list : dataSet.controlGroupEventLists )
						count += list.size();
					
					dataSet.setTitle( ( i == 0 ? "Assign: " : "Select: " ) + count );
					i++;
				}
			}
		}
	}
	
}
