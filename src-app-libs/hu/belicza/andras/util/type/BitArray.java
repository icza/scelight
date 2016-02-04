/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.belicza.andras.util.type;

import hu.scelight.util.Utils;
import hu.scelightapi.util.type.IBitArray;

/**
 * A bit array which stores the bits in a byte array.
 * 
 * @author Andras Belicza
 */
public class BitArray implements IBitArray {
	
	/** Masks of the 8 bits of a byte. */
	private static final byte[] BIT_MASKS = { 0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, (byte) 0x80 };
	
	
	/** Bits count. */
	public final int            count;
	
	/** The bytes holding the bits. */
	public final byte[]         array;
	
	
	/**
	 * Creates a new {@link BitArray}.
	 * 
	 * @param count bits count
	 * @param array byte array holding the bits
	 */
	public BitArray( final int count, final byte[] array ) {
		this.count = count;
		this.array = array;
	}
	
	@Override
	public int getCount() {
		return count;
	}
	
	@Override
	public byte[] getArray() {
		return array;
	}
	
	@Override
	public int getOnesCount() {
		int ones = 0;
		for ( final byte b : array )
			ones += Integer.bitCount( b & 0xff );
		return ones;
	}
	
	@Override
	public boolean getBit( final int index ) {
		return ( array[ index >> 3 ] & BIT_MASKS[ index & 0x07 ] ) != 0;
	}
	
	@Override
	public String toString() {
		return "0x" + Utils.toHexString( array ) + " (count=" + count + ")";
	}
	
}
