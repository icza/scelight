/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapi.sc2.rep.model.trackerevents;

import hu.scelightapi.sc2.rep.model.IEvent;

/**
 * Player stats tracker event.
 * 
 * <p>
 * This event contains only 1 field ({@link #F_STATS}) which is a structure. Everything else is inside of this stats structure field, so for simplicity and
 * efficiency everything else is provided relative to that.
 * </p>
 * 
 * @author Andras Belicza
 */
public interface IPlayerStatsEvent extends IEvent {
	
	/** Stats event field name. */
	String F_STATS                      = "stats";
	
	
	// Everything is inside of the preceding stats struct field, so for simplicity and efficiency
	// I provide everything else relative to that.
	
	
	/** Minerals current event field name. */
	String F_MINS_CURRENT               = "scoreValueMineralsCurrent";
	
	/** Vespene current event field name. */
	String F_GAS_CURRENT                = "scoreValueVespeneCurrent";
	
	
	/** Minerals collection rate event field name. */
	String F_MINS_COLL_RATE             = "scoreValueMineralsCollectionRate";
	
	/** Vespene collection rate current event field name. */
	String F_GAS_COLL_RATE              = "scoreValueVespeneCollectionRate";
	
	
	/** Workers active count event field name. */
	String F_WORKERS_ACTIVE_COUNT       = "scoreValueWorkersActiveCount";
	
	
	/** Minerals used in progress army event field name. */
	String F_MINS_USED_IN_PROGRESS_ARMY = "scoreValueMineralsUsedInProgressArmy";
	
	/** Minerals used in progress economy event field name. */
	String F_MINS_USED_IN_PROGRESS_ECON = "scoreValueMineralsUsedInProgressEconomy";
	
	/** Minerals used in progress technology event field name. */
	String F_MINS_USED_IN_PROGRESS_TECH = "scoreValueMineralsUsedInProgressTechnology";
	
	/** Vespene used in progress army event field name. */
	String F_GAS_USED_IN_PROGRESS_ARMY  = "scoreValueVespeneUsedInProgressArmy";
	
	/** Vespene used in progress economy event field name. */
	String F_GAS_USED_IN_PROGRESS_ECON  = "scoreValueVespeneUsedInProgressEconomy";
	
	/** Vespene used in progress technology event field name. */
	String F_GAS_USED_IN_PROGRESS_TECH  = "scoreValueVespeneUsedInProgressTechnology";
	
	
	/** Minerals used in current army event field name. */
	String F_MINS_USED_IN_CURRENT_ARMY  = "scoreValueMineralsUsedCurrentArmy";
	
	/** Minerals used in current economy event field name. */
	String F_MINS_USED_IN_CURRENT_ECON  = "scoreValueMineralsUsedCurrentEconomy";
	
	/** Minerals used in current technology event field name. */
	String F_MINS_USED_IN_CURRENT_TECH  = "scoreValueMineralsUsedCurrentTechnology";
	
	/** Vespene used in current army event field name. */
	String F_GAS_USED_IN_CURRENT_ARMY   = "scoreValueVespeneUsedCurrentArmy";
	
	/** Vespene used in current economy event field name. */
	String F_GAS_USED_IN_CURRENT_ECON   = "scoreValueVespeneUsedCurrentEconomy";
	
	/** Vespene used in current technology event field name. */
	String F_GAS_USED_IN_CURRENT_TECH   = "scoreValueVespeneUsedCurrentTechnology";
	
	
	/** Minerals lost army event field name. */
	String F_MINS_LOST_ARMY             = "scoreValueMineralsLostArmy";
	
	/** Minerals lost economy event field name. */
	String F_MINS_LOST_ECON             = "scoreValueMineralsLostEconomy";
	
	/** Minerals lost technology event field name. */
	String F_MINS_LOST_TECH             = "scoreValueMineralsLostTechnology";
	
	/** Vespene lost army event field name. */
	String F_GAS_LOST_ARMY              = "scoreValueVespeneLostArmy";
	
	/** Vespene lost economy event field name. */
	String F_GAS_LOST_ECON              = "scoreValueVespeneLostEconomy";
	
	/** Vespene lost technology event field name. */
	String F_GAS_LOST_TECH              = "scoreValueVespeneLostTechnology";
	
	
	/** Minerals killed army event field name. */
	String F_MINS_KILLED_ARMY           = "scoreValueMineralsKilledArmy";
	
	/** Minerals killed economy event field name. */
	String F_MINS_KILLED_ECON           = "scoreValueMineralsKilledEconomy";
	
	/** Minerals killed technology event field name. */
	String F_MINS_KILLED_TECH           = "scoreValueMineralsKilledTechnology";
	
	/** Vespene killed army event field name. */
	String F_GAS_KILLED_ARMY            = "scoreValueVespeneKilledArmy";
	
	/** Vespene killed economy event field name. */
	String F_GAS_KILLED_ECON            = "scoreValueVespeneKilledEconomy";
	
	/** Vespene killed technology event field name. */
	String F_GAS_KILLED_TECH            = "scoreValueVespeneKilledTechnology";
	
	
	/** Food used event field name. Fixed point, have to divide by 4096. */
	String F_FOOD_USED                  = "scoreValueFoodUsed";
	
