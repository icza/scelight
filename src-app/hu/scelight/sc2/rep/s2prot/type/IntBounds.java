/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.sc2.rep.s2prot.type;

/**
 * Bounds for reading an <code>int</code> number.
 * 
 * <p>
 * Instances are IMMUTABLE.
 * </p>
 * 
 * @author Andras Belicza
 */
public class IntBounds {
	
	/** Offset to add to the read value. */
	public final int offset;
	
	/** Number of bits to read. */
	public int       bits;
	
	/**
	 * Creates a new {@link IntBounds}.
	 * 
	 * @param offset offset to add to the read value
	 * @param bits number of bits to read
	 */
	public IntBounds( final int offset, final int bits ) {
		this.offset = offset;
		this.bits = bits;
	}
	
}
