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
import hu.scelight.gui.page.repanalyzer.charts.chartscanvas.chart.commands.CommandsChart;
import hu.scelight.gui.page.repanalyzer.charts.chartscanvas.chart.commands.DurationType;
import hu.scelight.gui.page.repanalyzer.charts.chartscanvas.chart.model.CommandsChartDataSet;
import hu.scelight.gui.page.repanalyzer.charts.chartscanvas.chart.model.DataModel;
import hu.scelight.gui.setting.SettingsGui;
import hu.scelight.sc2.balancedata.model.BuildCommand;
import hu.scelight.sc2.balancedata.model.TrainCommand;
import hu.scelight.sc2.balancedata.model.UpgradeCommand;
import hu.scelight.sc2.rep.model.gameevents.cmd.CmdEvent;
import hu.scelight.sc2.rep.repproc.User;
import hu.scelight.sc2.rep.s2prot.Event;
import hu.scelight.service.env.Env;
import hu.scelight.service.settings.Settings;
import hu.scelightapi.sc2.rep.model.gameevents.IGameEvents;
import hu.sllauncher.gui.comp.HelpIcon;
import hu.sllauncher.gui.comp.XCheckBox;
import hu.sllauncher.gui.comp.XLabel;
import hu.sllauncher.gui.comp.XSpinner;
import hu.sllauncher.gui.comp.XToolBar;
import hu.sllauncher.gui.comp.combobox.XComboBox;
import hu.sllauncher.util.gui.adapter.ActionAdapter;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * {@link ChartType#COMMANDS} chart factory.
 * 
 * @author Andras Belicza
 */
public class CommandsChartFactory extends BaseChartFactory< CommandsChartDataSet > {
	
	/** Check box to show build commands. */
	private final XCheckBox                 showBuildCmdsCheckBox          = SettingsGui.createSettingCheckBox( Settings.COMMANDS_SHOW_BUILDS,
	                                                                               Env.APP_SETTINGS, null );
	
	/** Check box to show train commands. */
	private final XCheckBox                 showTrainCmdsCheckBox          = SettingsGui.createSettingCheckBox( Settings.COMMANDS_SHOW_TRAINS,
	                                                                               Env.APP_SETTINGS, null );
	
	/** Check box to show worker train commands. */
	private final XCheckBox                 showWorkerCmdsCheckBox         = SettingsGui.createSettingCheckBox( Settings.COMMANDS_SHOW_WORKERS,
	                                                                               Env.APP_SETTINGS, null );
	
	/** Check box to show upgrade commands. */
	private final XCheckBox                 showUpgradeCmdsCheckBox        = SettingsGui.createSettingCheckBox( Settings.COMMANDS_SHOW_UPGRADES,
	                                                                               Env.APP_SETTINGS, null );
	
	/** Check box to show other commands (essential). */
	private final XCheckBox                 showOtherEssentialCmdsCheckBox = SettingsGui.createSettingCheckBox( Settings.COMMANDS_SHOW_OTHERS_ESSENTIAL,
	                                                                               Env.APP_SETTINGS, null );
	
	/** Check box to show other commands (rest). */
	private final XCheckBox                 showOtherRestCmdsCheckBox      = SettingsGui.createSettingCheckBox( Settings.COMMANDS_SHOW_OTHERS_REST,
	                                                                               Env.APP_SETTINGS, null );
	
	/** Check box to control use of icons. */
	private final XCheckBox                 useIconsCheckBox               = SettingsGui.createSettingCheckBox( Settings.COMMANDS_USE_ICONS, Env.APP_SETTINGS,
	                                                                               null );
	
	/** Spinner to specify icon size. */
	private final XSpinner                  iconSizeSpinner                = SettingsGui.createSettingSpinner( Settings.COMMANDS_ICON_SIZE, Env.APP_SETTINGS,
	                                                                               null );
	
	/** Icon size subsequent label. */
	private final XLabel                    iconSizeSsLabel                = new XLabel( Settings.COMMANDS_ICON_SIZE.viewHints.getSubsequentText() );
	
	/** Combo box to select the duration visualization type. */
	private final XComboBox< DurationType > durationTypeComboBox           = SettingsGui.createSettingComboBox( Settings.COMMANDS_DURATION_TYPE,
	                                                                               Env.APP_SETTINGS, null );
	
	
	
	/**
	 * Creates a new {@link CommandsChartFactory}.
	 * 
	 * @param chartsComp reference to the charts component that created us
	 */
	public CommandsChartFactory( final ChartsComp chartsComp ) {
		super( chartsComp );
		
		showBuildCmdsCheckBox.setText( "Builds" );
		showBuildCmdsCheckBox.addActionListener( chartsComp.chartsRebuilder );
		showTrainCmdsCheckBox.setText( "Trains" );
		showTrainCmdsCheckBox.addActionListener( chartsComp.chartsRebuilder );
		showTrainCmdsCheckBox.addActionListener( new ActionAdapter( true ) {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				showWorkerCmdsCheckBox.setEnabled( showTrainCmdsCheckBox.isSelected() );
			}
		} );
		showWorkerCmdsCheckBox.setText( "Workers" );
		showWorkerCmdsCheckBox.addActionListener( chartsComp.chartsRebuilder );
		showUpgradeCmdsCheckBox.setText( "Upgrades" );
		showUpgradeCmdsCheckBox.addActionListener( chartsComp.chartsRebuilder );
		showOtherEssentialCmdsCheckBox.setText( "Others (Essential)" );
		showOtherEssentialCmdsCheckBox.addActionListener( chartsComp.chartsRebuilder );
		showOtherRestCmdsCheckBox.setText( "Others (Rest)" );
		showOtherRestCmdsCheckBox.addActionListener( chartsComp.chartsRebuilder );
		
		useIconsCheckBox.setText( useIconsCheckBox.getText() + ":" );
		useIconsCheckBox.addActionListener( chartsComp.chartsReconfigurer );
		useIconsCheckBox.addActionListener( new ActionAdapter( true ) {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				iconSizeSpinner.setEnabled( useIconsCheckBox.isSelected() );
				iconSizeSsLabel.setEnabled( useIconsCheckBox.isSelected() );
			}
		} );
		iconSizeSpinner.addChangeListener( new ChangeListener() {
			@Override
			public void stateChanged( final ChangeEvent event ) {
				chartsComp.chartsReconfigurer.actionPerformed( null );
			}
		} );
		
		durationTypeComboBox.addActionListener( chartsComp.chartsReconfigurer );
	}
	
	@Override
	public void addChartOptions( final XToolBar toolBar ) {
		toolBar.add( new HelpIcon( Helps.COMMANDS_CHART, true ).rightBorder( 5 ) );
		
		toolBar.add( showBuildCmdsCheckBox );
		toolBar.add( showTrainCmdsCheckBox );
		toolBar.add( showWorkerCmdsCheckBox );
		toolBar.add( showUpgradeCmdsCheckBox );
		toolBar.add( showOtherEssentialCmdsCheckBox );
		toolBar.add( showOtherRestCmdsCheckBox );
		
		toolBar.addSeparator();
		toolBar.add( useIconsCheckBox );
		toolBar.add( iconSizeSpinner );
		toolBar.add( iconSizeSsLabel );
		
		toolBar.addSeparator();
		toolBar.add( new XLabel( "Duration:" ) );
		toolBar.add( durationTypeComboBox );
	}
	
	@Override
	public List< Chart< CommandsChartDataSet > > createCharts() {
		chartList = new ArrayList<>();
		
		createModelByUserIds();
		
		calculateDataSets();
		
		return chartList;
	}
	
	@Override
	protected CommandsChart createChart() {
		return new CommandsChart( repProc );
	}
	
	@Override
	protected DataModel< CommandsChartDataSet > createChartModel( final User user, final String modelTitle ) {
		final DataModel< CommandsChartDataSet > model = new DataModel<>( modelTitle, user.getPlayerColor().brighterColor );
		
		// One data set per model only: all types of commands are presented the same way
		final CommandsChartDataSet ds = new CommandsChartDataSet( user.getPlayerColor().color );
		model.addDataSet( ds );
		
		return model;
	}
	
	/**
	 * Calculates the data sets.
	 */
	private void calculateDataSets() {
		final boolean showBuilds = showBuildCmdsCheckBox.isSelected();
		final boolean showTrains = showTrainCmdsCheckBox.isSelected();
		final boolean showWorkers = showWorkerCmdsCheckBox.isSelected();
		final boolean showUpgrades = showUpgradeCmdsCheckBox.isSelected();
		final boolean showOthersEssential = showOtherEssentialCmdsCheckBox.isSelected();
		final boolean showOthersRest = showOtherRestCmdsCheckBox.isSelected();
		
		// Calculate data sets
		for ( final Event event : repProc.replay.gameEvents.events ) {
			if ( event.id != IGameEvents.ID_CMD )
				continue;
			
			final DataModel< CommandsChartDataSet > model = modelByUserIds[ event.userId ];
			if ( model == null )
				continue;
			
			final CmdEvent ce = (CmdEvent) event;
			if ( ce.command == null )
				continue;
			
			if ( ce.command instanceof TrainCommand ) {
				if ( showTrains )
					if ( showWorkers || !( (TrainCommand) ce.command ).isWorker() )
						model.getDataSetList().get( 0 ).cmdEventLists.add( ce );
			} else if ( ce.command instanceof BuildCommand ) {
				if ( showBuilds )
					model.getDataSetList().get( 0 ).cmdEventLists.add( ce );
			} else if ( ce.command instanceof UpgradeCommand ) {
				if ( showUpgrades )
					model.getDataSetList().get( 0 ).cmdEventLists.add( ce );
			} else {
				if ( ce.command.isEssential() ) {
					if ( showOthersEssential )
						model.getDataSetList().get( 0 ).cmdEventLists.add( ce );
				} else {
					if ( showOthersRest )
						model.getDataSetList().get( 0 ).cmdEventLists.add( ce );
				}
			}
		}
		
		// Set data set titles
		for ( final Chart< CommandsChartDataSet > chart : chartList ) {
			for ( final DataModel< CommandsChartDataSet > model : chart.getDataModelList() ) {
				// One data set only
				final CommandsChartDataSet ds = model.getDataSetList().get( 0 );
				
				int builds = 0;
				int trains = 0;
				int workers = 0;
				int upgrades = 0;
				int othersEssential = 0;
				int othersRest = 0;
				for ( final CmdEvent ce : ds.cmdEventLists )
					if ( ce.command instanceof TrainCommand ) {
						trains++;
						if ( showWorkers && ( (TrainCommand) ce.command ).isWorker() )
							workers++;
					} else if ( ce.command instanceof BuildCommand )
						builds++;
					else if ( ce.command instanceof UpgradeCommand )
						upgrades++;
					else {
						if ( ce.command.isEssential() )
							othersEssential++;
						else
							othersRest++;
					}
				
				final StringBuilder sb = new StringBuilder();
				if ( showBuilds )
					sb.append( "Builds: " ).append( builds );
				if ( showTrains ) {
					if ( sb.length() > 0 )
						sb.append( ",    " );
					sb.append( "Trains: " ).append( trains );
					if ( showWorkers )
						sb.append( " (" ).append( "Workers: " ).append( workers ).append( ")" );
				}
				if ( showUpgrades ) {
					if ( sb.length() > 0 )
						sb.append( ",    " );
					sb.append( "Upgrades: " ).append( upgrades );
				}
				if ( showOthersEssential ) {
					if ( sb.length() > 0 )
						sb.append( ",    " );
					sb.append( "Others (Essential): " ).append( othersEssential );
				}
				if ( showOthersRest ) {
					if ( sb.length() > 0 )
						sb.append( ",    " );
					sb.append( "Others (Rest): " ).append( othersRest );
				}
				
				if ( sb.length() > 0 )
					ds.setTitle( sb.toString() );
			}
		}
	}
	
	@Override
	public void reconfigureChartCanvas() {
		for ( final Chart< ? > chart : chartsComp.getChartsCanvas().getChartList() ) {
			if ( !( chart instanceof CommandsChart ) )
				continue;
			
			final CommandsChart cc = (CommandsChart) chart;
			
			cc.setIconSize( (Integer) iconSizeSpinner.getValue() );
			cc.setUseIcons( useIconsCheckBox.isSelected() );
			cc.setDurationType( durationTypeComboBox.getSelectedItem() );
		}
	}
	
}
