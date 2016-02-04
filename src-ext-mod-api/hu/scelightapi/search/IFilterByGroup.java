/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapi.search;

import hu.scelight.search.FilterByGroup;
import hu.scelightapibase.util.IEnum;
import hu.scelightapibase.util.gui.HasRIcon;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Replay filter by group entity.
 * 
 * @author Andras Belicza
 * 
 * @see IEnum
 */
public interface IFilterByGroup extends HasRIcon, IEnum {
	
	/** SC2 client group. */
	IFilterByGroup         SC2                       = FilterByGroup.SC2;
	
	/** Replay properties. */
	IFilterByGroup         REPLAY                    = FilterByGroup.REPLAY;
	
	/** Chat message group. */
	IFilterByGroup         CHAT                      = FilterByGroup.CHAT;
	
	/** Replay File group. */
	IFilterByGroup         FILE                      = FilterByGroup.FILE;
	
	/** Players Average group. */
	IFilterByGroup         PLAYERS_AVG               = FilterByGroup.PLAYERS_AVG;
	
	/** Any Player group. */
	IFilterByGroup         ANY_PLAYER                = FilterByGroup.ANY_PLAYER;
	
	/** All Players group. */
	IFilterByGroup         ALL_PLAYERS               = FilterByGroup.ALL_PLAYERS;
	
	/** Player A group. */
	IFilterByGroup         PLAYER_A                  = FilterByGroup.PLAYER_A;
	
	/** Player A group. */
	IFilterByGroup         PLAYER_B                  = FilterByGroup.PLAYER_B;
	
	/** Player A group. */
	IFilterByGroup         PLAYER_C                  = FilterByGroup.PLAYER_C;
	
	/** Player A group. */
	IFilterByGroup         PLAYER_D                  = FilterByGroup.PLAYER_D;
	
	
	/** An unmodifiable list of all the filter by groups. */
	List< IFilterByGroup > VALUE_LIST                = Collections.unmodifiableList( Arrays.< IFilterByGroup > asList( FilterByGroup.VALUES ) );
	
	/** An unmodifiable set of player-specific filter by groups. */
	Set< IFilterByGroup >  PLAYER_SPECIFIC_VALUE_SET = Collections.< IFilterByGroup > unmodifiableSet( FilterByGroup.PLAYER_SPECIFIC_VALUE_SET );
	
	
	/**
	 * Returns an unmodifiable list of the filter by's of the group.
	 * 
	 * @return an unmodifiable list of the filter by's of the group
	 */
	List< ? extends IFilterBy > getFilterByList();
	
}
