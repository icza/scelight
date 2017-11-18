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
import hu.scelight.sc2.rep.model.details.Realm;
import hu.scelight.util.Utils;
import hu.scelight.util.gui.TableIcon;
import hu.scelightapi.sc2.rep.model.details.IRealm;
import hu.scelightapi.sc2.rep.model.initdata.gamedesc.IBnetLang;
import hu.scelightapi.sc2.rep.model.initdata.gamedesc.IRegion;
import hu.sllauncher.gui.icon.LRIcon;

import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

/**
 * SC2 region (gateway).
 * 
 * @author Andras Belicza
 */
public enum Region implements IRegion {
	
	/** US */
	US( "US", "US", 1, Icons.C_UNITED_STATES, "http://us.depot.battle.net:1119/", "http://us.battle.net/", BnetLang.ENGLISH, EnumSet.of( BnetLang.ENGLISH,
	        BnetLang.SPANISH, BnetLang.PORTUGUESE ), Realm.NORTH_AMERICA, Realm.LATIN_AMERICA ),
	
	/** Europe */
	EUROPE( "Europe", "EU", 2, Icons.C_EUROPE, "http://eu.depot.battle.net:1119/", "http://eu.battle.net/", BnetLang.ENGLISH, EnumSet.of( BnetLang.ENGLISH,
	        BnetLang.GERMAN, BnetLang.FRENCH, BnetLang.SPANISH, BnetLang.RUSSIAN, BnetLang.ITALIAN, BnetLang.POLISH ), Realm.EUROPE, Realm.RUSSIA ),
	
	/** Korea */
	KOREA( "Korea", "KR", 3, Icons.C_KOREA, "http://kr.depot.battle.net:1119/", "http://kr.battle.net/", BnetLang.KOREAN, EnumSet.of( BnetLang.KOREAN,
	        BnetLang.CHINESE_TRADITIONAL ), Realm.KOREA, Realm.TAIWAN ),
	
	/** China */
	CHINA( "China", "CN", 5, Icons.C_CHINA, "http://cn.depot.battle.net:1119/", "http://www.battlenet.com.cn/", BnetLang.CHINESE_TRADITIONAL, EnumSet
	        .of( BnetLang.CHINESE_TRADITIONAL ), Realm.CHINA ),
	
	/** South East Asia */
	SEA(
	        "SEA",
	        "SG",
	        6,
	        Icons.C_AUSTRALIA,
	        "http://sg.depot.battle.net:1119/",
	        "http://sea.battle.net/",
	        BnetLang.ENGLISH,
	        EnumSet.of( BnetLang.ENGLISH ),
	        Realm.SEA ),
	
	/** Public Test */
	PUBLIC_TEST( "Public Test", "XX", 98, Icons.MY_EMPTY, "http://xx.depot.battle.net:1119/", "http://us.battle.net/", BnetLang.ENGLISH, EnumSet
	        .of( BnetLang.ENGLISH ) ),
	
	/** Unknown */
	UNKNOWN( "Unknown", "", -1, Icons.MY_EMPTY, "http://unknown.depot.battle.net:1119/", "http://unknown.battle.net/", BnetLang.ENGLISH, EnumSet
	        .of( BnetLang.ENGLISH ) );
	
	
	/** Text value of the region. */
	public final String              text;
	
	/** Region code. */
	public final String              code;
	
	/** Region id. */
	public final int                 regionId;
	
	/** Ricon of the region. */
	public final LRIcon              ricon;
	
	/** Depot server URL of the region. */
	public final URL                 depotServerUrl;
	
	/** URL of the region's battle.net web site. */
	public final URL                 bnetUrl;
	
	/** Default language ({@link BnetLang}) on the region's web page. */
	public final BnetLang            defaultLang;
	
	/** Available languages ({@link BnetLang}) on the region's web page. */
	public final EnumSet< BnetLang > langSet;
	
	/** An unmodifiable set of the available languages ({@link BnetLang}) on the region's web page. */
	public final Set< BnetLang >     unmodLangSet;
	
	/** Realms of the region. */
	public final Realm[]             realms;
	
	/** An unmodifiable list of the realms of the region. */
	public final List< Realm >       realmList;
	
