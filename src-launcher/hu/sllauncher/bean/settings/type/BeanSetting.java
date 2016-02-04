/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllauncher.bean.settings.type;

import hu.scelightapibase.bean.settings.type.ISetting;
import hu.scelightapibase.bean.settings.type.ISettingsGroup;
import hu.scelightapibase.bean.settings.type.viewhints.IViewHints;
import hu.scelightapibase.util.ISkillLevel;
import hu.sllauncher.bean.Bean;
import hu.sllauncher.service.env.LEnv;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXB;

/**
 * A bean setting.
 * 
 * <p>
 * Bean instances are not immutable. Because of this, there are some rules working with setting beans:
 * </p>
 * <ul>
 * <li>The default value cannot be modified!
 * <li>The setting value acquired from a settings bean cannot be modified, but can be read.
 * <li>If the setting value (or any of the bean's properties) have to be modified, modification has to be performed on a cloned instance (
 * {@link Bean#cloneBean()}), and the cloned instance must be set to the settings bean.
 * </ul>
 * 
 * @param <T> setting value type
 * 
 * @author Andras Belicza
 */
public class BeanSetting< T extends Bean > extends Setting< T > {
	
	/**
	 * Creates a new {@link BeanSetting}.
	 * 
	 * @param id setting id
	 * @param parent parent setting to be added to
	 * @param group settings group this setting belongs to (within a setting node page)
	 * @param skillLevel setting skill level
	 * @param name setting name
	 * @param viewHints optional view hints of the setting
	 * @param defaultValue default setting value
	 */
	public BeanSetting( final String id, final ISetting< ? > parent, final ISettingsGroup group, final ISkillLevel skillLevel, final String name,
	        final IViewHints viewHints, final T defaultValue ) {
		super( id, parent, group, skillLevel, name, viewHints, defaultValue );
	}
	
	@Override
	public String formatValue( final T value ) {
		try {
			// XML header plus a simple line for a property is about 160 bytes,
			// also give a little extra to minimize reallocations.
			final StringWriter wr = new StringWriter( 300 );
			JAXB.marshal( value, wr );
			return wr.toString();
		} catch ( final Exception e ) {
			LEnv.LOGGER.error( "Failed to marshal setting value bean: " + this.toString(), e );
			return null;
		}
	}
	
	@Override
	@SuppressWarnings( "unchecked" )
	public T parseValue( final String src ) {
		try {
			return (T) JAXB.unmarshal( new StringReader( src ), defaultValue.getClass() );
		} catch ( final Exception e ) {
			// If saved value was manually manipulated...
			return null;
		}
	}
	
}
