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
 * Bit packed decoder.
 * 
 * <p>
 * Implementation is not thread-safe!
 * </p>
 * 
 * @author Andras Belicza
 */
public class BitPackedDecoder extends BitPackedBuffer {
	
	/**
	 * Creates a new {@link BitPackedDecoder}.
	 * 
	 * @param data raw byte data
	 * @param typeInfos type info array
	 */
	public BitPackedDecoder( final byte[] data, final TypeInfo[] typeInfos ) {
		super( data, typeInfos );
	}
	
	/**
	 * Creates a new {@link BitPackedDecoder}.
	 * 
	 * @param data raw byte data
	 * @param typeInfos type info array
	 * @param bigEndian tells if byte order is big endian
	 */
	public BitPackedDecoder( final byte[] data, final TypeInfo[] typeInfos, final boolean bigEndian ) {
		super( data, typeInfos, bigEndian );
	}
	
	@Override
	public Object[] _array( final IntBounds bounds, final int typeid ) {
		final int length = _int( bounds );
		return instances( typeid, length );
	}
	
	@Override
	public BitArray _bitarray( final IntBounds bounds ) {
		final int length = _int( bounds );
		
		final byte[] array = new byte[ ( length + 7 ) >> 3 ];
		if ( ( length & 0x07 ) == 0 ) {
			// Length is a multiple of 8
			for ( int i = 0; i < array.length; i++ )
				array[ i ] = (byte) read_bits_int( 8 );
		} else {
			final int completeBytes = array.length - 1;
			for ( int i = 0; i < completeBytes; i++ )
				array[ i ] = (byte) read_bits_int( 8 );
			// Remaining bits:
			array[ completeBytes ] = (byte) read_bits_int( length & 0x07 );
		}
		
		return new BitArray( length, array );
	}
	
	@Override
	public XString _blob( final IntBounds bounds ) {
		final int length = _int( bounds );
		return read_aligned_bytes( length );
	}
	
	@Override
	public boolean _bool() {
		return read_bits_int( 1 ) != 0;
	}
	
	@Override
	public Pair< String, Object > _choice( final IntBounds bounds, final Field[] fields ) {
		final int tag = _int( bounds );
		final Field field = fields[ tag ];
		
		return new Pair<>( field.name, instance( field.typeid ) );
	}
	
	@Override
	public XString _fourcc() {
		return read_aligned_bytes( 4 );
	}
	
	@Override
	public int _int( final IntBounds bounds ) {
		return bounds.offset + read_bits_int( bounds.bits );
	}
	
	@Override
	public long _long( final LongBounds bounds ) {
		return bounds.offset + read_bits_long( bounds.bits );
	}
	
	@Override
	public Object _optional( final int typeid ) {
		return _bool() ? instance( typeid ) : null;
	}
	
	@Override
	public float _float() {
		System.out.println( "WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW" );
		// TODO Auto-generated method stub
		read_unaligned_bytes( 4 );
		return 0;
	}
	
	@Override
	public double _double() {
		System.out.println( "WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW" );
		// TODO Auto-generated method stub
		read_unaligned_bytes( 8 );
		return 0;
	}
	
	@Override
	@SuppressWarnings( "unchecked" )
	public Map< String, Object > _struct( final Field[] fields, final int extraEntriesCount ) {
		// Our ArrayMap also guarantees the same iteration order as elements are added
		final Map< String, Object > result = new ArrayMap<>( fields.length + extraEntriesCount );
		
		for ( final Field field : fields )
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
		
		return result;
	}
	
}
