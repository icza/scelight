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

import hu.scelightapibase.util.IRHtml;

/**
 * A logical/visual settings group within a setting node page.
 * 
 * @author Andras Belicza
 * 
 * @see hu.scelightapi.gui.setting.ISettingsUtils#newSettingsGroup(String)
 * @see hu.scelightapi.gui.setting.ISettingsUtils#newSettingsGroup(String, IRHtml)
 */
public interface ISettingsGroup {
	
	/**
	 * Returns the name of the settings group.
	 * 
	 * @return the name of the settings group
	 */
	String getName();
	
	/**
	 * Returns the help HTML resource of the settings group.
	 * 
	 * @return the help HTML resource of the settings group
	 */
	IRHtml gethelpRhtml();
	
}
