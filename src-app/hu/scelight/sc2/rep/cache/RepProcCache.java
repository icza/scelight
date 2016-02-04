/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.sc2.rep.cache;

import hu.belicza.andras.mpq.MpqParser;
import hu.belicza.andras.util.PersistentMap;
import hu.scelight.sc2.rep.factory.RepContent;
import hu.scelight.sc2.rep.model.Header;
import hu.scelight.sc2.rep.model.Replay;
import hu.scelight.sc2.rep.model.attributesevents.AttributesEvents;
import hu.scelight.sc2.rep.model.details.Details;
import hu.scelight.sc2.rep.model.initdata.InitData;
import hu.scelight.sc2.rep.model.messageevents.MessageEvents;
import hu.scelight.sc2.rep.repproc.RepProcessor;
import hu.scelight.sc2.rep.repproc.User;
import hu.scelight.sc2.rep.s2prot.Protocol;
import hu.scelight.service.env.Env;
import hu.scelight.service.settings.Settings;
import hu.scelightapibase.bean.settings.ISettingChangeEvent;
import hu.scelightapibase.bean.settings.ISettingChangeListener;
import hu.sllauncher.bean.VersionBean;

import java.beans.PropertyChangeListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

import javax.xml.bind.JAXB;

/**
 * Replay processor cache.
 * 
 * @author Andras Belicza
 */
public class RepProcCache {
	
	/** Version of the data stored in the replay processor cache. */
	public static final VersionBean             VERSION          = new VersionBean( 1, 0, 5 );
	
	/** Folder where the persistent cache files are stored. */
	private static final Path                   CACHE_FOLDER     = Env.PATH_WORKSPACE.resolve( "rep-proc-cache" );
	
	/** Persistent map storing the replay processor cache data (key: replay identifier unique short key; value: cache info. */
	private static final PersistentMap          PM;
	static {
		try {
			PM = new PersistentMap( CACHE_FOLDER, VERSION );
		} catch ( final IOException ie ) {
			throw new RuntimeException( ie );
		}
	}
	
	/** Path of the cache config bean file. */
	private static final Path                   PATH_CONFIG_BEAN = CACHE_FOLDER.resolve( "config.xml" );
	
	/** Cache config bean. */
	private static ConfigBean                   config;
	
	/** Setting change listener to listen to settings which the cache depends on. */
	private static final ISettingChangeListener scl;
	static {
		scl = new ISettingChangeListener() {
			@Override
			public void valuesChanged( final ISettingChangeEvent event ) {
				if ( event.affected( Settings.INITIAL_PER_MIN_CALC_EXCL_TIME ) ) {
					// Clear rep proc cache if it was built with a different initial per-min calculation exclusion time
					final ConfigBean config = getConfig();
					if ( config == null || !event.get( Settings.INITIAL_PER_MIN_CALC_EXCL_TIME ).equals( config.getInitialPerMinCalcExclTime() ) )
						clear();
				}
			}
		};
		Env.APP_SETTINGS.addAndExecuteChangeListener( Settings.INITIAL_PER_MIN_CALC_EXCL_TIME.SELF_SET, scl );
	}
	
	/**
	 * Returns the cache config bean.
	 * 
	 * @return the cache config bean
	 */
	private static ConfigBean getConfig() {
		if ( config == null && Files.exists( PATH_CONFIG_BEAN ) )
			try {
				config = JAXB.unmarshal( PATH_CONFIG_BEAN.toFile(), ConfigBean.class );
			} catch ( final Exception e ) {
				Env.LOGGER.error( "Failed to read replay processor cache config from: " + PATH_CONFIG_BEAN, e );
			}
		
		return config;
	}
	
	/**
	 * Sets the cache config bean.
	 * 
	 * @param config cache config bean to be set
	 */
	private static void setConfig( final ConfigBean config ) {
		try {
			JAXB.marshal( config, PATH_CONFIG_BEAN.toFile() );
			RepProcCache.config = config;
		} catch ( final Exception e ) {
			Env.LOGGER.error( "Failed to write replay processor cache config to: " + PATH_CONFIG_BEAN, e );
		}
	}
	
	/**
	 * Returns a {@link RepProcessor} constructed and initialized from the cache for the specified replay key.
	 * 
	 * @param replayKey key of the replay whose {@link RepProcessor} to return
	 * @param file replay file
	 * @return a {@link RepProcessor} constructed and initialized from the cache for the specified replay key or <code>null</code> if no cache entry for the
	 *         specified replay key
	 */
	public static RepProcessor getRepProc( final String replayKey, final Path file ) {
		final byte[] data = PM.get( replayKey );
		if ( data == null )
			return null;
		
		try ( final DataInputStream in = new DataInputStream( new InflaterInputStream( new ByteArrayInputStream( data ) ) ) ) {
			final Replay replay = new Replay();
			
			// Read replay header
			// If replay is in the cache, it (its base version) is surely supported.
			replay.header = new Header( Protocol.DEFAULT.decodeHeader( readByteArray( in ) ) );
			
			final Protocol p = Protocol.get( replay.header.getBaseBuild() );
			
			// Details
			replay.details = new Details( p.decodeDetails( readByteArray( in ) ) );
			
			// Init data
			replay.initData = new InitData( p.decodeInitData( readByteArray( in ) ) );
			
			// Attributes events
			replay.attributesEvents = new AttributesEvents( p.decodeAttributesEvents( readByteArray( in ) ) );
			
			// Message events
			replay.messageEvents = new MessageEvents( p.decodeMessageEvents( readByteArray( in ), replay ) );
			
			final RepProcessor repProc = new RepProcessor( file, replay );
			
			// Read derived repproc data (must use original users, RepProcessor's constructor reorders users, and the order
			// might differ from the order at the time it was saved!)
			for ( final User u : repProc.originalUsers ) {
				u.leaveLoop = in.readInt();
				u.lastCmdLoop = in.readInt();
				u.apmActions = in.readInt();
				u.spmActions = in.readInt();
				u.sq = in.readInt();
				u.startDirection = in.readInt();
				u.supplyCappedPercent = in.readDouble();
			}
			
			repProc.initFromCacheData();
			
			return repProc;
		} catch ( final Exception e ) {
			Env.LOGGER.error( "Error reading replay from replay processor cache, replayKey: " + replayKey, e );
		}
		
		return null;
	}
	
