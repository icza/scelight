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
 * Average SPM column.
 * 
 * @author Andras Belicza
 */
public class AvgSpmColumn extends BaseColumn< Double > {
	
	/**
	 * Creates a new {@link AvgSpmColumn}.
	 */
	public AvgSpmColumn() {
		super( "Avg SPM", Icons.MY_SPM, "Weighted Average SPM (Screens / min) of players", Double.class, true );
	}
	
	@Override
	public Double getData( final RepProcessor repProc ) {
		return repProc.getAvgSPM();
	}
	
}
