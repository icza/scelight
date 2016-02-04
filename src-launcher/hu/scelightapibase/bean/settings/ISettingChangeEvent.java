/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapibase.bean.settings;

import hu.scelightapibase.bean.settings.type.ISetting;

/**
 * Event describing setting changes.
 * 
 * @author Andras Belicza
 */
public interface ISettingChangeEvent {
	
	/**
	 * Returns the settings bean that fired the setting change event.
	 * 
	 * @return the settings bean that fired the setting change event
	 */
	ISettingsBean getSettings();
	
	/**
	 * Tells if any of the specified settings is affected by the setting change event.
	 * 
	 * @param settings settings to test
	 * @return true if any of the specified settings is affected by the setting change event
	 */
	boolean affectedAny( ISetting< ? >... settings );
	
	/**
	 * Tells if the specified setting is affected by the setting change event.
	 * 
	 * @param setting setting to test
	 * @return true if the specified setting is affected by the setting change event
	 */
	boolean affected( ISetting< ? > setting );
	
	/**
	 * Returns the value of the specified setting.
	 * 
	 * @param <T> value type of the setting
	 * @param setting setting whose value to be returned
	 * @return the value of the specified setting
	 */
	< T > T get( ISetting< T > setting );
	
}
