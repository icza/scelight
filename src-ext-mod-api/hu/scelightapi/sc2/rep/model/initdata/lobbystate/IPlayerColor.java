/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapi.sc2.rep.model.initdata.lobbystate;

import hu.scelight.sc2.rep.model.initdata.lobbystate.PlayerColor;
import hu.scelightapibase.util.IEnum;
import hu.scelightapibase.util.gui.HasIcon;

import java.awt.Color;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Player color.
 * 
 * @author Andras Belicza
 * 
 * @see IEnum
 */
public interface IPlayerColor extends HasIcon, IEnum {
	
	/** Unknown. */
	IPlayerColor         UNKNOWN     = PlayerColor.UNKNOWN;
	
	/** Red. */
	IPlayerColor         RED         = PlayerColor.RED;
	
	/** Blue. */
	IPlayerColor         BLUE        = PlayerColor.BLUE;
	
	/** Teal. */
	IPlayerColor         TEAL        = PlayerColor.TEAL;
	
	/** Purple. */
	IPlayerColor         PURPLE      = PlayerColor.PURPLE;
	
	/** Yellow. */
	IPlayerColor         YELLOW      = PlayerColor.YELLOW;
	
	/** Orange. */
	IPlayerColor         ORANGE      = PlayerColor.ORANGE;
	
	/** Green. */
	IPlayerColor         GREEN       = PlayerColor.GREEN;
	
	/** Light pink. */
	IPlayerColor         LIGHT_PINK  = PlayerColor.LIGHT_PINK;
	
	/** Violet. */
	IPlayerColor         VIOLET      = PlayerColor.VIOLET;
	
	/** Light gray. */
	IPlayerColor         LIGHT_GRAY  = PlayerColor.LIGHT_GRAY;
	
	/** Dark green. */
	IPlayerColor         DARK_GREEN  = PlayerColor.DARK_GREEN;
	
	/** Brown. */
	IPlayerColor         BROWN       = PlayerColor.BROWN;
	
	/** Light green. */
	IPlayerColor         LIGHT_GREEN = PlayerColor.LIGHT_GREEN;
	
	/** Dark gray. */
	IPlayerColor         DARK_GRAY   = PlayerColor.LIGHT_GRAY;
	
	/** Pink. */
	IPlayerColor         PINK        = PlayerColor.PINK;
	
	
	/** An unmodifiable list of all the player colors. */
	List< IPlayerColor > VALUE_LIST  = Collections.unmodifiableList( Arrays.< IPlayerColor > asList( PlayerColor.VALUES ) );
	
	/**
	 * Returns the color value of this player color.
	 * 
	 * @return the color value of this player color
	 */
	Color getColor();
	
	/**
	 * Returns a darker color value of this player color.
	 * 
	 * @return a darker color value of this player color
	 */
	Color getDarkerColor();
	
	/**
	 * Returns a brighter color value of this player color.
	 * 
	 * @return a brighter color value of this player color
	 */
	Color getBrighterColor();
	
	/**
	 * Returns a CSS value of the color.
	 * 
	 * @return CSS value of the color
	 */
	String getCssColor();
	
	/**
	 * Returns a CSS value of the darker color.
	 * 
	 * @return CSS value of the darker color
	 */
	String getDarkerCssColor();
	
}
