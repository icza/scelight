/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.sc2.rep.model;

import hu.belicza.andras.util.StructView;
import hu.belicza.andras.util.VersionView;
import hu.scelightapi.sc2.rep.model.IHeader;

import java.util.Map;

/**
 * StarCraft II Replay header.
 * 
 * @author Andras Belicza
 */
public class Header extends StructView implements IHeader {
	
	// Eagerly initialized "cached" values
	
	
	/** Major version. */
	public final Integer          major;
	
	/** Minor version. */
	public final Integer          minor;
	
	/** Revision version. */
	public final Integer          revision;
	
	/** Build version. */
	public final Integer          build;
	
	/** Base build version. */
	public final Integer          baseBuild;
	
	
	// Lazily initialized "cached" values
	
	
	/** Elapsed game loops. */
	private Integer               elapsedGameLoops;
	
	
	/** {@link VersionView} of the replay version. */
	private VersionView           versionView;
	
	/**
	 * Creates a new {@link Header}.
	 * 
	 * @param struct header data structure
	 */
	public Header( final Map< String, Object > struct ) {
		super( struct );
		
		major = get( P_MAJOR );
		minor = get( P_MINOR );
		revision = get( P_REVISION );
		build = get( P_BUILD );
		baseBuild = get( P_BASE_BUILD );
	}
	
	@Override
	public Integer getMajor() {
		return major;
	}
	
	@Override
	public Integer getMinor() {
		return minor;
	}
	
	@Override
	public Integer getRevision() {
		return revision;
	}
	
	@Override
	public Integer getBuild() {
		return build;
	}
	
	@Override
	public Integer getBaseBuild() {
		return baseBuild;
	}
	
	@Override
	public Integer getElapsedGameLoops() {
		if ( elapsedGameLoops == null )
			elapsedGameLoops = get( F_ELAPSED_GAME_LOOPS );
		
		return elapsedGameLoops;
	}
	
	@Override
	public Integer getVersionFlags() {
		return get( P_FLAGS );
	}
	
	@Override
	public Integer getType() {
		return get( F_TYPE );
	}
	
	@Override
	public Boolean getUseScaledTime() {
		return get( F_USE_SCALED_TIME );
	}
	
	@Override
	public VersionView getVersionView() {
		if ( versionView == null )
			versionView = new VersionView( major, minor, revision, build );
		
		return versionView;
	}
	
	@Override
	public String versionString() {
		return versionString( true );
	}
	
	@Override
	public String versionString( final boolean full ) {
		final StringBuilder sb = new StringBuilder().append( major ).append( '.' ).append( minor ).append( '.' ).append( revision );
		
		if ( full )
			sb.append( '.' ).append( build );
		
		return sb.toString();
	}
	
}
