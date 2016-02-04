/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapibase.bean.settings.type;

import hu.scelightapibase.bean.settings.type.viewhints.IViewHints;
import hu.scelightapibase.util.ISkillLevel;

/**
 * A string setting.
 * 
 * @author Andras Belicza
 * 
 * @see hu.scelightapi.gui.setting.ISettingsUtils#newStringSetting(String, ISetting, ISettingsGroup, ISkillLevel, String, IViewHints, String)
 */
public interface IStringSetting extends ISetting< String > {
	
	// No additional methods
	
}
