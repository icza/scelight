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
import hu.belicza.andras.mpq.model.BlockTable;
import hu.belicza.andras.mpq.model.HashTable;
import hu.belicza.andras.mpq.model.Header;
import hu.belicza.andras.mpq.model.MpqArchive;
import hu.belicza.andras.mpq.model.UserData;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.file.Path;

/**
 * Parser of Blizzard's MPQ archive file format.
 * 
 * <p>
 * Sources:
 * <ul>
 * <li><a href='http://wiki.devklog.net/index.php?title=The_MoPaQ_Archive_Format'>The_MoPaQ_Archive_Format</a>
 * <li><a href='http://en.wikipedia.org/wiki/MPQ'>MPQ on wikipedia</a>
 * <li><a href='http://www.zezula.net/mpq.html'>Zezula MPQ description</a>
 * <li><a href='https://libmpq.org/'>Libmpq project</a>
 * </ul>
 * </p>
 * 
 * <p>
 * Format of the <code>"(attributes)"</code> meta attributes file: (<a
 * href="https://github.com/stormlib/StormLib/blob/3a926f0228c68d7d91cf3946624d7859976440ec/src/SFileAttributes.cpp">src</a>)
 * </p>
 * <ul>
 * <li><code>int version:</code> Version of the (attributes) file. Must be 100 (0x64)
 * <li><code>int flags:</code> flags telling what is contained in the <code>"(attributes)"</code>
 * <ul>
 * <li><code>MPQ_ATTRIBUTE_CRC32         0x00000001  // The "(attributes)" contains CRC32 for each file</code>
 * <li><code>MPQ_ATTRIBUTE_FILETIME      0x00000002  // The "(attributes)" contains file time for each file</code>
 * <li><code>MPQ_ATTRIBUTE_MD5           0x00000004  // The "(attributes)" contains MD5 for each file</code>
 * <li><code>MPQ_ATTRIBUTE_PATCH_BIT     0x00000008  // The "(attributes)" contains a patch bit for each file</code>
 * <li><code>MPQ_ATTRIBUTE_ALL           0x0000000F  // Summary mask</code>
 * </ul>
 * <li>If has CRC32: int * BlockTableSize
 * <li>If has FILETIME: long * BlockTableSize
 * <li>If has MD5: MD5SIZE * BlockTableSize
 * <li>If has PATCH_BIT: enough bytes to hold BlockTableSize bits
 * </ul>
 * 
 * @author Andras Belicza
 */
public class MpqParser implements AutoCloseable {
	
	/**
	 * Decryption key of the hash table. This is the value of<br>
	 * <code>AlgorithmUtil.hashString( "(hash table)", {@link MpqHashType#FILE_KEY} )</code>
	 */
	private static final int   HASH_TABLE_DECRYPTION_KEY  = -1011927184;
	
	/**
	 * Decryption key of the block table. This is the value of<br>
	 * <code>AlgorithmUtil.hashString( "(block table)", {@link MpqHashType#FILE_KEY} );</code>
	 */
	private static final int   BLOCK_TABLE_DECRYPTION_KEY = -326913117;
	
	
	/** The descriptor of the MPQ archive. */
	private final MpqArchive   mpqArchive                 = new MpqArchive();
	
	/** Data provider to read MPQ content from. */
	private final MpqDataInput mpqDataInput;
	
	
	/**
	 * Creates a new MpqParser, opens the specified MPQ file and reads the MPQ headers: user data, mpq header, hash tables, block tables.
	 * 
	 * <p>
	 * If no exception is thrown, the parser will keep the file of the MPQ archive open. A call to the method <code>close()</code> is required to close the
	 * file.
	 * </p>
	 * 
	 * @param file MPQ file
	 * @throws InvalidMpqArchiveException if the specified file is not a valid MPQ archive
	 */
	public MpqParser( final Path file ) throws InvalidMpqArchiveException {
		this( new MpqDataInput() {
			/** Input stream of the MPQ archive. */
			private final FileInputStream input;
			
			/** Channel of the file input stream. */
			private final FileChannel     channel;
			
			{
				// "Constructor"
				try {
					input = new FileInputStream( file.toFile() );
					channel = input.getChannel();
				} catch ( final Exception e ) {
					// An exception here means input was not created => no need to call MpqParser.this.close()
					throw new InvalidMpqArchiveException( e.getMessage(), e );
				}
			}
			
			@Override
			public long position() throws IOException {
				return channel.position();
			}
			
			@Override
			public void position( final long newPosition ) throws IOException {
				channel.position( newPosition );
			}
			
			@Override
			public long read( final ByteBuffer destination ) throws IOException {
				return channel.read( destination );
			}
			
			@Override
			public void close() throws IOException {
				input.close();
			}
			
			@Override
			public Path getFile() {
				return file;
			}
		} );
	}
	
