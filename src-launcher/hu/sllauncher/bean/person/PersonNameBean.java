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

import hu.scelightapibase.bean.person.IPersonNameBean;
import hu.sllauncher.bean.Bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * Person name bean.
 * 
 * @author Andras Belicza
 */
@XmlAccessorType( XmlAccessType.FIELD )
public class PersonNameBean extends Bean implements IPersonNameBean {
	
	/** Current bean version. */
	public static final int BEAN_VER = 1;
	
	/** First name. */
	private String          first;
	
	/** Middle name. */
	private String          middle;
	
	/** Last name. */
	private String          last;
	
	/** Nickname. */
	private String          nick;
	
	/**
	 * Creates a new {@link PersonNameBean}.
	 */
	public PersonNameBean() {
		super( BEAN_VER );
	}
	
	/**
	 * Creates a new {@link PersonNameBean}.
	 * 
	 * @param first first name
	 * @param middle middle name
	 * @param last last name
	 * @param nick nickname
	 */
	public PersonNameBean( final String first, final String middle, final String last, final String nick ) {
		super( BEAN_VER );
		this.first = first;
		this.middle = middle;
		this.last = last;
		this.nick = nick;
	}
	
	@Override
	public String toString() {
		final StringBuilder b = new StringBuilder();
		
		final String[] parts = { first, middle, nick, last };
		for ( int i = 0; i < parts.length; i++ )
			if ( parts[ i ] != null ) {
				if ( b.length() > 0 )
					b.append( ' ' );
				
				if ( i == 2 ) // Put nickname in quotes
					b.append( '"' );
				
				b.append( parts[ i ] );
				
				if ( i == 2 ) // Put nickname in quotes
					b.append( '"' );
			}
		
		return b.toString();
	}
	
	@Override
	public String getFirst() {
		return first;
	}
	
	/**
	 * Sets the first name.
	 * 
	 * @param first first name to be set
	 */
	public void setFirst( String first ) {
		this.first = first;
	}
	
	@Override
	public String getMiddle() {
		return middle;
	}
	
	/**
	 * Sets the middle name.
	 * 
	 * @param middle middle name to be set
	 */
	public void setMiddle( String middle ) {
		this.middle = middle;
	}
	
	@Override
	public String getLast() {
		return last;
	}
	
	/**
	 * Sets the last name.
	 * 
	 * @param last last name to be set
	 */
	public void setLast( String last ) {
		this.last = last;
	}
	
	@Override
	public String getNick() {
		return nick;
	}
	
	/**
	 * Sets the nickname.
	 * 
	 * @param nick nickname to be set
	 */
	public void setNick( String nick ) {
		this.nick = nick;
	}
	
}
