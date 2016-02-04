/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapi.template;

/**
 * Indicates that a name template is invalid.
 * 
 * <p>
 * The message provided by this exception might be HTML code.
 * </p>
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public abstract class IInvalidTemplateException extends IllegalArgumentException {
	
	/**
	 * Creates a new {@link IInvalidTemplateException}.
	 * 
	 * @param message details to why the template is invalid (might be HTML format)
	 */
	public IInvalidTemplateException( final String message ) {
		super( message );
	}
	
	/**
	 * Returns the optional template position associated with the exception (e.g. position of invalid character).
	 * 
	 * @return the optional template position associated with the exception (e.g. position of invalid character)
	 */
	public abstract Integer getPosition();
	
}
