/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapibase.bean.settings.type.viewhints;

import hu.scelightapibase.bean.settings.ISettingsBean;
import hu.scelightapibase.bean.settings.type.ISetting;

import java.awt.Component;

import javax.swing.JComponent;

/**
 * Factory interface to create custom subsequent components for settings visualization / edit.
 * 
 * @param <T> setting type to create custom subsequent components for
 * 
 * @author Andras Belicza
 */
public interface ISsCompFactory< T extends ISetting< ? > > {
	
	/**
	 * Creates the custom subsequent component.
	 * 
	 * @param settingComp reference to the setting editor component
	 * @param setting setting to create the component for
	 * @param settings settings bean managing the setting value
	 * @return the custom subsequent component
	 */
	Component create( JComponent settingComp, T setting, ISettingsBean settings );
	
}
