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

import hu.scelight.sc2.balancedata.BalanceData;
import hu.scelight.sc2.rep.model.attributesevents.AttributesEvents;
import hu.scelight.sc2.rep.model.details.Details;
import hu.scelight.sc2.rep.model.gameevents.GameEvents;
import hu.scelight.sc2.rep.model.initdata.InitData;
import hu.scelight.sc2.rep.model.initdata.lobbystate.Controller;
import hu.scelight.sc2.rep.model.initdata.lobbystate.Slot;
import hu.scelight.sc2.rep.model.messageevents.MessageEvents;
import hu.scelight.sc2.rep.model.trackerevents.TrackerEvents;
import hu.scelight.sc2.rep.s2prot.type.Attribute;
import hu.scelightapi.sc2.rep.model.IReplay;

import java.util.Map;

/**
 * Class representing the data of a StarCraft II replay file.
 * 
 * @author Andras Belicza
 */
public class Replay implements IReplay {
	
	/** Header info of the replay. */
	public Header           header;
	
	/** Details of the replay. */
	public Details          details;
	
	/** Init data of the replay. */
	public InitData         initData;
	
	/** Attributes events of the replay. */
	public AttributesEvents attributesEvents;
	
	/** Message events of the replay. */
	public MessageEvents    messageEvents;
	
	/** Game events of the replay. */
	public GameEvents       gameEvents;
	
	/** Tracker events of the replay. */
	public TrackerEvents    trackerEvents;
	
	
	/**
	 * The <b>playerId &rArr; userId</b> mapping in the form of an array where the index is the player id and the value is the user id.
	 */
	private int[]           playerIdUserIdMap;
	
	/** Lazily initialized balance data. */
	private BalanceData     balanceData;
	
	
	@Override
	public Header getHeader() {
		return header;
	}
	
	@Override
	public Details getDetails() {
		return details;
	}
	
	@Override
	public InitData getInitData() {
		return initData;
	}
	
	@Override
	public AttributesEvents getAttributesEvents() {
		return attributesEvents;
	}
	
	@Override
	public MessageEvents getMessageEvents() {
		return messageEvents;
	}
	
	@Override
	public GameEvents getGameEvents() {
		return gameEvents;
	}
	
	@Override
	public TrackerEvents getTrackerEvents() {
		return trackerEvents;
	}
	
	/**
	 * Returns the <b>playerId &rArr; userId</b> mapping in the form of an array where the index is the player id and the value is the user id.
	 * 
	 * <p>
	 * The player id is the id found in events in "old" replays and in the tracker events in new replays.<br>
	 * This player id is not the player index of the players array found in the Details (there might be discrepancy).
	 * </p>
	 * 
	 * <p>
	 * This method can only be called if details, init data and attributes events have already been parsed!
	 * </p>
	 * 
	 * @return the <b>playerId &rArr; userId</b> mapping
	 */
	public int[] getPlayerIdUserIdMap() {
		if ( playerIdUserIdMap == null ) {
			playerIdUserIdMap = new int[ 17 ]; // Allow +1 because player id is 1-based
			
			final Slot[] slots = initData.getLobbyState().getSlots();
			final Map< Integer, Map< Integer, Attribute > > scopes = attributesEvents.scopes;
			
			for ( int slotIdx = 0, playerId = 0; slotIdx < slots.length; slotIdx++ ) {
				final Slot slot = slots[ slotIdx ];
				
				if ( slot.getController() != Controller.HUMAN && slot.getController() != Controller.COMPUTER ) {
					// Not a human or computer, "seemingly" not a player!
					
					// If a player drops during game start, his/her slot will be empty,
					// he/she will not have a player object in the details,
					// but attributes events will have a record of him/her, this is how we can detect this case.
					// In this case (and only in this case) playerId still have to be incremented!
					
					if ( scopes != null && scopes.containsKey( Integer.valueOf( playerId + 1 ) ) ) {
						// Found a player record in the attributes event => dropped player (during game start)
						// or player from slot have been moved by the game host and is left Open.
						// Increment playerId (but no user): doesn't matter what userId we assign,
						// there will be no event for this "missing" player.
						playerId++;
					}
					continue;
				}
				
				playerIdUserIdMap[ ++playerId ] = slot.userId == null ? -1 : slot.userId;
			}
		}
		
		return playerIdUserIdMap;
	}
	
	@Override
	public BalanceData getBalanceData() {
		if ( balanceData == null )
			balanceData = BalanceData.get( header.getVersionView() );
		
		return balanceData;
	}
	
}
