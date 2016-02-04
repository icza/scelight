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

/**
 * Command duration visualization type.
 * 
 * @author Andras Belicza
 */
public enum DurationType {
	
	/** Duration hidden, not displayed. */
	HIDDEN( "Hidden" ),
	
	/** Duration visualized with lines. */
	LINES( "Lines" ),
	
	/** Duration visualized with lines and transparent bars. */
	BARS( "Bars" );
	
	
	/** Text value of the player controller. */
	public final String text;
	
	
	/**
	 * Creates a new {@link DurationType}.
	 * 
	 * @param text text value
	 */
	private DurationType( final String text ) {
		this.text = text;
	}
	
	@Override
	public String toString() {
		return text;
	}
	
	
	/** Cache of the values array. */
	public static final DurationType[] VALUES = values();
	
}
