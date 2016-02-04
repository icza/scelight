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

/**
 * Bean which has an id property and overrides {@link Object#hashCode()} and {@link Object#equals(Object)} to operate based on this single field. The id
 * property is instantiated automatically, and its value is serialized and deserialized along with the bean.
 * 
 * @author Andras Belicza
 * 
 * @see IBean
 */
public interface IIdedBean extends IBean {
	
	// No additional methods published.
	
}
