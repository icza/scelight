/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.sc2.rep.s2prot;

import hu.belicza.andras.util.type.BitArray;
import hu.belicza.andras.util.type.XString;
import hu.scelight.sc2.rep.s2prot.type.Field;
import hu.scelight.sc2.rep.s2prot.type.IntBounds;
import hu.scelight.sc2.rep.s2prot.type.LongBounds;
import hu.scelight.sc2.rep.s2prot.type.TypeInfo;
import hu.scelight.sc2.rep.s2prot.type.TypeMethod;
import hu.sllauncher.util.Pair;

import java.nio.ByteBuffer;
import java.util.Map;

/**
 * Bit packed buffer.
 * 
 * <p>
 * Implementation is not thread-safe!
 * </p>
 * 
 * @author Andras Belicza
 */
public abstract class BitPackedBuffer {
	
	/** Bit masks, masking the bits of a byte for the bits count specified by the index. */
	private static final int[]  BITS_MASKS      = new int[] { 0x00, 0x01, 0x03, 0x07, 0x0f, 0x1f, 0x3f, 0x7f, 0xff };
	
	/**
	 * Bit masks, masking the bits of a byte for the bits count specified by the index.<br>
	 * Values are <code>long</code>s so when masking, result will be automatically a <code>long</code>.
	 */
	private static final long[] BITS_MASKS_LONG = new long[] { 0x00L, 0x01L, 0x03L, 0x07L, 0x0fL, 0x1fL, 0x3fL, 0x7fL, 0xffL };
	
	/** Raw byte data. */
	private final byte[]        data;
	
	/** Type info array. */
	private final TypeInfo[]    typeInfos;
	
	
	/** Tells if byte order is big endian. */
	private final boolean       bigEndian;
	
	/** Byte buffer used to process raw byte data. */
	protected final ByteBuffer  wrapper;
	
	/** Cache of the last read byte. */
	private int                 cache;
	
	/** Bits left in cache. */
	private int                 bitsLeftInCache;
	
	/**
	 * Creates a new {@link BitPackedBuffer}.
	 * 
	 * @param data raw byte data
	 * @param typeInfos type info array
	 */
	public BitPackedBuffer( final byte[] data, final TypeInfo[] typeInfos ) {
		this( data, typeInfos, true );
	}
	
	/**
	 * Creates a new {@link BitPackedBuffer}.
	 * 
	 * @param data raw byte data
	 * @param typeInfos type info array
	 * @param bigEndian tells if byte order is big endian
	 */
	public BitPackedBuffer( final byte[] data, final TypeInfo[] typeInfos, final boolean bigEndian ) {
		this.data = data;
		this.typeInfos = typeInfos;
		this.bigEndian = bigEndian;
		
		wrapper = ByteBuffer.wrap( data );
	}
	
	/**
	 * Clears the current byte, drops unread bits from it, so the next read will happen from the next byte. This is to byte align the bit input stream.
	 */
	public void byte_align() {
		bitsLeftInCache = 0;
	}
	
	/**
	 * Skips the specified amount of bytes.
	 * <p>
	 * The method also skips bits left in the cache (by calling {@link #byte_align()} first).
	 * </p>
	 * 
	 * @param n bytes to skip
	 */
	public void skipBytes( final int n ) {
		byte_align();
		
		wrapper.position( wrapper.position() + n );
	}
	
	/**
	 * Tells if there are unread bytes in the buffer.
	 * 
	 * @return true if there are unread bytes; false otherwise
	 */
	public boolean hasRemaining() {
		return wrapper.hasRemaining();
	}
	
	
	// GENERAL READ METHODS
	
	
	/**
	 * Reads the next aligned bytes and returns them as a string.
	 * <p>
	 * The bytes are interpreted as a UTF-8 encoded string.
	 * </p>
	 * 
	 * @param length number of bytes to read
	 * @return the next bytes as a string
	 */
	public XString read_aligned_bytes( final int length ) {
		return new XString( read_aligned_bytes_arr( length ) );
	}
	
