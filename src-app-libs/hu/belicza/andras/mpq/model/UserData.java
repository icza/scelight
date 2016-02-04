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
 * The User Data before the header of the MPQ archives.
 * </p>
 * 
 * <p>
 * The second version of the MoPaQ format, first used in Burning Crusade, features a mechanism to store some amount of data outside the archive proper, though
 * the reason for this mechanism is not known. This is implemented by means of a shunt block that precedes the archive itself. The format of this block is as
 * follows (see below):
 * </p>
 * 
 * <p>
 * When Storm encounters this block in its search for the archive header, it saves the location of the shunt block and resumes its search for the archive header
 * at the offset specified in the shunt.<br>
 * <br>
 * Blizzard-generated archives place the shunt at the beginning of the file, and begin the archive itself at the next 512-byte boundary after the end of the
 * shunt block.
 * </p>
 * 
 * <p>
 * Source: <a href='http://wiki.devklog.net/index.php?title=The_MoPaQ_Archive_Format'>The_MoPaQ_Archive_Format</a>
 * </p>
 * 
 * @author Andras Belicza
 */
public class UserData {
	
	/**
	 * The magic value indicating User Data section in valid MPQ archives. Indicates that this is a shunt block. ASCII "MPQ" 1Bh.
	 */
	public static final byte[] MPQ_MAGIC_EXT = new byte[] { 'M', 'P', 'Q', 0x1B };
	
	/**
	 * The number of bytes that have been allocated in this archive for user data. This does not need to be the exact size of the data itself, but merely the
	 * maximum amount of data which may be stored in this archive.
	 */
	public int                 userDataSize;
	
	/** The offset in the file at which to continue the search for the archive header. */
	public int                 archiveHeaderOffset;
	
	/** The block to store user data in. It has <code>userDataSize</code> bytes. */
	public byte[]              userData;
	
	/**
	 * Checks and returns if the specified magic is a valid MPQ_EXT magic.
	 * 
	 * @param magic magic to be checked
	 * @return true if the specified magic is valid; false otherwise
	 */
	public static boolean isMpqMagicExt( final byte[] magic ) {
		if ( magic.length != MPQ_MAGIC_EXT.length )
			return false;
		
		for ( int i = 0; i < MPQ_MAGIC_EXT.length; i++ )
			if ( MPQ_MAGIC_EXT[ i ] != magic[ i ] )
				return false;
		
		return true;
	}
	
}
