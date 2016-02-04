/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapi.gui.setting;

import hu.scelightapi.IModEnv;
import hu.scelightapi.bean.settings.type.ITemplateSetting;
import hu.scelightapi.bean.settings.type.IValidatedMultilineStringSetting;
import hu.scelightapibase.bean.settings.ISettingsBean;
import hu.scelightapibase.bean.settings.type.IBoolSetting;
import hu.scelightapibase.bean.settings.type.IEnumSetting;
import hu.scelightapibase.bean.settings.type.IFixedEnumValuesSetting;
import hu.scelightapibase.bean.settings.type.IFixedIntValuesSetting;
import hu.scelightapibase.bean.settings.type.IIntSetting;
import hu.scelightapibase.bean.settings.type.IMultilineStringSetting;
import hu.scelightapibase.bean.settings.type.INodeSetting;
import hu.scelightapibase.bean.settings.type.IPathSetting;
import hu.scelightapibase.bean.settings.type.ISetting;
import hu.scelightapibase.bean.settings.type.ISettingsGroup;
import hu.scelightapibase.bean.settings.type.IStringSetting;
import hu.scelightapibase.bean.settings.type.IValidatedStringSetting;
import hu.scelightapibase.bean.settings.type.viewhints.ICompConfigurer;
import hu.scelightapibase.bean.settings.type.viewhints.ISsCompFactory;
import hu.scelightapibase.bean.settings.type.viewhints.ITestBtnFactory;
import hu.scelightapibase.bean.settings.type.viewhints.ITestBtnListener;
import hu.scelightapibase.bean.settings.type.viewhints.IVHB;
import hu.scelightapibase.bean.settings.type.viewhints.IViewHints;
import hu.scelightapibase.gui.icon.IRIcon;
import hu.scelightapibase.util.IRHtml;
import hu.scelightapibase.util.ISkillLevel;

import java.nio.file.Path;

import javax.swing.Icon;

/**
 * Settings utilities for creating setting interface instances ({@link ISetting}.
 * 
 * <p>
 * An instance of {@link ISettingsBean} can be acquired by {@link IModEnv#initSettingsBean(String, java.util.List)}.
 * </p>
 * 
 * @author Andras Belicza
 * 
 * @see ISettingsGui
 * @see IModEnv#initSettingsBean(String, java.util.List)
 */
public interface ISettingsUtils {
	
	/**
	 * Creates a new {@link IBoolSetting}.
	 * 
	 * @param id setting id
	 * @param parent parent setting to be added to
	 * @param group settings group this setting belongs to (within a setting node page)
	 * @param skillLevel setting skill level
	 * @param name setting name
	 * @param viewHints optional view hints of the setting
	 * @param defaultValue default setting value
	 * @return a new {@link IBoolSetting}
	 */
	IBoolSetting newBoolSetting( String id, ISetting< ? > parent, ISettingsGroup group, ISkillLevel skillLevel, String name, IViewHints viewHints,
	        Boolean defaultValue );
	
	/**
	 * Creates a new {@link IEnumSetting}.
	 * 
	 * @param <T> setting value type; it has to be an enum type
	 * 
	 * @param id setting id
	 * @param parent parent setting to be added to
	 * @param group settings group this setting belongs to (within a setting node page)
	 * @param skillLevel setting skill level
	 * @param name setting name
	 * @param viewHints optional view hints of the setting
	 * @param defaultValue default setting value
	 * @return a new {@link IEnumSetting}
	 */
	< T extends Enum< T > > IEnumSetting< T > newEnumSetting( String id, ISetting< ? > parent, ISettingsGroup group, ISkillLevel skillLevel, String name,
	        IViewHints viewHints, T defaultValue );
	
	/**
	 * Creates a new {@link IFixedEnumValuesSetting}.
	 * 
	 * @param <T> setting value type; it has to be an enum type
	 * 
	 * @param id setting id
	 * @param parent parent setting to be added to
	 * @param group settings group this setting belongs to (within a setting node page)
	 * @param skillLevel setting skill level
	 * @param name setting name
	 * @param viewHints optional view hints of the setting
	 * @param defaultValue default setting value
	 * @param values valid values of the setting
	 * @return a new {@link IFixedEnumValuesSetting}
	 */
	< T extends Enum< T > > IFixedEnumValuesSetting< T > newFixedEnumValuesSetting( String id, ISetting< ? > parent, ISettingsGroup group,
	        ISkillLevel skillLevel, String name, IViewHints viewHints, T defaultValue, @SuppressWarnings( "unchecked" ) T... values );
	
