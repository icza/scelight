/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.gui.page.multirepanalyzer.model;

import hu.belicza.andras.util.VersionView;
import hu.scelight.gui.page.multirepanalyzer.MultiRepAnalyzerComp;
import hu.scelight.sc2.map.cache.MapImageCache;
import hu.scelight.sc2.rep.model.Replay;
import hu.scelight.sc2.rep.model.attributesevents.GameMode;
import hu.scelight.sc2.rep.model.initdata.gamedesc.ExpansionLevel;
import hu.scelight.sc2.rep.model.initdata.gamedesc.GameDescription;
import hu.scelight.sc2.rep.model.initdata.gamedesc.GameSpeed;
import hu.scelight.sc2.rep.model.initdata.gamedesc.Region;
import hu.scelight.sc2.rep.model.initdata.userinitdata.League;
import hu.scelight.sc2.rep.repproc.Format;
import hu.scelight.sc2.rep.repproc.RepProcessor;
import hu.scelightapi.sc2.rep.model.details.IToon;
import hu.sllauncher.gui.icon.LRIcon;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Date;

/**
 * Describes / models a game.
 * 
 * <p>
 * Implementation is IMMUTABLE (excluding the fact that {@link Date} is not immutable...).
 * </p>
 * 
 * @author Andras Belicza
 */
public class Game implements Comparable< Game > {
	
	/** Replay version the tracker events are available from */
	private static final VersionView TRACKER_EVENTS_VERSION = new VersionView( 2, 0, 8 );
	
	/** Replay file. */
	public final Path                file;
	
	/** Region of the game. */
	public final Region              region;
	
	/** Expansion level of the game. */
	public final ExpansionLevel      expansion;
	
	/** Name of the map. */
	public final String              map;
	
	/** Map size. */
	public final MapSize             mapSize;
	
	/** Date of the game. */
	public final Date                date;
	
	/** Elapsed game time in ms. */
	public final long                lengthMs;
	
	/** Game speed. */
	public final GameSpeed           gameSpeed;
	
	/** Format of the game. */
	public final Format              format;
	
	/** Game mode of the game. */
	public final GameMode            gameMode;
	
	/** Ricon of the map of the game (for visualizing in tables). */
	public final LRIcon              ricon;
	
	/** Participants (players) of the game. */
	public final Part[]              parts;
	
	/** Tells if participants in the game have league info. */
	public final boolean             hasLeagues;
	
	/** Tells if the game has info calculated from tracker events. */
	public final boolean             hasTrackerInfo;
	
	/** Cached number of teams. */
	private final int                teamsCount;
	
	
	/**
	 * Creates a new {@link Game}.
	 * 
	 * @param repProc replay processor to init from
	 * @param multiRepAnalyzerComp reference to the Multi-replay Analyzer component
	 */
	public Game( final RepProcessor repProc, final MultiRepAnalyzerComp multiRepAnalyzerComp ) {
		file = repProc.file;
		
		final Replay r = repProc.replay;
		final GameDescription gd = r.initData.getGameDescription();
		region = gd.getRegion();
		expansion = gd.getExpansionLevel();
		map = r.details.title;
		mapSize = new MapSize( gd.getMapSizeX(), gd.getMapSizeY() );
		date = r.details.getTime();
		lengthMs = repProc.getLengthMs();
		gameSpeed = repProc.gameSpeed;
		format = repProc.getFormat();
		gameMode = repProc.replay.attributesEvents.getGameMode();
		
		ricon = MapImageCache.getMapImage( repProc );
		
		parts = new Part[ repProc.playerUsers.length ];
		int lastTeam = -1;
		int teamsCount = 0;
		for ( int i = parts.length - 1; i >= 0; i-- ) {
			parts[ i ] = new Part( repProc.playerUsers[ i ], repProc, this, multiRepAnalyzerComp );
			if ( lastTeam != parts[ i ].team ) {
				teamsCount++;
				lastTeam = parts[ i ].team;
			}
		}
		hasLeagues = repProc.replay.header.major >= 2;
		hasTrackerInfo = repProc.replay.header.getVersionView().compareTo( TRACKER_EVENTS_VERSION, false ) >= 0;
		this.teamsCount = teamsCount;
	}
	
	/**
	 * Returns a participant of the game specified by his/her toon.
	 * 
	 * @param toon toon to find and return a participant for
	 * @return a participant of the game specified by his/her toon; or <code>null</code> if no participant found with the specified toon
	 */
	public Part getPartByToon( final IToon toon ) {
		for ( final Part part : parts )
			if ( part.toon.equals( toon ) )
				return part;
		
		return null;
	}
	
