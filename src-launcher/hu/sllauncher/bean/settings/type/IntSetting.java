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

import hu.scelightapibase.bean.settings.type.IIntSetting;
import hu.scelightapibase.bean.settings.type.ISetting;
import hu.scelightapibase.bean.settings.type.ISettingsGroup;
import hu.scelightapibase.bean.settings.type.viewhints.IViewHints;
import hu.scelightapibase.util.ISkillLevel;
import hu.sllauncher.service.env.LEnv;

/**
 * An integer setting with a valid range.
 * 
 * @author Andras Belicza
 */
public class IntSetting extends Setting< Integer > implements IIntSetting {
	
	/** Min valid value. */
	public final Integer minValue;
	
	/** Max valid value. */
	public final Integer maxValue;
	
	/**
	 * Creates a new {@link IntSetting}.
	 * 
	 * @param id setting id
	 * @param parent parent setting to be added to
	 * @param group settings group this setting belongs to (within a setting node page)
	 * @param skillLevel setting skill level
	 * @param name setting name
	 * @param viewHints optional view hints of the setting
	 * @param defaultValue default setting value
	 * @param minValue valid min value
	 * @param maxValue valid max value
	 */
	public IntSetting( final String id, final ISetting< ? > parent, final ISettingsGroup group, final ISkillLevel skillLevel, final String name,
	        final IViewHints viewHints, final Integer defaultValue, final Integer minValue, final Integer maxValue ) {
		super( id, parent, group, skillLevel, name, viewHints, defaultValue );
		
		if ( LEnv.DEV_MODE ) {
			if ( defaultValue < minValue )
				throw new IllegalArgumentException( "Default value (" + defaultValue + ") cannot be less than the min value (" + minValue + ")!" );
			if ( defaultValue > maxValue )
				throw new IllegalArgumentException( "Default value (" + defaultValue + ") cannot be greater than the max value (" + maxValue + ")!" );
		}
		
		this.minValue = minValue;
		this.maxValue = maxValue;
	}
	
	@Override
	public Integer getMinValue() {
		return minValue;
	}
	
	@Override
	public Integer getMaxValue() {
		return maxValue;
	}
	
	@Override
	public Integer parseValue( final String src ) {
		final Integer value;
		try {
			value = Integer.valueOf( src );
		} catch ( final NumberFormatException nfe ) {
			// If saved value was manually manipulated...
			return null;
		}
		
		// If valid range changed or saved value was manually manipulated...
		return value >= minValue && value <= maxValue ? value : null;
	}
	
}
