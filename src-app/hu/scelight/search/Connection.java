/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.search;

import hu.scelight.gui.icon.Icons;
import hu.scelightapi.search.IConnection;
import hu.sllauncher.gui.icon.LRIcon;

/**
 * Logical connection between filters.
 * 
 * @author Andras Belicza
 */
public enum Connection implements IConnection {
	
	/** Logical AND. */
	AND( "<html><font color=red>AND</font></html>", Icons.F_SQL_JOIN_INNER ),
	
	/** Closing parenthesis, logical OR and opening parenthesis. */
	C_OR_O( "<html>) <font color=green><b>OR</b></font> (</html>", Icons.F_SQL_JOIN_OUTER ),
	
	/** Logical OR. */
	OR( "<html><font color=green><b>OR</b></font></html>", Icons.F_SQL_JOIN_OUTER ),
	
	/** Closing parenthesis and logical OR. */
	C_OR( "<html>) <font color=green><b>OR</b></font></html>", Icons.F_SQL_JOIN_OUTER ),
	
	/** Logical OR and opening parenthesis. */
	OR_O( "<html><font color=green><b>OR</b></font> (</html>", Icons.F_SQL_JOIN_OUTER ),
	
	/** Closing parenthesis and logical AND. */
	C_AND( "<html>) <font color=red>AND</font></html>", Icons.F_SQL_JOIN_INNER ),
	
	/** Closing parenthesis, logical AND and opening parenthesis. */
	C_AND_O( "<html>) <font color=red>AND</font> (</html>", Icons.F_SQL_JOIN_INNER ),
	
	/** Logical AND and opening parenthesis. */
	AND_O( "<html><font color=red>AND</font> (</html>", Icons.F_SQL_JOIN_INNER );
	
	
	/** Formatted string value of the connection. */
	public final String  stringValue;
	
	/** Ricon of the connection. */
	public final LRIcon  ricon;
	
	/** Tells if this connection opens a parenthesis (after). */
	public final boolean opening;
	
	/** Tells if this connection closes a parenthesis (before). */
	public final boolean closing;
	
	/** Precedence of the logical connection. Logical OR has 0, logical AND has 1. */
	public final int     precedence;
	
	
	/**
	 * Creates a new {@link Connection}.
	 * 
	 * @param stringValue formatted string value of the connection
	 * @param ricon ricon of the connection
	 */
	private Connection( final String stringValue, final LRIcon ricon ) {
		this.stringValue = stringValue;
		this.ricon = ricon;
		
		opening = name().endsWith( "_O" );
		closing = name().startsWith( "C_" );
		precedence = name().contains( "OR" ) ? 0 : 1;
	}
	
	@Override
	public boolean isOpening() {
		return opening;
	}
	
	@Override
	public boolean isClosing() {
		return closing;
	}
	
	@Override
	public int getPrecedence() {
		return precedence;
	}
	
	@Override
	public Connection removeClosing() {
		switch ( this ) {
			case C_OR :
				return OR;
			case C_OR_O :
				return OR_O;
			case C_AND :
				return AND;
			case C_AND_O :
				return AND_O;
			default :
				return this;
		}
	}
	
	@Override
	public Connection removeOpening() {
		switch ( this ) {
			case OR_O :
				return OR;
			case C_OR_O :
				return C_OR;
			case AND_O :
				return AND;
			case C_AND_O :
				return C_AND;
			default :
				return this;
		}
	}
	
	@Override
	public String toString() {
		return stringValue;
	}
	
	@Override
	public LRIcon getRicon() {
		return ricon;
	}
	
	
	/** Cache of the values array. */
	public static final Connection[] VALUES = values();
	
}
