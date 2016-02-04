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
import hu.scelight.sc2.rep.model.initdata.userinitdata.League;
import hu.scelight.sc2.rep.repproc.RepProcessor;

/**
 * League matchup column.
 * 
 * @author Andras Belicza
 */
public class LeagueMatchupColumn extends BaseColumn< String > {
	
	/**
	 * Creates a new {@link LeagueMatchupColumn}.
	 */
	public LeagueMatchupColumn() {
		super( "Leagues", League.RICON, "League matchup", String.class, false );
	}
	
	@Override
	public String getData( final RepProcessor repProc ) {
		return repProc.getLeagueMatchup();
	}
	
}
