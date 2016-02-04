/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.sc2.balancedata;

import hu.scelightapi.sc2.balancedata.IBdUtil;

/**
 * SC2 Balance data utilities.
 * 
 * @author Andras Belicza
 */
public class BdUtil implements IBdUtil {
	
	/** Singleton instance. */
	public static final BdUtil INSTANCE = new BdUtil();
	
	/**
	 * Creates a new {@link BdUtil}.
	 * <p>
	 * Private to enforce singleton nature.
	 * </p>
	 */
	private BdUtil() {
	}
	
	
	/**
	 * Tells if the specified unit id is an id of a worker unit.
	 * 
	 * @param id unit id to be tested
	 * @return true if the specified unit id is an id of a worker unit; false otherwise
	 */
	public static boolean isWorkerImpl( final String id ) {
		return UNIT_DRONE.equals( id ) || UNIT_PROBE.equals( id ) || UNIT_SCV.equals( id );
	}
	
	/**
	 * Tells if the specified unit (building) id is an id of a main building unit.
	 * 
	 * @param id unit (building) id to be tested
	 * @return true if the specified unit id is an id of a main building unit; false otherwise
	 */
	public static boolean isMainBuildingImpl( final String id ) {
		return UNIT_NEXUS.equals( id ) || UNIT_HATCHERY.equals( id ) || UNIT_COMMAND_CENTER.equals( id );
	}
	
	/**
	 * Tells if the specified unit id is an id of a destructible unit.
	 * 
	 * @param id unit id to be tested
	 * @return true if the specified unit id is an id of a destructible unit; false otherwise
	 */
	public static boolean isDestructibleImpl( final String id ) {
		return id.startsWith( "Destructible" );
	}
	
	
	@Override
	public boolean isWorker( final String id ) {
		return isWorkerImpl( id );
	}
	
	@Override
	public boolean isMainBuilding( final String id ) {
		return isMainBuildingImpl( id );
	}
	
	@Override
	public boolean isDestructible( final String id ) {
		return isDestructibleImpl( id );
	}
	
}
