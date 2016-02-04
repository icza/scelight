/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.belicza.andras.util;

import hu.scelight.service.env.Env;

/**
 * The NullAwareComparable allows either of the comparable objects to be <code>null</code> with the following behavior:
 * <ul>
 * <li>if both objects are <code>null</code>s, <code>compareTo()</code> returns 0
 * <li><code>null</code> "objects" are smaller than non-nulls
 * <li>if both objects are non-nulls, their <code>compareTo()</code> will be called
 * </ul>
 * 
 * @author Andras Belicza
 * 
 * @param < T > type of the comparable objects
 */
public class NullAwareComparable< T extends Comparable< T > > implements Comparable< NullAwareComparable< T > > {
	
	/** Reference to the comparable object. */
	public final T value;
	
	/**
	 * Creates a new {@link NullAwareComparable}.
	 * 
	 * @param value reference to the comparable object
	 */
	public NullAwareComparable( final T value ) {
		this.value = value;
	}
	
	@Override
	public int compareTo( final NullAwareComparable< T > otherNAC ) {
		if ( value == null && otherNAC.value == null )
			return 0;
		else if ( value == null && otherNAC.value != null )
			return -1;
		else if ( value != null && otherNAC.value == null )
			return 1;
		return value.compareTo( otherNAC.value );
	}
	
	/**
	 * Returns a {@link NullAwareComparable} whose {@link #toString()} formats the value as percent.
	 * 
	 * @param percent the percent value; might be null
	 * @return a NullAwareComparable that toString() formats the value as percent
	 */
	public static NullAwareComparable< Double > getPercent( final Double percent ) {
		// If all is unknown, wins+losses=0!
		return new NullAwareComparable< Double >( percent ) {
			@Override
			public String toString() {
				return value == null ? "-" : Env.LANG.formatNumber( value, 2 ) + "%";
			}
		};
	}
	
}
