/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.gui.page.repanalyzer.inspector.binarydata;

/**
 * Hex line size (number of bytes displayed in a line).
 * 
 * @author Andras Belicza
 */
public enum HexLineSize {
	
	/** 8 Bytes. */
	EIGHT( 3 ),
	
	/** 16 Bytes. */
	SIXTEEN( 4 ),
	
	/** 32 Bytes. */
	THIRTY_TWO( 5 ),
	
	/** 64 Bytes. */
	SIXTY_FOUR( 6 );
	
	
	/** Text value of the hex line size. */
	public final String text;
	
	/** Number of left shifts to do to 1 to get the bytes in a line. */
	public final int    shiftCount;
	
	/** Number of bytes in a line. It is a power of 2. */
	public final int    bytesCount;
	
	
	/**
	 * Creates a new {@link HexLineSize}.
	 * 
	 * @param shiftCount number of left shifts to do to 1 to get the bytes in a line
	 */
	private HexLineSize( final int shiftCount ) {
		this.shiftCount = shiftCount;
		this.bytesCount = 1 << shiftCount;
		this.text = Integer.toString( bytesCount );
	}
	
	@Override
	public String toString() {
		return text;
	}
	
	
	/** Cache of the values array. */
	public static final HexLineSize[] VALUES = values();
	
}
