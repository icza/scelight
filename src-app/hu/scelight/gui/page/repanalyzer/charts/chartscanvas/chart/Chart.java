/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.gui.page.repanalyzer.charts.chartscanvas.chart;

import hu.scelight.gui.page.repanalyzer.charts.chartscanvas.Rect;
import hu.scelight.gui.page.repanalyzer.charts.chartscanvas.chart.model.DataModel;
import hu.scelight.gui.page.repanalyzer.charts.chartscanvas.chart.model.DataSet;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.List;

/**
 * Base of all charts.
 * 
 * <p>
 * Responsible to paint the charts data.
 * </p>
 * 
 * @param <T> type of the data sets of the chart model
 * 
 * @author Andras Belicza
 */
public class Chart< T extends DataSet > {
	
	// Strokes to be used anywhere
	
	/** Double width stroke (for curves). */
	public static final Stroke       STROKE_DOUBLE         = new BasicStroke( 2.5f );
	
	/** Default stroke (for curves). */
	public static final Stroke       STROKE_DEFAULT        = new BasicStroke( 1.25f );
	
	/** Double width stroke with rounded cap (for polygons). */
	public static final Stroke       STROKE_ROUNDED_DOUBLE = new BasicStroke( 2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND );
	
	/** Stroke with rounded cap (for polygons). */
	public static final Stroke       STROKE_ROUNDED        = new BasicStroke( 1.25f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND );
	
	/** Double width stroke with butt cap (for bars). */
	public static final Stroke       STROKE_BUTT_DOUBLE    = new BasicStroke( 2.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND );
	
	/** Stroke with butt cap (for bars). */
	public static final Stroke       STROKE_BUTT           = new BasicStroke( 1.25f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND );
	
	
	/** Dashed stroke with double width (for missing chart data, estimates). */
	public static final Stroke       STROKE_DOUBLE_DASHED  = new BasicStroke( 2.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10f, new float[] { 5 }, 1 );
	
	/** Dashed stroke (for assist lines and markers). */
	public static final Stroke       STROKE_DASHED         = new BasicStroke( 1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10f, new float[] { 5 }, 1 );
	
	
	
	// Stroke aliases for charts
	
	/** Stroke for drawing curves with double width. */
	public static final Stroke       STROKE_CURVE_DOUBLE   = STROKE_DOUBLE;
	
	/** Stroke for drawing curves lines. */
	public static final Stroke       STROKE_CURVE          = STROKE_DEFAULT;
	
	/** Stroke for drawing lines / polygons with double width. */
	public static final Stroke       STROKE_POLY_DOUBLE    = STROKE_ROUNDED_DOUBLE;
	
	/** Stroke for drawing lines / polygons. */
	public static final Stroke       STROKE_POLY           = STROKE_ROUNDED;
	
	/** Stroke for drawing bars with double width. */
	public static final Stroke       STROKE_BARS_DOUBLE    = STROKE_BUTT_DOUBLE;
	
	/** Stroke for drawing bars. */
	public static final Stroke       STROKE_BARS           = STROKE_BUTT;
	
	
	/** Graphics context in which to paint. */
	protected Graphics2D             g;
	
	/** Rectangle of the chart to be painted (location and size). */
	protected Rect                   bounds;
	
	/** Visible area of the chart (can be used for optimized drawing). */
	protected Rect                   visibleRect;
	
	/** Data models of the chart. */
	protected List< DataModel< T > > dataModelList         = new ArrayList<>();
	
	/** Event box list. */
	protected final List< EventBox > eventBoxList          = new ArrayList<>();
	
	/**
	 * Returns the data model list.
	 * 
	 * @return the data model list
	 */
	public List< DataModel< T > > getDataModelList() {
		return dataModelList;
	}
	
	/**
	 * Adds a new model to the chart.
	 * 
	 * @param model data model to be added
	 */
	public void addModel( final DataModel< T > model ) {
		dataModelList.add( model );
	}
	
	/**
	 * Returns the {@link EventBox} if one was created containing the point specified by its coordinates.
	 * 
	 * <p>
	 * If multiple event box was created containing the specified point, the one that was added last will be used.
	 * </p>
	 * 
	 * @param x x coordinate to return the event for
	 * @param y y coordinate to return the event for
	 * @return the {@link EventBox} if one was created containing the point specified by its coordinates; <code>null</code> otherwise
	 */
	public EventBox getEventBox( final int x, final int y ) {
		// Go downward because the one added later is on top of others
		for ( int i = eventBoxList.size() - 1; i >= 0; i-- )
			if ( eventBoxList.get( i ).contains( x, y ) )
				return eventBoxList.get( i );
		
		return null;
	}
	
	/**
	 * Paints this chart.
	 * 
	 * <p>
	 * The graphics context comes with a clipping region defined by <code>visibleRect</code> and the background is already prepared (which may be a simple color
	 * fill or an image).
	 * </p>
	 * 
	 * <p>
	 * This default implementation simply stores the parameters.
	 * </p>
	 * 
	 * @param g graphics context in which to paint
	 * @param bounds rectangle of the chart to be painted (location and size)
	 * @param visibleRect visible area of the chart (can be used for optimized drawing)
	 */
	public void paint( final Graphics2D g, final Rect bounds, final Rect visibleRect ) {
		this.g = g;
		this.bounds = bounds;
		this.visibleRect = visibleRect;
	}
	
}
