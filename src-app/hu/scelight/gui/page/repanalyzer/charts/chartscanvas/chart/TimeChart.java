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

import hu.scelight.gui.page.repanalyzer.charts.chartscanvas.ChartsCanvas;
import hu.scelight.gui.page.repanalyzer.charts.chartscanvas.Rect;
import hu.scelight.gui.page.repanalyzer.charts.chartscanvas.chart.model.DataModel;
import hu.scelight.gui.page.repanalyzer.charts.chartscanvas.chart.model.DataSet;
import hu.scelight.sc2.rep.repproc.RepProcessor;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;

/**
 * A time chart whose X axis is the time.
 * 
 * @param <T> type of the data sets of the model
 * 
 * @author Andras Belicza
 */
public abstract class TimeChart< T extends DataSet > extends Chart< T > {
	
	/** Insets of charts: space reserved for axis and labels. */
	protected static final Insets INSETS                   = new Insets( 14, 27, 14, 0 );
	
	/** Color of the axes lines and axis titles. */
	protected static final Color  COLOR_AXIS               = Color.YELLOW.darker().darker();
	
	/** Color of the axis labels. */
	protected static final Color  COLOR_AXIS_LABELS        = Color.CYAN.darker();
	
	/** Minimal distance between time labels. */
	protected static final int    TIME_LABELS_MIN_DISTANCE = 100;
	
	
	/** Reference to the rep processor, used to convert loop to time. */
	protected final RepProcessor  repProc;
	
	/** Chart drawing rectangle. Axes included when axes are painted, and excluded when chart data is painted. */
	protected Rect                drawingRect;
	
	/** Tells if time values are to be displayed in seconds (else loop). */
	protected boolean             inSeconds;
	
	/** The minimum loop (time) to draw. */
	protected int                 loopMin;
	
	/** The maximum loop (time) to draw. */
	protected int                 loopMax;
	
	/** Delta loop: the difference between the maximum and minimum loop. */
	protected int                 loopDelta;
	
	/**
	 * Creates a new {@link TimeChart}.
	 * 
	 * @param repProc reference to the rep processor
	 */
	public TimeChart( final RepProcessor repProc ) {
		this.repProc = repProc;
	}
	
	/**
	 * Sets whether time values are to be displayed in seconds.
	 * 
	 * @param inSeconds tells whether time values are to be displayed in seconds
	 */
	public void setInSeconds( boolean inSeconds ) {
		this.inSeconds = inSeconds;
	}
	
	/**
	 * Tells whether time values are displayed in seconds.
	 * 
	 * @return true if time values are displayed in seconds; false otherwise
	 */
	public boolean isInSeconds() {
		return inSeconds;
	}
	
	/**
	 * Sets the time range to be draw.
	 * 
	 * @param loopMin minimum loop to draw
	 * @param loopMax maximum loop to draw
	 */
	public void setRange( final int loopMin, final int loopMax ) {
		this.loopMin = loopMin;
		this.loopMax = loopMax;
		loopDelta = loopMax - loopMin;
	}
	
	@Override
	public void paint( final Graphics2D g, final Rect bounds, final Rect visibleRect ) {
		super.paint( g, bounds, visibleRect );
		
		drawingRect = bounds.applyInsets( INSETS );
		
		if ( drawingRect.isEmpty() ) {
			g.setColor( ChartsCanvas.COLOR_MESSAGE );
			g.drawString( "Not enough space to draw chart!", visibleRect.x1 + 5, visibleRect.y1 + 12 );
			return;
		}
		
		// Things that may be inside or outside of the drawing rectangle
		paintAxesLabels();
		
		// Things that must be inside of the drawing rectangle
		final Rectangle oldClipBounds = g.getClipBounds();
		g.clipRect( drawingRect.x1, drawingRect.y1, drawingRect.width, drawingRect.height );
		paintAxes();
		paintData();
		g.setClip( oldClipBounds );
		
		// Things that may be inside or outside of the drawing rectangle
		paintTitles();
	}
	
