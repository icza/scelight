/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.belicza.andras.mpq;

/**
 * Exception to indicate that the specified MPQ archive is invalid or not supported.
 * 
 * @author Andras Belicza
 */
public class InvalidMpqArchiveException extends Exception {
	
	/** Generated serial version UID by Eclipse. */
	private static final long serialVersionUID = 4062384310740215030L;
	
	/**
	 * Creates a new InvalidMpqArchive.
	 * 
	 * @param message error message
	 */
	public InvalidMpqArchiveException( final String message ) {
		super( message );
	}
	
	/**
	 * Creates a new InvalidMpqArchive.
	 * 
	 * @param message error message
	 * @param cause the cause of this exception
	 */
	public InvalidMpqArchiveException( final String message, final Throwable cause ) {
		super( message, cause );
	}
	
}
