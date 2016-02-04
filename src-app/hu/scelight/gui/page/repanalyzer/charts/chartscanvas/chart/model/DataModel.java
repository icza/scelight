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

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * Data model for a chart.
 * 
 * <p>
 * A {@link DataModel} may belong to a player, holding multiple {@link DataSet}s (e.g. normal APM, EAPM, XAPM).
 * </p>
 * 
 * @param <T> type of the data sets of the model
 * 
 * @author Andras Belicza
 */
public class DataModel< T extends DataSet > {
	
	/** Data model title. */
	protected final String    title;
	
	/** Color for the data model, e.g. for title visualization. */
	protected final Color     color;
	
	/** Data sets of the model . */
	protected final List< T > dataSetList = new ArrayList<>();
	
	/** An optional, custom user object which may be used to store additional info attached to the model. */
	protected final Object    userObject;
	
	
	/**
	 * Creates a new {@link DataModel}.
	 * 
	 * @param title title of the data model
	 * @param color color of the data model (e.g. for title visualization)
	 */
	public DataModel( final String title, final Color color ) {
		this( title, color, null );
	}
	
	/**
	 * Creates a new {@link DataModel}.
	 * 
	 * @param title title of the data model
	 * @param color color of the data model (e.g. for title visualization)
	 * @param userObject an optional, custom user object which may be used to store additional info attached to the model
	 */
	public DataModel( final String title, final Color color, final Object userObject ) {
		this.title = title;
		this.color = color;
		this.userObject = userObject;
	}
	
	/**
	 * Returns the title of the model.
	 * 
	 * @return the title of the model
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * Returns the color of the model.
	 * 
	 * @return the color of the model
	 */
	public Color getColor() {
		return color;
	}
	
	/**
	 * Adds a data set to the model.
	 * 
	 * @param dataSet data set to be added
	 */
	public void addDataSet( final T dataSet ) {
		dataSetList.add( dataSet );
	}
	
	/**
	 * Returns the data set list of the model.
	 * 
	 * @return the data set list of the model
	 */
	public List< T > getDataSetList() {
		return dataSetList;
	}
	
	/**
	 * Returns the optional, custom user object.
	 * 
	 * @return the optional, custom user object
	 */
	public Object getUserobObject() {
		return userObject;
	}
	
}
