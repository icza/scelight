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

import hu.scelightapibase.bean.settings.type.IFixedValuesSetting;
import hu.scelightapibase.bean.settings.type.ISetting;
import hu.scelightapibase.bean.settings.type.ISettingsGroup;
import hu.scelightapibase.bean.settings.type.viewhints.IViewHints;
import hu.scelightapibase.util.ISkillLevel;
import hu.sllauncher.service.env.LEnv;
import hu.sllauncher.util.LUtils;

/**
 * Fixed values setting, the value of the setting can only be one of a predefined values.
 * 
 * @param <T> setting value type; it HAS to be an IMMUTABLE type (because returned values are by reference and are not cloned!)
 * 
 * @author Andras Belicza
 */
public abstract class FixedValuesSetting< T > extends Setting< T > implements IFixedValuesSetting< T > {
	
	/** Valid values of the setting. */
	public final T[] values;
	
	/**
	 * Creates a new {@link FixedValuesSetting}.
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
	public FixedValuesSetting( final String id, final ISetting< ? > parent, final ISettingsGroup group, final ISkillLevel skillLevel, final String name,
	        final IViewHints viewHints, final T defaultValue, @SuppressWarnings( "unchecked" ) final T... values ) {
		super( id, parent, group, skillLevel, name, viewHints, defaultValue );
		
		if ( LEnv.DEV_MODE && !LUtils.contains( values, defaultValue ) )
			throw new IllegalArgumentException( "Default value is not included in valid values!" );
		
		this.values = values;
	}
	
	@Override
	public T[] getValues() {
		return values;
	}
	
}
