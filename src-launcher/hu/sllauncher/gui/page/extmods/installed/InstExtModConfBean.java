/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllauncher.gui.page.extmods.installed;

import hu.sllauncher.bean.Bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * Installed external module config bean.
 * 
 * @author Andras Belicza
 */
@XmlAccessorType( XmlAccessType.FIELD )
public class InstExtModConfBean extends Bean {
	
	/** Current bean version. */
	public static final int BEAN_VER = 1;
	
	/** Folder (id) of the external module. */
	private String          folder;
	
	/** Tells if the module is enabled. */
	private Boolean         enabled;
	
	/**
	 * Creates a new {@link InstExtModConfBean}.
	 */
	public InstExtModConfBean() {
		super( BEAN_VER );
	}
	
	/**
	 * @return the folder of the external module
	 */
	public String getFolder() {
		return folder;
	}
	
	/**
	 * @param folder folder of the external to be set
	 */
	public void setFolder( String folder ) {
		this.folder = folder;
	}
	
	/**
	 * @return whether the external module is enabled
	 */
	public Boolean getEnabled() {
		return enabled;
	}
	
	/**
	 * @param enabled enabled property to be set
	 */
	public void setEnabled( Boolean enabled ) {
		this.enabled = enabled;
	}
	
}
