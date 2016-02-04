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
 * New replay listener.
 * 
 * @author Andras Belicza
 * 
 * @see IRepFolderMonitor#addNewRepListener(INewRepListener)
 * @see IRepFolderMonitor#removeNewRepListener(INewRepListener)
 * @see INewRepEvent
 */
public interface INewRepListener {
	
	/**
	 * This method is called when a new replay is detected in a monitored Replay Folder.
	 * 
	 * @param event details of the detected new replay
	 */
	void newRepDetected( INewRepEvent event );
	
}
