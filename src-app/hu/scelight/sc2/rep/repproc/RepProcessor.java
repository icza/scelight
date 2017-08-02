/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.sc2.rep.repproc;

import java.awt.geom.Point2D;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import hu.scelight.gui.page.repanalyzer.RepAnalyzerPage;
import hu.scelight.sc2.balancedata.BdUtil;
import hu.scelight.sc2.map.MapParser;
import hu.scelight.sc2.map.model.MapInfo;
import hu.scelight.sc2.map.model.MapObjects;
import hu.scelight.sc2.rep.cache.RepProcCache;
import hu.scelight.sc2.rep.factory.RepContent;
import hu.scelight.sc2.rep.factory.RepParserEngine;
import hu.scelight.sc2.rep.model.Replay;
import hu.scelight.sc2.rep.model.attributesevents.AttributesEvents;
import hu.scelight.sc2.rep.model.details.Player;
import hu.scelight.sc2.rep.model.details.Result;
import hu.scelight.sc2.rep.model.details.Toon;
import hu.scelight.sc2.rep.model.gameevents.camera.CameraUpdateEvent;
import hu.scelight.sc2.rep.model.gameevents.camera.TargetPoint;
import hu.scelight.sc2.rep.model.gameevents.cmd.TagTransformation;
import hu.scelight.sc2.rep.model.initdata.gamedesc.CacheHandle;
import hu.scelight.sc2.rep.model.initdata.gamedesc.ExpansionLevel;
import hu.scelight.sc2.rep.model.initdata.gamedesc.GameSpeed;
import hu.scelight.sc2.rep.model.initdata.lobbystate.Controller;
import hu.scelight.sc2.rep.model.initdata.lobbystate.Role;
import hu.scelight.sc2.rep.model.initdata.lobbystate.Slot;
import hu.scelight.sc2.rep.model.initdata.userinitdata.League;
import hu.scelight.sc2.rep.model.initdata.userinitdata.UserInitData;
import hu.scelight.sc2.rep.model.messageevents.MessageEvents;
import hu.scelight.sc2.rep.model.trackerevents.PlayerStatsEvent;
import hu.scelight.sc2.rep.model.trackerevents.UnitBornEvent;
import hu.scelight.sc2.rep.s2prot.Event;
import hu.scelight.sc2.rep.s2prot.type.Attribute;
import hu.scelight.service.env.Env;
import hu.scelight.service.settings.Settings;
import hu.scelight.util.Utils;
import hu.scelightapi.sc2.rep.model.details.IResult;
import hu.scelightapi.sc2.rep.model.gameevents.IGameEvents;
import hu.scelightapi.sc2.rep.model.trackerevents.ITrackerEvents;
import hu.scelightapi.sc2.rep.repproc.IRepProcessor;
import hu.sllauncher.bean.VersionBean;
import hu.sllauncher.util.DurationFormat;

/**
 * Replay processor. Contains utilities and gives a higher level abstraction than the {@link Replay}.
 * 
 * <p>
 * This class is independent from the GUI. Replay analyzers are visualized on {@link RepAnalyzerPage}s.
 * </p>
 * 
 * @author Andras Belicza
 */
public class RepProcessor implements IRepProcessor {
	
	/** Implementation version bean. */
	public static final VersionBean						VERSION			 = new VersionBean( 1, 4, 1 );
																		 
																		 
	/** Game events included in APM calculation. */
	public static final Set< Integer >					APM_EVENT_ID_SET = Utils.asNewSet( IGameEvents.ID_SELECTION_DELTA, IGameEvents.ID_CMD,
	        IGameEvents.ID_CONTROL_GROUP_UPDATE );
			
			
	/** List of favored player toon list. */
	public static final AtomicReference< List< Toon > >	favoredToonList	 = new AtomicReference< >();
																		 
																		 
	/** Processed replay file. */
	public final Path									file;
														
	/** Parsed replay. */
	public final Replay									replay;
														
														
	// Cached settings. Many things are calculated lazily, but consistency is important.
	
	/** Tells if real-time time measurement is to be used. */
	public final boolean								realTime;
														
	/** Initial time to exclude from per-minute calculations in game-time seconds. */
	public final int									initialPerMinCalcExclTime;
														
	/** Tells if detected format is to be overridden based on matchup. */
	public final boolean								overrideFormatBasedOnMatchup;
														
	/** Tells if largest remaining team is to be declared winner if result info is missing. */
	public final boolean								largestRemainingTeamWins;
														
	/** Unit tag display transformation. */
	public final TagTransformation						tagTransformation;
														
														
	/** Game speed of the replay. */
	public final GameSpeed								gameSpeed;
														
	/** Game speed to be used to convert between game-time and real-time. */
	public final GameSpeed								converterGameSpeed;
														
	/** Users (players, observers etc.). Contains no <code>null</code> values. */
	public final User[]									users;
														
	/** Original users, this array is never reordered. */
	public final User[]									originalUsers;
														
	/** Users indexed by user id, might contain <code>null</code> values. */
	public final User[]									usersByUserId;
														
	/**
	 * Users indexed by player id. First element is <code>null</code> (player id is 1-based). It might also contain other <code>null</code> values (e.g. in
	 * custom games when slots are rearranged).
	 */
	public final User[]									usersByPlayerId;
														
	/** Users that have a player object (participants of the game). Contains no <code>null</code> values. */
	public final User[]									playerUsers;
														
	/** Game format. */
	private Format										format;
														
														
	/** Lazily initialized map info. */
	private MapInfo										mapInfo;
														
	/** Lazily initialized map objects. */
	private MapObjects									mapObjects;
														
														
	/**
	 * Creates a new {@link RepProcessor}.
	 * 
	 * <p>
	 * If the specified replay file cannot be parsed, the <code>replay</code> attribute will be null and the replay processor cannot be used. This always must
	 * be checked before proceeding to use the replay processor.
	 * </p>
	 * 
	 * @param file replay file to process
	 */
	public RepProcessor( final Path file ) {
		this( file, RepParserEngine.FULL_CONTENT_SET );
	}
	
