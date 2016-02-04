/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.sc2.rep.repproc;

import hu.scelight.sc2.rep.s2prot.type.Field;
import hu.scelightapi.sc2.balancedata.IBalanceData;
import hu.scelightapi.sc2.balancedata.model.IUnit;
import hu.scelightapi.sc2.rep.model.gameevents.IControlGroupUpdateEvent;
import hu.scelightapi.sc2.rep.model.gameevents.cmd.ITagTransformation;
import hu.scelightapi.sc2.rep.model.gameevents.selectiondelta.IDelta;
import hu.scelightapi.sc2.rep.model.gameevents.selectiondelta.ISelectionDeltaEvent;
import hu.scelightapi.sc2.rep.model.gameevents.selectiondelta.ISubgroup;
import hu.scelightapi.sc2.rep.repproc.IRepProcessor;
import hu.scelightapi.sc2.rep.repproc.ISelectionTracker;
import hu.scelightapi.util.type.IBitArray;
import hu.scelightapibase.util.IPair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Player selection tracker.
 * 
 * @author Andras Belicza
 */
public class SelectionTracker implements ISelectionTracker {
	
	// This selection tracker is similar to sc2reader's selection tracker found here:
	// https://github.com/GraylinKim/sc2reader/blob/master/sc2reader/engine/plugins/selection.py
	
	/**
	 * Selections (list of unit links and unit tags) for the 10 control groups and an extra one for the active selection (active selection is referenced as
	 * <code>controlGroupId=10</code> in replays).<br>
	 * A selection is a list of arrays holding the link and tag of a unit. Multiple units of the same type are listed multiple times properly. Order is
	 * important.
	 */
	@SuppressWarnings( "unchecked" )
	public final List< Integer[] >[] cgSelections = new List[ 11 ];
	
	/** Reference to the active selection (which is the last element of {@link #cgSelections}). */
	public final List< Integer[] >   activeSelection;
	
	
	/**
	 * Creates a new {@link SelectionTracker}.
	 */
	public SelectionTracker() {
		for ( int i = cgSelections.length - 1; i >= 0; i-- )
			cgSelections[ i ] = new ArrayList<>();
		
		activeSelection = cgSelections[ cgSelections.length - 1 ];
	}
	
	
	@Override
	public List< Integer[] >[] getCgSelections() {
		return cgSelections;
	}
	
	@Override
	public List< Integer[] > getActiveSelection() {
		return activeSelection;
	}
	
	@Override
	public void processSelectionDelta( final ISelectionDeltaEvent event ) {
		final List< Integer[] > selection = cgSelections[ event.getControlGroupId() ];
		final IDelta delta = event.getDelta();
		
		// First handle deselection
		processRemoveMask( delta.getRemoveMask(), selection );
		
		// Now handle adding to the selection
		final ISubgroup[] addSubgroups = delta.getAddSubgroups();
		if ( addSubgroups.length > 0 ) {
			final Integer[] addUnitTags = delta.getAddUnitTags();
			int tagCounter = 0;
			for ( int i = 0; i < addSubgroups.length; i++ ) {
				// Units of the same type
				final Integer unitLink = addSubgroups[ i ].getUnitLink();
				for ( int j = addSubgroups[ i ].getCount() - 1; j >= 0; j-- )
					selection.add( new Integer[] { unitLink, addUnitTags[ tagCounter++ ] } );
			}
		}
	}
	
	@SuppressWarnings( "unchecked" )
	@Override
	public void processControlGroupUpdate( final IControlGroupUpdateEvent event ) {
		final List< Integer[] > selection = cgSelections[ event.getGroupIndex() ];
		
		final Object maskObject = event.getMask();
		
		switch ( event.getGroupUpdate() ) {
			case 0 : // Assign
				selection.clear();
				selection.addAll( activeSelection );
				if ( maskObject instanceof IPair ) // Can only be true if maskObject is not null
					processRemoveMask( (IPair< String, Object >) maskObject, selection );
				else if ( maskObject instanceof Map ) // Can only be true if maskObject is not null
					processDeselectionMap( (IBitArray) ( (Map< ?, ? >) maskObject ).get( Field.NAME_PARENT ), selection );
				break;
			
			case 1 : // Assign (add selection)
				if ( maskObject instanceof IPair ) // Can only be true if maskObject is not null
					processRemoveMask( (IPair< String, Object >) maskObject, selection );
				else if ( maskObject instanceof Map ) // Can only be true if maskObject is not null
					processDeselectionMap( (IBitArray) ( (Map< ?, ? >) maskObject ).get( Field.NAME_PARENT ), selection );
				// IMPORTANT: only add units which are not yet added! Example
				// "Select control group 1" followed by "Assign to control group 1 (add selection)" would double the selection, but should remain the same!
				outerCycle:
				for ( final Integer[] unit : activeSelection ) {
					final int unitTag = unit[ 1 ];
					for ( final Integer[] unit2 : selection )
						if ( unitTag == unit2[ 1 ] )
							continue outerCycle; // Unit is already in control group, do not add it again (do not duplicate)
					selection.add( unit );
				}
				break;
			
			case 2 : // Select
				activeSelection.clear();
				activeSelection.addAll( selection );
				if ( maskObject instanceof IPair ) // Can only be true if maskObject is not null
					processRemoveMask( (IPair< String, Object >) maskObject, activeSelection );
				else if ( maskObject instanceof Map ) // Can only be true if maskObject is not null
					processDeselectionMap( (IBitArray) ( (Map< ?, ? >) maskObject ).get( Field.NAME_PARENT ), activeSelection );
				break;
		
		}
	}
	
