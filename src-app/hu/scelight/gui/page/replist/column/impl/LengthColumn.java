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
import hu.sllauncher.util.DurationValue;

/**
 * Game length column.
 * 
 * @author Andras Belicza
 */
public class LengthColumn extends BaseColumn< DurationValue > {
	
	/**
	 * Creates a new {@link LengthColumn}.
	 */
	public LengthColumn() {
		super( "Length", Icons.F_CLOCK_SELECT, "Game length", DurationValue.class, true );
	}
	
	@Override
	public DurationValue getData( final RepProcessor repProc ) {
		return new DurationValue( repProc.getLengthMs() );
	}
	
}
