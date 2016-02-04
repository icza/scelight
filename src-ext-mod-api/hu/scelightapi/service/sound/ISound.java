/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapi.service.sound;

import hu.scelightapibase.service.job.IJob;

import java.io.InputStream;
import java.nio.file.Path;

/**
 * Background sound player and sound utilities.
 * 
 * @author Andras Belicza
 */
public interface ISound {
	
	/**
	 * Beeps if beeps are allowed.
	 */
	void beep();
	
	/**
	 * Beeps if error beeps are allowed.
	 */
	void beepOnError();
	
	/**
	 * Beeps if warning beeps are allowed.
	 */
	void beepOnWarning();
	
	/**
	 * Beeps if confirmation beeps are allowed.
	 */
	void beepOnConfirmation();
	
	/**
	 * Beeps if input beeps are allowed.
	 */
	void beepOnInput();
	
	/**
	 * Beeps if input beeps are allowed.
	 */
	void beepOnInfo();
	
	/**
	 * Beeps if empty text search result beeps are allowed.
	 */
	void beepOnEmptyTxtSearchRslt();
	
	/**
	 * Plays a sound file if voice notifications are allowed.
	 * 
	 * <p>
	 * Supported file formats include <b>MP3</b>, WAV, AIFF, AU.
	 * </p>
	 * 
	 * <p>
	 * The sound will be played by a {@link IJob}.
	 * </p>
	 * 
	 * @param file sound file to be played
	 * @param wait tells if have to wait the end of the play or return immediately
	 * @return true if the file was started playing; false if error occurred
	 */
	boolean play( Path file, boolean wait );
	
	/**
	 * Plays a sound if voice notifications are allowed.
	 * 
	 * <p>
	 * Supported stream formats include data stream of <b>MP3</b>, WAV, AIFF, AU.
	 * </p>
	 * 
	 * <p>
	 * The sound will be played by an {@link IJob}. The <code>dataStream</code> will be closed at the end of play.
	 * </p>
	 * 
	 * @param name name of the sound to be played
	 * @param dataStream data stream to read the sound data from
	 * @param wait tells if have to wait the end of the play or return immediately
	 * @return true if the file was started playing; false if error occurred
	 */
	boolean play( String name, InputStream dataStream, boolean wait );
	
}
