/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllibs;

import hu.sllauncher.bean.VersionBean;

/**
 * Constants of the application libraries. These are real constants, can be inlined.
 * 
 * <p>
 * <i>Implementation note:</i> This is an interface instead of a class because constants defined in an interface are implicitly <code>public</code>,
 * <code>static</code> and <code>final</code>, and so these keywords can be omitted!
 * </p>
 * 
 * @author Andras Belicza
 */
public interface AConsts {
	
	/** Application Libs name. */
	String      APP_LIBS_NAME    = "Scelight Libs";
	
	/** Application libraries version. */
	VersionBean APP_LIBS_VERSION = new VersionBean( 1, 5, 2 );
	
}
