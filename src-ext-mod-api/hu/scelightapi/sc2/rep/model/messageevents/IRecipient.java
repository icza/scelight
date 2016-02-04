/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapi.sc2.rep.model.messageevents;

import hu.scelight.sc2.rep.model.messageevents.Recipient;
import hu.scelightapibase.util.IEnum;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Message recipient.
 * 
 * @author Andras Belicza
 * 
 * @see IEnum
 */
public interface IRecipient extends IEnum {
	
	/** All. */
	IRecipient         ALL        = Recipient.ALL;
	
	/** Allies. */
	IRecipient         ALLIES     = Recipient.ALLIES;
	
	/** Observers. */
	IRecipient         OBSERVERS  = Recipient.OBSERVERS;
	
	/** Unknown - placeholder so spectators will be 4. */
	IRecipient         UNKNOWN    = Recipient.UNKNOWN;
	
	/** Spectators. */
	IRecipient         SPECTATORS = Recipient.SPECTATORS;
	
	
	/** An unmodifiable list of all the recipients. */
	List< IRecipient > VALUE_LIST = Collections.unmodifiableList( Arrays.< IRecipient > asList( Recipient.VALUES ) );
	
}
