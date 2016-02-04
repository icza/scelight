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

import hu.sllauncher.bean.Bean;
import hu.sllauncher.bean.person.PersonBean;

import java.util.List;

import javax.swing.ImageIcon;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;

/**
 * External module reference bean.
 * 
 * <p>
 * Contains main module info like name and folder, and a URL to access the referenced {@link ModuleBean}.
 * </p>
 * 
 * @author Andras Belicza
 * 
 * @see ModuleBean
 */
@XmlAccessorType( XmlAccessType.FIELD )
public class ExtModRefBean extends Bean {
	
	/** Current bean version. */
	public static final int    BEAN_VER = 1;
	
	/** Module name. This must match the referenced module's folder. */
	private String             name;
	
	/**
	 * Mod-path relative folder path (where to place it, quarantine it).<br>
	 * Also used as the module identifier, this must be unique.
	 * <p>
	 * I leave it here to avoid the ext-mod developers to arbitrary changing this (that way settings would identify the old version). This must match the
	 * referenced module's folder.
	 * </p>
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
	
	/** URL of the referenced module bean. */
	private String             moduleBeanUrl;
	
	/** Lazily initialized icon of the module. */
	@XmlTransient
	private ImageIcon          icon;
	
	/**
	 * Creates a new {@link ExtModRefBean}.
	 */
	public ExtModRefBean() {
		super( BEAN_VER );
	}
	
	/**
	 * Returns the module name.
	 * 
	 * @return the module name
	 */
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
	
	/**
	 * Returns the module folder.
	 * 
	 * @return the module folder
	 */
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
	
	/**
	 * Returns the module icon image data in one of the formats of JPG, PNG or GIF, in size of 16x16.
	 * 
	 * @return the module icon image data
	 */
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
	
	/**
	 * Returns the list of authors of the module.
	 * 
	 * @return the list of authors
	 */
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
	
	/**
	 * Returns the module's home page URL
	 * 
	 * @return the module's home page URL
	 */
	public String getHomePage() {
		return homePage;
	}
	
	/**
	 * Sets the module's home page URL
	 * 
	 * @param homePage module's home page URL to be set
	 */
	public void setHomePage( String homePage ) {
		this.homePage = homePage;
	}
	
	/**
	 * Returns the short (1-line) description (plain text).
	 * 
	 * @return the short description
	 */
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
	
	/**
	 * Returns the URL of the referenced module bean.
	 * 
	 * @return the URL of the referenced module bean
	 */
	public String getModuleBeanUrl() {
		return moduleBeanUrl;
	}
	
	/**
	 * Sets the URL of the referenced module bean.
	 * 
	 * @param moduleBeanUrl URL of the referenced module bean to be set
	 */
	public void setModuleBeanUrl( String moduleBeanUrl ) {
		this.moduleBeanUrl = moduleBeanUrl;
	}
	
	/**
	 * Returns the lazily initialized icon of the module.
	 * <p>
	 * The icon is created from the icon image data returned by {@link #getIconImgData()}.
	 * </p>
	 * 
	 * @return the lazily initialized icon of the module
	 */
	public ImageIcon getIcon() {
		if ( icon == null ) {
			icon = iconImgData == null ? new ImageIcon() : new ImageIcon( iconImgData );
			// Set a description so that if this icon is used in a table, copying cells will not result in a meaningless
			// or strange text.
			icon.setDescription( "I" );
		}
		
		return icon;
	}
	
}