	/**
	 * Paints the labels of the axes, and optionally assist lines (onto the drawing area) for the labels.
	 * <p>
	 * This implementation only paints the labels of the X (time) axis, because only that is certain at this level of the class hierarchy.
	 * </p>
	 */
	protected void paintAxesLabels() {
		// X (time) axis
		// Labels count excluding the 0
		final int labelsCount = drawingRect.width < TIME_LABELS_MIN_DISTANCE ? 1 : drawingRect.width / TIME_LABELS_MIN_DISTANCE;
		final int fontAscent = g.getFontMetrics().getAscent();
		
		for ( int j = labelsCount; j >= 0; j-- ) {
			final int x = drawingRect.x1 + j * drawingRect.dx / labelsCount; // labelsCount > 0
			final int loop = loopMin + j * loopDelta / labelsCount;
			
			final String label = inSeconds ? repProc.formatLoopTime( loop ) : Integer.toString( loop );
			
			g.setColor( COLOR_AXIS );
			g.drawLine( x, drawingRect.y2 + 1, x, drawingRect.y2 + 3 ); // Marker on axis
			g.setColor( COLOR_AXIS_LABELS );
			g.drawString( label, x - g.getFontMetrics().stringWidth( label ) / ( j == labelsCount ? 1 : 2 ), drawingRect.y2 + fontAscent );
		}
	}
	
	/**
	 * Paints the X (time) and Y axes.
	 */
	protected void paintAxes() {
		g.setColor( COLOR_AXIS );
		
		// Y axis
		g.drawLine( drawingRect.x1, drawingRect.y1, drawingRect.x1, drawingRect.y2 );
		
		// X axis
		g.drawLine( drawingRect.x1, drawingRect.y2, drawingRect.x2, drawingRect.y2 );
	}
	
	/**
	 * Paints the data of the chart.
	 */
	protected abstract void paintData();
	
	/**
	 * Returns the x coordinate for the specified loop.
	 * 
	 * @param loop loop whose x coordinate to return
	 * @return the x coordinate for the specified loop
	 */
	public int loopToX( final int loop ) {
		if ( loopDelta == 0 )
			return drawingRect.x1 + drawingRect.dx / 2;
		return drawingRect.x1 + drawingRect.dx * ( loop - loopMin ) / loopDelta;
	}
	
	/**
	 * Returns the loop for the specified x coordinate.
	 * 
	 * @param x x coordinate whose loop to return
	 * @return the loop for the specified x coordinate or <code>null</code> if no loop belongs to the specified coordinate (e.g. it would be negative)
	 */
	public Integer xToLoop( final int x ) {
		if ( x < drawingRect.x1 || x > drawingRect.x2 )
			return null;
		
		if ( drawingRect.dx == 0 )
			return loopMin + loopDelta / 2;
		
		return loopMin + loopDelta * ( x - drawingRect.x1 ) / drawingRect.dx;
	}
	
	/**
	 * Returns the delta loop for the specified delta x coordinate.
	 * 
	 * @param dx delta x coordinate whose delta loop to return
	 * @return the delta loop for the specified delta x coordinate
	 */
	public Integer deltaxToDeltaLoop( final int dx ) {
		if ( drawingRect.dx == 0 )
			return loopDelta / 2;
		
		return loopDelta * dx / drawingRect.dx;
	}
	
	/**
	 * Paints the titles of the chart.
	 */
	protected void paintTitles() {
		final Font oldFont = g.getFont();
		g.setFont( oldFont.deriveFont( Font.BOLD ) );
		final int fontAscent = g.getFontMetrics().getAscent();
		
		for ( int i = 0; i < dataModelList.size(); i++ ) {
			final DataModel< T > model = dataModelList.get( i );
			g.setColor( model.getColor() );
			
			// TODO titles of the data sets should be vertically aligned, not concatenated and displayed as one string!
			
			final StringBuilder sb = new StringBuilder( model.getTitle() );
			for ( final T ds : model.getDataSetList() )
				if ( ds.getTitle() != null )
					sb.append( ",    " ).append( ds.getTitle() );
			
			g.drawString( sb.toString(), drawingRect.x1 + 3, drawingRect.y1 - 1 + i * fontAscent + i );
		}
		
		g.setFont( oldFont );
	}
	
}