	/**
	 * Creates a new MpqParser, and reads the MPQ headers: user data, mpq header, hash tables, block tables.
	 * 
	 * <p>
	 * If no exception is thrown, the parser will keep the data input open. A call to the method <code>close()</code> is required to close the data input.
	 * </p>
	 * 
	 * @param mpqDataInput data input to read the content of the MPQ from
	 * @throws InvalidMpqArchiveException if the specified input does not produces a valid MPQ archive
	 */
	public MpqParser( final MpqDataInput mpqDataInput ) throws InvalidMpqArchiveException {
		try {
			this.mpqDataInput = mpqDataInput;
			
			final byte[] magic = new byte[ 4 ];
			mpqDataInput.read( ByteBuffer.wrap( magic ) ); // If there isn't enough input, the check will fail
			
			// Optionally the MPQ starts with a User Data section
			if ( UserData.isMpqMagicExt( magic ) ) {
				mpqArchive.userData = readUserData();
				
				// 12 bytes: user data fields
				mpqDataInput.position( mpqDataInput.position() + mpqArchive.userData.archiveHeaderOffset - ( mpqArchive.userData.userDataSize + 12 ) );
				
				mpqDataInput.read( ByteBuffer.wrap( magic ) ); // If there isn't enough input, the check will fail
			}
			
			if ( !Header.isMpqMagic( magic ) )
				throw new InvalidMpqArchiveException( "Invalid MPQ archive" + ( mpqDataInput.getFile() == null ? "!" : ": " + mpqDataInput.getFile() ) );
			
			mpqArchive.header = readHeader();
			
			mpqArchive.blockSize = 512 << mpqArchive.header.sectorSizeShift;
			
			// Read hash tables
			mpqDataInput.position( ( (long) mpqArchive.header.hashTableOffsetHigh << 32 ) + ( mpqArchive.header.hashTableOffset & 0xFFFF_FFFFL )
			        + ( mpqArchive.userData == null ? 0 : mpqArchive.userData.archiveHeaderOffset ) );
			mpqArchive.hashTables = readHashTableEntries( mpqArchive.header.hashTableEntries );
			
			// Read block tables
			mpqDataInput.position( ( (long) mpqArchive.header.blockTableOffsetHigh << 32 ) + ( mpqArchive.header.blockTableOffset & 0xFFFF_FFFFL )
			        + ( mpqArchive.userData == null ? 0 : mpqArchive.userData.archiveHeaderOffset ) );
			mpqArchive.blockTables = readBlockTableEntries( mpqArchive.header.blockTableEntries );
			
			// Regardless of the version the extended block is only present in archives > 4 GB
			if ( mpqArchive.header.extendedBlockTableOffset > 0 ) {
				// We will probably not ever end up here in case of SC2Replay files.
				mpqDataInput.position( mpqArchive.header.extendedBlockTableOffset
				        + ( mpqArchive.userData == null ? 0 : mpqArchive.userData.archiveHeaderOffset ) );
				mpqArchive.extBlockTableHighOffsets = readExtBlockTableEntries( mpqArchive.header.blockTableEntries );
			}
			
			// Count valid files in the archive
			mpqArchive.blockTableIndices = new int[ mpqArchive.header.blockTableEntries ];
			for ( int i = 0; i < mpqArchive.header.blockTableEntries; i++ )
				if ( ( mpqArchive.blockTables[ i ].flags & BlockTable.FLAG_FILE ) != 0 )
					mpqArchive.blockTableIndices[ mpqArchive.filesCount++ ] = i;
			
		} catch ( final Exception e ) {
			close(); // We have to call close() here because the caller won't have reference to this object to call close()
			throw new InvalidMpqArchiveException( e.getMessage(), e );
		}
	}
	
