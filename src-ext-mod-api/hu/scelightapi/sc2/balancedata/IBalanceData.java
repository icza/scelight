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

import hu.scelightapi.sc2.balancedata.model.IAbility;
import hu.scelightapi.sc2.balancedata.model.IUnit;
import hu.scelightapi.sc2.balancedata.model.IUpgradeCommand;
import hu.scelightapi.util.IVersionView;

/**
 * SC2 Balance data interface.
 * 
 * @author Andras Belicza
 * 
 * @see IBdUtil
 */
public interface IBalanceData {
	
	/**
	 * Returns the minimum replay version this balance data is for.
	 * 
	 * @return the minimum replay version this balance data is for
	 */
	IVersionView getMinVersion();
	
	/**
	 * Returns the maximum replay version this balance data is for.
	 * 
	 * @return Returns the maximum replay version this balance data is for
	 */
	IVersionView getMaxVersion();
	
	/**
	 * Returns the {@link IUnit} specified by its id.
	 * 
	 * @param id id of the unit to return
	 * @return the {@link IUnit} specified by its id
	 */
	IUnit getUnit( String id );
	
	/**
	 * Returns the {@link IUnit} specified by its index.
	 * 
	 * @param idx index of the unit to return
	 * @return the {@link IUnit} specified by its index
	 */
	IUnit getUnit( Integer idx );
	
	/**
	 * Returns the {@link IAbility} specified by its index.
	 * 
	 * @param <T> the expected ability type
	 * @param idx index of the ability to return
	 * @return the {@link IAbility} specified by its index
	 */
	< T extends IAbility > T getAbility( Integer idx );
	
	/**
	 * Returns the {@link IUpgradeCommand} specified by its id.
	 * 
	 * @param id id of the upgrade command to return
	 * @return the {@link IUpgradeCommand} specified by its id
	 */
	IUpgradeCommand getUpgradeCmd( String id );
	
}
