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
import hu.scelightapibase.service.job.IJob;
import hu.sllauncher.service.env.LEnv;
import hu.sllauncher.util.ControlledThread;
import hu.sllauncher.util.gui.LGuiUtils;

import java.util.Date;

/**
 * A monitored background job.
 * 
 * @author Andras Belicza
 */
public abstract class Job extends ControlledThread implements IJob {
	
	/** Job ricon. */
	private IRIcon   ricon;
	
	/** Job execution start time. */
	private Date     startedAt;
	
	/** Callback to be called when job is done. */
	private Runnable callback;
	
	/** Callback to be called in the EDT when job is done. */
	private Runnable edtCallback;
	
	/**
	 * Creates a new {@link Job}.
	 * 
	 * @param name name of the job
	 * @param ricon job ricon
	 */
	public Job( final String name, final IRIcon ricon ) {
		super( name );
		
		this.ricon = ricon;
	}
	
	@Override
	public void setCallback( final Runnable callback ) {
		this.callback = callback;
	}
	
	@Override
	public void setEdtCallback( final Runnable edtCallback ) {
		this.edtCallback = edtCallback;
	}
	
	/**
	 * Runs {@link #jobRun()} between registering and deregistering the job at {@link LEnv#JOBS}.
	 */
	@Override
	public final void customRun() {
		startedAt = new Date( execStartTime );
		
		LEnv.JOBS.add( this );
		
		try {
			jobRun();
		} finally {
			// Remove job even if exception is thrown
			LEnv.JOBS.remove( this );
			
			if ( callback != null )
				callback.run();
			
			if ( edtCallback != null )
				LGuiUtils.runInEDT( edtCallback );
		}
	}
	
	/**
	 * Custom run method doing the job.
	 */
	public abstract void jobRun();
	
	@Override
	public Date getStartedAt() {
		return startedAt;
	}
	
	@Override
	public IRIcon getRicon() {
		return ricon;
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
}
