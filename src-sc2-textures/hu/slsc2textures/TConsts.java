/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.slsc2textures;

import hu.sllauncher.bean.VersionBean;

/**
 * Constants of the SC2 textures. These are real constants, can be inlined.
 * 
 * <p>
 * <i>Implementation note:</i> This is an interface instead of a class because constants defined in an interface are implicitly <code>public</code>,
 * <code>static</code> and <code>final</code>, and so these keywords can be omitted!
 * </p>
 * 
 * @author Andras Belicza
 */
public interface TConsts {
	
	/** SC2 Textures name. */
	String      SC2_TEXTURES_NAME    = "SC2 Textures";
	
	/** SC2 Textures version. */
	VersionBean SC2_TEXTURES_VERSION = new VersionBean( 2, 0 );
	
}
