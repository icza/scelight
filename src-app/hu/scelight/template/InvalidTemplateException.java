/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.template;

import hu.scelightapi.template.IInvalidTemplateException;

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
public class InvalidTemplateException extends IInvalidTemplateException {
	
	/** Optional template position associated with the exception (e.g. position of invalid character). */
	public final Integer position;
	
	/**
	 * Creates a new {@link InvalidTemplateException}.
	 * 
	 * @param message details to why the template is invalid (might be HTML format)
	 */
	public InvalidTemplateException( final String message ) {
		this( message, null );
	}
	
	/**
	 * Creates a new {@link InvalidTemplateException}.
	 * 
	 * @param message details to why the template is invalid (might be HTML format)
	 * @param position optional template position associated with the exception
	 */
	public InvalidTemplateException( final String message, final Integer position ) {
		super( message );
		this.position = position;
	}

	@Override
    public Integer getPosition() {
	    return position;
    }
	
}