	/**
	 * Creates a new {@link RepProcessor}.
	 * 
	 * <p>
	 * If the specified replay file cannot be parsed, the <code>replay</code> attribute will be <code>null</code> and the replay processor cannot be used. This
	 * always must be checked before proceeding to use the replay processor.
	 * </p>
	 * 
	 * @param file replay file to process
	 * @param contentSet content to be parsed; {@link RepContent#DETAILS}, {@link RepContent#INIT_DATA} and {@link RepContent#ATTRIBUTES_EVENTS} are always
	 *            parsed; extra content is to be listed here
	 */
	public RepProcessor( final Path file, final Set< RepContent > contentSet ) {
		this( file, RepParserEngine.parseReplay( file, contentSet ) );
	}
	
	/**
	 * Creates a new {@link RepProcessor}.
	 * 
	 * <p>
	 * If the specified replay is <code>null</code>, the replay processor cannot be used. This always must be checked before proceeding to use the replay
	 * processor.
	 * </p>
	 * 
	 * @param file replay file to process
	 * @param replay the constructed {@link Replay} object
	 */
	public RepProcessor( final Path file, final Replay replay ) {
		this.file = file;
		
		this.replay = replay;
		
		if ( replay == null ) {
			users = null;
			originalUsers = null;
			realTime = false;
			initialPerMinCalcExclTime = 0;
			usersByPlayerId = null;
			overrideFormatBasedOnMatchup = false;
			largestRemainingTeamWins = false;
			tagTransformation = null;
			gameSpeed = null;
			converterGameSpeed = null;
			usersByUserId = null;
			playerUsers = null;
			return;
		}
		
		realTime = Env.APP_SETTINGS.get( Settings.USE_REAL_TIME );
		initialPerMinCalcExclTime = Env.APP_SETTINGS.get( Settings.INITIAL_PER_MIN_CALC_EXCL_TIME );
		overrideFormatBasedOnMatchup = Env.APP_SETTINGS.get( Settings.OVERRIDE_FORMAT_BASED_ON_MATCHUP );
		largestRemainingTeamWins = Env.APP_SETTINGS.get( Settings.LARGEST_REMAINING_TEAM_WINS );
		tagTransformation = Env.APP_SETTINGS.get( Settings.UNIT_TAG_TRANSFORMATION );
		
		gameSpeed = replay.initData.getGameDescription().getGameSpeed();
		// LotV is played in real time, so for LotV the converter is always the FASTER game speed because that was chosen as real-time in LOTV:
		converterGameSpeed = replay.initData.getGameDescription().getExpansionLevel() == ExpansionLevel.LOTV ? GameSpeed.FASTER : gameSpeed;
		
		// Init users
		final Slot[] slots = replay.initData.getLobbyState().getSlots();
		final UserInitData[] uids = replay.initData.getUserInitDatas();
		final Player[] players = replay.details.getPlayerList();
		
		final List< User > userList = new ArrayList< >();
		final List< User > playerUserList = new ArrayList< >();
		
		// Add a first null element to players, player id is 1-based
		playerUserList.add( null );
		
		// Detect ARCHON mode
		boolean archonMode = false;
		for ( final Slot s : slots ) {
			if ( s.getTandemLeaderUserId() != null ) {
				archonMode = true;
				format = Format.ARCHON;
				break;
			}
		}
		
		// Going by slot index will ensure that users are in team order! (Exception found?)
		final Map< Integer, Map< Integer, Attribute > > scopes = replay.attributesEvents.scopes;
		for ( int slotIdx = 0, playerIdx = -1, playerId = 0; slotIdx < slots.length; slotIdx++ ) {
			final Slot slot = slots[ slotIdx ];
			if ( slot.getController() != Controller.HUMAN && slot.getController() != Controller.COMPUTER ) {
				if ( scopes != null && scopes.containsKey( Integer.valueOf( playerIdx + 1 ) ) ) {
					playerId++;
					playerUserList.add( null );
				}
				continue;
			}
			
			Player player = null;
			
			if ( archonMode ) {
				// ARCHON mode
				// Let's apply the logic that should work in all cases
				// (but in some custom games it sill doesn't work, that's why the other branch is kept, it handles more cases properly)
				playerId = 0;
				for ( playerIdx = players.length - 1; playerIdx >= 0; playerIdx-- ) {
					if ( players[ playerIdx ].workingSetSlotId == slotIdx ) {
						player = players[ playerIdx ];
						playerId = playerIdx + 1;
						break;
					}
				}
			} else {
				playerIdx++;
				player = playerIdx < players.length ? players[ playerIdx ] : null;
				if ( player != null ) {
					playerId++;
					// I've found example where for no visible reason whatsoever player id was incremented by 2 (Left 2 die custom game).
					// So verify player id by checking if there is an associated scope with the player id.
					if ( scopes != null && !scopes.isEmpty() )
						while ( !scopes.containsKey( Integer.valueOf( playerId ) ) ) {
							if ( playerId++ >= 16 )
								break; // To avoid Out of memory error...
							playerUserList.add( null );
						}
				}
			}
			
			final User user = new User( this, slotIdx, playerIdx, playerId, slot, slot.userId == null ? null : uids[ slot.userId ], player );
			userList.add( user );
			if ( player != null ) // In most cases same as: slot.getRole() == Role.PARTICIPANT
				playerUserList.add( user );
		}
		
		users = userList.toArray( new User[ userList.size() ] );
		originalUsers = users.clone();
		usersByPlayerId = playerUserList.toArray( new User[ playerUserList.size() ] );
		
		usersByUserId = new User[ 17 ]; // Allow +1 for 16 => SYSTEM event
		for ( final User u : users )
			if ( u.slot.userId != null )
				usersByUserId[ u.slot.userId ] = u;
				
		// Rearrange the users array based on the favored player list and team order
		applyFavoredToonListAndTeamOrder();
		
		// Fill user indices and playerUsers. playerId is not continuous (e.g. custom games when slots are rearranged).
		{
			int playerCount = 0;
			for ( final User u : usersByPlayerId )
				if ( u != null )
					playerCount++;
			playerUsers = new User[ playerCount ];
		}
		for ( int i = 0, playerUserIdx = 0; i < users.length; i++ ) {
			final User u = users[ i ];
			u.userIdx = i;
			if ( u.player != null ) {
				u.playerUserIdx = playerUserIdx;
				playerUsers[ playerUserIdx++ ] = u;
			}
		}
		
		preprocessGameEvents();
		
		preprocessTrackerEvents();
	}
	
