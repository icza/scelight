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
import hu.scelight.gui.page.repanalyzer.charts.chartscanvas.chart.model.ControlGroupChartDataSet;
import hu.scelight.gui.page.repanalyzer.charts.chartscanvas.chart.model.DataModel;
import hu.scelight.sc2.rep.model.gameevents.ControlGroupUpdateEvent;
import hu.scelight.sc2.rep.repproc.RepProcessor;
import hu.scelight.sc2.rep.repproc.SelectionTracker;
import hu.scelightapi.sc2.rep.model.IEvent;
import hu.scelightapi.sc2.rep.model.gameevents.IControlGroupUpdateEvent;
import hu.scelightapi.sc2.rep.model.gameevents.IGameEvents;
import hu.scelightapi.sc2.rep.model.gameevents.selectiondelta.ISelectionDeltaEvent;
import hu.scelightapi.sc2.rep.repproc.IRepProcessor;

import java.awt.FontMetrics;
import java.awt.Graphics2D;

/**
 * A time chart which displays the usage of control groups. Its data model is a series of control group usage events, grouped by the control group number.
 * 
 * @author Andras Belicza
 */
public class ControlGroupsChart extends TimeChart< ControlGroupChartDataSet > {
	
	/**
	 * Creates a new {@link ControlGroupsChart}.
	 * 
	 * @param repProc reference to the rep processor
	 */
	public ControlGroupsChart( final RepProcessor repProc ) {
		super( repProc );
	}
	
	@Override
	public void paint( final Graphics2D g, final Rect bounds, final Rect visibleRect ) {
		super.paint( g, bounds, visibleRect );
	}
	
	/** Y coordinates for the control groups. */
	private final int[]           groupYs      = new int[ 10 ];
	
	/** Control group labels (control group numbers as strings). */
	private static final String[] GROUP_LABELS = new String[ 10 ];
	static {
		for ( int group = 0; group < 10; group++ )
			GROUP_LABELS[ group ] = Integer.toString( group == 10 ? 0 : group );
	}
	
	/**
	 * Also paints the labels of the Y axis and assist lines.
	 */
	@Override
	protected void paintAxesLabels() {
		// X (time) axis
		super.paintAxesLabels();
		
		// Y axis
		
		// Determine which control groups were used and paint only those
		final boolean[] groupUseds = new boolean[ 10 ];
		for ( final DataModel< ControlGroupChartDataSet > model : dataModelList )
			for ( final ControlGroupChartDataSet dataSet : model.getDataSetList() ) {
				for ( int group = 0; group < 10; group++ )
					groupUseds[ group ] |= !dataSet.controlGroupEventLists[ group ].isEmpty();
			}
		
		fillGroupYs();
		
		final FontMetrics fontMetrics = g.getFontMetrics();
		
		g.setColor( COLOR_AXIS_LABELS );
		for ( int group = 0; group < 10; group++ ) {
			if ( !groupUseds[ group ] )
				continue;
			
			g.drawString( GROUP_LABELS[ group ], drawingRect.x1 - fontMetrics.stringWidth( GROUP_LABELS[ group ] ) - 1, groupYs[ group ] );
		}
	}
	
	/**
	 * Calculates the y coordinates of the control groups.
	 */
	protected void fillGroupYs() {
		final int fontAscent = g.getFontMetrics().getAscent();
		
		// Is there enough space?
		if ( drawingRect.dy > 10 * fontAscent ) {
			// Distribute equally, groups at the center of their group's space
			final int groupSpace = drawingRect.dy / 10;
			final int groupCenterOffset = ( groupSpace + fontAscent ) / 2 - 1;
			
			for ( int group = 0; group < 10; group++ ) {
				// We want group 1 to be at top and group 0 to appear after group 9
				groupYs[ group ] = drawingRect.y1 + ( group == 0 ? 9 : group - 1 ) * groupSpace + groupCenterOffset;
			}
		} else {
			// Put first and last group at the top and bottom of the drawing area, distribute the rest equally
			final int firstY = drawingRect.y1 + fontAscent - 1;
			final int lastY = Math.max( firstY, drawingRect.y2 - 2 );
			
			for ( int group = 0; group < 10; group++ ) {
				// We want group 1 to be at top and group 0 to appear after group 9
				groupYs[ group ] = firstY + ( lastY - firstY ) * ( group == 0 ? 9 : group - 1 ) / 9;
			}
		}
	}
	
	@Override
	protected void paintData() {
		eventBoxList.clear();
		
		final FontMetrics fontMetrics = g.getFontMetrics();
		
		for ( final DataModel< ControlGroupChartDataSet > model : dataModelList ) {
			int type = 0;
			for ( final ControlGroupChartDataSet dataSet : model.getDataSetList() ) {
				g.setColor( dataSet.getColor() );
				
				for ( int group = 0; group < 10; group++ ) {
					final String label = GROUP_LABELS[ group ];
					final int y = groupYs[ group ]; // This has been filled during painting axis labels
					final int width = fontMetrics.stringWidth( label );
					final int height = fontMetrics.getAscent();
					final int xOffset = fontMetrics.stringWidth( label ) / 2;
					
					for ( final ControlGroupUpdateEvent event : dataSet.controlGroupEventLists[ group ] ) {
						final EventBox eb = createEventBox( event );
						eb.x = loopToX( event.loop ) - xOffset;
						eb.y = y - height + 2;
						eb.width = width;
						eb.height = height;
						eventBoxList.add( eb );
						if ( type == 0 ) {
							// Assign
							g.setColor( dataSet.getColor() );
							g.fillRect( eb.x, eb.y, width, height );
							g.setColor( ChartsCanvas.COLOR_BACKGROUND );
							g.drawString( label, eb.x, y );
						} else {
							// Select
							g.drawString( label, eb.x, y );
						}
					}
				}
				
				type++;
			}
		}
	}
	
	/**
	 * Creates and returns an {@link EventBox} for the specified control group update event whose tool tip also includes the selection that is being recalled or
	 * assigned.
	 * 
	 * @param cgEvent control group update event to create an event box for
	 * @return an {@link EventBox} for the specified control group update event
	 */
	@SuppressWarnings( "serial" )
	private static EventBox createEventBox( final ControlGroupUpdateEvent cgEvent ) {
		// I intentionally used the param name "cgEvent" because we're extending the EventBox class here which also has an attribute named "event"!
		return new EventBox( cgEvent ) {
			@Override
			public String getToolTipText( final IRepProcessor repProc ) {
				final SelectionTracker st = new SelectionTracker();
				
				for ( final IEvent ie : repProc.getReplay().getGameEvents().getEvents() ) {
					if ( ie.getUserId() != cgEvent.userId )
						continue;
					
					switch ( ie.getId() ) {
						case IGameEvents.ID_CONTROL_GROUP_UPDATE :
							st.processControlGroupUpdate( (IControlGroupUpdateEvent) ie );
							break;
						case IGameEvents.ID_SELECTION_DELTA :
							st.processSelectionDelta( (ISelectionDeltaEvent) ie );
							break;
						default :
							continue;
					}
					
					if ( ie == cgEvent ) {
						// We're done. Now produce return value.
						return "<html><body style='width:400px;'>" + super.getToolTipText( repProc ) + "<br><br><b>Units:</b><br>"
						        + st.getSelectionString( cgEvent.getGroupIndex(), repProc ) + "</body></html>";
					}
				}
				
				// We never really end up here but if we would, return the value by the super implementation.
				return super.getToolTipText( repProc );
			}
		};
	}
	
}
