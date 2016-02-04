/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.sc2.rep.model.gameevents.selectiondelta;

import hu.belicza.andras.util.type.BitArray;
import hu.scelight.gui.icon.Icons;
import hu.scelight.sc2.rep.s2prot.Event;
import hu.scelight.sc2.rep.s2prot.type.Field;
import hu.scelightapi.sc2.balancedata.IBalanceData;
import hu.scelightapi.sc2.balancedata.model.IUnit;
import hu.scelightapi.sc2.rep.model.gameevents.cmd.ITagTransformation;
import hu.scelightapi.sc2.rep.model.gameevents.selectiondelta.ISelectionDeltaEvent;
import hu.scelightapi.sc2.rep.repproc.IRepProcessor;
import hu.sllauncher.gui.icon.LRIcon;
import hu.sllauncher.util.Pair;

import java.util.Map;

/**
 * Camera update game event.
 * 
 * @author Andras Belicza
 */
public class SelectionDeltaEvent extends Event implements ISelectionDeltaEvent {
	
	/** Old path to get the remove mask. */
	private static final String[] P_OLD_REMOVE_MASK = { "delta", Delta.F_REMOVE_MASK, Field.NAME_PARENT };
	
	
	/** Selection delta. */
	private Delta                 delta;
	
	
	/**
	 * Creates a new {@link SelectionDeltaEvent}.
	 * 
	 * @param struct event data structure
	 * @param id id of the event
	 * @param name type name of the event
	 * @param loop game loop when the event occurred
	 * @param userId user id causing the event
	 * @param baseBuild base build of the replay being parsed, there are structural differences in event structures in different versions
	 */
	public SelectionDeltaEvent( final Map< String, Object > struct, final int id, final String name, final int loop, final int userId, final int baseBuild ) {
		super( struct, id, name, loop, userId );
		
		// In the first retail version (1.0) removeMask is a structure holding always the deselection bitmap
		// with the key NAME_PARENT. Simulate the new remove mask;
		if ( baseBuild < 16561 ) {
			// The same key is used to set the simulated new remove mask, so it will also remove the old one.
			getDelta_().put( Delta.F_REMOVE_MASK, new Pair<>( "Mask", get( P_OLD_REMOVE_MASK ) ) );
		}
	}
	
	@Override
	public Integer getControlGroupId() {
		return get( F_GROUP_INDEX );
	}
	
	/**
	 * @return the delta struct
	 */
	private Map< String, Object > getDelta_() {
		return get( F_DELTA );
	}
	
	@Override
	public Delta getDelta() {
		if ( delta == null )
			delta = new Delta( getDelta_() );
		
		return delta;
	}
	
	@Override
	public String getParameters( final IRepProcessor repProc ) {
		final IBalanceData balanceData = repProc.getReplay().getBalanceData();
		
		final StringBuilder sb = new StringBuilder();
		
		final Integer controlGroupId = getControlGroupId();
		if ( controlGroupId != 10 )
			sb.append( "[Control group " ).append( controlGroupId ).append( "] " );
		
		if ( getDelta() == null ) // Call getDelta() to make sure delta attribute is initialized
			return sb.toString();
		
		final ITagTransformation tagTransformation = repProc.getTagTransformation();
		
		final Subgroup[] addSubgroups = delta.getAddSubgroups();
		if ( addSubgroups.length > 0 ) {
			sb.append( "Add " );
			final Integer[] addUnitTags = delta.getAddUnitTags();
			int tagCounter = 0;
			for ( int i = 0; i < addSubgroups.length; i++ ) {
				if ( i > 0 )
					sb.append( ", " );
				final int count = addSubgroups[ i ].getCount();
				final IUnit unit = balanceData == null ? null : balanceData.getUnit( addSubgroups[ i ].getUnitLink() );
				if ( unit == null )
					sb.append( " Unknown " ).append( addSubgroups[ i ].getUnitLink() );
				else
					sb.append( unit.getText() );
				if ( count > 1 )
					sb.append( " x" ).append( count );
				sb.append( " (" );
				for ( int j = 0; j < count; j++ ) {
					if ( j > 0 )
						sb.append( ',' );
					sb.append( tagTransformation.tagToString( addUnitTags[ tagCounter++ ] ) );
				}
				sb.append( ')' );
			}
		}
		
		final Pair< String, Object > removeMask = delta.getRemoveMask();
		if ( removeMask != null && !"None".equals( removeMask.value1 ) ) {
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
		
		return sb.toString();
	}
	
	@Override
	public LRIcon getRicon() {
		return Icons.F_SELECTION_SELECT;
	}
	
}
