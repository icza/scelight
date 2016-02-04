/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.sc2.rep.cache;

import hu.scelight.service.settings.Settings;
import hu.sllauncher.bean.Bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * Replay processor cache config bean.
 * 
 * @author Andras Belicza
 */
@XmlAccessorType( XmlAccessType.FIELD )
class ConfigBean extends Bean {
	
	/** Current bean version. */
	public static final int BEAN_VER = 1;
	
	/**
	 * Initial per-minute calculation exclusion time setting used when records were cached ({@link Settings#INITIAL_PER_MIN_CALC_EXCL_TIME}).
	 */
	@XmlAttribute
	private Integer         initialPerMinCalcExclTime;
	
	/**
	 * Creates a new {@link ConfigBean}.
	 */
	public ConfigBean() {
		super( BEAN_VER );
	}
	
	/**
	 * Returns the initial per-minute calculation exclusion time setting used when records were cached.
	 * 
	 * @return the initial per-minute calculation exclusion time setting used when records were cached
	 */
	public Integer getInitialPerMinCalcExclTime() {
		return initialPerMinCalcExclTime;
	}
	
	/**
	 * Sets the initial per-minute calculation exclusion time.
	 * 
	 * @param initialPerMinCalcExclTime initial per-minute calculation exclusion time to be set
	 */
	public void setInitialPerMinCalcExclTime( Integer initialPerMinCalcExclTime ) {
		this.initialPerMinCalcExclTime = initialPerMinCalcExclTime;
	}
	
}
