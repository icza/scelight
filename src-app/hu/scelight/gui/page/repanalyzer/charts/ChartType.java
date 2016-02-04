/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.gui.page.repanalyzer.charts;

import hu.scelight.gui.page.repanalyzer.charts.chartfactory.ApmChartFactory;
import hu.scelight.gui.page.repanalyzer.charts.chartfactory.BaseControlChartFactory;
import hu.scelight.gui.page.repanalyzer.charts.chartfactory.ChartFactory;
import hu.scelight.gui.page.repanalyzer.charts.chartfactory.CommandsChartFactory;
import hu.scelight.gui.page.repanalyzer.charts.chartfactory.ControlGroupsChartFactory;
import hu.scelight.gui.page.repanalyzer.charts.chartfactory.PlayerStatsChartFactory;
import hu.scelight.gui.page.repanalyzer.charts.chartfactory.SpmChartFactory;
import hu.scelight.util.gui.GuiUtils;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

/**
 * Chart type.
 * 
 * @author Andras Belicza
 */
public enum ChartType {
	
	/** APM chart type. */
	APM( "APM - Actions / min", ApmChartFactory.class ),
	
	/** SPM chart type. */
	SPM( "SPM - Screens / min", SpmChartFactory.class ),
	
	/** Control Groups chart type. */
	CONTROL_GROUPS( "Control Groups", ControlGroupsChartFactory.class ),
	
	/** Player Stats chart type. */
	PLAYER_STATS( "Player Stats", PlayerStatsChartFactory.class ),
	
	/** Commands chart type. */
	COMMANDS( "Commands", CommandsChartFactory.class ),
	
	/** Base Control chart type. */
	BASE_CONTROL( "Base Control", BaseControlChartFactory.class );
	
	
	
	/** Text value of the chart type. */
	public final String                          text;
	
	/** Class of the chart factory associated with this chart type. */
	public final Class< ? extends ChartFactory > chartFactoryClass;
	
	/** String value of the chart type. */
	public final String                          stringValue;
	
	/** Key stroke for fast switching to this chart type. */
	public final KeyStroke                       keyStroke;
	
	/**
	 * Creates a new {@link ChartType}.
	 * 
	 * @param text text value of the chart type
	 * @param chartFactoryClass class of the chart factory associated with this chart type
	 */
	private ChartType( final String text, final Class< ? extends ChartFactory > chartFactoryClass ) {
		this.text = text;
		this.chartFactoryClass = chartFactoryClass;
		
		// Keystroke: CTRL+1 for chart type 1, CTRL+2 select chart type 2, ... CTRL+0 to select chart type 10, the rest is null
		final int chartNumber = ordinal() + 1;
		keyStroke = chartNumber > 10 ? null : KeyStroke.getKeyStroke( KeyEvent.VK_0 + ( chartNumber == 10 ? 0 : chartNumber ), InputEvent.CTRL_MASK );
		
		stringValue = chartNumber > 10 ? text : "<html>" + text + "&nbsp;&nbsp;<font color=#777777>" + GuiUtils.keyStrokeToString( keyStroke )
		        + "</font></html>";
	}
	
	@Override
	public String toString() {
		return stringValue;
	}
	
	
	/** Cache of the values array. */
	public static final ChartType[] VALUES = values();
	
}