	/**
	 * Puts the specified replay processor into the replay cache.
	 * 
	 * @param replayKey key of the replay
	 * @param repProc replay processor to be cached
	 * @param mpqParser mpq parser of the replay file
	 */
	public static void putRepProc( final String replayKey, final RepProcessor repProc, final MpqParser mpqParser ) {
		final ByteArrayOutputStream bout = new ByteArrayOutputStream( 3000 );
		
		try ( final DataOutputStream out = new DataOutputStream( new DeflaterOutputStream( bout ) ) ) {
			
			// Replay header
			writeByteArray( mpqParser.getUserData().userData, out );
			
			// Details
			writeByteArray( mpqParser.getFile( RepContent.DETAILS ), out );
			
			// Init data
			writeByteArray( mpqParser.getFile( RepContent.INIT_DATA ), out );
			
			// Attributes events
			writeByteArray( mpqParser.getFile( RepContent.ATTRIBUTES_EVENTS ), out );
			
			// Message events
			writeByteArray( mpqParser.getFile( RepContent.MESSAGE_EVENTS ), out );
			
			// Write derived repproc data (must use original users, RepProcessor's constructor reorders users, and the order
			// might differ from the order at the time it was saved!)
			for ( final User u : repProc.originalUsers ) {
				out.writeInt( u.leaveLoop );
				out.writeInt( u.lastCmdLoop );
				out.writeInt( u.apmActions );
				out.writeInt( u.spmActions );
				out.writeInt( u.sq );
				out.writeInt( u.startDirection );
				out.writeDouble( u.supplyCappedPercent );
			}
			
		} catch ( final Exception e ) {
			Env.LOGGER.error( "Error writing replay to replay processor cache, replayKey: " + replayKey, e );
		}
		
		PM.put( replayKey, bout.toByteArray() );
	}
	
	/**
	 * Reads a byte array from the specified {@link DataInputStream}.
	 * <p>
	 * First the length of the array is read as an int, then the content of the array.
	 * </p>
	 * 
	 * @param in {@link DataInputStream} to read from
	 * @return the read byte array
	 * @throws IOException if reading data from the stream throws exception
	 */
	private static byte[] readByteArray( final DataInputStream in ) throws IOException {
		final byte[] array = new byte[ in.readInt() ];
		
		in.readFully( array );
		
		return array;
	}
	
	/**
	 * Writes the specified byte array into the specified {@link DataOutputStream}.
	 * <p>
	 * First the length of the array is written as an int, then the content of the array.
	 * </p>
	 * 
	 * @param array array to be written
	 * @param out {@link DataOutputStream} to write to
	 * @throws IOException if writing data to the stream throws exception
	 */
	private static void writeByteArray( final byte[] array, final DataOutputStream out ) throws IOException {
		out.writeInt( array.length );
		out.write( array );
	}
	
	/**
	 * Clears the replay processor cache.
	 */
	public static void clear() {
		PM.clear();
		
		ConfigBean config = getConfig();
		if ( config == null || !Env.APP_SETTINGS.get( Settings.INITIAL_PER_MIN_CALC_EXCL_TIME ).equals( config.getInitialPerMinCalcExclTime() ) ) {
			// Config changed: delete old and re-save current.
			try {
				Files.deleteIfExists( PATH_CONFIG_BEAN );
			} catch ( final IOException ie ) {
				Env.LOGGER.error( "Failed to delete replay processor cache config: " + PATH_CONFIG_BEAN, ie );
			}
			// Have to re-save current config
			config = new ConfigBean();
			config.setInitialPerMinCalcExclTime( Env.APP_SETTINGS.get( Settings.INITIAL_PER_MIN_CALC_EXCL_TIME ) );
			setConfig( config );
		}
	}
	
	/**
	 * Returns the size (number of cached replay processors) or the replay processor cache.
	 * 
	 * @return the size (number of cached replay processors) or the replay processor cache
	 */
	public static Integer size() {
		return PM.size();
	}
	
	/**
	 * Adds a change listener which will be called when the replay processor cache changes.
	 * 
	 * @param listener listener to be added
	 * 
	 * @see #removeListener(PropertyChangeListener)
	 */
	public static void addListener( final PropertyChangeListener listener ) {
		PM.addListener( listener );
	}
	
	/**
	 * Removes a change listener.
	 * 
	 * @param listener listener to be removed
	 * 
	 * @see #addListener(PropertyChangeListener)
	 */
	public static void removeListener( final PropertyChangeListener listener ) {
		PM.removeListener( listener );
	}
	
}