	/**
	 * Reads the next aligned bytes and returns them as a byte array.
	 * 
	 * @param length number of bytes to read
	 * @return the next bytes as a byte array
	 */
	public byte[] read_aligned_bytes_arr( final int length ) {
		skipBytes( length );
		
		final byte[] result = new byte[ length ];
		System.arraycopy( data, wrapper.position() - length, result, 0, length );
		
		return result;
	}
	
	/**
	 * Reads the specified amount of bits and returns it as a <code>int</code>.
	 * <p>
	 * Takes byte order ({@link #bigEndian}) into consideration.
	 * </p>
	 * 
	 * @param n number of bits to read; cannot be greater than 32
	 * @return the read bits as an int
	 */
	protected int read_bits_int( int n ) {
		return bigEndian ? read_bits_int_big( n ) : read_bits_int_little( n );
	}
	
	/**
	 * Reads the specified amount of bits and returns it as a <code>long</code>.
	 * <p>
	 * Takes byte order ({@link #bigEndian}) into consideration.
	 * </p>
	 * 
	 * @param n number of bits to read; cannot be greater than 64
	 * @return the read bits as an int
	 */
	protected long read_bits_long( int n ) {
		return bigEndian ? read_bits_long_big( n ) : read_bits_long_little( n );
	}
	
	/**
	 * Reads the specified amount of bits using big endian byte order and returns it as a <code>int</code>.
	 * 
	 * @param n number of bits to read; cannot be greater than 32
	 * @return the read bits as an int
	 */
	private int read_bits_int_big( int n ) {
		// WARNING! n might be 0!
		if ( n == 0 )
			return 0;
		
		int value = 0;
		while ( true ) {
			if ( bitsLeftInCache == 0 ) {
				cache = wrapper.get() & 0xff;
				bitsLeftInCache = 8;
			}
			
			if ( bitsLeftInCache >= n ) {
				// All remaining needed bits are in the cache
				if ( bitsLeftInCache == n ) {
					value = ( value << n ) | cache; // No need to mask, we need all cache bits
					bitsLeftInCache = 0;
					// Nothing left in cache => no need to shift it (cache will be overwritten before next read...)
				} else {
					value = ( value << n ) | ( cache & BITS_MASKS[ n ] );
					bitsLeftInCache -= n;
					cache >>= n;
				}
				// No need to decrease n, we can return
				return value;
			} else {
				// Use all the bits from the cache; and more will be needed...
				value = ( value << bitsLeftInCache ) | cache; // No need to mask, we need all cache bits
				n -= bitsLeftInCache;
				bitsLeftInCache = 0;
				// Nothing left in cache => no need to shift it (cache will be overwritten before next read...)
			}
		}
	}
	
	/**
	 * Reads the specified amount of bits using big endian byte order and returns it as a <code>long</code>.
	 * 
	 * @param n number of bits to read; cannot be greater than 64
	 * @return the read bits as an int
	 */
	private long read_bits_long_big( int n ) {
		// WARNING! n might be 0!
		if ( n == 0 )
			return 0;
		
		long value = 0;
		while ( true ) {
			if ( bitsLeftInCache == 0 ) {
				cache = wrapper.get() & 0xff;
				bitsLeftInCache = 8;
			}
			
			if ( bitsLeftInCache >= n ) {
				// All remaining needed bits are in the cache
				if ( bitsLeftInCache == n ) {
					value = ( value << n ) | cache; // No need to mask, we need all cache bits
					bitsLeftInCache = 0;
					// Nothing left in cache => no need to shift it (cache will be overwritten before next read...)
				} else {
					value = ( value << n ) | ( cache & BITS_MASKS[ n ] );
					bitsLeftInCache -= n;
					cache >>= n;
				}
				// No need to decrease n, we can return
				return value;
			} else {
				// Use all the bits from the cache; and more will be needed...
				value = ( value << bitsLeftInCache ) | cache; // No need to mask, we need all cache bits
				n -= bitsLeftInCache;
				bitsLeftInCache = 0;
				// Nothing left in cache => no need to shift it (cache will be overwritten before next read...)
			}
		}
	}
	
