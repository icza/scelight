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

import java.util.Date;

/**
 * Replay date column.
 * 
 * @author Andras Belicza
 */
public class DateColumn extends BaseColumn< Date > {
	
	/**
	 * Creates a new {@link DateColumn}.
	 */
	public DateColumn() {
		super( "Date", Icons.F_CALENDAR_BLUE, "Replay date", Date.class, true );
	}
	
	@Override
	public Date getData( final RepProcessor repProc ) {
		return repProc.replay.details.getTime();
	}
	
}
