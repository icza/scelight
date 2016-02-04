/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapi.sc2.rep.model.gameevents.cmd;

import hu.scelightapi.sc2.balancedata.model.ICommand;
import hu.scelightapi.sc2.rep.model.IEvent;
import hu.scelightapibase.util.IPair;

/**
 * Cmd game event.
 * 
 * @author Andras Belicza
 */
public interface ICmdEvent extends IEvent {
	
	/** Cmd flags field name. */
	String   F_CMD_FLAGS      = "cmdFlags";
	
	/** Ability link (index) field path. */
	String[] P_ABIL_LINK      = { "abil", "abilLink" };
	
	/** Ability cmd index field path. */
	String[] P_ABIL_CMD_INDEX = { "abil", "abilCmdIndex" };
	
	/** Data field name. */
	String   F_DATA           = "data";
	
	
	
	/**
	 * Returns the command associated with the event, if any.
	 * 
	 * @return the command associated with the event, if any
	 * 
	 * @see ICommand
	 */
	ICommand getCommand();
	
	/**
	 * Returns the cmd flags.
	 * 
	 * @return the cmd flags
	 * 
	 * @see ICmdFlag
	 */
	Integer getCmdFlags();
	
	/**
	 * Returns the abil link (index).
	 * 
	 * @return the abil link (index)
	 */
	Integer getAbilLink();
	
	/**
	 * Returns the abil cmd index.
	 * 
	 * @return the abil cmd index
	 */
	Integer getAbilCmdIndex();
	
	/**
	 * Returns the data.
	 * 
	 * @return the data
	 */
	IPair< String, Object > getData();
	
	/**
	 * Returns the target unit of the command.
	 * 
	 * @return the target unit of the command
	 */
	ITargetUnit getTargetUnit();
	
	/**
	 * Returns the target point of the command.
	 * 
	 * @return the target point of the command
	 */
	ITargetPoint getTargetPoint();
	
}
