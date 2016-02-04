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

import hu.scelightapi.sc2.rep.repproc.IRepProcessor;
import hu.scelightapi.util.IStructView;
import hu.scelightapibase.gui.icon.IRIcon;
import hu.scelightapibase.util.gui.HasRIcon;

/**
 * Base event type.
 * 
 * @author Andras Belicza
 */
public interface IEvent extends IStructView, HasRIcon {
	
	/** Id field name. */
	public static final String         F_ID                = "id";
	
	/** Name field name. */
	public static final String         F_NAME              = "name";
	
	/** Loop field name. */
	public static final String         F_LOOP              = "loop";
	
	
	/** Player id field name (in game events present below base build 24764). */
	public static final String         F_PLAYER_ID         = "playerId";
	
	/** User id field name (in game events present from base build 24764). */
	public static final String         F_USER_ID           = "userId";
	
	/**
	 * Returns the event id.
	 * 
	 * @return the event id
	 */
	int getId();
	
	/**
	 * Returns the event name.
	 * 
	 * @return the event name
	 */
	String getName();
	
	/**
	 * Returns the game loop when the event occurred.
	 * 
	 * @return the game loop when the event occurred
	 */
	int getLoop();
	
	/**
	 * Returns the player id.
	 * 
	 * @return the player id
	 */
	Integer getPlayerId();
	
	/**
	 * Returns the user id causing the event.
	 * 
	 * @return the user id causing the event
	 */
	int getUserId();
	
	/**
	 * Returns the {@link IRIcon} of the event.
	 */
	@Override
	IRIcon getRicon();
	
	/**
	 * Returns the parameters of the event.
	 * 
	 * @param repProc reference to the rep processor in case additional info is required to decode / translate field values
	 * @return the parameters of the event
	 */
	String getParameters( IRepProcessor repProc );
	
	/**
	 * Returns the raw parameters of the event.<br>
	 * Includes all non-default fields from the underlying struct which has a non-null value, recursively.
	 * 
	 * @return the raw parameters of the event
	 */
	String getRawParameters();
	
}
