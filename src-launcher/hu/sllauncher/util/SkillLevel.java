/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllauncher.util;

import hu.scelightapibase.util.ISkillLevel;
import hu.sllauncher.gui.icon.LIcons;
import hu.sllauncher.gui.icon.LRIcon;
import hu.sllauncher.service.env.LEnv;
import hu.sllauncher.service.settings.LSettings;

/**
 * Computer skill level.
 * 
 * @author Andras Belicza
 */
public enum SkillLevel implements ISkillLevel {
	
	/** Basic skill level, satisfied with basic computer skills. */
	BASIC( "Basic", LIcons.F_FIRE_SMALL ),
	
	/** Normal skill level, for the average / casual users. */
	NORMAL( "Normal", LIcons.F_FIRE ),
	
	/** Advanced skill level, requires advanced computer skills. */
	ADVANCED( "Advanced", LIcons.F_FIRE_BIG ),
	
	/** Developer skill level, requires developer computer skills. */
	DEVELOPER( "Developer", LIcons.MY_FIRE_BIG_DEV ),
	
	/**
	 * Hidden. This means the setting is not displayed to the user for view/edit purposes in the settings dialog. It is displayed in the navigation tree if it
	 * is a setting node though.<br>
	 * Usually these are used/set by the code in the background, or they are displayed in a custom way.
	 */
	HIDDEN( "Hidden", null );
	
	
	/** Text value of the skill level. */
	public final String text;
	
	/** Ricon of the skill level. */
	public final LRIcon ricon;
	
	
	/**
	 * Creates a new {@link SkillLevel}.
	 * 
	 * @param text text value of the skill level
	 * @param ricon ricon of the race
	 */
	private SkillLevel( final String text, final LRIcon ricon ) {
		this.text = text;
		this.ricon = ricon;
	}
	
	@Override
	public LRIcon getRicon() {
		return ricon;
	}
	
	@Override
	public String toString() {
		return text;
	}
	
	
	@Override
	public boolean isAtLeast() {
		return LEnv.LAUNCHER_SETTINGS.get( LSettings.SKILL_LEVEL ).compareTo( this ) >= 0;
	}
	
	@Override
	public boolean isAtMost() {
		return LEnv.LAUNCHER_SETTINGS.get( LSettings.SKILL_LEVEL ).compareTo( this ) <= 0;
	}
	
	@Override
	public boolean isAbove() {
		return LEnv.LAUNCHER_SETTINGS.get( LSettings.SKILL_LEVEL ).compareTo( this ) > 0;
	}
	
	@Override
	public boolean isBelow() {
		return LEnv.LAUNCHER_SETTINGS.get( LSettings.SKILL_LEVEL ).compareTo( this ) < 0;
	}
	
}