	/**
	 * Rearranges the users array based on the favored player list and team order.
	 */
	private void applyFavoredToonListAndTeamOrder() {
		final List< Integer > teamIdList = new ArrayList< >( 4 );
		
		final List< Toon > favoredToonList = RepProcessor.favoredToonList.get();
		
		if ( !favoredToonList.isEmpty() ) {
			// First determine team order
			for ( final Toon toon : favoredToonList ) {
				for ( final User u : users )
					if ( u.player != null && u.player.getToon().equals( toon ) ) {
						// Player match
						// Note: the same toon might be added multiple times, but since we check if team is already added
						// will filter out this case.
						if ( !teamIdList.contains( u.slot.teamId ) )
							teamIdList.add( u.slot.teamId );
						break;
					}
			}
		}
		
		// Add remaining teams
		for ( final User u : users )
			if ( !teamIdList.contains( u.slot.teamId ) )
				teamIdList.add( u.slot.teamId );
				
		// Now sort users array
		Arrays.sort( users, new Comparator< User >() {
			@Override
			public int compare( final User u1, final User u2 ) {
				final int team1Idx = teamIdList.indexOf( u1.slot.teamId );
				final int team2Idx = teamIdList.indexOf( u2.slot.teamId );
				
				// Move observers to the end!
				final boolean isObs1 = Role.PARTICIPANT != u1.slot.getRole();
				final boolean isObs2 = Role.PARTICIPANT != u2.slot.getRole();
				if ( isObs1 || isObs2 ) { // At least one of them is obs
					if ( isObs1 && isObs2 )
						return team1Idx - team2Idx; // Both obs, use team order
					if ( isObs2 )
						return -1; // Only User 1 is participant
					return 1; // Only User 2 is participant
				}
				
				if ( team1Idx != team2Idx )
					return team1Idx - team2Idx; // Different team, use team order
					
				// Same team, check position in the favored toon list
				final int favored1Idx = u1.player == null ? -1 : favoredToonList.indexOf( u1.player.toon );
				final int favored2Idx = u2.player == null ? -1 : favoredToonList.indexOf( u2.player.toon );
				
				if ( favored1Idx < 0 && favored2Idx < 0 )
					return 0; // Neither is favored, use original order
					
				if ( favored1Idx >= 0 && favored2Idx >= 0 )
					return favored1Idx - favored2Idx; // Both favored
					
				if ( favored1Idx >= 0 )
					return -1; // Only u1 is favored
				if ( favored2Idx >= 0 )
					return 1; // Only u2 is favored
					
				return 0; // Covered all cases, this is just a semantical requirement
			}
		} );
	}
	
	/**
	 * Calculates Game events ({@link Replay#gameEvents}) related derived user data.
	 * 
	 * <p>
	 * <b>Calculates the following:</b>
	 * </p>
	 * <ul>
	 * <li>First camera update event (which has a target point) ({@link User#startLocation})
	 * <li>User leave loops ({@link User#leaveLoop})
	 * <li>Last command loops ({@link User#lastCmdLoop})
	 * <li>Player APMs ({@link User#apm})
	 * <li>Player SPMs ({@link User#spm})
	 * <li>Missing match result deduction if result info is missing (also see {@link Settings#LARGEST_REMAINING_TEAM_WINS})
	 * </ul>
	 */
	private void preprocessGameEvents() {
		if ( replay.gameEvents == null )
			return;
			
		// Calculate APMs and SPMs
		
		final int initialPerMinCalcExclLoops = initialPerMinCalcExclTime * 16;
		
		// For tracking teams (for match result deduction).
		// Maps from teams to team members (player ids).
		final Map< Integer, List< Integer > > teamMemberListMap = new HashMap< >();
		for ( final User u : playerUsers ) {
			final Integer team = u.slot.teamId;
			List< Integer > memberList = teamMemberListMap.get( team );
			if ( memberList == null )
				teamMemberListMap.put( team, memberList = new ArrayList< >( 4 ) );
			memberList.add( u.playerId );
		}
		
		// We count actions for APM calculations in this
		final int[] apmActionsById = new int[ usersByUserId.length ];
		// We count actions for SPM calculations in this
		final int[] spmActionsById = new int[ usersByUserId.length ];
		// Previous camera update events which have a target point, indexed by user id.
		final CameraUpdateEvent[] prevCamEvents = new CameraUpdateEvent[ usersByUserId.length ];
		
		for ( final Event event : replay.gameEvents.events ) {
			// Count actions for APM calculation (exclude userId=16 system events!)
			if ( APM_EVENT_ID_SET.contains( event.id ) && event.userId < 16 && event.loop >= initialPerMinCalcExclLoops ) {
				apmActionsById[ event.userId ]++;
			}
			
			switch ( event.id ) {
				case IGameEvents.ID_CAMERA_UPDATE : {
					final CameraUpdateEvent cue = (CameraUpdateEvent) event;
					final TargetPoint point = cue.getTargetPoint();
					if ( usersByUserId[ event.userId ].firstCamUpdateEvent == null && point != null )
						usersByUserId[ event.userId ].firstCamUpdateEvent = cue;
					if ( event.loop >= initialPerMinCalcExclLoops ) {
						// Count actions for SPM calculation
						final TargetPoint prevPoint = prevCamEvents[ event.userId ] == null ? null : prevCamEvents[ event.userId ].getTargetPoint();
						if ( point != null && prevPoint != null ) {
							// Check if distance delta is greater than 15.
							// Coordinates are map coordinates multiplied by 256; in the range of 0..256; max delta is 256.
							// Max distance square: ffff * ffff + ffff * ffff = 2*MAX_INT => would require LONG;
							// Instead I omit the fraction, gives little error (unsignificant)
							final int dx = ( point.getX() >> 8 ) - ( prevPoint.getX() >> 8 );
							final int dy = ( point.getY() >> 8 ) - ( prevPoint.getY() >> 8 );
							if ( dx * dx + dy * dy > 225 )
								spmActionsById[ event.userId ]++;
						}
					}
					if ( point != null )
						prevCamEvents[ event.userId ] = cue;
					break;
				}
				case IGameEvents.ID_CMD :
					usersByUserId[ event.userId ].lastCmdLoop = event.loop;
					// Actions counted up to this point are now to be included in the APM calculation
					usersByUserId[ event.userId ].apmActions += apmActionsById[ event.userId ];
					apmActionsById[ event.userId ] = 0; // Reset counter
					// Actions counted up to this point are now to be included in the SPM calculation
					usersByUserId[ event.userId ].spmActions += spmActionsById[ event.userId ];
					spmActionsById[ event.userId ] = 0; // Reset counter
					break;
				case IGameEvents.ID_PLAYER_LEAVE : {
					// Observers are also recorded leaving, with higher player ids than the "real" players
					if ( event.getPlayerId() == null )
						break; // This might happen if game was saved and resumed, see issue #10.
					if ( event.getPlayerId() >= usersByPlayerId.length )
						break; // Do not track (we could find out the user from Replay.getPlayerIdUserIdMap() but no need)
					final User u = usersByPlayerId[ event.getPlayerId() ];
					if ( u != null ) {
						u.leaveLoop = event.loop;
						trackPlayerLeave( teamMemberListMap, event.getPlayerId() );
					}
					break;
				}
				case IGameEvents.ID_GAME_USER_LEAVE :
					usersByUserId[ event.userId ].leaveLoop = event.loop;
					if ( usersByUserId[ event.userId ].player != null ) // Is it a player leaving? (else obs)
						trackPlayerLeave( teamMemberListMap, usersByUserId[ event.userId ].playerId );
					break;
			}
		}
		
		completeWinnerDetection( teamMemberListMap );
		
		completePerMinCalculations();
	}
	
