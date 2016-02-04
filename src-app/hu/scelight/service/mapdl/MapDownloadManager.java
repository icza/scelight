/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.service.mapdl;

import hu.scelight.sc2.rep.model.initdata.gamedesc.CacheHandle;
import hu.scelight.service.env.Env;
import hu.scelight.service.settings.Settings;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

/**
 * Map download Manager service with support to register download listeners.
 * 
 * <p>
 * Implementation is thread-safe.
 * </p>
 * 
 * @author Andras Belicza
 */
public class MapDownloadManager {
	
	/** Queue of SC2 maps to download. */
	private final Queue< CacheHandle >  cacheHandleQueue = new LinkedList<>();
	
	/** Set of map hashes to download. */
	private final Set< String >         digestSet        = new HashSet<>();
	
	/** Number of active downloaders. */
	private int                         activeDownloadsCount;
	
	/** Registry of download listeners. */
	private final PropertyChangeSupport pcs              = new PropertyChangeSupport( this );
	
	
	/**
	 * Adds a new map to be downloaded.
	 * 
	 * @param cacheHandle cache handle of the map to be downloaded
	 */
	public synchronized void add( final CacheHandle cacheHandle ) {
		if ( digestSet.contains( cacheHandle.contentDigest ) )
			return;
		
		digestSet.add( cacheHandle.contentDigest );
		cacheHandleQueue.add( cacheHandle );
		
		pcs.firePropertyChange( null, null, null );
		
		checkDownloads();
	}
	
	/**
	 * Checks if a new download can be started.
	 */
	private synchronized void checkDownloads() {
		if ( cacheHandleQueue.isEmpty() || activeDownloadsCount >= Env.APP_SETTINGS.get( Settings.MAX_PARALLEL_MAP_DOWNLOADS ) )
			return;
		
		final CacheHandle cacheHandle = cacheHandleQueue.remove();
		
		final MapDownloaderJob mdj = new MapDownloaderJob( cacheHandle );
		mdj.setCallback( new Runnable() {
			@Override
			public void run() {
				synchronized ( MapDownloadManager.this ) {
					digestSet.remove( cacheHandle.contentDigest );
					activeDownloadsCount--;
					pcs.firePropertyChange( cacheHandle.contentDigest, null, null );
					checkDownloads();
				}
			}
		} );
		activeDownloadsCount++;
		mdj.start();
		
		pcs.firePropertyChange( null, null, null );
	}
	
	/**
	 * Returns the number of active downloads.
	 * 
	 * @return the number of active downloads
	 */
	public synchronized int getActiveCount() {
		return activeDownloadsCount;
	}
	
	/**
	 * Returns the number of queued downloads.
	 * 
	 * @return the number of queued downloads
	 */
	public synchronized int getQueuedCount() {
		return cacheHandleQueue.size();
	}
	
	/**
	 * Adds a download listener to all download events which will be called when a download starts or finishes.
	 * 
	 * @param listener listener to be added
	 * 
	 * @see #addListener(PropertyChangeListener, String)
	 * @see #removeListener(PropertyChangeListener)
	 */
	public void addListener( final PropertyChangeListener listener ) {
		pcs.addPropertyChangeListener( listener );
	}
	
	/**
	 * Removes a download listener bounded to all download events.
	 * 
	 * @param listener listener to be removed
	 * 
	 * @see #addListener(PropertyChangeListener)
	 */
	public void removeListener( final PropertyChangeListener listener ) {
		pcs.removePropertyChangeListener( listener );
	}
	
	/**
	 * Adds a download listener to the map specified by its hash.
	 * 
	 * @param listener listener to be added
	 * @param mapHash hash of the map to listen to
	 * 
	 * @see #addListener(PropertyChangeListener)
	 * @see #removeListener(PropertyChangeListener, String)
	 */
	public void addListener( final PropertyChangeListener listener, final String mapHash ) {
		pcs.addPropertyChangeListener( listener );
	}
	
	/**
	 * Adds a download listener from the map specified by its hash.
	 * 
	 * @param listener listener to be removed
	 * @param mapHash hash of the map to remove from
	 * 
	 * @see #addListener(PropertyChangeListener, String)
	 */
	public void removeListener( final PropertyChangeListener listener, final String mapHash ) {
		pcs.removePropertyChangeListener( listener );
	}
	
}
