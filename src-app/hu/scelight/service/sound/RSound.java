/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.service.sound;

import hu.scelight.r.R;
import hu.sllauncher.service.sound.LRSound;
import hu.sllauncher.service.sound.SoundTheme;

import java.io.InputStream;

/**
 * Application sound resource.
 * 
 * @author Andras Belicza
 */
public class RSound extends LRSound {
	
	/**
	 * Creates a new {@link RSound}.
	 * <p>
	 * The resource locator is acquired by calling {@link R#get(String)}.
	 * </p>
	 * 
	 * @param name name of the sound
	 */
	public RSound( final String name ) {
		super( name );
	}
	
	/**
	 * Returns the stream of the sound data for the specified sound theme.
	 * 
	 * @param theme sound theme whose sound to return
	 * @return the stream of the sound data for the specified sound theme
	 */
	@Override
	public InputStream getStream( final SoundTheme theme ) {
		return R.getStream( "sound/" + theme.resBase + resName );
	}
	
}
