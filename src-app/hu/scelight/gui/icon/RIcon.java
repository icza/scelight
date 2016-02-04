/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.gui.icon;

import hu.scelight.r.R;
import hu.sllauncher.gui.icon.LRIcon;

import java.net.URL;

/**
 * App icon resource and lazily loaded, cached image icon.
 * 
 * @author Andras Belicza
 */
public class RIcon extends LRIcon {
	
	/**
	 * Creates a new {@link RIcon}.
	 * <p>
	 * The resource locator is acquired by calling {@link R#get(String)}.
	 * </p>
	 * 
	 * @param resource icon resource
	 */
	public RIcon( final String resource ) {
		this( R.get( resource ) );
	}
	
	/**
	 * Creates a new {@link RIcon}.
	 * 
	 * @param resource icon resource
	 */
	public RIcon( final URL resource ) {
		super( resource );
	}
	
}
