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
 * The Hash table section of the MPQ archives.
 * </p>
 * 
 * <p>
 * Instead of storing file names, for quick access MoPaQs use a fixed, power of two-size hash table of files in the archive. A file is uniquely identified by
 * its file path, its language, and its platform.<br>
 * <br>
 * The home entry for a file in the hash table is computed as a hash of the file path. In the event of a collision (the home entry is occupied by another file),
 * progressive overflow is used, and the file is placed in the next available hash table entry. Searches for a desired file in the hash table proceed from the
 * home entry for the file until either the file is found, the entire hash table is searched, or an empty hash table entry (FileBlockIndex of 0xFFFFFFFF) is
 * encountered.<br>
 * <br>
 * The hash table is always encrypted, using the hash of "(hash table)" as the key.<br>
 * <br>
 * Prior to Starcraft 2, the hash table is stored uncompressed. In Starcraft 2, however, the table may optionally be compressed. If the offset of the block
 * table is not equal to the offset of the hash table plus the uncompressed size, Starcraft 2 interprets the hash table as being compressed (not imploded). This
 * calculation assumes that the block table immediately follows the hash table, and will fail or crash otherwise.<br>
 * <br>
 * Each entry is structured as follows (see below):
 * </p>
 * 
 * <p>
 * Source: <a href='http://wiki.devklog.net/index.php?title=The_MoPaQ_Archive_Format'>The_MoPaQ_Archive_Format</a>
 * </p>
 * 
 * @author Andras Belicza
 */
public class HashTable {
	
	/** Size of the hash table structure in bytes. */
	public static final int   HASH_TABLE_STRUCTURE_SIZE = 16;
	
	// Locale constants for the field <code>language</code>
	
	/** Neutral locale constant. */
	public static final short MPQNeutral                = 0;
	
	/** Chinese locale constant. */
	public static final short MPQChinese                = 0x404;
	
	/** Czech locale constant. */
	public static final short MPQCzech                  = 0x405;
	
	/** German locale constant. */
	public static final short MPQGerman                 = 0x407;
	
	/** English locale constant. */
	public static final short MPQEnglish                = 0x409;
	
	/** Spanish locale constant. */
	public static final short MPQSpanish                = 0x40a;
	
	/** French locale constant. */
	public static final short MPQFrench                 = 0x40c;
	
	/** Italian locale constant. */
	public static final short MPQItalian                = 0x410;
	
	/** Japanese locale constant. */
	public static final short MPQJapanese               = 0x411;
	
	/** Korean locale constant. */
	public static final short MPQKorean                 = 0x412;
	
	/** Dutch locale constant. */
	public static final short MPQDutch                  = 0x413;
	
	/** Polish locale constant. */
	public static final short MPQPolish                 = 0x415;
	
	/** Portuguese locale constant. */
	public static final short MPQPortuguese             = 0x416;
	
	/** Russian locale constant. */
	public static final short MPQRussian                = 0x419;
	
	/** English_UK locale constant. */
	public static final short MPQEnglishUK              = 0x809;
	
	/** Indicates that the hash table entry is empty, and has always been empty. Terminates searches for a given file. */
	public static final int   FILE_BLOCK_INDEX_EMPTY    = 0xFFFF_FFFF;
	
	/**
	 * Indicates taht the hash table entry is empty, but was valid at some point (in other words, the file was deleted). Does not terminate searches for a given
	 * file.
	 */
	public static final int   FILE_BLOCK_INDEX_DELETED  = 0xFFFF_FFFE;
	
	/** The hash of the file path, using method A. */
	public int                filePathHashA;
	
	/** The hash of the file path, using method B. */
	public int                filePathHashB;
	
	/**
	 * The language of the file. This is a Windows LANGID data type, and uses the same values. 0 indicates the default language (American English), or that the
	 * file is language-neutral.
	 */
	public short              language;
	
	/** The platform the file is used for. 0 indicates the default platform. No other values have been observed. */
	public short              platform;
	
	/**
	 * If the hash table entry is valid, this is the index into the block table of the file. Otherwise, one of the following two values:
	 * <ul>
	 * <li><b>0xFFFFFFFF</b> Hash table entry is empty, and has always been empty. Terminates searches for a given file.
	 * <li><b>0xFFFFFFFE</b> Hash table entry is empty, but was valid at some point (in other words, the file was deleted). Does not terminate searches for a
	 * given file.
	 * </ul>
	 */
	public int                fileBlockIndex;
	
}
