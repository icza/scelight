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

import hu.scelightapibase.bean.settings.type.IFixedIntValuesSetting;
import hu.scelightapibase.bean.settings.type.ISetting;
import hu.scelightapibase.bean.settings.type.ISettingsGroup;
import hu.scelightapibase.bean.settings.type.viewhints.IViewHints;
import hu.scelightapibase.util.ISkillLevel;
import hu.sllauncher.util.LUtils;

/**
 * Fixed int values setting, the value of the setting can only be one of a predefined int values.
 * 
 * @author Andras Belicza
 */
public class FixedIntValuesSetting extends FixedValuesSetting< Integer > implements IFixedIntValuesSetting {
	
	/**
	 * Creates a new {@link FixedIntValuesSetting}.
	 * 
	 * @param id setting id
	 * @param parent parent setting to be added to
	 * @param group settings group this setting belongs to (within a setting node page)
	 * @param skillLevel setting skill level
	 * @param name setting name
	 * @param viewHints optional view hints of the setting
	 * @param defaultValue default setting value
	 * @param values valid values of the setting
	 */
	public FixedIntValuesSetting( final String id, final ISetting< ? > parent, final ISettingsGroup group, final ISkillLevel skillLevel, final String name,
	        final IViewHints viewHints, final Integer defaultValue, final Integer... values ) {
		super( id, parent, group, skillLevel, name, viewHints, defaultValue, values );
	}
	
	@Override
	public Integer parseValue( final String src ) {
		final Integer value;
		try {
			value = Integer.valueOf( src );
		} catch ( final NumberFormatException nfe ) {
			return null;
		}
		return LUtils.contains( values, value ) ? value : null;
	}
	
}
