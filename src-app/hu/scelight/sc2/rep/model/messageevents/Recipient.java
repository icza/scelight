/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.sc2.rep.model.messageevents;

import hu.scelightapi.sc2.rep.model.messageevents.IRecipient;

/**
 * Message recipient.
 * 
 * @author Andras Belicza
 */
public enum Recipient implements IRecipient {
	
	/** All. */
	ALL( "ALL", 0 ),
	
	/** Allies. */
	ALLIES( "ALLIES", 1 ),
	
	/** Observers. */
	OBSERVERS( "OBS", 2 ),
	
	/** Unknown - placeholder so spectators will be 4. */
	UNKNOWN( "UNK", 3 ),
	
	/** Spectators. */
	SPECTATORS( "SPEC", 4 );
	
	
	/** Text value of the recipient. */
	public final String text;
	
	/** Raw value of the recipient (as found/used in the message events stream). */
	public final int    rawValue;
	
	
	/**
	 * Creates a new {@link Recipient}.
	 * 
	 * @param text text value
	 * @param rawValue raw value of the recipient (as found/used in the message events stream)
	 */
	private Recipient( final String text, final int rawValue ) {
		this.text = text;
		this.rawValue = rawValue;
	}
	
	@Override
	public String toString() {
		return text;
	}
	
	/**
	 * Returns the recipient for the specified raw value.
	 * 
	 * @param rawValue raw value of the recipient
	 * @return the recipient for the specified raw value
	 */
	public static Recipient fromRawValue( final int rawValue ) {
		return VALUES[ rawValue ];
	}
	
	
	/** Cache of the values array. */
	public static final Recipient[] VALUES = values();
	
	
}
