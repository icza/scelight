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

import hu.scelightapibase.bean.settings.type.ISettingsGroup;
import hu.scelightapibase.util.IRHtml;

/**
 * A logical/visual settings group within a setting node page.
 * 
 * @author Andras Belicza
 */
public class SettingsGroup implements ISettingsGroup {
	
	/** Name of the settings group. */
	public final String name;
	
	/** Help HTML resource of the settings group. */
	public final IRHtml helpRhtml;
	
	/**
	 * Creates a new {@link SettingsGroup}.
	 * 
	 * @param name name of the settings group
	 */
	public SettingsGroup( final String name ) {
		this( name, null );
	}
	
	/**
	 * Creates a new {@link SettingsGroup}.
	 * 
	 * @param name name of the settings group
	 * @param helpRhtml help HTML resource of the settings group
	 */
	public SettingsGroup( final String name, final IRHtml helpRhtml ) {
		this.name = name;
		this.helpRhtml = helpRhtml;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public IRHtml gethelpRhtml() {
		return helpRhtml;
	}
	
}
