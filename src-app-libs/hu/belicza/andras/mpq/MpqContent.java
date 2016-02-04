/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.belicza.andras.mpq;

import hu.belicza.andras.mpq.AlgorithmUtil.MpqHashType;
import hu.sllauncher.util.LUtils;

/**
 * General MPQ content hashes.
 * 
 * @author Andras Belicza
 */
public enum MpqContent implements IMpqContent {
	
	/** <code>"(attributes)"</code> file. */
	ATTRIBUTES( "MPQ Attributes", "(attributes)", 1132703182, -746309685, 132115180 ),
	
	/** <code>"(attributes)"</code> file. */
	LISTFILE( "MPQ Listfile", "(listfile)", 1597892697, -43681520, 1318820007 );
	
	
	
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
	 * Creates a new {@link MpqContent}.
	 * 
	 * @param name optional display name of the MPQ content
	 * @param fileName name of the file inside the MPQ archive.
	 * @param hash1 {@link MpqHashType#TABLE_OFFSET} hash value of the file name
	 * @param hash2 {@link MpqHashType#NAME_A} hash value of the file name
	 * @param hash3 {@link MpqHashType#NAME_B} hash value of the file name
	 */
	private MpqContent( final String name, final String fileName, final int hash1, final int hash2, final int hash3 ) {
		this.fileName = fileName;
		this.hash1 = hash1;
		this.hash2 = hash2;
		this.hash3 = hash3;
		
		stringValue = getDisplayName( name, this );
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
	
	
	/**
	 * Returns a display name for the specified MPQ content.
	 * 
	 * @param <T> type of the MPQ content
	 * @param name optional name to use, if missing, the normalized enum name will be used (normalized by {@link LUtils#constNameToNormal(String, boolean)}
	 * @param mpqContent MPQ content to return a name for
	 * @return a display name for the specified MPQ content
	 */
	public static < T extends Enum< ? > & IMpqContent > String getDisplayName( final String name, final T mpqContent ) {
		return "<html>" + ( name == null ? LUtils.constNameToNormal( mpqContent.name(), true ) : name ) + " (<code>" + mpqContent.getFileName() + "</code>)";
	}
	
	
	/** Cache of the values array. */
	public static final MpqContent[] VALUES = values();
	
}
