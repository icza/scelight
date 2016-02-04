/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.sc2.rep.model.gameevents.cmd;

import hu.scelightapi.sc2.rep.model.gameevents.cmd.ITagTransformation;

/**
 * Unit tag display transformation strategy.
 * 
 * @author Andras Belicza
 */
public enum TagTransformation implements ITagTransformation {
	
	/** Original tag in decimal radix. */
	DECIMAL( "Decimal radix" ),
	
	/** Original tag in hexadecimal radix. */
	HEXA( "Hexadecimal radix" ),
	
	/** Shuffled tag in radix 36. */
	SHUFFLED( "Shuffled, radix 36" );
	
	
	/** Text value to include in the parameters string. */
	public final String text;
	
	
	/**
	 * Creates a new {@link TagTransformation}.
	 * 
	 * @param text text value of the tag strategy
	 */
	private TagTransformation( final String text ) {
		this.text = text;
	}
	
	@Override
	public String toString() {
		return text;
	}
	
	@Override
	public String tagToString( final int tag ) {
		switch ( this ) {
			case SHUFFLED :
				// Lowest 18 bits are unit tag recycle bits Remaining highest 14 bits are unit tag index bits
				// ______________31__18__17__ 0
				// Input bits : [I13..0][R17..0]
				// ______________31__18__17___4_ 3___0
				// Output bits: [R17..4][I13..0][R3..0]
				return Integer.toString( ( ( tag & 0x03fff0 ) << 14 ) | ( ( tag & 0xfffc0000 ) >>> 14 ) | ( tag & 0x0f ), 36 );
			case HEXA :
				return Integer.toHexString( tag );
			case DECIMAL :
			default :
				return Integer.toString( tag );
		}
	}
	
	/** Cache of the values array. */
	public static final TagTransformation[] VALUES = values();
	
}
