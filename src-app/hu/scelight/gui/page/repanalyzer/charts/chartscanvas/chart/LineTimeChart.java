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
import hu.scelight.gui.page.repanalyzer.charts.chartscanvas.chart.model.LineChartDataSet;
import hu.scelight.sc2.rep.repproc.RepProcessor;
import hu.scelight.util.gui.GuiUtils;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Point2D;
import java.util.Arrays;

/**
 * A time chart which displays a line chart. Its data model is a series of data points connected somehow (e.g. straight lines or cubic approximation).
 * 
 * @author Andras Belicza
 */
public class LineTimeChart extends TimeChart< LineChartDataSet > {
	
	/**
	 * Line time chart presentation.
	 * 
	 * @author Andras Belicza
	 */
	public enum Presentation {
		/** Linear graph. */
		LINEAR_GRAPH( "Linear Graph", STROKE_POLY, STROKE_POLY_DOUBLE ),
		
		/** Cubic graph. */
		CUBIC_GRAPH( "Cubic Graph", STROKE_CURVE, STROKE_CURVE_DOUBLE ),
		
		/** Filled bars. */
		FILLED_BARS( "Filled Bars", STROKE_BARS, STROKE_BARS_DOUBLE );
		
		
		/** Text value of the presentation. */
		public final String text;
		
		/** Suggested stroke for the presentation. */
		public final Stroke storke;
		
		/** Suggested stroke with double width for the presentation. */
		public final Stroke storkeDouble;
		
		
		/**
		 * Creates a new {@link Presentation}.
		 * 
		 * @param text text of the presentation
		 * @param stroke suggested stroke for the presentation
		 * @param strokeDouble suggested stroke with double width for the presentation
		 */
		private Presentation( final String text, final Stroke stroke, final Stroke strokeDouble ) {
			this.text = text;
			this.storke = stroke;
			this.storkeDouble = strokeDouble;
		}
		
		@Override
		public String toString() {
			return text;
		}
		
		
		/** Cache of the values array. */
		public static final Presentation[] VALUES = values();
		
	}
	
	/** Minimal distance between assist lines. */
	protected static final int   ASSIST_LINES_MIN_DISTANCE = 20;
	
	/** Color of the assist lines. */
	protected static final Color COLOR_ASSIST_LINES        = new Color( 60, 60, 60 );
	
	
	/** Presentation of the data sets. */
	protected Presentation       presentation;
	
	/** Global max value. */
	protected int                valueMax;
	
	/**
	 * This property tells if in case of {@link Presentation#CUBIC_GRAPH} negative values mean estimated values for missing data points, and therefore segments
	 * belonging to these points are to be drawn with dashed stroke.<br>
	 * The default value is false (meaning no dashed stroke for negative points).
	 */
	protected boolean            cubicNegativeEstimation   = false;
	
	
	/**
	 * Creates a new {@link LineTimeChart}.
	 * 
	 * @param repProc reference to the rep processor
	 */
	public LineTimeChart( final RepProcessor repProc ) {
		super( repProc );
	}
	
	/**
	 * Sets the presentation of the data sets.
	 * 
	 * @param presentation presentation of the data sets to be set
	 */
	public void setPresentation( final Presentation presentation ) {
		this.presentation = presentation;
	}
	
	@Override
	public void paint( final Graphics2D g, final Rect bounds, final Rect visibleRect ) {
		// Determine global max
		valueMax = 0;
		for ( final DataModel< LineChartDataSet > model : dataModelList )
			for ( final LineChartDataSet dataSet : model.getDataSetList() )
				if ( dataSet.getValueMax() > valueMax )
					valueMax = dataSet.getValueMax();
		
		super.paint( g, bounds, visibleRect );
	}
	
