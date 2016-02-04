/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapi.sc2.rep.model.initdata.lobbystate;

import hu.scelight.sc2.rep.model.initdata.lobbystate.Role;
import hu.scelightapibase.util.IEnum;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * User role.
 * 
 * @author Andras Belicza
 */
public interface IRole extends IEnum {
	
	/** Participant. */
	IRole         PARTICIPANT = Role.PARTICIPANT;
	
	/** Spectator. Can only talk to other observers. */
	IRole         SPECTATOR   = Role.SPECTATOR;
	
	/** Referee. Can talk to players as well. */
	IRole         REFEREE     = Role.REFEREE;
	
	
	/** An unmodifiable list of all the roles. */
	List< IRole > VALUE_LIST  = Collections.unmodifiableList( Arrays.< IRole > asList( Role.VALUES ) );
	
}
