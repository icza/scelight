/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapi.sc2.rep.repproc;

import hu.scelight.sc2.rep.repproc.Format;
import hu.scelightapibase.util.IEnum;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Format.
 * 
 * @author Andras Belicza
 * 		
 * @see IEnum
 */
public interface IFormat extends IEnum {
	
	/** 1v1. */
	IFormat			ONE_VS_ONE	   = Format.ONE_VS_ONE;
								   
	/** 2v2. */
	IFormat			TWO_VS_TWO	   = Format.TWO_VS_TWO;
								   
	/** 3v3. */
	IFormat			THREE_VS_THREE = Format.THREE_VS_THREE;
								   
	/** 4v4. */
	IFormat			FOUR_VS_FOUR   = Format.FOUR_VS_FOUR;
								   
	/** Archon. */
	IFormat			ARCHON		   = Format.ARCHON;
								   
	/** 5v5. */
	IFormat			FIVE_VS_FIVE   = Format.FIVE_VS_FIVE;
								   
	/** 6v6. */
	IFormat			SIX_VS_SIX	   = Format.SIX_VS_SIX;
								   
	/** FFA. */
	IFormat			FFA			   = Format.FFA;
								   
	/** Custom (e.g. 2v3). */
	IFormat			CUSTOM		   = Format.CUSTOM;
								   
								   
	/** An unmodifiable list of all the formats. */
	List< IFormat >	VALUE_LIST	   = Collections.unmodifiableList( Arrays.< IFormat > asList( Format.VALUES ) );
								   
}
