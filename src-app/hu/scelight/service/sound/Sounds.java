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

/**
 * Application sound collection.
 * 
 * <p>
 * <i>Implementation note:</i> This is an interface instead of a a class because constants defined in an interface are implicitly <code>public</code>,
 * <code>static</code> and <code>final</code>, and so these keywords can be omitted!
 * </p>
 * 
 * @author Andras Belicza
 */
public interface Sounds {
	
	/*
	 * GB dialect:
	 * 
	 * http://translate.google.com/translate_tts?ie=UTF-8&tl=en-gb&q=Updates+available!
	 * http://translate.google.com/translate_tts?ie=UTF-8&tl=en-gb&q=Thank+You+for+registering.
	 * http://translate.google.com/translate_tts?ie=UTF-8&tl=en-gb&q=Replay+saved.
	 * http://translate.google.com/translate_tts?ie=UTF-8&tl=en-gb&q=Failed+to+save+replay!
	 * http://translate.google.com/translate_tts?ie=UTF-8&tl=en-gb&q=Game+started.
	 * 
	 * http://translate.google.com/translate_tts?ie=UTF-8&tl=en-gb&q=Game+ended.
	 * 
	 * http://translate.google.com/translate_tts?ie=UTF-8&tl=en-gb&q=Low+APM!
	 * 
	 * http://translate.google.com/translate_tts?ie=UTF-8&tl=en-gb&q=APM+OK.
	 */
	
	/** Updates available sound. */
	RSound UPDATES_AVAILABLE      = new RSound( "updates-available" );     // "Updates available!"
	                                                                        
	/** Thank You for registering sound. */
	RSound THANKS_FOR_REGISTERING = new RSound( "thanks-for-registering" ); // "Thank You for registering."
	                                                                        
	/** Replay saved sound. */
	RSound REPLAY_SAVED           = new RSound( "replay-saved" );          // "Replay saved"
	                                                                        
	/** Failed to save replay sound. */
	RSound FAILED_TO_SAVE_REPLAY  = new RSound( "failed-to-save-replay" ); // "Failed to save replay!"
	                                                                        
	/** Game started sound. */
	RSound GAME_STARTED           = new RSound( "game-started" );          // "Game started"
	                                                                        
	/** Game ended sound. */
	RSound GAME_ENDED             = new RSound( "game-ended" );            // "Game ended"
	                                                                        
	/** Low APM sound. */
	RSound LOW_APM                = new RSound( "low-apm" );               // "Low APM!"
	                                                                        
	/** APM OK sound. */
	RSound APM_OK                 = new RSound( "apm-ok" );                // "APM OK."
	                                                                        
}