	/**
	 * Processes the specified remove mask on the specified selection.
	 * 
	 * @param removeMask remove mask specifying deselection instructions
	 * @param selection selection to apply the remove mask on
	 */
	private static void processRemoveMask( final IPair< String, Object > removeMask, final List< Integer[] > selection ) {
		if ( removeMask == null )
			return;
		
		switch ( removeMask.getValue1() ) {
			case "ZeroIndices" : {
				// Retained indices are listed here
				final Integer[] indices = (Integer[]) removeMask.getValue2(); // Note: this array is sorted
				if ( indices.length == 0 )
					selection.clear(); // Remove all
				else {
					// Retain (indices specify which units remain selected
					final List< Integer[] > retained = new ArrayList<>( indices.length );
					for ( final int index : indices )
						if ( index < selection.size() )
							retained.add( selection.get( index ) );
					selection.clear();
					selection.addAll( retained );
				}
				break;
			}
			case "OneIndices" : {
				// Removed indices are listed here
				final Integer[] indices = (Integer[]) removeMask.getValue2(); // Note: this array is sorted
				// intValue() inside the cycle is required because elements are Integer and else it would be interpreted as value and not as index!
				for ( int i = indices.length - 1; i >= 0; i-- ) { // Since we're modifying the list, we have to go downward!
					final int index = indices[ i ];
					if ( index < selection.size() )
						selection.remove( index );
				}
				break;
			}
			case "Mask" : {
				processDeselectionMap( (IBitArray) removeMask.getValue2(), selection );
				break;
			}
			case "None" :
				break; // Nothing to do
		}
	}
	
	/**
	 * Processes the specified deselection map on the specified selection. The deselection map is a bit array where the position of bits set to 1 are unit
	 * indices to be deselected (removed).
	 * 
	 * @param mask deselection map in the form of a bit array
	 * @param selection selection to apply the deselection map on
	 */
	private static void processDeselectionMap( final IBitArray mask, final List< Integer[] > selection ) {
		if ( mask != null )
			for ( int i = Math.min( selection.size() - 1, mask.getCount() - 1 ); i >= 0; i-- )
				// Since we're modifying the list, we have to go downward!
				if ( mask.getBit( i ) )
					selection.remove( i );
	}
	
	@Override
	public String getSelectionString( final int controlGroupIndex, final IRepProcessor repProc ) {
		return getSelectionString( cgSelections[ controlGroupIndex ], repProc );
	}
	
	@Override
	public String getActiveSelectionString( final IRepProcessor repProc ) {
		return getSelectionString( activeSelection, repProc );
	}
	
	/**
	 * Returns an HTML formatted string representation of the specified selection. <br>
	 * The returned string might contain HTML formatting elements but does not start with the <code>&lt;html&gt;</code> tag.
	 * 
	 * @param selection selection to be converted to string
	 * @param repProc reference to the rep processor in case additional info is required (e.g. unit names)
	 * @return a string representation of the specified selection
	 */
	public static String getSelectionString( final List< Integer[] > selection, final IRepProcessor repProc ) {
		final IBalanceData balanceData = repProc.getReplay().getBalanceData();
		
		final ITagTransformation tagTransformation = repProc.getTagTransformation();
		
		final StringBuilder sb = new StringBuilder();
		
		final int size = selection.size();
		for ( int i = 0; i < size; ) {
			// In the string representation only list once units of the same type being neighbors.
			
			final Integer unitLink = selection.get( i )[ 0 ];
			int count = 1;
			for ( int j = i + 1; j < size; j++ )
				if ( unitLink.equals( selection.get( j )[ 0 ] ) ) // No automatic out-boxing, must use either equals() or intValue()!
					count++;
				else
					break;
			
			if ( sb.length() > 0 )
				sb.append( "<br>" );
			
			final IUnit unit = balanceData == null ? null : balanceData.getUnit( unitLink );
			if ( unit == null )
				sb.append( " Unknown " ).append( unitLink );
			else
				sb.append( unit.getText() );
			if ( count > 1 )
				sb.append( " x" ).append( count );
			
			// Now append unit ids
			sb.append( " <span style='color:#888888'>(" );
			for ( int j = 0; j < count; j++ ) {
				if ( j > 0 )
					sb.append( ", " ); // Use comma and space not just a comma because this will be used in tool tip so allow line breaking if too long.
				sb.append( tagTransformation.tagToString( selection.get( i + j )[ 1 ] ) );
			}
			sb.append( ")</span>" );
			
			i += count;
		}
		
		return sb.toString();
	}
	
}
