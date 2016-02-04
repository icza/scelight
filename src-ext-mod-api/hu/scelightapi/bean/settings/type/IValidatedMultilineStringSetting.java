/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapi.bean.settings.type;

import hu.scelightapi.gui.comp.IIndicatorTextArea;
import hu.scelightapi.gui.setting.ISettingsUtils;
import hu.scelightapibase.bean.settings.type.IMultilineStringSetting;
import hu.scelightapibase.bean.settings.type.ISetting;
import hu.scelightapibase.bean.settings.type.ISettingsGroup;
import hu.scelightapibase.bean.settings.type.viewhints.ICompConfigurer;
import hu.scelightapibase.bean.settings.type.viewhints.IViewHints;
import hu.scelightapibase.gui.comp.ITextField.IValidator;
import hu.scelightapibase.util.ISkillLevel;

/**
 * A validated multi-line string setting.
 * 
 * <p>
 * It does not provide any new methods or attributes, its purpose is to create a different setting component for it.<br>
 * The setting component will be of type {@link IIndicatorTextArea} and an {@link ICompConfigurer} can be used to install an {@link IValidator} for the setting
 * component.
 * </p>
 * 
 * @since 1.4
 * 
 * @author Andras Belicza
 * 
 * @see ISettingsUtils#newValidatedMultilineStringSetting(String, ISetting, ISettingsGroup, ISkillLevel, String, IViewHints, String)
 */
public interface IValidatedMultilineStringSetting extends IMultilineStringSetting {
	
	// No additional methods
	
}
