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
 * SQ of the second player column.
 * 
 * @author Andras Belicza
 */
public class Sq2Column extends BaseColumn< Integer > {
	
	/**
	 * Creates a new {@link Sq2Column}.
	 */
	public Sq2Column() {
		super( "SQ2", Icons.MY_SQ, "SQ (Spending Quotient) of the 2nd player. Add yourself to the Favored Player list and in 1v1 this will be your opponent.",
		        Integer.class, true );
	}
	
	@Override
	public Integer getData( final RepProcessor repProc ) {
		return repProc.playerUsers.length > 1 ? repProc.playerUsers[ 1 ].sq : null;
	}
	
}
