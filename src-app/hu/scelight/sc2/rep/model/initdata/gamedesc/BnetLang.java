/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.sc2.rep.model.initdata.gamedesc;

import hu.scelight.gui.icon.Icons;
import hu.scelightapi.sc2.rep.model.initdata.gamedesc.IBnetLang;
import hu.sllauncher.gui.icon.LRIcon;

/**
 * Available Battle.net languages on the Battle.net web.
 * 
 * @author Andras Belicza
 * 
 * @see Region
 */
public enum BnetLang implements IBnetLang {
	
	/** English. */
	ENGLISH( "English", "en", Icons.C_UNITED_STATES ),
	
	/** Chinese (Traditional). */
	CHINESE_TRADITIONAL( "Chinese (Traditional)", "zh", Icons.C_CHINA ),
	
	/** French. */
	FRENCH( "French", "fr", Icons.C_FRANCE ),
	
	/** German. */
	GERMAN( "German", "de", Icons.C_GERMANY ),
	
	/** Italian. */
	ITALIAN( "Italian", "it", Icons.C_ITALY ),
	
	/** Korean. */
	KOREAN( "Korean", "ko", Icons.C_KOREA ),
	
	/** Polish. */
	POLISH( "Polish", "pl", Icons.C_POLAND ),
	
	/** Portuguese. */
	PORTUGUESE( "Portuguese", "pt", Icons.C_BRAZIL ),
	
	/** Russian. */
	RUSSIAN( "Russian", "ru", Icons.C_RUSSIA ),
	
	/** Spanish. */
	SPANISH( "Spanish", "es", Icons.C_SPAIN );
	
	
	/** Text value of the bnet language. */
	public final String text;
	
	/** Language code, the way it appears in URLs. */
	public final String langCode;
	
	/** Ricon of the region. */
	public final LRIcon ricon;
	
	
	/**
	 * Creates a new {@link BnetLang}.
	 * 
	 * @param text text value
	 * @param langCode language code
	 * @param ricon ricon of the region
	 */
	private BnetLang( final String text, final String langCode, final LRIcon ricon ) {
		this.text = text;
		this.langCode = langCode;
		this.ricon = ricon;
	}
	
	@Override
	public String getLangCode() {
		return langCode;
	}
	
	@Override
	public String toString() {
		return text;
	}
	
	@Override
	public LRIcon getRicon() {
		return ricon;
	}
	
	
	/** Cache of the values array. */
	public static final BnetLang[] VALUES = values();
	
}