	/**
	 * Completes the per-minute calculations (APM, SPM) of players.
	 */
	private void completePerMinCalculations() {
		final int initialPerMinCalcExclLoops = initialPerMinCalcExclTime * 16;
		
		for ( final User u : users ) {
			u.apm = calculatePerMinute( u.apmActions, u.lastCmdLoop - initialPerMinCalcExclLoops );
			u.spm = calculatePerMinute( (double) u.spmActions, u.lastCmdLoop - initialPerMinCalcExclLoops );
		}
	}
	
	/**
	 * Tracks a player leave for the winner determination.
	 * 
	 * @param teamMemberListMap map of the teams and their members still in the game
	 * @param playerId id of player who just left
	 */
	private void trackPlayerLeave( final Map< Integer, List< Integer > > teamMemberListMap, final int playerId ) {
		if ( teamMemberListMap.size() <= 1 )
			return; // Members ("leavers") of the last team are not losers
			
		final int team = usersByPlayerId[ playerId ].slot.teamId;
		final List< Integer > memberList = teamMemberListMap.get( team );
		memberList.remove( new Integer( playerId ) );
		
		if ( memberList.isEmpty() ) { // Team "vanished" => Loser team
			teamMemberListMap.remove( team );
			setTeamDeducedResult( team, Result.DEFEAT );
			
			if ( teamMemberListMap.size() == 1 ) { // One team remained: it is the winner team
				final int winnerTeam = teamMemberListMap.keySet().iterator().next();
				teamMemberListMap.remove( winnerTeam );
				setTeamDeducedResult( winnerTeam, Result.VICTORY );
			}
		}
	}
	
	/**
	 * Completes the winner detection based on the teams and members still in the game.
	 * 
	 * @param teamMemberListMap map of the teams and their members still in the game
	 */
	private void completeWinnerDetection( final Map< Integer, List< Integer > > teamMemberListMap ) {
		// If there is only one team left with human(s) in it and the rest are computer teams,
		// then the human team wins (computers don't leave)
		
		final List< Integer > humanTeamList = new ArrayList< >( teamMemberListMap.size() );
		final List< Integer > compTeamList = new ArrayList< >( teamMemberListMap.size() );
		int unknownTeamCounter = 0;
		for ( final Entry< Integer, List< Integer > > entry : teamMemberListMap.entrySet() ) {
			final int team = entry.getKey();
			
			// Since controller can be other than just HUMAN and COMPUTER,
			// we have to store the only-computer and found-human test results too
			boolean computerOnly = true;
			boolean foundHuman = false;
			for ( final Integer playerId : entry.getValue() )
				if ( usersByPlayerId[ playerId ].slot.getController() == Controller.HUMAN ) {
					foundHuman = true;
					break;
				} else if ( usersByPlayerId[ playerId ].slot.getController() != Controller.COMPUTER ) {
					computerOnly = false;
					if ( foundHuman )
						break; // Found human earlier, no need to check further players
					// We don't "break" here, because if we find a human, the team will "qualify" as human team
				}
				
			if ( foundHuman )
				humanTeamList.add( team );
			else if ( computerOnly )
				compTeamList.add( team );
			else
				unknownTeamCounter++;
		}
		
		if ( unknownTeamCounter == 0 && humanTeamList.size() == 1 ) {
			setTeamDeducedResult( humanTeamList.get( 0 ), Result.VICTORY );
			for ( final int compTeam : compTeamList )
				setTeamDeducedResult( compTeam, Result.DEFEAT );
		} else if ( largestRemainingTeamWins && teamMemberListMap.size() > 1 ) {
			// The largest remaining team wins.
			// If there are multiple remaining teams with the same highest players count, it still remains unknown.
			// If only 1 team would've remained, it already would've been declared winner and have been removed.
			int maxTeamSize = 0;
			for ( final Entry< Integer, List< Integer > > entry : teamMemberListMap.entrySet() )
				if ( maxTeamSize < entry.getValue().size() )
					maxTeamSize = entry.getValue().size();
					
			// First handle losers
			Integer loserTeam;
			do {
				loserTeam = null;
				for ( final Entry< Integer, List< Integer > > entry : teamMemberListMap.entrySet() )
					if ( entry.getValue().size() < maxTeamSize ) {
						loserTeam = entry.getKey();
						teamMemberListMap.remove( loserTeam );
						setTeamDeducedResult( loserTeam, Result.DEFEAT );
						break; // We have to break to avoid ConcurrentModificationException
					}
			} while ( loserTeam != null );
			
			// Now handle the optional 1 winner team
			if ( teamMemberListMap.size() == 1 ) { // One team remained: it is the winner team
				final int winnerTeam = teamMemberListMap.keySet().iterator().next();
				teamMemberListMap.remove( winnerTeam );
				setTeamDeducedResult( winnerTeam, Result.VICTORY );
			}
		}
	}
	
