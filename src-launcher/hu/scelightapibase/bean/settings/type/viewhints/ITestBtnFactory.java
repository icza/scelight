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

import hu.scelightapibase.bean.settings.type.ISetting;
import hu.scelightapibase.gui.comp.IButton;

import javax.swing.Icon;

/**
 * An {@link ISsCompFactory} extension which creates test {@link IButton} subsequent components.
 * 
 * @param <T> setting type to create custom subsequent components for
 * 
 * @author Andras Belicza
 * 
 * @see hu.scelightapi.gui.setting.ISettingsUtils#newTestBtnFactory(ITestBtnListener)
 * @see hu.scelightapi.gui.setting.ISettingsUtils#newTestBtnFactory(String, Icon, ITestBtnListener)
 * @see hu.scelightapi.gui.setting.ISettingsUtils#newHostCheckTestBtnFactory()
 * @see ITestBtnListener
 * @see ISsCompFactory
 */
public interface ITestBtnFactory< T extends ISetting< ? > > extends ISsCompFactory< T > {
	
	/**
	 * Returns the text of the test button that will be created.
	 * 
	 * @return the text of the test button that will be created
	 */
	String getText();
	
	/**
	 * Returns the icon of the test button that will be created.
	 * 
	 * @return the icon of the test button that will be created
	 */
	Icon getIcon();
	
}
