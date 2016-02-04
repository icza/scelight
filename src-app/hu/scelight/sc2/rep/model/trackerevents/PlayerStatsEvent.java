/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.sc2.rep.model.trackerevents;

import hu.belicza.andras.util.StructView;
import hu.scelight.sc2.rep.s2prot.Event;
import hu.scelightapi.sc2.rep.model.trackerevents.IPlayerStatsEvent;

import java.util.Map;

/**
 * Player stats tracker event.
 * 
 * @author Andras Belicza
 */
public class PlayerStatsEvent extends Event implements IPlayerStatsEvent {
	
	/** Eagerly initialized value of the stats struct field. */
	public final StructView stats;
	
	
	/**
	 * Creates a new {@link PlayerStatsEvent}.
	 * 
	 * @param struct event data structure
	 * @param id id of the event
	 * @param name type name of the event
	 * @param loop game loop when the event occurred
	 * @param userId user id causing the event
	 */
	public PlayerStatsEvent( final Map< String, Object > struct, final int id, final String name, final int loop, final int userId ) {
		super( struct, id, name, loop, userId );
		
		stats = new StructView( this.< Map< String, Object > > get( F_STATS ) );
	}
	
	
	@Override
	public Integer getMineralsCurrent() {
		return stats.get( F_MINS_CURRENT );
	}
	
	@Override
	public Integer getGasCurrent() {
		return stats.get( F_GAS_CURRENT );
	}
	
	@Override
	public Integer getMinsCollRate() {
		return stats.get( F_MINS_COLL_RATE );
	}
	
	@Override
	public Integer getGasCollRate() {
		return stats.get( F_GAS_COLL_RATE );
	}
	
	@Override
	public Integer getWorkersActiveCount() {
		return stats.get( F_WORKERS_ACTIVE_COUNT );
	}
	
	@Override
	public Integer getMinsUsedInProgressArmy() {
		return stats.get( F_MINS_USED_IN_PROGRESS_ARMY );
	}
	
	@Override
	public Integer getMinsUsedInProgressEcon() {
		return stats.get( F_MINS_USED_IN_PROGRESS_ECON );
	}
	
	@Override
	public Integer getMinsUsedInProgressTech() {
		return stats.get( F_MINS_USED_IN_PROGRESS_TECH );
	}
	
	@Override
	public Integer getGasUsedInProgressArmy() {
		return stats.get( F_GAS_USED_IN_PROGRESS_ARMY );
	}
	
	@Override
	public Integer getGasUsedInProgressEcon() {
		return stats.get( F_GAS_USED_IN_PROGRESS_ECON );
	}
	
	@Override
	public Integer getGasUsedInProgressTech() {
		return stats.get( F_GAS_USED_IN_PROGRESS_TECH );
	}
	
	@Override
	public Integer getMinsUsedInCurrentArmy() {
		return stats.get( F_MINS_USED_IN_CURRENT_ARMY );
	}
	
	@Override
	public Integer getMinsUsedInCurrentEcon() {
		return stats.get( F_MINS_USED_IN_CURRENT_ECON );
	}
	
	@Override
	public Integer getMinsUsedInCurrentTech() {
		return stats.get( F_MINS_USED_IN_CURRENT_TECH );
	}
	
	@Override
	public Integer getGasUsedInCurrentArmy() {
		return stats.get( F_GAS_USED_IN_CURRENT_ARMY );
	}
	
	@Override
	public Integer getGasUsedInCurrentEcon() {
		return stats.get( F_GAS_USED_IN_CURRENT_ECON );
	}
	
	@Override
	public Integer getGasUsedInCurrentTech() {
		return stats.get( F_GAS_USED_IN_CURRENT_TECH );
	}
	
	@Override
	public Integer getMinsLostArmy() {
		return stats.get( F_MINS_LOST_ARMY );
	}
	
	@Override
	public Integer getMinsLostEcon() {
		return stats.get( F_MINS_LOST_ECON );
	}
	
	@Override
	public Integer getMinsLostTech() {
		return stats.get( F_MINS_LOST_TECH );
	}
	
	@Override
	public Integer getGasLostArmy() {
		return stats.get( F_GAS_LOST_ARMY );
	}
	
	@Override
	public Integer getGasLostEcon() {
		return stats.get( F_GAS_LOST_ECON );
	}
	
	@Override
	public Integer getGasLostTech() {
		return stats.get( F_GAS_LOST_TECH );
	}
	
	@Override
	public Integer getMinsKilledArmy() {
		return stats.get( F_MINS_KILLED_ARMY );
	}
	
	@Override
	public Integer getMinsKilledEcon() {
		return stats.get( F_MINS_KILLED_ECON );
	}
	
	@Override
	public Integer getMinsKilledTech() {
		return stats.get( F_MINS_KILLED_TECH );
	}
	
	@Override
	public Integer getGasKilledArmy() {
		return stats.get( F_GAS_KILLED_ARMY );
	}
	
	@Override
	public Integer getGasKilledEcon() {
		return stats.get( F_GAS_KILLED_ECON );
	}
	
	@Override
	public Integer getGasKilledTech() {
		return stats.get( F_GAS_KILLED_TECH );
	}
	
	@Override
	public Integer getFoodUsed() {
		return stats.get( F_FOOD_USED );
	}
	
	@Override
	public Integer getFoodMade() {
		return stats.get( F_FOOD_MADE );
	}
	
	@Override
	public Integer getMinsUsedActiveForces() {
		return stats.get( F_MINS_USED_ACTIVE_FORCES );
	}
	
	@Override
	public Integer getGasUsedActiveForces() {
		return stats.get( F_GAS_USED_ACTIVE_FORCES );
	}
	
}
