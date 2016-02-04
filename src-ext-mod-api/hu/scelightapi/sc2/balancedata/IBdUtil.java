/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapi.sc2.balancedata;

import hu.scelightapi.IServices;

/**
 * SC2 Balance data utilities.
 * 
 * @author Andras Belicza
 * 		
 * @see IServices#getBdUtil()
 * @see IBalanceData
 */
public interface IBdUtil {
	
	/** Id of the Drone unit. */
	String UNIT_DRONE						  = "Drone";
											  
	/** Id of the Probe unit. */
	String UNIT_PROBE						  = "Probe";
											  
	/** Id of the SCV unit. */
	String UNIT_SCV							  = "SCV";
											  
											  
											  
	/** Id of the Broodling unit. */
	String UNIT_BROODLING					  = "Broodling";
											  
	/** Id of the Broodling Escort unit. */
	String UNIT_BROODLING_ESCORT			  = "BroodlingEscort";
											  
	/** Id of the Larva unit. */
	String UNIT_LARVA						  = "Larva";
											  
	/** Id of the Locusts unit. */
	String UNIT_LOCUST						  = "LocustMP";
											  
	/** Id of the Reaper (Placeholder) unit. */
	String UNIT_REAPER_PLACEHOLDER			  = "ReaperPlaceholder";
											  
											  
											  
	/** Id of the Nexus unit (building). */
	String UNIT_NEXUS						  = "Nexus";
											  
	/** Id of the Hatchery unit (building). */
	String UNIT_HATCHERY					  = "Hatchery";
											  
	/** Id of the Command Center unit (building). */
	String UNIT_COMMAND_CENTER				  = "CommandCenter";
											  
											  
											  
	/** Id of the Mineral Field unit. */
	String UNIT_MINERAL_FIELD				  = "MineralField";
											  
	/** Id of the Mineral Field (750 minerals only) unit. */
	String UNIT_MINERAL_FIELD_750			  = "MineralField750";
											  
	/** Id of the Lab Mineral Field unit. */
	String UNIT_LAB_MINERAL_FIELD			  = "LabMineralField";
											  
	/** Id of the Lab Mineral Field (750 minerals only) unit. */
	String UNIT_LAB_MINERAL_FIELD_750		  = "LabMineralField750";
											  
	/** Id of the Rich Mineral Field unit. */
	String UNIT_RICH_MINERAL_FIELD			  = "RichMineralField";
											  
	/** Id of the Rich Mineral Field (750 minerals only) unit. */
	String UNIT_RICH_MINERAL_FIELD_750		  = "RichMineralField750";
											  
											  
											  
	/** Id of the Vespene Geyser unit. */
	String UNIT_VESPENE_GEYSER				  = "VespeneGeyser";
											  
	/** Id of the Protoss Vespene Geyser unit. */
	String UNIT_PROTOSS_VESPENE_GEYSER		  = "ProtossVespeneGeyser";
											  
	/** Id of the Rich Vespene Geyser unit. */
	String UNIT_RICH_VESPENE_GEYSER			  = "RichVespeneGeyser";
											  
	/** Id of the Space Platform Vespene Geyser unit. */
	String UNIT_SPACE_PLATFORM_VESPENE_GEYSER = "SpacePlatformGeyser";
											  
											  
											  
	/** Id of the Xel'Naga Tower unit. */
	String UNIT_XEL_NAGA_TOWER				  = "XelNagaTower";
											  
											  
											  
	/**
	 * Tells if the specified unit id is an id of a worker unit.
	 * 
	 * @param id unit id to be tested
	 * @return true if the specified unit id is an id of a worker unit; false otherwise
	 */
	boolean isWorker( String id );
	
	/**
	 * Tells if the specified unit (building) id is an id of a main building unit.
	 * 
	 * @param id unit (building) id to be tested
	 * @return true if the specified unit id is an id of a main building unit; false otherwise
	 */
	boolean isMainBuilding( String id );
	
	/**
	 * Tells if the specified unit id is an id of a destructible unit.
	 * 
	 * @param id unit id to be tested
	 * @return true if the specified unit id is an id of a destructible unit; false otherwise
	 */
	boolean isDestructible( String id );
	
}
