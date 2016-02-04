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

import hu.scelightapibase.util.IControlledThread;
import hu.sllauncher.service.env.LEnv;

/**
 * A controlled thread with helper methods to pause/unpause or stop it.
 * 
 * <p>
 * Key methods to control a {@link ControlledThread}:
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
 * 3 examples of proper {@link ControlledThread#customRun()} implementations in subclasses:
 * 
 * <pre>
 * <blockquote style='border:1px solid black'>
 * // The simplest and clearest:
 * public void customRun() {
 * 	while ( mayContinue() &amp;&amp; !done() )
 * 		doSomeWork();
 * }
 * 
 * // A lower lever usage/implementation:
 * public void customRun() {
 * 	while ( !cancelRequested &amp;&amp; !done() ) {
 * 		if ( waitIfPaused() )
 * 			continue; // If execution was paused, &quot;continue&quot; so cancellation will be checked again first
 * 			
 * 		doSomeWork();
 * 	}
 * }
 * 
 * // Usage/implementation when task is to walk a file tree (Files.walkFileTree()):
 * public void customRun() {
 * 	try {
 * 		Files.walkFileTree( folder, new SimpleFileVisitor&lt; Path &gt;() {
 * 			&#064;Override
 * 			public FileVisitResult visitFile( Path file, BasicFileAttributes attrs ) throws IOException {
 * 				if ( !mayContinue() )
 * 					return FileVisitResult.TERMINATE;
 * 				
 * 				doSomethingWithFile( file );
 * 				
 * 				return FileVisitResult.CONTINUE;
 * 			}
 * 		} );
 * 	} catch ( IOException ie ) {
 * 		ie.printStackTrace();
 * 	}
 * }
 * </blockquote>
 * </pre>
 * 
 * </p>
 * 
 * @author Andras Belicza
 */
public abstract class ControlledThread extends NormalThread implements IControlledThread {
	
	/**
	 * Tells if a request has been made to cancel the execution of the thread.
	 * 
	 * <p>
	 * The subclasses are responsible to periodically check this variable in their {@link #customRun()} method whether they are allowed to continue their work
	 * or they have to return in order to end the thread.
	 * </p>
	 */
	protected volatile boolean       cancelRequested;
	
	/**
	 * Tells if a request has been made to pause the execution of the thread.
	 * 
	 * <p>
	 * The subclasses are responsible to periodically check this variable in their {@link #customRun()} method whether they are allowed to continue their work
	 * or they have to wait for either an unpause or cancel request.
	 * </p>
	 */
	protected volatile boolean       pauseRequested;
	
	/** Controlled state of the thread. */
	private volatile ControlledState controlledState;
	
	/** Lock object to be owned for changing the controlled state. */
	private final Object             STATE_LOCK = new Object();
	
	/** Execution start time. */
	protected long                   execStartTime;
	
	/** Execution end time. */
	protected long                   execEndTime;
	
	/** Time spent waiting in paused state. */
	protected long                   pausedTimeMs;
	
	/**
	 * Creates a new ControlledThread.
	 * 
	 * @param name name of the thread
	 */
	public ControlledThread( final String name ) {
		super( name );
		
		controlledState = ControlledState.NEW;
	}
	
	@Override
	public final void run() {
		synchronized ( STATE_LOCK ) {
			controlledState = ControlledState.EXECUTING;
		}
		execStartTime = System.currentTimeMillis();
		
		try {
			customRun();
		} catch ( final Throwable t ) {
			LEnv.LOGGER.error( "Uncaught exception, prematurely ended thread: " + getName(), t );
		}
		
		execEndTime = System.currentTimeMillis();
		synchronized ( STATE_LOCK ) {
			controlledState = ControlledState.ENDED;
		}
	}
	
	/**
	 * Custom run method to do the work.
	 */
	public abstract void customRun();
	
	@Override
	public void requestCancel() {
		// Volatile variables are synchronized internally, so no need external synchronization here.
		cancelRequested = true;
		
		updateControlledState();
	}
	
