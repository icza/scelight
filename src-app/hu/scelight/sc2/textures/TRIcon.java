/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.sc2.textures;

import hu.belicza.andras.dds.DdsParser;
import hu.scelight.r.R;
import hu.scelight.service.env.Env;
import hu.scelight.util.Utils;
import hu.sllauncher.gui.icon.LRIcon;
import hu.slsc2textures.r.TR;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Set;

import javax.swing.ImageIcon;

/**
 * An SC2 texture icon resource and lazily loaded, cached image icon.
 * 
 * @author Andras Belicza
 */
public class TRIcon extends LRIcon {
	
	/**
	 * Set of texture names which are too light and are very hard to see on the default Nimbus gray background. <br>
	 * These will be "darkened".
	 */
	public static final Set< String > LIGHT_TEXTURE_SET = Utils.asNewSet( "btn-command-attack.dds", "btn-command-cancel.dds", "btn-command-holdposition.dds",
	                                                            "btn-command-move.dds", "btn-command-patrol.dds", "btn-command-returncargo.dds",
	                                                            "btn-command-stop.dds", "btn-ability-terran-holdFire.dds",
	                                                            "btn-ability-terran-weaponsfree.dds", "btn-missing-kaeo.dds", "btn-ability-terran-land.dds",
	                                                            "btn-ability-terran-liftoff.dds", "btn-ability-protoss-gather.dds",
	                                                            "btn-ability-terran-gather.dds", "btn-ability-zerg-gather.dds",
	                                                            "btn-ability-terran-setrallypoint.dds", "btn-ability-zerg-setworkerrallypoint.dds",
	                                                            "btn-ability-terran-load.dds", "btn-ability-terran-unloadall.dds",
	                                                            "btn-ability-terran-repair.dds", "btn-ability-terran-salvage.dds" );
	
	
	/**
	 * Creates a new {@link TRIcon}.
	 * <p>
	 * The resource locator is acquired by calling {@link R#get(String)}.
	 * </p>
	 * 
	 * @param resource icon resource
	 */
	public TRIcon( final String resource ) {
		this( TR.get( resource ) );
	}
	
	/**
	 * Creates a new {@link TRIcon}.
	 * 
	 * @param resource icon resource
	 */
	public TRIcon( final URL resource ) {
		super( resource );
	}
	
	/**
	 * Creates the image icon by parsing the DDS source file.
	 */
	@Override
	protected ImageIcon createImageIcon() {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream( 5904 );
		
		try ( final InputStream in = resource.openStream() ) {
			
			int data;
			while ( ( data = in.read() ) != -1 )
				baos.write( data );
			
		} catch ( final IOException ie ) {
			Env.LOGGER.error( "Failed to read texture: " + resource.getFile(), ie );
			return null;
		}
		
		final BufferedImage bimg = DdsParser.parseDDS( baos.toByteArray() );
		
		String name = resource.getFile();
		name = name.substring( name.lastIndexOf( '/' ) + 1 );
		if ( LIGHT_TEXTURE_SET.contains( name ) ) {
			// This texture is too light. Make it darker.
			for ( int y = bimg.getHeight() - 1; y >= 0; y-- )
				for ( int x = bimg.getWidth() - 1; x >= 0; x-- ) {
					final int rgb = bimg.getRGB( x, y );
					bimg.setRGB( x, y, ( rgb & 0xff000000 ) | ( ( rgb & 0xfe0000 ) >> 1 ) | ( ( rgb & 0xfe00 ) >> 1 ) | ( ( rgb & 0xfe ) >> 1 ) );
				}
		}
		
		return new ImageIcon( bimg );
	}
	
}
