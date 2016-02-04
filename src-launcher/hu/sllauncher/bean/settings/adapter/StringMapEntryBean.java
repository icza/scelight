/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllauncher.bean.settings.adapter;

import hu.sllauncher.bean.Bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * A map entry bean having {@link String} key and value type, used by {@link SettingsMapAdapter}.
 * 
 * @author Andras Belicza
 */
@XmlAccessorType( XmlAccessType.FIELD )
public class StringMapEntryBean extends Bean {
	
	/** Current bean version. */
	public static final int BEAN_VER = 1;
	
	/** Key of the entry. */
	@XmlAttribute( name = "id" )
	private String          key;
	
	/** Value of the entry. */
	@XmlAttribute
	private String          value;
	
	/**
	 * Creates a new {@link StringMapEntryBean}.
	 */
	public StringMapEntryBean() {
		this( null, null );
	}
	
	/**
	 * Creates a new {@link StringMapEntryBean}.
	 * 
	 * @param key key of the entry
	 * @param value value of the entry
	 */
	public StringMapEntryBean( final String key, final String value ) {
		super( BEAN_VER );
		
		this.key = key;
		this.value = value;
	}
	
	/**
	 * Returns the key of the entry.
	 * 
	 * @return the key of the entry
	 */
	public String getKey() {
		return key;
	}
	
	/**
	 * Sets the key of the entry.
	 * 
	 * @param key key of the entry to be set
	 */
	public void setKey( String key ) {
		this.key = key;
	}
	
	/**
	 * Returns the value of the entry.
	 * 
	 * @return the value of the entry
	 */
	public String getValue() {
		return value;
	}
	
	/**
	 * Returns the value of the entry.
	 * 
	 * @param value value of the entry to be set
	 */
	public void setValue( String value ) {
		this.value = value;
	}
	
}
