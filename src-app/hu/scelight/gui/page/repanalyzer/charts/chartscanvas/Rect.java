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

import java.awt.Insets;
import java.awt.Rectangle;

/**
 * An int precision immutable rectangle.
 * 
 * <p>
 * Guarantees:
 * </p>
 * <ul>
 * <li><code>{@link #x1} &lt;= {@link #x2}</code>
 * <li><code>{@link #y1} &lt;= {@link #y2}</code>
 * <li><code>{@link #width} >= 0</code>
 * <li><code>{@link #height} >= 0</code>
 * </ul>
 * 
 * 
 * <p>
 * Problems with {@link Rectangle}:
 * </p>
 * <ul>
 * <li><b>Major concept bug:</b> {@link Rectangle#getMaxX()} for example returns <code>x + width</code> instead of <code>x + width - 1</code> (it is OK for
 * double precision but it is an <i>error</i> for int precision!)
 * <li>It always checks for overflow and performs safe operations which require to convert to long and double.
 * </ul>
 * 
 * @author Andras Belicza
 */
public class Rect {
	
	/** "Empty" rectangle: having top left point of (0,0) and zero size. */
	public static Rect EMPTY = new Rect();
	
	
	// Base properties
	
	/** X coordinate of the top left point of the rectangle. */
	public final int   x1;
	
	/** Y coordinate of the top left point of the rectangle. */
	public final int   y1;
	
	/** Width of the rectangle. */
	public final int   width;
	
	/** Height of the rectangle. */
	public final int   height;
	
	// Derived properties
	
	/** X coordinate of the bottom right point of the rectangle. */
	public final int   x2;
	
	/** Y coordinate of the bottom right point of the rectangle. */
	public final int   y2;
	
	/** Delta x between x2 and x1. */
	public final int   dx;
	
	/** Delta y between y2 and y1. */
	public final int   dy;
	
	
	/**
	 * Creates a new {@link Rect} with (0,0) point and 0 size.
	 * <p>
	 * Use the {@link #EMPTY} constant instead of this.
	 * </p>
	 */
	public Rect() {
		this( 0, 0, 0, 0 );
	}
	
	/**
	 * Creates a new {@link Rect}.
	 * 
	 * @param x1 x coordinate of the top left point of the rectangle.
	 * @param y1 y coordinate of the top left point of the rectangle.
	 * @param width width of the rectangle
	 * @param height height of the rectangle
	 * 
	 * @throws IllegalArgumentException if width or height are negative
	 */
	public Rect( final int x1, final int y1, final int width, final int height ) {
		if ( width < 0 )
			throw new IllegalArgumentException( "Width cannot be negative!" );
		if ( height < 0 )
			throw new IllegalArgumentException( "Height cannot be negative!" );
		
		this.x1 = x1;
		this.y1 = y1;
		this.width = width;
		this.height = height;
		
		this.x2 = x1 + ( dx = width - 1 );
		this.y2 = y1 + ( dy = height - 1 );
	}
	
	/**
	 * Creates a new {@link Rect}.
	 * 
	 * @param r {@link Rectangle} whose point and size to use
	 */
	public Rect( final Rectangle r ) {
		this( r.x, r.y, r.width, r.height );
	}
	
	/**
	 * Tells if this rectangle intersects the specified other rectangle.
	 * 
	 * @param r rectangle to test intersection with
	 * @return true if this rectangle intersects the specified other rectangle; false otherwise
	 */
	public boolean intersects( final Rect r ) {
		return !( x1 > r.x2 || x2 < r.x1 || y1 > r.y2 || y2 < r.y1 );
	}
	
	/**
	 * Returns a new {@link Rect} having the intersection with the specified rectangle.
	 * 
	 * <p>
	 * {@link #EMPTY} is returned if the 2 rectangles do not intersect.
	 * </p>
	 * 
	 * @param r rectangle to intersect with
	 * @return a new {@link Rect} having the intersection with the specified rectangle
	 */
	public Rect intersection( final Rect r ) {
		return intersects( r ) ? new Rect( Math.max( x1, r.x1 ), Math.max( y1, r.y1 ), Math.min( x2, r.x2 ), Math.min( y2, r.y2 ) ) : EMPTY;
	}
	
	/**
	 * Returns a new {@link Rect} with the specified insets applied.
	 * 
	 * @param insets insets to apply (exclude)
	 * @return a new {@link Rect} with the specified insets applied
	 */
	public Rect applyInsets( final Insets insets ) {
		final int width = this.width - insets.left - insets.right;
		final int height = this.height - insets.top - insets.bottom;
		return width < 0 || height < 0 ? EMPTY : new Rect( x1 + insets.left, y1 + insets.top, width, height );
	}
	
	/**
	 * Tells if this rectangle is empty (it's area is zero).
	 * 
	 * @return true if this rectangle is emplty; false otherwise
	 */
	public boolean isEmpty() {
		return width == 0 || height == 0;
	}
	
	@Override
	public boolean equals( final Object o ) {
		if ( !( o instanceof Rect ) )
			return false;
		
		final Rect r = (Rect) o;
		return x1 == r.x1 && y1 == r.y1 && width == r.width && height == r.height;
	}
	
	@Override
	public int hashCode() {
		return x1 + 31 * ( y1 + 31 * ( width + 31 * height ) );
	}
	
	@Override
	public String toString() {
		return "Rect[x1=" + x1 + ", y1=" + y1 + ", width=" + width + ", height=" + height + "]";
	}
	
}
