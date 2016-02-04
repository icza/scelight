/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapi.util.type;

/**
 * A bit array which stores the bits in a byte array.
 * 
 * @author Andras Belicza
 */
public interface IBitArray {
	
	/**
	 * Returns the bits count.
	 * 
	 * @return the bits count
	 * 
	 * @see #getOnesCount()
	 */
	int getCount();
	
	/**
	 * Returns the bytes holding the bits.
	 * 
	 * @return the bytes holding the bits
	 */
	byte[] getArray();
	
	/**
	 * Returns the number of <code>1</code> bits.
	 * 
	 * @return the number of <code>1</code> bits.
	 * 
	 * @since 1.3
	 * 
	 * @see #getBit(int)
	 */
	int getOnesCount();
	
	/**
	 * Tells whether the bit at the specified index is <code>1</code>.
	 * 
	 * @param index index of the bit to test, must be in the range of 0..count (exclusive)
	 * @return true if the bit at the specified index is <code>1</code>; false otherwise
	 * 
	 * @since 1.3
	 * 
	 * @see #getOnesCount()
	 */
	boolean getBit( final int index );
	
	/**
	 * Returns the hex string representation of the bits postpended with the count in parenthesis.
	 * 
	 * @return the hex string representation of the bits postpended with the count in parenthesis
	 */
	@Override
	String toString();
	
}
