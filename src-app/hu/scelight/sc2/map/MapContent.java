/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.sc2.map;

import hu.belicza.andras.mpq.AlgorithmUtil.MpqHashType;
import hu.belicza.andras.mpq.IMpqContent;
import hu.belicza.andras.mpq.MpqContent;

/**
 * MPQ content hashes of SC2 map files.
 * 
 * @author Andras Belicza
 * 
 * @see MpqContent
 */
public enum MapContent implements IMpqContent {
	
	/** <code>"MapInfo"</code> file. */
	MAP_INFO( null, "MapInfo", 456326858, 2000504491, 1514959542 ),
	
	/** <code>"DocumentHeader"</code> file. Stores map attributes. */
	DOCUMENT_HEADER( "Map Attributes", "DocumentHeader", 967573924, 1586069117, 1498525374 ),
	
	/** <code>"Minimap.tga"</code> file. The most common name of the map preview file. */
	MINIMAP_TGA( "Map Image", "Minimap.tga", -1658863222, -1379586317, 1671265210 ),
	
	/** <code>"Objects"</code> file. Stores map objects. */
	OBJECTS( "Map Objects", "Objects", 602486196, -842158972, 1775018687 );
	
	
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
	 * Creates a new {@link MapContent}.
	 * 
	 * @param name optional display name of the MPQ content
	 * @param fileName name of the file inside the MPQ archive.
	 * @param hash1 {@link MpqHashType#TABLE_OFFSET} hash value of the file name
	 * @param hash2 {@link MpqHashType#NAME_A} hash value of the file name
	 * @param hash3 {@link MpqHashType#NAME_B} hash value of the file name
	 */
	private MapContent( final String name, final String fileName, final int hash1, final int hash2, final int hash3 ) {
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
	public static final MapContent[] VALUES = values();
	
}
