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

import hu.scelight.sc2.rep.s2prot.BitPackedBuffer;

/**
 * Reader/parser methods enum for different types.
 * 
 * @author Andras Belicza
 */
public enum TypeMethod {
	
	/** Associated with {@link BitPackedBuffer#_array(IntBounds, int)}. */
	ARRAY,
	
	/** Associated with {@link BitPackedBuffer#_bitarray(IntBounds)}. */
	BITARRAY,
	
	/** Associated with {@link BitPackedBuffer#_blob(IntBounds)} */
	BLOB,
	
	/** Associated with {@link BitPackedBuffer#_bool()} */
	BOOL,
	
	/** Associated with {@link BitPackedBuffer#_choice(IntBounds, Field[])}. */
	CHOICE,
	
	/** Associated with {@link BitPackedBuffer#_fourcc()}. */
	FOURCC,
	
	/** Associated with {@link BitPackedBuffer#_int(IntBounds)}. */
	INT,
	
	/** Associated with {@link BitPackedBuffer#_long(LongBounds)}. */
	LONG,
	
	/** Constant <code>null</code>. */
	NULL,
	
	/** Associated with {@link BitPackedBuffer#_optional(int)}. */
	OPTIONAL,
	
	/** Associated with {@link BitPackedBuffer#_float()}. */
	FLOAT,
	
	/** Associated with {@link BitPackedBuffer#_double()}. */
	DOUBLE,
	
	/** Associated with {@link BitPackedBuffer#_struct(Field[], int)}. */
	STRUCT;
	
}
