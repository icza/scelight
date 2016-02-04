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

/**
 * Fixed values setting, the value of the setting can only be one of a predefined values.
 * 
 * @param <T> setting value type; it HAS to be an IMMUTABLE type (because returned values are by reference and are not cloned!)
 * 
 * @author Andras Belicza
 */
public interface IFixedValuesSetting< T > extends ISetting< T > {
	
	/**
	 * Returns the valid values of the setting.
	 * 
	 * @return the valid values of the setting
	 */
	T[] getValues();
	
}
