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

/**
 * Data set of charts.
 * 
 * <p>
 * Holds the data to be painted / visualized.
 * </p>
 * 
 * @author Andras Belicza
 * 
 * @see DataModel
 */
public class DataSet {
	
	/** Title of the data set. */
	protected String title;
	
	/**
	 * Returns the title of the data set.
	 * 
	 * @return the title of the data set
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * Sets the title of the data set
	 * 
	 * @param title title of the data set to be set
	 */
	public void setTitle( final String title ) {
		this.title = title;
	}
	
}
