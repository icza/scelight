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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

import org.apache.tools.bzip2.CBZip2InputStream;

/**
 * Provides algorithm utilities for the MPQ format.
 * 
 * <p>
 * These include:
 * <ul>
 * <li>Decryption utility for MPQ encrypted data
 * <li>Hash calculation for a string
 * </ul>
 * (They are based on code from StormLib.)
 * </p>
 * 
 * <p>
 * Source: <a href='http://wiki.devklog.net/index.php?title=The_MoPaQ_Archive_Format'>The_MoPaQ_Archive_Format</a> </p>
 * 
 * @author Andras Belicza
 */
public class AlgorithmUtil {
	
	/**
	 * No need to instantiate this class.
	 */
	private AlgorithmUtil() {
	}
	
	/**
	 * Different types of hashes to make with hashString.
	 * 
	 * @author Andras Belicza
	 */
	public static enum MpqHashType {
	    /** Table offset hash type. */
		TABLE_OFFSET( 0 ),
		/** Name A hash type. */
		NAME_A( 1 ),
		/** Name A hash type. */
		NAME_B( 2 ),
		/** File key hash type. */
		FILE_KEY( 3 );
		
		/** Offset associated with this hash type. */
		public final int offset;
		
		/**
		 * Creates a new MpqHashType.
		 * 
		 * @param offsetBase base of the offset associated with this hash type
		 */
		private MpqHashType( final int offsetBase ) {
			offset = offsetBase << 8;
		}
	}
	
	/** A number table used by the decryption and hashing algorithms. */
	private static final long[] CRYPT_TABLE = new long[ 0x500 ];
	
	static {
		// The encryption/decryption and hashing functions use a number table in their procedures.
		// This table must be initialized before the functions are called the first time.
		
		long seed = 0x0010_0001L;
		int index1 = 0;
		int index2 = 0;
		int i;
		
		for ( index1 = 0; index1 < 0x100; index1++ ) {
			for ( index2 = index1, i = 0; i < 5; i++, index2 += 0x100 ) {
				long temp1, temp2;
				
				seed = ( ( seed * 125 + 3 ) & 0xFFFF_FFFFL ) % 0x2A_AAABL;
				temp1 = ( seed & 0xFFFFL ) << 0x10; // No need to mask for unsigned int: this denotes the higher 2 bytes
				
				seed = ( ( seed * 125 + 3 ) & 0xFFFF_FFFFL ) % 0x2A_AAABL;
				temp2 = ( seed & 0xFFFFL );
				
				CRYPT_TABLE[ index2 ] = temp1 | temp2;
			}
		}
	}
	
	/**
	 * Decrypts the given encrypted data with the specified key.
	 * 
	 * <p>
	 * The decryption will wrap the data with a byte buffer, and decrypt inside that (it shares the original byte buffer).
	 * 
	 * @param buffer a buffer wrapping the encrypted data to be decrypted
	 * @param size size of the buffer in bytes
	 * @param key the decryption key
	 * @return the byte buffer wrapping the decrypted data
	 */
	public static ByteBuffer decryptData( final ByteBuffer buffer, final int size, final int key ) {
		long seed1 = key & 0xFFFF_FFFFL;
		long seed2 = 0xEEEE_EEEEL;
		int ch;
		
		for ( int i = 0; i < size; i += 4 ) {
			seed2 = ( seed2 + CRYPT_TABLE[ 0x400 + (int) ( seed1 & 0xFF ) ] ) & 0xFFFF_FFFFL;
			ch = buffer.getInt( i ) ^ (int) ( seed1 + seed2 );
			
			seed1 = ( ( ( ( ~seed1 & 0xFFFF_FFFFL ) << 0x15 ) + 0x1111_1111L ) & 0xFFFF_FFFFL ) | ( seed1 >> 0x0B );
			seed2 = ( ( ch & 0xFFFF_FFFFL ) + seed2 + ( seed2 << 5 ) + 3 ) & 0xFFFF_FFFFL;
			
			buffer.putInt( i, ch );
		}
		
		buffer.position( 0 );
		return buffer;
	}
	
	/**
	 * Computes the hash of a string.
	 * 
	 * @param string string whose hash to be computed
	 * @param mpqHash type of hash to be computed
	 * @return the computed hash of the string
	 */
	protected static int hashString( final String string, final MpqHashType mpqHash ) {
		final int offset = mpqHash.offset;
		
		long seed1 = 0x7FED_7FEDL;
		long seed2 = 0xEEEE_EEEEL;
		int ch;
		
		final int length = string.length();
		for ( int i = 0; i < length; i++ ) {
			ch = Character.toUpperCase( string.charAt( i ) );
			
			seed1 = CRYPT_TABLE[ offset + ch ] ^ ( ( seed1 + seed2 ) & 0xFFFF_FFFFL );
			seed2 = ( ch + seed1 + seed2 + ( seed2 << 5 ) + 3 ) & 0xFFFF_FFFFL; // ch is small so it does not need to be masked
			                                                                    // (it's a char)
		}
		
		return (int) seed1;
	}
	
	/** Flag to indicate zlib compression. */
	private static final byte FLAG_COMPRESSION_ZLIB	 = 0x02;
	
	/** Flag to indicate BZip2 compression. */
	private static final byte FLAG_COMPRESSION_BZIP2 = 0x10;
	
	/**
	 * Decompresses a block which was compressed using the multi compression method.
	 * 
	 * @param block block to be decompressed
	 * @param outSize size of the decompressed data
	 * @param dest buffer to copy the decompressed data
	 * @param destPos position in the destination buffer to copy the decompressed data
	 * @throws InvalidMpqArchiveException if unsupported compression is found or the decompression of the block fails
	 */
	public static void decompressMultiBlock( byte[] block, final int outSize, final byte[] dest, final int destPos ) throws InvalidMpqArchiveException {
		// Check if block is really compressed, some blocks have set the compression flag, but are not compressed.
		if ( block.length >= outSize ) {
			// Copy block
			System.arraycopy( block, 0, dest, destPos, outSize );
		} else {
			final byte compressionFlag = block[ 0 ];
			
			switch ( compressionFlag ) {
				case FLAG_COMPRESSION_ZLIB : {
					// Handle deflated code (compressionFlag = 0x02)
					final Inflater inflater = new Inflater();
					inflater.setInput( block, 1, block.length - 1 );
					try {
						inflater.inflate( dest, destPos, outSize );
						inflater.end();
					} catch ( final DataFormatException dfe ) {
						throw new InvalidMpqArchiveException( "Data format exception, failed to decompressed block!", dfe );
					}
					break;
					// End of inflating
				}
				case FLAG_COMPRESSION_BZIP2 : {
					try ( final CBZip2InputStream cis = new CBZip2InputStream( new ByteArrayInputStream( block, 3, block.length - 3 ) ) ) {
						cis.read( dest );
					} catch ( final IOException ie ) {
						throw new InvalidMpqArchiveException( "Data format exception, failed to decompressed block!", ie );
					}
					break;
				}
				default :
					throw new InvalidMpqArchiveException( "Compression (" + compressionFlag + ") not supported!" );
			}
		}
	}
	
}