	/**
	 * Reads the specified amount of bits using little endian byte order and returns it as a <code>int</code>.
	 * 
	 * @param n number of bits to read; cannot be greater than 32
	 * @return the read bits as an int
	 */
	private int read_bits_int_little( int n ) {
		// WARNING! n might be 0!
		if ( n == 0 )
			return 0;
		
		int value = 0;
		int valueBits = 0; // Bits already set in value
		while ( true ) {
			if ( bitsLeftInCache == 0 ) {
				cache = wrapper.get() & 0xff;
				bitsLeftInCache = 8;
			}
			
			if ( bitsLeftInCache >= n ) {
				// All remaining needed bits are in the cache
				if ( bitsLeftInCache == n ) {
					value |= cache << valueBits; // No need to mask, we need all cache bits
					bitsLeftInCache = 0;
					// Nothing left in cache => no need to shift it (cache will be overwritten before next read...)
				} else {
					value |= ( cache & BITS_MASKS[ n ] ) << valueBits;
					bitsLeftInCache -= n;
					cache >>= n;
				}
				// No need to decrease n or increase valueBits, we can return
				return value;
			} else {
				// Use all the bits from the cache; and more will be needed...
				value |= cache << valueBits; // No need to mask, we need all cache bits
				n -= bitsLeftInCache;
				valueBits += bitsLeftInCache;
				bitsLeftInCache = 0;
				// Nothing left in cache => no need to shift it (cache will be overwritten before next read...)
			}
		}
	}
	
	/**
	 * Reads the specified amount of bits using little endian byte order and returns it as a <code>long</code>.
	 * 
	 * @param n number of bits to read; cannot be greater than 64
	 * @return the read bits as an int
	 */
	private long read_bits_long_little( int n ) {
		// WARNING! n might be 0!
		if ( n == 0 )
			return 0;
		
		long value = 0;
		int valueBits = 0; // Bits already set in value
		while ( true ) {
			if ( bitsLeftInCache == 0 ) {
				cache = wrapper.get() & 0xff;
				bitsLeftInCache = 8;
			}
			
			if ( bitsLeftInCache >= n ) {
				// All remaining needed bits are in the cache
				if ( bitsLeftInCache == n ) {
					// No need to mask, we need all cache bits BUT we still mask to get a long value and avoid overflow for
					// valueBits > 24
					value |= ( cache & BITS_MASKS_LONG[ n ] ) << valueBits;
					bitsLeftInCache = 0;
					// Nothing left in cache => no need to shift it (cache will be overwritten before next read...)
				} else {
					value |= ( cache & BITS_MASKS_LONG[ n ] ) << valueBits;
					bitsLeftInCache -= n;
					cache >>= n;
				}
				// No need to decrease n or increase valueBits, we can return
				return value;
			} else {
				// Use all the bits from the cache; and more will be needed...
				// No need to mask, we need all cache bits BUT we still mask to get a long value and avoid overflow for
				// valueBits > 24
				value |= ( cache & BITS_MASKS_LONG[ bitsLeftInCache ] ) << valueBits;
				n -= bitsLeftInCache;
				valueBits += bitsLeftInCache;
				bitsLeftInCache = 0;
				// Nothing left in cache => no need to shift it (cache will be overwritten before next read...)
			}
		}
	}
	
	/**
	 * Reads the next unaligned bytes and returns them.
	 * 
	 * @param length number of bytes to read
	 * @return the next unaligned bytes
	 */
	public byte[] read_unaligned_bytes( final int length ) {
		final byte[] result = new byte[ length ];
		
		for ( int i = 0; i < length; i++ )
			result[ i ] = (byte) read_bits_int( 8 );
		
		return result;
	}
	
	
	// READ METHODS GIVING MEANING TO THE READ BITS/BYTES
	
	
	/**
	 * Reads an object.
	 * 
	 * @param typeid type id of the object to read
	 * @return the read object
	 */
	public Object instance( final int typeid ) {
		return instance( typeid, 0 );
	}
	
