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

import hu.scelightapi.sc2.balancedata.model.IAbility;
import hu.scelightapi.sc2.balancedata.model.ICommand;

import java.util.HashMap;
import java.util.Map;

/**
 * Describes a StarCraft II unit ability.
 * 
 * @author Andras Belicza
 */
public class Ability implements IAbility {
	
	/** Ability id. */
	public final String                  id;
	
	/** Ability commands, mapped from command index to {@link Command}. */
	public final Map< Integer, Command > idxCmdMap = new HashMap<>( 6 );
	
	
	/**
	 * Creates a new {@link Ability}.
	 * 
	 * @param id id of the ability
	 */
	public Ability( final String id ) {
		this.id = id;
	}
	
	@Override
	public String getId() {
		return id;
	}
	
	@Override
	@SuppressWarnings( "unchecked" )
	public < T extends ICommand > T getCommand( final Integer idx ) {
		return (T) idxCmdMap.get( idx );
	}
	
}
