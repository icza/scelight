/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapibase.bean;

import hu.scelightapibase.bean.person.IPersonBean;

import java.util.List;

import javax.swing.ImageIcon;

/**
 * External Module Manifest bean interface.
 * 
 * <p>
 * Contains module info like name, authors, release date, long description, main class (entry point).
 * </p>
 * 
 * <p>
 * Modules are identified by their folder ({@link #getFolder()}), must be unique amongst modules.
 * </p>
 * 
 * @author Andras Belicza
 * 
 * @see hu.scelightapi.IExternalModule#init(IExtModManifestBean, hu.scelightapi.IServices, hu.scelightapi.IModEnv)
 * @see hu.scelightapi.IModEnv#getManifest()
 * @see IBean
 */
public interface IExtModManifestBean extends IBean {
	
	/**
	 * Returns the module name.
	 * 
	 * @return the module name
	 */
	String getName();
	
	/**
	 * Returns the module version.
	 * 
	 * @return the module version
	 */
	IVersionBean getVersion();
	
	/**
	 * Returns the build info.
	 * 
	 * @return the build info
	 */
	IBuildInfoBean getBuildInfo();
	
	/**
	 * Returns the used External Module API version.
	 * 
	 * <p>
	 * The External Module API version that was used when developing the external module should be returned. This might be checked for compatibility reasons.<br>
	 * The used API version must not be lower than the minimum version of the API whose services and parts are used by the module, and should be the highest
	 * possible.
	 * </p>
	 * 
	 * <p>
	 * For example if the module uses a method which was added in the API version 1.1, the returned used API version must be at least 1.1 but may be 1.2 for
	 * example if the module was developed with API version 1.2 even if the module does not uses 1.2 additions.
	 * </p>
	 * 
	 * <p>
	 * A good practice is to always return the API version the external module was developed and built with, and if an external module is modified and recompiled
	 * with a newer API version, that newer API version should be returned as the used External Module API version.
	 * </p>
	 * 
	 * @return the used External Module API version
	 * 
	 * @since 1.1
	 */
	IVersionBean getApiVersion();
	
	/**
	 * Returns the external module path relative folder.<br>
	 * Also used as the module identifier, must be unique amongst modules.
	 * 
	 * @return the module folder
	 */
	String getFolder();
	
	/**
	 * Returns the module icon image data in one of the formats of JPG, PNG or GIF, in size of 16x16.
	 * 
	 * @return the module icon image data
	 */
	byte[] getIconImgData();
	
	/**
	 * Returns the list of authors of the module.
	 * 
	 * @return the list of authors
	 */
	List< ? extends IPersonBean > getAuthorList();
	
	/**
	 * Returns the module's home page URL.
	 * 
	 * @return the home page URL
	 */
	String getHomePage();
	
	/**
	 * Returns the short (1-line) description (plain text).
	 * 
	 * @return the short description
	 */
	String getShortDesc();
	
	/**
	 * Returns the (long) HTML description of the module.
	 * 
	 * <p>
	 * HTML support: <a href="http://www.w3.org/TR/REC-html32.html">HTML 3.2</a> with no scripts allowed (neither embedded nor referenced).
	 * </p>
	 * 
	 * @return the HTML description of the module
	 */
	String getDescription();
	
	/**
	 * Returns the main class (entry point) of the module.<br>
	 * Must be unique. Must implement {@link hu.scelightapi.IExternalModule IExternalModule} and must have a no-arg constructor.
	 * 
	 * @return the main class of the module
	 */
	String getMainClass();
	
	/**
	 * Returns the lazily initialized icon of the module.
	 * <p>
	 * The icon is created from the icon image data returned by {@link #getIconImgData()}.
	 * </p>
	 * 
	 * @return the lazily initialized icon of the module
	 */
	ImageIcon getIcon();
	
}
