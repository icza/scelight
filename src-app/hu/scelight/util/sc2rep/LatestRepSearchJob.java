/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.util.sc2rep;

import hu.scelight.gui.icon.Icons;
import hu.scelight.service.env.Env;
import hu.sllauncher.service.job.Job;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Collections;

/**
 * Latest replay searcher job.
 * 
 * @author Andras Belicza
 */
public class LatestRepSearchJob extends Job {
	
	/** Folder in which to search. */
	private final Path    folder;
	
	/** Tells if replays contained in sub-folders are also to be searched. */
	private final boolean recursive;
	
	/** Latest replay. */
	private Path          latestReplay;
	
	/** Last modified time of the latest replay. */
	private FileTime      latestTime = FileTime.fromMillis( 0 );
	
	/**
	 * Creates a new {@link LatestRepSearchJob}.
	 * 
	 * @param folder folder in which to search
	 * @param recursive tells if replays contained in sub-folders are also to be searched
	 */
	public LatestRepSearchJob( final Path folder, final boolean recursive ) {
		super( "Latest Replay Search: " + folder, Icons.F_BINOCULAR_ARROW );
		
		this.folder = folder;
		this.recursive = recursive;
	}
	
	@Override
	public void jobRun() {
		if ( !Files.exists( folder ) || !Files.isDirectory( folder ) )
			return;
		
		try {
			Files.walkFileTree( folder, Collections.< FileVisitOption > emptySet(), recursive ? Integer.MAX_VALUE : 1, new SimpleFileVisitor< Path >() {
				@Override
				public FileVisitResult visitFile( final Path file, final BasicFileAttributes attrs ) throws IOException {
					if ( !mayContinue() )
						return FileVisitResult.TERMINATE;
					
					if ( !attrs.isDirectory() && RepUtils.hasRepExt( file ) && attrs.lastModifiedTime().compareTo( latestTime ) > 0 ) {
						latestReplay = file;
						latestTime = attrs.lastModifiedTime();
					}
					
					return FileVisitResult.CONTINUE;
				}
			} );
		} catch ( final IOException ie ) {
			Env.LOGGER.error( "Could not search replays in folder: " + folder, ie );
			latestReplay = null;
			return;
		}
		
		if ( cancelRequested )
			latestReplay = null;
	}
	
	/**
	 * Returns the latest replay, the result of the search.
	 * 
	 * @return the latest replay file in the specified folder; <code>null</code> if the specified folder does no exists or is not a folder or if the search was
	 *         aborted or some error occurred
	 */
	public Path getLatestReplay() {
		return latestReplay;
	}
	
	/**
	 * Returns the last modified time of the latest replay.
	 * 
	 * @return the last modified time of the latest replay
	 */
	public FileTime getLatestTime() {
		return latestTime;
	}
	
}
