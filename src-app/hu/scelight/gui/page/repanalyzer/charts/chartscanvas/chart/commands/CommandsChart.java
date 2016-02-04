/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.gui.page.repanalyzer.charts.chartscanvas.chart.commands;

import hu.scelight.gui.page.repanalyzer.charts.chartscanvas.Rect;
import hu.scelight.gui.page.repanalyzer.charts.chartscanvas.chart.EventBox;
import hu.scelight.gui.page.repanalyzer.charts.chartscanvas.chart.TimeChart;
import hu.scelight.gui.page.repanalyzer.charts.chartscanvas.chart.model.CommandsChartDataSet;
import hu.scelight.gui.page.repanalyzer.charts.chartscanvas.chart.model.DataModel;
import hu.scelight.sc2.balancedata.model.TrainCommand;
import hu.scelight.sc2.rep.model.gameevents.cmd.CmdEvent;
import hu.scelight.sc2.rep.repproc.RepProcessor;
import hu.scelight.util.gui.GuiUtils;

import java.awt.Color;
import java.awt.Graphics2D;

import javax.swing.Icon;

/**
 * A time chart which displays the usage/occurrences of command events. Its data model is a series of command events.
 * 
 * @author Andras Belicza
 */
public class CommandsChart extends TimeChart< CommandsChartDataSet > {
	
	/** Tells if commands is to be visualized with icons (text is used otherwise). */
	private boolean      useIcons;
	
	/** Icon size. */
	private int          iconSize;
	
	/** Duration visualization type. */
	private DurationType durationType;
	
	/**
	 * Creates a new {@link CommandsChart}.
	 * 
	 * @param repProc reference to the rep processor
	 */
	public CommandsChart( final RepProcessor repProc ) {
		super( repProc );
	}
	
	/**
	 * Sets the icon size.
	 * 
	 * @param iconSize icon size to be set
	 */
	public void setIconSize( final int iconSize ) {
		this.iconSize = iconSize;
	}
	
	/**
	 * Sets whether commands is to be visualized with icons.
	 * 
	 * @param useIcons true if commands is to be visualized with icons; false if with text
	 */
	public void setUseIcons( final boolean useIcons ) {
		this.useIcons = useIcons;
	}
	
	/**
	 * Sets the duration visualization type.
	 * 
	 * @param durationType duration visualization type to be set
	 */
	public void setDurationType( final DurationType durationType ) {
		this.durationType = durationType;
	}
	
	@Override
	public void paint( final Graphics2D g, final Rect bounds, final Rect visibleRect ) {
		super.paint( g, bounds, visibleRect );
	}
	
	@Override
	protected void paintData() {
		eventBoxList.clear();
		
		final int entityHeight = useIcons ? iconSize + 2 : g.getFontMetrics().getAscent();
		final int MAX_LEVEL = Math.max( 1, ( drawingRect.dy - 7 ) / entityHeight );
		
		// Paint in 2 rounds: first lines and durations and last the icons/texts
		// Reason: this way icons and texts will not be overdrawn with "junk".
		
		// first round: lines and durations
		
		// "Global" level, not reset between models, so if all is on one chart, it will still remain readable.
		int level = MAX_LEVEL > 2 ? 1 : 0;
		
		for ( final DataModel< CommandsChartDataSet > model : dataModelList ) {
			// One data set only
			final CommandsChartDataSet ds = model.getDataSetList().get( 0 );
			
			
			final Color barColor = durationType == DurationType.BARS ? GuiUtils.colorWithAlpha( ds.getColor(), 50 ) : null;
			
			for ( final CmdEvent ce : ds.cmdEventLists ) {
				g.setStroke( ce.command instanceof TrainCommand ? STROKE_DASHED : STROKE_DEFAULT );
				
				final int x = loopToX( ce.loop );
				final int y = drawingRect.y2 - 7 - level * entityHeight;
				if ( ++level == MAX_LEVEL )
					level = 0;
				
				g.setColor( ds.getColor() );
				g.drawLine( x, y + 1, x, drawingRect.y2 );
				
				// Duration
				if ( durationType != DurationType.HIDDEN && ce.command.costTime > 0 ) {
					g.setStroke( STROKE_DEFAULT );
					g.setColor( ds.getColor() );
					final int x2 = loopToX( ce.loop + (int) ( ce.command.costTime * 16 ) );
					g.drawLine( x, y + 1, x2, y + 1 );
					if ( durationType == DurationType.BARS ) {
						g.setColor( barColor );
						g.fillRect( x + 1, y + 2, x2 - x, drawingRect.y2 - y - 2 );
					}
				}
			}
		}
		
		// 2nd round: icons / texts
		
		level = MAX_LEVEL > 2 ? 1 : 0;
		
		for ( final DataModel< CommandsChartDataSet > model : dataModelList ) {
			// One data set only
			final CommandsChartDataSet ds = model.getDataSetList().get( 0 );
			
			g.setColor( ds.getColor() );
			
			for ( final CmdEvent ce : ds.cmdEventLists ) {
				final int x = loopToX( ce.loop );
				final int y = drawingRect.y2 - 7 - level * entityHeight;
				if ( ++level == MAX_LEVEL )
					level = 0;
				
				if ( useIcons ) {
					final Icon icon = ce.command.getRicon().size( iconSize );
					final EventBox eb = new EventBox( ce );
					eb.width = icon.getIconWidth();
					eb.height = icon.getIconHeight();
					eb.x = x - eb.width / 2;
					eb.y = y - eb.height;
					icon.paintIcon( null, g, eb.x, eb.y );
					eventBoxList.add( eb );
				} else
					g.drawString( ce.command.text, x - g.getFontMetrics().stringWidth( ce.command.text ) / 2, y );
			}
		}
		
		g.setStroke( STROKE_DEFAULT );
	}
	
}
