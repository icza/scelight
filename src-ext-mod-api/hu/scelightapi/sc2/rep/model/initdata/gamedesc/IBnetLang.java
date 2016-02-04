/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapi.sc2.rep.model.initdata.gamedesc;

import hu.scelight.sc2.rep.model.initdata.gamedesc.BnetLang;
import hu.scelightapibase.util.IEnum;
import hu.scelightapibase.util.gui.HasRIcon;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Available Battle.net languages on the Battle.net web.
 * 
 * @author Andras Belicza
 * 
 * @see IRegion
 * @see IEnum
 */
public interface IBnetLang extends HasRIcon {
	
	/** English. */
	IBnetLang         ENGLISH             = BnetLang.ENGLISH;
	
	/** Chinese (Traditional). */
	IBnetLang         CHINESE_TRADITIONAL = BnetLang.CHINESE_TRADITIONAL;
	
	/** French. */
	IBnetLang         FRENCH              = BnetLang.FRENCH;
	
	/** German. */
	IBnetLang         GERMAN              = BnetLang.GERMAN;
	
	/** Italian. */
	IBnetLang         ITALIAN             = BnetLang.ITALIAN;
	
	/** Korean. */
	IBnetLang         KOREAN              = BnetLang.KOREAN;
	
	/** Polish. */
	IBnetLang         POLISH              = BnetLang.POLISH;
	
	/** Portuguese. */
	IBnetLang         PORTUGUESE          = BnetLang.PORTUGUESE;
	
	/** Russian. */
	IBnetLang         RUSSIAN             = BnetLang.RUSSIAN;
	
	/** Spanish. */
	IBnetLang         SPANISH             = BnetLang.SPANISH;
	
	
	/** An unmodifiable list of all the battle.net languages. */
	List< IBnetLang > VALUE_LIST          = Collections.unmodifiableList( Arrays.< IBnetLang > asList( BnetLang.VALUES ) );
	
	
	/**
	 * Returns the language code, the way it appears in URLs.
	 * 
	 * @return the language code, the way it appears in URLs
	 */
	String getLangCode();
	
}
