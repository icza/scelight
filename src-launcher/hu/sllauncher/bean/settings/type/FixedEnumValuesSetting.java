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

import hu.scelightapibase.bean.settings.type.IFixedEnumValuesSetting;
import hu.scelightapibase.bean.settings.type.ISetting;
import hu.scelightapibase.bean.settings.type.ISettingsGroup;
import hu.scelightapibase.bean.settings.type.viewhints.IViewHints;
import hu.scelightapibase.util.ISkillLevel;
import hu.sllauncher.util.LUtils;

/**
 * Fixed enum values setting, the value of the setting can only be one of a predefined enum values.
 * 
 * @param <T> setting value type; it has to be an enum type
 * 
 * @author Andras Belicza
 */
public class FixedEnumValuesSetting< T extends Enum< T > > extends FixedValuesSetting< T > implements IFixedEnumValuesSetting< T > {
	
	/**
	 * Creates a new {@link FixedEnumValuesSetting}.
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
	public FixedEnumValuesSetting( final String id, final ISetting< ? > parent, final ISettingsGroup group, final ISkillLevel skillLevel, final String name,
	        final IViewHints viewHints, final T defaultValue, @SuppressWarnings( "unchecked" ) final T... values ) {
		super( id, parent, group, skillLevel, name, viewHints, defaultValue, values );
	}
	
	@Override
	@SuppressWarnings( "unchecked" )
	public T parseValue( final String src ) {
		final T value;
		try {
			value = Enum.valueOf( (Class< T >) defaultValue.getClass(), src );
		} catch ( final IllegalArgumentException iae ) {
			// If enum constant names changed or saved value was manually manipulated...
			return null;
		}
		
		// If valid values changed or saved value was manually manipulated...
		return LUtils.contains( values, value ) ? value : null;
	}
	
	/**
	 * Have to use {@link Enum#name()} because most enum types override {@link Object#toString()} for displaying purposes.
	 */
	@Override
	public String formatValue( final T value ) {
		return value.name();
	}
	
}
