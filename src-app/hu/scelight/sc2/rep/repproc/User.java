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
import java.awt.geom.Point2D.Float;

import hu.scelight.sc2.rep.model.details.Details;
import hu.scelight.sc2.rep.model.details.Player;
import hu.scelight.sc2.rep.model.details.Toon;
import hu.scelight.sc2.rep.model.gameevents.camera.CameraUpdateEvent;
import hu.scelight.sc2.rep.model.initdata.lobbystate.PlayerColor;
import hu.scelight.sc2.rep.model.initdata.lobbystate.Slot;
import hu.scelight.sc2.rep.model.initdata.userinitdata.UserInitData;
import hu.scelightapi.sc2.rep.repproc.IUser;

/**
 * User. Grouped and precomputed info about a user.
 * 
 * <p>
 * There is a user to all players and observers, including computers.
 * </p>
 * 
 * @author Andras Belicza
 */
public class User implements IUser {
	
	/** Reference to the replay processor. */
	public final RepProcessor repProc;
							  
	/** Slot index. */
	public final int		  slotIdx;
							  
	/**
	 * Player index in the array returned by {@link Details#getPlayerList()}, in ladder games <code>{@link #playerId} = {@link #playerIdx} + 1</code> but in
	 * custom games it can deviate.
	 */
	public final int		  playerIdx;
							  
	/** Player Id, in ladder games <code>{@link #playerId} = {@link #playerIdx} + 1</code> but in custom games it can deviate. */
	public final int		  playerId;
							  
	/** Slot of the user. */
	public final Slot		  slot;
							  
	/** User init data of the user. */
	public final UserInitData uid;
							  
	/** {@link Player} object from the {@link Details#getPlayerList()}, if any. */
	public final Player		  player;
							  
							  
	// Miscellaneous derived data
	
	/** Player user index after optional reordering (e.g. favored player list) in {@link RepProcessor#playerUsers}. */
	public int				  playerUserIdx;
							  
	/** Name of the user excluding clan tag. */
	public final String		  name;
							  
	/** Full name of the user including clan tag. */
	public final String		  fullName;
							  
	/** User index (index in the {@link RepProcessor#users} array. */
	public int				  userIdx;
							  
							  
	// Derived data from game events
	
	/** First camera update event which has a target point. */
	public CameraUpdateEvent  firstCamUpdateEvent;
							  
	/** Loop at which the user left the game. */
	public int				  leaveLoop	  = -1;
										  
	/** Last loop when the user issued a command game event. */
	public int				  lastCmdLoop = -1;
										  
	/** Number of actions to be included in the APM calculation. */
	public int				  apmActions;
							  
	/**
	 * APM of the user.
	 * <p>
	 * Algorithm: See {@link RepProcessor#calculatePerMinute(long, int)}.<br>
	 * Additionally: time range: initial time is excluded based on setting, and last loop is the loop of the last cmd game event of the user; and actions are
	 * all game events within this time range except camera update game events.
	 * </p>
	 */
	public int				  apm;
							  
	/** Number of actions to be included in the SPM calculation. */
	public int				  spmActions;
							  
	/**
	 * SPM of the user.
	 * <p>
	 * Algorithm: See {@link RepProcessor#calculatePerMinute(double, int)}.<br>
	 * Additionally: time range: initial time is excluded based on setting, and last loop is the loop of the last cmd game event of the user; and actions are
	 * all camera update events which move the screen by a distance larger than 15.
	 * </p>
	 */
	public double			  spm;
	
	/**
	 * MMR of the user.
	 */
	public int mmr;
							  
							  
	// Derived data from tracker events
	
	/**
	 * SQ (Spending Quotient) of the user.
	 * <p>
	 * Algorithm: See {@link RepProcessor#calculateSQImpl(int, int)}.<br>
	 * Additionally: samples are taken up to the loop of the last cmd game event of the user.
	 * </p>
	 */
	public int				  sq;
							  
	/** Direction of the start location of the player from the center of the map in the range of 1..12 as an hour value. */
	public int				  startDirection;
							  
