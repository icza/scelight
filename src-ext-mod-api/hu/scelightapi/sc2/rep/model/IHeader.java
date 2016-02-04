/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapi.sc2.rep.model;

import hu.scelightapi.util.IStructView;
import hu.scelightapi.util.IVersionView;

/**
 * Interface modeling the header of StarCraft II replays.
 * 
 * @author Andras Belicza
 */
public interface IHeader extends IStructView {
	
	// Structure field names and paths (all literal strings are interned)
	
	/** Version struct field name. */
	String   F_VERSION            = "version";
	
	/** Major version field path. */
	String[] P_MAJOR              = { F_VERSION, "major" };
	
	/** Minor version field path. */
	String[] P_MINOR              = { F_VERSION, "minor" };
	
	/** Revision version field path. */
	String[] P_REVISION           = { F_VERSION, "revision" };
	
	/** Build version field path. */
	String[] P_BUILD              = { F_VERSION, "build" };
	
	/** Base build version field path. */
	String[] P_BASE_BUILD         = { F_VERSION, "baseBuild" };
	
	/** Version flags field path. */
	String[] P_FLAGS              = { F_VERSION, "flags" };
	
	/** Elapsed game loops field name. */
	String   F_ELAPSED_GAME_LOOPS = "elapsedGameLoops";
	
	/** Type field name. */
	String   F_TYPE               = "type";
	
	/** Use scaled time field name. Available only from 2.0.10. */
	String   F_USE_SCALED_TIME    = "useScaledTime";
	
	
	/**
	 * Returns the replay version major part.
	 * 
	 * @return the replay version major part
	 */
	Integer getMajor();
	
	/**
	 * Returns the replay version minor part.
	 * 
	 * @return the replay version minor part
	 */
	Integer getMinor();
	
	/**
	 * Returns the replay version revision part.
	 * 
	 * @return the replay version revision part
	 */
	Integer getRevision();
	
	/**
	 * Returns the replay version build part.
	 * 
	 * @return the replay version build part
	 */
	Integer getBuild();
	
	/**
	 * Returns the replay base build.
	 * 
	 * @return the replay base build
	 */
	Integer getBaseBuild();
	
	/**
	 * Returns the elapsed game loops.
	 * 
	 * @return the elapsed game loops
	 */
	Integer getElapsedGameLoops();
	
	/**
	 * Returns the version flags.
	 * 
	 * @return the version flags
	 */
	Integer getVersionFlags();
	
	/**
	 * Returns the type.
	 * 
	 * @return the type
	 */
	Integer getType();
	
	/**
	 * Returns whether to use scaled time.
	 * 
	 * @return true if to use scaled time; false otherwise
	 */
	Boolean getUseScaledTime();
	
	/**
	 * Returns an {@link IVersionView} of the replay version.
	 * 
	 * @return an {@link IVersionView} of the replay version
	 */
	IVersionView getVersionView();
	
	/**
	 * Returns the (full) version string (including build number).
	 * 
	 * @return the (full) version string (including build number)
	 */
	String versionString();
	
	/**
	 * Returns the version string.
	 * 
	 * @param full tells if full version string is to be returned (including the build number)
	 * @return the version string
	 */
	String versionString( boolean full );
	
}
