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

import hu.scelight.sc2.rep.s2prot.Event;
import hu.scelightapi.sc2.rep.repproc.IRepProcessor;

import java.awt.Rectangle;

/**
 * A rectangular area of a chart which is bound to an {@link Event}.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class EventBox extends Rectangle {
	
	/** The bounded event. */
	public final Event event;
	
	/**
	 * Creates a new {@link EventBox}.
	 * 
	 * @param event the bounded event
	 */
	public EventBox( final Event event ) {
		this.event = event;
	}
	
	/**
	 * Returns a tool tip text for this event box.
	 * 
	 * @param repProc reference to the rep processor in case additional info is required by the "boxed" event
	 * @return a tool tip text for this event box
	 */
	public String getToolTipText( final IRepProcessor repProc ) {
		return event.getParameters( repProc );
	}
	
}
