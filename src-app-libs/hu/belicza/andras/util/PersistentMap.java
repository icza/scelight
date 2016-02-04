/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.belicza.andras.util;

import hu.scelight.service.env.Env;
import hu.sllauncher.bean.VersionBean;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * A file-persistent map-like utility.
 * 
 * <p>
 * The implementation is thread-safe.<br>
 * The implementation is also close-state-tolerant: every method can be called after a {@link PersistentMap#close()}, but they will not return any valid
 * results.
 * </p>
 * 
 * @author Andras Belicza
 */
public class PersistentMap {
	
	/** Root folder to read/write files to. */
	private final Path                  rootFolder;
	
	/** Index file. */
	private final RandomAccessFile      indexFile;
	
	/** Data file. */
	private final RandomAccessFile      dataFile;
	
	/** The version of the data stored in the persistent map. */
	private final VersionBean           version;
	
	/** Registry of change listeners. */
	private final PropertyChangeSupport pcs = new PropertyChangeSupport( this );
	
	
	/**
	 * Info about a value in the persistent map.
	 * 
	 * @author Andras Belicza
	 */
	private static class ValueInfo {
		/** Byte position of the value. */
		public final int pos;
		
		/** Size of the value in bytes. */
		public final int size;
		
		/**
		 * Creates a new {@link ValueInfo}.
		 * 
		 * @param pos byte position of the value
		 * @param size size of the value in bytes
		 */
		public ValueInfo( final int pos, final int size ) {
			this.pos = pos;
			this.size = size;
		}
	}
	
	/**
	 * Index map: map of the persistent map keys and value info set.<br>
	 * This is an in-memory cached value of the content of the index file ({@link #indexFile}).
	 */
	private final Map< String, ValueInfo > indexMap = new HashMap<>();
	
	/** Tells if the persistent map has been closed. */
	private boolean                        closed;
	
	/**
	 * Creates a new {@link PersistentMap}.
	 * 
	 * @param rootFolder root folder to read/write files to
	 * @param version tells the version of the data stored in the persistent map; if it does not equal to the version of the persistent file, it will be cleared
	 *            automatically
	 * @throws IOException if the persistent map could not be initialized
	 */
	public PersistentMap( final Path rootFolder, final VersionBean version ) throws IOException {
		this.rootFolder = rootFolder;
		
		if ( !Files.exists( rootFolder ) )
			Files.createDirectories( rootFolder );
		
		final Path indexPath = rootFolder.resolve( "index" );
		indexFile = new RandomAccessFile( indexPath.toFile(), "rw" );
		if ( indexFile.getChannel().tryLock() == null )
			throw new IOException( "Index file is already in use: " + indexPath );
		
		final Path dataPath = rootFolder.resolve( "data" );
		dataFile = new RandomAccessFile( dataPath.toFile(), "rw" );
		if ( dataFile.getChannel().tryLock() == null )
			throw new IOException( "Data file is already in use: " + dataPath );
		
		this.version = version;
		
		final long indexSize = indexFile.length();
		// New file or old version?
		final VersionBean oldVersion = indexSize == 0 ? null : VersionBean.fromString( indexFile.readUTF() );
		if ( !version.equals( oldVersion ) ) {
			if ( oldVersion != null && Env.LOGGER.testTrace() )
				Env.LOGGER.trace( "Persistent map content outdated (old version: " + oldVersion + ", new version: " + version + "): " + rootFolder );
			clear();
		} else {
			// Read the index file into memory
			String key;
			while ( indexFile.getFilePointer() < indexSize ) {
				key = indexFile.readUTF();
				final int pos = indexFile.readInt();
				final int size = indexFile.readInt();
				indexMap.put( key, new ValueInfo( pos, size ) );
			}
			if ( Env.LOGGER.testTrace() )
				Env.LOGGER.trace( "Loaded " + indexMap.size() + " entries from persistent map: " + rootFolder );
		}
	}
	
	/**
	 * Puts a new entry into the persistent map.
	 * 
	 * @param key key of the new entry
	 * @param value value of the new entry
	 */
	public synchronized void put( final String key, final byte[] value ) {
		if ( closed || indexMap.containsKey( key ) )
			return;
		
		try {
			final ValueInfo valueInfo = new ValueInfo( (int) dataFile.length(), value.length );
			
			dataFile.setLength( valueInfo.pos + value.length );
			dataFile.seek( valueInfo.pos );
			dataFile.write( value );
			
			// Index file's pointer is always at the end, no need to seek
			indexFile.writeUTF( key );
			indexFile.writeInt( valueInfo.pos );
			indexFile.writeInt( valueInfo.size );
			
			indexMap.put( key, valueInfo );
			
			pcs.firePropertyChange( null, null, null );
		} catch ( final IOException ie ) {
			Env.LOGGER.error( "Error adding new value to persistent map: " + rootFolder, ie );
		}
	}
	
	/**
	 * Reads a value from the persistent map.
	 * 
	 * @param key key whose associated value to be read
	 * @return the value associated with the specified key; or <code>null</code> if there is no value associated with the specified key
	 */
	public synchronized byte[] get( final String key ) {
		if ( closed )
			return null;
		
		final ValueInfo valueInfo = indexMap.get( key );
		
		if ( valueInfo != null )
			try {
				dataFile.seek( valueInfo.pos );
				
				final byte[] value = new byte[ valueInfo.size ];
				dataFile.readFully( value );
				
				return value;
			} catch ( final IOException ie ) {
				Env.LOGGER.error( "Error reading value from persistent map: " + rootFolder, ie );
			}
		
		return null;
	}
	
	/**
	 * Returns the size (number of entries) of the persistent map.
	 * 
	 * @return the size (number of entries) of the persistent map; or <code>null</code> if the persistent map is closed
	 */
	public synchronized Integer size() {
		if ( closed )
			return null;
		
		return indexMap.size();
	}
	
	/**
	 * Clears the persistent map.
	 */
	public synchronized void clear() {
		if ( closed )
			return;
		
		try {
			if ( Env.LOGGER.testTrace() )
				Env.LOGGER.trace( ( indexFile.length() == 0 ? "Initializing" : "Clearing" ) + " persistent map: " + rootFolder );
			
			indexFile.setLength( 0L );
			indexFile.writeUTF( version.toString() );
			dataFile.setLength( 0L );
			indexMap.clear();
			
			pcs.firePropertyChange( null, null, null );
		} catch ( final IOException ie ) {
			Env.LOGGER.error( "Error clearing persistent map: " + rootFolder, ie );
		}
	}
	
	/**
	 * Closes the persistent map.
	 */
	public synchronized void close() {
		if ( closed )
			return;
		
		closed = true;
		
		try {
			indexFile.close();
		} catch ( final IOException ie ) {
			// Silently ignore. We're about to exit anyway.
		}
		try {
			dataFile.close();
		} catch ( final IOException ie ) {
			// Silently ignore. We're about to exit anyway.
		}
	}
	
	/**
	 * Tells if the persistent map has been closed.
	 * 
	 * @return true if the persistent map has been closed; false otherwise
	 */
	public synchronized boolean isClosed() {
		return closed;
	}
	
	/**
	 * Adds a change listener which will be called when the persistent map changes.
	 * 
	 * @param listener listener to be added
	 * 
	 * @see #removeListener(PropertyChangeListener)
	 */
	public void addListener( final PropertyChangeListener listener ) {
		pcs.addPropertyChangeListener( listener );
	}
	
	/**
	 * Removes a change listener.
	 * 
	 * @param listener listener to be removed
	 * 
	 * @see #addListener(PropertyChangeListener)
	 */
	public void removeListener( final PropertyChangeListener listener ) {
		pcs.removePropertyChangeListener( listener );
	}
	
}
