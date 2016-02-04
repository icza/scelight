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

import hu.scelight.gui.page.multirepanalyzer.MultiRepAnalyzerComp;
import hu.scelight.sc2.rep.model.details.Race;
import hu.scelight.sc2.rep.model.details.Result;
import hu.scelight.sc2.rep.model.details.Toon;
import hu.scelight.sc2.rep.model.initdata.userinitdata.League;
import hu.scelight.sc2.rep.repproc.RepProcessor;
import hu.scelight.sc2.rep.repproc.User;

/**
 * Participant (player) of a game.
 * 
 * @author Andras Belicza
 */
public class Part {
	
	/** Tells if the account (toon) is a merged main account. */
	public final boolean merged;
	
	/** Toon of the player (might be a merged toon). */
	public final Toon    toon;
	
	/** Original toon of the player. */
	public final Toon    originalToon;
	
	/** Full name of the player. */
	public final String  name;
	
	/** Team. */
	public final int     team;
	
	
	/** Duration of the play in milliseconds. */
	public final long    lengthMs;
	
	
	/** Actions for APM calculation. */
	public final long    apmActions;
	
	/** Actions for SPM calculation. */
	public final long    spmActions;
	
	/** Time for per minute calculations in milliseconds. */
	public final long    perMinMs;
	
	/** SQ for SQ calculation. */
	public final long    sq;
	
	/** Supply-capped percent in the play. */
	public final double  supplyCappedPercent;
	
	/** Last cmd loop in play, used as weight in weighted average calculations (SQ, supply-capped). */
	public final long    lastCmdLoop;
	
	
	/** Used race. */
	public final Race    race;
	
	/** Result of the play. */
	public final Result  result;
	
	/** Participant's league. */
	public final League  league;
	
	
	/**
	 * Creates a new {@link Part}.
	 * 
	 * @param user user to init from
	 * @param repProc replay processor to init from
	 * @param game reference to the {@link Game}
	 * @param multiRepAnalyzerComp reference to the Multi-replay Analyzer component
	 */
	public Part( final User user, final RepProcessor repProc, final Game game, final MultiRepAnalyzerComp multiRepAnalyzerComp ) {
		originalToon = user.getToon();
		name = user.fullName;
		team = user.slot.teamId;
		
		final Toon virtualToon = multiRepAnalyzerComp.mergedAccountsMap.get( originalToon );
		merged = virtualToon != null;
		toon = merged ? virtualToon : originalToon;
		
		lengthMs = user.leaveLoop < 0 ? game.lengthMs : repProc.loopToTime( user.leaveLoop );
		
		if ( user.lastCmdLoop < 0 ) {
			apmActions = 0;
			spmActions = 0;
			perMinMs = 0;
			sq = 0;
			supplyCappedPercent = 0;
			lastCmdLoop = 0;
		} else {
			apmActions = user.apmActions;
			spmActions = user.spmActions;
			perMinMs = repProc.loopToTime( user.lastCmdLoop - repProc.initialPerMinCalcExclTime * 16 );
			sq = user.sq;
			supplyCappedPercent = user.supplyCappedPercent;
			lastCmdLoop = user.lastCmdLoop;
		}
		
		race = user.player.race;
		
		result = user.player.getResult();
		
		league = user.uid == null ? null : user.uid.getHighestLeague();
	}
	
	/**
	 * Returns the participant's APM.
	 * 
	 * @return the participant's APM
	 */
	public int getApm() {
		return (int) ( perMinMs == 0 ? 0 : apmActions * 60_000L / perMinMs );
	}
	
	/**
	 * Returns the participant's SPM.
	 * 
	 * @return the participant's SPM
	 */
	public double getSpm() {
		return perMinMs == 0 ? 0 : spmActions * 60_000.0 / perMinMs;
	}
	
}