	/**
	 * Sets the deduced match result property for all players of a team.
	 * 
	 * @param teamId team id whose deduced result to be set
	 * @param result deduced result to be set
	 */
	private void setTeamDeducedResult( final Integer teamId, final Result result ) {
		for ( final User u : playerUsers ) {
			// Only override if recorded result is UNKNOWN
			if ( teamId.equals( u.slot.teamId ) && u.player.getRecordedResult() == Result.UNKNOWN )
				u.player.setDeducedResult( result );
		}
	}
	
	/**
	 * Calculates Tracker events ({@link Replay#trackerEvents}) related derived user data.
	 * 
	 * <p>
	 * <b>Calculates the following:</b>
	 * </p>
	 * <ul>
	 * <li>Player SQs ({@link User#sq})
	 * <li>Player start locations ({@link User#startDirection})
	 * <li>Supply-capped percent
	 * </ul>
	 */
	private void preprocessTrackerEvents() {
		if ( replay.trackerEvents == null )
			return;
			
		// Calculate SQs (Spending Quotient) calculation
		
		// SQ Data samples count
		final int[] samplesById = new int[ usersByPlayerId.length ];
		// Unspent resources (Resources Current)
		final long[] unspentsById = new long[ usersByPlayerId.length ];
		// Income (Resource collection rate)
		final long[] incomesById = new long[ usersByPlayerId.length ];
		// Supply-capped count
		final int[] supplyCappedById = new int[ usersByPlayerId.length ];
		
		
		final int mapCX = replay.initData.getGameDescription().getMapSizeX() / 2;
		final int mapCY = replay.initData.getGameDescription().getMapSizeX() / 2;
		
		int playerIdx;
		for ( final Event event : replay.trackerEvents.events ) {
			if ( event.loop == 0 && event.id == ITrackerEvents.ID_UNIT_BORN ) {
				final UnitBornEvent ube = (UnitBornEvent) event;
				final String unitType = ube.getUnitTypeName().toString();
				if ( BdUtil.isMainBuildingImpl( unitType ) ) {
					// It's the main start building of the player => calculate start location
					final int dx = ube.getXCoord() - mapCX;
					final int dy = ube.getYCoord() - mapCY;
					// In custom games user might still be 0, example:
					// Custom games specifies units for both player 1 and player 2 in cooperation mode, but game was started with 1 player only.
					final User u = usersByPlayerId[ ube.getControlPlayerId() ];
					if ( u != null )
						u.startDirection = angleToHour( Math.atan2( dy, dx ) );
				}
			}
			
			if ( event.id == ITrackerEvents.ID_PLAYER_STATS && event.loop < usersByPlayerId[ playerIdx = event.getPlayerId() ].lastCmdLoop ) {
				final PlayerStatsEvent pse = (PlayerStatsEvent) event;
				
				unspentsById[ playerIdx ] += pse.getMineralsCurrent() + pse.getGasCurrent();
				incomesById[ playerIdx ] += pse.getMinsCollRate() + pse.getGasCollRate();
				samplesById[ playerIdx ]++;
				if ( pse.getFoodUsed() >= pse.getFoodMade() )
					supplyCappedById[ playerIdx ]++;
			}
		}
		
		// Finish SQ and supply-capped calculations
		for ( int i = 0; i < usersByPlayerId.length; i++ ) {
			if ( samplesById[ i ] > 0 ) {
				final User u = usersByPlayerId[ i ];
				final long count = samplesById[ i ];
				u.sq = calculateSQImpl( (int) ( unspentsById[ i ] / count ), (int) ( incomesById[ i ] / count ) );
				u.supplyCappedPercent = supplyCappedById[ i ] * 100.0 / count;
			}
		}
		
		// Archon mode
		if ( format == Format.ARCHON ) { // Intentionally comparing to attribute (and not using isArchon() or getFormat())!
			for ( final User u : usersByPlayerId ) {
				final Integer tluid = u == null ? null : u.slot.getTandemLeaderUserId();
				if ( tluid != null ) {
					final User u2 = usersByUserId[ tluid ];
					u.sq = u2.sq;
					u.supplyCappedPercent = u2.supplyCappedPercent;
					u.startDirection = u2.startDirection;
				}
			}
		}
	}
	
