/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllauncher.gui.comp.logo;

import java.awt.Color;
import java.awt.Point;

/**
 * A star.
 * 
 * @author Andras Belicza
 */
class Star {
	
	/** X coordinate of the star. */
	public double x = Double.MAX_VALUE;
	
	/** Y coordinate of the star. */
	public double y;
	
	/** Radius of the star. */
	public double r;
	
	/** X coordinate of the velocity of the star. */
	public double vx;
	
	/** Y coordinate of the velocity of the star. */
	public double vy;
	
	/** Color of the star. */
	public Color  color;
	
	/**
	 * Creates a new {@link Star}.
	 */
	public Star() {
	}
	
	/**
	 * Creates a new {@link Star}.
	 * 
	 * @param target default location to be set
	 */
	public Star( final Point.Double target ) {
		setLocation( target );
	}
	
	/**
	 * Sets the location of the star.
	 * 
	 * @param target target location to be set
	 */
	public void setLocation( final Point.Double target ) {
		x = target.x;
		y = target.y;
	}
	
	/**
	 * Moves the star according to its speed.
	 */
	public void move() {
		x += vx;
		y += vy;
	}
	
}
