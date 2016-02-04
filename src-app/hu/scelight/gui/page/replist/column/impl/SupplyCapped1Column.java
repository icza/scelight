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
 * Supply-capped percent of the first player column.
 * 
 * @author Andras Belicza
 */
public class SupplyCapped1Column extends BaseColumn< Double > {
	
	/**
	 * Creates a new {@link SupplyCapped1Column}.
	 */
	public SupplyCapped1Column() {
		super( "SC1%", Icons.MY_SCP, "Supply-capped % of the 1st player. Add yourself to the Favored Player list to be the first player.", Double.class, true );
	}
	
	@Override
	public Double getData( final RepProcessor repProc ) {
		return repProc.playerUsers.length > 0 ? repProc.playerUsers[ 0 ].supplyCappedPercent : null;
	}
	
}