	/**
	 * Converts the specified angle (in radian) to a time value (hour) in the range of 1..12.
	 * 
	 * @param angle angle in radian to convert to time
	 * @return the specified angle converted to a time value in the range of 1..12
	 */
	private static int angleToHour( double angle ) {
		// I calculate in radian, degree precision is not enough!
		
		// Shift by 90 Degrees (12 o'clock) and invert direction (clockwise)
		angle = Math.PI / 2 - angle;
		// Put in range of 0..360
		while ( angle < 0 )
			angle += Math.PI * 2;
		while ( angle >= Math.PI * 2 )
			angle -= Math.PI * 2;
		// And convert to a clock value: 12 o'clock => 0; 1 o'clock => 30; 2 o 'clock => 60 ...
		// Rounding / boundaries: in the range of -15..+15 (e.g. 15..45 degrees => 1 o'clock)
		angle = ( angle + Math.PI / 12 ) / ( Math.PI / 6 ); // Note the +15 instead of -15: direction has been inverted
		
		final int hour = (int) Math.round( angle ); // Round, not just cast! (casting drops away the fraction part...)
		return hour == 0 ? 12 : hour;
	}
	
	/**
	 * Initializes the replay processor from the data loaded from the {@link RepProcCache}.
	 * 
	 * <p>
	 * <b>Calculates the following:</b>
	 * </p>
	 * <ul>
	 * <li>Completes the per-minute calculations (e.g. APM, SPM) from the loaded actions counts.
	 * <li>Missing match result deduction if result info is missing (also see {@link Settings#LARGEST_REMAINING_TEAM_WINS})
	 * </ul>
	 */
	public void initFromCacheData() {
		// Since largestRemainingTeamWins property is not part of the rep proc cache config (and I don't want it to be),
		// and since winner detection algorithm depends on it (deduced result might differ based on this property),
		// the winner detection algorithm has to be run to get the same missing deduced results.
		
		// For tracking teams (for match result deduction).
		// Maps from teams to team members (player ids).
		final Map< Integer, List< Integer > > teamMemberListMap = new HashMap< >();
		for ( final User u : playerUsers ) {
			final Integer team = u.slot.teamId;
			List< Integer > memberList = teamMemberListMap.get( team );
			if ( memberList == null )
				teamMemberListMap.put( team, memberList = new ArrayList< >( 4 ) );
			memberList.add( u.playerId );
		}
		
		// Sort users by their leave loop:
		final User[] loUsers = users.clone();
		Arrays.sort( loUsers, new Comparator< User >() {
			@Override
			public int compare( final User u1, final User u2 ) {
				return u1.leaveLoop - u2.leaveLoop;
			}
		} );
		
		// And "simulate" the leave actions
		for ( final User u : loUsers ) {
			if ( u.leaveLoop < 0 )
				continue;
				
			if ( u.player != null ) // Is it a player leaving? (else obs)
				trackPlayerLeave( teamMemberListMap, u.playerId );
		}
		
		completeWinnerDetection( teamMemberListMap );
		
		completePerMinCalculations();
	}
	
	@Override
	public Path getFile() {
		return file;
	}
	
	@Override
	public Replay getReplay() {
		return replay;
	}
	
	@Override
	public boolean isRealTime() {
		return realTime;
	}
	
	@Override
	public int getInitialPerMinCalcExclTime() {
		return initialPerMinCalcExclTime;
	}
	
	@Override
	public boolean isOverrideFormatBasedOnMatchup() {
		return overrideFormatBasedOnMatchup;
	}
	
	@Override
	public boolean isLargestRemainingTeamWins() {
		return largestRemainingTeamWins;
	}
	
	@Override
	public TagTransformation getTagTransformation() {
		return tagTransformation;
	}
	
	@Override
	public GameSpeed getGameSpeed() {
		return gameSpeed;
	}
	
	@Override
	public GameSpeed getConverterGameSpeed() {
		return converterGameSpeed;
	}
	
	@Override
	public User[] getUsers() {
		return users;
	}
	
	@Override
	public User[] getOriginalUsers() {
		return originalUsers;
	}
	
	@Override
	public User[] getUsersByUserId() {
		return usersByUserId;
	}
	
	@Override
	public User[] getUsersByPlayerId() {
		return usersByPlayerId;
	}
	
	@Override
	public User[] getPlayerUsers() {
		return playerUsers;
	}
	
	@Override
	public int calculateSQ( final int unspentResources, final int income ) {
		return calculateSQImpl( unspentResources, income );
	}
	
	/**
	 * Calculates the SQ (Spending Quotient).
	 * <p>
	 * Algorithm: <br>
	 * <blockquote><code>SQ = 35 * ( 0.00137 * I - ln( U ) ) + 240</code></blockquote>
	 * </p>
	 * <p>
	 * Where <code>U</code> is the average unspent resources and <code>I</code> is the average income; and and samples are taken up to the loop of the last cmd
	 * game event of the user.
	 * </p>
	 * <p>
	 * Source: <a href="http://www.teamliquid.net/forum/viewmessage.php?topic_id=266019">Do you macro like a pro?</a>
	 * </p>
	 * 
	 * @param unspentResources unspent resources (Resources Current; including minerals and vespene)
	 * @param income income (Resource Collection Rate; including minerals and vespene)
	 * @return the calculated SQ (Spending Quotient)
	 */
	public static int calculateSQImpl( final int unspentResources, final int income ) {
		return (int) Math.round( 35 * ( 0.00137 * income - Math.log( unspentResources ) ) + 240 );
	}
	
	@Override
	public int calculatePerMinute( final long count, final int loops ) {
		// APM = actions / time_minutes = actions / (time_seconds/60) = actions * 60 / time_seconds = actions * 60_000 / time_ms
		
		return loops <= 0 ? 0 : (int) Math.round( count * 60_000.0 / loopToTime( loops ) );
	}
	
	@Override
	public double calculatePerMinute( final double count, final int loops ) {
		// APM = actions / time_minutes = actions / (time_seconds/60) = actions * 60 / time_seconds = actions * 60_000 / time_ms
		
		return loops <= 0 ? 0 : count * 60_000.0 / loopToTime( loops );
	}
	
	@Override
	public User getUser( final int userId ) {
		return usersByUserId[ userId ];
	}
	
