/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllauncher.bean.settings;

import hu.scelightapibase.bean.settings.ISettingChangeListener;
import hu.scelightapibase.bean.settings.ISettingsBean;
import hu.scelightapibase.bean.settings.type.INodeSetting;
import hu.scelightapibase.bean.settings.type.ISetting;
import hu.sllauncher.bean.Bean;
import hu.sllauncher.bean.VersionBean;
import hu.sllauncher.bean.settings.adapter.SettingsMapAdapter;
import hu.sllauncher.service.env.LEnv;

import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import javax.xml.bind.JAXB;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * Settings bean implementation.
 * 
 * <p>
 * Implements change tracking which is turned off by default.
 * </p>
 * 
 * <p>
 * Also implements registering and notifying setting change listeners.
 * </p>
 * 
 * @author Andras Belicza
 */
@XmlAccessorType( XmlAccessType.FIELD )
public class SettingsBean extends Bean implements ISettingsBean {
	
	/**
	 * Utility method which creates a list of settings from the fields of the specified class which stores a collection of settings as public static class
	 * fields.
	 * 
	 * @param settingCollectionClass class holding settings as public static fields
	 * @return a list of settings gained from the specified class
	 */
	public static List< ISetting< ? > > createSettingListFromFields( final Class< ? > settingCollectionClass ) {
		final List< ISetting< ? > > settingList = new ArrayList<>();
		
		for ( final Field field : settingCollectionClass.getDeclaredFields() ) {
			if ( !ISetting.class.isAssignableFrom( field.getType() ) )
				continue; // It's not a setting field...
				
			final ISetting< ? > setting;
			try {
				setting = (ISetting< ? >) field.get( settingCollectionClass );
			} catch ( final IllegalArgumentException | IllegalAccessException e ) {
				// Never to happen...
				LEnv.LOGGER.error( "Failed to get setting field!", e );
				continue;
			}
			
			if ( setting instanceof INodeSetting )
				continue; // Page nodes are not used to store an actual value
				
			settingList.add( setting );
		}
		
		return settingList;
	}
	
	
	/** Current bean version. */
	public static final int                                           BEAN_VER                    = 1;
	
	
	
	// Change tracking implementation
	
	/** Tells if change tracking is enabled. */
	@XmlTransient
	private boolean                                                   trackChanges;
	
	/** Set storing the tracked setting changes. */
	@XmlTransient
	private final Set< ISetting< ? > >                                changedSettingSet           = new HashSet<>();
	
	
	
	// Setting change notifier implementation
	
	/**
	 * Map storing the setting change listeners.<br>
	 * Key is the listened setting; the value is a {@link Set} of listeners.<br>
	 * The value is a set-view of a {@link WeakHashMap} storing the listeners as weak-keys of the map; and it is created with
	 * {@link Collections#newSetFromMap(Map)}.
	 */
	@XmlTransient
	private final Map< ISetting< ? >, Set< ISettingChangeListener > > settingChangeListenerSetMap = new HashMap<>();
	
	
	
	// Other properties saved with the settings
	
	/** Name of module that saved the settings. */
	private String                                                    savedByModuleName;
	
	/** Version of module that saved the settings. */
	private VersionBean                                               savedByModuleVersion;
	
	/** Path to save the settings to. */
	@XmlTransient
	private Path                                                      path;
	
	/** Time when the settings were saved. */
	private Date                                                      saveTime;
	
	
	
	// And finally the setting value map. Put it here last so it will be serialized last, and other properties will appear at the beginning of the output XML.
	// NOTE: if this map is before other properties, JAXB can't marshal it (NullPointerException is thrown...).
	
	/**
	 * Map storing the non-default settings. Only settings diverging from the default setting values are stored here.
	 */
	@XmlJavaTypeAdapter( value = SettingsMapAdapter.class )
	private final Map< ISetting< ? >, Object >                        settingValueMap             = new HashMap<>();
	
	
	/** List of valid settings that this settings bean is used for. */
	@XmlTransient
	private List< ISetting< ? > >                                     validSettingList;
	
	
	
