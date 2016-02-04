/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.sc2.rep.model.gameevents;

import hu.belicza.andras.util.type.BitArray;
import hu.scelight.gui.icon.Icons;
import hu.scelight.sc2.rep.s2prot.Event;
import hu.scelight.sc2.rep.s2prot.type.Field;
import hu.scelightapi.sc2.rep.model.gameevents.IControlGroupUpdateEvent;
import hu.scelightapi.sc2.rep.repproc.IRepProcessor;
import hu.scelightapibase.util.IPair;
import hu.sllauncher.gui.icon.LRIcon;
import hu.sllauncher.util.Pair;

import java.util.Map;

/**
 * Control group update game event.
 * 
 * @author Andras Belicza
 */
public class ControlGroupUpdateEvent extends Event implements IControlGroupUpdateEvent {
	
	/**
	 * Creates a new {@link ControlGroupUpdateEvent}.
	 * 
	 * @param struct event data structure
	 * @param id id of the event
	 * @param name type name of the event
	 * @param loop game loop when the event occurred
	 * @param userId user id causing the event
	 */
	public ControlGroupUpdateEvent( final Map< String, Object > struct, final int id, final String name, final int loop, final int userId ) {
		super( struct, id, name, loop, userId );
	}
	
	@Override
	public Integer getGroupIndex() {
		return get( F_GROUP_INDEX );
	}
	
	@Override
	public Integer getGroupUpdate() {
		return get( F_GROUP_UPDATE );
	}
	
	@Override
	public Object getMask() {
		return get( F_MASK );
	}
	
	@Override
	public boolean isAssign() {
		return getGroupUpdate().intValue() != 2; // Either 1 or 2
	}
	
	@Override
	public boolean isSelect() {
		return getGroupUpdate().intValue() == 2;
	}
	
	@Override
	public LRIcon getRicon() {
		return Icons.F_KEYBOARD;
	}
	
	@Override
	public String getParameters( final IRepProcessor repProc ) {
		final StringBuilder sb = new StringBuilder();
		switch ( getGroupUpdate() ) {
			case 0 :
				sb.append( "Assign to control group " ).append( getGroupIndex() );
				break;
			case 1 :
				sb.append( "Assign to control group " ).append( getGroupIndex() ).append( " (add selection)" );
				break;
			case 2 :
				sb.append( "Select control group " ).append( getGroupIndex() );
				break;
		}
		
		final Object maskObject = getMask();
		
		if ( maskObject instanceof IPair ) {// Can only be true if maskObject is not null
			@SuppressWarnings( "unchecked" )
			final Pair< String, Object > removeMask = (Pair< String, Object >) maskObject;
			if ( !"None".equals( removeMask.value1 ) ) {
				if ( sb.length() > 0 )
					sb.append( "; " );
				switch ( removeMask.value1 ) {
					case "ZeroIndices" : {
						// Retained indices are listed here
						final Integer[] indices = (Integer[]) removeMask.value2; // Note: this array is sorted
						if ( indices.length == 0 )
							sb.append( "Remove all" );
						else
							sb.append( "Retain " ).append( indices.length ).append( indices.length == 1 ? " unit" : " units" );
						break;
					}
					case "OneIndices" : {
						// Removed indices are listed here
						final Integer[] indices = (Integer[]) removeMask.value2; // Note: this array is sorted
						sb.append( "Remove " ).append( indices.length ).append( indices.length == 1 ? " unit" : " units" );
						break;
					}
					case "Mask" : {
						final BitArray mask = (BitArray) removeMask.value2;
						// Mask is a deselection map, units at indices where bit is set are removed
						// deselection count is the number of "1" bits
						final int count = mask.getOnesCount();
						sb.append( "Remove " ).append( count ).append( count == 1 ? " unit" : " units" );
						break;
					}
				}
			}
		} else if ( maskObject instanceof Map ) { // Can only be true if maskObject is not null
			final BitArray mask = (BitArray) ( (Map< ?, ? >) maskObject ).get( Field.NAME_PARENT );
			if ( sb.length() > 0 )
				sb.append( "; " );
			// Mask is a deselection map, units at indices where bit is set are removed
			// deselection count is the number of "1" bits
			final int count = mask.getOnesCount();
			sb.append( "Remove " ).append( count ).append( count == 1 ? " unit" : " units" );
		} else if ( maskObject != null ) { // Only append if it has some value
			sb.append( "; " ).append( "mask=" );
			appendValue( sb, maskObject );
		}
		
		return sb.toString();
	}
	
}
