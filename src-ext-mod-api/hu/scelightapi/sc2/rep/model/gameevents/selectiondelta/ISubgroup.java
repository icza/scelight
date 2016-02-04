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

/**
 * Interface describing the selection delta.
 * 
 * @author Andras Belicza
 */
public interface ISubgroup extends IStructView {
	
	/** Unit link field name. */
	String F_UNIT_LINK               = "unitLink";
	
	/** Subgroup priority field name. */
	String F_SUBGROUP_PRIORITY       = "subgroupPriority";
	
	/** Intra subgroup priority field name. */
	String F_INTRA_SUBGROUP_PRIORITY = "intraSubgroupPriority";
	
	/** Count field name. */
	String F_COUNT                   = "count";
	
	
	/**
	 * Returns the unit link.
	 * 
	 * @return the unit link
	 */
	Integer getUnitLink();
	
	/**
	 * Returns the subgroup priority.
	 * 
	 * @return the subgroup priority
	 */
	Integer getSubgroupPriority();
	
	/**
	 * Returns the intra subgroup priority.
	 * 
	 * @return the intra subgroup priority
	 */
	Integer getIntraSubgroupPriority();
	
	/**
	 * Returns the count.
	 * 
	 * @return the count
	 */
	Integer getCount();
	
}
