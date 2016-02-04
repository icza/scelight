/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.sc2.balancedata.model;

import hu.scelightapi.sc2.balancedata.model.ITrainAbility;

/**
 * Describes a StarCraft II unit train ability.
 * 
 * <p>
 * This is a "virtual" ability. The ability id will be the id of the unit to whom the command is given.
 * </p>
 * 
 * @author Andras Belicza
 */
public class TrainAbility extends Ability implements ITrainAbility {
	
	/**
	 * Creates a new {@link TrainAbility}.
	 * 
	 * @param id id of the train ability, it is the id of the unit to whom the command is given
	 */
	public TrainAbility( final String id ) {
		super( id );
	}
	
}
