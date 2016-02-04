/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllauncher.service.registration;

import hu.sllauncher.gui.comp.StatusLabel.StatusType;
import hu.sllauncher.gui.help.LHelps;
import hu.sllauncher.util.LRHtml;

/**
 * Registration status.
 * 
 * @author Andras Belicza
 */
public enum RegStatus {
	
	/** Registration file not found. */
	NOT_FOUND( StatusType.WARNING, "Registration file not found.", null ),
	
	/** Registration file found but is invalid or corrupt. */
	INVALID( StatusType.HARD_WARNING, "Registration file found but it is invalid or corrupt!", LHelps.INVALID_REGISTRATION_FILE ),
	
	/** Registration file found, valid, but it has expired. */
	EXPIRED( StatusType.HARD_WARNING, "Registration file found, it is valid, but it has expired!", LHelps.REGISTRATION_FILE_EXPIRED ),
	
	/** Registration file found, valid, but system info does not match. */
	SYSINFO_MISMATCH(
	        StatusType.HARD_WARNING,
	        "Registration file found, it is valid, but your system info does not match the registered system info!",
	        LHelps.REGISTERED_SYSTEM_INFO_MISMATCH ),
	
	/** Registration file found, valid, system info matches. */
	MATCH( StatusType.SUCCESS, "OK. Registration file found, valid, system info matches.", null );
	
	
	/** Status type associated with the reg status. */
	public final StatusType statusType;
	
	/** Message associated with the reg status. */
	public final String     message;
	
	/** Help of the reg status. */
	public final LRHtml     help;
	
	
	/**
	 * Creates a new {@link RegStatus}.
	 * 
	 * @param statusType Status type associated with the reg status
	 * @param message message associated with the reg status.
	 * @param help help of the reg status
	 */
	private RegStatus( final StatusType statusType, final String message, final LRHtml help ) {
		this.statusType = statusType;
		this.message = message;
		this.help = help;
	}
	
}
