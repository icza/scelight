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

import hu.scelight.gui.page.replist.column.BaseColumn;
import hu.scelight.sc2.rep.repproc.Format;
import hu.scelight.sc2.rep.repproc.RepProcessor;

/**
 * Format column.
 * 
 * @author Andras Belicza
 */
public class FormatColumn extends BaseColumn< Format > {
	
	/**
	 * Creates a new {@link FormatColumn}.
	 */
	public FormatColumn() {
		super( "Format", Format.RICON, "Format", Format.class, false );
	}
	
	@Override
	public Format getData( final RepProcessor repProc ) {
		return repProc.getFormat();
	}
	
}