	/** Supply-capped percent (ratio of supply-capped time up until the last cmd game event and the time of the last cmd game event of the user). */
	public double			  supplyCappedPercent;
							  
							  
	// Derived data from map objects
	
	/** Start location on the map, matched by the first camera update event (which has a target point). */
	public Point2D.Float	  startLocation;
							  
	/** Cached player color. */
	private PlayerColor		  playerColor;
							  
							  
	/**
	 * Creates a new {@link User}.
	 * 
	 * @param repProc reference to the replay processor
	 * @param slotIdx slot index
	 * @param playerIdx player index
	 * @param playerId player id
	 * @param slot slot of the user
	 * @param uid user init data of the user
	 * @param player {@link Player} object from the {@link Details}, if any
	 */
	public User( final RepProcessor repProc, final int slotIdx, final int playerIdx, final int playerId, final Slot slot, final UserInitData uid,
	        final Player player ) {
		this.repProc = repProc;
		this.slotIdx = slotIdx;
		this.playerIdx = playerIdx;
		this.playerId = playerId;
		this.slot = slot;
		this.uid = uid;
		this.player = player;
		
		if ( uid != null ) {
			name = uid.name;
			mmr = uid.getMMR();
		}
		else if ( player != null ) {
			String name_ = player.getName();
			// Cut off clan name
			if ( name_ != null && name_.indexOf( ']' ) >= 0 )
				name = name_.substring( name_.indexOf( ']' ) );
			else
				name = name_;
		} else
			name = null;
			
		fullName = uid != null ? uid.fullName : player != null ? player.getName() : null;
	}
	
	@Override
	public Toon getToon() {
		if ( player != null ) {
			return player.toon;
		}
		final String th = slot.getToonHandle();
		if ( th != null && th.length() > 0 ) {
			return new Toon( th ); // In Co-op games toon might be empty string
		}
		return null;
	}
	
	@Override
	public RepProcessor getRepProcessor() {
		return repProc;
	}
	
	@Override
	public int getSlotIdx() {
		return slotIdx;
	}
	
	@Override
	public int getPlayerIdx() {
		return playerIdx;
	}
	
	@Override
	public Slot getSlot() {
		return slot;
	}
	
	@Override
	public UserInitData getUid() {
		return uid;
	}
	
	@Override
	public Player getPlayer() {
		return player;
	}
	
	@Override
	public int getPlayerUserIdx() {
		return playerUserIdx;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public String getFullName() {
		return fullName;
	}
	
	@Override
	public int getUserIdx() {
		return userIdx;
	}
	
	@Override
	public CameraUpdateEvent getFirstCamUpdateEvent() {
		return firstCamUpdateEvent;
	}
	
	@Override
	public int getLeaveLoop() {
		return leaveLoop;
	}
	
	@Override
	public int getLastCmdLoop() {
		return lastCmdLoop;
	}
	
	@Override
	public int getApmActions() {
		return apmActions;
	}
	
	@Override
	public int getApm() {
		return apm;
	}
	
	@Override
	public int getSpmActions() {
		return spmActions;
	}
	
	@Override
	public double getSpm() {
		return spm;
	}
	
	@Override
	public int getSq() {
		return sq;
	}
	
	@Override
	public int getStartDirection() {
		return startDirection;
	}
	
	@Override
	public double getSupplyCappedPercent() {
		return supplyCappedPercent;
	}
	
	@Override
	public Float getStartLocation() {
		return startLocation;
	}
	
	@Override
	public PlayerColor getPlayerColor() {
		if ( playerColor == null ) {
			if ( repProc.isArchon() && slot.getTandemLeaderUserId() != null ) {
				playerColor = repProc.usersByUserId[ slot.getTandemLeaderUserId() ].getPlayerColor();
			} else {
				playerColor = slot.getPlayerColor();
			}
		}
		
		return playerColor;
	}
	
	@Override
	public int getMMR() {
		return mmr;
	}
}
