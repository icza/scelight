/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sltool.release;

import hu.sllauncher.bean.person.ContactBean;
import hu.sllauncher.bean.person.PersonBean;
import hu.sllauncher.bean.person.PersonNameBean;
import hu.sllauncher.r.LR;

import java.io.File;

import javax.xml.bind.JAXB;

/**
 * Application which creates and writes the <code>author.xml</code> file.
 * 
 * @author Andras Belicza
 */
public class CreateAuthorXml {
	
	/**
	 * @param args used to take arguments from the running environment - not used here
	 * @throws Exception if any error occurs
	 */
	public static void main( final String[] args ) throws Exception {
		final PersonBean author = new PersonBean();
		
		final PersonNameBean personName = new PersonNameBean();
		personName.setFirst( "András" );
		personName.setLast( "Belicza" );
		author.setPersonName( personName );
		
		final ContactBean contact = new ContactBean();
		contact.setEmail( "iczaaa@gmail.com" );
		contact.setLocation( "Budapest, Hungary" );
		author.setContact( contact );
		author.setDescription( "Author of BWHF, Sc2gears and Scelight" );
		
		JAXB.marshal( author, new File( LR.get( "bean/author.xml" ).toURI() ) );
	}
	
}
