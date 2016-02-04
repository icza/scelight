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

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * A {@link DataSource} which provides the full content of a file as the data.
 * 
 * @author Andras Belicza
 */
class FullFileDataSource extends FileDataSource {
	
	/**
	 * Creates a new {@link FullFileDataSource}.
	 * 
	 * @param displayName display name of the data source
	 * @param file source of data, file whose content to return as the data
	 */
	public FullFileDataSource( final String displayName, final Path file ) {
		super( displayName, file.getFileName().toString(), file );
	}
	
	@Override
	public byte[] getCustomData() throws Exception {
		return Files.readAllBytes( file );
	}
	
}
