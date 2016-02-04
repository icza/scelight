/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.sc2.rep.model.initdata.lobbystate;

import hu.scelightapi.sc2.rep.model.initdata.lobbystate.IRole;

/**
 * User role.
 * 
 * @author Andras Belicza
 */
public enum Role implements IRole {
	
	/** Participant. */
	PARTICIPANT( "Participant" ),
	
	/** Spectator. Can only talk to other observers. */
	SPECTATOR( "Spectator" ),
	
	/** Referee. Can talk to players as well. */
	REFEREE( "Referee" );
	
	
	/** Text value of the player controller. */
	public final String text;
	
	
	/**
	 * Creates a new {@link Role}.
	 * 
	 * @param text text value
	 */
	private Role( final String text ) {
		this.text = text;
	}
	
	@Override
	public String toString() {
		return text;
	}
	
	
	/** Cache of the values array. */
	public static final Role[] VALUES = values();
	
}
