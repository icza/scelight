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
 * An integer setting with a valid range.
 * 
 * @author Andras Belicza
 * 
 * @see hu.scelightapi.gui.setting.ISettingsUtils#newIntSetting(String, ISetting, ISettingsGroup, ISkillLevel, String, IViewHints, Integer, Integer, Integer)
 */
public interface IIntSetting extends ISetting< Integer > {
	
	/**
	 * Returns the min valid value.
	 * 
	 * @return the min valid value
	 */
	Integer getMinValue();
	
	/**
	 * Returns the max valid value.
	 * 
	 * @return the max valid value
	 */
	Integer getMaxValue();
	
}
