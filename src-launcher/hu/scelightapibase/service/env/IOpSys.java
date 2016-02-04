/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapibase.service.env;

import hu.scelightapibase.util.IEnum;
import hu.sllauncher.service.env.OpSys;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Operating system.
 * 
 * @author Andras Belicza
 * 
 * @see hu.scelightapi.util.IUtils#detectOs()
 * @see IEnum
 */
public interface IOpSys extends IEnum {
	
	/** Windows. */
	IOpSys         WINDOWS    = OpSys.WINDOWS;
	
	/** MAC OS-X. */
	IOpSys         OS_X       = OpSys.OS_X;
	
	/** Unix (including Linux). */
	IOpSys         UNIX       = OpSys.UNIX;
	
	/** Solaris (Sun OS). */
	IOpSys         SOLARIS    = OpSys.SOLARIS;
	
	/** Other. */
	IOpSys         OTHER      = OpSys.OTHER;
	
	
	/** An unmodifiable list of all the operating systems. */
	List< IOpSys > VALUE_LIST = Collections.unmodifiableList( Arrays.< IOpSys > asList( OpSys.VALUES ) );
	
}
