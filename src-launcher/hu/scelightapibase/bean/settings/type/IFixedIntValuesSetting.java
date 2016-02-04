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
 * Fixed int values setting, the value of the setting can only be one of a predefined int values.
 * 
 * @author Andras Belicza
 * 
 * @see hu.scelightapi.gui.setting.ISettingsUtils#newFixedIntValuesSetting(String, ISetting, ISettingsGroup, ISkillLevel, String, IViewHints, Integer,
 *      Integer...)
 */
public interface IFixedIntValuesSetting extends IFixedValuesSetting< Integer > {
	
	// No additional methods
	
}
