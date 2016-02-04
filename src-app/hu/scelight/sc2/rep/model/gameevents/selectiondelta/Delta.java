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

import hu.belicza.andras.util.StructView;
import hu.scelightapi.sc2.rep.model.gameevents.selectiondelta.IDelta;
import hu.sllauncher.util.Pair;

import java.util.Map;

/**
 * Data structure describing the selection delta.
 * 
 * @author Andras Belicza
 */
public class Delta extends StructView implements IDelta {
	
	/** The add subgroups. */
	private Subgroup[] addSubGroups;
	
	/**
	 * Creates a new {@link Delta}.
	 * 
	 * @param struct data structure
	 */
	public Delta( final Map< String, Object > struct ) {
		super( struct );
	}
	
	@Override
	public Integer getSubgroupIndex() {
		return get( F_SUBGROUP_INDEX );
	}
	
	@Override
	public Pair< String, Object > getRemoveMask() {
		return get( F_REMOVE_MASK );
	}
	
	@Override
	public Subgroup[] getAddSubgroups() {
		if ( addSubGroups == null ) {
			final Map< String, Object >[] structs = get( F_ADD_SUBGROUPS );
			addSubGroups = new Subgroup[ structs.length ];
			for ( int i = structs.length - 1; i >= 0; i-- )
				addSubGroups[ i ] = new Subgroup( structs[ i ] );
		}
		
		return addSubGroups;
	}
	
	@Override
	public Integer[] getAddUnitTags() {
		return get( F_ADD_UNIT_TAGS );
	}
	
}
