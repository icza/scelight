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
 * Describes a StarCraft II unit ability.
 * 
 * @author Andras Belicza
 * 
 * @see ICommand
 */
public interface IAbility {
	
	// ZERG ABILITY ID CONSTANTS
	
	/** Id of the Spawn Larva ability. */
	String ID_SPAWN_LARVA             = "SpawnLarva";
	
	
	// PROTOSS ABILITY ID CONSTANTS
	
	/** Id of the Chrono Boost ability. */
	String ID_CHRONO_BOOST            = "TimeWarp";
	
	
	// TERRAN ABILITY ID CONSTANTS
	
	/** Id of the Scanner Sweep ability. */
	String ID_SCANNER_SWEEP           = "ScannerSweep";
	
	/** Id of the Calldown MULE ability. */
	String ID_CALLDOWN_MULE           = "CalldownMULE";
	
	/** Id of the Calldown Extra Supplies ability. */
	String ID_CALLDOWN_EXTRA_SUPPLIES = "SupplyDrop";
	
	
	
	/**
	 * Returns the ability id.
	 * 
	 * @return the ability id
	 */
	String getId();
	
	/**
	 * Returns the {@link ICommand} specified by its index.
	 * 
	 * @param <T> the expected command type
	 * @param idx index of the command to return
	 * @return the {@link ICommand} specified by its index
	 */
	< T extends ICommand > T getCommand( Integer idx );
	
}
