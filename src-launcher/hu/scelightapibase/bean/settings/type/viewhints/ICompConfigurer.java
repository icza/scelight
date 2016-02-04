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

import javax.swing.JComponent;

/**
 * Setting component configurer for settings visualization / edit.
 * 
 * <p>
 * A setting configurer can make any adjustment or configuring for a setting component <i>after</i> it has been created but not yet displayed.
 * </p>
 * 
 * @author Andras Belicza
 */
public interface ICompConfigurer {
	
	/**
	 * Configures the setting component.
	 * 
	 * @param settingComp reference to the setting editor component
	 * @param setting setting the component is associated with
	 * @param settings settings bean managing the setting value
	 */
	void configure( JComponent settingComp, ISetting< ? > setting, ISettingsBean settings );
	
}
