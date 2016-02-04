/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.sc2.rep.model.gameevents.cmd;

import hu.scelightapi.sc2.rep.model.gameevents.cmd.ICmdFlag;

/**
 * Cmd flag bits / components.
 * 
 * @author Andras Belicza
 */
public enum CmdFlag implements ICmdFlag {
	
	/** Alternate flag. */
	ALTERNATE( "Alternate" ),
	
	/** Queued flag. */
	QUEUED( "Queued" ),
	
	/** Preempt flag. */
	PREEMPT( "Preempt" ),
	
	/** Smart click flag. */
	SMART_CLICK( "Right click" ),
	
	/** Smart rally flag. */
	SMART_RALLY( "Smart rally" ),
	
	/** Subgroup flag. */
	SUBGROUP( "Wireframe click " ),
	
	/** Set auto cast flag. */
	SET_AUTO_CAST( "Auto cast Off" ),
	
	/** Set autocast on flag. */
	SET_AUTO_CAST_ON( "Auto cast On" ),
	
	/**
	 * User flag.<br>
	 * Basically every command is user issued, so do not indicate this flag...
	 */
	USER( null ),
	
	/** Data A flag; aka: DATA_PASSENGER */
	DATA_A( "Wireframe unload" ),
	
	/** Data B flag; aka: DATA_ABIL_QUEUE_ORDER_ID. */
	DATA_B( "Wireframe cancel" ),
	
	/** AI flag. */
	AI( "AI" ),
	
	/** AI ignore on finish flag. */
	AI_IGNORE_ON_FINISH( "AI ignore on finish" ),
	
	/** Is order flag. */
	IS_ORDER( "Order" ),
	
	/** Script flag. */
	SCRIPT( "Script" ),
	
	/** Homogenous interruption flag. */
	HOMOGENOUS_INTERRUPTION( "Hom. Int." ),
	
	/** Minimap flag. */
	MINIMAP( "Minimap" ),
	
	/** Repeat flag. */
	REPEAT( "Repeat" ),
	
	/** Dispatch to other unit flag. */
	DISPATCH_TO_OTHER_UNIT( "Dispatch" ),
	
	/** Target self flag. */
	TARGET_SELF( "Target self" );
	
	
	/** Text value to include in the parameters string. */
	public final String text;
	
	/** Mask of the component. */
	public final int    mask;
	
	
	/**
	 * Creates a new {@link CmdFlag}.
	 * 
	 * @param text text value to include in the parameters string
	 */
	private CmdFlag( final String text ) {
		this.text = text == null ? "" : '[' + text + "] ";
		this.mask = 0x01 << ordinal();
	}
	
	@Override
	public int getMask() {
		return mask;
	}
	
	
	/** Cache of the values array. */
	public static final CmdFlag[] VALUES = values();
	
}
