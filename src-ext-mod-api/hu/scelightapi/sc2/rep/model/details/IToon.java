/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapi.sc2.rep.model.details;

import hu.scelight.sc2.rep.model.initdata.gamedesc.BnetLang;
import hu.scelightapi.sc2.rep.model.initdata.gamedesc.IRegion;
import hu.scelightapi.service.IFactory;
import hu.scelightapi.util.IStructView;

import java.net.URL;

/**
 * Toon (player handler / id).
 * 
 * <p>
 * Although this interface extends {@link IStructView}, an actual instance of {@link IToon} might not be constructed from a key-value structure (map).<br>
 * Generally it is true that if an {@link IToon} instance is constructed from a key-value structure, the methods of {@link IStructView} can be used, and in this
 * case the player name will not be present ({@link #getPlayerName()} will return <code>null</code>). Before using the methods of {@link IStructView}, one
 * should always test if {@link IStructView#getStruct()} returns a non-null value. The player name will be present if an {@link IToon} instance is not
 * constructed from a key-value structure.
 * </p>
 * 
 * @author Andras Belicza
 * 
 * @see IFactory#newToon(String)
 * @see IFactory#newToon(String, boolean)
 */
public interface IToon extends IStructView, Comparable< IToon > {
	
	// Structure field names and paths (all literal strings are interned)
	
	/** Id field name. */
	String F_ID         = "id";
	
	/** Program id field name. */
	String F_PROGRAM_ID = "programId";
	
	/** Realm field name. */
	String F_REALM      = "realm";
	
	/** Region field name. */
	String F_REGION     = "region";
	
	
	/**
	 * Tells if this toon is a zero-toon.
	 * 
	 * <p>
	 * A toon is a zero-toon if all parts are zeros. A toon might be a zero-toon if the controller is Computer or the game was played offline (not on the
	 * Battle.net).
	 * </p
	 * 
	 * @return true if this toon is a zero-toon; false otherwise
	 * 
	 * @since 1.1
	 */
	boolean isZero();
	
	/**
	 * Returns the region id.
	 * 
	 * @return the region id
	 */
	Integer getRegionId();
	
	/**
	 * Returns the program id. Should always be <code>"S2"</code> (StarCraft II).
	 * 
	 * @return the program id
	 */
	String getProgramId();
	
	/**
	 * Returns the realm id.
	 * 
	 * @return the realm id
	 */
	Integer getRealmId();
	
	/**
	 * Returns the player Id.
	 * 
	 * @return the player id
	 */
	Integer getId();
	
	/**
	 * Returns the region pointed by this toon (more specifically pointed by the region id).
	 * 
	 * @return the region pointed by this toon
	 */
	IRegion getRegion();
	
	/**
	 * Returns the optional player name if present.
	 * 
	 * @return the optional player name if present; <code>null</code> otherwise
	 */
	String getPlayerName();
	
	
	/**
	 * Returns the realm pointed/specified by this toon (more specifically pointed by the region and realm ids).
	 * 
	 * @return the realm pointed/specified by this toon
	 */
	IRealm getRealm();
	
	/**
	 * Returns the profile {@link URL} associated with this toon with preferably the user's preferred battle.net language.
	 * 
	 * @param playerName player name to be included in the URL
	 * @return the profile {@link URL} associated with this toon
	 */
	URL getProfileUrl( String playerName );
	
	/**
	 * Returns the profile {@link URL} associated with this toon with the specified preferred battle.net language.
	 * 
	 * <p>
	 * If the specified language is not supported on the region specified by this toon, the region's default language will be used.
	 * </p>
	 * 
	 * @param lang language of the profile page to return
	 * @param playerName player name to be included in the URL
	 * @return the profile {@link URL} associated with this toon
	 */
	URL getProfileUrl( BnetLang lang, String playerName );
	
	/**
	 * Produces a string representation in a form of:<br>
	 * If player name is present:
	 * 
	 * <blockquote><code>regionId-programId-realmId-id-playerName</code></blockquote>
	 * 
	 * else:
	 * 
	 * <blockquote><code>regionId-programId-realmId-id</code></blockquote>
	 * 
	 * <p>
	 * Example: <code>"2-S2-1-206154-DakotaFannin"</code>
	 * </p>
	 * 
	 * <p>
	 * The produced toon string is exactly in a form that is required by the factory method {@link IFactory#newToon(String)}.
	 * </p>
	 */
	@Override
	String toString();
	
	/**
	 * Provides an order based on:
	 * <ol>
	 * <li>region
	 * <li>realm
	 * <li>player id
	 * </ol>
	 * The player name is not used in comparison.
	 */
	@Override
	int compareTo( IToon t );
	
}
