/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllauncher.bean.settings.adapter;

import hu.scelightapibase.bean.settings.type.ISetting;
import hu.sllauncher.bean.settings.type.Setting;
import hu.sllauncher.util.LUtils;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * An {@link XmlAdapter} which converts {@link Map}s to {@link StringMapEntryBean} arrays and vice versa.
 * 
 * <p>
 * Settings listed in the saved settings XML file which contain an invalid full setting id will be discarded. This can happen if old settings are loaded by a
 * newer version where a setting was removed or its id was changed.<br>
 * Invalid values are also discarded (when {@link Setting#parseValue(String)} returns <code>null</code>.
 * </p>
 * 
 * @author Andras Belicza
 */
public class SettingsMapAdapter extends XmlAdapter< StringMapEntryBean[], Map< ISetting< ? >, Object > > {
	
	@Override
	public Map< ISetting< ? >, Object > unmarshal( final StringMapEntryBean[] v ) throws Exception {
		final Map< ISetting< ? >, Object > settingsMap = LUtils.newHashMap( v == null ? 0 : v.length );
		
		if ( v == null )
			return settingsMap;
		
		for ( final StringMapEntryBean entry : v ) {
			// Find setting for full id
			final String fullId = entry.getKey();
			final ISetting< ? > setting = Setting.FULL_ID_SETTING_MAP.get( fullId );
			
			if ( setting != null ) {
				final Object value = setting.parseValue( entry.getValue() );
				if ( value != null )
					settingsMap.put( setting, value );
			}
		}
		
		return settingsMap;
	}
	
	/*
	 * We can omit unchecked cast because a value associated with an existing setting can only be of the type of the setting value.
	 */
	@Override
	@SuppressWarnings( "unchecked" )
	public StringMapEntryBean[] marshal( final Map< ISetting< ? >, Object > v ) throws Exception {
		if ( v == null || v.isEmpty() )
			return null;
		
		// First use a list rather than a fixed size array
		final ArrayList< StringMapEntryBean > entryList = new ArrayList<>( v.size() );
		
		for ( final Entry< ISetting< ? >, Object > entry : v.entrySet() )
			entryList.add( new StringMapEntryBean( entry.getKey().getFullId(), ( (Setting< Object >) entry.getKey() ).formatValue( entry.getValue() ) ) );
		
		return entryList.toArray( new StringMapEntryBean[ entryList.size() ] );
	}
	
}
