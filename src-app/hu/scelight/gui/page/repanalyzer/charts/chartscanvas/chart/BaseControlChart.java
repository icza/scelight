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
import hu.scelight.gui.page.repanalyzer.charts.chartscanvas.chart.model.BaseControlChartDataSet;
import hu.scelight.gui.page.repanalyzer.charts.chartscanvas.chart.model.BaseControlChartDataSet.Target;
import hu.scelight.gui.page.repanalyzer.charts.chartscanvas.chart.model.DataModel;
import hu.scelight.sc2.rep.model.gameevents.cmd.CmdEvent;
import hu.scelight.sc2.rep.repproc.RepProcessor;
import hu.scelight.service.env.Env;
import hu.scelight.util.gui.GuiUtils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import javax.swing.Icon;

/**
 * A time chart which visualizes the base control (main building control).<br>
 * Its data model is a series Spawn Larva command events grouped by target units, a series of Chrono Boost command events grouped by target units, a series of
 * CallDown MULE command events, a series of Scanner Sweep command events and last a series of Calldown Extra Supplies command events.
 * 
 * @author Andras Belicza
 */
public class BaseControlChart extends TimeChart< BaseControlChartDataSet > {
	
	/** Duration of a Spawn Larva on a Hatchery (in frames). */
	public static final int DURATION_LARVA_SPAWNING = 40 * 16; // 40 seconds...
	                                                           
	/** Duration of a Chrono Boost (in frames). */
	public static final int DURATION_CHRONO_BOOST   = 20 * 16; // 20 seconds...
	                                                           
	/** Duration (lifetime) of a MULE (in frames). */
	public static final int DURATION_MULE_LIFE      = 90 * 16; // 90 seconds...
	                                                           
	/** Duration of a Scanner Sweep (in frames). */
	public static final int DURATION_SCAN           = 196;    // 12.2775 seconds...
	                                                           
	/** Duration of a Calldown Extra Supplies (in frames). */
	public static final int DURATION_XSUPPLIES      = 4 * 16; // 4 seconds...
	                                                           
	                                                           
	/** Tells if icons is to be shown. */
	private boolean         showIcons;
	
	
	/**
	 * Creates a new {@link BaseControlChart}.
	 * 
	 * @param repProc reference to the rep processor
	 */
	public BaseControlChart( final RepProcessor repProc ) {
		super( repProc );
	}
	
	/**
	 * Sets whether icons is to be shown.
	 * 
	 * @param showIcons true if icons is to be shown; false if not
	 */
	public void setShowIcons( final boolean showIcons ) {
		this.showIcons = showIcons;
	}
	
	@Override
	public void paint( final Graphics2D g, final Rect bounds, final Rect visibleRect ) {
		super.paint( g, bounds, visibleRect );
	}
	
	
	/** Data set being painted. */
	private BaseControlChartDataSet ds;
	
	/** Visual row height. */
	private int                     rowHeight;
	
	/** Y coordinate of the current row to paint at. */
	private int                     y;
	
	/** Color to be used to fill boxes. */
	private Color                   fillColor;
	
