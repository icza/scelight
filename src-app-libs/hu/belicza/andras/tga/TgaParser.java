/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.belicza.andras.tga;

import java.awt.image.BufferedImage;

/**
 * TGA image format reader / parser.
 * 
 * <p>
 * Sources, references:
 * </p>
 * <ul>
 * <li><a href="http://en.wikipedia.org/wiki/Truevision_TGA">Truevision TGA on Wikipedia</a>
 * </ul>
 * 
 * @author Andras Belicza
 */
public class TgaParser {
	
	/**
	 * Parses an image from its raw TGA data and returns its "useful" area.
	 * 
	 * <p>
	 * The "useful" area is defined as the <i>smallest</i> rectangle area that includes all non-black pixels.
	 * </p>
	 * 
	 * <p>
	 * The image must be uncompressed true color image (type=2). This is the format of the map preview images stored in SC2 map files.
	 * </p>
	 * 
	 * @param data raw TGA data to parse the image from
	 * @return the "useful" area of the parsed map image
	 */
	public static BufferedImage parseTga( final byte[] data ) {
		final int width = ( data[ 12 ] & 0xff ) | ( data[ 13 ] & 0xff ) << 8;
		final int height = ( data[ 14 ] & 0xff ) | ( data[ 15 ] & 0xff ) << 8;
		
		// No need to optimize this code by pre-scanning for the "useful" image size
		// and creating only a buffered image of the useful size because the returned buffered image
		// is only used to write the cached map image file which will be read back right after that.
		
		final BufferedImage bufferedImage = new BufferedImage( width, height, BufferedImage.TYPE_INT_RGB );
		
		int pos = 18;
		int x1 = Integer.MAX_VALUE, y1 = Integer.MAX_VALUE, x2 = Integer.MIN_VALUE, y2 = Integer.MIN_VALUE;
		for ( int y = 0; y < height; y++ )
			for ( int x = 0; x < width; x++ ) {
				final int rgb = ( data[ pos + 2 ] & 0xff ) << 16 | ( data[ pos + 1 ] & 0xff ) << 8 | ( data[ pos ] & 0xff );
				if ( rgb != 0 ) {
					bufferedImage.setRGB( x, y, rgb );
					if ( x < x1 )
						x1 = x;
					if ( x > x2 )
						x2 = x;
					if ( y < y1 )
						y1 = y;
					if ( y > y2 )
						y2 = y;
				}
				pos += 3;
			}
		
		return bufferedImage.getSubimage( x1, y1, x2 - x1 + 1, y2 - y1 + 1 );
	}
	
}