	/**
	 * Also paints the labels of the Y axis and assist lines.
	 */
	@Override
	protected void paintAxesLabels() {
		// X (time) axis
		super.paintAxesLabels();
		
		// Y axis
		final int assistLinesCount = drawingRect.height < ASSIST_LINES_MIN_DISTANCE ? 1 : drawingRect.height / ASSIST_LINES_MIN_DISTANCE;
		final Stroke oldStroke = g.getStroke();
		g.setStroke( STROKE_DASHED );
		
		// Calculate appropriate font so value labels will fit
		Font oldFont = null;
		if ( valueMax > 9999 ) {
			// Max value is 5 digit (at least), decrease the font size
			oldFont = g.getFont();
			g.setFont( oldFont.deriveFont( 9f ) );
		} else if ( valueMax > 999 ) {
			// Max value is 4 digit (at least), decrease the font size
			oldFont = g.getFont();
			g.setFont( oldFont.deriveFont( 10f ) );
		}
		
		final FontMetrics fontMetrics = g.getFontMetrics();
		final int fontAscent = fontMetrics.getAscent();
		
		for ( int i = assistLinesCount - 1; i >= 0; i-- ) {
			final int y = drawingRect.y1 + i * drawingRect.dy / assistLinesCount; // assistLinesCount > 0
			
			g.setColor( COLOR_ASSIST_LINES );
			g.drawLine( drawingRect.x1 + 1, y, drawingRect.x2, y );
			
			g.setColor( COLOR_AXIS_LABELS );
			final String label = Integer.toString( valueMax * ( assistLinesCount - i ) / assistLinesCount );
			g.drawString( label, drawingRect.x1 - fontMetrics.stringWidth( label ) - 1, y + fontAscent / 2 - 1 );
		}
		
		if ( oldFont != null )
			g.setFont( oldFont );
		g.setStroke( oldStroke );
	}
	
	
	/** Data set currently being painted. */
	private LineChartDataSet dataSet;
	
	/** Loops currently being painted. */
	private int[]            loops;
	
	/** Values currently being painted. */
	private int[]            values;
	
	/** Minimum index being painted. */
	private int              idxMin;
	
	/** Maximum index being painted. */
	private int              idxMax;
	
	@Override
	protected void paintData() {
		final Stroke oldStroke = g.getStroke();
		
		for ( final DataModel< LineChartDataSet > model : dataModelList )
			for ( final LineChartDataSet dataSet : model.getDataSetList() ) {
				this.dataSet = dataSet;
				loops = dataSet.getLoops();
				values = dataSet.getValues();
				
				// Determine first and last data points
				idxMin = Arrays.binarySearch( loops, loopMin );
				if ( idxMin < 0 )
					idxMin = Math.max( 0, -idxMin - 2 );
				idxMax = Arrays.binarySearch( loops, loopMax );
				if ( idxMax < 0 )
					idxMax = Math.min( loops.length - 1, -idxMax - 1 );
				
				g.setColor( dataSet.getColor() );
				g.setStroke( dataSet.getStroke() );
				
				switch ( presentation ) {
					case LINEAR_GRAPH :
						paintLinearGraph();
						break;
					case CUBIC_GRAPH :
						paintCubicGraph();
						break;
					case FILLED_BARS :
						paintFilledBars();
						break;
				}
			}
		
		g.setStroke( oldStroke );
	}
	
	/**
	 * Paints the current data set as a linear approximated graph.
	 */
	private void paintLinearGraph() {
		int loopX = loopToX( loops[ idxMin ] );
		int valueY = valueToY( values[ idxMin ] );
		
		for ( int i = idxMin; i < idxMax; i++ ) {
			final int nextLoopX = loopToX( loops[ i + 1 ] );
			final int nextValueY = valueToY( values[ i + 1 ] );
			
			g.drawLine( loopX, valueY, nextLoopX, nextValueY );
			
			loopX = nextLoopX;
			valueY = nextValueY;
		}
	}
	
