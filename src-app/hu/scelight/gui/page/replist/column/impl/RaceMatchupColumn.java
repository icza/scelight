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
import hu.scelight.sc2.rep.model.details.Race;
import hu.scelight.sc2.rep.repproc.RepProcessor;

/**
 * Race matchup column.
 * 
 * @author Andras Belicza
 */
public class RaceMatchupColumn extends BaseColumn< String > {
	
	/**
	 * Creates a new {@link RaceMatchupColumn}.
	 */
	public RaceMatchupColumn() {
		super( "Matchup", Race.RICON, "Race matchup", String.class, false );
	}
	
	@Override
	public String getData( final RepProcessor repProc ) {
		return repProc.getRaceMatchup();
	}
	
}
