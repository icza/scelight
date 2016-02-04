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
 * A setting which is used only as a node in the navigation tree.
 * 
 * @author Andras Belicza
 * 
 * @see hu.scelightapi.gui.setting.ISettingsUtils#newNodeSetting(String, ISetting, String, hu.scelightapibase.gui.icon.IRIcon)
 */
public interface INodeSetting extends ISetting< Boolean > {
	
	// No additional methods
	
}
