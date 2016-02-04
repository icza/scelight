/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.belicza.andras.mpq.model;

/**
 * This class is the representation of an MPQ archive.
 * 
 * <p>
 * The representation might not be complete, I aimed to handle SC2Replay MPQ archives.
 * </p>
 * 
 * @author Andras Belicza
 */
public class MpqArchive {
	
	/** Optional user data. */
	public UserData     userData;
	
	/** The header. */
	public Header       header;
	
	/** The hash tables. */
	public HashTable[]  hashTables;
	
	/** The block tables. */
	public BlockTable[] blockTables;
	
	/**
	 * The upper bits of the archive offsets for each block in the block table. Only present if the archive is > 4GB.
	 */
	public short[]      extBlockTableHighOffsets;
	
	// Derived data
	
	/** Size of the blocks. */
	public int          blockSize;
	
	/** Block table indices of the files. */
	public int[]        blockTableIndices;
	
	/** Number of files in the archive. */
	public int          filesCount;
	
}