	/**
	 * Reads the User data section from the input.
	 * 
	 * @return the read user data
	 * @throws IOException if IOException is thrown during reading from the stream
	 */
	private UserData readUserData() throws IOException {
		final UserData userData = new UserData();
		
		userData.userDataSize = readFullBuffer( 4 ).getInt();
		userData.archiveHeaderOffset = readFullBuffer( 4 ).getInt();
		
		userData.userData = readFullBuffer( userData.userDataSize ).array();
		
		return userData;
	}
	
	/**
	 * Reads an MPQ archive header from the specified input.
	 * 
	 * @return the read header
	 * @throws IOException if IOException is thrown during reading from the stream
	 */
	private Header readHeader() throws IOException {
		final Header header = new Header();
		
		header.headerSize = readFullBuffer( 4 ).getInt();
		
		final ByteBuffer headerBuffer = readFullBuffer( header.headerSize - 8 ); // 8 bytes: Magic and header size are already
		                                                                         // read
		
		header.archiveSize = headerBuffer.getInt();
		header.formatVersion = headerBuffer.getShort();
		header.sectorSizeShift = headerBuffer.getShort();
		header.hashTableOffset = headerBuffer.getInt();
		header.blockTableOffset = headerBuffer.getInt();
		header.hashTableEntries = headerBuffer.getInt();
		header.blockTableEntries = headerBuffer.getInt();
		
		if ( header.formatVersion == Header.FORMAT_VERSION_BURNING_CRUSADE ) {
			header.extendedBlockTableOffset = headerBuffer.getLong();
			header.hashTableOffsetHigh = headerBuffer.getShort();
			header.blockTableOffsetHigh = headerBuffer.getShort();
		}
		
		return header;
	}
	
	/**
	 * Reads the hash table entries from the input.
	 * 
	 * @param hashTableEntires number of hash table entries
	 * @return the read hash table entries
	 * @throws IOException if IOException is thrown during reading from the stream
	 */
	private HashTable[] readHashTableEntries( final int hashTableEntires ) throws IOException {
		final int size = hashTableEntires * HashTable.HASH_TABLE_STRUCTURE_SIZE;
		final ByteBuffer hashTablesBuffer = AlgorithmUtil.decryptData( readFullBuffer( size ), size, HASH_TABLE_DECRYPTION_KEY );
		
		final HashTable[] hashTables = new HashTable[ hashTableEntires ];
		
		for ( int i = 0; i < hashTables.length; i++ ) {
			final HashTable hashTable = new HashTable();
			
			hashTable.filePathHashA = hashTablesBuffer.getInt();
			hashTable.filePathHashB = hashTablesBuffer.getInt();
			hashTable.language = hashTablesBuffer.getShort();
			hashTable.platform = hashTablesBuffer.getShort();
			hashTable.fileBlockIndex = hashTablesBuffer.getInt();
			
			hashTables[ i ] = hashTable;
		}
		
		return hashTables;
	}
	
	/**
	 * Reads the block table entries from the input.
	 * 
	 * @param blockTableEntires number of block table entries
	 * @return the read block table entries
	 * @throws IOException if IOException is thrown during reading from the stream
	 */
	private BlockTable[] readBlockTableEntries( final int blockTableEntires ) throws IOException {
		final int size = blockTableEntires * BlockTable.BLOCK_TABLE_STRUCTURE_SIZE;
		final ByteBuffer blockTablesBuffer = AlgorithmUtil.decryptData( readFullBuffer( size ), size, BLOCK_TABLE_DECRYPTION_KEY );
		
		final BlockTable[] blockTables = new BlockTable[ blockTableEntires ];
		
		for ( int i = 0; i < blockTables.length; i++ ) {
			final BlockTable blockTable = new BlockTable();
			
			blockTable.blockOffset = blockTablesBuffer.getInt();
			blockTable.blockSize = blockTablesBuffer.getInt();
			blockTable.fileSize = blockTablesBuffer.getInt();
			blockTable.flags = blockTablesBuffer.getInt();
			
			blockTables[ i ] = blockTable;
		}
		
		return blockTables;
	}
	