	/**
	 * Paints the current data set as a cubic approximated graph.
	 */
	private void paintCubicGraph() {
		// Must have at least 4 data points for cubic approximation
		if ( loops.length < 2 )
			return;
		
		// values array might contain negative values which indicates the point is a missing data,
		// it is an estimate to make the chart better/more informative. Multiply this by -1 to get the proper data.
		// Segments which contain a point having negative value should be drawn with dash line/curve.
		
		final CubicCurve2D.Float cubicCurve = new CubicCurve2D.Float();
		Point2D.Float p = new Point2D.Float(), p1 = new Point2D.Float(), p2 = new Point2D.Float(), p3 = new Point2D.Float(), pTemp;
		final float s = 0.25f;
		getCP( idxMin, p );
		getCP( idxMin + 1, p1 );
		getCP( idxMin + 2, p2 );
		
		for ( int j = idxMin; j < idxMax; j++ ) {
			getCP( j + 3, p3 );
			if ( cubicNegativeEstimation && ( values[ j ] < 0 || values[ j + 1 ] < 0 ) )
				g.setStroke( STROKE_DOUBLE_DASHED );
			cubicCurve.setCurve( p1.x, p1.y, -s * p.x + p1.x + s * p2.x, -s * p.y + p1.y + s * p2.y, s * p1.x + p2.x - s * p3.x, s * p1.y + p2.y - s * p3.y,
			        p2.x, p2.y );
			g.draw( cubicCurve );
			if ( cubicNegativeEstimation && ( values[ j ] < 0 || values[ j + 1 ] < 0 ) )
				g.setStroke( dataSet.getStroke() );
			pTemp = p;
			p = p1;
			p1 = p2;
			p2 = p3;
			p3 = pTemp;
		}
	}
	
	/**
	 * Helper method for drawing cubic curves.<br>
	 * <code>loops</code> and <code>values</code> must contain at least 2 points!
	 * 
	 * @param i index of the curve segment
	 * @param p point to store the control point location
	 */
	private void getCP( final int i, final Point2D.Float p ) {
		int l;
		if ( i == 0 ) {
			p.setLocation( -0.5f * loops[ 0 ] + 1.5f * loops[ 1 ], -0.5f * valueForCubic( values[ 0 ] ) + 1.5f * valueForCubic( values[ 1 ] ) );
		} else if ( i == ( l = loops.length ) + 1 ) {
			p.setLocation( -0.5f * loops[ l - 1 ] + 1.5f * loops[ l - 2 ], -0.5f * valueForCubic( values[ l - 1 ] ) + 1.5f * valueForCubic( values[ l - 2 ] ) );
		} else
			p.setLocation( loops[ i - 1 ], valueForCubic( values[ i - 1 ] ) );
		
		p.setLocation( loopToX( (int) p.x ), valueToY( (int) p.y ) );
	}
	
	/**
	 * Returns a value to be used in case of {@link Presentation#CUBIC_GRAPH}, which is the absolute value of the specified value if
	 * {@link #cubicNegativeEstimation} is true.
	 * 
	 * @param value data point value
	 * @return a value to be used
	 */
	private int valueForCubic( final int value ) {
		return cubicNegativeEstimation && value < 0 ? -value : value;
	}
	
	/**
	 * Paints the current data set as filled bars.
	 */
	private void paintFilledBars() {
		// Half-transparent fill color
		final Color fillColor = GuiUtils.colorWithAlpha( dataSet.getColor(), 0x70 );
		
		int loopX = loopToX( loops[ idxMin ] );
		
		int x1;
		x1 = idxMin > 0 ? ( loopToX( loops[ idxMin - 1 ] ) + loopX ) / 2 : drawingRect.x1;
		
		for ( int i = idxMin; i <= idxMax; i++ ) {
			final int loopX2, x2;
			if ( i < loops.length - 1 ) {
				// Last segment might be smaller, the segment before that has to be calculated differently:
				if ( i == loops.length - 2 && i > 0 ) {
					// The one before the last
					loopX2 = -1; // will not be used
					x2 = loopToX( loops[ i ] ) * 2 - x1;
				} else {
					// Normal segment
					loopX2 = loopToX( loops[ i + 1 ] );
					x2 = ( loopX + loopX2 ) / 2;
				}
			} else {
				loopX2 = drawingRect.x2;
				x2 = drawingRect.x2;
			}
			
			final int valueY = valueToY( values[ i ] );
			
			g.setColor( fillColor );
			g.fillRect( x1 + 1, valueY, x2 - x1, drawingRect.y2 - valueY );
			g.setColor( dataSet.getColor() );
			g.drawLine( x1 + 1, valueY, x2, valueY );
			
			loopX = loopX2;
			x1 = x2;
		}
	}
	
	/**
	 * Returns the y coordinate for the specified data value.
	 * 
	 * @param value data value whose y coordinate to return
	 * @return the y coordinate for the specified data value
	 */
	protected int valueToY( final int value ) {
		return drawingRect.y2 - ( valueMax == 0 ? 0 : drawingRect.dy * value / valueMax );
	}
	
}
