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

import hu.belicza.andras.mpq.AlgorithmUtil.MpqHashType;
import hu.belicza.andras.mpq.IMpqContent;
import hu.belicza.andras.mpq.MpqContent;

/**
 * MPQ content hashes of SC2 replay files.
 * 
 * @author Andras Belicza
 * 
 * @see MpqContent
 */
public enum RepContent implements IMpqContent {
	
	// Processed ones...
	
	/** <code>"replay.details"</code> file. */
	DETAILS( null, "replay.details", 620083690, -746339684, -281006446 ),
	
	/** <code>"replay.initData"</code> file. */
	INIT_DATA( null, "replay.initData", -750801643, 1518242780, -14336164 ),
	
	/** <code>"replay.attributes.events"</code> file. */
	ATTRIBUTES_EVENTS( "Attributes", "replay.attributes.events", 1306016990, 497594575, -1563492568 ),
	
	/** <code>"replay.message.events"</code> file. */
	MESSAGE_EVENTS( "Messages", "replay.message.events", 1089231967, 831857289, 1784674979 ),
	
	/** <code>"replay.game.events"</code> file. */
	GAME_EVENTS( null, "replay.game.events", 496563520, -1430084277, -193582187 ),
	
	/** <code>"replay.tracker.events"</code> file. */
	TRACKER_EVENTS( null, "replay.tracker.events", 1501940595, -31863906, 1648390237 ),
	
	// Non-processed ones...
	
	/** <code>"replay.sync.events"</code> file. */
	SYNC_EVENTS( null, "replay.sync.events", 1206425669, -907692176, 991491766 ),
	
	/** <code>"replay.smartcam.events"</code> file. */
	SMARTCAM_EVENTS( null, "replay.smartcam.events", 1029411303, 992681632, -1221660585 ),
	
	/** <code>"replay.load.info"</code> file. */
	LOAD_INFO( null, "replay.load.info", -1560341370, 876349563, 663631490 ),
	
	/** <code>"replay.resumable.events"</code> file. */
	RESUMABLE_EVENTS( null, "replay.resumable.events", 627749409, -1040694775, 1096289738 ),
	
	/** <code>"replay.server.battlelobby"</code> file. */
	SERVER_BATTLELOBBY( null, "replay.server.battlelobby", -35545810, 1984207325, 1796956592 );
	
	
	
	/** String value of the MPQ content. */
	public final String stringValue;
	
	/** Name of the file inside the MPQ archive. */
	public final String fileName;
	
	/** {@link MpqHashType#TABLE_OFFSET} hash value of the file name. */
	public final int    hash1;
	
	/** {@link MpqHashType#NAME_A} hash value of the file name. */
	public final int    hash2;
	
	/** {@link MpqHashType#NAME_B} hash value of the file name. */
	public final int    hash3;
	
	
	/**
	 * Creates a new {@link RepContent}.
	 * 
	 * @param name optional display name of the MPQ content
	 * @param fileName name of the file inside the MPQ archive.
	 * @param hash1 {@link MpqHashType#TABLE_OFFSET} hash value of the file name
	 * @param hash2 {@link MpqHashType#NAME_A} hash value of the file name
	 * @param hash3 {@link MpqHashType#NAME_B} hash value of the file name
	 */
	private RepContent( final String name, final String fileName, final int hash1, final int hash2, final int hash3 ) {
		this.fileName = fileName;
		this.hash1 = hash1;
		this.hash2 = hash2;
		this.hash3 = hash3;
		
		stringValue = MpqContent.getDisplayName( name, this );
	}
	
	@Override
	public String getFileName() {
		return fileName;
	}
	
	@Override
	public int getHash1() {
		return hash1;
	}
	
	@Override
	public int getHash2() {
		return hash2;
	}
	
	@Override
	public int getHash3() {
		return hash3;
	}
	
	@Override
	public String toString() {
		return stringValue;
	}
	
	
	/** Cache of the values array. */
	public static final RepContent[] VALUES = values();
	
}
