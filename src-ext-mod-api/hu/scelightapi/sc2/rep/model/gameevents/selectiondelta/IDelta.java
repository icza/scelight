/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapi.sc2.rep.model.gameevents.selectiondelta;

import hu.scelightapi.util.IStructView;
import hu.scelightapi.util.type.IBitArray;
import hu.scelightapibase.util.IPair;

/**
 * Interface describing the selection delta.
 * 
 * @author Andras Belicza
 */
public interface IDelta extends IStructView {
	
	/** Subgroup index field name. */
	String F_SUBGROUP_INDEX = "subgroupIndex";
	
	/** Remove mask field name. */
	String F_REMOVE_MASK    = "removeMask";
	
	/** Add subgroups field name. */
	String F_ADD_SUBGROUPS  = "addSubgroups";
	
	/** Add unit tags field name. */
	String F_ADD_UNIT_TAGS  = "addUnitTags";
	
	
	/**
	 * Returns the subgroup index.
	 * 
	 * @return the subgroup index
	 */
	Integer getSubgroupIndex();
	
	/**
	 * Returns the remove mask.
	 * 
	 * <p>
	 * If the second value of the returned pair ({@link IPair#getValue2()}) is not the string <code>"None"</code>, then the first value of the returned pair (
	 * {@link IPair#getValue1()}) may have the following values:
	 * </p>
	 * <ul>
	 * <li><code>"ZeroIndices"</code> - in this case the second value is an {@link Integer} array (<code>Integer[]</code>) listing the retained indices from the
	 * current selection, units of unlisted index values are removed</li>
	 * <li><code>"OneIndices"</code> - second value is an {@link Integer} array (<code>Integer[]</code>) listing the indices that are removed</li>
	 * <li><code>"Mask"</code> - second value will be a {@link IBitArray} specifying a deselection map: units at indices where bit is set are removed,
	 * deselection count is the number of <code>1</code>s ({@link IBitArray#getOnesCount()})</li>
	 * </ul>
	 * 
	 * @return the remove mask
	 */
	IPair< String, Object > getRemoveMask();
	
	/**
	 * Returns the add subgroups.
	 * 
	 * @return the add subgroups
	 */
	ISubgroup[] getAddSubgroups();
	
	/**
	 * Returns the add unit tags.
	 * 
	 * @return the add unit tags
	 */
	Integer[] getAddUnitTags();
	
}
