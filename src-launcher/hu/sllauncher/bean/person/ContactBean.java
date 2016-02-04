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

import hu.scelightapibase.bean.person.IContactBean;
import hu.sllauncher.bean.Bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * Contact bean.
 * 
 * @author Andras Belicza
 */
@XmlAccessorType( XmlAccessType.FIELD )
public class ContactBean extends Bean implements IContactBean {
	
	/** Current bean version. */
	public static final int BEAN_VER = 1;
	
	/** Geographical location. */
	private String          location;
	
	/** Email address. */
	private String          email;
	
	/** Facebook address. */
	private String          facebook;
	
	/** Google+ address. */
	private String          googlePlus;
	
	/** Twitter address. */
	private String          twitter;
	
	/** LinkedIn address. */
	private String          linkedIn;
	
	/** YouTube address. */
	private String          youtube;
	
	/** Other address. */
	private String          other;
	
	/**
	 * Creates a new {@link ContactBean}.
	 */
	public ContactBean() {
		super( BEAN_VER );
	}
	
	@Override
	public String getLocation() {
		return location;
	}
	
	/**
	 * Sets the geographical location.
	 * 
	 * @param location geographical location to be set
	 */
	public void setLocation( String location ) {
		this.location = location;
	}
	
	@Override
	public String getEmail() {
		return email;
	}
	
	/**
	 * Sets the email address.
	 * 
	 * @param email email address to be set
	 */
	public void setEmail( String email ) {
		this.email = email;
	}
	
	@Override
	public String getFacebook() {
		return facebook;
	}
	
	/**
	 * Sets the Facebook address.
	 * 
	 * @param facebook Facebook address to be set
	 */
	public void setFacebook( String facebook ) {
		this.facebook = facebook;
	}
	
	@Override
	public String getGooglePlus() {
		return googlePlus;
	}
	
	/**
	 * Sets the Google+ address.
	 * 
	 * @param googlePlus Google+ address to be set
	 */
	public void setGooglePlus( String googlePlus ) {
		this.googlePlus = googlePlus;
	}
	
	@Override
	public String getTwitter() {
		return twitter;
	}
	
	/**
	 * Sets the Twitter address.
	 * 
	 * @param twitter Twitter address to be set
	 */
	public void setTwitter( String twitter ) {
		this.twitter = twitter;
	}
	
	@Override
	public String getLinkedIn() {
		return linkedIn;
	}
	
	/**
	 * Sets the LinkedIn address.
	 * 
	 * @param linkedIn LinkedIn address to be set
	 */
	public void setLinkedIn( String linkedIn ) {
		this.linkedIn = linkedIn;
	}
	
	@Override
	public String getYoutube() {
		return youtube;
	}
	
	/**
	 * Sets the YouTube address.
	 * 
	 * @param youtube YouTube address to be set
	 */
	public void setYoutube( String youtube ) {
		this.youtube = youtube;
	}
	
	@Override
	public String getOther() {
		return other;
	}
	
	/**
	 * Sets other address.
	 * 
	 * @param other other address to be set
	 */
	public void setOther( String other ) {
		this.other = other;
	}
	
}
