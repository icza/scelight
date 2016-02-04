/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllauncher.util;

import hu.scelightapibase.util.ISizeFormat;
import hu.sllauncher.service.env.LEnv;

/**
 * Representation formats of a size (capacity) value.
 * 
 * @author Andras Belicza
 */
public enum SizeFormat implements ISizeFormat {
	
	/** Auto, depends on size value. */
	AUTO( "Auto" ),
	
	/** Bytes. */
	BYTES( "Bytes" ),
	
	/** KB. */
	KB( "KB" ),
	
	/** MB. */
	MB( "MB" ),
	
	/** GB. */
	GB( "GB" ),
	
	/** TB. */
	TB( "TB" );
	
	/** Text value of the size format. */
	public final String  text;
	
	/** String representation of the size format prepended with a space. */
	private final String preSpaceString;
	
	/**
	 * Creates a new {@link SizeFormat}.
	 * 
	 * @param text text value of the size format
	 */
	private SizeFormat( final String text ) {
		this.text = text;
		preSpaceString = " " + text;
	}
	
	@Override
	public String toString() {
		return text;
	}
	
	@Override
	public String formatSize( final long size, final int fractionDigits ) {
		switch ( this ) {
			case AUTO :
				if ( size < 1000 )
					return BYTES.formatSize( size, fractionDigits );
				else if ( ( size >> 10 ) < 1000 )
					return KB.formatSize( size, fractionDigits );
				else if ( ( size >> 20 ) < 1024 )
					return MB.formatSize( size, fractionDigits );
				else if ( ( size >> 30 ) < 1024 )
					return GB.formatSize( size, fractionDigits );
				else
					return TB.formatSize( size, fractionDigits );
				
			default : // default is only a syntactical requirement, we've covered all cases...
			case BYTES :
				return LEnv.LANG.formatNumber( size ) + preSpaceString;
			case KB :
				return LEnv.LANG.formatNumber( size / 1_024.0, fractionDigits ) + preSpaceString;
			case MB :
				return LEnv.LANG.formatNumber( size / 1_048_576.0, fractionDigits ) + preSpaceString;
			case GB :
				return LEnv.LANG.formatNumber( size / 1_073_741_824.0, fractionDigits ) + preSpaceString;
			case TB :
				return LEnv.LANG.formatNumber( size / 1_099_511_627_776.0, fractionDigits ) + preSpaceString;
		}
	}
	
	
	/** Cache of the values array. */
	public static final SizeFormat[] VALUES = values();
	
}
