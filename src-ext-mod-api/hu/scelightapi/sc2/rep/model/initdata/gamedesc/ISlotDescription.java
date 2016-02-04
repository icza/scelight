/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapi.sc2.rep.model.initdata.gamedesc;

import hu.scelightapi.util.IStructView;

/**
 * Slot description.
 * 
 * @author Andras Belicza
 */
public interface ISlotDescription extends IStructView {
	
	// Structure field names and paths (all literal strings are interned)
	
	/** Allowed AI builds field name. */
	String F_ALLOWED_AI_BUILDS     = "allowedAIBuilds";
	
	/** Allowed colors field name. */
	String F_ALLOWED_COLORS        = "allowedColors";
	
	/** Allowed controls field name. */
	String F_ALLOWED_CONTROLS      = "allowedControls";
	
	/** Allowed difficulty field name. */
	String F_ALLOWED_DIFFICULTY    = "allowedDifficulty";
	
	/** Allowed observe types field name. */
	String F_ALLOWED_OBSERVE_TYPES = "allowedObserveTypes";
	
	/** Allowed races field name. */
	String F_ALLOWED_RACES         = "allowedRaces";
	
	
	/**
	 * @return the allowedAiBuilds
	 */
	int[] getAllowedAiBuilds();
	
	/**
	 * @return the allowedColors
	 */
	int[] getAllowedColors();
	
	/**
	 * @return the allowedControls
	 */
	int[] getAllowedControls();
	
	/**
	 * @return the allowedDifficulty
	 */
	int[] getAllowedDifficulty();
	
	/**
	 * @return the allowedObserveTypes
	 */
	int[] getAllowedObserveTypes();
	
	/**
	 * @return the allowedRaces
	 */
	int[] getAllowedRaces();
	
}
