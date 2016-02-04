/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapi.sc2.rep.model.gameevents;

import hu.scelightapi.sc2.rep.model.IEvent;

/**
 * StarCraft II Replay game events.
 * 
 * @author Andras Belicza
 */
public interface IGameEvents {
	
	/** Id of the CameraUpdate game event. */
	int ID_CAMERA_UPDATE            = 49;
	
	/** Id of the SelectionDelta game event. */
	int ID_SELECTION_DELTA          = 28;
	
	/** Id of the Cmd game event. */
	int ID_CMD                      = 27;
	
	/** Id of the ControlGroupUpdate game event. */
	int ID_CONTROL_GROUP_UPDATE     = 29;
	
	
	
	/** Id of the CameraSave game event. */
	int ID_CAMERA_SAVE              = 14;
	
	/** Id of the SaveGame game event. */
	int ID_SAVE_GAME                = 21;
	
	/** Id of the SaveGameDone game event. */
	int ID_SAVE_GAME_DONE           = 22;
	
	/** Id of the PlayerLeave game event. */
	int ID_PLAYER_LEAVE             = 25;
	
	/** Id of the GameCheat game event. */
	int ID_GAME_CHEAT               = 26;
	
	/** Id of the ResourceTrade game event. */
	int ID_RESOURCE_TRADE           = 31;
	
	/** Id of the SetAbsoluteGameSpeed game event. */
	int ID_SET_ABSOLUTE_GAME_SPEED  = 34;
	
	/** Id of the AddAbsoluteGameSpeed game event. */
	int ID_ADD_ABSOLUTE_GAME_SPEED  = 35;
	
	/** Id of the Alliance game event. */
	int ID_ALLIANCE                 = 38;
	
	/** Id of the ResourceRequest game event. */
	int ID_RESOURCE_REQUEST         = 70;
	
	/** Id of the ResourceRequestFulfill game event. */
	int ID_RESOURCE_REQUEST_FULFILL = 71;
	
	/** Id of the ResourceRequestCancel game event. */
	int ID_RESOURCE_REQUEST_CANCEL  = 72;
	
	/** Id of the GameUserLeave game event. */
	int ID_GAME_USER_LEAVE          = 101;
	
	
	/**
	 * Returns the array of game events.
	 * 
	 * @return the array of game events
	 */
	IEvent[] getEvents();
	
}