	/**
	 * Creates a new {@link SettingsBean}.
	 */
	public SettingsBean() {
		super( BEAN_VER );
	}
	
	/**
	 * Configures additional properties for saving.
	 * 
	 * @param moduleName name of module that saves the settings
	 * @param moduleVersion version of the module that saves the settings
	 * @param path path to save the settings to
	 */
	public void configureSave( final String moduleName, final VersionBean moduleVersion, final Path path ) {
		this.savedByModuleName = moduleName;
		this.savedByModuleVersion = moduleVersion;
		this.path = path;
	}
	
	/**
	 * Sets the list of valid settings that this settings bean is used for.
	 * 
	 * <p>
	 * Also purges the settings map: removes all settings that are not listed in the specified valid settings list.
	 * </p>
	 * 
	 * @param validSettingList the list of valid settings that this settings bean is used for to be set
	 */
	public void setValidSettingList( final List< ISetting< ? > > validSettingList ) {
		this.validSettingList = validSettingList;
		
		// Purge (keyset is backed by the map, removing elements from it will also remove elements from the map):
		settingValueMap.keySet().retainAll( validSettingList );
	}
	
	@Override
	public List< ISetting< ? > > getValidSettingList() {
		return validSettingList;
	}
	
	@Override
	public String getSettingValueMapString() {
		return settingValueMap.toString();
	}
	
	@Override
	public < T > T get( final ISetting< T > setting ) {

		
		@SuppressWarnings( "unchecked" )
		final T value = (T) settingValueMap.get( setting );
		
		return value == null ? setting.getDefaultValue() : value;
	}
	
	@Override
	public void reset( final ISetting< ? > setting ) {
		if ( settingValueMap.remove( setting ) != null ) {
			if ( trackChanges )
				changedSettingSet.add( setting );
			
			notifyListeners( setting.selfSet() );
		}
	}
	
	@Override
	public < T > void set( final ISetting< T > setting, final T value ) {
		// Do not store default value:
		if ( setting.getDefaultValue().equals( value ) ) {
			if ( settingValueMap.remove( setting ) != null ) {
				if ( trackChanges )
					changedSettingSet.add( setting );
				notifyListeners( setting.selfSet() );
			}
		} else {
			// Previous value might have been null (default value is not stored),
			// so compare value to previous value (and not the other way).
			// If value would be default value, then we would be in the other if branch (and not on this else branch).
			if ( !value.equals( settingValueMap.put( setting, value ) ) ) {
				if ( trackChanges )
					changedSettingSet.add( setting );
				notifyListeners( setting.selfSet() );
			}
		}
	}
	
	@Override
	public void addChangeListener( final ISetting< ? > setting, final ISettingChangeListener listener ) {
		addChangeListener( setting.selfSet(), listener );
	}
	
	@Override
	public void addChangeListener( final Set< ? extends ISetting< ? > > settingSet, final ISettingChangeListener listener ) {
		for ( final ISetting< ? > setting : settingSet ) {
			Set< ISettingChangeListener > listenerSet = settingChangeListenerSetMap.get( setting );
			
			if ( listenerSet == null )
				settingChangeListenerSetMap.put( setting, listenerSet = Collections.newSetFromMap( new WeakHashMap< ISettingChangeListener, Boolean >() ) );
			
			listenerSet.add( listener );
		}
	}
	
	@Override
	public void addAndExecuteChangeListener( final ISetting< ? > setting, final ISettingChangeListener listener ) {
		addAndExecuteChangeListener( setting.selfSet(), listener );
	}
	
	@Override
	public void addAndExecuteChangeListener( final Set< ? extends ISetting< ? > > settingSet, final ISettingChangeListener listener ) {
		addChangeListener( settingSet, listener );
		listener.valuesChanged( new SettingChangeEvent( this, settingSet ) );
	}
	
	@Override
	public void removeChangeListener( final ISetting< ? > setting, final ISettingChangeListener listener ) {
		removeChangeListener( setting.selfSet(), listener );
	}
	