	@Override
	public void requestPause() {
		if ( pauseRequested )
			return; // Pause already requested
			
		// Volatile variables are synchronized internally, so no need external synchronization here.
		pauseRequested = true;
		
		updateControlledState();
	}
	
	@Override
	public void requestUnpause() {
		if ( !pauseRequested )
			return; // Pause not requested currently
			
		// Volatile variables are synchronized internally, so no need external synchronization here.
		pauseRequested = false;
		
		updateControlledState();
	}
	
	/**
	 * Updates the controlled state.
	 */
	private void updateControlledState() {
		synchronized ( STATE_LOCK ) {
			if ( controlledState == ControlledState.ENDED )
				return;
			
			if ( cancelRequested ) {
				controlledState = ControlledState.EXECUTING_CANCEL_REQUESTED;
				return;
			}
			
			if ( controlledState != ControlledState.PAUSED && pauseRequested ) {
				controlledState = ControlledState.EXECUTING_PAUSE_REQUESTED;
				return;
			}
			
			controlledState = ControlledState.EXECUTING;
		}
	}
	
	@Override
	public boolean isCancelRequested() {
		return cancelRequested;
	}
	
	@Override
	public boolean isPauseRequested() {
		return cancelRequested;
	}
	
	@Override
	public ControlledState getControlledState() {
		return controlledState;
	}
	
	/**
	 * If execution is paused, this method will block the thread until the execution is unpaused or cancelled.
	 * 
	 * <p>
	 * This method should be called when a subclass wants to wait while the execution is paused, because this method properly sets the controlled state to
	 * {@link ControlledState#PAUSED} while waiting, and also properly handles execution times regarding wait time counting toward the paused time. Time waited
	 * inside this method is properly excluded from execution time and is included in the paused time ({@link #pausedTimeMs}).<br>
	 * If a subclass implementation waits in some other way (e.g. using {@link Thread#sleep(long)}, those are included in the execution time.
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
	@Override
	public boolean waitIfPaused() {
		if ( !pauseRequested )
			return false;
		
		synchronized ( STATE_LOCK ) {
			controlledState = ControlledState.PAUSED;
		}
		
		// Store waiting start time which will cause the execution time calculated properly,
		// because the time we wait here is only added at the end to the paused time,
		// but due to this it will properly be excluded from the execution time.
		execEndTime = System.currentTimeMillis();
		
		while ( !cancelRequested && pauseRequested && checkedSleep( 10 ) == null )
			;
		
		pausedTimeMs += System.currentTimeMillis() - execEndTime;
		
		// Restore that execution has not yet been ended
		execEndTime = 0;
		
		synchronized ( STATE_LOCK ) {
			controlledState = cancelRequested ? ControlledState.EXECUTING_CANCEL_REQUESTED : ControlledState.EXECUTING;
		}
		
		return true;
	}
	
	/**
	 * Returns true if the thread is allowed to continue.
	 * 
	 * <p>
	 * Besides checking and returning the inversion of {@link #cancelRequested} property this method also waits if pause is requested (but returns false if
	 * cancel is requested while in the paused state).
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
	@Override
	public boolean mayContinue() {
		// Cycle to wait out pause but abort if cancelled:
		while ( !cancelRequested && waitIfPaused() )
			;
		
		return !cancelRequested;
	}
	
	@Override
	public boolean guestWaitIfPaused() {
		if ( !pauseRequested )
			return false;
		
		while ( !cancelRequested && pauseRequested && checkedSleep( 10 ) == null )
			;
		
		return true;
	}
	
	@Override
	public boolean guestMayContinue() {
		// Cycle to wait out pause but abort if cancelled:
		while ( !cancelRequested && guestWaitIfPaused() )
			;
		
		return !cancelRequested;
	}
	
	@Override
	public long getPausedTimeMs() {
		return pausedTimeMs;
	}
	
	@Override
	public long getExecTimeMs() {
		final long end = execEndTime == 0 ? System.currentTimeMillis() : execEndTime;
		return end - execStartTime - pausedTimeMs;
	}
	
	@Override
	public void close() {
		requestCancel();
		waitToFinish();
	}
	
}
