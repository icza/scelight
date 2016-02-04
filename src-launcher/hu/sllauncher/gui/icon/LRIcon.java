/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllauncher.gui.icon;

import hu.scelightapibase.gui.icon.IRIcon;
import hu.sllauncher.gui.comp.XLabel;
import hu.sllauncher.r.LR;
import hu.sllauncher.service.env.LEnv;
import hu.sllauncher.service.settings.LSettings;
import hu.sllauncher.util.ResourceHolder;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.RenderingHints;
import java.net.URL;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.GrayFilter;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * Launcher icon resource and lazily loaded, cached image icon.
 * 
 * @author Andras Belicza
 */
public class LRIcon extends ResourceHolder implements IRIcon {
	
	/** Lazily loaded image icon. */
	protected ImageIcon icon;
	
	/** Lazily created grayed version of the icon's image. */
	protected Image     grayedImage;
	
	/** The string value of icons originating from this instance. */
	protected String    stringValue = "I";
	
	/**
	 * Creates a new {@link LRIcon}.
	 * <p>
	 * The resource locator is acquired by calling {@link LR#get(String)}.
	 * </p>
	 * 
	 * @param resource icon resource
	 */
	public LRIcon( final String resource ) {
		this( LR.get( resource ) );
	}
	
	/**
	 * Creates a new {@link LRIcon}.
	 * 
	 * @param resource icon resource
	 */
	public LRIcon( final URL resource ) {
		super( resource );
	}
	
	/**
	 * Sets the string value of the icons originating from this instance.<br>
	 * 
	 * It will be the return value of {@link Object#toString()} of the icons originating from this instance ({@link #get()} and {@link #size(int)} for example).
	 * 
	 * @param stringValue the string value of the icons originating from this instance
	 */
	public void setStringValue( final String stringValue ) {
		this.stringValue = stringValue;
	}
	
	@Override
	public ImageIcon get() {
		return get( true );
	}
	
	@Override
	public ImageIcon get( final boolean cache ) {
		return icon != null ? icon : cache ? icon = createImageIcon() : createImageIcon();
	}
	
	/**
	 * Creates the image icon specified by the {@link ResourceHolder#resource resource}.
	 * 
	 * <p>
	 * This implementation simply passes the {@link ResourceHolder#resource resource} to {@link ImageIcon#ImageIcon(URL)}. Subclasses may override this to load the
	 * image differently.
	 * </p>
	 * 
	 * @return the created image icon.
	 */
	protected ImageIcon createImageIcon() {
		return new ImageIcon( resource );
	}
	
	/** {@link MediaTracker} instance used to monitor images (image transformations). */
	private static final MediaTracker  MEDIA_TRACKER = new MediaTracker( new XLabel() );
	
	/** Unique ID generator for the {@link #MEDIA_TRACKER}. */
	private static final AtomicInteger ID_GENERATOR  = new AtomicInteger();
	
	@Override
	public Image getGrayedImage() {
		return getGrayedImage( true );
	}
	
	@Override
	public Image getGrayedImage( final boolean cache ) {
		Image grayedImage = this.grayedImage;
		if ( grayedImage == null ) {
			grayedImage = GrayFilter.createDisabledImage( get( cache ).getImage() );
			if ( cache )
				this.grayedImage = grayedImage;
			
			// The Java image API uses asynchronous image processing, wait for the grayed image to complete
			// in order to avoid empty images being displayed!
			final int id = ID_GENERATOR.getAndIncrement();
			MEDIA_TRACKER.addImage( grayedImage, id );
			try {
				MEDIA_TRACKER.waitForID( id );
			} catch ( final InterruptedException ie ) {
				LEnv.LOGGER.warning( "Image conversion (graying) interrupted!", ie );
			} finally {
				MEDIA_TRACKER.removeImage( grayedImage, id );
			}
		}
		
		return grayedImage;
	}
	
	@Override
	public Icon size( final int height ) {
		return size( height, false, true );
	}
	
	@Override
	public Icon size( final int height, final boolean grayed, final boolean cache ) {
		return new Icon() {
			final Image img   = grayed ? getGrayedImage( cache ) : get( cache ).getImage();
			
			int         width = -1;                                                        // Indicate unknown / not yet calculated
			                                                                                
			@Override
			public void paintIcon( final Component c, final Graphics g, final int x, final int y ) {
				( (Graphics2D) g ).setRenderingHint( RenderingHints.KEY_INTERPOLATION,
				        LEnv.LAUNCHER_SETTINGS.get( LSettings.ICON_SCALING_QUALITY ).renderingHintValue );
				g.drawImage( img, x, y, getIconWidth(), height, null );
			}
			
			@Override
			public int getIconWidth() {
				if ( width < 0 ) {
					final int imgHeight = img.getHeight( null );
					final int imgWidth = img.getWidth( null );
					if ( imgHeight >= 0 && imgWidth >= 0 ) {
						width = imgHeight == 0 ? 0 : (int) ( imgWidth * ( (double) height / imgHeight ) );
					}
				}
				return width;
			}
			
			@Override
			public int getIconHeight() {
				return height;
			}
			
			/**
			 * Override {@link Object#toString()} so that if this ricon is used in a table, copying cells will not result in a meaningless or strange text.
			 */
			@Override
			public String toString() {
				return stringValue;
			}
		};
	}
	
	@Override
	public String getCSS() {
		return "background-image:url(" + resource + ");background-position:left center;background-repeat:no-repeat;padding-left:" + get().getIconWidth()
		        + "px;padding-top:1px;";
	}
	
	/**
	 * Overrides {@link Object#toString()} so that if this ricon is used in a table, copying cells will not result in a meaningless or strange text.<br>
	 * The returned string can be set with {@link #setStringValue(String)}.
	 * 
	 * @see #setStringValue(String)
	 */
	@Override
	public String toString() {
		return stringValue;
	}
	
}
