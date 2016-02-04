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
 * <p>
 * The header of the MPQ archives.
 * </p>
 * 
 * <p>
 * The archive header is the first structure in the archive, at archive offset 0; however, the archive does not need to be at offset 0 of the containing file.
 * The offset of the archive in the file is referred to here as ArchiveOffset. If the archive is not at the beginning of the file, it must begin at a disk
 * sector boundary (512 bytes). Early versions of Storm require that the archive be at the end of the containing file (ArchiveOffset + ArchiveSize = file size),
 * but this is not required in newer versions (due to the strong digital signature not being considered a part of the archive).
 * </p>
 * 
 * <p>
 * Source: <a href='http://wiki.devklog.net/index.php?title=The_MoPaQ_Archive_Format'>The_MoPaQ_Archive_Format</a>
 * </p>
 * 
 * @author Andras Belicza
 */
public class Header {
	
	/**
	 * The magic value of valid MPQ archives. Indicates that the file is a MoPaQ archive. Must be ASCII "MPQ" 0x1A.
	 */
	public static final byte[] MPQ_MAGIC                      = new byte[] { 'M', 'P', 'Q', 0x1A };
	
	/** The original format version. */
	public static final short  FORMAT_VERSION_ORIGINAL        = 0x0000;
	
	/** The Burning Crusade format version. */
	public static final short  FORMAT_VERSION_BURNING_CRUSADE = 0x0001;
	
	/** Size of the archive header. */
	public int                 headerSize;
	
	/**
	 * Size of the whole archive, including the header. Does not include the strong digital signature, if present. This size is used, among other things, for
	 * determining the region to hash in computing the digital signature. This field is deprecated in the Burning Crusade MoPaQ format, and the size of the
	 * archive is calculated as the size from the beginning of the archive to the end of the hash table, block table, or extended block table (whichever is
	 * largest).
	 */
	public int                 archiveSize;
	
	/**
	 * MoPaQ format version. MPQAPI will not open archives where this is negative. Known versions:
	 * <ul>
	 * <li><b>0x0000</b> Original format. HeaderSize should be 20h, and large archives are not supported.
	 * <li><b>0x0001</b> Burning Crusade format. Header size should be 2Ch, and large archives are supported.
	 * </ul>
	 */
	public short               formatVersion;
	
	/**
	 * Power of two exponent specifying the number of 512-byte disk sectors in each logical sector in the archive. The size of each logical sector in the
	 * archive is 512 * 2^SectorSizeShift. Bugs in the Storm library dictate that this should always be 3 (4096 byte sectors).
	 */
	public short               sectorSizeShift;
	
	/** Offset to the beginning of the hash table, relative to the beginning of the archive. */
	public int                 hashTableOffset;
	
	/** Offset to the beginning of the block table, relative to the beginning of the archive. */
	public int                 blockTableOffset;
	
	/**
	 * Number of entries in the hash table. Must be a power of two, and must be less than 2^16 for the original MoPaQ format, or less than 2^20 for the Burning
	 * Crusade format.
	 */
	public int                 hashTableEntries;
	
	/** Number of entries in the block table. */
	public int                 blockTableEntries;
	
	// Fields only present in the Burning Crusade format and later:
	
	/** Offset to the beginning of the extended block table, relative to the beginning of the archive. */
	public long                extendedBlockTableOffset;
	
	/** High 16 bits of the hash table offset for large archives. */
	public short               hashTableOffsetHigh;
	
	/** High 16 bits of the block table offset for large archives. */
	public short               blockTableOffsetHigh;
	
	/**
	 * Checks and returns if the specified magic is a valid MPQ magic.
	 * 
	 * @param magic magic to be checked
	 * @return true if the specified magic is valid; false otherwise
	 */
	public static boolean isMpqMagic( final byte[] magic ) {
		if ( magic.length != MPQ_MAGIC.length )
			return false;
		
		for ( int i = 0; i < MPQ_MAGIC.length; i++ )
			if ( MPQ_MAGIC[ i ] != magic[ i ] )
				return false;
		
		return true;
	}
	
}
