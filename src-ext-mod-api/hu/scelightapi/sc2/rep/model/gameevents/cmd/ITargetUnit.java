/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapi.sc2.rep.model.gameevents.cmd;

import hu.scelightapi.util.IStructView;

import java.util.Map;

/**
 * Interface describing a cmd target unit.
 * 
 * @author Andras Belicza
 */
public interface ITargetUnit extends IStructView {
	
	/** Target unit flags field name. */
	String F_TARGET_UNIT_FLAGS          = "targetUnitFlags";
	
	/** Timer field name. */
	String F_TIMER                      = "timer";
	
	/** Tag field name. */
	String F_TAG                        = "tag";
	
	/** Snapshot unit link field name. */
	String F_SNAPSHOT_UNIT_LINK         = "snapshotUnitLink";
	
	/** Snapshot control player id field name. */
	String F_SNAPSHOT_CONTROL_PLAYER_ID = "snapshotControlPlayerId";
	
	/** Snapshot upkeep player id field name. */
	String F_SNAPSHOT_UPKEEP_PLAYER_ID  = "snapshotUpkeepPlayerId";
	
	/** Snapshot point field name. */
	String F_SNAPSHOT_POINT             = "snapshotPoint";
	
	
	// In old versions (below 1.4) there is only "snapshotPlayerId" instead of "snapshotControlPlayerId"
	
	/**
	 * Snapshot player id field name.<br>
	 * In old versions (below 1.4) there is only "snapshotPlayerId" instead of "snapshotControlPlayerId".
	 */
	String F_SNAPSHOT_PLAYER_ID         = "snapshotPlayerId";
	
	
	/**
	 * Target flags just notes the visibility of the targeted unit (hidden, cloaked, fogged, visible, conjoined).
	 * 
	 * @return the target unit flags
	 */
	Integer getTargetUnitFlags();
	
	/**
	 * Timer seems to be related to whether the unit was recently visible, to do with losing targets (probably when they disappear under the fog?).
	 * 
	 * @return the timer
	 */
	Integer getTimer();
	
	/**
	 * The unit tag.
	 * 
	 * @return the unit tag
	 */
	Integer getTag();
	
	/**
	 * The snapshot unit link.
	 * 
	 * @return the snapshot unit link
	 */
	Integer getSnapshotUnitLink();
	
	/**
	 * Returns the snapshot control player id.
	 * 
	 * @return the snapshot control player id
	 */
	Integer getSnapshotControlPlayerId();
	
	/**
	 * Returnst he snapshot upkeep player id.
	 * 
	 * @return the snapshot upkeep player id
	 */
	Integer getSnapshotUpkeepPlayerId();
	
	/**
	 * Returns the snapshot point.
	 * 
	 * @return the snapshot point
	 */
	Map< String, Object > getSnapshotPoint();
	
}
