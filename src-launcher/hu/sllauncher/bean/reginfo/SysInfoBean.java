/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllauncher.bean.reginfo;

import hu.sllauncher.bean.Bean;

import java.util.Date;
import java.util.Objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * Registered system info bean.
 * 
 * @author Andras Belicza
 */
@XmlAccessorType( XmlAccessType.FIELD )
public class SysInfoBean extends Bean {
	
	/** Current bean version. */
	public static final int BEAN_VER = 1;
	
	/** OS name. */
	private String          osName;
	
	/** OS version. */
	private String          osVersion;
	
	/** Available processors. */
	private Integer         availProcs;
	
	/** User name. */
	private String          userName;
	
	/** User country. */
	private String          userCountry;
	
	/** User time zone. */
	private String          userTimeZone;
	
	/** Main root size. */
	private Long            mainRootSize;
	
	/** Date when system info was queried/assembled. */
	private Date            date;
	
	/**
	 * Creates a new {@link SysInfoBean}.
	 */
	public SysInfoBean() {
		super( BEAN_VER );
	}
	
	/**
	 * Tests if this system info matches the specified other one.
	 * 
	 * @param s other system info to match with
	 * @return true if this system info matches the specified other one, false otherwise
	 */
	public boolean matches( final SysInfoBean s ) {
		Objects.requireNonNull( s );
		
		if ( !Objects.equals( osName, s.osName ) )
			return false;
		if ( !Objects.equals( osVersion, s.osVersion ) )
			return false;
		if ( !Objects.equals( availProcs, s.availProcs ) )
			return false;
		if ( !Objects.equals( userName, s.userName ) )
			return false;
		if ( !Objects.equals( userCountry, s.userCountry ) )
			return false;
		if ( !Objects.equals( userTimeZone, s.userTimeZone ) )
			return false;
		if ( !Objects.equals( mainRootSize, s.mainRootSize ) )
			return false;
		
		return true;
	}
	
	/**
	 * Returns the OS name.
	 * 
	 * @return the OS name
	 */
	public String getOsName() {
		return osName;
	}
	
	/**
	 * Sets the OS name.
	 * 
	 * @param osName OS name to be set
	 */
	public void setOsName( String osName ) {
		this.osName = osName;
	}
	
	/**
	 * Returns the OS version.
	 * 
	 * @return the OS version
	 */
	public String getOsVersion() {
		return osVersion;
	}
	
	/**
	 * Sets the OS version.
	 * 
	 * @param osVersion OS version to be set
	 */
	public void setOsVersion( String osVersion ) {
		this.osVersion = osVersion;
	}
	
	/**
	 * Returns the available processors.
	 * 
	 * @return the available processors
	 */
	public Integer getAvailProcs() {
		return availProcs;
	}
	
	/**
	 * Sets the available processors.
	 * 
	 * @param availProcs available processors to be set
	 */
	public void setAvailProcs( Integer availProcs ) {
		this.availProcs = availProcs;
	}
	
	/**
	 * Returns the user name.
	 * 
	 * @return the user name
	 */
	public String getUserName() {
		return userName;
	}
	
	/**
	 * Sets the user name.
	 * 
	 * @param userName user name to be set
	 */
	public void setUserName( String userName ) {
		this.userName = userName;
	}
	
	/**
	 * Returns the user country.
	 * 
	 * @return the user country
	 */
	public String getUserCountry() {
		return userCountry;
	}
	
	/**
	 * Sets the user country.
	 * 
	 * @param userCountry user country to be set
	 */
	public void setUserCountry( String userCountry ) {
		this.userCountry = userCountry;
	}
	
	/**
	 * Returns the user time zone.
	 * 
	 * @return the user time zone
	 */
	public String getUserTimeZone() {
		return userTimeZone;
	}
	
	/**
	 * Sets the user time zone.
	 * 
	 * @param userTimeZone user time zone to be set
	 */
	public void setUserTimeZone( String userTimeZone ) {
		this.userTimeZone = userTimeZone;
	}
	
	/**
	 * Returns the main root size.
	 * 
	 * @return the main root size
	 */
	public Long getMainRootSize() {
		return mainRootSize;
	}
	
	/**
	 * Sets the main root size.
	 * 
	 * @param mainRootSize main root size to be set
	 */
	public void setMainRootSize( Long mainRootSize ) {
		this.mainRootSize = mainRootSize;
	}
	
	/**
	 * Returns the date when system info was queried/assembled.
	 * 
	 * @return the date when the system info was queried/assembled
	 */
	public Date getDate() {
		return date;
	}
	
	/**
	 * Sets the date when system info was queried/assembled.
	 * 
	 * @param date date to be set, date of when the system info was queried/assembled
	 */
	public void setDate( Date date ) {
		this.date = date;
	}
	
}
