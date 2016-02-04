/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.sc2.rep.model.details;

import hu.belicza.andras.util.StructView;
import hu.scelight.sc2.rep.model.initdata.gamedesc.BnetLang;
import hu.scelight.sc2.rep.model.initdata.gamedesc.Region;
import hu.scelight.service.env.Env;
import hu.scelight.service.settings.Settings;
import hu.scelight.util.Utils;
import hu.scelightapi.sc2.rep.model.details.IToon;
import hu.scelightapi.sc2.rep.model.initdata.gamedesc.IRegion;

import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Toon (player handler / id).
 * 
 * @author Andras Belicza
 */
public class Toon extends StructView implements IToon {
	
	// Eagerly initialized "cached" field values
	
	/** Region id. */
	public final Integer regionId;
	
	/** Program id. Should always be <code>"S2"</code> (StarCraft II). */
	public final String  programId;
	
	/** Realm id. */
	public final Integer realmId;
	
	/** Player Id. */
	public final Integer id;
	
	/** Region pointed by this toon. */
	public final Region  region;
	
	/** Optional player name extracted from the string representation (see {@link #Toon(String)}). */
	public final String  playerName;
	
	
	// Lazily initialized "cached" field values
	
	
	/**
	 * Creates a new {@link Toon}.
	 * 
	 * @param struct header data structure
	 */
	public Toon( final Map< String, Object > struct ) {
		super( struct );
		
		regionId = get( F_REGION );
		// Strip off leading zeros
		programId = Utils.stripOffLeadingZeros( get( F_PROGRAM_ID ).toString() );
		realmId = get( F_REALM );
		id = this.< Long > get( F_ID ).intValue();
		
		region = Region.fromRegionId( regionId );
		
		playerName = null;
	}
	
	/**
	 * Creates a new {@link Toon}.
	 * 
	 * @param toon toon string representation to parse and initialize from; exactly the form produced by {@link #toString()}
	 * 
	 * @throws IllegalArgumentException if the specified toon string is invalid
	 * 
	 * @see #toString()
	 * @see #Toon(String, boolean)
	 */
	public Toon( final String toon ) throws IllegalArgumentException {
		this( toon, true );
	}
	
	/**
	 * Creates a new {@link Toon}.
	 * 
	 * @param toon toon string representation to parse and initialize from; exactly the form produced by {@link #toString()}
	 * @param parsePlayerName tells if player name is to be parsed from the toon string if present
	 * 
	 * @throws IllegalArgumentException if the specified toon string is invalid
	 * 
	 * @see #toString()
	 * @see #Toon(String)
	 */
	public Toon( final String toon, final boolean parsePlayerName ) throws IllegalArgumentException {
		super( null );
		
		try {
			// Example: 2-S2-1-206154
			// or: 2-S2-1-206154-DakotaFannin
			
			final StringTokenizer st = new StringTokenizer( toon, "-" );
			
			regionId = Integer.valueOf( st.nextToken() );
			programId = st.nextToken();
			if ( !"S2".equals( programId ) )
				throw new Exception( "Unexpected program id: '" + programId + "' (was expecting 'S2')!" );
			realmId = Integer.valueOf( st.nextToken() );
			id = Integer.valueOf( st.nextToken() );
			
			playerName = parsePlayerName && st.hasMoreTokens() ? st.nextToken() : null;
			
			region = Region.fromRegionId( regionId );
		} catch ( final Exception e ) {
			throw new IllegalArgumentException( e );
		}
	}
	
	@Override
	public boolean isZero() {
		return regionId == 0 && realmId == 0 && id == 0;
	}
	
	@Override
	public Integer getRegionId() {
		return regionId;
	}
	
	@Override
	public String getProgramId() {
		return programId;
	}
	
	@Override
	public Integer getRealmId() {
		return realmId;
	}
	
	@Override
	public Integer getId() {
		return id;
	}
	
	@Override
	public IRegion getRegion() {
		return region;
	}
	
	@Override
	public String getPlayerName() {
		return playerName;
	}
	
	@Override
	public Realm getRealm() {
		return region.getRealm( realmId );
	}
	
	@Override
	public URL getProfileUrl( final String playerName ) {
		BnetLang lang;
		if ( region.langSet.contains( lang = Env.APP_SETTINGS.get( Settings.PREFERRED_BNET_LANG_1 ) ) )
			;
		else if ( region.langSet.contains( lang = Env.APP_SETTINGS.get( Settings.PREFERRED_BNET_LANG_2 ) ) )
			;
		else if ( region.langSet.contains( lang = Env.APP_SETTINGS.get( Settings.PREFERRED_BNET_LANG_3 ) ) )
			;
		else
			lang = region.defaultLang;
		
		return getProfileUrl( lang, playerName );
	}
	
	@Override
	public URL getProfileUrl( BnetLang lang, final String playerName ) {
		if ( !region.langSet.contains( lang ) )
			lang = region.defaultLang;
		
		try {
			final StringBuilder sb = new StringBuilder( 50 );
			sb.append( "sc2/" ).append( lang.langCode ).append( "/profile/" ).append( id ).append( '/' );
			sb.append( realmId ).append( '/' ).append( URLEncoder.encode( playerName, "UTF-8" ) ).append( '/' );
			
			return new URL( region.bnetUrl, sb.toString() );
		} catch ( final Exception e ) {
			Env.LOGGER.error( "Failed to create profile URL for toon: " + this + ", player name: " + playerName, e );
			return null;
		}
	}
	
	@Override
	public boolean equals( final Object obj ) {
		if ( !( obj instanceof Toon ) )
			return false;
		
		final Toon t = (Toon) obj;
		return id.intValue() == t.id.intValue() && realmId.intValue() == t.realmId.intValue() && regionId.intValue() == t.regionId.intValue();
	}
	
	@Override
	public int hashCode() {
		return 31 * ( 31 * ( 31 + id ) + realmId ) + regionId;
	}
	
	/**
	 * Produces a string representation in a form that is required by the {@link #Toon(String)} constructor.
	 * 
	 * @see #Toon(String)
	 * @see #Toon(String, boolean)
	 */
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder( 32 );
		
		sb.append( regionId ).append( '-' ).append( programId ).append( '-' ).append( realmId ).append( '-' ).append( id );
		if ( playerName != null )
			sb.append( '-' ).append( playerName );
		
		return sb.toString();
	}
	
	@Override
	public int compareTo( final IToon t ) {
		int result = regionId.compareTo( t.getRegionId() );
		if ( result != 0 )
			return result;
		
		result = realmId.compareTo( t.getRealmId() );
		if ( result != 0 )
			return result;
		
		return id.compareTo( t.getId() );
	}
	
}
