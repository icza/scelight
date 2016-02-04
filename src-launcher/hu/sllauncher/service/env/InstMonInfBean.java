/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllauncher.service.env;

import hu.sllauncher.bean.Bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * Instance Monitor info bean.
 * 
 * @author Andras Belicza
 */
@XmlAccessorType( XmlAccessType.FIELD )
public class InstMonInfBean extends Bean {
	
	/** Current bean version. */
	public static final int BEAN_VER = 1;
	
	/** Communication protocol version. */
	private Integer         comProtVer;
	
	/** Communication port. */
	private Integer         port;
	
	/**
	 * Creates a new {@link InstMonInfBean}.
	 */
	public InstMonInfBean() {
		super( BEAN_VER );
	}
	
	/**
	 * Returns the communication protocol version.
	 * 
	 * @return the communication protocol version
	 */
	public Integer getComProtVer() {
		return comProtVer;
	}
	
	/**
	 * Sets the communication protocol version.
	 * 
	 * @param comProtVer communication protocol version to be set
	 */
	public void setComProtVer( Integer comProtVer ) {
		this.comProtVer = comProtVer;
	}
	
	/**
	 * Returns the communication port.
	 * 
	 * @return the communication port
	 */
	public Integer getPort() {
		return port;
	}
	
	/**
	 * Sets the communication port.
	 * 
	 * @param port communication port to be set
	 */
	public void setPort( Integer port ) {
		this.port = port;
	}
	
}
