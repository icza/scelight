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

import javax.swing.ImageIcon;

/**
 * Person bean interface.
 * 
 * @author Andras Belicza
 * 
 * @see IBean
 */
public interface IPersonBean extends IBean {
	
	/**
	 * Returns the name of the person.
	 * 
	 * @return the name of the person
	 */
	IPersonNameBean getPersonName();
	
	/**
	 * Returns the contact info.
	 * 
	 * @return the contact info
	 */
	IContactBean getContact();
	
	/**
	 * Returns the home page of the person.
	 * 
	 * @return the home page
	 */
	String getHomePage();
	
	/**
	 * Returns the description / comment of the person.
	 * 
	 * @return the description / comment of the person
	 */
	String getDescription();
	
	/**
	 * Returns the person photo image data in one of the formats of JPG, PNG or GIF.
	 * 
	 * @return the person photo image data
	 */
	byte[] getPhotoImageData();
	
	/**
	 * Returns the lazily initialized photo image of the person.
	 * <p>
	 * The image is created from the image data returned by {@link #getPhotoImageData()}.
	 * </p>
	 * 
	 * @return the lazily initialized photo image of the person
	 */
	ImageIcon getPhoto();
	
}
