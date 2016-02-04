/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapibase.service.job;

import hu.scelightapibase.gui.icon.IRIcon;

/**
 * An {@link IJob} which publishes its progress.
 * 
 * @author Andras Belicza
 * 
 * @see hu.scelightapi.service.IFactory#newJob(String, IRIcon, Runnable)
 * @see IJob
 */
public interface IProgressJob extends IJob {
	
	/**
	 * Returns the maximum progress value. A negative value indicates indeterminate status.
	 * 
	 * @return the maximum progress value; a negative value indicates indeterminate status
	 * 
	 * @see #setMaximumProgress(int)
	 * @see #getCurrentProgress()
	 */
	int getMaximumProgress();
	
	/**
	 * Sets the maximum progress value. A negative value indicates indeterminate status.
	 * 
	 * @param maxValue max progress value to be set
	 * 
	 * @see #getMaximumProgress()
	 */
	void setMaximumProgress( int maxValue );
	
	/**
	 * Returns the current progress value.
	 * 
	 * @return the current progress value
	 * 
	 * @see #setCurrentProgress(int)
	 * @see #getMaximumProgress()
	 */
	int getCurrentProgress();
	
	/**
	 * Sets the current progress value.
	 * 
	 * @param currentValue current progress value to be set
	 * 
	 * @see #getCurrentProgress()
	 */
	void setCurrentProgress( int currentValue );
	
}
