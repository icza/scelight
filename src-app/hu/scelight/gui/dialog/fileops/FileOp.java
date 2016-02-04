/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.gui.dialog.fileops;

import hu.scelight.gui.icon.Icons;
import hu.scelight.gui.icon.RIcon;

/**
 * File operations.
 * 
 * @author Andras Belicza
 */
public enum FileOp {
    
    /** Copy file operation. */
	COPY( "Copy", "C_opy", "Copied", Icons.F_BLUE_DOCUMENT_COPY ),
	
	/** Move file operation. */
	MOVE( "Move", "_Move", "Moved", Icons.F_BLUE_DOCUMENT_ARROW ),
	
	/** Pack file operation. */
	PACK( "Pack", "_Pack", "Packed", Icons.F_BLUE_DOCUMENT_ZIPPER );
	
	
	/** Text value of the file op. */
	public final String	text;
						
	/** Button text (with mnemonic indicator) of the file op. */
	public final String	buttonText;
						
	/** Text expressing "processed". */
	public final String	processedText;
						
	/** Ricon of the file op. */
	public final RIcon	ricon;
						
	/**
	 * Creates a new {@link FileOp}.
	 * 
	 * @param text text value
	 * @param buttonText button text (with mnemonic indicator) of the file op
	 * @param processedText text expressing "processed"
	 * @param ricon ricon of the file op
	 */
	private FileOp( final String text, final String buttonText, final String processedText, final RIcon ricon ) {
		this.text = text;
		this.buttonText = buttonText;
		this.processedText = processedText;
		this.ricon = ricon;
	}
	
	
	/** Cache of the values array. */
	public static final FileOp[] VALUES = values();
	
}