	@Override
	protected void paintData() {
		eventBoxList.clear();
		
		final Font oldFont = g.getFont();
		// g.setFont( oldFont.deriveFont( Font.BOLD ) );
		
		// Calculate rows count in a "global" way, and so if all is displayed on 1 chart, it will properly align rows.
		int rows = 0;
		for ( final DataModel< BaseControlChartDataSet > model : dataModelList ) {
			// One data set only
			final BaseControlChartDataSet ds = model.getDataSetList().get( 0 );
			rows += ds.injectList.size();
			rows += ds.chronoList.size();
			if ( !ds.muleList.isEmpty() )
				rows++;
			if ( !ds.scanList.isEmpty() )
				rows++;
			if ( !ds.xsupplyList.isEmpty() )
				rows++;
		}
		if ( rows == 0 )
			return;
		
		// Leave 1 pixel between rows; rows > 0
		rowHeight = ( drawingRect.dy - rows ) / rows;
		
		y = drawingRect.y1 + 1;
		
		for ( final DataModel< BaseControlChartDataSet > model : dataModelList ) {
			// One data set only
			final BaseControlChartDataSet ds = model.getDataSetList().get( 0 );
			this.ds = ds;
			fillColor = GuiUtils.colorWithAlpha( ds.getColor(), 50 );
			
			// Always first paint boxes, then row text so text will be on top.
			
			for ( final Target target : ds.injectList ) {
				// Hatch time: from the first injection loop till the last injection loop + the Larva spawning duration
				// (but of course not greater than the elapsed game loops).
				final int hatchTime = Math.min( repProc.replay.header.getElapsedGameLoops(), target.cmdList.get( target.cmdList.size() - 1 ).loop
				        + DURATION_LARVA_SPAWNING )
				        - target.cmdList.get( 0 ).loop;
				int hatchSpawnTime = 0;
				int injectionGap = 0;
				int injectionGapCount = 0;
				
				int lastSpawningEnd = 0;
				for ( final CmdEvent ce : target.cmdList ) {
					handleEvent( ce, DURATION_LARVA_SPAWNING );
					
					hatchSpawnTime += Math.min( repProc.replay.header.getElapsedGameLoops() - ce.loop, DURATION_LARVA_SPAWNING );
					if ( lastSpawningEnd > 0 ) {
						injectionGap += ce.loop - lastSpawningEnd;
						injectionGapCount++;
					}
					lastSpawningEnd = ce.loop + DURATION_LARVA_SPAWNING;
				}
				final String ratio = hatchTime == 0 ? "N/A" : Env.LANG.formatNumber( hatchSpawnTime * 100.0 / hatchTime, 2 ) + "%";
				final String avgInjGap = injectionGapCount == 0 ? "N/A" : Env.LANG.formatNumber( repProc.loopToTime( injectionGap ) / 1000.0
				        / injectionGapCount, 2 )
				        + " sec";
				paintRowText( target.name + " (" + repProc.tagTransformation.tagToString( target.tag ) + ") (" + ratio + ", " + avgInjGap + ", "
				        + target.cmdList.size() + ")" );
			}
			
			for ( final Target target : ds.chronoList ) {
				for ( final CmdEvent ce : target.cmdList )
					handleEvent( ce, DURATION_CHRONO_BOOST );
				paintRowText( target.name + " (" + repProc.tagTransformation.tagToString( target.tag ) + ") (" + target.cmdList.size() + ')' );
			}
			
			if ( !ds.muleList.isEmpty() ) {
				for ( final CmdEvent ce : ds.muleList )
					handleEvent( ce, DURATION_MULE_LIFE );
				paintRowText( "MULEs (" + ds.muleList.size() + ")" );
			}
			
			if ( !ds.scanList.isEmpty() ) {
				for ( final CmdEvent ce : ds.scanList )
					handleEvent( ce, DURATION_SCAN );
				paintRowText( "Scans (" + ds.scanList.size() + ")" );
			}
			
			if ( !ds.xsupplyList.isEmpty() ) {
				for ( final CmdEvent ce : ds.xsupplyList )
					handleEvent( ce, DURATION_XSUPPLIES );
				paintRowText( "XSupplies (" + ds.xsupplyList.size() + ")" );
			}
		}
		
		g.setFont( oldFont );
	}
	
	/**
	 * Handles an event.
	 * <p>
	 * Creates and adds an {@link EventBox} for the specified event and specified parameters and paints a corresponding box.
	 * </p>
	 * 
	 * @param ce command event to create an {@link EventBox} for
	 * @param widthLoop width of the box in loops
	 */
	private void handleEvent( final CmdEvent ce, final int widthLoop ) {
		final int x1 = loopToX( ce.loop );
		final int x2 = loopToX( ce.loop + widthLoop );
		
		final EventBox eb = new EventBox( ce );
		eb.x = x1;
		eb.y = y;
		eb.width = x2 - x1;
		eb.height = rowHeight;
		eventBoxList.add( eb );
		
		g.setColor( fillColor );
		g.fillRect( eb.x + 1, eb.y + 1, eb.width - 1, eb.height - 2 );
		
		if ( showIcons ) {
			final Icon icon = ce.command.getRicon().size( Math.min( 76, Math.min( eb.width, eb.height ) ) );
			icon.paintIcon( null, g, eb.x + ( eb.width - icon.getIconWidth() ) / 2, eb.y + ( eb.height - icon.getIconHeight() ) / 2 );
		}
		
		// Paint border last so icon will not overlap it.
		g.setColor( ds.getColor() );
		g.drawRect( eb.x, eb.y, eb.width, eb.height - 1 );
	}
	
	/**
	 * Paints a row text and closes the row (by increasing the row y coordinate).
	 * 
	 * @param text row text to be painted
	 */
	private void paintRowText( final String text ) {
		g.setColor( ds.getTextColor() );
		g.drawString( text, drawingRect.x1 + 2, y + ( rowHeight + g.getFontMetrics().getAscent() ) / 2 );
		y += rowHeight + 1;
	}
	
}
