/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllauncher;

import hu.sllauncher.bean.VersionBean;
import hu.sllauncher.bean.person.PersonBean;
import hu.sllauncher.r.LR;
import hu.sllauncher.util.LUtils;

import java.net.URL;

import javax.xml.bind.JAXB;

/**
 * Constants of the launcher. These are real constants, can be inlined.
 * 
 * <p>
 * <i>Implementation note:</i> This is an interface instead of a class because constants defined in an interface are implicitly <code>public</code>,
 * <code>static</code> and <code>final</code>, and so these keywords can be omitted!
 * </p>
 * 
 * @author Andras Belicza
 */
public interface LConsts {
	
	/** Launcher name. */
	String		LAUNCHER_NAME	   = "Scelight Launcher";
								   
	/** Launcher version. */
	VersionBean	LAUNCHER_VERSION   = new VersionBean( 3, 1, 3 );
								   
	/** Launcher full name (launcher name + launcher version). */
	String		LAUNCHER_NAME_FULL = LAUNCHER_NAME + " " + LAUNCHER_VERSION;
								   
								   
	/** Application name. */
	String		APP_NAME		   = "Scelight";
								   
								   
	/** Application Operator name. */
	String		APP_OPERATOR_NAME  = APP_NAME + " Operator";
								   
								   
	/** Application author. */
	PersonBean	APP_AUTHOR		   = JAXB.unmarshal( LR.get( "bean/author.xml" ), PersonBean.class );
								   
	/** Home page URL. */
	URL			URL_HOME_PAGE	   = LUtils.createUrl( "https://sites.google.com/site/scelight/" );
								   
	/** Registration info page URL. */
	URL			URL_REGINFO_PAGE   = LUtils.createUrl( URL_HOME_PAGE, "registration" );
								   
	/** Downloads page URL. */
	URL			URL_DOWNLOADS_PAGE = LUtils.createUrl( URL_HOME_PAGE, "downloads" );
								   
}
