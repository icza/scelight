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

import hu.belicza.andras.util.ArrayMap;
import hu.belicza.andras.util.type.BitArray;
import hu.belicza.andras.util.type.XString;
import hu.scelight.sc2.rep.s2prot.type.Field;
import hu.scelight.sc2.rep.s2prot.type.IntBounds;
import hu.scelight.sc2.rep.s2prot.type.LongBounds;
import hu.scelight.sc2.rep.s2prot.type.TypeInfo;
import hu.sllauncher.util.Pair;

import java.util.Map;

/**
 * Versioned decoder.
 * 
 * <p>
 * Implementation is not thread-safe!
 * </p>
 * 
 * @author Andras Belicza
 */
public class VersionedDecoder extends BitPackedBuffer {
	
	/**
	 * Creates a new {@link VersionedDecoder}.
	 * 
	 * @param data raw byte data
	 * @param typeInfos type info array
	 */
	public VersionedDecoder( final byte[] data, final TypeInfo[] typeInfos ) {
		super( data, typeInfos );
	}
	
	/**
	 * Creates a new {@link VersionedDecoder}.
	 * 
	 * @param data raw byte data
	 * @param typeInfos type info array
	 * @param bigEndian tells if byte order is big endian
	 */
	public VersionedDecoder( final byte[] data, final TypeInfo[] typeInfos, final boolean bigEndian ) {
		super( data, typeInfos, bigEndian );
	}
	
	/**
	 * Reads a variable length <code>int</code>.
	 * 
	 * @return the read <code>int</code>
	 */
	public int _vint() {
		int data, value = 0;
		
		for ( int shift = 0;; shift += 7 ) {
			data = read_bits_int( 8 );
			value |= ( data & 0x7f ) << shift;
			if ( ( data & 0x80 ) == 0 )
				return ( value & 0x01 ) > 0 ? -( value >> 1 ) : value >> 1;
		}
	}
	
	/**
	 * Reads a variable length <code>long</code>.
	 * 
	 * @return the read <code>long</code>
	 */
	public long _vlong() {
		long data, value = 0;
		
		for ( int shift = 0;; shift += 7 ) {
			data = read_bits_int( 8 );
			value |= ( data & 0x7f ) << shift;
			if ( ( data & 0x80 ) == 0 )
				return ( value & 0x01 ) > 0 ? -( value >> 1 ) : value >> 1;
		}
	}
	
	@Override
	public Object[] _array( final IntBounds bounds, final int typeid ) {
		read_bits_int( 8 ); // field type (0)
		
		final int length = _vint();
		return instances( typeid, length );
	}
	
	@Override
	public BitArray _bitarray( final IntBounds bounds ) {
		read_bits_int( 8 ); // field type (1)
		
		final int length = _vint();
		
		return new BitArray( length, read_aligned_bytes_arr( ( length + 7 ) >> 3 ) );
	}
	
	@Override
	public XString _blob( final IntBounds bounds ) {
		read_bits_int( 8 ); // field type (2)
		
		final int length = _vint();
		return read_aligned_bytes( length );
	}
	
	@Override
	public boolean _bool() {
		read_bits_int( 8 ); // field type (6)
		
		return read_bits_int( 8 ) != 0;
	}
	
	@Override
	public Pair< String, Object > _choice( final IntBounds bounds, final Field[] fields ) {
		read_bits_int( 8 ); // field type (3)
		
		final int tag = _vint();
		if ( tag >= fields.length )
			return null;
		
		final Field field = fields[ tag ];
		
		return new Pair<>( field.name, instance( field.typeid ) );
	}
	
	@Override
	public XString _fourcc() {
		read_bits_int( 8 ); // field type (7)
		
		return read_aligned_bytes( 4 );
	}
	
	@Override
	public int _int( final IntBounds bounds ) {
		read_bits_int( 8 ); // field type (9)
		
		return _vint();
	}
	
	@Override
	public long _long( final LongBounds bounds ) {
		read_bits_int( 8 ); // field type (9)
		
		return _vlong();
	}
	
	@Override
	public Object _optional( final int typeid ) {
		read_bits_int( 8 ); // field type (4)
		
		return read_bits_int( 8 ) != 0 ? instance( typeid ) : null;
	}
	
	@Override
	public float _float() {
		read_bits_int( 8 ); // field type (7)
		
		System.out.println( "WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW" );
		// TODO Auto-generated method stub
		read_aligned_bytes_arr( 4 );
		return 0;
	}
	
	@Override
	public double _double() {
		read_bits_int( 8 ); // field type (8)
		
		System.out.println( "WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW" );
		// TODO Auto-generated method stub
		read_aligned_bytes_arr( 8 );
		return 0;
	}
	
	@Override
	@SuppressWarnings( "unchecked" )
	public Map< String, Object > _struct( final Field[] fields, final int extraEntriesCount ) {
		read_bits_int( 8 ); // field type (5)
		
		// Our ArrayMap also guarantees the same iteration order as elements are added
		final Map< String, Object > result = new ArrayMap<>( fields.length + extraEntriesCount );
		
		final int length = _vint();
		
		for ( int i = 0; i < length; i++ ) {
			final int tag = _vint();
			
			Field field = null;
			for ( int j = fields.length - 1; j >= 0; j-- )
				if ( fields[ j ].tag == tag ) {
					field = fields[ j ];
					break;
				}
			
			if ( field == null )
				_skip_instance();
			else {
				if ( field.isNameParent ) {
					final Object parent = instance( field.typeid );
					
					if ( parent instanceof Map )
						result.putAll( (Map< String, Object >) parent );
					else if ( fields.length == 1 )
						result.put( field.name, parent ); // it should be: result = parent; but parent is not a map!
					else
						result.put( field.name, parent );
				} else
					result.put( field.name, instance( field.typeid ) );
			}
		}
		
		return result;
	}
	
	/**
	 * Skips an instance.
	 */
	public void _skip_instance() {
		switch ( read_bits_int( 8 ) ) { // Switch by field type
			case 0 : { // array
				final int length = _vint();
				for ( int i = 0; i < length; i++ )
					_skip_instance();
				break;
			}
			case 1 : // bitblob
				skipBytes( ( _vint() + 7 ) >> 3 );
				break;
			case 2 : // blob
				skipBytes( _vint() );
				break;
			case 3 : // choice
				_vint(); // tag
				_skip_instance();
				break;
			case 4 : // optional
				if ( read_bits_int( 8 ) != 0 )
					_skip_instance();
				break;
			case 5 : {// struct
				final int length = _vint();
				for ( int i = 0; i < length; i++ ) {
					_vint(); // tag
					_skip_instance();
				}
				break;
			}
			case 6 : // u8
				skipBytes( 1 );
				break;
			case 7 : // u32
				skipBytes( 4 );
				break;
			case 8 : // u64
				skipBytes( 8 );
				break;
			case 9 : // vint
				_vint();
				break;
		}
	}
	
}
