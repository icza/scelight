/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapi;

import hu.scelightapibase.bean.IVersionBean;
import hu.sllauncher.bean.VersionBean;

/**
 * Constants of the External Module API. These are real constants, can be inlined.
 * 
 * <p>
 * <i>Implementation note:</i> This is an interface instead of a class because constants defined in an interface are implicitly <code>public</code>,
 * <code>static</code> and <code>final</code>, and so these keywords can be omitted!
 * </p>
 * 
 * @author Andras Belicza
 */
public interface XConsts {
	
	/** External Module API name. */
	String		 EXT_MOD_API_NAME	 = "External Module API";
	
	/** External Module API version. */
	IVersionBean EXT_MOD_API_VERSION = new VersionBean( 1, 5, 2 );
	
}
