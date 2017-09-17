/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.sc2.rep.s2prot;

import hu.scelight.sc2.rep.model.Replay;
import hu.scelight.sc2.rep.model.attributesevents.AttributesEvents;
import hu.scelight.sc2.rep.model.gameevents.GameEventFactory;
import hu.scelight.sc2.rep.model.messageevents.MessageEventFactory;
import hu.scelight.sc2.rep.model.trackerevents.TrackerEventFactory;
import hu.scelight.sc2.rep.s2prot.type.Attribute;
import hu.scelight.sc2.rep.s2prot.type.TypeInfo;
import hu.scelight.service.env.Env;
import hu.scelight.util.Utils;
import hu.sllauncher.util.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Set;

/**
 * Protocol handler.
 * 
 * @author Andras Belicza
 */
public class Protocol {
	
	/** Set of base builds that belong to beta/PTR releases. */
	public static final Set< Integer >			  BETA_BASE_BUILD_SET = Utils.asNewSet( 17266, 18468, 19458, 19595, 21955, 24764, 27950, 28272, 34784, 34835,
	        36442, 38535, 38624, 39117, 39948, 40384, 40977, 41128, 41219, 41973, 44169, 44293, 44743, 44765, 45186, 45364, 45386, 45542, 45556, 45593, 45737,
	        45944, 47932, 49957 );
			
	/** Cache of the already instantiated protocol handlers (they are reusable). */
	private static final Map< Integer, Protocol > BBUILD_PROTOCOL_MAP = new HashMap< >();
	
	/**
	 * Returns a protocol handler for the specified base build.
	 * 
	 * @param baseBuild base build to return a protocol handler for
	 * @return a suitable protocol handler for the specified base build; or <code>null</code> if the specified base build is not supported
	 */
	public static Protocol get( final Integer baseBuild ) {
		Protocol p = BBUILD_PROTOCOL_MAP.get( baseBuild );
		
		if ( p == null && !BBUILD_PROTOCOL_MAP.containsKey( baseBuild ) ) {
			try {
				p = new Protocol( baseBuild );
			} catch ( final Exception e ) {
				// Catch other exceptions not just IOException, there are potential RuntimeExceptions like NumberFormatException
				if ( !( e instanceof MissingResourceException ) )
					Env.LOGGER.error( "Failed to load static protocol data for base build: " + baseBuild, e );
			}
			
			// Store protocol even if it's null (so next time we don't have to come to this result again)
			BBUILD_PROTOCOL_MAP.put( baseBuild, p );
		}
		
		return p;
	}
	
	/**
	 * The default protocol handler.<br>
	 * This protocol handler is used to decode the replay header. Recommended to use the protocol handler for the latest supported base build because it's the
	 * most likely needed).
	 */
	public static final Protocol DEFAULT = get( 57507 );
	
	
	
	/** Base build this protocol handler implements. */
	public final int	 baseBuild;
	
	/** Tells if the base build this protocol handler handles belongs to a beta or PTR (Public Test Release) release. */
	public final boolean betaOrPtr;
	
	
	/**
	 * Type info array.<br>
	 * Decoding instructions for each protocol type.
	 */
	private final TypeInfo[]							  typeInfos;
	
	/**
	 * Map describing the game event types.<br>
	 * Key is the game event id, value is a pair of typeid and type name.<br>
	 * Map from protocol NNet.Game.*Event eventid to (typeid, name).
	 */
	private final Map< Integer, Pair< Integer, String > > gameEventTypeMap	  = new HashMap< >();
	
	/**
	 * Typeid of the events stored in the game events stream.<br>
	 * The typeid of the NNet.Game.EEventId enum.
	 */
	private final int									  gameEventIdTypeid;
	
	/**
	 * Map describing the message event types.<br>
	 * Key is the message event id, value is a pair of typeid and type name.<br>
	 * Map from protocol NNet.Game.*Message eventid to (typeid, name).
	 */
	private final Map< Integer, Pair< Integer, String > > messageEventTypeMap = new HashMap< >();
	
	/**
	 * Typeid of the events stored in the message events stream.<br>
	 * The typeid of the NNet.Game.EMessageId enum.
	 */
	private final int									  messageEventIdTypeid;
	
	/** Tells if the protocol has tracker events. */
	private final boolean								  hasTrackerEvents;
	
	/**
	 * Map describing the tracker event types.<br>
	 * Key is the message event id, value is a pair of typeid and type name.<br>
	 * Map from protocol NNet.Replay.Tracker.*Event eventid to (typeid, name).
	 */
	private final Map< Integer, Pair< Integer, String > > trackerEventTypeMap = new HashMap< >();
	
