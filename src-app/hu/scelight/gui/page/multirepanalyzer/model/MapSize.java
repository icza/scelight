/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.gui.page.multirepanalyzer.model;

import hu.sllauncher.gui.comp.table.renderer.ProgressBarView;

/**
 * Map size.
 * 
 * <p>
 * Implementation is IMMUTABLE.
 * </p>
 * 
 * @author Andras Belicza
 */
public class MapSize implements Comparable< MapSize > {
	
	/** Area specified by the map size. */
	public final int    area;
	
	/** String value of the map size. */
	public final String stringValue;
	
	/**
	 * Creates a new {@link MapSize}.
	 * 
	 * @param width width of the map
	 * @param height height of the map
	 */
	public MapSize( final int width, final int height ) {
		area = width * height;
		stringValue = width + "x" + height;
	}
	
	/**
	 * Returns the map size progress bar.
	 * 
	 * @param maxArea max map area
	 * @return the map size progress bar
	 */
	public ProgressBarView getSizeBar( final int maxArea ) {
		return new ProgressBarView( area, maxArea, stringValue );
	}
	
	/**
	 * Returns the string representation of the map size.
	 * 
	 * <pre>
	 * WIDTHxHEIGHT
	 * </pre>
	 */
	@Override
	public String toString() {
		return stringValue;
	}
	
	/**
	 * Implements an order of the area defined by the map size.
	 */
	@Override
	public int compareTo( final MapSize ms ) {
		return area - ms.area;
	}
	
}