	/** Icon for tables. */
	public final TableIcon< Region > tableIcon;
	
	
	/**
	 * Creates a new {@link Region}.
	 * 
	 * @param text text value
	 * @param code region code
	 * @param regionId region id
	 * @param ricon ricon of the region
	 * @param depotServerUrl Depot server URL of the region
	 * @param bnetUrl URL of the region's battle.net web site
	 * @param defaultLang default language ({@link BnetLang}) on the region's web page
	 * @param langSet available languages ({@link BnetLang}) on the region's web page
	 * @param realms realms of the region
	 */
	private Region( final String text, final String code, final int regionId, final LRIcon ricon, final String depotServerUrl, final String bnetUrl,
	        final BnetLang defaultLang, final EnumSet< BnetLang > langSet, final Realm... realms ) {
		this.text = text;
		this.code = code;
		this.regionId = regionId;
		this.ricon = ricon;
		this.depotServerUrl = Utils.createUrl( depotServerUrl );
		this.bnetUrl = Utils.createUrl( bnetUrl );
		this.defaultLang = defaultLang;
		this.langSet = langSet;
		unmodLangSet = Collections.unmodifiableSet( langSet );
		this.realms = realms;
		realmList = Collections.unmodifiableList( Arrays.asList( realms ) );
		
		tableIcon = new TableIcon<>( this );
	}
	
	@Override
	public String getCode() {
		return code;
	}
	
	@Override
	public int getRegionId() {
		return regionId;
	}
	
	@Override
	public URL getDepotServerUrl() {
		return depotServerUrl;
	}
	
	@Override
	public URL getBnetUrl() {
		return bnetUrl;
	}
	
	@Override
	public BnetLang getDefaultLang() {
		return defaultLang;
	}
	
	@Override
	public Realm getRealm( int realmId ) {
		return --realmId < realms.length ? realms[ realmId ] : Realm.UNKNOWN;
	}
	
	@Override
	public Set< ? extends IBnetLang > getLangSet() {
		return unmodLangSet;
	}
	
	@Override
	public List< ? extends IRealm > getRealmList() {
		return realmList;
	}
	
	@Override
	@SuppressWarnings( "unchecked" )
	public TableIcon< Region > getTableIcon() {
		return tableIcon;
	}
	
	@Override
	public String toString() {
		return text;
	}
	
	@Override
	public LRIcon getRicon() {
		return ricon;
	}
	
	
	/**
	 * Returns the region associated with the specified code.
	 * 
	 * @param code region code to return the region for
	 * @return the region associated with the specified code; or {@link #UNKNOWN} if no region is found for the specified code
	 */
	public static Region fromCode( final String code ) {
		for ( final Region r : VALUES )
			if ( r.code.equals( code ) )
				return r;
		
		return UNKNOWN;
	}
	
	/**
	 * Returns the region associated with the specified region id.
	 * 
	 * @param regionId region id to return the region for
	 * @return the region associated with the specified region id; or {@link #UNKNOWN} if no region is found for the specified region id
	 */
	public static Region fromRegionId( final int regionId ) {
		for ( final Region r : VALUES )
			if ( r.regionId == regionId )
				return r;
		
		return UNKNOWN;
	}
	
	/**
	 * Returns an HTML table summarizing the supported languages at each region, the default being underlined.
	 * 
	 * @return an HTML table summarizing the supported languages at each region, the default being underlined
	 */
	public static String getLanguageSupportTable() {
		final StringBuilder sb = new StringBuilder( 512 );
		
		sb.append( "<table border=1 cellspacing=0 cellpadding=3>" );
		sb.append( "<tr><th>Region<th align=left>Supported Languages" );
		for ( final Region r : VALUES ) {
			if ( r == PUBLIC_TEST || r == UNKNOWN )
				continue;
			sb.append( "<tr><td>" ).append( r.text );
			sb.append( "<td><u>" ).append( r.defaultLang.text ).append( "</u>" );
			for ( final BnetLang bl : r.langSet )
				if ( bl != r.defaultLang )
					sb.append( ", " ).append( bl.text );
		}
		sb.append( "</table>" );
		
		return sb.toString();
	}
	
	
	/** Ricon representing this entity. */
	public static final LRIcon   RICON  = Icons.F_GLOBE_GREEN;
	
	/** Cache of the values array. */
	public static final Region[] VALUES = values();
	
}
