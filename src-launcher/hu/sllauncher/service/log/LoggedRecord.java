/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllauncher.service.log;

import java.util.Date;

/**
 * Info that is logged by the {@link Logger}.
 * 
 * @author Andras Belicza
 */
public class LoggedRecord {
	
	/** Time of the log record. */
	public Date     time;
	
	/** Log level. */
	public LogLevel logLevel;
	
	/** Message. */
	public String   message;
	
}
