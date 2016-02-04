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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Background job monitor service with support to register jobs listeners.
 * 
 * <p>
 * Implementation is thread-safe.
 * </p>
 * 
 * @author Andras Belicza
 */
public class Jobs {
	
	/** List of (active) jobs. */
	private final List< Job >           jobList = Collections.synchronizedList( new ArrayList< Job >() );
	
	/** Registry of jobs listeners. */
	private final PropertyChangeSupport pcs     = new PropertyChangeSupport( this );
	
	/**
	 * Adds a new job.
	 * 
	 * @param job job to be added
	 * 
	 * @see #remove(Job)
	 */
	public void add( final Job job ) {
		jobList.add( job );
		
		pcs.firePropertyChange( null, null, null );
	}
	
	/**
	 * Removes a job.
	 * 
	 * @param job job to be added
	 * 
	 * @see #add(Job)
	 */
	public void remove( final Job job ) {
		jobList.remove( job );
		
		pcs.firePropertyChange( null, null, null );
	}
	
	/**
	 * Returns the number of running jobs.
	 * 
	 * @return the number of running jobs
	 */
	public int getJobsCount() {
		return jobList.size();
	}
	
	/**
	 * Returns a copy of the job list.
	 * 
	 * @return a copy of the job list
	 */
	public List< Job > getJobList() {
		// This must by synchronized because copying is not thread safe and job list might change during that!
		synchronized ( jobList ) {
			return new ArrayList<>( jobList );
		}
	}
	
	/**
	 * Cancels all jobs.
	 */
	public void cancelJobs() {
		for ( final Job job : getJobList() )
			job.requestCancel();
	}
	
	/**
	 * Closes all jobs.
	 */
	public void closeJobs() {
		// The whole method cannot be synchronized, because jobs remove themselves when finished (and would be dead-lock).
		// Instead copy the job list and close those (getJobList() is synchronized).
		for ( final Job job : getJobList() )
			job.close();
	}
	
	/**
	 * Adds a jobs listener which will be called when a job is added or removed.
	 * 
	 * @param listener listener to be added
	 * 
	 * @see #removeListener(PropertyChangeListener)
	 */
	public void addListener( final PropertyChangeListener listener ) {
		pcs.addPropertyChangeListener( listener );
	}
	
	/**
	 * Removes a jobs listener.
	 * 
	 * @param listener listener to be removed
	 * 
	 * @see #addListener(PropertyChangeListener)
	 */
	public void removeListener( final PropertyChangeListener listener ) {
		pcs.removePropertyChangeListener( listener );
	}
	
}
