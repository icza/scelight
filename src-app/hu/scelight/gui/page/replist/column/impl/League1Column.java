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
import hu.scelight.util.gui.TableIcon;

/**
 * League of the first player column.
 * 
 * @author Andras Belicza
 */
public class League1Column extends BaseColumn< TableIcon< League > > {
	
	/**
	 * Creates a new {@link League1Column}.
	 */
	@SuppressWarnings( "unchecked" )
	public League1Column() {
		super( "L1", League.RICON, "League of the 1st player. Add yourself to the Favored Player list to be the first player.",
		        (Class< TableIcon< League > >) (Object) TableIcon.class, true, League.TABLE_ICON_COMPARATOR );
	}
	
	@Override
	public TableIcon< League > getData( final RepProcessor repProc ) {
		return repProc.playerUsers.length > 0 && repProc.playerUsers[ 0 ].uid != null ? repProc.playerUsers[ 0 ].uid.getHighestLeague().tableIcon
		        : League.UNKNOWN.tableIcon;
	}
	
}
