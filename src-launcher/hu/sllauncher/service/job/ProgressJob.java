/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllauncher.service.job;

import hu.scelightapibase.gui.icon.IRIcon;
import hu.scelightapibase.service.job.IProgressJob;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * A {@link Job} which publishes its progress.
 * 
 * @author Andras Belicza
 */
public abstract class ProgressJob extends Job implements IProgressJob {
	
	/**
	 * Maximum progress value. A negative value indicates indeterminate status.<br>
	 * Default value is <code>-1</code> (indeterminate).
	 */
	protected final AtomicInteger maximumProgress = new AtomicInteger( -1 );
	
	/** Current progress value. */
	protected final AtomicInteger currentProgress = new AtomicInteger();
	
	
	/**
	 * Creates a new {@link ProgressJob}.
	 * 
	 * @param name name of the job
	 * @param ricon job ricon
	 */
	public ProgressJob( final String name, final IRIcon ricon ) {
		super( name, ricon );
	}
	
	@Override
	public int getMaximumProgress() {
		return maximumProgress.get();
	}
	
	@Override
	public void setMaximumProgress( final int maxValue ) {
		maximumProgress.set( maxValue );
	}
	
	@Override
	public int getCurrentProgress() {
		return currentProgress.get();
	}
	
	@Override
	public void setCurrentProgress( final int currentValue ) {
		currentProgress.set( currentValue );
	}
	
}
