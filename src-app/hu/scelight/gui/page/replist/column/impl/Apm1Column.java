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
 * APM of the first player column.
 * 
 * @author Andras Belicza
 */
public class Apm1Column extends BaseColumn< Integer > {
	
	/**
	 * Creates a new {@link Apm1Column}.
	 */
	public Apm1Column() {
		super( "APM1", Icons.MY_APM, "APM (Actions / min) of the 1st player. Add yourself to the Favored Player list to be the first player.", Integer.class,
		        true );
	}
	
	@Override
	public Integer getData( final RepProcessor repProc ) {
		return repProc.playerUsers.length > 0 ? repProc.playerUsers[ 0 ].apm : null;
	}
	
}
