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

import java.nio.file.Path;

/**
 * Watched folder info.
 * 
 * @author Andras Belicza
 */
class FolderInfo {
	
	/** Path of the folder. */
	public final Path    path;
	
	/** Tells if the folder is watched recursively. */
	public final boolean recursive;
	
	/**
	 * Creates a new {@link FolderInfo}.
	 * 
	 * @param path path of the folder
	 * @param recursive tells if the folder is watched recursively
	 */
	public FolderInfo( final Path path, final boolean recursive ) {
		this.path = path;
		this.recursive = recursive;
	}
	
}
