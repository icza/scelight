/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllauncher.bean;

import hu.scelightapibase.bean.IBuildInfoBean;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * Build info bean.
 * 
 * @author Andras Belicza
 */
@XmlAccessorType( XmlAccessType.FIELD )
public class BuildInfoBean extends Bean implements IBuildInfoBean {
	
	/** Current bean version. */
	public static final int BEAN_VER = 1;
	
	/** Build number. */
	private Integer         buildNumber;
	
	/** Build date and time. */
	private Date            date;
	
	/**
	 * Creates a new {@link BuildInfoBean}.
	 */
	public BuildInfoBean() {
		super( BEAN_VER );
	}
	
	@Override
	public Integer getBuildNumber() {
		return buildNumber;
	}
	
	/**
	 * Sets the build number.
	 * 
	 * @param buildNumber build number to be set
	 */
	public void setBuild( Integer buildNumber ) {
		this.buildNumber = buildNumber;
	}
	
	@Override
	public Date getDate() {
		return date;
	}
	
	/**
	 * Sets the build date and time.
	 * 
	 * @param date build date and time to be set
	 */
	public void setDate( Date date ) {
		this.date = date;
	}
	
}