	/**
	 * Reads the extended block table entries from the input.
	 * 
	 * @param blockTableEntires number of block table entries
	 * @return the read extended block table entries
	 * @throws IOException if IOException is thrown during reading from the stream
	 */
	private short[] readExtBlockTableEntries( final int blockTableEntires ) throws IOException {
		final ByteBuffer extBlockTablesBuffer = readFullBuffer( blockTableEntires * 2 );
		
		final short[] extBlockTableHighOffsets = new short[ blockTableEntires ];
		
		for ( int i = 0; i < extBlockTableHighOffsets.length; i++ )
			extBlockTableHighOffsets[ i ] = extBlockTablesBuffer.getShort();
		
		return extBlockTableHighOffsets;
	}
	
	/**
	 * Returns the user data section of the MPQ archive.
	 * 
	 * @return the user data section of the MPQ archive
	 */
	public UserData getUserData() {
		return mpqArchive.userData;
	}
	
	/**
	 * Returns the extracted size of a file in the archive determined by the 3 different hash values of the file name (and path).
	 * 
	 * @param hash1 {@link MpqHashType#TABLE_OFFSET} hash value of the file name
	 * @param hash2 {@link MpqHashType#NAME_A} hash value of the file name
	 * @param hash3 {@link MpqHashType#NAME_B} hash value of the file name
	 * @return the extracted size of the specified file in the archive; or <code>-1</code> if the archive does not contain the specified file
	 */
	public int getFileSize( final int hash1, final int hash2, final int hash3 ) {
		final int hashTableEntries = mpqArchive.header.hashTableEntries;
		
		int counter = 0;
		
		for ( int i = hash1 & ( hashTableEntries - 1 );; i++ ) {
			if ( i == hashTableEntries )
				i = 0;
			final HashTable hashTable = mpqArchive.hashTables[ i ];
			if ( hashTable.fileBlockIndex == HashTable.FILE_BLOCK_INDEX_EMPTY )
				break;
			
			if ( hashTable.filePathHashA != hash2 || hashTable.filePathHashB != hash3 )
				continue;
			
			// FOUND!
			
			for ( int j = 0; j < hashTable.fileBlockIndex; j++ )
				if ( ( mpqArchive.blockTables[ j ].flags & BlockTable.FLAG_FILE ) == 0 )
					counter++;
			
			// File index:
			final int fileIndex = hashTable.fileBlockIndex - counter;
			if ( fileIndex < 0 || fileIndex >= mpqArchive.filesCount )
				return -1;
			
			return mpqArchive.blockTables[ mpqArchive.blockTableIndices[ fileIndex ] ].fileSize;
		}
		
		return -1;
	}
	
	/**
	 * Returns the content of a file taken from the archive.
	 * 
	 * @param fileName name of the file whose content to be returned
	 * @return the content of the specified file; or <code>null</code> if the archive does not contain the specified file
	 * @throws InvalidMpqArchiveException if the storing method of the file is not supported/implemented or some error occurs
	 */
	public byte[] getFile( final String fileName ) throws InvalidMpqArchiveException {
		// Hash1 tells us where the file might be (if exists)
		final int hash1 = AlgorithmUtil.hashString( fileName, MpqHashType.TABLE_OFFSET );
		final int hash2 = AlgorithmUtil.hashString( fileName, MpqHashType.NAME_A );
		final int hash3 = AlgorithmUtil.hashString( fileName, MpqHashType.NAME_B );
		
		return getFile( hash1, hash2, hash3 );
	}
	
