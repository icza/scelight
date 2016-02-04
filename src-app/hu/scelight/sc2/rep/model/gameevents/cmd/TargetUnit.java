/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.sc2.rep.model.gameevents.cmd;

import hu.belicza.andras.util.StructView;
import hu.scelightapi.sc2.rep.model.gameevents.cmd.ITargetUnit;

import java.util.Map;

/**
 * Data structure describing a target unit.
 * 
 * @author Andras Belicza
 */
public class TargetUnit extends StructView implements ITargetUnit {
	
	/**
	 * Creates a new {@link TargetUnit}.
	 * 
	 * @param struct data structure
	 */
	public TargetUnit( final Map< String, Object > struct ) {
		super( struct );
		
		// In old versions (below 1.4) there is only "snapshotPlayerId" instead of "snapshotControlPlayerId" and
		// "snapshotUpkeepPlayerId"
		if ( get( F_SNAPSHOT_CONTROL_PLAYER_ID ) == null ) {
			final Integer playerId = get( F_SNAPSHOT_PLAYER_ID );
			struct.put( F_SNAPSHOT_CONTROL_PLAYER_ID, playerId );
			struct.put( F_SNAPSHOT_UPKEEP_PLAYER_ID, playerId );
		}
	}
	
	@Override
	public Integer getTargetUnitFlags() {
		return get( F_TARGET_UNIT_FLAGS );
	}
	
	@Override
	public Integer getTimer() {
		return get( F_TIMER );
	}
	
	@Override
	public Integer getTag() {
		return get( F_TAG );
	}
	
	@Override
	public Integer getSnapshotUnitLink() {
		return get( F_SNAPSHOT_UNIT_LINK );
	}
	
	@Override
	public Integer getSnapshotControlPlayerId() {
		return get( F_SNAPSHOT_CONTROL_PLAYER_ID );
	}
	
	@Override
	public Integer getSnapshotUpkeepPlayerId() {
		return get( F_SNAPSHOT_UPKEEP_PLAYER_ID );
	}
	
	@Override
	public Map< String, Object > getSnapshotPoint() {
		return get( F_SNAPSHOT_POINT );
	}
	
}
