/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllauncher.util;

import java.lang.Thread.UncaughtExceptionHandler;

import hu.sllauncher.service.env.LEnv;

/**
 * An {@link UncaughtExceptionHandler} which simply logs any uncaught exceptions.
 * 
 * @author Andras Belicza
 */
public class LoggerUncaughtExceptionHandler implements UncaughtExceptionHandler {
	
	/** Singleton instance. */
	public static final LoggerUncaughtExceptionHandler INSTANCE = new LoggerUncaughtExceptionHandler();
	
	/**
	 * Creates a new {@link LoggerUncaughtExceptionHandler}. Private so we can be a singleton class.
	 */
	private LoggerUncaughtExceptionHandler() {
	}
	
	@Override
	public void uncaughtException( final Thread thread, final Throwable t ) {
		LEnv.LOGGER.error( "Uncaught exception in thread: " + thread.getName(), t );
	}
	
}
