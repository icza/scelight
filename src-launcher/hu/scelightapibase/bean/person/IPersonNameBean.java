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
import hu.scelightapibase.service.lang.ILanguage;

/**
 * Person name bean interface.
 * 
 * @author Andras Belicza
 * 
 * @see ILanguage#formatPersonName(IPersonNameBean)
 * @see IBean
 */
public interface IPersonNameBean extends IBean {
	
	/**
	 * Returns the first name.
	 * 
	 * @return the first name
	 */
	String getFirst();
	
	/**
	 * Returns the middle name.
	 * 
	 * @return the middle name
	 */
	String getMiddle();
	
	/**
	 * Returns the last name.
	 * 
	 * @return the last name
	 */
	String getLast();
	
	/**
	 * Returns the nickname.
	 * 
	 * @return the nickname
	 */
	String getNick();
	
	/**
	 * Returns the formatted person name in <strong>English</strong> locale.
	 * <p>
	 * The returned name format: <code>first middle "nickname" last</code>
	 * </p>
	 * <p>
	 * This is only for debugging purposes. A proper name formatter is to be used for the proper locale ({@link ILanguage#formatPersonName(IPersonNameBean)}).
	 * </p>
	 * 
	 * @see ILanguage#formatPersonName(IPersonNameBean)
	 */
	@Override
	String toString();
	
}
