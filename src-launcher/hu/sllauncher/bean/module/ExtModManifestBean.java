/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllauncher.bean.module;

import hu.scelightapibase.bean.IExtModManifestBean;
import hu.sllauncher.bean.Bean;
import hu.sllauncher.bean.BuildInfoBean;
import hu.sllauncher.bean.VersionBean;
import hu.sllauncher.bean.person.PersonBean;
import hu.sllauncher.service.updater.Updater;

import java.util.List;

import javax.swing.ImageIcon;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;

/**
 * External Module Manifest bean.
 * 
 * <p>
 * Contains module info like name, authors, release date, long description, main class (entry point).<br>
 * Must be placed in the root of the version folder of a module in a file named <code>"Scelight-mod-x-manifest.xml"</code>.
 * </p>
 * 
 * <p>
 * Modules are identified by their <code>folder</code> attribute, must be unique amongst modules.
 * </p>
 * 
 * @author Andras Belicza
 */
@XmlAccessorType( XmlAccessType.FIELD )
public class ExtModManifestBean extends Bean implements IExtModManifestBean {
	
	/** Current bean version. */
	public static final int    BEAN_VER = 1;
	
	/** Module name. */
	private String             name;
	
	/** Module version. */
	private VersionBean        version;
	
	/** Build info. */
	private BuildInfoBean      buildInfo;
	
	/** Used External Module API version. */
	private VersionBean        apiVersion;
	
	/**
	 * Mod-path relative folder path (where to place it, quarantine it).<br>
	 * Also used as the module identifier, must be unique amongst modules.
	 */
	private String             folder;
	
	/** Module icon image data in one of the formats of JPG, PNG or GIF, in size of 16x16. */
	private byte[]             iconImgData;
	
	/** List of authors of the module. */
	private List< PersonBean > authorList;
	
	/** Module's home page URL. */
	private String             homePage;
	
	/** Short (1-line) description (plain text). */
	private String             shortDesc;
	
	/**
	 * HTML description of the module.<br>
	 * HTML support: <a href="http://www.w3.org/TR/REC-html32.html">HTML 3.2</a> with no scripts allowed (not embedded nor referenced).
	 */
	private String             description;
	
	/**
	 * Main class (entry point) of the module.<br>
	 * Must be unique. Must implement {@link hu.scelightapi.IExternalModule IExternalModule} and must have a no-arg constructor.
	 */
	private String             mainClass;
	
	
	/** Lazily initialized icon of the module. */
	@XmlTransient
	private ImageIcon          icon;
	
	/** Module bean constructed by the {@link Updater#detectInstalledModules(java.nio.file.Path)}. */
	@XmlTransient
	private ModuleBean         moduleBean;
	
	
	/**
	 * Creates a new {@link ExtModManifestBean}.
	 */
	public ExtModManifestBean() {
		super( BEAN_VER );
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the module name.
	 * 
	 * @param name module name to be set
	 */
	public void setName( String name ) {
		this.name = name;
	}
	
	@Override
	public VersionBean getVersion() {
		return version;
	}
	
	/**
	 * Sets the module version.
	 * 
	 * @param version module version to be set
	 */
	public void setVersion( VersionBean version ) {
		this.version = version;
	}
	
	@Override
	public BuildInfoBean getBuildInfo() {
		return buildInfo;
	}
	
	/**
	 * Sets the build info.
	 * 
	 * @param buildInfo build info to be set
	 */
	public void setBuildInfo( BuildInfoBean buildInfo ) {
		this.buildInfo = buildInfo;
	}
	
	@Override
	public VersionBean getApiVersion() {
		return apiVersion;
	}
	
	/**
	 * Sets the used External Module API version.
	 * 
	 * @param apiVersion used External Module API version to be set
	 */
	public void setApiVersion( VersionBean apiVersion ) {
		this.apiVersion = apiVersion;
	}
	
	@Override
	public String getFolder() {
		return folder;
	}
	
	/**
	 * Sets the module folder.
	 * 
	 * @param folder module folder to be set
	 */
	public void setFolder( String folder ) {
		this.folder = folder;
	}
	
	@Override
	public byte[] getIconImgData() {
		return iconImgData;
	}
	
	/**
	 * Sets the module icon image data in one of the formats of JPG, PNG or GIF, in size of 16x16.
	 * 
	 * @param iconImgData module icon image data to be set
	 */
	public void setIconImgData( byte[] iconImgData ) {
		this.iconImgData = iconImgData;
	}
	
	@Override
	public List< PersonBean > getAuthorList() {
		return authorList;
	}
	
	/**
	 * Sets the list of authors of the module.
	 * 
	 * @param authorList list of authors to be set
	 */
	public void setAuthorList( List< PersonBean > authorList ) {
		this.authorList = authorList;
	}
	
	@Override
	public String getHomePage() {
		return homePage;
	}
	
	/**
	 * Sets the module's home page URL.
	 * 
	 * @param homePage module's home page URL to be set
	 */
	public void setHomePage( String homePage ) {
		this.homePage = homePage;
	}
	
	@Override
	public String getShortDesc() {
		return shortDesc;
	}
	
	/**
	 * Sets the short (1-line) description (plain text).
	 * 
	 * @param shortDesc short description to be set
	 */
	public void setShortDesc( String shortDesc ) {
		this.shortDesc = shortDesc;
	}
	
	@Override
	public String getDescription() {
		return description;
	}
	
	/**
	 * Sets the (long) HTML description of the module.
	 * 
	 * <p>
	 * HTML support: <a href="http://www.w3.org/TR/REC-html32.html">HTML 3.2</a> with no scripts allowed (not embedded nor referenced).
	 * </p>
	 * 
	 * @param description HTML description of the module to be set
	 */
	public void setDescription( String description ) {
		this.description = description;
	}
	
	@Override
	public String getMainClass() {
		return mainClass;
	}
	
	/**
	 * Sets the main class (entry point) of the module.<br>
	 * Must be unique. Must implement {@link hu.scelightapi.IExternalModule IExternalModule} and must have a no-arg constructor.
	 * 
	 * @param mainClass main class of the module to be set
	 */
	public void setMainClass( String mainClass ) {
		this.mainClass = mainClass;
	}
	
	@Override
	public ImageIcon getIcon() {
		if ( icon == null ) {
			icon = iconImgData == null ? new ImageIcon() : new ImageIcon( iconImgData );
			// Set a description so that if this icon is used in a table, copying cells will not result in a meaningless
			// or strange text.
			icon.setDescription( "I" );
		}
		
		return icon;
	}
	
	/**
	 * Returns the module bean constructed by the {@link Updater#detectInstalledModules(java.nio.file.Path)}.
	 * 
	 * @return the moduleBean
	 */
	public ModuleBean getModuleBean() {
		return moduleBean;
	}
	
	/**
	 * Sets the module bean constructed by the {@link Updater#detectInstalledModules(java.nio.file.Path)}.
	 * 
	 * @param moduleBean the moduleBean to set
	 */
	public void setModuleBean( ModuleBean moduleBean ) {
		this.moduleBean = moduleBean;
	}
	
}
