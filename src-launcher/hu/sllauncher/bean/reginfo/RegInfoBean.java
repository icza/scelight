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

import java.util.Date;

import hu.sllauncher.bean.Bean;
import hu.sllauncher.bean.person.PersonBean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * Registration info bean.
 * 
 * @author Andras Belicza
 */
@XmlAccessorType( XmlAccessType.FIELD )
public class RegInfoBean extends Bean {
	
	/** Current bean version. */
	public static final int BEAN_VER = 1;
	
	/** Google account email address. */
	private String          googleAccount;
	
	/** Ticket that was used to create and download the registration file. */
	private String          ticket;
	
	/** Registered person. */
	private PersonBean      person;
	
	/** Registered system info. */
	private SysInfoBean     sysInfo;
	
	/** Registration date and time. */
	private Date            regDate;
	
	/** Registration file encryption date. */
	private Date            encryptionDate;
	
	/**
	 * Creates a new {@link RegInfoBean}.
	 */
	public RegInfoBean() {
		super( BEAN_VER );
	}
	
	/**
	 * Returns the Google account email address.
	 * 
	 * @return the Google account email address
	 */
	public String getGoogleAccount() {
		return googleAccount;
	}
	
	/**
	 * Sets the Google account email address.
	 * 
	 * @param googleAccount Google account email address to be set
	 */
	public void setGoogleAccount( String googleAccount ) {
		this.googleAccount = googleAccount;
	}
	
	/**
	 * Returns the ticket that was used to create and download the registration file.
	 * 
	 * @return the ticket that was used to create and download the registration file
	 */
	public String getTicket() {
		return ticket;
	}
	
	/**
	 * Sets the ticket that was used to create and download the registration file.
	 * 
	 * @param ticket ticket to be set
	 */
	public void setTicket( String ticket ) {
		this.ticket = ticket;
	}
	
	/**
	 * Returns the registered person.
	 * 
	 * @return the registered person
	 */
	public PersonBean getPerson() {
		return person;
	}
	
	/**
	 * Sets the registered person.
	 * 
	 * @param person registered person to be set
	 */
	public void setPerson( PersonBean person ) {
		this.person = person;
	}
	
	/**
	 * Returns the registered system info.
	 * 
	 * @return the registered system info
	 */
	public SysInfoBean getSysInfo() {
		return sysInfo;
	}
	
	/**
	 * Sets the registered system info.
	 * 
	 * @param sysInfo registered system info to be set
	 */
	public void setSysInfo( SysInfoBean sysInfo ) {
		this.sysInfo = sysInfo;
	}
	
	/**
	 * Returns the registration date and time.
	 * 
	 * @return the registration date and time
	 */
	public Date getRegDate() {
		return regDate;
	}
	
	/**
	 * Sets the registration date and time.
	 * 
	 * @param regDate registration date and time to be set
	 */
	public void setRegDate( Date regDate ) {
		this.regDate = regDate;
	}
	
	/**
	 * Returns the registration file encryption date.
	 * 
	 * @return the registration file encryption date
	 */
	public Date getEncryptionDate() {
		return encryptionDate;
	}
	
	/**
	 * Sets the registration file encryption date.
	 * 
	 * @param encryptionDate registration file encryption date to be set
	 */
	public void setEncryptionDate( Date encryptionDate ) {
		this.encryptionDate = encryptionDate;
	}
	
}
