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

import hu.scelightapibase.service.job.IJob;

/**
 * A controlled thread interface with helper methods to pause/unpause or stop it.
 * 
 * <p>
 * Key methods to control a {@link IControlledThread}:
 * </p>
 * <ul>
 * <li>{@link #requestPause()} to pause it
 * <li>{@link #requestUnpause()} to unpause it
 * <li>{@link #requestCancel()} to cancel it
 * <li>{@link #waitToFinish()} to wait for the thread to end
 * <li>{@link #close()} to cancel the thread and wait for it to end
 * </ul>
 * 
 * <p>
 * 3 examples of proper usage of {@link IControlledThread}s:
 * 
 * <pre>
 * <blockquote style='border:1px solid black'>
 * IControlledThread ct; // If your runnable needs the reference: must be instance attribute or else a reference wrapper
 * 
 * // The simplest and clearest:
 * ct = factory.newControlledThread( &quot;Example #1&quot;, new Runnable() {
 * 	public void run() {
 * 		while ( ct.mayContinue() &amp;&amp; !done() )
 * 			doSomeWork();
 * 	}
 * } );
 * 
 * // A lower lever usage/implementation:
 * ct = factory.newControlledThread( &quot;Example #2&quot;, new Runnable() {
 * 	public void run() {
 * 		while ( !ct.isCancelRequested() &amp;&amp; !done() ) {
 * 			if ( ct.waitIfPaused() )
 * 				continue; // If execution was paused, &quot;continue&quot; so cancellation will be checked again first
 * 				
 * 			doSomeWork();
 * 		}
 * 	}
 * } );
 * 
 * // Usage/implementation when task is to walk a file tree (Files.walkFileTree()):
 * ct = factory.newControlledThread( &quot;Example #3&quot;, new Runnable() {
 * 	public void run() {
 * 		try {
 * 			Files.walkFileTree( folder, new SimpleFileVisitor&lt; Path &gt;() {
 * 				&#064;Override
 * 				public FileVisitResult visitFile( Path file, BasicFileAttributes attrs ) throws IOException {
 * 					if ( !ct.mayContinue() )
 * 						return FileVisitResult.TERMINATE;
 * 					
 * 					doSomethingWithFile( file );
 * 					
 * 					return FileVisitResult.CONTINUE;
 * 				}
 * 			} );
 * 		} catch ( IOException ie ) {
 * 			ie.printStackTrace();
 * 		}
 * 	}
 * } );
 * 
 * // To start the controlled thread in each case:
 * ct.start();
 * </blockquote>
 * </pre>
 * 
 * </p>
 * 
 * @author Andras Belicza
 * 
 * @see hu.scelightapi.service.IFactory#newControlledThread(String, Runnable)
 * @see IJob
 */
public interface IControlledThread extends INormalThread {
	
	/**
	 * Requests canceling of the execution of the thread.
	 */
	void requestCancel();
	
	/**
	 * Requests pausing of the execution of the thread.
	 */
	void requestPause();
	
	/**
	 * Requests unpausing of the execution of the thread from a paused state.
	 */
	void requestUnpause();
	
	/**
	 * Tells whether a cancel has been requested.
	 * 
	 * <p>
	 * {@link IControlledThread} users are responsible to periodically check this whether they are allowed to continue their work or they have to return in
	 * order to end the thread.
	 * </p>
	 * 
	 * @return true if a cancel has been requested; false otherwise
	 */
	boolean isCancelRequested();
	
	/**
	 * Tells whether a pause has been requested.
	 * 
	 * <p>
	 * {@link IControlledThread} users are responsible to periodically check this whether they are allowed to continue their work or they have to wait for
	 * either an unpause or cancel request.
	 * </p>
	 * 
	 * @return true if a pause has been requested; false otherwise
	 */
	boolean isPauseRequested();
	
	/**
	 * Returns the controlled state of the thread.
	 * 
	 * @return the controlled state of the thread
	 */
	IControlledState getControlledState();
	
	/**
	 * If execution is paused, this method will block the thread until the execution is unpaused or cancelled.
	 * 
	 * <p>
	 * This method should be called when the executing thread wants to wait while the execution is paused, because this method properly sets the controlled
	 * state to {@link IControlledState#PAUSED} while waiting, and also properly handles execution times regarding wait time counting toward the paused time.
	 * Time waited inside this method is properly excluded from execution time and is included in the paused time ({@link #getPausedTimeMs()}).<br>
	 * If the executing thread implementation waits in some other way (e.g. using {@link Thread#sleep(long)}, those are included in the execution time.
	 * </p>
	 * 
	 * <p>
	 * WARNING! This method can only be called from the job's executing thread!
	 * </p>
	 * 
	 * @return true if the execution was paused and thread was blocked for some period of time; false otherwise
	 * 
	 * @see #mayContinue()
	 * @see #guestMayContinue()
	 */
	boolean waitIfPaused();
	
	/**
	 * Returns true if the thread is allowed to continue.
	 * 
	 * <p>
	 * Besides checking and returning the inversion of {@link #isCancelRequested()} this method also waits if pause is requested (but returns false if cancel is
	 * requested while in the paused state).
	 * </p>
	 * 
	 * <p>
	 * WARNING! This method can only be called from the job's executing thread!
	 * </p>
	 * 
	 * @return true if the thread is allowed to continue; false if the thread must terminate execution.
	 * 
	 * @see #waitIfPaused()
	 * @see #guestMayContinue()
	 */
	boolean mayContinue();
	
	/**
	 * If job's execution is paused, this method will block the caller thread until the execution is unpaused or cancelled.
	 * 
	 * <p>
	 * This method should only be called from <i>guest</i> threads, meaning threads other than the job's executing thread.
	 * </p>
	 * 
	 * <p>
	 * Purpose of this method is for example if the job spawns more threads and they want to respect the owner job's paused state.
	 * </p>
	 * 
	 * <p>
	 * WARNING! This method is not for the job's executing thread!
	 * </p>
	 * 
	 * @return true if the job's execution was paused and the caller thread was blocked for some period of time; false otherwise
	 * 
	 * @see #guestMayContinue()
	 */
	boolean guestWaitIfPaused();
	
	/**
	 * Returns true if the job's thread is allowed to continue.
	 * 
	 * <p>
	 * Besides checking whether job was requested to be cancelled this method also waits if job pause is requested (but returns false if cancel is requested
	 * while in the paused state).
	 * </p>
	 * 
	 * <p>
	 * Purpose of this method is for example if the job spawns more threads and they want to respect the owner job's paused state.
	 * </p>
	 * 
	 * <p>
	 * WARNING! This method is not for the job's executing thread!
	 * </p>
	 * 
	 * @return true if the job is allowed to continue; false if the job must terminate execution.
	 * 
	 * @see #guestWaitIfPaused()
	 */
	boolean guestMayContinue();
	
	/**
	 * Returns the time spent waiting in paused state.
	 * 
	 * @return the time spent waiting in paused state
	 */
	long getPausedTimeMs();
	
	/**
	 * Returns the execution time in ms.
	 * 
	 * @return the execution time in ms
	 */
	long getExecTimeMs();
	
	/**
	 * Properly closes this thread and waits for it to die.
	 * <p>
	 * First calls {@link #requestCancel()} and then waits for this thread to die by calling {@link #waitToFinish()}.
	 * </p>
	 */
	void close();
	
}
