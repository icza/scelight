/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapibase.util;

import hu.scelightapibase.util.gui.HasRIcon;
import hu.sllauncher.util.SkillLevel;

/**
 * Computer skill level.
 * 
 * @author Andras Belicza
 * 
 * @see IEnum
 */
public interface ISkillLevel extends HasRIcon, IEnum {
	
	/** Basic skill level, satisfied with basic computer skills. */
	ISkillLevel BASIC     = SkillLevel.BASIC;
	
	/** Normal skill level, for the average / casual users. */
	ISkillLevel NORMAL    = SkillLevel.NORMAL;
	
	/** Advanced skill level, requires advanced computer skills. */
	ISkillLevel ADVANCED  = SkillLevel.ADVANCED;
	
	/** Developer skill level, requires developer computer skills. */
	ISkillLevel DEVELOPER = SkillLevel.DEVELOPER;
	
	/**
	 * Hidden. This means the setting is not displayed to the user for view/edit purposes in the settings dialog. It is displayed in the navigation tree if it
	 * is a setting node though.<br>
	 * Usually these are used/set by the code in the background, or they are displayed in a custom way.
	 */
	ISkillLevel HIDDEN    = SkillLevel.HIDDEN;
	
	/**
	 * Tests if the current skill level is at least this skill level.
	 * 
	 * @return true if the current skill level is at least this skill level (or above)
	 */
	boolean isAtLeast();
	
	/**
	 * Tests if the current skill level is at most this skill level.
	 * 
	 * @return true if the current skill level is at most this skill level (or below)
	 */
	boolean isAtMost();
	
	/**
	 * Tests if the current skill level is above this skill level.
	 * 
	 * @return true if the current skill level is above this skill level
	 */
	boolean isAbove();
	
	/**
	 * Tests if the current skill level is below this skill level.
	 * 
	 * @return true if the current skill level is below this skill level
	 */
	boolean isBelow();
	
}
