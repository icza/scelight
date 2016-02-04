/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.sc2.balancedata;

import hu.belicza.andras.util.VersionView;
import hu.slsc2balancedata.r.BR;

import java.net.URL;
import java.util.concurrent.atomic.AtomicReference;

/**
 * An SC2 Balance data resource.
 * 
 * @author Andras Belicza
 */
public class BDRes implements Comparable< BDRes > {
	
	/** Name of the balance data pack. */
	public final String                         name;
	
	/** Minimum replay version this balance data resource applies to. */
	public final VersionView                    minVer;
	
	/** Maximum replay version this balance data resource applies to. */
	public final VersionView                    maxVer;
	
	/** {@link URL} pointing to the resource. */
	public final URL                            res;
	
	/** Lazily loaded balance data. */
	public final AtomicReference< BalanceData > balanceData = new AtomicReference<>();
	
	
	/**
	 * Creates a new {@link BDRes}.
	 * 
	 * @param name name of the balance data resource
	 */
	public BDRes( final String name ) {
		this.name = name;
		
		final int extIdx = name.lastIndexOf( '.' );
		final int dashIdx = name.indexOf( '-' );
		
		if ( dashIdx < 0 )
			maxVer = minVer = VersionView.fromString( name.substring( 0, extIdx ) );
		else {
			minVer = VersionView.fromString( name.substring( 0, dashIdx ) );
			maxVer = VersionView.fromString( name.substring( dashIdx + 1, extIdx ) );
		}
		
		res = BR.get( name );
	}
	
	
	/**
	 * Implements order based on {@link #minVer}.
	 */
	@Override
	public int compareTo( final BDRes bdr ) {
		return minVer.compareTo( bdr.minVer );
	}
	
}
