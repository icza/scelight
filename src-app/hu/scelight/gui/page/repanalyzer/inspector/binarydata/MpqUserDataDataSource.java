/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.gui.page.repanalyzer.inspector.binarydata;

import hu.belicza.andras.mpq.MpqParser;
import hu.scelight.sc2.rep.factory.RepContent;

import java.nio.file.Path;

/**
 * A {@link DataSource} which provides the MPQ user data of an MPQ file.
 * 
 * @author Andras Belicza
 */
class MpqUserDataDataSource extends FileDataSource {
	
	/**
	 * Creates a new {@link MpqUserDataDataSource}.
	 * 
	 * @param file MPQ file whose {@link RepContent} to return as the data
	 */
	public MpqUserDataDataSource( final Path file ) {
		super( "Header (MPQ User Data)", "header", file );
	}
	
	@Override
	public byte[] getCustomData() throws Exception {
		try ( final MpqParser mpqParser = new MpqParser( file ) ) {
			return mpqParser.getUserData().userData;
		}
	}
	
}
