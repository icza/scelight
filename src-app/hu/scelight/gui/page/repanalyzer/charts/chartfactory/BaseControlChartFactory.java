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
import hu.scelight.gui.page.repanalyzer.charts.chartscanvas.chart.BaseControlChart;
import hu.scelight.gui.page.repanalyzer.charts.chartscanvas.chart.Chart;
import hu.scelight.gui.page.repanalyzer.charts.chartscanvas.chart.model.BaseControlChartDataSet;
import hu.scelight.gui.page.repanalyzer.charts.chartscanvas.chart.model.BaseControlChartDataSet.Target;
import hu.scelight.gui.page.repanalyzer.charts.chartscanvas.chart.model.DataModel;
import hu.scelight.gui.setting.SettingsGui;
import hu.scelight.sc2.balancedata.model.Ability;
import hu.scelight.sc2.rep.model.gameevents.cmd.CmdEvent;
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
 * {@link ChartType#BASE_CONTROL} chart factory.
 * 
 * @author Andras Belicza
 */
public class BaseControlChartFactory extends BaseChartFactory< BaseControlChartDataSet > {
	
	/** Check box to show build commands. */
	private final XCheckBox showIconsCheckBox = SettingsGui.createSettingCheckBox( Settings.BASE_CONTROL_SHOW_ICONS, Env.APP_SETTINGS, null );
	
	
	/**
	 * Creates a new {@link BaseControlChartFactory}.
	 * 
	 * @param chartsComp reference to the charts component that created us
	 */
	public BaseControlChartFactory( final ChartsComp chartsComp ) {
		super( chartsComp );
		
		showIconsCheckBox.addActionListener( chartsComp.chartsRebuilder );
	}
	
	@Override
	public void addChartOptions( final XToolBar toolBar ) {
		toolBar.add( new HelpIcon( Helps.BASE_CONTROL_CHART, true ).rightBorder( 5 ) );
		
		toolBar.add( showIconsCheckBox );
	}
	
	@Override
	public List< Chart< BaseControlChartDataSet > > createCharts() {
		chartList = new ArrayList<>();
		
		createModelByUserIds();
		
		calculateDataSets();
		
		return chartList;
	}
	
	@Override
	protected BaseControlChart createChart() {
		return new BaseControlChart( repProc );
	}
	
	@Override
	protected DataModel< BaseControlChartDataSet > createChartModel( final User user, final String modelTitle ) {
		final DataModel< BaseControlChartDataSet > model = new DataModel<>( modelTitle, user.getPlayerColor().brighterColor );
		
		// One data set per model only: all types of commands are presented the same way
		final BaseControlChartDataSet ds = new BaseControlChartDataSet( user.getPlayerColor().color, user.getPlayerColor().brighterColor );
		model.addDataSet( ds );
		
		return model;
	}
	
	/**
	 * Calculates the data sets.
	 */
	private void calculateDataSets() {
		// Calculate data sets
		for ( final Event event : repProc.replay.gameEvents.events ) {
			if ( event.id != IGameEvents.ID_CMD )
				continue;
			
			final DataModel< BaseControlChartDataSet > model = modelByUserIds[ event.userId ];
			if ( model == null )
				continue;
			
			final CmdEvent ce = (CmdEvent) event;
			if ( ce.command == null || ce.command.abilId == null )
				continue;
			
			// One data set only
			final BaseControlChartDataSet ds = model.getDataSetList().get( 0 );
			
			switch ( ce.command.abilId ) {
				case Ability.ID_SPAWN_LARVA :
				case Ability.ID_CHRONO_BOOST : {
					// Target unit exists in case of Spawn Larva and Chrono Boost
					final boolean isInject = ce.command.abilId.equals( Ability.ID_SPAWN_LARVA );
					Target target = null;
					for ( final Target target_ : isInject ? ds.injectList : ds.chronoList ) {
						if ( target_.tag == ce.getTargetUnit().getTag() ) {
							target = target_;
							break;
						}
					}
					if ( target == null )
						( isInject ? ds.injectList : ds.chronoList ).add( target = new Target( ce.getTargetUnit().getTag(), repProc.replay.getBalanceData()
						        .getUnit( ce.getTargetUnit().getSnapshotUnitLink() ).text ) );
					if ( isInject ) {
						// Check if this Spawn Larva overlaps the previous one; If yes, that means the previous inject
						// was not executed for sure, remove it (Spawn Larva cannot be stacked!)
						if ( !target.cmdList.isEmpty()
						        && target.cmdList.get( target.cmdList.size() - 1 ).loop + BaseControlChart.DURATION_LARVA_SPAWNING > ce.loop )
							target.cmdList.remove( target.cmdList.size() - 1 );
					}
					target.cmdList.add( ce );
					break;
				}
				case Ability.ID_CALLDOWN_MULE :
					ds.muleList.add( ce );
					break;
				case Ability.ID_SCANNER_SWEEP :
					ds.scanList.add( ce );
					break;
				case Ability.ID_CALLDOWN_EXTRA_SUPPLIES :
					ds.xsupplyList.add( ce );
					break;
			}
		}
		
		// Set data set titles
		for ( final Chart< BaseControlChartDataSet > chart : chartList ) {
			for ( final DataModel< BaseControlChartDataSet > model : chart.getDataModelList() ) {
				// One data set only
				final BaseControlChartDataSet ds = model.getDataSetList().get( 0 );
				
				// Calculate Avg Spawning Ratio, Avg Injection Gap
				// TODO decide if hatch time is till the last spawning end or last cmd loop of user!
				int injects = 0;
				int hatchTime = 0;
				int hatchSpawnTime = 0;
				int injectionGap = 0;
				int injectionGapCount = 0;
				for ( final Target target : ds.injectList ) {
					injects += target.cmdList.size();
					
					// Hatch time: from the first injection loop till the last injection loop + the Larva spawning duration
					// (but of course not greater than the elapsed game loops).
					hatchTime += Math.min( repProc.replay.header.getElapsedGameLoops(), target.cmdList.get( target.cmdList.size() - 1 ).loop
					        + BaseControlChart.DURATION_LARVA_SPAWNING )
					        - target.cmdList.get( 0 ).loop;
					
					int lastSpawningEnd = 0;
					for ( final CmdEvent ce : target.cmdList ) {
						hatchSpawnTime += Math.min( repProc.replay.header.getElapsedGameLoops() - ce.loop, BaseControlChart.DURATION_LARVA_SPAWNING );
						if ( lastSpawningEnd > 0 ) {
							injectionGap += ce.loop - lastSpawningEnd;
							injectionGapCount++;
						}
						lastSpawningEnd = ce.loop + BaseControlChart.DURATION_LARVA_SPAWNING;
					}
				}
				final String spawningRatio = hatchTime == 0 ? "N/A" : Env.LANG.formatNumber( hatchSpawnTime * 100.0 / hatchTime, 2 ) + "%";
				final String avgInjGap = injectionGapCount == 0 ? "N/A" : Env.LANG.formatNumber( repProc.loopToTime( injectionGap ) / 1000.0
				        / injectionGapCount, 2 )
				        + " sec";
				
				int chronos = 0;
				for ( final Target target : ds.chronoList )
					chronos += target.cmdList.size();
				
				final StringBuilder sb = new StringBuilder();
				if ( injects > 0 ) {
					if ( sb.length() > 0 )
						sb.append( ",    " );
					sb.append( "Avg Spawning ratio: " ).append( spawningRatio ).append( ", Avg Injection gap: " ).append( avgInjGap ).append( ", Injects: " )
					        .append( injects );
				}
				if ( chronos > 0 ) {
					if ( sb.length() > 0 )
						sb.append( ",    " );
					sb.append( "Chronos: " ).append( chronos );
				}
				if ( !ds.muleList.isEmpty() ) {
					if ( sb.length() > 0 )
						sb.append( ",    " );
					sb.append( "MULEs: " ).append( ds.muleList.size() );
				}
				if ( !ds.scanList.isEmpty() ) {
					if ( sb.length() > 0 )
						sb.append( ",    " );
					sb.append( "Scans: " ).append( ds.scanList.size() );
				}
				if ( !ds.xsupplyList.isEmpty() ) {
					if ( sb.length() > 0 )
						sb.append( ",    " );
					sb.append( "XSupplies: " ).append( ds.xsupplyList.size() );
				}
				
				if ( sb.length() > 0 )
					ds.setTitle( sb.toString() );
			}
		}
	}
	
	@Override
	public void reconfigureChartCanvas() {
		for ( final Chart< ? > chart : chartsComp.getChartsCanvas().getChartList() ) {
			if ( !( chart instanceof BaseControlChart ) )
				continue;
			
			final BaseControlChart bcc = (BaseControlChart) chart;
			
			bcc.setShowIcons( showIconsCheckBox.isSelected() );
		}
	}
	
}
