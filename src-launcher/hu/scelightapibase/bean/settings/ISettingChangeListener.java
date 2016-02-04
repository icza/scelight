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

/**
 * Setting change listener.
 * 
 * @author Andras Belicza
 */
public interface ISettingChangeListener {
	
	/**
	 * Called when the values of the specified settings have changed.
	 * 
	 * @param event setting change event describing setting changes
	 */
	void valuesChanged( ISettingChangeEvent event );
	
}
