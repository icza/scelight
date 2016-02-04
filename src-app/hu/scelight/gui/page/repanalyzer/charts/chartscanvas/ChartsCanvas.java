/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.gui.page.repanalyzer.charts.chartscanvas;

import hu.scelight.gui.page.repanalyzer.charts.ChartsComp;
import hu.scelight.gui.page.repanalyzer.charts.chartscanvas.chart.Chart;
import hu.scelight.gui.page.repanalyzer.charts.chartscanvas.chart.EventBox;
import hu.scelight.gui.page.repanalyzer.charts.chartscanvas.chart.TimeChart;
import hu.scelight.sc2.rep.repproc.RepProcessor;
import hu.scelight.sc2.rep.s2prot.Event;
import hu.scelight.util.gui.GuiUtils;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.ToolTipManager;

/**
 * Charts canvas. Responsible to hold and render the charts.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class ChartsCanvas extends JComponent {
	
	/** Insets of charts: space that must be left blank on the sides. */
	private static final Insets      INSETS           = new Insets( 0, 0, 1, 0 );
	
	/** Background color. */
	public static final Color        COLOR_BACKGROUND = Color.BLACK;
	
	/** Message color. */
	public static final Color        COLOR_MESSAGE    = new Color( 200, 80, 80 );
	
	/** Color of the chart markers. */
	private static final Color       COLOR_MARKER     = new Color( 150, 150, 255 );
	
	
	/** Replay processor. */
	protected final RepProcessor     repProc;
	
	/** List of charts. */
	private final List< Chart< ? > > chartList        = new ArrayList<>();
	
	/** Optional message to be displayed. */
	private String                   message;
	
	/** Marker loop #1. */
	private Integer                  markerLoop1;
	
	/** Marker loop #2. */
	private Integer                  markerLoop2;
	
	
	/**
	 * Creates a new {@link ChartsCanvas}.
	 * 
	 * @param repProc replay processor
	 * @param chartsComp reference to the charts tab
	 */
	public ChartsCanvas( final RepProcessor repProc, final ChartsComp chartsComp ) {
		this.repProc = repProc;
		
		ToolTipManager.sharedInstance().registerComponent( this );
		
		setOpaque( true );
		
		// Focusable so when enlarged, still can receive keys...
		setFocusable( true );
		
		final MouseAdapter ma = new MouseAdapter() {
			@Override
			public void mousePressed( final MouseEvent event ) {
				// Did the user clicked on an event box?
				final EventBox eb = getEventBox( event );
				if ( eb != null ) {
					// Select the event in the events table
					chartsComp.getEventsTableComp().selectEvent( eb.event );
				}
				
				// Marker loops only have meaning if chart is a time chart:
				if ( chartList.isEmpty() || !( chartList.get( 0 ) instanceof TimeChart ) )
					return;
				
				// If a marker is set and CTRL is pressed, clear the marker.
				// Must wrap e.loop else the other one gets unwrapped but that can be null!
				final Integer loop = eb == null ? ( (TimeChart< ? >) chartList.get( 0 ) ).xToLoop( event.getX() ) : Integer.valueOf( eb.event.loop );
				
				if ( event.getButton() == GuiUtils.MOUSE_BTN_LEFT )
					markerLoop1 = event.isControlDown() && markerLoop1 != null ? null : loop;
				else if ( event.getButton() == GuiUtils.MOUSE_BTN_RIGHT )
					markerLoop2 = event.isControlDown() && markerLoop2 != null ? null : loop;
				else
					return;
				
				if ( eb == null && loop != null )
					chartsComp.getEventsTableComp().selectByLoop( loop );
				repaint();
			}
			
			@Override
			public void mouseMoved( final MouseEvent event ) {
				// Set Hand cursor if we're over an event box.
				setCursor( getEventBox( event ) == null ? null : Cursor.getPredefinedCursor( Cursor.HAND_CURSOR ) );
			}
		};
		addMouseListener( ma );
		addMouseMotionListener( ma );
	}
	
	/**
	 * Returns the marker loop #1.
	 * 
	 * @return the marker loop #1
	 */
	public Integer getMarkerLoop1() {
		return markerLoop1;
	}
	
	/**
	 * Returns the marker loop #2.
	 * 
	 * @return the marker loop #2
	 */
	public Integer getMarkerLoop2() {
		return markerLoop2;
	}
	
	/**
	 * Sets the marker loop #1.
	 * 
	 * @param loop marker loop #1 to be set
	 */
	public void setMarkerLoop1( final Integer loop ) {
		markerLoop1 = loop;
		repaint();
	}
	
	/**
	 * Sets the message to be displayed.
	 * 
	 * @param message message to be set
	 */
	public void setMessage( final String message ) {
		this.message = message;
	}
	
	/**
	 * Sets the charts list.
	 * 
	 * @param chartList chart list to be set
	 */
	public void setChartsList( final Collection< ? extends Chart< ? > > chartList ) {
		this.chartList.clear();
		this.chartList.addAll( chartList );
	}
	
	/**
	 * Returns the list of charts.
	 * 
	 * @return the list of charts
	 */
	public List< Chart< ? > > getChartList() {
		return chartList;
	}
	
	@Override
	protected void paintComponent( final Graphics g_ ) {
		final int width = getWidth();
		final int height = getHeight();
		
		if ( width < 0 || height < 0 )
			return;
		
		final Graphics2D g = (Graphics2D) g_;
		g.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
		
		// Clear charts area
		
		g.setColor( COLOR_BACKGROUND );
		g.fillRect( 0, 0, width, height );
		
		paintMessage( g );
		
		paintCharts( g );
		
		paintMarkers( g );
	}
	
	/**
	 * Paints the charts.
	 * 
	 * @param g graphics context in which to paint
	 */
	private void paintCharts( final Graphics2D g ) {
		if ( chartList.isEmpty() )
			return;
		
		// Calculate charts sizes
		
		// Use full width
		final int chartWidth = getWidth() - INSETS.left - INSETS.right;
		
		// Vertical space available for a chart:
		final int chartVSpace = getHeight() / chartList.size(); // Size > 0
		final int chartHeight = chartVSpace - INSETS.top - INSETS.bottom;
		if ( chartHeight <= 0 )
			return;
		
		final Rect visibleRect = new Rect( getVisibleRect() );
		
		final Rectangle oldClipBounds = g.getClipBounds();
		int y = 0;
		for ( final Chart< ? > chart : chartList ) {
			final Rect r = new Rect( INSETS.left, y + INSETS.top, chartWidth, chartHeight );
			
			g.setClip( oldClipBounds );
			g.clipRect( r.x1, r.y1, r.width, r.height );
			chart.paint( g, r, visibleRect.intersection( r ) );
			
			y += chartVSpace;
		}
		g.setClip( oldClipBounds );
	}
	
	/**
	 * Paints the optional message.
	 * 
	 * @param g graphics context in which to paint
	 */
	private void paintMessage( final Graphics2D g ) {
		if ( message == null )
			return;
		
		g.setColor( COLOR_MESSAGE );
		final Font oldFont = g.getFont();
		g.setFont( oldFont.deriveFont( Font.BOLD ) );
		g.drawString( message, 10, 20 );
		g.setFont( oldFont );
	}
	
	/**
	 * Paints the markers.
	 * 
	 * @param g graphics context in which to paint
	 */
	private void paintMarkers( final Graphics2D g ) {
		if ( markerLoop1 == null && markerLoop2 == null || chartList.isEmpty() || !( chartList.get( 0 ) instanceof TimeChart ) )
			return;
		
		final TimeChart< ? > tc = (TimeChart< ? >) chartList.get( 0 );
		
		g.setStroke( Chart.STROKE_DASHED );
		
		for ( int i = 0; i < 2; i++ ) { // 2 markers
			final Integer loop = i == 0 ? markerLoop1 : markerLoop2;
			if ( loop == null )
				continue;
			
			g.setColor( COLOR_MARKER );
			int x = tc.loopToX( loop );
			g.drawLine( x, 0, x, getHeight() - 1 );
			
			// Display marker time
			final String text = tc.isInSeconds() ? repProc.formatLoopTime( loop ) : Integer.toString( loop );
			final int width = g.getFontMetrics().stringWidth( text );
			
			// Is there enough room to display text on the right? If not display on the left:
			if ( x + width > getWidth() )
				x -= width;
			
			g.setColor( Color.BLACK );
			g.fillRect( x - 1, 0 + i * 12, width + 2, 12 );
			
			g.setColor( COLOR_MARKER );
			g.drawString( text, x, 10 + i * 12 );
		}
	}
	
	/**
	 * Returns the {@link EventBox} for the specified mouse event, if an event box has been created containing the mouse location.
	 * 
	 * @param event mouse event to return the {@link Event} for
	 * @return the {@link EventBox} for the specified mouse event, if an event box has been created containing the mouse location; <code>null</code> otherwise
	 */
	private EventBox getEventBox( final MouseEvent event ) {
		for ( final Chart< ? > chart : chartList ) {
			final EventBox eb = chart.getEventBox( event.getX(), event.getY() );
			if ( eb != null )
				return eb;
		}
		
		return null;
	}
	
	/**
	 * If event is over an {@link EventBox}, returns the {@link EventBox}'s tool tip.
	 */
	@Override
	public String getToolTipText( final MouseEvent event ) {
		final EventBox eb = getEventBox( event );
		return eb == null ? super.getToolTipText( event ) : eb.getToolTipText( repProc );
	}
	
}
