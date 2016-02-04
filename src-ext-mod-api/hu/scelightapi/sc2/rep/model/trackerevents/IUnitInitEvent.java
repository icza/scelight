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

/**
 * Unit init tracker event: appears for units under construction. When complete a {@link IUnitDoneEvent} appears with the same unit tag.
 * 
 * @author Andras Belicza
 * 
 * @see IUnitDoneEvent
 */
public interface IUnitInitEvent extends IBaseUnitEvent {
	
	// No additional fields added yet.
	
}
