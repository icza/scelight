/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllauncher.util.gui;

/**
 * A text with mnemonic.
 * 
 * <p>
 * Instances are immutable.
 * </p>
 * 
 * @author Andras Belicza
 */
public class TextWithMnemonic {
	
	/** Mnemonic indicator character in raw texts. */
	public static final char   MNEMONIC_INDICATOR_CHAR   = '_';
	
	/** Mnemonic indicator character as string. */
	public static final String MNEMONIC_INDICATOR_STRING = Character.toString( MNEMONIC_INDICATOR_CHAR );
	
	/** Text without the mnemonic indicator. */
	public final String        text;
	
	/**
	 * Mnemonic character.<br>
	 * <code>null</code> if no mnemonic for this text.
	 */
	public final Character     mnemonic;
	
	/**
	 * Creates a new {@link TextWithMnemonic}.
	 * 
	 * @param rawText raw text with mnemonic indicator included
	 */
	public TextWithMnemonic( final String rawText ) {
		final int mnemonicIndex = rawText.indexOf( MNEMONIC_INDICATOR_CHAR );
		
		if ( mnemonicIndex < 0 ) {
			text = rawText;
			mnemonic = null;
		} else {
			text = rawText.replace( MNEMONIC_INDICATOR_STRING, "" );
			mnemonic = text.charAt( mnemonicIndex );
		}
	}
	
	/**
	 * Creates a new {@link TextWithMnemonic}.
	 * 
	 * @param text text without the mnemonic indicator
	 * @param mnemonic mnemonic character
	 */
	public TextWithMnemonic( final String text, final Character mnemonic ) {
		this.text = text;
		this.mnemonic = mnemonic;
	}
	
	/**
	 * Tells if text has a mnemonic.
	 * 
	 * @return true if the text has a mnemonic; false otherwise
	 */
	public boolean hasMnemonic() {
		return mnemonic != null;
	}
	
}
