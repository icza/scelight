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

import hu.scelight.gui.icon.Icons;
import hu.scelightapibase.util.gui.HasRIcon;
import hu.sllauncher.gui.icon.LRIcon;

import java.nio.file.Path;

/**
 * An abstract {@link DataSource} which provides the data from a file, most likely after a transformation and / or only a part of it.
 * 
 * @author Andras Belicza
 */
abstract class FileDataSource extends DataSource implements HasRIcon {
	
	/** Display name of the data source. */
	protected final String displayName;
	
	/** Suggested file name of the data. */
	protected final String fileName;
	
	/** Source file of data. */
	protected final Path   file;
	
	/** Ricon of the data source. */
	protected LRIcon       ricon;
	
	/**
	 * Creates a new {@link FileDataSource}.
	 * 
	 * @param displayName display name of the data source
	 * @param fileName suggested file name of the data
	 * @param file source file of data
	 */
	public FileDataSource( final String displayName, final String fileName, final Path file ) {
		this.displayName = displayName;
		this.fileName = fileName;
		this.file = file;
		
		String soruceName = file.getFileName().toString().toLowerCase();
		
		if ( soruceName != null ) {
			if ( soruceName.endsWith( ".sc2replay" ) )
				ricon = Icons.SC2_REPLAY;
			else if ( soruceName.endsWith( ".s2ma" ) )
				ricon = Icons.F_MAP;
		}
	}
	
	@Override
	public String getFileName() {
		return fileName;
	}
	
	@Override
	public String toString() {
		return displayName;
	}
	
	@Override
	public LRIcon getRicon() {
		return ricon;
	}
	
}
