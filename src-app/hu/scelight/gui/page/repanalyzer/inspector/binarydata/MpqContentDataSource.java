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

import hu.belicza.andras.mpq.IMpqContent;
import hu.belicza.andras.mpq.MpqParser;
import hu.scelight.sc2.rep.factory.RepContent;

import java.nio.file.Path;

/**
 * A {@link DataSource} which provides data from a file of an MPQ file.
 * 
 * @author Andras Belicza
 */
class MpqContentDataSource extends FileDataSource {
	
	/** Source of data inside the MPQ file. */
	private final IMpqContent mpqContent;
	
	/**
	 * Creates a new {@link MpqContentDataSource}.
	 * 
	 * @param file MPQ file whose {@link RepContent} to return as the data
	 * @param mpqContent source of data inside the MPQ file
	 */
	public MpqContentDataSource( final Path file, final IMpqContent mpqContent ) {
		super( mpqContent.toString(), mpqContent.getFileName(), file );
		
		this.mpqContent = mpqContent;
	}
	
	@Override
	public byte[] getCustomData() throws Exception {
		try ( final MpqParser mpqParser = new MpqParser( file ) ) {
			return mpqParser.getFile( mpqContent );
		}
	}
	
}
