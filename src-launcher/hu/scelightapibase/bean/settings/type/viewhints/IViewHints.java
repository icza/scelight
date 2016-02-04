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

import hu.scelightapibase.bean.settings.type.IMultilineStringSetting;
import hu.scelightapibase.bean.settings.type.INodeSetting;
import hu.scelightapibase.bean.settings.type.IPathSetting;
import hu.scelightapibase.bean.settings.type.ISetting;
import hu.scelightapibase.bean.settings.type.IStringSetting;
import hu.scelightapibase.bean.settings.type.IValidatedStringSetting;
import hu.scelightapibase.gui.icon.IRIcon;
import hu.scelightapibase.util.IRHtml;
import hu.scelightapibase.util.gui.HasRIcon;

/**
 * Non-required properties intended for the Settings view dialog to customize the setting's view/edit components.
 * 
 * <p>
 * Instances are IMMUTABLE. For easy creation of instances, see the {@link IVHB View hints builder} class.
 * </p>
 * 
 * @author Andras Belicza
 * 
 * @see hu.scelightapi.gui.setting.ISettingsUtils#newViewHints(IRIcon, String, IRHtml, String, ISsCompFactory, ICompConfigurer, boolean, Integer, Integer)
 * @see IVHB
 */
public interface IViewHints extends HasRIcon {
	
	/**
	 * Setting ricon, used for {@link INodeSetting}s as the icon in the navigation tree and in the settings node page title.
	 */
	@Override
	IRIcon getRicon();
	
	/**
	 * Returns the subsequent text of the setting component, usually a measurement unit name (e.g. "pixel" or "sec") but can be any other text (e.g. a note that
	 * changing this requires restart).
	 * 
	 * @return the subsequent text of the setting component
	 */
	String getSubsequentText();
	
	/**
	 * Returns the help HTML resource of the setting.
	 * 
	 * @return the help HTML resource of the setting
	 */
	IRHtml getHelpRhtml();
	
	/**
	 * Returns the dialog title (used by settings whose editor component might open a dialog, e.g. {@link IPathSetting}).
	 * 
	 * @return the dialog title (used by settings whose editor component might open a dialog, e.g. {@link IPathSetting})
	 */
	String getDialogTitle();
	
	/**
	 * Returns the custom subsequent component factory.
	 * 
	 * @return the custom subsequent component factory
	 */
	ISsCompFactory< ISetting< ? > > getSsCompFactory();
	
	/**
	 * Returns the setting component configurer.
	 * 
	 * @return the setting component configurer
	 */
	ICompConfigurer getCompConfigurer();
	
	/**
	 * Tells if editing the setting requires registration.
	 * 
	 * @return if editing the setting requires registration
	 */
	boolean isEditRequiresRegistration();
	
	/**
	 * Returns the rows count to be used for the setting component if supported. Used for {@link IMultilineStringSetting} and
	 * {@link hu.scelightapi.bean.settings.type.IValidatedMultilineStringSetting}.
	 * 
	 * @return the rows count to be used
	 * 
	 * @since 1.4
	 */
	Integer getRows();
	
	/**
	 * Returns the columns count to be used for the setting component if supported. Used for {@link IStringSetting}, {@link IPathSetting},
	 * {@link IValidatedStringSetting}, {@link hu.scelightapi.bean.settings.type.ITemplateSetting}, {@link IMultilineStringSetting} and
	 * {@link hu.scelightapi.bean.settings.type.IValidatedMultilineStringSetting}.
	 * 
	 * @return the columns count to be used
	 * 
	 * @since 1.4
	 */
	Integer getColumns();
	
}