	/**
	 * Typeid of the events stored in the tracker events stream.<br>
	 * The typeid of the NNet.Replay.Tracker.EEventId enum.
	 */
	private final int									  trackerEventIdTypeid;
	
	
	/** The typeid of NNet.SVarUint32 (the type used to encode gameloop deltas). */
	private final int svaruint32Typeid;
	
	/** The typeid of NNet.Replay.SGameUserId (the type used to encode user ids). */
	private final int replayUseridTypeid;
	
	/**
	 * Typeid of the replay header data.<br>
	 * The typeid of NNet.Replay.SHeader (the type used to store replay game version and length).
	 */
	private final int replayHeaderTypeid;
	
	/**
	 * Typeid of the replay details data.<br>
	 * The typeid of NNet.Game.SDetails (the type used to store overall replay details).
	 */
	private final int gameDetailsTypeid;
	
	/**
	 * Typeid of the replay init data data.<br>
	 * The typeid of NNet.Replay.SInitData (the type used to store the inital lobby).
	 */
	private final int replayInitDataTypeid;
	
	
	
	/**
	 * Creates a new {@link Protocol}.
	 * <p>
	 * Loads static protocol data and initializes the protocol from a file <code>"build/static_BASEBUILD.dat"</code>.
	 * </p>
	 * 
	 * @param baseBuild base build this protocol handler implements
	 * @throws MissingResourceException if there is no static protocol data for the specified base build
	 * @throws IOException if an error occurs reading static protocol data
	 */
	private Protocol( final int baseBuild ) throws MissingResourceException, IOException {
		this.baseBuild = baseBuild;
		
		betaOrPtr = BETA_BASE_BUILD_SET.contains( baseBuild );
		
		final URL resource = Protocol.class.getResource( "build/" + baseBuild + ".dat" );
		if ( resource == null )
			throw new MissingResourceException( "Missing static protocol data for base build: " + baseBuild, null, null );
			
		// Load static protocol data
		try ( final BufferedReader in = new BufferedReader( new InputStreamReader( resource.openStream() ) ) ) {
			String line;
			
			// Type infos
			final List< TypeInfo > list = new ArrayList< >( 174 );
			while ( !( line = in.readLine() ).isEmpty() )
				list.add( TypeInfo.fromString( line ) );
			typeInfos = list.toArray( new TypeInfo[ list.size() ] );
			
			// Game event types
			readEventTypeMap( in, gameEventTypeMap, "NNet.Game.S", "Event" );
			gameEventIdTypeid = readTypeid( in );
			
			// Message event types
			readEventTypeMap( in, messageEventTypeMap, "NNet.Game.S", "Message" );
			messageEventIdTypeid = readTypeid( in );
			
			// Tracker event types
			hasTrackerEvents = baseBuild >= 24944;
			if ( hasTrackerEvents ) {
				readEventTypeMap( in, trackerEventTypeMap, "NNet.Replay.Tracker.S", "Event" );
				trackerEventIdTypeid = readTypeid( in );
			} else
				trackerEventIdTypeid = -1;
				
			svaruint32Typeid = readTypeid( in );
			replayUseridTypeid = readTypeid( in );
			replayHeaderTypeid = readTypeid( in );
			gameDetailsTypeid = readTypeid( in );
			replayInitDataTypeid = readTypeid( in );
		}
	}
	
	/**
	 * Reads an event type map from the specified input, and puts entries to the speicified even type map.
	 * 
	 * @param in input to read entries from
	 * @param eventTypeMap event type map to put the entries into
	 * @param prefixToStrip event name prefix to be stripped off
	 * @param postfixToStrip event name postfix to be stripped off
	 * @throws IOException if an error occurs reading/parsing map entries
	 */
	private static void readEventTypeMap( final BufferedReader in, final Map< Integer, Pair< Integer, String > > eventTypeMap, final String prefixToStrip,
	        final String postfixToStrip ) throws IOException {
		String line;
		while ( !( line = in.readLine() ).isEmpty() ) {
			// Example:
			// 5: (70, 'NNet.Game.SUserFinishedLoadingSyncEvent'),
			final int i = line.indexOf( ':' );
			final int j = line.indexOf( '(', i + 1 );
			final int k = line.indexOf( ',', j + 1 );
			final int l = line.indexOf( '\'', k + 1 );
			final int m = line.indexOf( '\'', l + 1 );
			
			// String.intern() the names because the same names are used in all protocol data files!
			// Getting them using intern()-ed names will be much faster! (pre-calculated hash, reference equality etc.)
			final String name = line.substring( l + 1 + prefixToStrip.length(), m - postfixToStrip.length() ).intern();
			
			eventTypeMap.put( Integer.parseInt( line.substring( 0, i ) ), new Pair< >( Integer.parseInt( line.substring( j + 1, k ) ), name ) );
		}
	}
	
