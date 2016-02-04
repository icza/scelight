/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapi.sc2.rep.model.gameevents.cmd;

import hu.scelight.sc2.rep.model.gameevents.cmd.CmdFlag;
import hu.scelightapibase.util.IEnum;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Cmd flag bits / components.
 * 
 * @author Andras Belicza
 * 
 * @see IEnum
 */
public interface ICmdFlag extends IEnum {
	
	/** Alternate flag. */
	ICmdFlag         ALTERNATE               = CmdFlag.ALTERNATE;
	
	/** Queued flag. */
	ICmdFlag         QUEUED                  = CmdFlag.QUEUED;
	
	/** Preempt flag. */
	ICmdFlag         PREEMPT                 = CmdFlag.PREEMPT;
	
	/** Smart click flag. */
	ICmdFlag         SMART_CLICK             = CmdFlag.SMART_CLICK;
	
	/** Smart rally flag. */
	ICmdFlag         SMART_RALLY             = CmdFlag.SMART_RALLY;
	
	/** Subgroup flag. */
	ICmdFlag         SUBGROUP                = CmdFlag.SUBGROUP;
	
	/** Set auto cast flag. */
	ICmdFlag         SET_AUTO_CAST           = CmdFlag.SET_AUTO_CAST;
	
	/** Set autocast on flag. */
	ICmdFlag         SET_AUTO_CAST_ON        = CmdFlag.SET_AUTO_CAST_ON;
	
	/**
	 * User flag.<br>
	 * Basically every command is user issued, so do not indicate this flag...
	 */
	ICmdFlag         USER                    = CmdFlag.USER;
	
	/** Data A flag; aka: DATA_PASSENGER */
	ICmdFlag         DATA_A                  = CmdFlag.DATA_A;
	
	/** Data B flag; aka: DATA_ABIL_QUEUE_ORDER_ID. */
	ICmdFlag         DATA_B                  = CmdFlag.DATA_B;
	
	/** AI flag. */
	ICmdFlag         AI                      = CmdFlag.AI;
	
	/** AI ignore on finish flag. */
	ICmdFlag         AI_IGNORE_ON_FINISH     = CmdFlag.AI_IGNORE_ON_FINISH;
	
	/** Is order flag. */
	ICmdFlag         IS_ORDER                = CmdFlag.IS_ORDER;
	
	/** Script flag. */
	ICmdFlag         SCRIPT                  = CmdFlag.SCRIPT;
	
	/** Homogenous interruption flag. */
	ICmdFlag         HOMOGENOUS_INTERRUPTION = CmdFlag.HOMOGENOUS_INTERRUPTION;
	
	/** Minimap flag. */
	ICmdFlag         MINIMAP                 = CmdFlag.MINIMAP;
	
	/** Repeat flag. */
	ICmdFlag         REPEAT                  = CmdFlag.REPEAT;
	
	/** Dispatch to other unit flag. */
	ICmdFlag         DISPATCH_TO_OTHER_UNIT  = CmdFlag.DISPATCH_TO_OTHER_UNIT;
	
	/** Target self flag. */
	ICmdFlag         TARGET_SELF             = CmdFlag.TARGET_SELF;
	
	
	/** An unmodifiable list of all the leagues. */
	List< ICmdFlag > VALUE_LIST              = Collections.unmodifiableList( Arrays.< ICmdFlag > asList( CmdFlag.VALUES ) );
	
	
	/**
	 * The mask of the cmd flag.
	 * 
	 * @return the mask of the cmd flag
	 */
	int getMask();
	
}
