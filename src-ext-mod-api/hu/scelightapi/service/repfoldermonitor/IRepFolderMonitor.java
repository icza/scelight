/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapi.service.repfoldermonitor;

/**
 * Replay Folder monitor interface.
 * 
 * @author Andras Belicza
 * 
 * @see INewRepListener
 */
public interface IRepFolderMonitor {
	
	/**
	 * Adds an {@link INewRepListener} which will be called when a new replay is detected in a monitored Replay Folder.
	 * 
	 * @param listener listener to be added
	 * 
	 * @see #removeNewRepListener(INewRepListener)
	 */
	void addNewRepListener( INewRepListener listener );
	
	/**
	 * Removes an {@link INewRepListener}.
	 * 
	 * @param listener listener to be removed
	 * 
	 * @see #addNewRepListener(INewRepListener)
	 */
	void removeNewRepListener( INewRepListener listener );
	
}