	/** Food used event field name. Fixed point, have to divide by 4096. */
	String F_FOOD_MADE                  = "scoreValueFoodMade";
	
	
	/** Minerals used active forces event field name. */
	String F_MINS_USED_ACTIVE_FORCES    = "scoreValueMineralsUsedActiveForces";
	
	/** Vespene used active forces event field name. */
	String F_GAS_USED_ACTIVE_FORCES     = "scoreValueVespeneUsedActiveForces";
	
	
	
	/**
	 * Returns the minerals current.
	 * 
	 * @return the minerals current
	 */
	Integer getMineralsCurrent();
	
	/**
	 * Returns the vespene current.
	 * 
	 * @return the vespene current
	 */
	Integer getGasCurrent();
	
	/**
	 * Returns the minerals collection rate.
	 * 
	 * @return the minerals collection rate
	 */
	Integer getMinsCollRate();
	
	/**
	 * Returns the vespene collection rate.
	 * 
	 * @return the vespene collection rate
	 */
	Integer getGasCollRate();
	
	/**
	 * Returns the workers active count.
	 * 
	 * @return the workers active count
	 */
	Integer getWorkersActiveCount();
	
	/**
	 * Returns the the minerals used in progress army.
	 * 
	 * @return the minerals used in progress army
	 */
	Integer getMinsUsedInProgressArmy();
	
	/**
	 * Returns the minerals used in progress economy.
	 * 
	 * @return the minerals used in progress economy
	 */
	Integer getMinsUsedInProgressEcon();
	
	/**
	 * Returns the minerals used in progress technology.
	 * 
	 * @return the minerals used in progress technology
	 */
	Integer getMinsUsedInProgressTech();
	
	/**
	 * Returns the vespene used in progress army.
	 * 
	 * @return the vespene used in progress army
	 */
	Integer getGasUsedInProgressArmy();
	
	/**
	 * Returns the vespene used in progress economy.
	 * 
	 * @return the vespene used in progress economy
	 */
	Integer getGasUsedInProgressEcon();
	
	/**
	 * Returns the vespene used in progress technology.
	 * 
	 * @return the vespene used in progress technology
	 */
	Integer getGasUsedInProgressTech();
	
	/**
	 * Returns the minerals used in current army.
	 * 
	 * @return the minerals used in current army
	 */
	Integer getMinsUsedInCurrentArmy();
	
	/**
	 * Returns the minerals used in current economy.
	 * 
	 * @return the minerals used in current economy
	 */
	Integer getMinsUsedInCurrentEcon();
	
	/**
	 * Returns the minerals used in current technology.
	 * 
	 * @return the minerals used in current technology
	 */
	Integer getMinsUsedInCurrentTech();
	
	/**
	 * Returns the vespene used in current army.
	 * 
	 * @return the vespene used in current army
	 */
	Integer getGasUsedInCurrentArmy();
	
	/**
	 * Returns the vespene used in current economy.
	 * 
	 * @return the vespene used in current economy
	 */
	Integer getGasUsedInCurrentEcon();
	
	/**
	 * Returns the vespene used in current technology.
	 * 
	 * @return the vespene used in current technology
	 */
	Integer getGasUsedInCurrentTech();
	
	/**
	 * Returns the minerals lost army.
	 * 
	 * @return the minerals lost army
	 */
	Integer getMinsLostArmy();
	
	/**
	 * Returns the minerals lost economy.
	 * 
	 * @return the minerals lost economy
	 */
	Integer getMinsLostEcon();
	
	/**
	 * Returns the minerals lost technology.
	 * 
	 * @return the minerals lost technology
	 */
	Integer getMinsLostTech();
	
	/**
	 * Returns the vespene lost army.
	 * 
	 * @return the vespene lost army
	 */
	Integer getGasLostArmy();
	
	/**
	 * Returns the vespene lost economy.
	 * 
	 * @return the vespene lost economy
	 */
	Integer getGasLostEcon();
	
	/**
	 * Returns the vespene lost technology.
	 * 
	 * @return the vespene lost technology
	 */
	Integer getGasLostTech();
	
	/**
	 * Returns the minerals killed army.
	 * 
	 * @return the minerals killed army
	 */
	Integer getMinsKilledArmy();
	
	/**
	 * Returns the minerals killed economy.
	 * 
	 * @return the minerals killed economy
	 */
	Integer getMinsKilledEcon();
	
	/**
	 * Returns the minerals killed technology.
	 * 
	 * @return the minerals killed technology
	 */
	Integer getMinsKilledTech();
	
	/**
	 * Returns the vespene killed army.
	 * 
	 * @return the vespene killed army
	 */
	Integer getGasKilledArmy();
	
	/**
	 * Returns the vespene killed economy.
	 * 
	 * @return the vespene killed economy
	 */
	Integer getGasKilledEcon();
	
	/**
	 * Returns the vespene killed technology.
	 * 
	 * @return the vespene killed technology
	 */
	Integer getGasKilledTech();
	
	/**
	 * Returns the food used.
	 * 
	 * @return the food used
	 */
	Integer getFoodUsed();
	
	/**
	 * Returns the food made.
	 * 
	 * @return the food made
	 */
	Integer getFoodMade();
	
	/**
	 * Returns the minerals used active forces.
	 * 
	 * @return the minerals used active forces
	 */
	Integer getMinsUsedActiveForces();
	
	/**
	 * Returns the vespene used active forces.
	 * 
	 * @return the vespene used active forces
	 */
	Integer getGasUsedActiveForces();
	
}
