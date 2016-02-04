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

import hu.scelightapi.gui.setting.ISettingsGui;
import hu.scelightapi.gui.setting.ISettingsUtils;
import hu.scelightapi.sc2.rep.factory.IRepParserEngine;
import hu.scelightapi.service.IFactory;
import hu.scelightapi.service.IGuiFactory;
import hu.scelightapi.util.IUtils;
import hu.scelightapi.util.gui.IGuiUtils;
import hu.scelightapibase.bean.IExtModManifestBean;
import hu.scelightapibase.service.lang.ILanguage;
import hu.scelightapibase.service.log.ILogger;

/**
 * A base, abstract implementation of {@link IExternalModule}.
 * 
 * <p>
 * External modules may choose to extend this class (this class implements the {@link IExternalModule} interface).
 * </p>
 * 
 * <p>
 * Stores the parameters received by the {@link #init(IExtModManifestBean, IServices, IModEnv)} in attributes for later use.<br>
 * Also stores some references that may/should be used frequently (like the application logger).
 * </p>
 * 
 * @version Implementation version: {@value #IMPL_VERSION}
 * 
 * @author Andras Belicza
 * 
 * @see IExternalModule
 */
public abstract class BaseExtModule implements IExternalModule {
	
	/** Implementation version. */
	public static final String IMPL_VERSION = "1.0";
	
	
	/**
	 * External module manifest loaded from the <code>"Scelight-mod-x-manifest.xml"</code> file from the version folder of the external module root folder.
	 */
	public IExtModManifestBean manifest;
	
	/** Services provided for the module. */
	public IServices           services;
	
	/** External module environment. */
	public IModEnv             modEnv;
	
	
	
	/** Application logger to be used to log messages. */
	public ILogger             logger;
	
	/** Application logger to be used to log messages. */
	public ILanguage           language;
	
	/** General utilities. */
	public IUtils              utils;
	
	/** (Swing) GUI utilities. */
	public IGuiUtils           guiUtils;
	
	/** Setting utilities. */
	public ISettingsUtils      settingsUtils;
	
	/** Settings GUI utilities. */
	public ISettingsGui        settingsGui;
	
	/** General factory to create / acquire instances of API interfaces. */
	public IFactory            factory;
	
	/** GUI factory to create / acquire instances of GUI API interfaces. */
	public IGuiFactory         guiFactory;
	
	/** Replay parser engine. */
	public IRepParserEngine    repParserEngine;
	
	
	
	/**
	 * Stores the received parameters for later use.<br>
	 * Also stores some references that may/should be used frequently (like the application logger and the general factory).
	 * 
	 * <p>
	 * If a module extends this class and overrides this method, in that overrider method super implementation (this version) should be called (start the
	 * overrider method with <code>super.init( manifest, services, modEnv )</code>).
	 * </p>
	 */
	@Override
	public void init( final IExtModManifestBean manifest, final IServices services, final IModEnv modEnv ) {
		this.manifest = manifest;
		this.services = services;
		this.modEnv = modEnv;
		
		logger = services.getLogger();
		utils = services.getUtils();
		guiUtils = services.getGuiUtils();
		settingsUtils = services.getSettingsUtils();
		settingsGui = services.getSettingsGui();
		factory = services.getFactory();
		guiFactory = services.getGuiFactory();
		repParserEngine = services.getRepParserEngine();
	}
	
}
