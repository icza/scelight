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

import hu.scelightapibase.bean.settings.type.INodeSetting;
import hu.scelightapibase.bean.settings.type.ISetting;
import hu.scelightapibase.gui.icon.IRIcon;
import hu.sllauncher.bean.settings.type.viewhints.VHB;
import hu.sllauncher.service.env.LEnv;
import hu.sllauncher.util.SkillLevel;

/**
 * A setting which is used only as a node in the navigation tree.
 * 
 * @author Andras Belicza
 */
public class NodeSetting extends Setting< Boolean > implements INodeSetting {
	
	/**
	 * Creates a new {@link NodeSetting}.
	 * 
	 * @param id setting id
	 * @param parent parent setting to be added to
	 * @param name setting name
	 * @param ricon setting ricon
	 */
	public NodeSetting( final String id, final ISetting< ? > parent, final String name, final IRIcon ricon ) {
		super( id, parent, null, SkillLevel.HIDDEN, name, new VHB().ricon( ricon ).build(), Boolean.FALSE );
		
		if ( LEnv.DEV_MODE && ricon == null )
			throw new RuntimeException( "Node settings cannot have null ricon!" );
	}
	
	@Override
	public Boolean parseValue( final String src ) {
		// Doesn't matter what we return, node settings are not used to store settings.
		return Boolean.FALSE;
	}
	
}
