/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.belicza.andras.dds;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * DDS image format reader / parser.
 * 
 * <p>
 * Sources, references:
 * </p>
 * <ul>
 * <li><a href="http://msdn.microsoft.com/en-us/library/bb943982(v=vs.85).aspx">DDS_HEADER reference</a>
 * <li><a href="http://msdn.microsoft.com/en-us/library/bb943984(v=vs.85).aspx">DDS_PIXELFORMAT reference</a>
 * <li><a href="http://en.wikipedia.org/wiki/S3_Texture_Compression">S3 Texture Compression</a>
 * <li><a href="http://oss.sgi.com/projects/ogl-sample/registry/EXT/texture_compression_s3tc.txt">Texture compression s3tc specification</a>
 * <li><a href="https://code.google.com/p/java-dds/">Java DDS ImageIO plugin</a>
 * </ul>
 * 
 * @author Andras Belicza
 */
public class DdsParser {
	
	/** Pixel format structure flag bit indicating the Texture contains alpha data; RGBAlphaBitMask contains valid data. */
	private static final int DDPF_ALPHAPIXELS = 0x01;
	
	/** Pixel format structure flag bit indicating the fourCC field is valid and indicates the format. */
	private static final int DDPF_FOURCC      = 0x04;
	
	/**
	 * Pixel format structure flag bit indicating the Texture contains uncompressed RGB data.<br>
	 * RGBBitCount and the RGB masks (dwRBitMask, dwRBitMask, dwRBitMask) contain valid data.
	 */
	private static final int DDPF_RGB         = 0x40;
	
	
	