	/**
	 * Creates a new {@link IFixedIntValuesSetting}.
	 * 
	 * @param id setting id
	 * @param parent parent setting to be added to
	 * @param group settings group this setting belongs to (within a setting node page)
	 * @param skillLevel setting skill level
	 * @param name setting name
	 * @param viewHints optional view hints of the setting
	 * @param defaultValue default setting value
	 * @param values valid values of the setting
	 * @return a new {@link IFixedIntValuesSetting}
	 */
	IFixedIntValuesSetting newFixedIntValuesSetting( String id, ISetting< ? > parent, ISettingsGroup group, ISkillLevel skillLevel, String name,
	        IViewHints viewHints, Integer defaultValue, Integer... values );
	
	/**
	 * Creates a new {@link IIntSetting}.
	 * 
	 * @param id setting id
	 * @param parent parent setting to be added to
	 * @param group settings group this setting belongs to (within a setting node page)
	 * @param skillLevel setting skill level
	 * @param name setting name
	 * @param viewHints optional view hints of the setting
	 * @param defaultValue default setting value
	 * @param minValue valid min value
	 * @param maxValue valid max value
	 * @return a new {@link IIntSetting}
	 */
	IIntSetting newIntSetting( String id, ISetting< ? > parent, ISettingsGroup group, ISkillLevel skillLevel, String name, IViewHints viewHints,
	        Integer defaultValue, Integer minValue, Integer maxValue );
	
	/**
	 * Creates a new {@link IMultilineStringSetting}.
	 * 
	 * @param id setting id
	 * @param parent parent setting to be added to
	 * @param group settings group this setting belongs to (within a setting node page)
	 * @param skillLevel setting skill level
	 * @param name setting name
	 * @param viewHints optional view hints of the setting
	 * @param defaultValue default setting value
	 * @return a new {@link IMultilineStringSetting}
	 */
	IMultilineStringSetting newMultilineStringSetting( String id, ISetting< ? > parent, ISettingsGroup group, ISkillLevel skillLevel, String name,
	        IViewHints viewHints, String defaultValue );
	
	/**
	 * Creates a new {@link INodeSetting}.
	 * 
	 * @param id setting id
	 * @param parent parent setting to be added to
	 * @param name setting name
	 * @param ricon setting ricon
	 * @return a new {@link INodeSetting}
	 */
	INodeSetting newNodeSetting( String id, ISetting< ? > parent, String name, IRIcon ricon );
	
	/**
	 * Creates a new {@link IPathSetting}.
	 * 
	 * @param id setting id
	 * @param parent parent setting to be added to
	 * @param group settings group this setting belongs to (within a setting node page)
	 * @param skillLevel setting skill level
	 * @param name setting name
	 * @param viewHints optional view hints of the setting
	 * @param defaultValue default setting value
	 * @return a new {@link IPathSetting}
	 */
	IPathSetting newPathSetting( String id, ISetting< ? > parent, ISettingsGroup group, ISkillLevel skillLevel, String name, IViewHints viewHints,
	        Path defaultValue );
	
	/**
	 * Creates a new {@link IStringSetting}.
	 * 
	 * @param id setting id
	 * @param parent parent setting to be added to
	 * @param group settings group this setting belongs to (within a setting node page)
	 * @param skillLevel setting skill level
	 * @param name setting name
	 * @param viewHints optional view hints of the setting
	 * @param defaultValue default setting value
	 * @return a new {@link IStringSetting}
	 */
	IStringSetting newStringSetting( String id, ISetting< ? > parent, ISettingsGroup group, ISkillLevel skillLevel, String name, IViewHints viewHints,
	        String defaultValue );
	
	/**
	 * Creates a new {@link IValidatedStringSetting}.
	 * 
	 * @param id setting id
	 * @param parent parent setting to be added to
	 * @param group settings group this setting belongs to (within a setting node page)
	 * @param skillLevel setting skill level
	 * @param name setting name
	 * @param viewHints optional view hints of the setting
	 * @param defaultValue default setting value
	 * @return a new {@link IValidatedStringSetting}
	 */
	IValidatedStringSetting newValidatedStringSetting( String id, ISetting< ? > parent, ISettingsGroup group, ISkillLevel skillLevel, String name,
	        IViewHints viewHints, String defaultValue );
	
