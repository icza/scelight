/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapibase.bean.settings;

import hu.scelightapibase.bean.IBean;
import hu.scelightapibase.bean.IVersionBean;
import hu.scelightapibase.bean.settings.type.ISetting;

import java.lang.ref.WeakReference;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Settings bean interface.
 * 
 * <p>
 * Implements change tracking which is turned off by default.
 * </p>
 * 
 * <p>
 * Also implements registering and notifying setting change listeners.
 * </p>
 * 
 * <p>
 * An instance of {@link ISettingsBean} can be acquired by {@link hu.scelightapi.IModEnv#initSettingsBean(String, java.util.List)}.
 * </p>
 * 
 * @author Andras Belicza
 * 
 * @see hu.scelightapi.IModEnv#initSettingsBean(String, List)
 * @see hu.scelightapi.IModEnv#getSettingsBean(String)
 * @see hu.scelightapi.gui.setting.ISettingsGui
 * @see IBean
 */
public interface ISettingsBean extends IBean {
	
	/**
	 * Returns the list of valid settings that this settings bean is used for.
	 * 
	 * @return the list of valid settings that this settings bean is used for
	 */
	List< ISetting< ? > > getValidSettingList();
	
	/**
	 * Returns the setting value map as a string.
	 * 
	 * @return the setting value map as a string
	 */
	String getSettingValueMapString();
	
	/**
	 * Returns the value of the specified setting.
	 * 
	 * @param <T> value type of the setting
	 * @param setting setting whose value to be returned
	 * @return the value of the specified setting
	 */
	< T > T get( ISetting< T > setting );
	
	/**
	 * Resets the value of the specified setting, restores the default value of the specified setting.<br>
	 * A setting is reset by removing its value (in which case when queried, the default value will be returned).
	 * 
	 * @param setting setting whose value to be reset
	 */
	void reset( ISetting< ? > setting );
	
	/**
	 * Sets the value of a setting.
	 * 
	 * @param <T> value type of the setting
	 * @param setting setting whose value to be set
	 * @param value value of the setting to be set
	 */
	< T > void set( ISetting< T > setting, T value );
	
	/**
	 * Adds a new setting change listener.
	 * 
	 * <p>
	 * <strong>Note:</strong> listeners are stored as {@link WeakReference}. This means that no need to remove registered listeners, but this also means that if
	 * an anonymous listener with no references is added, it will be removed shortly after!
	 * </p>
	 * 
	 * <p>
	 * Adding the same listener to the same setting key multiple times has no side effect (will only be stored and called once).
	 * </p>
	 * 
	 * @param setting setting whose changes to be notified for
	 * @param listener listener to be added
	 * 
	 * @see #addChangeListener(Set, ISettingChangeListener)
	 */
	void addChangeListener( ISetting< ? > setting, ISettingChangeListener listener );
	
	/**
	 * Adds a new setting change listener.
	 * 
	 * <p>
	 * <strong>Note:</strong> listeners are stored as {@link WeakReference}. This means that no need to remove registered listeners, but this also means that if
	 * an anonymous listener with no references is added, it will be removed shortly after!
	 * </p>
	 * 
	 * <p>
	 * Adding the same listener to the same setting key multiple times has no side effect (will only be stored and called once).
	 * </p>
	 * 
	 * @param settingSet set of settings whose changes to be notified for
	 * @param listener listener to be added
	 * 
	 * @see #addChangeListener(ISetting, ISettingChangeListener)
	 */
	void addChangeListener( Set< ? extends ISetting< ? > > settingSet, ISettingChangeListener listener );
	
	/**
	 * Adds a new setting change listener and executes it.
	 * 
	 * <p>
	 * For documentation see {@link #addChangeListener(ISetting, ISettingChangeListener)}.
	 * </p>
	 * 
	 * <p>
	 * <i>Executing</i> means to call the listener's {@link ISettingChangeListener#valuesChanged(ISettingChangeEvent)} method and passing the specified setting
	 * and <code>this</code> as the settings bean.
	 * </p>
	 * 
	 * @param setting setting whose changes to be notified for
	 * @param listener listener to be added
	 * 
	 * @see #addAndExecuteChangeListener(Set, ISettingChangeListener)
	 */
	void addAndExecuteChangeListener( ISetting< ? > setting, ISettingChangeListener listener );
	
	/**
	 * Adds a new setting change listener and executes it.
	 * 
	 * <p>
	 * For documentation see {@link #addChangeListener(Set, ISettingChangeListener)}.
	 * </p>
	 * 
	 * <p>
	 * <i>Executing</i> means to call the listener's {@link ISettingChangeListener#valuesChanged(ISettingChangeEvent)} method and passing the specified setting
	 * set and <code>this</code> as the settings bean.
	 * </p>
	 * 
	 * @param settingSet set of settings whose changes to be notified for
	 * @param listener listener to be added
	 * 
	 * @see #addAndExecuteChangeListener(ISetting, ISettingChangeListener)
	 */
	void addAndExecuteChangeListener( Set< ? extends ISetting< ? > > settingSet, ISettingChangeListener listener );
	
	/**
	 * Removes a setting change listener.
	 * 
	 * <p>
	 * Removing a listener which is not added is a no-op.
	 * </p>
	 * 
	 * @param setting setting to remove the listener from
	 * @param listener listener to be removed
	 * 
	 * @see #removeChangeListener(Set, ISettingChangeListener)
	 */
	void removeChangeListener( ISetting< ? > setting, ISettingChangeListener listener );
	
	/**
	 * Removes a setting change listener.
	 * 
	 * <p>
	 * Removing a listener which is not added is a no-op.
	 * </p>
	 * 
	 * @param settingSet set of settings to remove the listener from
	 * @param listener listener to be removed
	 * 
	 * @see #removeChangeListener(ISetting, ISettingChangeListener)
	 */
	void removeChangeListener( Set< ? extends ISetting< ? > > settingSet, ISettingChangeListener listener );
	
	/**
	 * Saves the settings.
	 */
	void save();
	
	/**
	 * Clones this settings.
	 * 
	 * <p>
	 * Only the following properties are copied, nothing else:
	 * <ul>
	 * <li>the stored setting values
	 * <li>the valid setting list
	 * <li>the save path
	 * </ul>
	 * </p>
	 * 
	 * @return a new settings instance having the same settings
	 */
	ISettingsBean cloneSettings();
	
	/**
	 * Copies the changed settings to the specified target settings.
	 * 
	 * <p>
	 * Only target settings with the same save path are allowed, else {@link IllegalArgumentException} will be thrown.<br>
	 * Moreover setting bean instances returned by the API can only copy to setting bean instances also returned by the API. If you pass an instance created by
	 * you, an {@link IllegalArgumentException} will be thrown.
	 * </p>
	 * 
	 * @param targetSettings target settings to copy the changed settings to
	 * @throws IllegalArgumentException if the target settings has a different save path
	 */
	void copyChangedSettingsTo( ISettingsBean targetSettings ) throws IllegalArgumentException;
	
	/**
	 * Sets the state of change tracking.
	 * 
	 * @param trackChanges state of change tracking to be set
	 */
	void setTrackChanges( boolean trackChanges );
	
	/**
	 * Tells if change tracking is enabled.
	 * 
	 * @return true if change tracking is enabled; false otherwise
	 */
	boolean isTrackChanges();
	
	/**
	 * Clears the previously tracked changes.
	 */
	void clearTrackedChanges();
	
	/**
	 * Returns the name of the module that saved the settings.
	 * 
	 * @return the name of the module that saved the settings
	 */
	String getSavedByModuleName();
	
	/**
	 * Returns the version of the module that saved the settings.
	 * 
	 * @return the version of the module that saved the settings
	 */
	IVersionBean getSavedByModuleVersion();
	
	/**
	 * Returns the time when the settings were saved.
	 * 
	 * @return the time when the settings were saved
	 */
	Date getSaveTime();
	
}
