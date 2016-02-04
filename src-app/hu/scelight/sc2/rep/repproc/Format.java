/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.sc2.rep.repproc;

import hu.scelight.gui.icon.Icons;
import hu.scelight.sc2.rep.model.attributesevents.AttributesEvents;
import hu.scelightapi.sc2.rep.repproc.IFormat;
import hu.sllauncher.gui.icon.LRIcon;

/**
 * Format.
 * 
 * @author Andras Belicza
 */
public enum Format implements IFormat {
    
    /** 1v1. */
	ONE_VS_ONE( "1v1", "1v1" ),
	
	/** 2v2. */
	TWO_VS_TWO( "2v2", "2v2" ),
	
	/** 3v3. */
	THREE_VS_THREE( "3v3", "3v3" ),
	
	/** 4v4. */
	FOUR_VS_FOUR( "4v4", "4v4" ),
	
	/** Archon. */
	ARCHON( "Archon", "" ),
	
	/** 5v5. */
	FIVE_VS_FIVE( "5v5", "5v5" ),
	
	/** 6v6. */
	SIX_VS_SIX( "6v6", "6v6" ),
	
	/** FFA. */
	FFA( "FFA", "FFA" ),
	
	/** Custom (e.g. 2v3). */
	CUSTOM( "Custom", "" );
	
	
	/** Text value of the format. */
	public final String	text;
						
	/** Controller value used for {@link AttributesEvents#A_PARTIES_PREMADE}. */
	public final String	attrValue;
						
						
	/**
	 * Creates a new {@link Format}.
	 * 
	 * @param text text value
	 * @param attrValue format value used for {@link AttributesEvents#A_PARTIES_PREMADE}
	 */
	private Format( final String text, final String attrValue ) {
		this.text = text;
		this.attrValue = attrValue;
	}
	
	@Override
	public String toString() {
		return text;
	}
	
	
	/**
	 * Returns the {@link Format} for the specified attribute value.
	 * 
	 * @param attrValue attribute value to return the format for
	 * @return the {@link Format} for the specified attribute value; or {@link Format#CUSTOM} if no matching format found
	 */
	public static Format fromAttrValue( final String attrValue ) {
		for ( final Format f : VALUES )
			if ( f.attrValue.equals( attrValue ) )
				return f;
				
		return Format.CUSTOM;
	}
	
	
	
	/** Ricon representing this entity. */
	public static final LRIcon	 RICON	= Icons.MY_FORMAT;
										
	/** Cache of the values array. */
	public static final Format[] VALUES	= values();
										
}
