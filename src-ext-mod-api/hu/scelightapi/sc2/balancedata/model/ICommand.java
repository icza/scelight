/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapi.sc2.balancedata.model;

/**
 * Describes an ability command.
 * 
 * @author Andras Belicza
 * 
 * @see IEntity
 */
public interface ICommand extends IEntity {
	
	/**
	 * Returns the ability id this command belongs to.
	 * 
	 * @return the ability id this command belongs to
	 */
	String getAbilId();
	
	/**
	 * Tells if this command is <i>essential</i>.
	 * 
	 * @return true if this command is <i>essential</i>; false otherwise
	 */
	boolean isEssential();
	
}