	@Override
	public String getRaceMatchup() {
		final StringBuilder playerRacesBuilder = new StringBuilder();
		
		int lastTeam = -1;
		for ( final User u : playerUsers ) {
			if ( lastTeam < 0 )
				lastTeam = u.slot.teamId;
				
			final int team = u.slot.teamId;
			if ( team != lastTeam ) {
				playerRacesBuilder.append( 'v' );
				lastTeam = team;
			}
			playerRacesBuilder.append( u.player == null ? '-' : u.player.race.letter );
		}
		
		return playerRacesBuilder.toString();
	}
	
	@Override
	public String getLeagueMatchup() {
		final StringBuilder playerRacesBuilder = new StringBuilder();
		
		int lastTeam = -1;
		for ( final User u : playerUsers ) {
			if ( lastTeam < 0 )
				lastTeam = u.slot.teamId;
				
			final int team = u.slot.teamId;
			if ( team != lastTeam ) {
				playerRacesBuilder.append( 'v' );
				lastTeam = team;
			}
			playerRacesBuilder.append( u.uid == null ? League.UNKNOWN.letter : u.uid.getHighestLeague().letter );
		}
		
		return playerRacesBuilder.toString();
	}
	
	@Override
	public Format getFormat() {
		if ( format == null ) {
			Format f = Format.CUSTOM;
			final Map< Integer, Attribute > gs = replay.attributesEvents.scopes == null ? null
			        : replay.attributesEvents.scopes.get( AttributesEvents.S_GLOBAL );
			if ( gs != null ) {
				final Attribute isPremade = gs.get( AttributesEvents.A_IS_PREMADE_GAME );
				if ( isPremade != null && "yes".equals( isPremade.value ) ) {
					final Attribute parties = gs.get( AttributesEvents.A_PARTIES_PREMADE );
					if ( parties != null )
						f = Format.fromAttrValue( parties.value );
				}
			}
			
			if ( overrideFormatBasedOnMatchup ) {
				final Format guessedFormat = guessFormat();
				// if ( guessedFormat != Format.CUSTOM )
				f = guessedFormat;
			}
			
			format = f;
		}
		
		return format;
	}
	
	@Override
	public Format guessFormat() {
		final String raceMatchup = getRaceMatchup();
		if ( raceMatchup.isEmpty() )
			return Format.CUSTOM;
			
		int teamsCount = 1;
		for ( int i = raceMatchup.length() - 2; i > 0; i-- )
			if ( raceMatchup.charAt( i ) == 'v' )
				teamsCount++;
				
		if ( teamsCount < 2 )
			return Format.CUSTOM;
			
		Format format = Format.CUSTOM; // PPvZZZ
		if ( teamsCount == 2 )
			switch ( raceMatchup.length() ) {
				case 3 :
					if ( raceMatchup.charAt( 1 ) == 'v' )
						format = Format.ONE_VS_ONE;
					break; // PvZ
				case 5 :
					if ( raceMatchup.charAt( 2 ) == 'v' )
						format = Format.TWO_VS_TWO;
					break; // PPvZZ
				case 7 :
					if ( raceMatchup.charAt( 3 ) == 'v' )
						format = Format.THREE_VS_THREE;
					break; // PPPvZZZ
				case 9 :
					if ( raceMatchup.charAt( 4 ) == 'v' )
						format = Format.FOUR_VS_FOUR;
					break; // PPPPvZZZZ
				case 11 :
					if ( raceMatchup.charAt( 5 ) == 'v' )
						format = Format.FOUR_VS_FOUR;
					break; // PPPPPvZZZZZ
				case 13 :
					if ( raceMatchup.charAt( 6 ) == 'v' )
						format = Format.FOUR_VS_FOUR;
					break; // PPPPPPvZZZZZZ
			}
		else {
			// Check if FFA (examples: ZvZvP, PvPvPvP, PvPvPvPvPvPvPvP)
			boolean ffa = true;
			for ( int i = 1; i < raceMatchup.length(); i += 2 )
				if ( raceMatchup.charAt( i ) != 'v' ) {
					ffa = false;
					break;
				}
			if ( ffa )
				format = Format.FFA;
		}
		
		return format;
	}
	
	@Override
	public boolean isArchon() {
		return getFormat() == Format.ARCHON;
	}
	
	/**
	 * User preference is stored in {@link Settings#USE_REAL_TIME}.
	 */
	@Override
	public long convertToRealTime( final long gameMs ) {
		return realTime ? converterGameSpeed.convertToRealTime( gameMs ) : gameMs;
	}
	
	/**
	 * User preference is stored in {@link Settings#USE_REAL_TIME}.
	 */
	@Override
	public long convertToGameTime( final long realMs ) {
		return realTime ? converterGameSpeed.convertToGameTime( realMs ) : realMs;
	}
	
	/**
	 * User preference is stored in {@link Settings#USE_REAL_TIME}.
	 */
	@Override
	public long loopToTime( final int gameloop ) {
		// 1 second = 16 loops
		// timeMs = loop * 1000 / 16 = loop * 125 / 2
		
		// Using long - else calculation would overflow:
		// Integer.MAX_VALUE = gameloop / 125 => 2 147 483 647 / 125 = 17 179 869 loops (max allowed loop ~298 hours game time)
		
		// In next step game speed converts by multiplying argument by GameSpeed.relativeSpeed (it's max 60 on SLOWER):
		// Integer.MAX_VALUE = ms * 60 => 2 147 483 647 / 60 = 35 791 394 ms (max allowed ms ~10 hours game time!!)
		return convertToRealTime( ( gameloop * 125L ) / 2 );
	}
	
	@Override
	public long getLengthMs() {
		return loopToTime( replay.header.getElapsedGameLoops() );
	}
	
	@Override
	public String formatLoopTime( final int gameloop ) {
		return DurationFormat.AUTO.formatDuration( loopToTime( gameloop ) );
	}
	
	@Override
	public String getPlayersGrouped() {
		final StringBuilder sb = new StringBuilder();
		
		int lastTeam = -1;
		int playerInTeam = 0;
		
		for ( final User u : playerUsers ) {
			if ( lastTeam < 0 )
				lastTeam = u.slot.teamId;
				
			final int team = u.slot.teamId;
			if ( team != lastTeam ) {
				sb.append( " vs " );
				lastTeam = team;
				playerInTeam = 1;
			} else if ( playerInTeam++ > 0 )
				sb.append( ", " );
				
			sb.append( u.fullName );
		}
		
		return sb.toString();
	}
	
