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
import hu.sllauncher.bean.settings.type.viewhints.VHB;
import hu.sllauncher.bean.settings.type.viewhints.ViewHints;
import hu.sllauncher.util.OneValueSet;
import hu.sllauncher.util.SkillLevel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * A hierarchical ancestor setting descriptor.
 * 
 * @param <T> setting value type; it HAS to be an IMMUTABLE type (because returned values are by reference and are not cloned!)
 * 
 * @author Andras Belicza
 */
public abstract class Setting< T > implements ISetting< T > {
	
	/** A global map of all created settings. */
	public static final Map< String, Setting< ? > > FULL_ID_SETTING_MAP = new HashMap<>();
	
	
	
	/** Id separator in the full id of a setting. */
	public static final char                        ID_SEPARATOR        = '/';
	
	
	/** Unmodifiable set containing only <code>this</code>. */
	public final Set< Setting< T > >                SELF_SET            = new OneValueSet<>( this );
	
	
	/** Empty view hints to be set if view hints is not specified. */
	private static final ViewHints                  EMPTY_VIEW_HINTS    = new VHB().build();
	
	
	/** Setting id. */
	public final String                             id;
	
	/** Optional parent setting. */
	public final ISetting< ? >                      parent;
	
	/** Settings group this setting belongs to (within a setting node page). */
	public final ISettingsGroup                     group;
	
	/** Full id including parent id (recursive). */
	public final String                             fullId;
	
	/** Setting skill level. */
	public final SkillLevel                         skillLevel;
	
	/** Setting name. */
	public final String                             name;
	
	/** View hints of the setting (always not null). */
	public final IViewHints                         viewHints;
	
	/** Default setting value. */
	public final T                                  defaultValue;
	
	/** Child setting list. */
	private List< ISetting< ? > >                   childList;
	
	/**
	 * Creates a new {@link Setting}.
	 * 
	 * <p>
	 * NOTE: The full setting id (id of this setting and the path specified by its parent) will be allocated for this setting and another setting with the same
	 * full id cannot be created. Attempting to do so will result in an {@link IllegalArgumentException}.
	 * </p>
	 * 
	 * @param id setting id
	 * @param parent parent setting to be added to
	 * @param group settings group this setting belongs to (within a setting node page)
	 * @param skillLevel setting skill level
	 * @param name setting name
	 * @param viewHints optional view hints of the setting
	 * @param defaultValue default setting value
	 */
	public Setting( final String id, final ISetting< ? > parent, final ISettingsGroup group, final ISkillLevel skillLevel, final String name,
	        final IViewHints viewHints, final T defaultValue ) {
		
		// Check general requirements:
		Objects.requireNonNull( id, "Id cannot be null!" );
		if ( skillLevel != SkillLevel.HIDDEN )
			Objects.requireNonNull( group, "Group cannot be null for non-hidden settings!" );
		Objects.requireNonNull( skillLevel, "Skill level cannot be null!" );
		Objects.requireNonNull( defaultValue, "Default value cannot be null!" );
		if ( id.indexOf( ID_SEPARATOR ) >= 0 )
			throw new IllegalArgumentException( "Id separator character '" + ID_SEPARATOR + "' cannot be used in ids: " + id );
		
		this.id = id;
		this.parent = parent;
		this.group = group;
		fullId = parent == null ? id : parent.getId() + ID_SEPARATOR + id;
		this.skillLevel = SkillLevel.valueOf( skillLevel.name() );
		this.name = name;
		this.viewHints = viewHints == null ? EMPTY_VIEW_HINTS : viewHints;
		this.defaultValue = defaultValue;
		
		synchronized ( FULL_ID_SETTING_MAP ) {
			// Check unique id constraint:
			if ( FULL_ID_SETTING_MAP.containsKey( fullId ) )
				throw new IllegalArgumentException( "A setting with the same full id already exists: " + fullId );
			
			FULL_ID_SETTING_MAP.put( fullId, this );
		}
		
		if ( parent != null )
			parent.addChild( this );
	}
	
	@Override
	public void addChild( final ISetting< ? > setting ) {
		if ( childList == null )
			childList = new ArrayList<>();
		
		childList.add( setting );
	}
	
	@Override
	public Set< ? extends ISetting< T > > selfSet() {
		return SELF_SET;
	}
	
	@Override
	public String getId() {
		return id;
	}
	
	@Override
	public ISetting< ? > getParent() {
		return parent;
	}
	
	@Override
	public ISettingsGroup getGroup() {
		return group;
	}
	
	@Override
	public String getFullId() {
		return fullId;
	}
	
	@Override
	public SkillLevel getSkillLevel() {
		return skillLevel;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public IViewHints getViewHints() {
		return viewHints;
	}
	
	@Override
	public T getDefaultValue() {
		return defaultValue;
	}
	
	/**
	 * Returns the child setting list.
	 * 
	 * @return the child setting list
	 */
	public List< ISetting< ? > > getChildList() {
		return childList;
	}
	
	@Override
	public String toString() {
		return "id:" + fullId;
	}
	
	@Override
	public void getSettingPath( final List< ISetting< ? > > settingList ) {
		if ( parent != null )
			parent.getSettingPath( settingList );
		
		settingList.add( this );
	}
	
	/**
	 * This default implementation returns <code>value.toString()</code>.
	 */
	@Override
	public String formatValue( final T value ) {
		return value.toString();
	}
	
	@Override
	public abstract T parseValue( String src );
	
}
