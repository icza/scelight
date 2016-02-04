/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.service.watcher;

import hu.scelightapi.service.repfoldermonitor.INewRepEvent;

import java.nio.file.Path;

/**
 * Event describing a new replay detection.
 * 
 * @author Andras Belicza
 */
public class NewRepEvent implements INewRepEvent {
	
	/** The detected new replay file. */
	public final Path file;
	
	/** The detected original new replay file. */
	public final Path originalFile;
	
	
	/**
	 * Creates a new {@link NewRepEvent}.
	 * 
	 * @param file the detected new replay file
	 * @param originalFile the detected original new replay file
	 */
	public NewRepEvent( final Path file, final Path originalFile ) {
		this.file = file;
		this.originalFile = originalFile;
	}
	
	@Override
	public Path getFile() {
		return file;
	}
	
	@Override
	public Path getOriginalFile() {
		return originalFile;
	}
	
}