	/**
	 * Reads a typeid from the specified input.
	 * 
	 * @param in input to read from
	 * @return the read typeid
	 * @throws IOException if an error occurs reading/parsing the typeid
	 */
	private static int readTypeid( final BufferedReader in ) throws IOException {
		// Example:
		// game_eventid_typeid = 0
		final String line = in.readLine();
		return Integer.parseInt( line.substring( line.lastIndexOf( ' ' ) + 1 ) );
	}
	
	
	
	/**
	 * Decodes the replay header data.
	 * 
	 * @param data raw byte data to decode
	 * @return the decoded replay header structure
	 */
	@SuppressWarnings( "unchecked" )
	public Map< String, Object > decodeHeader( final byte[] data ) {
		final VersionedDecoder decoder = new VersionedDecoder( data, typeInfos );
		
		decoder.skipBytes( 4 ); // 3c 00 00 00 (might be part of the MPQ header and not the user data)
		
		return (Map< String, Object >) decoder.instance( replayHeaderTypeid );
	}
	
	/**
	 * Decodes the replay details data.
	 * 
	 * @param data raw byte data to decode
	 * @return the decoded replay details structure
	 */
	@SuppressWarnings( "unchecked" )
	public Map< String, Object > decodeDetails( final byte[] data ) {
		final VersionedDecoder decoder = new VersionedDecoder( data, typeInfos );
		return (Map< String, Object >) decoder.instance( gameDetailsTypeid );
	}
	
	/**
	 * Decodes the replay init data data.
	 * 
	 * @param data raw byte data to decode
	 * @return the decoded replay init data structure
	 */
	@SuppressWarnings( "unchecked" )
	public Map< String, Object > decodeInitData( final byte[] data ) {
		final BitPackedDecoder decoder = new BitPackedDecoder( data, typeInfos );
		return (Map< String, Object >) decoder.instance( replayInitDataTypeid );
	}
	
	/**
	 * Decodes the replay attributes events.
	 * 
	 * @param data raw byte data to decode
	 * @return the decoded replay attributes events data structure
	 */
	public Map< String, Object > decodeAttributesEvents( final byte[] data ) {
		// s2protocol reference implementation uses a BitPackedBuffer here, but I made it abstract,
		// so I go with the BitPackedDecoder (but advanced data structures are not read).
		final BitPackedDecoder decoder = new BitPackedDecoder( data, typeInfos, false );
		
		final Map< String, Object > attrs = new HashMap< >();
		
		if ( !decoder.hasRemaining() )
			return attrs;
			
		// Source is only present from 1.2 and onward (beta base build 17266, retail base build 17326)
		if ( baseBuild >= 17266 )
			attrs.put( AttributesEvents.F_SOURCE, decoder.read_bits_int( 8 ) );
		attrs.put( AttributesEvents.F_MAP_NAMESPACE, decoder.read_bits_int( 32 ) );
		
		decoder.read_bits_int( 32 ); // attributes count
		
		// attributes.events format allows defining multiple values in the same scope for the same attribute id,
		// s2protocol library suggests to store these values in a list,
		// but in practice there is always only 1 value, so I don't create lists in waste...
		// It would mean to define scopes like this:
		// final Map< Integer, Map< Integer, List< Attribute > > > scopes = new HashMap<>();
		
		final Map< Integer, Map< Integer, Attribute > > scopes = new HashMap< >();
		attrs.put( AttributesEvents.F_SCOPES, scopes );
		
		final byte[] valueBuff = new byte[ 4 ];
		while ( decoder.hasRemaining() ) {
			final Attribute attr = new Attribute();
			attr.namespace = decoder.read_bits_int( 32 );
			attr.id = decoder.read_bits_int( 32 );
			attr.scope = decoder.read_bits_int( 8 );
			
			// Should be:
			// attr.value = decoder.read_aligned_bytes( 4 ); and reverse() and strip_zero_chars()
			// But for performance: read attribute value reversed and find last null_char in one step!
			decoder.byte_align();
			int lastZeroIdx = -1; // Zero chars are at the beginning (in the reversed version) so we need the last zero char
			for ( int i = 3; i >= 0; i-- ) {
				if ( ( valueBuff[ i ] = (byte) decoder.read_bits_int( 8 ) ) == '\0' )
					if ( lastZeroIdx < 0 )
						lastZeroIdx = i;
			}
			// Cut of trailing zero characters by using only the remaining to construct the value string
			attr.value = lastZeroIdx < 0 ? new String( valueBuff, 0, 4, Env.UTF8 ) : new String( valueBuff, lastZeroIdx + 1, 3 - lastZeroIdx, Env.UTF8 );
			
			Map< Integer, Attribute > scope = scopes.get( attr.scope );
			if ( scope == null )
				scopes.put( attr.scope, scope = new HashMap< >() );
			scope.put( attr.id, attr );
		}
		
		return attrs;
	}
	
	
	// ***************************************************************************************************
	// ************************************ DECODING OF EVENT STREAMS ************************************
	// ***************************************************************************************************
	