	/**
	 * Returns the content of a file taken from the archive.
	 * 
	 * @param mpqContent file name hashes provider whose content to be returned
	 * @return the content of the specified file; or <code>null</code> if the archive does not contain the specified file
	 * @throws InvalidMpqArchiveException if the storing method of the file is not supported/implemented or some error occurs
	 */
	public byte[] getFile( final IMpqContent mpqContent ) throws InvalidMpqArchiveException {
		return getFile( mpqContent.getHash1(), mpqContent.getHash2(), mpqContent.getHash3() );
	}
	
	/**
	 * Returns the content of a file taken from the archive determined by the 3 different hash values of the file name (and path).
	 * 
	 * @param hash1 {@link MpqHashType#TABLE_OFFSET} hash value of the file name
	 * @param hash2 {@link MpqHashType#NAME_A} hash value of the file name
	 * @param hash3 {@link MpqHashType#NAME_B} hash value of the file name
	 * @return the content of the specified file; or <code>null</code> if the archive does not contain the specified file
	 * @throws InvalidMpqArchiveException if the storing method of the file is not supported/implemented or some error occurs
	 */
	public byte[] getFile( final int hash1, final int hash2, final int hash3 ) throws InvalidMpqArchiveException {
		final int hashTableEntries = mpqArchive.header.hashTableEntries;
		
		int counter = 0;
		for ( int i = hash1 & ( hashTableEntries - 1 );; i++ ) {
			if ( i == hashTableEntries )
				i = 0;
			final HashTable hashTable = mpqArchive.hashTables[ i ];
			if ( hashTable.fileBlockIndex == HashTable.FILE_BLOCK_INDEX_EMPTY )
				break;
			
			if ( hashTable.filePathHashA != hash2 || hashTable.filePathHashB != hash3 )
				continue;
			
			// FOUND!
			
			for ( int j = 0; j < hashTable.fileBlockIndex; j++ )
				if ( ( mpqArchive.blockTables[ j ].flags & BlockTable.FLAG_FILE ) == 0 )
					counter++;
			
			// File index:
			final int fileIndex = hashTable.fileBlockIndex - counter;
			if ( fileIndex < 0 || fileIndex >= mpqArchive.filesCount )
				return null;
			
			final int blockTableIndex = mpqArchive.blockTableIndices[ fileIndex ];
			// The block containing the file
			final BlockTable blockTable = mpqArchive.blockTables[ blockTableIndex ];
			
			try {
				final int blocksCount = ( blockTable.flags & BlockTable.FLAG_SINGLE ) != 0 ? 1 : ( blockTable.fileSize + mpqArchive.blockSize - 1 )
				        / mpqArchive.blockSize;
				
				// Create a packed block offset table
				final int[] packedBlockOffsetTable = new int[ blocksCount + ( ( blockTable.flags & BlockTable.FLAG_EXTRA ) != 0 ? 2 : 1 ) ]; // 1 entry for each
																																			 // block + 1
				                                                                                                                             // extra + 1 extra
																																			 // if
				                                                                                                                             // FLAG_EXTRA is 1
				
				if ( ( blockTable.flags & BlockTable.FLAG_COMPRESSED ) != 0 && ( blockTable.flags & BlockTable.FLAG_SINGLE ) == 0 ) {
					// We need to load the packed block offset table, we will maintain this table for unpacked files too.
					
					mpqDataInput.position( ( mpqArchive.extBlockTableHighOffsets == null ? 0
					        : ( mpqArchive.extBlockTableHighOffsets[ blockTableIndex ] & 0xFFFFL ) << 32 )
					        + ( blockTable.blockOffset & 0xFFFF_FFFFL )
					        + ( mpqArchive.userData == null ? 0 : mpqArchive.userData.archiveHeaderOffset ) );
					
					// Read block positions from the beginning of file
					final ByteBuffer buffer = readFullBuffer( packedBlockOffsetTable.length * 4 );
					for ( int k = 0; k < packedBlockOffsetTable.length; k++ )
						packedBlockOffsetTable[ k ] = buffer.getInt();
					
					if ( ( blockTable.flags & BlockTable.FLAG_ENCRYPTED ) != 0 )
						throw new InvalidMpqArchiveException( "Decryption of packed block offset table is not yet implemented!" );
				} else {
					if ( ( blockTable.flags & BlockTable.FLAG_SINGLE ) == 0 ) {
						for ( int k = 0; k < blocksCount; k++ )
							packedBlockOffsetTable[ k ] = k * mpqArchive.blockSize;
						packedBlockOffsetTable[ blocksCount ] = blockTable.blockSize;
					} else {
						packedBlockOffsetTable[ 0 ] = 0;
						packedBlockOffsetTable[ 1 ] = blockTable.blockSize;
					}
				}
				
				final byte[] content = new byte[ blockTable.fileSize ];
				int contentIndex = 0;
				
				for ( int k = 0; k < blocksCount; k++ ) {
					// Unpacked size of the block
					final int unpackedSize = ( blockTable.flags & BlockTable.FLAG_SINGLE ) != 0 ? blockTable.fileSize
					        : ( k < blocksCount - 1 ? mpqArchive.blockSize : blockTable.fileSize - mpqArchive.blockSize * k );
					
					// Read block
					final int inSize = packedBlockOffsetTable[ k + 1 ] - packedBlockOffsetTable[ k ];
					mpqDataInput.position( ( mpqArchive.extBlockTableHighOffsets == null ? 0
					        : ( mpqArchive.extBlockTableHighOffsets[ blockTableIndex ] & 0xFFFFL ) << 32 )
					        + ( blockTable.blockOffset & 0xFFFF_FFFFL )
					        + ( mpqArchive.userData == null ? 0 : mpqArchive.userData.archiveHeaderOffset ) + packedBlockOffsetTable[ k ] );
					
					final byte[] inBuffer = readFullBuffer( inSize ).array();
					
					// Check encryption
					if ( ( blockTable.flags & BlockTable.FLAG_ENCRYPTED ) != 0 )
						throw new InvalidMpqArchiveException( "Decryption of data block is not yet implemented!" );
					
					// Check compression
					if ( ( blockTable.flags & BlockTable.FLAG_COMPRESSED_MULTI ) != 0 ) {
						// Decompress block
						AlgorithmUtil.decompressMultiBlock( inBuffer, unpackedSize, content, contentIndex );
					}
					// Check implosion
					else if ( ( blockTable.flags & BlockTable.FLAG_COMPRESSED_PKWARE ) != 0 ) {
						// Explode block
						throw new InvalidMpqArchiveException( "Explosion of data block is not yet implemented!" );
					} else {
						// Copy block
						System.arraycopy( inBuffer, 0, content, contentIndex, inSize );
					}
					
					contentIndex += unpackedSize;
					
				}
				
				return content;
				
			} catch ( final IOException ie ) {
				throw new InvalidMpqArchiveException( ie.getMessage(), ie );
			}
		}
		
		return null;
	}
	
