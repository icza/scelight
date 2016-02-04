/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.util.gui;

import java.awt.Color;

import javax.swing.Icon;

import hu.scelight.gui.icon.Icons;
import hu.scelightapibase.util.gui.HasIcon;
import hu.sllauncher.gui.comp.ColorIcon;

/**
 * Color enumeration.
 * 
 * @author Andras Belicza
 */
public enum ColorEnum implements HasIcon {
    
    /** Default, has color value <code>null</code>. */
	DEFAULT( "System Default", null ),
	
	/** Black. */
	BLACK( "Black", Color.BLACK ),
	
	/** Dark Gray. */
	DARK_GRAY( "Dark Gray", Color.DARK_GRAY ),
	
	/** Gray. */
	GRAY( "Gray", Color.GRAY ),
	
	/** Light Gray. */
	LIGHT_GRAY( "Light Gray", Color.LIGHT_GRAY ),
	
	/** White. */
	WHITE( "White", Color.WHITE ),
	
	/** Yellow. */
	YELLOW( "Yellow", Color.YELLOW ), // Red+Green
	
	/** Orange. */
	ORANGE( "Orange", Color.ORANGE ),
	
	/** Pink. */
	PINK( "Pink", Color.PINK ),
	
	/** Red. */
	RED( "Red", Color.RED ),
	
	/** Magenta. */
	MAGENTA( "Magenta", Color.MAGENTA ), // Red+Blue
	
	/** Blue. */
	BLUE( "Blue", Color.BLUE ),
	
	/** Cyan. */
	CYAN( "Cyan", Color.CYAN ), // Green+Blue
	
	/** Green. */
	GREEN( "Green", Color.GREEN ),
	
	/** Dark Yellow. */
	DARK_YELLOW( "Dark Yellow", new Color( 0x80, 0x80, 0x00 ) ),
	
	/** Dark Magenta */
	DARK_MAGENTA( "Dark Magenta", new Color( 0x80, 0x00, 0x80 ) ),
	
	/** Dark Cyan. */
	DARK_CYAN( "Dark Cyan", new Color( 0x00, 0x80, 0x80 ) );
	
	
	/** Original name (without extra info). */
	public final String	origName;
						
	/** Text value of the color (with extra info such as RGB hex value). */
	public final String	text;
						
	/** {@link Color} value of this color. */
	public final Color	color;
						
	/** An icon which is filled with the player color. */
	public final Icon	icon;
						
						
	/**
	 * Creates a new {@link Color}.
	 * 
	 * @param origName original name (without extra info)
	 * @param color {@link Color} value of this player color
	 */
	private ColorEnum( final String origName, final Color color ) {
		this.origName = origName;
		this.text = origName + ( color == null ? "" : String.format( " (%06X)", color.getRGB() & 0xffffff ) );
		this.color = color;
		icon = "DEFAULT".equals( name() ) ? Icons.MY_EMPTY.get() : new ColorIcon( color, text );
	}
	
	/**
	 * Returns the {@link Color} value of this color.
	 * 
	 * @return the {@link Color} value of this color
	 */
	public Color getColor() {
		return color;
	}
	
	@Override
	public Icon getIcon() {
		return icon;
	}
	
	@Override
	public String toString() {
		return text;
	}
	
	
	/** Cache of the values array. */
	public static final ColorEnum[] VALUES = values();
	
}
