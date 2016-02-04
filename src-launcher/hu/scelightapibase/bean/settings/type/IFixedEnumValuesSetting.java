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
 * Fixed enum values setting, the value of the setting can only be one of a predefined enum values.
 * 
 * @param <T> setting value type; it has to be an enum type
 * 
 * @author Andras Belicza
 * 
 * @see hu.scelightapi.gui.setting.ISettingsUtils#newFixedEnumValuesSetting(String, ISetting, ISettingsGroup, ISkillLevel, String, IViewHints, Enum, Enum...)
 */
public interface IFixedEnumValuesSetting< T extends Enum< T > > extends IFixedValuesSetting< T > {
	
	// No additional methods
	
}
