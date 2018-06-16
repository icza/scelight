/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.sc2.rep.factory;

import hu.belicza.andras.mpq.InvalidMpqArchiveException;
import hu.belicza.andras.mpq.MpqContent;
import hu.belicza.andras.mpq.MpqParser;
import hu.scelight.sc2.rep.cache.RepProcCache;
import hu.scelight.sc2.rep.model.Header;
import hu.scelight.sc2.rep.model.Replay;
import hu.scelight.sc2.rep.model.attributesevents.AttributesEvents;
import hu.scelight.sc2.rep.model.details.Details;
import hu.scelight.sc2.rep.model.gameevents.GameEvents;
import hu.scelight.sc2.rep.model.initdata.InitData;
import hu.scelight.sc2.rep.model.messageevents.MessageEvents;
import hu.scelight.sc2.rep.model.trackerevents.TrackerEvents;
import hu.scelight.sc2.rep.repproc.RepProcessor;
import hu.scelight.sc2.rep.s2prot.Protocol;
import hu.scelight.service.env.Env;
import hu.scelight.util.Utils;
import hu.sllauncher.bean.VersionBean;

import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.EnumSet;
import java.util.Set;

/**
 * Replay parser engine.<br>
 * Able to parse StarCraft II replay files and construct {@link Replay} objects.
 * 
 * @author Andras Belicza
 */
public class RepParserEngine {
	
	/** Version of the replay parser engine. */
	public static final VersionBean VERSION = new VersionBean( 1, 3, 0 );
	
	
	
	/** Empty replay content set. */
	public static final Set< RepContent > EMPTY_CONTENT_SET		  = EnumSet.noneOf( RepContent.class );
	
	/** Replay content set containing only the game events. */
	public static final Set< RepContent > GAME_EVENTS_CONTENT_SET = EnumSet.of( RepContent.GAME_EVENTS );
	
	/** Full replay content set. */
	public static final Set< RepContent > FULL_CONTENT_SET		  = EnumSet.of( RepContent.MESSAGE_EVENTS, RepContent.GAME_EVENTS, RepContent.TRACKER_EVENTS );
	
	
	
	/**
	 * Parses the specified replay file and returns a {@link Replay} object.
	 * 
	 * @param file replay file to be parsed
	 * @return the constructed {@link Replay} object or <code>null</code> if the replay cannot be parsed
	 */
	public static Replay parseReplay( final Path file ) {
		return parseReplay( file, FULL_CONTENT_SET );
	}
	
	/**
	 * Parses the specified replay file and returns a {@link Replay} object.
	 * 
	 * @param file replay file to be parsed
	 * @param contentSet content to be parsed; {@link RepContent#DETAILS}, {@link RepContent#INIT_DATA} and {@link RepContent#ATTRIBUTES_EVENTS} are always
	 *            parsed; extra content is to be specified here
	 * @return the constructed {@link Replay} object or <code>null</code> if the replay cannot be parsed
	 * 		
	 * @see #getRepProc(Path)
	 */
	public static Replay parseReplay( final Path file, final Set< RepContent > contentSet ) {
		try ( final MpqParser mpqParser = new MpqParser( file ) ) {
			
			return parseReplay( mpqParser, contentSet );
			
		} catch ( final Exception e ) {
			Env.LOGGER.debug( "Failed to parse replay: " + file, e );
			return null;
		}
	}
	
	
	
