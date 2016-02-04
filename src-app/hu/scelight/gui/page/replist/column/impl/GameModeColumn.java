/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.gui.page.replist.column.impl;

import hu.scelight.gui.page.replist.column.BaseColumn;
import hu.scelight.sc2.rep.model.attributesevents.GameMode;
import hu.scelight.sc2.rep.repproc.RepProcessor;

/**
 * Game mode column.
 * 
 * @author Andras Belicza
 */
public class GameModeColumn extends BaseColumn< GameMode > {
	
	/**
	 * Creates a new {@link GameModeColumn}.
	 */
	public GameModeColumn() {
		super( "Mode", GameMode.RICON, "Game mode", GameMode.class, false );
	}
	
	@Override
	public GameMode getData( final RepProcessor repProc ) {
		return repProc.replay.attributesEvents.getGameMode();
	}
	
}
