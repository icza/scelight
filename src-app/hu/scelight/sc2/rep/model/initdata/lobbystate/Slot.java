/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.sc2.rep.model.initdata.lobbystate;

import hu.belicza.andras.util.StructView;
import hu.belicza.andras.util.type.XString;
import hu.scelight.sc2.rep.model.details.Race;
import hu.scelightapi.sc2.rep.model.initdata.lobbystate.ISlot;

import java.util.Map;

/**
 * Slot.
 * 
 * @author Andras Belicza
 */
public class Slot extends StructView implements ISlot {
	
	// Eagerly initialized "cached" values
	
	/**
	 * User id.<br>
	 * Computers don't have a user id (they don't issue commands).
	 */
	public final Integer userId;
						 
	/** Team id. The real team id. */
	public final Integer teamId;
						 
						 
	// Lazily initialized "cached" values
	
	/** Player color. */
	private PlayerColor	 playerColor;
						 
	/** Chosen race. */
	private Race		 chosenRace;
						 
	/** Controller. */
	private Controller	 controller;
						 
	/** Participant role. */
	private Role		 role;
						 
						 
	/**
	 * Creates a new {@link Slot}.
	 * 
	 * @param struct slot data structure
	 */
	public Slot( final Map< String, Object > struct ) {
		super( struct );
		
		userId = get( F_USER_ID );
		teamId = get( F_TEAM_ID );
	}
	
	@Override
	public Integer getAiBuild() {
		return get( F_AI_BUILD );
	}
	
	@Override
	public Integer getColorPref() {
		return get( P_COLOR_PREF_COLOR );
	}
	
	@Override
	public Integer getControl() {
		return get( F_CONTROL );
	}
	
	@Override
	public Integer getDifficulty() {
		return get( F_DIFFICULTY );
	}
	
	@Override
	public Integer getHandicap() {
		return get( F_HANDICAP );
	}
	
	@Override
	public Integer[] getLicenses() {
		return get( F_LICENSES );
	}
	
	@Override
	public Integer getObserve() {
		return get( F_OBSERVE );
	}
	
	@Override
	public Integer getRacePref() {
		return get( P_RACE_PREF_RACE );
	}
	
	@Override
	public Long[] getRewards() {
		return get( F_REWARDS );
	}
	
	@Override
	public Integer getTeamId() {
		return teamId;
	}
	
	@Override
	public String getToonHandle() {
		final XString xs = get( F_TOON_HANDLE );
		return xs == null ? null : xs.toString();
	}
	
	@Override
	public Integer getUserId() {
		return userId;
	}
	
	@Override
	public Integer getWorkingSetSlotId() {
		return get( F_WORKING_SET_SLOT_ID );
	}
	
	@Override
	public PlayerColor getPlayerColor() {
		if ( playerColor == null )
			playerColor = PlayerColor.VALUES[ getColorPref() ];
			
		return playerColor;
	}
	
	@Override
	public Controller getController() {
		if ( controller == null )
			controller = Controller.VALUES[ getControl() ];
			
		return controller;
	}
	
	@Override
	public Race getChosenRace() {
		if ( chosenRace == null ) {
			final Integer racePref = getRacePref();
			chosenRace = racePref == null ? Race.RANDOM : Race.VALUES[ racePref ];
		}
		
		return chosenRace;
	}
	
	@Override
	public Role getRole() {
		if ( role == null )
			role = Role.VALUES[ getObserve() ];
			
		return role;
	}
	
	@Override
	public Integer getLogoIndex() {
		return get( F_LOGO_INDEX );
	}
	
	@Override
	public Integer getTandemLeaderUserId() {
		// F_TANDEM_LEADER_ID is present from 3.1.0
		// F_TANDEM_LEADER_USER_ID is present in 3.0.0 only.
		final Integer tandemLeaderId = get( F_TANDEM_LEADER_ID );
		return tandemLeaderId != null ? tandemLeaderId : this.< Integer > get( F_TANDEM_LEADER_USER_ID );
	}
	
	@Override
	public Integer getTandemId() {
		return get( F_TANDEM_ID );
	}
	
}
