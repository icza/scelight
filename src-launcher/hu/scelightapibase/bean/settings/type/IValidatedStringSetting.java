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

import hu.scelightapibase.bean.settings.type.viewhints.ICompConfigurer;
import hu.scelightapibase.bean.settings.type.viewhints.IViewHints;
import hu.scelightapibase.gui.comp.IIndicatorTextField;
import hu.scelightapibase.gui.comp.ITextField.IValidator;
import hu.scelightapibase.util.ISkillLevel;

/**
 * A validated string setting.
 * 
 * <p>
 * It does not provide any new methods or attributes, it's purpose is to create a different setting component for it.<br>
 * The setting component will be of time {@link IIndicatorTextField} and an {@link ICompConfigurer} can be used to install an {@link IValidator} for the setting
 * component.
 * </p>
 * 
 * @author Andras Belicza
 * 
 * @see hu.scelightapi.gui.setting.ISettingsUtils#newValidatedStringSetting(String, ISetting, ISettingsGroup, ISkillLevel, String, IViewHints, String)
 */
public interface IValidatedStringSetting extends IStringSetting {
	
	// No additional methods
	
}