	/**
	 * Reads the specified amount of bytes from the input stream and wraps it into a ByteBuffer.<br>
	 * Little endian byte order is set in the returned buffer.<br>
	 * If EOF is reached before the full buffer is read, IOException will be thrown.
	 * 
	 * @param size number of bytes to read
	 * @return a ByteBuffer wrapping the byte array containing the read bytes
	 * @throws IOException if IOException occurs during the read or if EOF is reached before <code>size</code> bytes are read
	 */
	private ByteBuffer readFullBuffer( final int size ) throws IOException {
		final ByteBuffer buffer = ByteBuffer.wrap( new byte[ size ] );
		
		if ( mpqDataInput.read( buffer ) < size )
			throw new IOException( "Unexpected end of file, tried to read " + size + " bytes but EOF reached!" );
		
		buffer.position( 0 );
		return buffer.order( ByteOrder.LITTLE_ENDIAN );
	}
	
	/**
	 * Returns the file if a file is being parsed.
	 * 
	 * @return the file if a file is being parsed; <code>null</code> otherwise
	 */
	public Path getFileName() {
		return mpqDataInput.getFile();
	}
	
	/**
	 * Closes the resources associated with data input of the MPQ archive.
	 */
	@Override
	public void close() {
		if ( mpqDataInput != null )
			try {
				mpqDataInput.close();
			} catch ( final IOException ie ) {
				// We're done, ignore this.
			}
	}
	
}
