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

import hu.scelight.sc2.rep.model.initdata.lobbystate.Controller;
import hu.scelightapibase.util.IEnum;
import hu.scelightapibase.util.gui.HasRIcon;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Player controller.
 * 
 * @author Andras Belicza
 * 
 * @see IEnum
 */
public interface IController extends HasRIcon, IEnum {
	
	/** Open slot. */
	IController         OPEN       = Controller.OPEN;
	
	/** Closed slot. */
	IController         CLOSED     = Controller.CLOSED;
	
	/** Human. */
	IController         HUMAN      = Controller.HUMAN;
	
	/** Computer. */
	IController         COMPUTER   = Controller.COMPUTER;
	
	
	/** An unmodifiable list of all the controllers. */
	List< IController > VALUE_LIST = Collections.unmodifiableList( Arrays.< IController > asList( Controller.VALUES ) );
	
}
