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

import hu.scelight.gui.page.repanalyzer.charts.chartscanvas.chart.BaseControlChart;
import hu.scelight.sc2.rep.model.gameevents.cmd.CmdEvent;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * Data set for a {@link BaseControlChart}.
 * 
 * @author Andras Belicza
 */
public class BaseControlChartDataSet extends DataSet {
	
	/**
	 * Target of base commands.
	 * 
	 * @author Andras Belicza
	 */
	public static class Target {
		/** Tag of this target. */
		public final int              tag;
		
		/** Name of this target. */
		public final String           name;
		
		/** List of commands targeting this target. */
		public final List< CmdEvent > cmdList = new ArrayList<>();
		
		/**
		 * Creates a new {@link Target}.
		 * 
		 * @param tag tag of this target
		 * @param name name of this target
		 */
		public Target( final int tag, final String name ) {
			this.tag = tag;
			this.name = name;
		}
	}
	
	
	/** Color for data set visualization. */
	protected Color               color;
	
	/** Secondary color for text visualization. */
	protected Color               textColor;
	
	/** List of injects (Spawn Larvae). */
	public final List< Target >   injectList  = new ArrayList<>();
	
	/** List of Chrono Boosts. */
	public final List< Target >   chronoList  = new ArrayList<>();
	
	/** List of Scanner Sweeps command events. */
	public final List< CmdEvent > muleList    = new ArrayList<>();
	
	/** List of Calldown MULE command events. */
	public final List< CmdEvent > scanList    = new ArrayList<>();
	
	/** List of Calldown Extra Supplies command events. */
	public final List< CmdEvent > xsupplyList = new ArrayList<>();
	
	
	/**
	 * Creates a new {@link BaseControlChartDataSet}.
	 * 
	 * @param color color of the data set
	 * @param textColor secondary color for text visualization
	 */
	public BaseControlChartDataSet( final Color color, final Color textColor ) {
		this.color = color;
		this.textColor = textColor;
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
	
	/**
	 * Returns the color of the text.
	 * 
	 * @return the color of the text
	 */
	public Color getTextColor() {
		return textColor;
	}
	
	/**
	 * Sets the color of the text.
	 * 
	 * @param textColor color of the text to be set
	 */
	public void setTextColor( final Color textColor ) {
		this.textColor = textColor;
	}
	
}
