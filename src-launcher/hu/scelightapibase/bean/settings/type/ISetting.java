/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapibase.bean.settings.type;

import hu.scelightapibase.bean.settings.type.viewhints.IViewHints;
import hu.scelightapibase.util.ISkillLevel;

import java.util.List;
import java.util.Set;

/**
 * A hierarchical ancestor setting descriptor.
 * 
 * @param <T> setting value type; it HAS to be an IMMUTABLE type (because returned values are by reference and are not cloned!)
 * 
 * @author Andras Belicza
 */
public interface ISetting< T > {
	
	/**
	 * Adds a child setting to this setting.
	 * 
	 * @param setting child setting to be added
	 */
	void addChild( ISetting< ? > setting );
	
	/**
	 * Returns an unmodifiable set containing only <code>this</code>.
	 * 
	 * @return an unmodifiable set containing only <code>this</code>
	 */
	Set< ? extends ISetting< T > > selfSet();
	
	/**
	 * Returns the setting id.
	 * 
	 * @return the setting id
	 */
	String getId();
	
	/**
	 * Returns the optional parent setting.
	 * 
	 * @return the optional parent setting
	 */
	ISetting< ? > getParent();
	
	/**
	 * Returns the settings group this setting belongs to (within a setting node page).
	 * 
	 * @return the settings group this setting belongs to (within a setting node page)
	 */
	ISettingsGroup getGroup();
	
	/**
	 * Returns the full id including parent id (recursive).
	 * 
	 * @return Returns the full id including parent id (recursive)
	 */
	String getFullId();
	
	/**
	 * Returns the setting skill level.
	 * 
	 * @return the setting skill level
	 */
	ISkillLevel getSkillLevel();
	
	/**
	 * Returns the setting name.
	 * 
	 * @return the setting name
	 */
	String getName();
	
	/**
	 * Returns the view hints of the setting (always not null).
	 * 
	 * @return the view hints of the setting (always not null)
	 */
	IViewHints getViewHints();
	
	/**
	 * Returns the default setting value.
	 * 
	 * @return the default setting value
	 */
	T getDefaultValue();
	
	/**
	 * Returns the full id of the setting in the form of <code>"id:fullId"</code>.
	 * 
	 * @return the full id of the setting in the form of <code>"id:fullId"</code>
	 */
	@Override
	String toString();
	
	/**
	 * Returns the setting path assembled in the specified list.
	 * <p>
	 * The setting path is the list of settings starting from the root setting going down to this.
	 * </p>
	 * 
	 * @param settingList list to assemble the setting path in
	 */
	void getSettingPath( List< ISetting< ? > > settingList );
	
	/**
	 * Formats a value to a string.
	 * 
	 * @param value value to be formatted
	 * @return the string representation of the value
	 */
	String formatValue( T value );
	
	/**
	 * Parses a value from a string representation.
	 * 
	 * @param src source to parse the value from
	 * @return the parsed value, or <code>null</code> if src contains an invalid setting value
	 */
	T parseValue( String src );
	
}
