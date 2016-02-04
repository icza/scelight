/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.gui.page.repanalyzer.charts.chartscanvas.chart.model;

import hu.scelight.gui.page.repanalyzer.charts.chartscanvas.chart.ControlGroupsChart;
import hu.scelight.sc2.rep.model.gameevents.ControlGroupUpdateEvent;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * Data set for a {@link ControlGroupsChart}.
 * 
 * @author Andras Belicza
 */
public class ControlGroupChartDataSet extends DataSet {
	
	/** Color for data set visualization. */
	protected Color                                color;
	
	/** Array of control group event lists, indexed by control group number in the range of 0..9 (both inclusive). */
	@SuppressWarnings( "unchecked" )
	public final List< ControlGroupUpdateEvent >[] controlGroupEventLists = new List[ 10 ];
	
	/**
	 * Creates a new {@link ControlGroupChartDataSet}.
	 * 
	 * @param color color of the data set
	 */
	public ControlGroupChartDataSet( final Color color ) {
		this.color = color;
		
		for ( int i = controlGroupEventLists.length - 1; i >= 0; i-- )
			controlGroupEventLists[ i ] = new ArrayList<>();
	}
	
	/**
	 * Returns the color of the data set.
	 * 
	 * @return the color of the data set
	 */
	public Color getColor() {
		return color;
	}
	
	/**
	 * Sets the color of the data set.
	 * 
	 * @param color color of the data set to be set
	 */
	public void setColor( final Color color ) {
		this.color = color;
	}
	
}
