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
 * SPM of the second player column.
 * 
 * @author Andras Belicza
 */
public class Spm2Column extends BaseColumn< Double > {
	
	/**
	 * Creates a new {@link Spm2Column}.
	 */
	public Spm2Column() {
		super( "SPM2", Icons.MY_SPM, "SPM (Screens / min) of the 2nd player. Add yourself to the Favored Player list and in 1v1 this will be your opponent.",
		        Double.class, true );
	}
	
	@Override
	public Double getData( final RepProcessor repProc ) {
		return repProc.playerUsers.length > 1 ? repProc.playerUsers[ 1 ].spm : null;
	}
	
}
