/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllauncher.bean.person;

import hu.scelightapibase.bean.person.IPersonBean;
import hu.sllauncher.bean.Bean;

import javax.swing.ImageIcon;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Person bean.
 * 
 * @author Andras Belicza
 */
@XmlAccessorType( XmlAccessType.FIELD )
public class PersonBean extends Bean implements IPersonBean {
	
	/** Current bean version. */
	public static final int BEAN_VER = 1;
	
	/** Name of the person. */
	private PersonNameBean  personName;
	
	/** Contact info. */
	private ContactBean     contact;
	
	/** Home page of the person. */
	private String          homePage;
	
	/** Description / comment of the person. */
	private String          description;
	
	/** Person photo image data in one of the formats of JPG, PNG or GIF. */
	private byte[]          photoImageData;
	
	/** Lazily initialized photo image of the person. */
	@XmlTransient
	private ImageIcon       photo;
	
	/**
	 * Creates a new {@link PersonBean}.
	 */
	public PersonBean() {
		super( BEAN_VER );
	}
	
	@Override
	public PersonNameBean getPersonName() {
		return personName;
	}
	
	/**
	 * Sets the name of the person.
	 * 
	 * @param personName name of the person to be set
	 */
	public void setPersonName( PersonNameBean personName ) {
		this.personName = personName;
	}
	
	@Override
	public ContactBean getContact() {
		return contact;
	}
	
	/**
	 * Sets the contact info.
	 * 
	 * @param contact contact info to set
	 */
	public void setContact( ContactBean contact ) {
		this.contact = contact;
	}
	
	@Override
	public String getHomePage() {
		return homePage;
	}
	
	/**
	 * Sets the home page of the person.
	 * 
	 * @param homePage home page to be set
	 */
	public void setHomePage( String homePage ) {
		this.homePage = homePage;
	}
	
	@Override
	public String getDescription() {
		return description;
	}
	
	/**
	 * Sets the description / comment of the person.
	 * 
	 * @param description description / comment of the person to be set
	 */
	public void setDescription( String description ) {
		this.description = description;
	}
	
	@Override
	public byte[] getPhotoImageData() {
		return photoImageData;
	}
	
	/**
	 * Sets the person photo image data in one of the formats of JPG, PNG or GIF.
	 * 
	 * @param photoImageData photo image data to be set
	 */
	public void setPhotoImageData( byte[] photoImageData ) {
		this.photoImageData = photoImageData;
	}
	
	@Override
	public ImageIcon getPhoto() {
		if ( photo == null )
			photo = photoImageData == null ? new ImageIcon() : new ImageIcon( photoImageData );
		
		return photo;
	}
	
}
