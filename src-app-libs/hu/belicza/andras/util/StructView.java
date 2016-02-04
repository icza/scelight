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

import hu.scelightapi.util.IStructView;

import java.util.Map;

/**
 * A class which has a source structure as a {@link Map} and provides easy access to its content.
 * 
 * @author Andras Belicza
 */
public class StructView implements IStructView {
	
	/** Source data structure. */
	protected final Map< String, Object > struct;
	
	/**
	 * Creates a new {@link StructView}.
	 * 
	 * @param struct data structure
	 */
	public StructView( final Map< String, Object > struct ) {
		this.struct = struct;
	}
	
	@Override
	@SuppressWarnings( "unchecked" )
	public < T > T get( final String name ) {
		return (T) struct.get( name );
	}
	
	@Override
	@SuppressWarnings( "unchecked" )
	public < T > T get( final String[] path ) {
		// Last name index
		final int lastNameIdx = path.length - 1;
		
		Map< String, Object > struct2 = struct;
		for ( int i = 0; i < lastNameIdx; i++ )
			if ( ( struct2 = (Map< String, Object >) struct2.get( path[ i ] ) ) == null )
				return null;
		
		return struct2 == null ? null : (T) struct2.get( path[ lastNameIdx ] );
	}
	
	@Override
	public Map< String, Object > getStruct() {
		return struct;
	}
	
}
