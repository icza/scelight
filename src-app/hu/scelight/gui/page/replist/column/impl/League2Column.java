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
 * League of the second player column.
 * 
 * @author Andras Belicza
 */
public class League2Column extends BaseColumn< TableIcon< League > > {
	
	/**
	 * Creates a new {@link League2Column}.
	 */
	@SuppressWarnings( "unchecked" )
	public League2Column() {
		super( "L2", League.RICON, "League of the 2nd player. Add yourself to the Favored Player list and in 1v1 this will be your opponent.",
		        (Class< TableIcon< League > >) (Object) TableIcon.class, true, League.TABLE_ICON_COMPARATOR );
	}
	
	@Override
	public TableIcon< League > getData( final RepProcessor repProc ) {
		return repProc.playerUsers.length > 1 && repProc.playerUsers[ 1 ].uid != null ? repProc.playerUsers[ 1 ].uid.getHighestLeague().tableIcon
		        : League.UNKNOWN.tableIcon;
	}
	
}
