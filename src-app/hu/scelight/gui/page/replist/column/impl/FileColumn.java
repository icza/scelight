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

import java.nio.file.Path;

/**
 * Replay file column.
 * 
 * @author Andras Belicza
 */
public class FileColumn extends BaseColumn< Path > {
	
	/**
	 * Creates a new {@link FileColumn}.
	 */
	public FileColumn() {
		super( "Replay file", Icons.F_BLUE_DOCUMENT, "Replay file (full path with folder)", Path.class, false );
	}
	
	@Override
	public Path getData( final RepProcessor repProc ) {
		return repProc.file;
	}
	
}
