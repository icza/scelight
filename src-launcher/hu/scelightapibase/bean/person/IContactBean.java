/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapibase.bean.person;

import hu.scelightapibase.bean.IBean;

/**
 * Contact bean interface.
 * 
 * @author Andras Belicza
 * 
 * @see IBean
 */
public interface IContactBean extends IBean {
	
	/**
	 * Returns the geographical location.
	 * 
	 * @return the geographical location
	 */
	String getLocation();
	
	/**
	 * Returns the email address.
	 * 
	 * @return the email address
	 */
	String getEmail();
	
	/**
	 * Returns the Facebook address.
	 * 
	 * @return the Facebook address
	 */
	String getFacebook();
	
	/**
	 * Returns the Google+ address.
	 * 
	 * @return the Google+ address
	 */
	String getGooglePlus();
	
	/**
	 * Returns the Twitter address.
	 * 
	 * @return the Twitter address
	 */
	String getTwitter();
	
	/**
	 * Returns the LinkedIn address.
	 * 
	 * @return the LinkedIn address
	 */
	String getLinkedIn();
	
	/**
	 * Returns the YouTube address.
	 * 
	 * @return the YouTube address
	 */
	String getYoutube();
	
	/**
	 * Returns other address.
	 * 
	 * @return other address
	 */
	String getOther();
	
}
