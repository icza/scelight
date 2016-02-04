/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.sc2.map.cache;

import hu.sllauncher.bean.Bean;
import hu.sllauncher.bean.VersionBean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * Map image cache config bean.
 * 
 * @author Andras Belicza
 */
@XmlAccessorType( XmlAccessType.FIELD )
class ConfigBean extends Bean {
	
	/** Current bean version. */
	public static final int BEAN_VER = 1;
	
	/** Cache version. */
	private VersionBean     version;
	
	/**
	 * Creates a new {@link ConfigBean}.
	 */
	public ConfigBean() {
		super( BEAN_VER );
	}
	
	/**
	 * Returns the module version.
	 * 
	 * @return the module version
	 */
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
	
}
