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
 * A multi-line string setting.
 * 
 * <p>
 * It does not provide any new methods or attributes, its purpose is to create a different setting component for it.
 * </p>
 * 
 * @author Andras Belicza
 * 
 * @see hu.scelightapi.gui.setting.ISettingsUtils#newMultilineStringSetting(String, ISetting, ISettingsGroup, ISkillLevel, String, IViewHints, String)
 */
public interface IMultilineStringSetting extends IStringSetting {
	
	// No additional methods
	
}
