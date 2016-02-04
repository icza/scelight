/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapibase.gui.icon;

import hu.scelightapibase.util.HasResource;

import java.awt.Image;

import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * Icon resource and lazily loaded, cached image icon.
 * 
 * @author Andras Belicza
 * 
 * @see hu.scelightapi.service.IGuiFactory#newRIcon(java.net.URL)
 */
public interface IRIcon extends HasResource {
	
	/**
	 * Returns the lazily loaded icon.
	 * <p>
	 * If the icon was loaded and cached previously, the cached instance is returned.<br>
	 * Else the icon is loaded and cached first, and returned.
	 * </p>
	 * 
	 * @return the lazily loaded icon
	 * 
	 * @see #get(boolean)
	 */
	ImageIcon get();
	
	/**
	 * Returns the lazily loaded icon.
	 * <p>
	 * If the icon was loaded and cached previously, the cached instance is returned.<br>
	 * Else the the icon will be loaded and the <code>cache</code> parameter tells if the icon is to be cached.<br>
	 * The significance of not caching is that if a big resource is only need once (or in one place), with this it can be accessed and avoided for it to remain
	 * in memory.
	 * </p>
	 * 
	 * @param cache tells if the icon is to be cached
	 * 
	 * @return the lazily loaded icon
	 * 
	 * @see #get()
	 */
	ImageIcon get( boolean cache );
	
	/**
	 * Returns a lazily created grayed version of the icon's image.
	 * 
	 * <p>
	 * It is guaranteed that when this method returns, the grayed version is ready to be rendered.
	 * </p>
	 * 
	 * @return a grayed version of the icon's image
	 * 
	 * @see #getGrayedImage(boolean)
	 */
	Image getGrayedImage();
	
	/**
	 * Returns a lazily created grayed version of the icon's image.
	 * 
	 * <p>
	 * It is guaranteed that when this method returns, the grayed version is ready to be rendered.
	 * </p>
	 * 
	 * <p>
	 * If the icon was loaded and cached previously, the cached instance is returned.<br>
	 * Else the the icon will be loaded and the <code>cache</code> parameter tells if the icon is to be cached.<br>
	 * The significance of not caching is that if a big resource is only need once (or in one place), with this it can be accessed and avoided for it to remain
	 * in memory.
	 * </p>
	 * 
	 * @param cache tells if the icon is to be cached
	 * @return a grayed version of the icon's image
	 * 
	 * @see #getGrayedImage()
	 */
	Image getGrayedImage( boolean cache );
	
	/**
	 * Returns an icon with the specified height.
	 * <p>
	 * The returned icon will have a height specified by the parameter and a width calculated to keep aspect ratio.
	 * </p>
	 * 
	 * @param height height of the icon in pixels to return
	 * @return an icon with the specified height
	 * 
	 * @see #size(int, boolean, boolean)
	 */
	Icon size( int height );
	
	/**
	 * Returns an icon with the specified height.
	 * <p>
	 * The returned icon will have a height specified by the parameter and a width calculated to keep aspect ratio.
	 * </p>
	 * 
	 * <p>
	 * If the icon was loaded and cached previously, the cached instance is returned.<br>
	 * Else the the icon will be loaded and the <code>cache</code> parameter tells if the icon is to be cached.<br>
	 * The significance of not caching is that if a big resource is only need once (or in one place), with this it can be accessed and avoided for it to remain
	 * in memory.
	 * </p>
	 * 
	 * @param height height of the icon in pixels to return
	 * @param grayed tells if the grayed version of the icon should be used (e.g. for disabled components)
	 * @param cache tells if the icon is to be cached
	 * @return an icon with the specified height
	 * 
	 * @see #size(int)
	 */
	Icon size( int height, boolean grayed, boolean cache );
	
	/**
	 * Returns a CSS style code which contains this image as a non-repeatable background image.
	 * 
	 * @return a CSS style code which contains this image as a non-repeatable background image
	 */
	String getCSS();
	
	/**
	 * Overrides {@link Object#toString()} so that if this ricon is used in a table, copying cells will not result in a meaningless or strange text.
	 */
	@Override
	String toString();
	
}
