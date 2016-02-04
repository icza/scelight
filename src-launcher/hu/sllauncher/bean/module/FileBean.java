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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * File bean.
 * 
 * <p>
 * Describes a file.
 * </p>
 * 
 * @author Andras Belicza
 */
@XmlAccessorType( XmlAccessType.FIELD )
public class FileBean extends Bean {
	
	/** Current bean version. */
	public static final int BEAN_VER = 1;
	
	/** App-path relative file path (where to place it). */
	@XmlAttribute
	private String          path;
	
	/** Content SHA-256 checksum. */
	@XmlAttribute
	private String          sha256;
	
	/**
	 * Creates a new {@link FileBean}.
	 */
	public FileBean() {
		super( BEAN_VER );
	}
	
	/**
	 * Returns the app-path relative file path.
	 * 
	 * @return the app-path relative file path
	 */
	public String getPath() {
		return path;
	}
	
	/**
	 * Sets the app-path relaitve file path.
	 * 
	 * @param path the app-path relative file path to be set
	 */
	public void setPath( String path ) {
		this.path = path;
	}
	
	/**
	 * Returns the content SHA-256 checksum.
	 * 
	 * @return the content SHA-256 checksum
	 */
	public String getSha256() {
		return sha256;
	}
	
	/**
	 * Sets the content SHA-256 checksum.
	 * 
	 * @param sha256 content SHA-256 checksum to be set
	 */
	public void setSha256( String sha256 ) {
		this.sha256 = sha256;
	}
	
}
