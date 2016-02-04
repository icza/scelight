/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.gui.setting;

import hu.scelight.service.env.Env;
import hu.scelight.service.extmod.ModEnv;
import hu.scelightapibase.bean.settings.ISettingsBean;
import hu.scelightapibase.bean.settings.type.INodeSetting;
import hu.sllauncher.gui.setting.LSettingsDialog;
import hu.sllauncher.gui.setting.LSettingsNodePage;

import java.awt.Frame;
import java.util.ArrayList;
import java.util.List;

/**
 * A dialog to view/edit settings of the application.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class SettingsDialog extends LSettingsDialog {
	
	/**
	 * Returns an array of all settings beans. These include the settings beans of loaded external modules.
	 * 
	 * @return an array of all settings beans
	 */
	private static ISettingsBean[] getAllSettingss() {
		final List< ISettingsBean > settingsList = new ArrayList<>();
		settingsList.add( Env.BOOT_SETTINGS );
		settingsList.add( Env.LAUNCHER_SETTINGS );
		settingsList.add( Env.APP_SETTINGS );
		
		for ( final ModEnv modEnv : Env.EXT_MOD_MANAGER.getStartedExtModEnvList() )
			settingsList.addAll( modEnv.getSettingsBeanList() );
		
		return settingsList.toArray( new ISettingsBean[ settingsList.size() ] );
	}
	
	/**
	 * Creates a new {@link SettingsDialog}.
	 * 
	 * <p>
	 * <b>Does not make the dialog visible!</b>
	 * </p>
	 * 
	 * @param owner reference to the owner frame
	 * @param defaultNodeSetting optional node setting to select by default
	 */
	public SettingsDialog( final Frame owner, final INodeSetting defaultNodeSetting ) {
		super( owner, defaultNodeSetting, getAllSettingss() );
	}
	
	/**
	 * Overridden to return a new instance of {@link SettingsNodePage}.
	 */
	@Override
	protected LSettingsNodePage createNewSettingsNodePage( final INodeSetting nodeSetting ) {
		return new SettingsNodePage( nodeSetting );
	}
	
}
