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
import hu.scelight.util.Utils;

/**
 * Replay file name column (without extension).
 * 
 * @author Andras Belicza
 */
public class FileNameColumn extends BaseColumn< String > {
	
	/**
	 * Creates a new {@link FileNameColumn}.
	 */
	public FileNameColumn() {
		super( "File name", Icons.F_BLUE_DOCUMENT, "Replay file name (without folder and extension)", String.class, false );
	}
	
	@Override
	public String getData( final RepProcessor repProc ) {
		return Utils.getFileNameWithoutExt( repProc.file );
	}
	
}
