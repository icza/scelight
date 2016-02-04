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
import hu.scelightapi.sc2.rep.model.gameevents.cmd.ITargetPoint;

import java.util.Map;

/**
 * Data structure describing a cmd target point.
 * 
 * @author Andras Belicza
 */
public class TargetPoint extends StructView implements ITargetPoint {
	
	/**
	 * Creates a new {@link TargetPoint}.
	 * 
	 * @param struct data structure
	 */
	public TargetPoint( final Map< String, Object > struct ) {
		super( struct );
	}
	
	@Override
	public Integer getX() {
		return get( F_X );
	}
	
	@Override
	public float getXFloat() {
		final Integer x = get( F_X );
		return x == null ? null : x / 8192.0f;
	}
	
	@Override
	public Integer getY() {
		return get( F_Y );
	}
	
	@Override
	public float getYFloat() {
		final Integer y = get( F_Y );
		return y == null ? null : y / 8192.0f;
	}
	
	@Override
	public Integer getZ() {
		return get( F_Z );
	}
	
	@Override
	public float getZFloat() {
		final Integer z = get( F_Z );
		return z == null ? null : z / 8192.0f;
	}
	
}
