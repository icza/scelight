/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.sc2.rep.model.gameevents.camera;

import hu.scelight.gui.icon.Icons;
import hu.scelight.sc2.rep.s2prot.Event;
import hu.scelight.service.env.Env;
import hu.scelightapi.sc2.rep.model.gameevents.camera.ICameraUpdateEvent;
import hu.scelightapi.sc2.rep.repproc.IRepProcessor;
import hu.sllauncher.gui.icon.LRIcon;

import java.util.Map;

/**
 * Camera update game event.
 * 
 * @author Andras Belicza
 */
public class CameraUpdateEvent extends Event implements ICameraUpdateEvent {
	
	/** Target point of the camera. */
	private TargetPoint targetPoint;
	
	/**
	 * Creates a new {@link CameraUpdateEvent}.
	 * 
	 * @param struct event data structure
	 * @param id id of the event
	 * @param name type name of the event
	 * @param loop game loop when the event occurred
	 * @param userId user id causing the event
	 */
	public CameraUpdateEvent( final Map< String, Object > struct, final int id, final String name, final int loop, final int userId ) {
		super( struct, id, name, loop, userId );
	}
	
	@Override
	public Integer getDistance() {
		return get( F_DISTANCE );
	}
	
	@Override
	public Float getDistanceFloat() {
		final Integer distance = get( F_DISTANCE );
		return distance == null ? null : distance / 256.0f;
	}
	
	@Override
	public Integer getPitch() {
		return get( F_PITCH );
	}
	
	@Override
	public Float getPitchDegree() {
		final Integer pitch = get( F_PITCH );
		return pitch == null ? null : ( 45 * ( ( ( ( ( ( pitch << 5 ) - 0x2000 ) << 17 ) - 1 ) >> 17 ) + 1 ) ) / 4096.0f;
	}
	
	@Override
	public Integer getYaw() {
		return get( F_YAW );
	}
	
	@Override
	public Float getYawDegree() {
		final Integer yaw = get( F_YAW );
		return yaw == null ? null : ( 45 * ( ( ( ( ( ( yaw << 5 ) - 0x2000 ) << 17 ) - 1 ) >> 17 ) + 1 ) ) / 4096.0f;
	}
	
	@Override
	public Map< String, Object > getTarget() {
		return get( F_TARGET );
	}
	
	@Override
	public TargetPoint getTargetPoint() {
		if ( targetPoint == null ) {
			final Map< String, Object > target = getTarget();
			if ( target != null )
				targetPoint = new TargetPoint( target );
		}
		
		return targetPoint;
	}
	
	@Override
	public Integer getReason() {
		return get( F_REASON );
	}
	
	@Override
	public String getParameters( final IRepProcessor repProc ) {
		final StringBuilder sb = new StringBuilder();
		
		Float f;
		
		getTargetPoint();
		if ( targetPoint != null ) {
			f = targetPoint.getXFloat();
			if ( f != null )
				sb.append( "x=" ).append( Env.LANG.formatNumber( f, 3 ) );
			
			f = targetPoint.getYFloat();
			if ( f != null ) {
				if ( sb.length() > 0 )
					sb.append( "; " );
				sb.append( "y=" ).append( Env.LANG.formatNumber( f, 3 ) );
			}
		}
		
		f = getDistanceFloat();
		if ( f != null ) {
			if ( sb.length() > 0 )
				sb.append( "; " );
			sb.append( "distance=" ).append( Env.LANG.formatNumber( f, 3 ) );
		}
		
		f = getPitchDegree();
		if ( f != null ) {
			if ( sb.length() > 0 )
				sb.append( "; " );
			sb.append( "pitch=" ).append( Env.LANG.formatNumber( f, 3 ) ).append( '°' );
		}
		
		f = getYawDegree();
		if ( f != null ) {
			if ( sb.length() > 0 )
				sb.append( "; " );
			sb.append( "yaw=" ).append( Env.LANG.formatNumber( f, 3 ) ).append( '°' );
		}
		
		final Integer reason = getReason();
		if ( reason != null ) {
			if ( sb.length() > 0 )
				sb.append( "; " );
			sb.append( "reason=" ).append( reason );
		}
		
		return sb.toString();
	}
	
	@Override
	public LRIcon getRicon() {
		return Icons.F_CAMERA_LENS;
	}
	
}