	@Override
	public void removeChangeListener( final Set< ? extends ISetting< ? > > settingSet, final ISettingChangeListener listener ) {
		for ( final ISetting< ? > setting : settingSet ) {
			final Set< ISettingChangeListener > listenerSet = settingChangeListenerSetMap.get( setting );
			if ( listenerSet != null )
				listenerSet.remove( listener );
		}
	}
	
	/**
	 * Notifies the listeners registered to the specified settings.
	 * 
	 * @param settingSet set of settings whose values have changed
	 */
	private void notifyListeners( final Set< ? extends ISetting< ? > > settingSet ) {
		// Only call once each listener that is registered to any of the specified setting keys!
		// For this, we first collect the listeners to be called:
		final Set< ISettingChangeListener > listenerSet = new HashSet<>();
		
		for ( final ISetting< ? > setting : settingSet ) {
			final Set< ISettingChangeListener > listenerSet2 = settingChangeListenerSetMap.get( setting );
			if ( listenerSet2 != null )
				listenerSet.addAll( listenerSet2 );
		}
		
		if ( !listenerSet.isEmpty() ) {
			final SettingChangeEvent event = new SettingChangeEvent( this, settingSet );
			for ( final ISettingChangeListener listener : listenerSet )
				listener.valuesChanged( event );
		}
	}
	
	/**
	 * @see #configureSave(String, VersionBean, Path)
	 */
	@Override
	public void save() {
		saveTime = new Date();
		
		try {
			JAXB.marshal( this, path.toFile() );
		} catch ( final Exception e ) {
			e.printStackTrace();
			LEnv.LOGGER.error( "Failed to save " + savedByModuleName + " settings to: " + path + "\nDo you have write permission in the folder?", e );
		}
	}
	
	@Override
	public SettingsBean cloneSettings() {
		final SettingsBean clonedSettings;
		try {
			clonedSettings = getClass().newInstance();
		} catch ( final InstantiationException | IllegalAccessException e ) {
			LEnv.LOGGER.error( "Failed to clone settings!", e );
			return null;
		}
		
		clonedSettings.settingValueMap.putAll( settingValueMap );
		clonedSettings.validSettingList = new ArrayList<>( validSettingList );
		clonedSettings.path = path;
		
		return clonedSettings;
	}
	
	@Override
	public void copyChangedSettingsTo( final ISettingsBean targetSettings_ ) {
		if ( !( targetSettings_ instanceof SettingsBean ) )
			throw new IllegalArgumentException( "Cannot copy settings to target: it is of differnet type!" );
		
		final SettingsBean targetSettings = (SettingsBean) targetSettings_;
		if ( !path.equals( targetSettings.path ) )
			throw new IllegalArgumentException( "Copying settings to a target with different save path is denied!" );
		
		// Copy all changed settings and notify listeners in one step for optimization reasons
		// (listeners might perform lengthy operations when a setting changes).
		
		// Simply copying the properties is not enough, because if a setting previously was not the default value
		// and was changed to the default value, it is removed from the properties (and it is not copied).
		// Remove those by ourselves:
		
		final Map< ISetting< ? >, Object > thisSettingValueMap = this.settingValueMap;
		final Map< ISetting< ? >, Object > targetSettingValueMap = targetSettings.settingValueMap;
		
		for ( final ISetting< ? > setting : changedSettingSet ) {
			if ( thisSettingValueMap.containsKey( setting ) )
				targetSettingValueMap.put( setting, thisSettingValueMap.get( setting ) );
			else
				targetSettingValueMap.remove( setting );
		}
		
		// Notify listeners using the whole changed set
		targetSettings.notifyListeners( changedSettingSet );
	}
	
	@Override
	public void setTrackChanges( final boolean trackChanges ) {
		this.trackChanges = trackChanges;
	}
	
	@Override
	public boolean isTrackChanges() {
		return trackChanges;
	}
	
	@Override
	public void clearTrackedChanges() {
		changedSettingSet.clear();
	}
	
	@Override
	public String getSavedByModuleName() {
		return savedByModuleName;
	}
	
	@Override
	public VersionBean getSavedByModuleVersion() {
		return savedByModuleVersion;
	}
	
	@Override
	public Date getSaveTime() {
		return saveTime;
	}
	
}
