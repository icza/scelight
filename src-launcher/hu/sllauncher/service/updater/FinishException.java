/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllauncher.service.updater;

/**
 * Exception to signal the Updater should finish and return.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class FinishException extends RuntimeException {
	
	/** Tells if restart is required. */
	public final boolean restartRequired;
	
	/**
	 * Creates a new {@link FinishException}.
	 */
	public FinishException() {
		this( false );
	}
	
	/**
	 * Creates a new {@link FinishException}.
	 * 
	 * @param restartRequired tells if restart is required
	 */
	public FinishException( final boolean restartRequired ) {
		this.restartRequired = restartRequired;
	}
	
}
