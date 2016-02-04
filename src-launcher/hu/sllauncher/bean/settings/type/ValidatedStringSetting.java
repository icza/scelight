/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllauncher.bean.settings.type;

import hu.scelightapibase.bean.settings.type.ISetting;
import hu.scelightapibase.bean.settings.type.ISettingsGroup;
import hu.scelightapibase.bean.settings.type.IValidatedStringSetting;
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
 * The setting component will be of time {@link IIndicatorTextField} and a {@link ICompConfigurer} can be used to install an {@link IValidator} for the setting
 * component.
 * </p>
 * 
 * @author Andras Belicza
 */
public class ValidatedStringSetting extends StringSetting implements IValidatedStringSetting {
	
	/**
	 * Creates a new {@link ValidatedStringSetting}.
	 * 
	 * @param id setting id
	 * @param parent parent setting to be added to
	 * @param group settings group this setting belongs to (within a setting node page)
	 * @param skillLevel setting skill level
	 * @param name setting name
	 * @param viewHints optional view hints of the setting
	 * @param defaultValue default setting value
	 */
	public ValidatedStringSetting( final String id, final ISetting< ? > parent, final ISettingsGroup group, final ISkillLevel skillLevel, final String name,
	        final IViewHints viewHints, final String defaultValue ) {
		super( id, parent, group, skillLevel, name, viewHints, defaultValue );
	}
	
}
