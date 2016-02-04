/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapibase.bean;

import java.util.Date;

/**
 * Build info bean.
 * 
 * @author Andras Belicza
 * 
 * @see IBean
 */
public interface IBuildInfoBean extends IBean {
	
	/**
	 * Returns the build number.
	 * 
	 * @return the build number
	 */
	Integer getBuildNumber();
	
	/**
	 * Returns the build date and time.
	 * 
	 * @return the build date and time
	 */
	Date getDate();
	
}
