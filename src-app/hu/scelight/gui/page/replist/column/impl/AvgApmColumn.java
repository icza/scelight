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
 * Average APM column.
 * 
 * @author Andras Belicza
 */
public class AvgApmColumn extends BaseColumn< Integer > {
	
	/**
	 * Creates a new {@link AvgApmColumn}.
	 */
	public AvgApmColumn() {
		super( "Avg APM", Icons.MY_APM, "Weighted Average APM (Actions / min) of players", Integer.class, true );
	}
	
	@Override
	public Integer getData( final RepProcessor repProc ) {
		return repProc.getAvgAPM();
	}
	
}
