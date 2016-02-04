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

import hu.scelight.gui.page.repanalyzer.charts.ChartsComp;
import hu.scelight.gui.page.repanalyzer.charts.chartscanvas.chart.Chart;
import hu.scelight.sc2.rep.repproc.RepProcessor;
import hu.sllauncher.gui.comp.XToolBar;

import java.util.Collections;
import java.util.List;

/**
 * Base of all chart factories.
 * 
 * <p>
 * Responsible to build the charts' tool bars and to create charts (including building their models).
 * </p>
 * 
 * @author Andras Belicza
 */
public class ChartFactory {
	
	/** Reference to the charts component that created us. */
	protected final ChartsComp   chartsComp;
	
	/** Reference to the replay processor. */
	protected final RepProcessor repProc;
	
	/**
	 * Creates a new {@link ChartFactory}.
	 * 
	 * @param chartsComp reference to the charts component that created us
	 */
	public ChartFactory( final ChartsComp chartsComp ) {
		this.chartsComp = chartsComp;
		repProc = chartsComp.getRepProcessor();
	}
	
	/**
	 * Adds the option components of the chart to the specified tool bar.
	 * 
	 * <p>
	 * This implementation does not add any components.
	 * </p>
	 * 
	 * @param toolBar tool bar to add the option components to
	 */
	public void addChartOptions( final XToolBar toolBar ) {
		// Subclasses will optionally add options. 
	}
	
	/**
	 * Creates the charts.
	 * 
	 * <p>
	 * This implementation sets an "unimplemented" message to the charts canvas and returns an empty list.
	 * </p>
	 * 
	 * @return the charts
	 */
	public List< ? extends Chart< ? > > createCharts() {
		chartsComp.getChartsCanvas().setMessage( "This chart is not yet implemented." );
		return Collections.emptyList();
	}
	
	/**
	 * Reconfigures the charts canvas with chart specific parameters (does not repaint).
	 * 
	 * <p>
	 * This implementation does nothing.
	 * </p>
	 */
	public void reconfigureChartCanvas() {
		// Subclasses will optionally reconfigure chart canvas.
	}
	
}
