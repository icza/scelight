/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllauncher.service.sound;

import hu.sllauncher.r.LR;

import java.io.InputStream;

/**
 * Launcher sound resource.
 * 
 * @author Andras Belicza
 */
public class LRSound {
	
	/** Name of the sound. */
	public final String name;
	
	/** Name of the sound resource. */
	public final String resName;
	
	/**
	 * Creates a new {@link LRSound}.
	 * <p>
	 * The resource locator is acquired by calling {@link LR#get(String)}.
	 * </p>
	 * 
	 * @param name name of the sound
	 */
	public LRSound( final String name ) {
		this.name = name;
		resName = '/' + name + ".mp3";
	}
	
	/**
	 * Returns the stream of the sound data for the specified sound theme.
	 * 
	 * @param theme sound theme whose sound to return
	 * @return the stream of the sound data for the specified sound theme
	 */
	public InputStream getStream( final SoundTheme theme ) {
		return LR.getStream( "sound/" + theme.resBase + resName );
	}
	
}
