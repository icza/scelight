/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.util.sc2rep;

import hu.scelight.bean.repfolders.RepFolderBean;
import hu.scelight.gui.icon.Icons;

import java.nio.file.Path;

/**
 * Replay file counter job.
 * 
 * @author Andras Belicza
 */
public class RepCounterJob extends RepCrawlerJob {
	
	/** Result of the counting. */
	private Integer count;
	
	/**
	 * Creates a new {@link RepCounterJob}.
	 * 
	 * @param repFolderBean replay folder to count in
	 */
	public RepCounterJob( final RepFolderBean repFolderBean ) {
		super( "Replay Counter: " + repFolderBean.getPath(), Icons.F_COUNTER, repFolderBean, "count" );
	}
	
	/** Primitive-type counter. */
	private int count_;
	
	@Override
	protected void onFolderMissing() {
		count = -1;
	}
	
	@Override
	protected void onFoundRepFile( final Path repFile ) {
		count_++;
	}
	
	@Override
	protected void onEnd() {
		count = cancelRequested ? null : count_;
	}
	
	/**
	 * Returns the result of the counting.
	 * 
	 * @return the replay files in the specified folder; -1 if the replay folder does not exists or is not a folder; <code>null</code> if count was aborted or
	 *         some error occurred
	 */
	public Integer getCount() {
		return count;
	}
	
}