	/**
	 * Reads an object.
	 * 
	 * <p>
	 * If <code>typeid</code> denotes a structure, a structure of initial capacity defined by the parameters of the {@link TypeInfo} will be allocated. If the
	 * returned structure is manually extended (new entries are added), the structure type most likely needs to internally reallocate to accommodate the extra
	 * entries. This can be avoided if the number of extra entries (which will be added in the future) is passed as the <code>extraEntriesCount</code>
	 * parameter, which will be used to allocate a structure with a bigger initial capacity.
	 * </p>
	 * 
	 * @param typeid type id of the object to read
	 * @param extraEntriesCount optional number of extra structure entries to allocate (for future manual additions)
	 * @return the read object
	 */
	public Object instance( final int typeid, final int extraEntriesCount ) {
		final TypeInfo ti = typeInfos[ typeid ];
		
		switch ( ti.method ) {
			case INT :
				return _int( (IntBounds) ti.param1 );
			case STRUCT :
				return _struct( (Field[]) ti.param1, extraEntriesCount );
			case ARRAY :
				return _array( (IntBounds) ti.param1, (Integer) ti.param2 );
			case BITARRAY :
				return _bitarray( (IntBounds) ti.param1 );
			case BLOB :
				return _blob( (IntBounds) ti.param1 );
			case BOOL :
				return _bool();
			case CHOICE :
				return _choice( (IntBounds) ti.param1, (Field[]) ti.param2 );
			case DOUBLE :
				return _double();
			case FLOAT :
				return _float();
			case LONG :
				return _long( (LongBounds) ti.param1 );
			case OPTIONAL :
				return _optional( (Integer) ti.param1 );
			case FOURCC :
				return _fourcc();
			case NULL :
				return null;
		}
		
		return null;
	}
	
	/**
	 * Reads multiple objects, all of the same type.<br>
	 * This is an optimized version of reading multiple objects of the same type, type-related checks and conversions are only performed once!<br>
	 * Aids the creation of an optimized implementation for the {@link #_array(IntBounds, int)}.
	 * 
	 * <p>
	 * While return type is specified to be <code>Object[]</code>, the dynamic type is a more concrete array type, for example if the type method specified by
	 * <code>typeid</code> is {@link TypeMethod#INT}, the returned type will be <code>Integer[]</code>.
	 * </p>
	 * 
	 * @param typeid type id of the objects to read
	 * @param length number of objects to read
	 * @return the read objects in an array
	 */
	protected Object[] instances( final int typeid, final int length ) {
		final TypeInfo ti = typeInfos[ typeid ];
		
		switch ( ti.method ) {
			case INT : {
				final Integer[] result = new Integer[ length ];
				final IntBounds param1 = (IntBounds) ti.param1;
				for ( int i = 0; i < length; i++ )
					result[ i ] = _int( param1 );
				return result;
			}
			case STRUCT : {
				@SuppressWarnings( "unchecked" )
				final Map< String, Object >[] result = new Map[ length ];
				final Field[] param1 = (Field[]) ti.param1;
				for ( int i = 0; i < length; i++ )
					result[ i ] = _struct( param1, 0 );
				return result;
			}
			case ARRAY : {
				final Object[][] result = new Object[ length ][];
				final IntBounds param1 = (IntBounds) ti.param1;
				final Integer param2 = (Integer) ti.param1;
				for ( int i = 0; i < length; i++ )
					result[ i ] = _array( param1, param2 );
				return result;
			}
			case BITARRAY : {
				final BitArray[] result = new BitArray[ length ];
				final IntBounds param1 = (IntBounds) ti.param1;
				for ( int i = 0; i < length; i++ )
					result[ i ] = _bitarray( param1 );
				return result;
			}
			case BLOB : {
				final XString[] result = new XString[ length ];
				final IntBounds param1 = (IntBounds) ti.param1;
				for ( int i = 0; i < length; i++ )
					result[ i ] = _blob( param1 );
				return result;
			}
			case BOOL : {
				final Boolean[] result = new Boolean[ length ];
				for ( int i = 0; i < length; i++ )
					result[ i ] = _bool();
				return result;
			}
			case CHOICE : {
				@SuppressWarnings( "unchecked" )
				final Pair< String, Object >[] result = new Pair[ length ];
				final IntBounds param1 = (IntBounds) ti.param1;
				final Field[] param2 = (Field[]) ti.param2;
				for ( int i = 0; i < length; i++ )
					result[ i ] = _choice( param1, param2 );
				return result;
			}
			case DOUBLE : {
				final Double[] result = new Double[ length ];
				for ( int i = 0; i < length; i++ )
					result[ i ] = _double();
				return result;
			}
			case FLOAT : {
				final Float[] result = new Float[ length ];
				for ( int i = 0; i < length; i++ )
					result[ i ] = _float();
				return result;
			}
			case LONG : {
				final Long[] result = new Long[ length ];
				final LongBounds param1 = (LongBounds) ti.param1;
				for ( int i = 0; i < length; i++ )
					result[ i ] = _long( param1 );
				return result;
			}
			case OPTIONAL : {
				final Object[] result = new Object[ length ];
				final Integer param1 = (Integer) ti.param1;
				for ( int i = 0; i < length; i++ )
					result[ i ] = _optional( param1 );
				return result;
			}
			case FOURCC : {
				final XString[] result = new XString[ length ];
				for ( int i = 0; i < length; i++ )
					result[ i ] = _fourcc();
				return result;
			}
			case NULL :
				return new Object[ length ]; // New array is initialized with nulls, nothing else to do
		}
		
		return new Object[ 0 ];
	}
	
