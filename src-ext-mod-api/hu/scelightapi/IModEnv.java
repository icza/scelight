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

import hu.scelightapibase.bean.IExtModManifestBean;
import hu.scelightapibase.bean.settings.ISettingsBean;
import hu.scelightapibase.bean.settings.type.ISetting;

import java.nio.file.Path;
import java.util.List;

/**
 * External module environment, external module specific info source.
 * 
 * @author Andras Belicza
 */
public interface IModEnv {
	
	/**
	 * Returns the same external module manifest that was passed to the {@link IExternalModule#init(IExtModManifestBean, IServices, IModEnv)}.
	 * 
	 * <p>
	 * This is also provided here for convenience.
	 * </p>
	 * 
	 * @return the external module manifest
	 */
	IExtModManifestBean getManifest();
	
	/**
	 * Returns the external module root folder.
	 * 
	 * <p>
	 * The folder returned contains the version folder of the external module.
	 * </p>
	 * 
	 * <p>
	 * The root folder is the parent of the version folder.
	 * </p>
	 * 
	 * @return the external module root folder
	 * 
	 * @see #getVersionFolder()
	 */
	Path getRootFolder();
	
	/**
	 * Returns the external module version folder.
	 * 
	 * <p>
	 * The folder returned contains the files of the external module, including the external module manifest file ( <code>"Scelight-mod-x-manifest.xml"</code>).
	 * </p>
	 * 
	 * <p>
	 * The root folder is the parent of the version folder.
	 * </p>
	 * 
	 * @return the external module version folder
	 * 
	 * @see #getRootFolder()
	 */
	Path getVersionFolder();
	
	/**
	 * Returns the external module cache folder.
	 * 
	 * <p>
	 * The folder returned may be used to store persistent files. The returned folder is unique to the external module, it is not shared with other external
	 * modules (each external module has its own cache folder).<br>
	 * The cache folder is under the application workspace, and files stored in the cache folder are kept and will be available if the application is restarted.
	 * </p>
	 * 
	 * <p>
	 * Calling this method will also create the cache folder if it does not yet exist.
	 * </p>
	 * 
	 * @return the external module cache folder
	 */
	Path getCacheFolder();
	
	/**
	 * Creates a new {@link ISettingsBean} and initializes it.
	 * 
	 * <p>
	 * The created and returned settings bean will be backed by a persistent file located in the cache folder.<br>
	 * The file name will be the specified name (postpended with the extension <code>.xml</code>), so the specified <code>name</code> must qualify for a file
	 * name. Recommended to specify a name that ends with <code>"-settings"</code> so it will be evident what the persistent file is.<br>
	 * Example: if the specified name is <code>"server-settings"</code>, the persistent file will be <code>"server-settings.xml"</code> under the cache folder.
	 * </p>
	 * 
	 * <p>
	 * If the backing file already exists, settings will be loaded from them. Invalid settings or settings having invalid values will be silently discarded
	 * during the loading.
	 * </p>
	 * 
	 * <p>
	 * The settings bean will be associated with the specified name, and can also be queried later with the {@link #getSettingsBean(String)} method.
	 * </p>
	 * 
	 * <p>
	 * Settings beans acquired via this method will automatically appear in the application settings dialog.
	 * </p>
	 * 
	 * @param name name of the settings bean; this will also be the name of the backing persistent file
	 * @param validSettingList list of valid {@link ISetting}s managed by the settings bean
	 * 
	 * @return a new {@link ISettingsBean}
	 * @throws IllegalArgumentException if a settings bean with the specified name has already been initialized
	 * 
	 * @see ISettingsBean
	 * @see #getSettingsBeanList()
	 * @see #getSettingsBean(String)
	 */
	ISettingsBean initSettingsBean( String name, List< ISetting< ? > > validSettingList ) throws IllegalArgumentException;
	
	/**
	 * Returns the list of initialized settings beans in initialization order.
	 * 
	 * @return the list of initialized settings beans in initialization order.
	 */
	List< ISettingsBean > getSettingsBeanList();
	
	/**
	 * Returns the {@link ISettingsBean} associated with the specified name.
	 * 
	 * <p>
	 * The settings bean must be initialized prior to this call with the {@link #initSettingsBean(String, List)}.
	 * </p>
	 * 
	 * @param name name of the settings bean to return
	 * @return the {@link ISettingsBean} associated with the specified name or <code>null</code> if no settings bean have been initialized for the specified
	 *         name
	 * 
	 * @see #initSettingsBean(String, List)
	 * @see #getSettingsBeanList()
	 */
	ISettingsBean getSettingsBean( String name );
	
}
