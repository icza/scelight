/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapi.service.repfoldermonitor;

import java.nio.file.Path;

/**
 * Event describing a detected new replay.
 * 
 * @author Andras Belicza
 * 
 * @see INewRepListener
 */
public interface INewRepEvent {
	
	/**
	 * Returns the detected new replay file.
	 * 
	 * <p>
	 * <b>Note:</b> The original replay file might get renamed, moved or deleted, this method always returns the result of those operations.
	 * </p>
	 * 
	 * @return the detected new replay file
	 */
	Path getFile();
	
	/**
	 * Returns the detected original new replay file.
	 * 
	 * <p>
	 * <b>Note:</b> Since the original replay file might get renamed, moved or deleted, the returned file might not exist! Use {@link #getFile()} to get the
	 * existing replay file.
	 * </p>
	 * 
	 * @return the original replay file
	 * @see #getFile()
	 */
	Path getOriginalFile();
	
}
