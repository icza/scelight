/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.service.watcher;

/**
 * Info about a path being ignored.
 * 
 * @author Andras Belicza
 */
class IgnoredPathInfo {
	
	/** Date and time the path has been first added to ignore. */
	public final long firstDate = System.currentTimeMillis();
	
	/** Date and time the path has been last added to ignore. */
	public long       lastDate  = firstDate;
	
	/** Number of how many times the path has been added. */
	public int        count     = 1;
	
	/**
	 * Registers a new added event.
	 */
	public void registerAdded() {
		count++;
		lastDate = System.currentTimeMillis();
	}
	
}
