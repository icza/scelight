/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapibase.util;

/**
 * A normal thread interface with some utility methods.
 * 
 * <p>
 * Instances provided by the API extend {@link Thread} and do not take the inherited priority but instead set {@link Thread#NORM_PRIORITY} and install an
 * {@link java.lang.Thread.UncaughtExceptionHandler} which simply logs any uncaught exceptions.
 * </p>
 * 
 * <p>
 * Example usage of {@link INormalThread}:
 * 
 * <pre>
 * <blockquote style='border:1px solid black'>
 * INormalThread nt; // If your runnable needs the reference: must be instance attribute or else a reference wrapper
 * 
 * nt = factory.newNormalThread( &quot;Example&quot;, new Runnable() {
 * 	public void run() {
 * 		nt.checkedSleep( 500 );
 * 		System.out.println( &quot;I am a NormalThread.&quot; );
 * 	}
 * } );
 * 
 * // Start the normal thread:
 * nt.start();
 * 
 * System.out.println( &quot;I am not a NormalThread.&quot; );
 * // Wait for it to finish:
 * nt.waitToFinish();
 * </blockquote>
 * </pre>
 * 
 * </p>
 * 
 * @author Andras Belicza
 * 
 * @see hu.scelightapi.service.IFactory#newNormalThread(String, Runnable)
 * @see IControlledThread
 */
public interface INormalThread {
	
	/**
	 * Casts this instance to {@link Thread}.
	 * 
	 * @return <code>this</code> as a {@link Thread}
	 */
	Thread asThread();
	
	/**
	 * Waits for this thread to finish.
	 * 
	 * @return true if thread finished properly; false otherwise (interrupted)
	 */
	boolean waitToFinish();
	
	/**
	 * Sleeps for the specified amount of milliseconds.
	 * 
	 * @param ms milliseconds to sleep
	 * @return the exception that was thrown if sleep was interrupted; <code>null</code> otherwise
	 */
	InterruptedException checkedSleep( long ms );
	
	/**
	 * Starts the thread. See {@link Thread#start()} for details.
	 * 
	 * @see Thread#start()
	 */
	void start();
	
}
