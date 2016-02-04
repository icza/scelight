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
import hu.scelight.bean.repfolders.RepFoldersBean;
import hu.scelight.gui.icon.Icons;
import hu.scelight.service.env.Env;
import hu.scelight.service.settings.Settings;
import hu.sllauncher.service.job.Job;

import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Coordinator of latest replay searches.
 * 
 * @author Andras Belicza
 */
public class LatestRepSearchCoordinatorJob extends Job {
	
	/** Parallel search for each replay folder. */
	private final List< LatestRepSearchJob > repSearches;
	
	/** Latest replay. */
	private Path                             latestReplay;
	
	/**
	 * Creates a new {@link LatestRepSearchCoordinatorJob}.
	 */
	public LatestRepSearchCoordinatorJob() {
		super( "Latest Replay Search Coordinator", Icons.F_BINOCULAR_ARROW );
		
		final RepFoldersBean rfsBean = Env.APP_SETTINGS.get( Settings.REPLAY_FOLDERS_BEAN );
		repSearches = new ArrayList<>( rfsBean.getReplayFolderBeanList().size() );
		
		// Scan replay folders marked as monitored
		for ( final RepFolderBean rf : rfsBean.getReplayFolderBeanList() ) {
			if ( rf.getMonitored() )
				repSearches.add( new LatestRepSearchJob( rf.getPath(), rf.getRecursive() ) );
		}
	}
	
	@Override
	public void jobRun() {
		// Launch searches parallel
		for ( final LatestRepSearchJob repSearch : repSearches )
			repSearch.start();
		
		// Wait for the searches to finish
		for ( final LatestRepSearchJob repSearch : repSearches )
			repSearch.waitToFinish();
		
		if ( !mayContinue() )
			return;
		
		// Merge results
		FileTime latestTime = FileTime.fromMillis( 0 );
		for ( final LatestRepSearchJob repSearch : repSearches ) {
			if ( repSearch.getLatestReplay() != null && repSearch.getLatestTime().compareTo( latestTime ) > 0 ) {
				latestReplay = repSearch.getLatestReplay();
				latestTime = repSearch.getLatestTime();
			}
		}
	}
	
	@Override
	public void requestCancel() {
		super.requestCancel();
		// Forward cancel request to sub-tasks
		for ( final LatestRepSearchJob repSearch : repSearches )
			repSearch.requestCancel();
	}
	
	/**
	 * Returns the latest replay.
	 * 
	 * @return the latest replay
	 */
	public Path getLatestReplay() {
		return latestReplay;
	}
	
}