	/**
	 * Parses the specified replay file and returns a {@link Replay} object.
	 * 
	 * @param mpqParser MPQ parser providing the replay content
	 * @param contentSet content to be parsed; {@link RepContent#DETAILS}, {@link RepContent#INIT_DATA} and {@link RepContent#ATTRIBUTES_EVENTS} are always
	 *            parsed; extra content is to be specified here
	 * 		   
	 * @return the constructed {@link Replay} object or <code>null</code> if the replay could not be parsed
	 * 		
	 * @throws InvalidMpqArchiveException if error occurs reading files from the MPQ archive
	 */
	private static Replay parseReplay( final MpqParser mpqParser, final Set< RepContent > contentSet ) throws InvalidMpqArchiveException {
		final Replay replay = new Replay();
		
		// Read replay header, this can be read with any protocol
		replay.header = new Header( Protocol.DEFAULT.decodeHeader( mpqParser.getUserData().userData ) );
		
		final Protocol p = Protocol.get( replay.header.getBaseBuild() );
		if ( p == null ) {
			Env.LOGGER.info( "Unsupported Replay version: " + replay.header.versionString() + " (base build: " + replay.header.getBaseBuild() + ")"
			        + ( mpqParser.getFileName() == null ? "!" : ": " + mpqParser.getFileName() ) );
			return null;
		}
		
		// Contents that are always parsed:
		
		byte[] data = mpqParser.getFile( RepContent.DETAILS );
		if ( data == null ) {
			// Try to open anonymized version:
			data = mpqParser.getFile( RepContent.DETAILS_BACKUP );
		}
		replay.details = new Details( p.decodeDetails( data ) );
		
		data =  mpqParser.getFile( RepContent.INIT_DATA );
		if ( data == null ) {
			// Try to open anonymized version:
			data = mpqParser.getFile( RepContent.INIT_DATA_BACKUP );
		}
		replay.initData = new InitData( p.decodeInitData( data ) );
		
		replay.attributesEvents = new AttributesEvents( p.decodeAttributesEvents( mpqParser.getFile( RepContent.ATTRIBUTES_EVENTS ) ) );
		
		// Optionally parsed contents:
		
		if ( contentSet.contains( RepContent.MESSAGE_EVENTS ) )
			replay.messageEvents = new MessageEvents( p.decodeMessageEvents( mpqParser.getFile( RepContent.MESSAGE_EVENTS ), replay ) );
			
		if ( contentSet.contains( RepContent.GAME_EVENTS ) )
			replay.gameEvents = new GameEvents( p.decodeGameEvents( mpqParser.getFile( RepContent.GAME_EVENTS ), replay ) );
			
		if ( contentSet.contains( RepContent.TRACKER_EVENTS ) ) {
			final byte[] trackerData = mpqParser.getFile( RepContent.TRACKER_EVENTS );
			if ( trackerData != null )
				replay.trackerEvents = new TrackerEvents( p.decodeTrackerEvents( trackerData, replay ) );
		}
		
		return replay;
	}
	
	/**
	 * Returns a {@link RepProcessor} constructed and initialized from the {@link RepProcCache} for the specified replay file if it is cached, and if not, it
	 * will be parsed and cached first, then returned.
	 * 
	 * @param file replay file whose {@link RepProcessor} object to return
	 * @return a {@link RepProcessor} preferably constructed and initialized from the {@link RepProcCache} or <code>null</code> if the replay cannot be parsed
	 * 		
	 * @see #parseReplay(Path)
	 */
	public static RepProcessor getRepProc( final Path file ) {
		try ( final MpqParser mpqParser = new MpqParser( file ) ) {
			
			// Replay Key specification:
			// Base64 encoded string of the SHA-1 digest of the "(attributes)" content of the MPQ replay files.
			
			// Reasoning:
			// In case of SC2Replay files "(attributes)" contains CRC32 and MD5 hashes of all other MPQ components
			// ensuring "(attributes)" is different for all individual replays.
			// It is also very small (<300 bytes), it is stored uncompressed in SC2Replay files (mostly)
			// therefore allows fast reading and digest calculation.
			
			final byte[] mpqAttributes = mpqParser.getFile( MpqContent.ATTRIBUTES );
			if ( mpqAttributes == null )
				throw new NullPointerException( "MPQ archive does not contain \"" + MpqContent.ATTRIBUTES.fileName + "\"!" );
				
			// Optimization: 160-bit SHA-1 is 20 bytes, in Base64 it's 20*4/3 = 26.6 chars,
			// so 27 chars is enough, Base64 generates 28 chars => cut off the last char which is always '='
			
			final MessageDigest md = MessageDigest.getInstance( "SHA-1" );
			final String replayKey = Utils.toBase64String( md.digest( mpqAttributes ) ).substring( 0, 27 );
			
			RepProcessor repProc = RepProcCache.getRepProc( replayKey, file );
			if ( repProc == null ) {
				final Replay replay = parseReplay( mpqParser, FULL_CONTENT_SET );
				if ( replay == null )
					return null;
				repProc = new RepProcessor( file, replay );
				RepProcCache.putRepProc( replayKey, repProc, mpqParser );
			}
			
			return repProc;
			
		} catch ( final Exception e ) {
			Env.LOGGER.debug( "Failed to parse replay: " + file, e );
			return null;
		}
	}
	
}