	/**
	 * Creates a new {@link ITemplateSetting}.
	 * 
	 * @param id setting id
	 * @param parent parent setting to be added to
	 * @param group settings group this setting belongs to (within a setting node page)
	 * @param skillLevel setting skill level
	 * @param name setting name
	 * @param viewHints optional view hints of the setting
	 * @param defaultValue default setting value
	 * @return a new {@link ITemplateSetting}
	 */
	ITemplateSetting newTemplateSetting( String id, ISetting< ? > parent, ISettingsGroup group, ISkillLevel skillLevel, String name, IViewHints viewHints,
	        String defaultValue );
	
	/**
	 * Creates a new {@link IValidatedMultilineStringSetting}.
	 * 
	 * @param id setting id
	 * @param parent parent setting to be added to
	 * @param group settings group this setting belongs to (within a setting node page)
	 * @param skillLevel setting skill level
	 * @param name setting name
	 * @param viewHints optional view hints of the setting
	 * @param defaultValue default setting value
	 * @return a new {@link IValidatedMultilineStringSetting}
	 * 
	 * @since 1.4
	 */
	IValidatedMultilineStringSetting newValidatedMultilineStringSetting( String id, ISetting< ? > parent, ISettingsGroup group, ISkillLevel skillLevel,
	        String name, IViewHints viewHints, String defaultValue );
	
	/**
	 * Creates a new {@link ISettingsGroup}.
	 * 
	 * @param name name of the settings group
	 * @return a new {@link ISettingsGroup}
	 */
	ISettingsGroup newSettingsGroup( String name );
	
	/**
	 * Creates a new {@link ISettingsGroup}.
	 * 
	 * @param name name of the settings group
	 * @param helpRhtml help HTML resource of the settings group
	 * @return a new {@link ISettingsGroup}
	 */
	ISettingsGroup newSettingsGroup( String name, IRHtml helpRhtml );
	
	
	/**
	 * Creates a new {@link IViewHints}. For easy creation of instances, see the {@link IVHB View hints builder} class.
	 * 
	 * @param ricon setting ricon
	 * @param subsequentText subsequent text of the setting component
	 * @param rhtml help HTML resource of the setting
	 * @param dialogTitle dialog title
	 * @param ssCompFactory custom subsequent component factory
	 * @param compConfigurer setting component configurer
	 * @param editRequiresRegistration tells if editing the setting requires registration
	 * @param rows the rows count to be used for the setting component if supported
	 * @param columns the columns count to be used for the setting component if supported
	 * @return a new {@link IViewHints}
	 * 
	 * @see IVHB
	 * @see #newVHB()
	 */
	IViewHints newViewHints( IRIcon ricon, String subsequentText, IRHtml rhtml, String dialogTitle, ISsCompFactory< ISetting< ? > > ssCompFactory,
	        ICompConfigurer compConfigurer, boolean editRequiresRegistration, Integer rows, Integer columns );
	
	/**
	 * Creates a new {@link IVHB}, an {@link IViewHints} builder class.
	 * 
	 * @return a new {@link IVHB}, an {@link IViewHints} builder class
	 */
	IVHB newVHB();
	
	/**
	 * Creates a new {@link ITestBtnFactory} with default text and icon which calls the specified listener when created test buttons are pressed.
	 * 
	 * @param <T> setting type to create custom subsequent components for
	 * @param listener {@link ITestBtnListener} to be called when the test setting button is pressed
	 * 
	 * @return a new {@link ITestBtnFactory}
	 */
	< T extends ISetting< ? > > ITestBtnFactory< T > newTestBtnFactory( ITestBtnListener< T > listener );
	
	/**
	 * Creates a new {@link ITestBtnFactory} with default text and icon which calls the specified listener when created test buttons are pressed.
	 * 
	 * @param <T> setting type to create custom subsequent components for
	 * @param text optional text of the test button that will be created; defaults to <code>"Test"</code>
	 * @param icon optional icon of the test button that will be created
	 * @param listener {@link ITestBtnListener} to be called when the test setting button is pressed
	 * 
	 * @return a new {@link ITestBtnFactory}
	 */
	< T extends ISetting< ? > > ITestBtnFactory< T > newTestBtnFactory( String text, Icon icon, ITestBtnListener< T > listener );
	
	/**
	 * Creates a new {@link ITestBtnFactory} whose created test buttons check whether the setting value of the {@link IStringSetting} as a host is reachable.
	 * 
	 * @return a new {@link ITestBtnFactory}
	 */
	ITestBtnFactory< IStringSetting > newHostCheckTestBtnFactory();
	
}
