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

import hu.belicza.andras.util.type.XString;
import hu.scelight.sc2.rep.s2prot.Event;
import hu.scelightapi.sc2.rep.model.trackerevents.IBaseUnitEvent;

import java.util.Map;

/**
 * Base Unit tracker event.
 * 
 * @author Andras Belicza
 */
public class BaseUnitEvent extends Event implements IBaseUnitEvent {
	
	/** Base build of the replay being parsed. */
	protected final int baseBuild;
	
	
	/**
	 * Creates a new {@link BaseUnitEvent}.
	 * 
	 * @param struct event data structure
	 * @param id id of the event
	 * @param name type name of the event
	 * @param loop game loop when the event occurred
	 * @param userId user id causing the event
	 * @param baseBuild base build of the replay being parsed, there are resolution differences in tracker positions in different versions
	 */
	public BaseUnitEvent( final Map< String, Object > struct, final int id, final String name, final int loop, final int userId, final int baseBuild ) {
		super( struct, id, name, loop, userId );
		
		this.baseBuild = baseBuild;
	}
	
	@Override
	public Integer getUnitTagIndex() {
		return get( F_UNIT_TAG_INDEX );
	}
	
	@Override
	public Integer getUnitTagRecycle() {
		return get( F_UNIT_TAG_RECYCLE );
	}
	
	@Override
	public XString getUnitTypeName() {
		return get( F_UNIT_TYPE_NAME );
	}
	
	@Override
	public Integer getControlPlayerId() {
		return get( F_CONTROL_PLAYER_ID );
	}
	
	@Override
	public Integer getUpkeepPlayerId() {
		return get( F_UPKEEP_PLAYER_ID );
	}
	
	@Override
	public Integer getX() {
		return get( F_X );
	}
	
	@Override
	public Integer getY() {
		return get( F_Y );
	}
	
	@Override
	public Integer getXCoord() {
		// From base build 27950 (2.1 PTR) tracker positions have 1x1 cells resolution (before that it's 4x4 cell resolution)
		return baseBuild < 27950 ? this.< Integer > get( F_X ) * 4 : this.< Integer > get( F_X );
	}
	
	@Override
	public Integer getYCoord() {
		// From base build 27950 (2.1 PTR) tracker positions have 1x1 cells resolution (before that it's 4x4 cell resolution)
		return baseBuild < 27950 ? this.< Integer > get( F_Y ) * 4 : this.< Integer > get( F_Y );
	}
	
}
