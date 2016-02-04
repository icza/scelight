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

import hu.scelight.gui.icon.Icons;
import hu.scelight.gui.page.replist.column.BaseColumn;
import hu.scelight.sc2.rep.repproc.RepProcessor;
import hu.sllauncher.util.RelativeDate;

/**
 * Played ago column, displays a relative date.
 * 
 * @author Andras Belicza
 */
public class PlayedAgoColumn extends BaseColumn< RelativeDate > {
	
	/**
	 * Creates a new {@link PlayedAgoColumn}.
	 */
	public PlayedAgoColumn() {
		super( "Played", Icons.F_CALENDAR_BLUE,
		        "Replay date in played 'xxx' ago format; Legend: s = sec, m = min, h = hour, d = day, w = week, M = month, y = year", RelativeDate.class, true );
	}
	
	@Override
	public RelativeDate getData( final RepProcessor repProc ) {
		return new RelativeDate( repProc.replay.details.getTime(), false, 2, false );
	}
	
}
