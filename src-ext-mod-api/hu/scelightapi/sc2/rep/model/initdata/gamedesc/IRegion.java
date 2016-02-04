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

import hu.scelight.sc2.rep.model.initdata.gamedesc.Region;
import hu.scelightapi.sc2.rep.model.details.IRealm;
import hu.scelightapi.util.gui.ITableIcon;
import hu.scelightapibase.util.IEnum;
import hu.scelightapibase.util.gui.HasRIcon;

import java.net.URL;
import java.util.List;
import java.util.Set;

/**
 * SC2 region (gateway).
 * 
 * @author Andras Belicza
 * 
 * @see IEnum
 */
public interface IRegion extends HasRIcon, IEnum {
	
	/** US */
	IRegion US          = Region.US;
	
	/** Europe */
	IRegion EUROPE      = Region.EUROPE;
	
	/** Korea */
	IRegion KOREA       = Region.KOREA;
	
	/** China */
	IRegion CHINA       = Region.CHINA;
	
	/** South East Asia */
	IRegion SEA         = Region.SEA;
	
	/** Public Test */
	IRegion PUBLIC_TEST = Region.PUBLIC_TEST;
	
	/** Unknown */
	IRegion UNKNOWN     = Region.UNKNOWN;
	
	
	/**
	 * Returns the region code.
	 * 
	 * @return the region code
	 */
	String getCode();
	
	/**
	 * Returns the region id.
	 * 
	 * @return the region id
	 */
	int getRegionId();
	
	/**
	 * Returns the depot server URL of the region.
	 * 
	 * @return the depot server URL of the region
	 */
	URL getDepotServerUrl();
	
	/**
	 * Returns the URL of the region's battle.net web site.
	 * 
	 * @return the URL of the region's battle.net web site
	 */
	URL getBnetUrl();
	
	/**
	 * Returns the default language ({@link IBnetLang}) on the region's web page.
	 * 
	 * @return the default language ({@link IBnetLang}) on the region's web page
	 */
	IBnetLang getDefaultLang();
	
	/**
	 * Returns an unmodifiable set of the available languages ({@link IBnetLang}) on the region's web page.
	 * 
	 * @return an unmodifiable set of the available languages ({@link IBnetLang}) on the region's web page
	 */
	Set< ? extends IBnetLang > getLangSet();
	
	/**
	 * Returns an unmodifiable list of the realms of the region.
	 * 
	 * @return an unmodifiable list of the realms of the region
	 */
	List< ? extends IRealm > getRealmList();
	
	/**
	 * Returns an icon for tables.
	 * 
	 * @param <T> type parameter specifying the requirements for {@link ITableIcon}
	 * 
	 * @return an icon for tables
	 */
	< T extends IRegion & Comparable< T > > ITableIcon< T > getTableIcon();
	
	/**
	 * Returns the realm of the region specified by its id.
	 * 
	 * @param realmId id of the realm to return
	 * @return the realm of the region specified by its id; or {@link IRealm#UNKNOWN} if no realm is found for the specified realm id
	 */
	IRealm getRealm( int realmId );
	
}