	@Override
	public String getPlayersStringOfResult( final IResult result ) {
		final StringBuilder sb = new StringBuilder();
		
		for ( final User u : playerUsers )
			if ( result == null || u.player.getResult() == result ) {
				if ( sb.length() > 0 )
					sb.append( ", " );
				sb.append( u.player.name );
			}
			
		return sb.toString();
	}
	
	@Override
	public String getPlayersString() {
		return getPlayersStringOfResult( null );
	}
	
	@Override
	public String getWinnersString() {
		return getPlayersStringOfResult( Result.VICTORY );
	}
	
	@Override
	public String getLosersString() {
		return getPlayersStringOfResult( Result.DEFEAT );
	}
	
	@Override
	public int getAvgAPM() {
		if ( playerUsers.length == 0 )
			return 0;
			
		int actions = 0;
		int loops = 0;
		
		final int initialPerMinCalcExclLoops = initialPerMinCalcExclTime * 16;
		
		for ( final User u : playerUsers ) {
			if ( u.slot.getController() != Controller.HUMAN )
				continue;
				
			actions += u.apmActions;
			loops += u.lastCmdLoop - initialPerMinCalcExclLoops;
		}
		
		return calculatePerMinute( actions, loops );
	}
	
	@Override
	public double getAvgSPM() {
		if ( playerUsers.length == 0 )
			return 0;
			
		int actions = 0;
		int loops = 0;
		
		final int initialPerMinCalcExclLoops = initialPerMinCalcExclTime * 16;
		
		for ( final User u : playerUsers ) {
			if ( u.slot.getController() != Controller.HUMAN )
				continue;
				
			actions += u.spmActions;
			loops += u.lastCmdLoop - initialPerMinCalcExclLoops;
		}
		
		return calculatePerMinute( (double) actions, loops );
	}
	
	@Override
	public int getAvgSQ() {
		if ( playerUsers.length == 0 )
			return 0;
			
		long sq = 0;
		int weights = 0;
		
		for ( final User u : playerUsers )
			if ( u.lastCmdLoop >= 0 ) {
				sq += u.sq * u.lastCmdLoop;
				weights += u.lastCmdLoop;
			}
			
		return weights == 0 ? 0 : (int) Math.round( (double) sq / weights );
	}
	
	@Override
	public int getAvgLevels() {
		if ( playerUsers.length == 0 )
			return 0;
			
		int levels = 0;
		int count = 0;
		
		for ( final User u : playerUsers )
			if ( u.uid != null ) {
				levels += u.uid.getCombinedRaceLevels();
				count++;
			}
			
		return count == 0 ? 0 : (int) Math.round( (double) levels / count );
	}
	
	@Override
	public double getAvgSupplyCappedPercent() {
		if ( playerUsers.length == 0 )
			return 0;
			
		double sum = 0;
		int count = 0;
		
		for ( final User u : playerUsers )
			if ( u.lastCmdLoop >= 0 ) {
				sum += u.supplyCappedPercent;
				count++;
			}
			
		return count == 0 ? 0 : sum / count;
	}
	
	@Override
	public League getAvgLeague() {
		if ( playerUsers.length == 0 )
			return null;
			
		int count = 0;
		int leagueOrd = 0;
		boolean foundUnranked = false;
		
		for ( final User u : playerUsers ) {
			if ( u.uid == null )
				continue;
			final League league = u.uid.getHighestLeague();
			if ( league == null || league == League.UNKNOWN )
				continue;
			if ( league == League.UNRANKED ) {
				foundUnranked = true;
				continue;
			}
			
			count++;
			leagueOrd += league.ordinal();
		}
		
		return count > 0 ? League.VALUES[ Math.round( (float) leagueOrd / count ) ] : foundUnranked ? League.UNRANKED : League.UNKNOWN;
	}
	
	@Override
	public CacheHandle getMapCacheHandle() {
		final CacheHandle[] cacheHandles = replay.initData.getGameDescription().cacheHandles;
		return cacheHandles.length == 0 ? null : cacheHandles[ cacheHandles.length - 1 ];
	}
	
	@Override
	public int getChatMessagesCount() {
		if ( replay.messageEvents == null )
			return -1;
			
		int count = 0;
		
		for ( final Event e : replay.messageEvents.events )
			if ( e.id == MessageEvents.ID_CHAT )
				count++;
				
		return count;
	}
	
	@Override
	public MapInfo getMapInfo() {
		if ( mapInfo == null )
			mapInfo = MapParser.getMapInfo( this );
			
		return mapInfo;
	}
	
	@Override
	public MapObjects getMapObjects() {
		if ( mapObjects == null ) {
			mapObjects = MapParser.getMapObjects( this );
			
			if ( mapObjects == null )
				return mapObjects;
				
			// Determine start locations of players based on their first camera update actions
			// (which has a target point because I saw examples when the first camera update did not have a target)
			for ( final User u : playerUsers ) {
				if ( u.firstCamUpdateEvent == null )
					continue;
					
				final float x = u.firstCamUpdateEvent.getTargetPoint().getXFloat();
				final float y = u.firstCamUpdateEvent.getTargetPoint().getYFloat();
				
				// Find closest start location
				Point2D.Float closestStartLoc = null;
				// Sometimes camera is off when game starts (usually to a corner or edge of the map).
				// So only accept this location if distance is within a reasonable value.
				// I implement this by setting an initial closestDist
				float closestDist = 200;
				for ( final Point2D.Float startLoc : mapObjects.startLocationList ) {
					final float distance = (float) startLoc.distanceSq( x, y );
					if ( distance < closestDist ) {
						closestDist = distance;
						closestStartLoc = startLoc;
					}
				}
				
				u.startLocation = closestStartLoc;
			}
		}
		
		return mapObjects;
	}
	
}
