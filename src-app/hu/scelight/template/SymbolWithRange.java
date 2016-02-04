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

/**
 * A symbol with an optional symbol-specific param and with an optional value range.
 * 
 * @author Andras Belicza
 */
class SymbolWithRange {
	
	/** The symbol. */
	public final Symbol symbol;
	
	/** Symbol-specific parameter; <code>-1</code> if not specified. */
	public final int    param;
	
	/** Value range first; <code>-1</code> if range is not specified. */
	public final int    first;
	
	/** Value range last; <code>-1</code> if range is not specified. */
	public final int    last;
	
	
	/**
	 * Creates a new {@link SymbolWithRange}.
	 * 
	 * @param symbol the symbol
	 * @param param symbol-specific parameter
	 * @param first value range first; must be <code>-1</code> if range is not specified
	 * @param last value range last; must be <code>-1</code> if range is not specified
	 */
	public SymbolWithRange( final Symbol symbol, final int param, final int first, final int last ) {
		this.symbol = symbol;
		this.param = param;
		this.first = first;
		this.last = last;
	}
	
}
