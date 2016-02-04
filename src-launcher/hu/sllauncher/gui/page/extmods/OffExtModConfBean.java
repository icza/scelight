/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllauncher.gui.page.extmods;

import hu.sllauncher.bean.Bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * Official external module config bean.
 * 
 * @author Andras Belicza
 */
@XmlAccessorType( XmlAccessType.FIELD )
public class OffExtModConfBean extends Bean {
	
	/** Current bean version. */
	public static final int BEAN_VER = 1;
	
	/** Folder (id) of the external module. */
	private String          folder;
	
	/** Tells if the module is to be installed / auto-updated. */
	private Boolean         autoUpdate;
	
	/**
	 * Creates a new {@link OffExtModConfBean}.
	 */
	public OffExtModConfBean() {
		super( BEAN_VER );
	}
	
	/**
	 * Returns the folder of the external module.
	 * 
	 * @return the folder of the external module
	 */
	public String getFolder() {
		return folder;
	}
	
	/**
	 * Sets the folder of the external module.
	 * 
	 * @param folder folder of the external to be set
	 */
	public void setFolder( String folder ) {
		this.folder = folder;
	}
	
	/**
	 * Returns the auto-update property.
	 * 
	 * @return whether the external module to be installed / auto-updated
	 */
	public Boolean getAutoUpdate() {
		return autoUpdate;
	}
	
	/**
	 * Sets the auto-update property.
	 * 
	 * @param autoUpdate autoUpdate property value to be set (whether the external module to be installed / auto-updated)
	 */
	public void setAutoUpdate( Boolean autoUpdate ) {
		this.autoUpdate = autoUpdate;
	}
	
}
