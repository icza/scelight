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

import hu.scelight.gui.page.repanalyzer.charts.chartscanvas.chart.commands.CommandsChart;
import hu.scelight.sc2.rep.model.gameevents.cmd.CmdEvent;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * Data set for a {@link CommandsChart}.
 * 
 * @author Andras Belicza
 */
public class CommandsChartDataSet extends DataSet {
	
	/** Color for data set visualization. */
	protected Color               color;
	
	/** List of command events. */
	public final List< CmdEvent > cmdEventLists = new ArrayList<>();
	
	/**
	 * Creates a new {@link CommandsChartDataSet}.
	 * 
	 * @param color color of the data set
	 */
	public CommandsChartDataSet( final Color color ) {
		this.color = color;
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
