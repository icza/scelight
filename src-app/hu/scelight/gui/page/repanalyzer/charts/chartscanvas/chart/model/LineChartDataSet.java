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

import hu.scelight.gui.page.repanalyzer.charts.chartscanvas.chart.LineTimeChart;

import java.awt.Color;
import java.awt.Stroke;

/**
 * Data set for a {@link LineTimeChart}.
 * 
 * @author Andras Belicza
 */
public class LineChartDataSet extends DataSet {
	
	/** Color for data set visualization. */
	protected Color  color;
	
	/** Stroke for data visualization. */
	protected Stroke stroke;
	
	/** Loops (time values) of the data points. */
	protected int[]  loops;
	
	/** Values of the data points. */
	protected int[]  values;
	
	/** Max value. */
	protected int    valueMax;
	
	/**
	 * Creates a new {@link LineChartDataSet}.
	 * 
	 * @param color color of the data set
	 * @param stroke stroke of the data set
	 * @param loops loops (time values) of the data points, must be in ascending order (sorted)
	 * @param values values of the data points
	 */
	public LineChartDataSet( final Color color, final Stroke stroke, final int[] loops, final int[] values ) {
		this.color = color;
		this.stroke = stroke;
		this.loops = loops;
		this.values = values;
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
	 * Returns the stroke of the data set.
	 * 
	 * @return the stroke of the data set
	 */
	public Stroke getStroke() {
		return stroke;
	}
	
	/**
	 * Sets the stroke of the data set.
	 * 
	 * @param stroke stroke of the data set to be set
	 */
	public void setStroke( final Stroke stroke ) {
		this.stroke = stroke;
	}
	
	/**
	 * Returns the loops (time values) of the data points.
	 * 
	 * @return the loops (time values) of the data points.
	 */
	public int[] getLoops() {
		return loops;
	}
	
	/**
	 * Sets the loops (time values) of the data points, must be in ascending order (sorted).
	 * 
	 * @param loops loops of the data points to be set
	 */
	public void setLoops( final int[] loops ) {
		this.loops = loops;
	}
	
	/**
	 * Returns the values of the data points.
	 * 
	 * @return the values of the data points
	 */
	public int[] getValues() {
		return values;
	}
	
	/**
	 * Sets the values of the data points.
	 * 
	 * @param values values of the data points to be set
	 */
	public void setValues( final int[] values ) {
		this.values = values;
	}
	
	/**
	 * Calculates and stores the value max.
	 */
	public void calculateValueMax() {
		int max = 0;
		
		for ( final int value : values )
			if ( value > max )
				max = value;
		
		valueMax = max;
	}
	
	/**
	 * Returns the max value.
	 * 
	 * @return the max value
	 */
	public int getValueMax() {
		return valueMax;
	}
	
}