	/**
	 * Reads an array.
	 * 
	 * <p>
	 * While return type is specified to be <code>Object[]</code>, the dynamic type is a more concrete array type, for example if the type method specified by
	 * <code>typeid</code> is {@link TypeMethod#INT}, the returned type will be <code>Integer[]</code>.
	 * </p>
	 * 
	 * @param bounds bounds for reading array length
	 * @param typeid type id of the array elements
	 * @return the read array
	 */
	public abstract Object[] _array( final IntBounds bounds, final int typeid );
	
	/**
	 * Reads a bit array.
	 * 
	 * @param bounds bounds for reading array length
	 * @return the read bit array
	 */
	public abstract BitArray _bitarray( final IntBounds bounds );
	
	/**
	 * Reads a string.
	 * 
	 * @param bounds bounds for reading string length
	 * @return the read string
	 */
	public abstract XString _blob( final IntBounds bounds );
	
	/**
	 * Reads a boolean.
	 * 
	 * @return the read boolean
	 */
	public abstract boolean _bool();
	
	/**
	 * Reads a variable object.
	 * 
	 * @param bounds bounds for reading object type
	 * @param fields list of possible fields (property name and type id pairs)
	 * @return the name of the field and its read value
	 */
	public abstract Pair< String, Object > _choice( final IntBounds bounds, final Field[] fields );
	
	/**
	 * Reads a <a href="http://en.wikipedia.org/wiki/FourCC">FourCC</a> (4 characters).
	 * 
	 * @return the read FourCC
	 */
	public abstract XString _fourcc();
	
	/**
	 * Reads an <code>int</code>.
	 * 
	 * @param bounds offset and bits count of the read number
	 * @return the read <code>int</code>
	 */
	public abstract int _int( final IntBounds bounds );
	
	/**
	 * Reads a <code>long</code>.
	 * 
	 * @param bounds offset and bits count of the read number
	 * @return the read <code>long</code>
	 */
	public abstract long _long( final LongBounds bounds );
	
	/**
	 * Optionally reads an object.
	 * <p>
	 * First reads a boolean, and if that is <code>true</code>, then reads an object and returns it; if it is <code>false</code>, <code>null</code> is returned.
	 * </p>
	 * 
	 * @param typeid type id of the object to read
	 * @return the read object
	 */
	public abstract Object _optional( final int typeid );
	
	/**
	 * Reads a 32-bit float.
	 * 
	 * @return the read float
	 */
	public abstract float _float();
	
	/**
	 * Reads a 64-bit double.
	 * 
	 * @return the read double
	 */
	public abstract double _double();
	
	/**
	 * Reads a map (structure).
	 * 
	 * @param fields list of fields (property name and type id pairs)
	 * @param extraEntriesCount optional number of extra structure entries to allocate (for future manual additions)
	 * @return the read map
	 */
	public abstract Map< String, Object > _struct( final Field[] fields, final int extraEntriesCount );
	
}
