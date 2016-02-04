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

import hu.scelightapibase.bean.settings.ISettingChangeEvent;
import hu.scelightapibase.bean.settings.ISettingsBean;
import hu.scelightapibase.bean.settings.type.ISetting;

import java.util.Set;

/**
 * Event describing setting changes.
 * 
 * @author Andras Belicza
 */
public class SettingChangeEvent implements ISettingChangeEvent {
	
	/** Settings bean that fired the setting change event. */
	public final ISettingsBean                   settings;
	
	/**
	 * Set of settings whose values have changed.
	 * <p>
	 * <i>Note:</i> this set might be larger than the set a listener was registered for (but it is guaranteed that at least 1 registered setting is contained in
	 * this set).
	 * </p>
	 * <p>
	 * <i>Also note</i> that there is no guarantee that the settings contained in this set really changed. (For example a setting might be changed in the
	 * settings dialog therefore marked as changed, then changed back to its previous value and still marked as changed therefore included when changes are
	 * applied).
	 * </p>
	 */
	private final Set< ? extends ISetting< ? > > settingSet;
	
	/**
	 * Creates a new {@link SettingChangeEvent}.
	 * 
	 * @param settings reference to the settings bean that fired the setting change event
	 * @param settingSet set of settings whose values have changed
	 */
	public SettingChangeEvent( final ISettingsBean settings, final Set< ? extends ISetting< ? > > settingSet ) {
		this.settings = settings;
		this.settingSet = settingSet;
	}
	
	@Override
	public boolean affectedAny( final ISetting< ? >... settings ) {
		for ( final ISetting< ? > setting : settings )
			if ( affected( setting ) )
				return true;
		
		return false;
	}
	
	@Override
	public boolean affected( final ISetting< ? > setting ) {
		return settingSet.contains( setting );
	}
	
	@Override
	public < T > T get( final ISetting< T > setting ) {
		return settings.get( setting );
	}
	
	@Override
	public ISettingsBean getSettings() {
		return settings;
	}
	
}
