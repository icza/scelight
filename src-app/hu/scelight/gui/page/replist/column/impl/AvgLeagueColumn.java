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
 * Average league column.
 * 
 * @author Andras Belicza
 */
public class AvgLeagueColumn extends BaseColumn< TableIcon< League > > {
	
	/**
	 * Creates a new {@link AvgLeagueColumn}.
	 */
	@SuppressWarnings( "unchecked" )
	public AvgLeagueColumn() {
		super( "Avg L", League.RICON, "Average league of players", (Class< TableIcon< League > >) (Object) TableIcon.class, true, League.TABLE_ICON_COMPARATOR );
	}
	
	@Override
	public TableIcon< League > getData( final RepProcessor repProc ) {
		return repProc.getAvgLeague().tableIcon;
	}
	
}
