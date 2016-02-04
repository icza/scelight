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
import hu.scelightapibase.util.IControlledThread;
import hu.scelightapibase.util.gui.HasRIcon;

import java.util.Date;

/**
 * A monitored background job.
 * 
 * <p>
 * Running instances created and returned by the API appear in the Running Jobs dialog inside Scelight.
 * </p>
 * 
 * <p>
 * Example usage of {@link IJob}:
 * 
 * <pre>
 * <blockquote style='border:1px solid black'>
 * // Use an image stored next to this class as the job icon:
 * IRIcon ricon = factory.newRIcon( getClass().getResource( &quot;job-icon.png&quot; ) );
 * 
 * IJob job; // If your runnable needs the reference: must be instance attribute or else a reference wrapper
 * 
 * job = factory.newJob( &quot;Italian Job&quot;, ricon, new Runnable() {
 * 	public void run() {
 * 		System.out.println( &quot;I am a Job, doing the job.&quot; );
 * 	}
 * } );
 * 
 * // Start the job:
 * job.start();
 * </blockquote>
 * </pre>
 * 
 * </p>
 * 
 * @author Andras Belicza
 * 
 * @see hu.scelightapi.service.IFactory#newJob(String, IRIcon, Runnable)
 * @see IProgressJob
 */
public interface IJob extends IControlledThread, HasRIcon {
	
	/**
	 * Sets the callback to be called when job is done.
	 * 
	 * @param callback callback to be set
	 */
	void setCallback( Runnable callback );
	
	/**
	 * Sets the callback to be called in the EDT when job is done.
	 * 
	 * @param edtCallback EDT callback to be set
	 */
	void setEdtCallback( Runnable edtCallback );
	
	/**
	 * Returns the job execution start time.
	 * 
	 * @return the job execution start time
	 */
	Date getStartedAt();
	
	/**
	 * Overridden to return the job's name.
	 * 
	 * @return the job's name
	 */
	@Override
	String toString();
	
}