	/** Message event factory. */
	private static final EventFactory MESSAGE_EF = new MessageEventFactory();
	
	
	/**
	 * Decodes and returns the events from the event stream handled by the specified decoder.
	 * 
	 * @param decoder decoder to use to read the events
	 * @param eventIdTypeid type of the id of the events to read and decode
	 * @param eventTypeMap event type map
	 * @param decodeUserId tells if the events contain user id which is to be read and decoded
	 * @param ef event factory
	 * @param playerIdUserIdMap mapping to convert player ids to user ids
	 * 		   
	 * @return the list of decoded events
	 */
	@SuppressWarnings( "unchecked" )
	private List< Event > decodeEventStream( final BitPackedBuffer decoder, final int eventIdTypeid, final Map< Integer, Pair< Integer, String > > eventTypeMap,
	        final boolean decodeUserId, final EventFactory ef, final int[] playerIdUserIdMap ) {
		// Local cache of attributes used frequently
		final int svaruint32Typeid = this.svaruint32Typeid;
		final int replayUseridTypeid = this.replayUseridTypeid;
		
		// From beta base build 24764 (retail 24944) user id is stored instead of player id
		final String userIdProp = baseBuild >= 24764 ? Event.F_USER_ID : Event.F_PLAYER_ID;
		
		final int extraEntriesCount = decodeUserId ? 4 : 3;
		
		final List< Event > eventList = new ArrayList< >();
		
		int loop = 0;
		
		int userId = decodeUserId ? 0 : -1;
		while ( decoder.hasRemaining() ) {
			// svaruint32Typeid denotes a _choice type which returns a Pair< String, Object >.
			final Pair< String, Integer > delta = (Pair< String, Integer >) decoder.instance( svaruint32Typeid );
			loop += delta.value2; // Numeric value of delta
			
			if ( decodeUserId )
				userId = ( (Map< String, Integer >) decoder.instance( replayUseridTypeid ) ).get( userIdProp );
				
			final int eventId = (Integer) decoder.instance( eventIdTypeid );
			
			final Pair< Integer, String > eventType = eventTypeMap.get( eventId );
			
			final Map< String, Object > eventStruct = (Map< String, Object >) decoder.instance( eventType.value1, extraEntriesCount );
			
			eventStruct.put( Event.F_ID, eventId );
			eventStruct.put( Event.F_NAME, eventType.value2 );
			eventStruct.put( Event.F_LOOP, loop );
			if ( decodeUserId ) {
				// Store the original player id instead of the user id (if player id is available in the rep)
				eventStruct.put( userIdProp, userId );
				if ( baseBuild < 24764 )
					userId = playerIdUserIdMap[ userId ];
			}
			
			eventList.add( ef.create( eventStruct, eventId, eventType.value2, loop, userId ) );
			
			decoder.byte_align();
		}
		
		return eventList;
	}
	
	/**
	 * Decodes the replay message events.
	 * 
	 * @param data raw byte data to decode
	 * @param replay reference to the {@link Replay} being parsed, source for optionally required more information
	 * @return the list of decoded message events
	 */
	public List< Event > decodeMessageEvents( final byte[] data, final Replay replay ) {
		final BitPackedDecoder decoder = new BitPackedDecoder( data, typeInfos, true );
		
		return decodeEventStream( decoder, messageEventIdTypeid, messageEventTypeMap, true, MESSAGE_EF, replay.getPlayerIdUserIdMap() );
	}
	
	/**
	 * Decodes the replay game events.
	 * 
	 * @param data raw byte data to decode
	 * @param replay reference to the {@link Replay} being parsed, source for optionally required more information
	 * @return the list of decoded game events
	 */
	public List< Event > decodeGameEvents( final byte[] data, final Replay replay ) {
		final BitPackedDecoder decoder = new BitPackedDecoder( data, typeInfos, true );
		
		return decodeEventStream( decoder, gameEventIdTypeid, gameEventTypeMap, true, new GameEventFactory( replay ), replay.getPlayerIdUserIdMap() );
	}
	
	/**
	 * Decodes the replay tracker events.
	 * 
	 * @param data raw byte data to decode
	 * @param replay reference to the {@link Replay} being parsed, source for optionally required more information
	 * @return the list of decoded game events
	 */
	public List< Event > decodeTrackerEvents( final byte[] data, final Replay replay ) {
		final VersionedDecoder decoder = new VersionedDecoder( data, typeInfos, true );
		
		return decodeEventStream( decoder, trackerEventIdTypeid, trackerEventTypeMap, false, new TrackerEventFactory( replay ), replay.getPlayerIdUserIdMap() );
	}
	
}
