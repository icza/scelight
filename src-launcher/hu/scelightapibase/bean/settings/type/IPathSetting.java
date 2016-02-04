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

import java.nio.file.Path;

/**
 * A path setting.
 * 
 * @author Andras Belicza
 * 
 * @see hu.scelightapi.gui.setting.ISettingsUtils#newPathSetting(String, ISetting, ISettingsGroup, hu.scelightapibase.util.ISkillLevel, String,
 *      hu.scelightapibase.bean.settings.type.viewhints.IViewHints, Path)
 */
public interface IPathSetting extends ISetting< Path > {
	
	// No additional methods
	
}
