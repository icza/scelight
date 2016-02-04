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

/**
 * Average SQ column.
 * 
 * @author Andras Belicza
 */
public class AvgSqColumn extends BaseColumn< Integer > {
	
	/**
	 * Creates a new {@link AvgSqColumn}.
	 */
	public AvgSqColumn() {
		super( "Avg SQ", Icons.MY_SQ, "Weighted Average SQ (Spending Quotient) of players", Integer.class, true );
	}
	
	@Override
	public Integer getData( final RepProcessor repProc ) {
		return repProc.getAvgSQ();
	}
	
}
