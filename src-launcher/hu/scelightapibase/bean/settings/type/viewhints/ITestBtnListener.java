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
import hu.scelightapibase.gui.comp.IButton;

import javax.swing.JComponent;

/**
 * A listener which is called when a test button subsequent component created by the {@link ITestBtnFactory} is clicked.
 * 
 * @param <T> setting type whose test button was clicked
 * 
 * @author Andras Belicza
 * 
 * @see ITestBtnFactory
 */
public interface ITestBtnListener< T extends ISetting< ? > > {
	
	/**
	 * Performs the test.
	 * 
	 * @param button reference to the test button
	 * @param settingComp reference to the setting editor component
	 * @param setting setting to create the component for
	 * @param settings settings bean managing the setting value
	 */
	void doTest( IButton button, JComponent settingComp, T setting, ISettingsBean settings );
	
}
