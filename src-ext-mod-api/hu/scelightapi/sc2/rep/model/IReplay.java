/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapi.sc2.rep.model;

import hu.scelightapi.sc2.balancedata.IBalanceData;
import hu.scelightapi.sc2.rep.factory.IRepParserEngine;
import hu.scelightapi.sc2.rep.model.attributesevents.IAttributesEvents;
import hu.scelightapi.sc2.rep.model.details.IDetails;
import hu.scelightapi.sc2.rep.model.gameevents.IGameEvents;
import hu.scelightapi.sc2.rep.model.initdata.IInitData;
import hu.scelightapi.sc2.rep.model.messageevents.IMessageEvents;
import hu.scelightapi.sc2.rep.model.trackerevents.ITrackerEvents;
import hu.scelightapi.sc2.rep.repproc.IRepProcessor;

/**
 * Interface modeling a StarCraft II replay file.
 * 
 * <p>
 * Recommended to process replays via {@link IRepProcessor}s which contains many utilities and gives a higher level view of the data stored in replays.
 * </p>
 * 
 * @author Andras Belicza
 * 
 * @see IRepParserEngine
 * @see IRepProcessor
 * @see IBalanceData
 */
public interface IReplay {
	
	/**
	 * Returns the replay header.
	 * 
	 * @return the replay header
	 */
	IHeader getHeader();
	
	/**
	 * Returns the replay details.
	 * 
	 * @return the replay details
	 */
	IDetails getDetails();
	
	/**
	 * Returns the init data.
	 * 
	 * @return the init data
	 */
	IInitData getInitData();
	
	/**
	 * Returns the attributes events.
	 * 
	 * @return the attributes events
	 */
	IAttributesEvents getAttributesEvents();
	
	/**
	 * Returns the message events of the replay.
	 * 
	 * @return the message events of the replay
	 */
	IMessageEvents getMessageEvents();
	
	/**
	 * Returns game events of the replay.
	 * 
	 * @return game events of the replay
	 */
	IGameEvents getGameEvents();
	
	/**
	 * Returns the tracker events of the replay.
	 * 
	 * @return the tracker events of the replay
	 */
	ITrackerEvents getTrackerEvents();
	
	/**
	 * Returns the balance data that applies to and to be used for this replay.
	 * 
	 * @return the balance data that applies to and to be used for this replay
	 */
	IBalanceData getBalanceData();
	
}
