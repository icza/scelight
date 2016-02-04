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

import hu.scelight.service.env.Env;
import hu.scelight.util.Utils;
import hu.scelight.util.sc2rep.RepUtils;
import hu.sllauncher.util.ControlledThread;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;

/**
 * File watcher service, primary goal is to watch replay folders for new replays.
 * 
 * <p>
 * Implementation is thread safe.
 * </p>
 * 
 * @author Andras Belicza
 */
public class Watcher extends ControlledThread {
	
	/** Reference to the native IO watcher service. */
	private final WatchService                 watcher;
	
	/** Registered keys and their info. */
	private final Map< WatchKey, FolderInfo >  keyInfoMap     = new HashMap<>();
	
	/** A map of paths to be ignored. */
	private final Map< Path, IgnoredPathInfo > ignoredPathMap = new HashMap<>();
	
	
	/**
	 * Creates a new {@link Watcher}.
	 * 
	 * @throws IOException if native IO watcher service cannot be obtained
	 */
	public Watcher() throws IOException {
		super( "Watcher" );
		
		watcher = FileSystems.getDefault().newWatchService();
	}
	
	/**
	 * Clears all previously registered watched folders.
	 */
	public void clear() {
		synchronized ( keyInfoMap ) {
			for ( final WatchKey key : keyInfoMap.keySet() )
				key.cancel();
			keyInfoMap.clear();
		}
	}
	
	/**
	 * Registers a new folder to be watched.
	 * 
	 * @param folder folder to be watched
	 * @param recursive tells if the folder is to be watched recursively
	 */
	public void watchFolder( final Path folder, final boolean recursive ) {
		try {
			if ( recursive ) {
				// Register folder and sub-folders recursively
				Files.walkFileTree( folder, new SimpleFileVisitor< Path >() {
					@Override
					public FileVisitResult preVisitDirectory( final Path dir, final BasicFileAttributes attrs ) throws IOException {
						final WatchKey key = dir.register( watcher, StandardWatchEventKinds.ENTRY_CREATE );
						synchronized ( keyInfoMap ) {
							keyInfoMap.put( key, new FolderInfo( dir, recursive ) );
						}
						return FileVisitResult.CONTINUE;
					}
				} );
			} else {
				final WatchKey key = folder.register( watcher, StandardWatchEventKinds.ENTRY_CREATE );
				synchronized ( keyInfoMap ) {
					keyInfoMap.put( key, new FolderInfo( folder, recursive ) );
				}
			}
		} catch ( final IOException ie ) {
			Env.LOGGER.error( "Failed to initiate folder monitoring: " + folder, ie );
		}
	}
	
	/**
	 * Adds a path to be ignored.
	 * 
	 * @param path path to be ignored
	 */
	public void addIgnoredPath( final Path path ) {
		synchronized ( ignoredPathMap ) {
			final IgnoredPathInfo info = ignoredPathMap.get( path );
			if ( info == null )
				ignoredPathMap.put( path, new IgnoredPathInfo() );
			else
				info.registerAdded();
		}
	}
	
	/**
	 * Removes a previously added ignored path.
	 * 
	 * @param path ignored path to be removed
	 */
	public void removeIgnoredPath( final Path path ) {
		synchronized ( ignoredPathMap ) {
			final IgnoredPathInfo info = ignoredPathMap.get( path );
			if ( info == null )
				return; // Not contained, should never get here
				
			if ( --info.count == 0 )
				ignoredPathMap.remove( path );
		}
	}
	
	@Override
	public void customRun() {
		while ( mayContinue() ) {
			final WatchKey key = watcher.poll();
			
			if ( key != null ) {
				for ( final WatchEvent< ? > event : key.pollEvents() ) {
					final Kind< ? > kind = event.kind();
					
					if ( kind == StandardWatchEventKinds.OVERFLOW ) {
						Env.LOGGER.debug( "Overflow WatchEvent detected." );
						continue; // Ignore these
					}
					
					if ( kind == StandardWatchEventKinds.ENTRY_CREATE ) {
						final FolderInfo info;
						synchronized ( keyInfoMap ) {
							info = keyInfoMap.get( key );
						}
						
						final Path file = info.path.resolve( (Path) event.context() ); // Event context is the path
						synchronized ( ignoredPathMap ) {
							final IgnoredPathInfo ignoredInfo = ignoredPathMap.get( file );
							if ( ignoredInfo != null ) {
								removeIgnoredPath( file );
								// Only take path really ignored (valid),
								// if it was added to be ignored no more than 1 minute ago
								if ( ignoredInfo.firstDate + Utils.MS_IN_MIN > System.currentTimeMillis() )
									continue;
							}
						}
						if ( Env.LOGGER.testTrace() )
							Env.LOGGER.trace( "[Watcher] Detected new entry: " + file );
						
						// If the new entry is a folder and parent is watched recursively, add the new entry too (recursively)
						if ( Files.isDirectory( file, LinkOption.NOFOLLOW_LINKS ) ) {
							if ( info.recursive )
								watchFolder( file, info.recursive );
						} else {
							// Potential new replay
							if ( RepUtils.hasRepExt( file ) )
								new NewReplayHandlerJob( file, info ).start();
						}
					}
				}
				
				// Reset the key - required to receive further watch events!
				if ( !key.reset() ) { // reset() returns whether the key is still valid
					// Key became invalid (e.g. folder deleted), remove it
					synchronized ( keyInfoMap ) {
						keyInfoMap.remove( key );
					}
				}
				
			} else
				checkedSleep( 20 );
		}
	}
	
}
