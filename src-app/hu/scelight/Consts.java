/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight;

import java.net.URL;

import hu.scelight.util.Utils;
import hu.scelightapi.XConsts;
import hu.sllauncher.LConsts;
import hu.sllauncher.bean.VersionBean;
import hu.sllibs.AConsts;
import hu.slsc2balancedata.BConsts;
import hu.slsc2textures.TConsts;

/**
 * Constants of the application. These are real constants, can be inlined.
 * 
 * <p>
 * <i>Implementation note:</i> This is an interface instead of a class because constants defined in an interface are implicitly <code>public</code>,
 * <code>static</code> and <code>final</code>, and so these keywords can be omitted!
 * </p>
 * 
 * @author Andras Belicza
 */
public interface Consts extends LConsts, TConsts, BConsts, AConsts, XConsts {
	
	/** Application version. */
	VersionBean	APP_VERSION					 = new VersionBean( 6, 5, 4 );
											 
	/** Application full name (app name + app version). */
	String		APP_NAME_FULL				 = APP_NAME + " " + APP_VERSION;
											 
											 
	/** Time between scheduled update checks, in milliseconds. */
	int			SCHEDULED_UPDATE_CHECK_DELAY = 60 * 60 * 1000;
											 
	/** Project page URL (source code). */
	URL			URL_PROJECT_PAGE			 = Utils.createUrl( "https://github.com/icza/scelight" );
											 
}
