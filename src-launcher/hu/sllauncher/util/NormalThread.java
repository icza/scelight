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

import hu.scelightapibase.util.INormalThread;
import hu.sllauncher.service.env.LEnv;

/**
 * An {@link INormalThread} implementation.
 * 
 * @author Andras Belicza
 * 
 * @see ControlledThread
 */
public class NormalThread extends Thread implements INormalThread {
	
	/**
	 * Creates a new {@link NormalThread}.
	 * 
	 * @param name name of the thread
	 */
	public NormalThread( final String name ) {
		super( name );
		
		setPriority( NORM_PRIORITY );
		
		setUncaughtExceptionHandler( LoggerUncaughtExceptionHandler.INSTANCE );
	}
	
	@Override
	public Thread asThread() {
		return this;
	}
	
	@Override
	public boolean waitToFinish() {
		try {
			join();
			return true;
		} catch ( final InterruptedException ie ) {
			LEnv.LOGGER.warning( "Thread interrupted!", ie );
			return false;
		}
	}
	
	@Override
	public InterruptedException checkedSleep( final long ms ) {
		try {
			sleep( ms );
			return null;
		} catch ( InterruptedException ie ) {
			LEnv.LOGGER.error( "Thread interrupted!", ie );
			return ie;
		}
	}
	
}
