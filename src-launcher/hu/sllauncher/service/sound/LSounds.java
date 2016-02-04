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

/**
 * Launcher sound collection.
 * 
 * <p>
 * <i>Implementation note:</i> This is an interface instead of a class because constants defined in an interface are implicitly <code>public</code>,
 * <code>static</code> and <code>final</code>, and so these keywords can be omitted!
 * </p>
 * 
 * @author Andras Belicza
 */
public interface LSounds {
	
	/*
	 * GB dialect:
	 * 
	 * http://translate.google.com/translate_tts?ie=UTF-8&tl=en-gb&q=Scelight<br>
	 * http://translate.google.com/translate_tts?ie=UTF-8&tl=en-gb&q=Scelight+is+ready!
	 * http://translate.google.com/translate_tts?ie=UTF-8&tl=en-gb&q=Scelight+must+be+restarted!
	 * http://translate.google.com/translate_tts?ie=UTF-8&tl=en-gb&q=Scelight+error:+No+connection!
	 * http://translate.google.com/translate_tts?ie=UTF-8&tl=en-gb&q=Scelight+error:+Update+failed!
	 * http://translate.google.com/translate_tts?ie=UTF-8&tl=en-gb&q=Please+register.
	 * http://translate.google.com/translate_tts?ie=UTF-8&tl=en-gb&q=Thank+You+for+choosing+Scelight.
	 */
	
	/** Scelight sound. */
	LRSound SCELIGHT                   = new LRSound( "scelight" );                  // "Scelight"
	                                                                                  
	/** Scelight is ready sound. */
	LRSound SCELIGHT_READY             = new LRSound( "scelight-ready" );            // "Scelight is ready!"
	                                                                                  
	/** Scelight must be restarted sound. */
	LRSound SCELIGHT_RESTART           = new LRSound( "scelight-restart" );          // "Scelight must be restarted!"
	                                                                                  
	/** Scelight error: No connection! sound. */
	LRSound SCELIGHT_ERR_NO_CONNECTION = new LRSound( "scelight-err-no-connection" ); // "Scelight error: No connection!"
	                                                                                  
	/** Scelight error: Update failed! sound. */
	LRSound SCELIGHT_ERR_UPDATE_FAILED = new LRSound( "scelight-err-update-failed" ); // "Scelight error: Update failed!"
	                                                                                  
	/** Please register sound. */
	LRSound PLEASE_REGISTER            = new LRSound( "please-register" );           // "Please register."
	                                                                                  
	/** Thank You for choosing Scelight sound. */
	LRSound SCELIGHT_THANK_YOU         = new LRSound( "scelight-thank-you" );        // "Thank You for choosing Scelight."
	                                                                                  
}
