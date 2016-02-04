/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.gui.page.repanalyzer.buildorders;

import hu.scelight.sc2.balancedata.model.Entity;

/**
 * Build order element, basically the data model of a row in the table.
 * 
 * @author Andras Belicza
 */
class BoElement implements Comparable< BoElement > {
	
	/** Loop of the element. */
	public int     loop;
	
	/** Player id of the element. */
	public Integer playerId;
	
	/** Entity of the element. */
	public Entity  entity;
	
	/**
	 * Defines a time (loop) order.
	 */
	@Override
	public int compareTo( final BoElement be ) {
		return loop - be.loop;
	}
	
}
