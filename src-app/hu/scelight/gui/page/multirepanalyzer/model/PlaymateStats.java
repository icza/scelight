/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.gui.page.multirepanalyzer.model;

import hu.sllauncher.gui.comp.table.renderer.ProgressBarView;


/**
 * Describes / models stats of a Playmate. Contains aggregate stats.
 * 
 * @author Andras Belicza
 */
public class PlaymateStats extends PlayerStats {
	
	/** Record with the playmate. */
	public final Record recordWith = new Record();
	
	/** Record vs the playmate. */
	public final Record recordVs   = new Record();
	
	
	/**
	 * Creates a new {@link PlaymateStats}.
	 * 
	 * @param playmatePart playmate participant to init from
	 */
	public PlaymateStats( final Part playmatePart ) {
		super( playmatePart );
	}
	
	/**
	 * Updates the player stats with the specified {@link Part} and {@link Game}.
	 * 
	 * @param part participant
	 * @param playmatePart playmate participant
	 * @param game game
	 */
	public void updateWithPartGame( final Part part, final Part playmatePart, final Game game ) {
		// Time spent playing together:
		updateWithPartGame( part, game, Math.min( part.lengthMs, playmatePart.lengthMs ) );
		
		( part.team == playmatePart.team ? recordWith : recordVs ).updateWithResult( part.result );
	}
	
	/**
	 * Returns the plays with progress bar.
	 * 
	 * @param maxPlaysWith max plays with
	 * @return the plays with progress bar
	 */
	public ProgressBarView getPlaysWithBar( final int maxPlaysWith ) {
		return new ProgressBarView( recordWith.getAll(), maxPlaysWith );
	}
	
	/**
	 * Returns the plays vs progress bar.
	 * 
	 * @param maxPlaysVs max plays vs
	 * @return the plays vs progress bar
	 */
	public ProgressBarView getPlaysVsBar( final int maxPlaysVs ) {
		return new ProgressBarView( recordVs.getAll(), maxPlaysVs );
	}
	
}
