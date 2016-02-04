/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapi.search;

import hu.scelight.search.Connection;
import hu.scelightapibase.util.IEnum;
import hu.scelightapibase.util.gui.HasRIcon;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Logical connection between filters.
 * 
 * @author Andras Belicza
 * 
 * @see IEnum
 */
public interface IConnection extends HasRIcon, IEnum {
	
	/** Logical AND. */
	IConnection         AND        = Connection.AND;
	
	/** Closing parenthesis, logical OR and opening parenthesis. */
	IConnection         C_OR_O     = Connection.C_OR_O;
	
	/** Logical OR. */
	IConnection         OR         = Connection.OR;
	
	/** Closing parenthesis and logical OR. */
	IConnection         C_OR       = Connection.C_OR;
	
	/** Logical OR and opening parenthesis. */
	IConnection         OR_O       = Connection.OR_O;
	
	/** Closing parenthesis and logical AND. */
	IConnection         C_AND      = Connection.C_AND;
	
	/** Closing parenthesis, logical AND and opening parenthesis. */
	IConnection         C_AND_O    = Connection.C_AND_O;
	
	/** Logical AND and opening parenthesis. */
	IConnection         AND_O      = Connection.AND_O;
	
	
	/** An unmodifiable list of all the connections. */
	List< IConnection > VALUE_LIST = Collections.unmodifiableList( Arrays.< IConnection > asList( Connection.VALUES ) );
	
	
	/**
	 * Tells if this connection opens a parenthesis (after).
	 * 
	 * @return true if this connection opens a parenthesis (after); false otherwise
	 */
	boolean isOpening();
	
	/**
	 * Tells if this connection closes a parenthesis (before).
	 * 
	 * @return true if this connection opens a parenthesis (after); false otherwise
	 */
	boolean isClosing();
	
	/**
	 * Returns the precedence of the logical connection.
	 * 
	 * @return the precedence of the logical connection
	 */
	int getPrecedence();
	
	
	/**
	 * Returns an {@link IConnection} which is derived from this by removing the closing parenthesis.
	 * 
	 * @return an {@link IConnection} which is derived from this by removing the closing parenthesis
	 */
	IConnection removeClosing();
	
	/**
	 * Returns an {@link IConnection} which is derived from this by removing the opening parenthesis.
	 * 
	 * @return an {@link IConnection} which is derived from this by removing the opening parenthesis
	 */
	IConnection removeOpening();
	
}
