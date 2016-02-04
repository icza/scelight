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
 * The Block table section of the MPQ archives.
 * </p>
 * 
 * <p>
 * The block table contains entries for each region in the archive. Regions may be either files, empty space, which may be overwritten by new files (typically
 * this space is from deleted file data), or unused block table entries. Empty space entries should have BlockOffset and BlockSize nonzero, and FileSize and
 * Flags zero; unused block table entries should have BlockSize, FileSize, and Flags zero. The block table is encrypted, using the hash of "(block table)" as
 * the key. Each entry is structured as follows: (see below)
 * </p>
 * 
 * <p>
 * <b>Extended block table</b><br>
 * The extended block table was added to support archives larger than 4 gigabytes (2^32 bytes). The table contains the upper bits of the archive offsets for
 * each block in the block table. It is simply an array of int16s, which become bits 32-47 of the archive offsets for each block, with bits 48-63 being zero.
 * Individual blocks in the archive are still limited to 4 gigabytes in size. This table is only present in Burning Crusade format archives that exceed 4
 * gigabytes size.<br>
 * <br>
 * Unlike the hash and block tables, the extended block table is not encrypted nor compressed.
 * </p>
 * 
 * <p>
 * Source: <a href='http://wiki.devklog.net/index.php?title=The_MoPaQ_Archive_Format'>The_MoPaQ_Archive_Format</a>
 * </p>
 * 
 * @author Andras Belicza
 */
public class BlockTable {
	
	/** Size of the block table structure in bytes. */
	public static final int BLOCK_TABLE_STRUCTURE_SIZE = 16;
	
	/** Flag indicating that block is a file, and follows the file data format; otherwise, block is free space or unused. */
	public static final int FLAG_FILE                  = 0x8000_0000;
	
	/** Flag indicating that file is stored as a single unit, rather than split into sectors. */
	public static final int FLAG_SINGLE                = 0x0100_0000;
	
	/**
	 * Flag indicating that the file has checksums for each sector (explained in the File Data section). Ignored if file is not compressed or imploded.
	 */
	public static final int FLAG_EXTRA                 = 0x0400_0000;
	
	/** Flag indicating that the file is compressed. */
	public static final int FLAG_COMPRESSED            = 0x0000_FF00;
	
	/** Flag indicating that the file is compressed with pkware algorithm. */
	public static final int FLAG_COMPRESSED_PKWARE     = 0x0000_0100;
	
	/** Flag indicating that the file is under multiple compression. */
	public static final int FLAG_COMPRESSED_MULTI      = 0x0000_0200;
	
	/** Flag indicating that the file is encrypted. */
	public static final int FLAG_ENCRYPTED             = 0x0001_0000;
	
	/** Offset of the beginning of the block, relative to the beginning of the archive. */
	public int              blockOffset;
	
	/** Size of the block in the archive. Also referred to as <code>packedSize</code>. */
	public int              blockSize;
	
	/**
	 * Size of the file data stored in the block. Only valid if the block is a file; otherwise meaningless, and should be 0. If the file is compressed, this is
	 * the size of the uncompressed file data. Also referred to as <code>unpackedSize</code>.
	 */
	public int              fileSize;
	
	/**
	 * Bit mask of the flags for the block. The following values are conclusively identified:
	 * <ul>
	 * <li><b>0x80000000</b> Block is a file, and follows the file data format; otherwise, block is free space or unused. If the block is not a file, all other
	 * flags should be cleared, and FileSize should be 0.
	 * <li><b>0x04000000</b> File has checksums for each sector (explained in the File Data section). Ignored if file is not compressed or imploded.
	 * <li><b>0x02000000</b> File is a deletion marker, indicating that the file no longer exists. This is used to allow patch archives to delete files present
	 * in lower-priority archives in the search chain.
	 * <li><b>0x01000000</b> File is stored as a single unit, rather than split into sectors.
	 * <li><b>0x00020000</b> The file's encryption key is adjusted by the block offset and file size (explained in detail in the File Data section). File must
	 * be encrypted.
	 * <li><b>0x00010000</b> File is encrypted.
	 * <li><b>0x00000200</b> File is compressed. File cannot be imploded.
	 * <li><b>0x00000100</b> File is imploded. File cannot be compressed.
	 * </ul>
	 */
	public int              flags;
	
}
