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
import hu.scelight.service.env.Env;
import hu.sllauncher.util.SizeValue;

import java.io.IOException;
import java.nio.file.Files;

/**
 * Replay file size column.
 * 
 * @author Andras Belicza
 */
public class FileSizeColumn extends BaseColumn< SizeValue > {
	
	/**
	 * Creates a new {@link FileSizeColumn}.
	 */
	public FileSizeColumn() {
		super( "File size", Icons.F_BLUE_DOCUMENT, "Replay file size", SizeValue.class, true );
	}
	
	@Override
	public SizeValue getData( final RepProcessor repProc ) {
		try {
			return new SizeValue( Files.size( repProc.file ) );
		} catch ( final IOException ie ) {
			Env.LOGGER.debug( "Failed to get file size: " + repProc.file, ie );
			return new SizeValue( -1 );
		}
	}
	
}
