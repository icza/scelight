/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapi.sc2.rep.model.gameevents;

import hu.scelightapi.sc2.rep.model.IEvent;

/**
 * Control group update game event.
 * 
 * @author Andras Belicza
 */
public interface IControlGroupUpdateEvent extends IEvent {
	
	/** Control group index field name. */
	String F_GROUP_INDEX  = "controlGroupIndex";
	
	/** Control group update field name. */
	String F_GROUP_UPDATE = "controlGroupUpdate";
	
	/** Mask field name. */
	String F_MASK         = "mask";
	
	
	/**
	 * Returns the control group index.
	 * 
	 * @return the control group index
	 */
	Integer getGroupIndex();
	
	/**
	 * Returns the control group update.<br>
	 * Possible values:
	 * <ul>
	 * <li><code>0x00</code>: Assign
	 * <li><code>0x01</code>: Add selection (assign)
	 * <li><code>0x02</code>: Select
	 * </ul>
	 * 
	 * @return the control group update
	 */
	Integer getGroupUpdate();
	
	/**
	 * Returns the mask (result of a choice, either a Mask or OneIndices).
	 * 
	 * @return the mask (result of a choice, either a Mask or OneIndices)
	 */
	Object getMask();
	
	/**
	 * Tells if this control group event is a control group assign.
	 * 
	 * @return true if this control group event is a control group assign; false otherwise
	 */
	boolean isAssign();
	
	/**
	 * Tells if this control group event is a control group select.
	 * 
	 * @return true if this control group event is a control group select; false otherwise
	 */
	boolean isSelect();
	
}
