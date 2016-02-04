/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapi.sc2.rep.model.attributesevents;

/**
 * Describes an attribute in the attribute events.
 * 
 * @author Andras Belicza
 */
public interface IAttribute {
	
	/**
	 * Returns the attribute namespace.
	 * 
	 * @return attribute namespace
	 */
	Integer getNamespace();
	
	/**
	 * Returns the attribute id.
	 * 
	 * @return the attribute id
	 */
	Integer getId();
	
	/**
	 * Returns the attribute scope.
	 * 
	 * @return the attribute scope
	 */
	Integer getScope();
	
	/**
	 * Returns the attribute value.
	 * 
	 * @return the attribute value
	 */
	String getValue();
	
	/**
	 * Returns the attribute value.
	 * 
	 * @return the attribute value
	 */
	@Override
	String toString();
	
}
