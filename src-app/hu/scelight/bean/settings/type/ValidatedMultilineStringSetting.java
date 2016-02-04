/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.bean.settings.type;

import hu.scelightapi.bean.settings.type.IValidatedMultilineStringSetting;
import hu.scelightapibase.bean.settings.type.ISetting;
import hu.scelightapibase.bean.settings.type.ISettingsGroup;
import hu.scelightapibase.bean.settings.type.viewhints.IViewHints;
import hu.scelightapibase.util.ISkillLevel;
import hu.sllauncher.bean.settings.type.MultilineStringSetting;

/**
 * A validated multi-line string setting.
 * 
 * @author Andras Belicza
 */
public class ValidatedMultilineStringSetting extends MultilineStringSetting implements IValidatedMultilineStringSetting {
	
	/**
	 * Creates a new {@link ValidatedMultilineStringSetting}.
	 * 
	 * @param id setting id
	 * @param parent parent setting to be added to
	 * @param group settings group this setting belongs to (within a setting node page)
	 * @param skillLevel setting skill level
	 * @param name setting name
	 * @param viewHints optional view hints of the setting
	 * @param defaultValue default setting value
	 */
	public ValidatedMultilineStringSetting( String id, ISetting< ? > parent, ISettingsGroup group, ISkillLevel skillLevel, String name, IViewHints viewHints,
	        String defaultValue ) {
		super( id, parent, group, skillLevel, name, viewHints, defaultValue );
	}
	
}
