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

import hu.scelightapi.util.IVersionView;

import java.util.Arrays;

/**
 * A class to represent a version with any number of parts.
 * 
 * <p>
 * Instances are IMMUTABLE, but the array passed in the constructor {@link #VersionView(int...)} is not cloned!
 * </p>
 * 
 * @author Andras Belicza
 * 
 * @see hu.sllauncher.bean.VersionBean
 */
public class VersionView implements IVersionView {
	
	/** Parts of the version. */
	private final int[] parts;
	
	/**
	 * Creates a new {@link VersionView}.
	 * 
	 * @param parts parts of the version
	 */
	public VersionView( final int... parts ) {
		this.parts = parts;
	}
	
	/**
	 * Creates a {@link VersionView} from its string representation.
	 * 
	 * @param s string representation of the version bean
	 * @return the version view parsed from the specified string, or <code>null</code> if the specified string is not a valid version string
	 */
	public static VersionView fromString( final String s ) {
		final String[] stringParts = s.split( "\\." );
		final int[] parts = new int[ stringParts.length ];
		
		for ( int i = 0; i < stringParts.length; i++ )
			try {
				parts[ i ] = Integer.parseInt( stringParts[ i ] );
			} catch ( final Exception e ) {
				return null;
			}
		
		return new VersionView( parts );
	}
	
	@Override
	public int length() {
		return parts.length;
	}
	
	@Override
	public int part( final int i ) {
		return parts[ i ];
	}
	
	@Override
	public int compareTo( final IVersionView v ) {
		return compareTo( v, true );
	}
	
	@Override
	public int compareTo( final IVersionView v, final boolean strict ) {
		final int minLength = Math.min( parts.length, v.length() );
		
		for ( int i = 0; i < minLength; i++ ) {
			final int result = parts[ i ] - v.part( i );
			if ( result != 0 )
				return result;
		}
		
		// All "common" parts are equal.
		// If strict mode, the longer version wins, else declare tie!
		return strict ? parts.length - v.length() : 0;
	}
	
	@Override
	public boolean equals( final Object obj ) {
		if ( this == obj )
			return true;
		
		return obj instanceof IVersionView && compareTo( (IVersionView) obj, true ) == 0;
	}
	
	/** Cached hash code. */
	private int hashCode;
	
	@Override
	public int hashCode() {
		if ( hashCode == 0 ) {
			hashCode = Arrays.hashCode( parts );
			
			// Ensure not zero to avoid recalculation
			if ( hashCode == 0 )
				hashCode = 1;
		}
		
		return hashCode;
	}
	
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		
		for ( final int part : parts ) {
			if ( sb.length() > 0 )
				sb.append( '.' );
			sb.append( part );
		}
		
		return sb.toString();
	}
	
}
