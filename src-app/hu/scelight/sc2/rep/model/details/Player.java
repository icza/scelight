/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.sc2.rep.model.details;

import hu.belicza.andras.util.StructView;
import hu.scelight.sc2.rep.model.initdata.lobbystate.Slot;
import hu.scelight.util.Utils;
import hu.scelightapi.sc2.rep.model.details.IPlayer;

import java.util.Map;

/**
 * Player.
 * 
 * @author Andras Belicza
 */
public class Player extends StructView implements IPlayer {
	
	// Eagerly initialized "cached" values
	
	/** Name. */
	public final String  name;
	
	/** Localized race. */
	public final String  raceString;
	
	/** Race. */
	public final Race    race;
	
	/** Recorded match result. */
	private final Result recordedResult;
	
	/**
	 * Team, not always accurate! Use {@link Slot#getTeamId()} instead!
	 * 
	 * @see Slot#getTeamId()
	 * */
	@Deprecated
	public final Integer team;
	
	/** Toon. */
	public final Toon    toon;
	
	/** Working set slot id. */
	public final Integer workingSetSlotId;
	
	
	// Lazily initialized "cached" values
	
	/** Color ARGB. */
	private int[]        argb;
	
	
	/** Deduced match result. */
	private Result       deducedResult;
	
	
	/**
	 * Creates a new {@link Player}.
	 * 
	 * @param struct header data structure
	 * @param playerIdx player index in the players array, used as the working set slot id if that's missing (it is in reps where base build &lt; 24764)
	 */
	public Player( final Map< String, Object > struct, final int playerIdx ) {
		super( struct );
		
		// The name contains optional formatting tags (in mark-up format).
		// Example: "[RA]<sp/>SvnthSyn"
		// Starting with 3.1, it looks like this: "<RA><sp/>SvnthSyn"
		// Starting with 3.6, it looks like this: "&lt;RA&gt;<sp/>SvnthSyn"
		// Strip these off
		name = Utils.stripOffMarkupFormatting( get( F_NAME ).toString().replace( "&lt;", "<" ).replace( "&gt;", ">" ) );
		raceString = get( F_RACE ).toString();
		race = Race.fromLocalizedValue( raceString );
		recordedResult = Result.VALUES[ (int) get( F_RESULT ) ];
		team = get( F_TEAM_ID );
		toon = new Toon( this.< Map< String, Object > > get( F_TOON ) );
		
		final Integer slotId = get( F_WORKING_SET_SLOT_ID );
		workingSetSlotId = slotId == null ? playerIdx : slotId;
	}
	
	@Override
	public int[] getArgb() {
		if ( argb == null )
			argb = new int[] { get( P_COLOR_A ), get( P_COLOR_R ), get( P_COLOR_G ), get( P_COLOR_B ) };
		
		return argb;
	}
	
	@Override
	public Integer getControl() {
		return get( F_CONTROL );
	}
	
	@Override
	public Integer getHandicap() {
		return get( F_HANDICAP );
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public Integer getObserve() {
		return get( F_OBSERVE );
	}
	
	@Override
	public String getRaceString() {
		return raceString;
	}
	
	@Override
	public Race getRace() {
		return race;
	}
	
	@Override
	public Result getRecordedResult() {
		return recordedResult;
	}
	
	@Override
	public Result getDeducedResult() {
		return deducedResult;
	}
	
	/**
	 * Sets the deduced match result of the player.
	 * 
	 * @param result deduced result to be set
	 */
	public void setDeducedResult( final Result result ) {
		this.deducedResult = result;
	}
	
	@Override
	public Result getResult() {
		return deducedResult == null ? recordedResult : deducedResult;
	}
	
	@Override
	public boolean isResultDeduced() {
		return deducedResult != null;
	}
	
	@Override
	public Integer getTeamId() {
		return team;
	}
	
	@Override
	public Toon getToon() {
		return toon;
	}
	
	@Override
	public Integer getWorkingSetSlotId() {
		return workingSetSlotId;
	}
	
}