	/**
	 * Parses a DDS image from the specified data.
	 * 
	 * @param buffer data to parse an image from
	 * @return the parsed image
	 */
	public static BufferedImage parseDDS( final byte[] buffer ) {
		final ByteBuffer w = ByteBuffer.wrap( buffer );
		w.order( ByteOrder.LITTLE_ENDIAN );
		
		// Magic
		if ( w.getInt() != 0x20534444 ) // "DDS "
			throw new RuntimeException( "Invalid DDS magic!" );
		
		// DDS_HEADER
		@SuppressWarnings( "unused" )
		final int size1 = w.getInt(); // Header size, set to 124
		@SuppressWarnings( "unused" )
		final int flags1 = w.getInt(); // Flags to indicate which members contain valid data
		final int height = w.getInt();
		final int width = w.getInt();
		@SuppressWarnings( "unused" )
		final int pitch = w.getInt(); // Pitch or number of bytes per scan line in an uncompressed texture; UNRELIABLE
		@SuppressWarnings( "unused" )
		final int depth = w.getInt(); // Depth of a volume texture (in pixels)
		@SuppressWarnings( "unused" )
		final int mipMapcount = w.getInt(); // Number of mipmap levels
		w.position( w.position() + 44 ); // Reserved
		
		// DDS_PIXELFORMAT
		@SuppressWarnings( "unused" )
		final int size2 = w.getInt(); // Structure size, set to 32
		final int flags2 = w.getInt(); // Values which indicate what type of data is in the surface.
		final int fourCC = w.getInt(); // Four-character codes for specifying compressed or custom formats.
		final int rgbBitCount = w.getInt(); // Number of bits in an RGB (possibly including alpha) format.
		final int rBitMask = w.getInt(); // Red (or lumiannce or Y) mask for reading color data.
		final int gBitMask = w.getInt(); // Green (or U) mask for reading color data.
		final int bBitMask = w.getInt(); // Blue (or V) mask for reading color data.
		final int aBitMask = w.getInt(); // Alpha mask for reading alpha data.
		// END OF DDS_PIXELFORMAT
		
		final boolean dxt5 = flags2 == DDPF_FOURCC && fourCC == 0x35545844; // "DXT5"
		final boolean uncompressed = flags2 == ( DDPF_ALPHAPIXELS | DDPF_RGB );
		
		if ( uncompressed ) // If uncompressed, only support INT_ARGB format
			if ( rgbBitCount != 32 || aBitMask != 0xff000000 || rBitMask != 0xff0000 || gBitMask != 0xff00 || bBitMask != 0xff )
				throw new RuntimeException( "Unsupported DDS compression format!" );
		if ( !dxt5 && !uncompressed )
			throw new RuntimeException( "Unsupported DDS compression format!" );
		
		@SuppressWarnings( "unused" )
		final int caps = w.getInt(); // Specifies the complexity of the surfaces stored.
		@SuppressWarnings( "unused" )
		final int caps2 = w.getInt(); // Additional detail about the surfaces stored.
		w.getInt(); // Caps 3; unused
		w.getInt(); // Caps 4; unused
		w.getInt(); // Reserved
		// END OF DDS_HEADER
		
		
		// Pixel data
		
		final BufferedImage bi = new BufferedImage( width, height, BufferedImage.TYPE_INT_ARGB );
		
		if ( uncompressed ) {
			for ( int y = 0; y < height; y++ )
				for ( int x = 0; x < width; x++ )
					bi.setRGB( x, y, w.getInt() );
			
			return bi;
		}
		
		// DXT5 compressed blocks
		
		// Array to store the calculated alpha values of a 4x4 pixel block
		final int[][] alphaBlock = new int[ 4 ][ 4 ];
		
		for ( int y = 0; y < height; y += 4 )
			for ( int x = 0; x < width; x += 4 ) {
				// 16 bytes data contains colors of a 4x4 block (16 pixels)
				// First 8 bytes is the compressed alpha image data
				// Followed by 8 bytes of RGB image data.
				
				// Decode the compressed alpha image data
				// Read the 8 bytes
				final int alpha0 = w.get() & 0xff;
				final int alpha1 = w.get() & 0xff;
				long bits = ( w.get() & 0xffL ) | ( ( w.get() & 0xffL ) << 8 ) | ( ( w.get() & 0xffL ) << 16 ) | ( ( w.get() & 0xffL ) << 24 )
				        | ( ( w.get() & 0xffL ) << 32 ) | ( ( w.get() & 0xffL ) << 40 );
				
				for ( int by = 0; by < 4; by++ )
					for ( int bx = 0; bx < 4; bx++ ) {
						final int code = (int) ( bits & 0x07 );
						bits >>= 3;
						
						if ( code == 0 )
							alphaBlock[ by ][ bx ] = alpha0;
						else if ( code == 1 )
							alphaBlock[ by ][ bx ] = alpha1;
						else if ( alpha0 > alpha1 ) {
							if ( code == 2 )
								alphaBlock[ by ][ bx ] = ( 6 * alpha0 + 1 * alpha1 ) / 7;
							else if ( code == 3 )
								alphaBlock[ by ][ bx ] = ( 5 * alpha0 + 2 * alpha1 ) / 7;
							else if ( code == 4 )
								alphaBlock[ by ][ bx ] = ( 4 * alpha0 + 3 * alpha1 ) / 7;
							else if ( code == 5 )
								alphaBlock[ by ][ bx ] = ( 3 * alpha0 + 4 * alpha1 ) / 7;
							else if ( code == 6 )
								alphaBlock[ by ][ bx ] = ( 2 * alpha0 + 5 * alpha1 ) / 7;
							else
								alphaBlock[ by ][ bx ] = ( 1 * alpha0 + 6 * alpha1 ) / 7; // code == 7
						} else {
							if ( code == 2 )
								alphaBlock[ by ][ bx ] = ( 4 * alpha0 + 1 * alpha1 ) / 5;
							else if ( code == 3 )
								alphaBlock[ by ][ bx ] = ( 3 * alpha0 + 2 * alpha1 ) / 5;
							else if ( code == 4 )
								alphaBlock[ by ][ bx ] = ( 2 * alpha0 + 3 * alpha1 ) / 5;
							else if ( code == 5 )
								alphaBlock[ by ][ bx ] = ( 1 * alpha0 + 4 * alpha1 ) / 5;
							else if ( code == 6 )
								alphaBlock[ by ][ bx ] = 0;
							else
								alphaBlock[ by ][ bx ] = 255; // code == 7
						}
					}
				
				// Now decode the RGB image data, and since we have all pixel info, set the ARGB pixel right away
				// Read the 8 bytes
				final int color0 = ( w.get() & 0xff ) | ( ( w.get() & 0xff ) << 8 );
				final int color1 = ( w.get() & 0xff ) | ( ( w.get() & 0xff ) << 8 );
				int bits2 = ( w.get() & 0xff ) | ( ( w.get() & 0xff ) << 8 ) | ( ( w.get() & 0xff ) << 16 ) | ( ( w.get() & 0xff ) << 24 );
				
				// color0 and color1 are 16-bit rgb values, extract rgb components:
				final int r0 = ( color0 & 0xf800 ) >> 8; // red
				final int g0 = ( color0 & 0x07e0 ) >> 3; // green
				final int b0 = ( color0 & 0x001f ) << 3; // blue
				final int r1 = ( color1 & 0xf800 ) >> 8; // red
				final int g1 = ( color1 & 0x07e0 ) >> 3; // green
				final int b1 = ( color1 & 0x001f ) << 3; // blue
				
				for ( int by = 0; by < 4; by++ )
					for ( int bx = 0; bx < 4; bx++ ) {
						final int code = bits2 & 0x3;
						bits2 >>= 2;
						int r, g, b;
						
						if ( code == 0 ) {
							r = r0;
							g = g0;
							b = b0;
						} else if ( code == 1 ) {
							r = r1;
							g = g1;
							b = b1;
						} else if ( code == 2 ) {
							r = ( ( r0 << 1 ) + r1 ) / 3;
							g = ( ( g0 << 1 ) + g1 ) / 3;
							b = ( ( b0 << 1 ) + b1 ) / 3;
						} else { // code == 3
							r = ( r0 + ( r1 << 1 ) ) / 3;
							g = ( g0 + ( g1 << 1 ) ) / 3;
							b = ( b0 + ( b1 << 1 ) ) / 3;
						}
						
						bi.setRGB( x + bx, y + by, ( alphaBlock[ by ][ bx ] << 24 ) | ( r << 16 ) | ( g << 8 ) | b );
					}
			}
		
		return bi;
	}
	
}