	/**
	 * Returns the races of teams.
	 * 
	 * <p>
	 * Each team will have a string in the returned array, and each string contains the race letters of the team members, sorted alphabetically, except the
	 * first letter of the first team will be the race letter of the specified participant.<br>
	 * If there are more than 2 teams (e.g. in case of Free-for-all ({@link Format#FFA})) team races are also sorted alphabetically (but the first team is not
	 * moved).
	 * </p>
	 * 
	 * @param part part whose team to list first and part to list first inside the team
	 * @return the races of teams
	 */
	public String[] getTeamRacess( final Part part ) {
		final String[] teamRacess = new String[ teamsCount ];
		
		// First the participant's team
		final StringBuilder sb = new StringBuilder( 4 );
		for ( final Part p : parts ) {
			if ( p.team == part.team && p != part )
				sb.append( p.race.letter );
		}
		teamRacess[ 0 ] = sb.length() > 0 ? sort( sb ).insert( 0, part.race.letter ).insert( 1, '+' ).toString() : Character.toString( part.race.letter );
		
		
		// And the remaining teams
		sb.setLength( 0 );
		int i = 1;
		int lastTeam = -1;
		for ( final Part p : parts ) {
			if ( p.team == part.team )
				continue; // This team is already handled
				
			if ( lastTeam != p.team ) {
				if ( lastTeam >= 0 )
					teamRacess[ i++ ] = sort( sb ).toString();
				sb.setLength( 0 );
				lastTeam = p.team;
			}
			sb.append( p.race.letter );
		}
		if ( sb.length() > 0 )
			teamRacess[ i ] = sort( sb ).toString();
		
		if ( teamRacess.length > 2 )
			Arrays.sort( teamRacess, 1, teamRacess.length );
		
		return teamRacess;
	}
	
	/**
	 * Returns the leagues of teams.
	 * 
	 * <p>
	 * Each team will have a string in the returned array, and each string contains the league letters of the team members, sorted alphabetically, except the
	 * first letter of the first team will be the league letter of the specified participant.<br>
	 * If there are more than 2 teams (e.g. in case of Free-for-all ({@link Format#FFA})) team leagues are also sorted alphabetically (but the first team is not
	 * moved).
	 * </p>
	 * 
	 * @param part part whose team to list first and part to list first inside the team
	 * @return the leagues of teams
	 */
	public String[] getTeamLeaguess( final Part part ) {
		final String[] teamLeaguess = new String[ teamsCount ];
		
		// First the participant's team
		final StringBuilder sb = new StringBuilder( 4 );
		for ( final Part p : parts ) {
			if ( p.team == part.team && p != part )
				sb.append( p.league == null ? League.UNKNOWN.letter : p.league.letter );
		}
		teamLeaguess[ 0 ] = sb.length() > 0 ? sort( sb ).insert( 0, part.league == null ? League.UNKNOWN.letter : part.league.letter ).insert( 1, '+' )
		        .toString() : Character.toString( part.league == null ? League.UNKNOWN.letter : part.league.letter );
		
		
		// And the remaining teams
		sb.setLength( 0 );
		int i = 1;
		int lastTeam = -1;
		for ( final Part p : parts ) {
			if ( p.team == part.team )
				continue; // This team is already handled
				
			if ( lastTeam != p.team ) {
				if ( lastTeam >= 0 )
					teamLeaguess[ i++ ] = sort( sb ).toString();
				sb.setLength( 0 );
				lastTeam = p.team;
			}
			sb.append( p.league == null ? League.UNKNOWN.letter : p.league.letter );
		}
		if ( sb.length() > 0 )
			teamLeaguess[ i ] = sort( sb ).toString();
		
		if ( teamLeaguess.length > 2 )
			Arrays.sort( teamLeaguess, 1, teamLeaguess.length );
		
		return teamLeaguess;
	}
	
	/**
	 * Sorts the letters of the specified {@link StringBuilder}.
	 * 
	 * <p>
	 * Implementation is only effective if the length of the string builder is very small, this is the case in case of team races and leagues.
	 * </p>
	 * 
	 * @param sb string builder whose content to be sorted
	 * @return the string builder
	 */
	private static StringBuilder sort( final StringBuilder sb ) {
		for ( int i = sb.length() - 1; i > 0; i-- )
			for ( int j = i - 1; j >= 0; j-- )
				if ( sb.charAt( i ) < sb.charAt( j ) ) {
					// Swap
					final char ch = sb.charAt( i );
					sb.setCharAt( i, sb.charAt( j ) );
					sb.setCharAt( j, ch );
				}
		
		return sb;
	}
	
	/**
	 * Implements an order of the game date.
	 */
	@Override
	public int compareTo( final Game g ) {
		return date.compareTo( g.date );
	}
	
}
