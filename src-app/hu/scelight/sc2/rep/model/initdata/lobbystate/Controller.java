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

import hu.scelight.gui.icon.Icons;
import hu.scelight.sc2.rep.model.attributesevents.AttributesEvents;
import hu.scelightapi.sc2.rep.model.initdata.lobbystate.IController;
import hu.sllauncher.gui.icon.LRIcon;

/**
 * Player controller.
 * 
 * @author Andras Belicza
 */
public enum Controller implements IController {
	
	/** Open slot. */
	OPEN( "Open", Icons.MY_EMPTY, "Open" ),
	
	/** Closed slot. */
	CLOSED( "Closed", Icons.MY_EMPTY, "Clsd" ),
	
	/** Human. */
	HUMAN( "Human", Icons.F_USER, "Humn" ),
	
	/** Computer. */
	COMPUTER( "Computer", Icons.F_COMPUTER, "Comp" );
	
	
	/** Text value of the player controller. */
	public final String text;
	
	/** Ricon of the controller. */
	public final LRIcon ricon;
	
	/** Controller value used for {@link AttributesEvents#A_CONTROLLER}. */
	public final String attrValue;
	
	
	/**
	 * Creates a new {@link Controller}.
	 * 
	 * @param text text value
	 * @param ricon ricon of the controller
	 * @param attrValue controller value used for {@link AttributesEvents#A_CONTROLLER}
	 */
	private Controller( final String text, final LRIcon ricon, final String attrValue ) {
		this.text = text;
		this.attrValue = attrValue;
		this.ricon = ricon;
	}
	
	@Override
	public String toString() {
		return text;
	}
	
	@Override
	public LRIcon getRicon() {
		return ricon;
	}
	
	
	/** Cache of the values array. */
	public static final Controller[] VALUES = values();
	
}
