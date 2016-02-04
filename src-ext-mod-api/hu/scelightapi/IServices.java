/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapi;

import hu.scelightapi.gui.IMainFrame;
import hu.scelightapi.gui.setting.ISettingsGui;
import hu.scelightapi.gui.setting.ISettingsUtils;
import hu.scelightapi.sc2.balancedata.IBdUtil;
import hu.scelightapi.sc2.rep.factory.IRepParserEngine;
import hu.scelightapi.service.IFactory;
import hu.scelightapi.service.IGuiFactory;
import hu.scelightapi.service.repfoldermonitor.IRepFolderMonitor;
import hu.scelightapi.service.sc2monitor.ISc2Monitor;
import hu.scelightapi.service.sound.ISound;
import hu.scelightapi.util.IUtils;
import hu.scelightapi.util.gui.IGuiUtils;
import hu.scelightapibase.bean.IVersionBean;
import hu.scelightapibase.service.lang.ILanguage;
import hu.scelightapibase.service.log.ILogger;

/**
 * Defines the module-independent services provided to external modules.
 * 
 * @author Andras Belicza
 */
public interface IServices {
	
	/**
	 * Returns the version bean of the services implementation.
	 * 
	 * @return the version bean of the services implementation
	 */
	IVersionBean getVersionBean();
	
	
	/**
	 * Returns the application logger that can / should be used to log messages by the module.
	 * 
	 * <p>
	 * Messages logged through the returned logger will appear in the application log.
	 * </p>
	 * 
	 * @return the application logger that can / should be used to log messages by the module
	 * 
	 * @see ILogger
	 */
	ILogger getLogger();
	
	/**
	 * Returns an {@link ILanguage} instance, a language and locale specific utilities object.
	 * 
	 * @return an {@link ILanguage} instance, a language and locale specific utilities object
	 */
	ILanguage getLanguage();
	
	/**
	 * Returns an {@link IUtils} instance, a general utilities object.
	 * 
	 * @return an {@link IUtils} instance, a general utilities object
	 */
	IUtils getUtils();
	
	/**
	 * Returns an {@link IGuiUtils} instance, a GUI utilities object.
	 * 
	 * @return an {@link IGuiUtils} instance, a GUI utilities object
	 */
	IGuiUtils getGuiUtils();
	
	/**
	 * Returns an {@link ISettingsUtils} instance, a settings utilities for creating setting interface instances and acquiring setting beans.
	 * 
	 * @return an {@link ISettingsUtils} instance, a settings utilities for creating setting interface instances and acquiring setting beans
	 */
	ISettingsUtils getSettingsUtils();
	
	/**
	 * Returns an {@link ISettingsGui} instance, a GUI utilities for settings visualization and edit.
	 * 
	 * @return an {@link ISettingsGui} instance, a GUI utilities for settings visualization and edit
	 */
	ISettingsGui getSettingsGui();
	
	/**
	 * Returns an {@link IFactory} instance, a general factory object.
	 * 
	 * @return an {@link IFactory} instance, a general factory object
	 */
	IFactory getFactory();
	
	/**
	 * Returns an {@link IGuiFactory} instance, a GUI factory object.
	 * 
	 * @return an {@link IGuiFactory} instance, a GUI factory object
	 */
	IGuiFactory getGuiFactory();
	
	/**
	 * Returns an {@link IRepParserEngine} instance, a StarCraft II replay parser.
	 * 
	 * @return an {@link IRepParserEngine} instance, a StarCraft II replay parser
	 */
	IRepParserEngine getRepParserEngine();
	
	/**
	 * Returns an {@link IBdUtil}, a balance data utility.
	 * 
	 * @return an {@link IBdUtil}, a balance data utility
	 */
	IBdUtil getBdUtil();
	
	/**
	 * Returns an {@link ISc2Monitor} instance, a StarCraft II monitor.
	 * 
	 * @return an {@link ISc2Monitor} instance, a StarCraft II monitor
	 */
	ISc2Monitor getSc2Monitor();
	
	/**
	 * Returns an {@link IRepFolderMonitor} instance, a Replay Folder monitor.
	 * 
	 * @return an {@link IRepFolderMonitor} instance, a Replay Folder monitor
	 */
	IRepFolderMonitor getRepFolderMonitor();
	
	/**
	 * Returns an {@link ISound} instance, a background sound player and sound utility.
	 * 
	 * @return an {@link ISound} instance, a background sound player and sound utility
	 */
	ISound getSound();
	
	/**
	 * Returns an {@link IMainFrame} instance, the main frame of the application.
	 * 
	 * @return an {@link IMainFrame} instance, the main frame of the application
	 */
	IMainFrame getMainFrame();
	
}
