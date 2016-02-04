/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelightapi.sc2.rep.model.details;

import hu.scelight.sc2.rep.model.details.Realm;
import hu.scelightapi.sc2.rep.model.initdata.gamedesc.IRegion;
import hu.scelightapibase.util.IEnum;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * SC2 realm (sub-region).
 * 
 * @author Andras Belicza
 * 
 * @see IRegion
 * @see IEnum
 */
public interface IRealm extends IEnum {
	
	/** North America. */
	IRealm         NORTH_AMERICA = Realm.NORTH_AMERICA;
	
	/** Latin America. */
	IRealm         LATIN_AMERICA = Realm.LATIN_AMERICA;
	
	/** China. */
	IRealm         CHINA         = Realm.CHINA;
	
	/** Europe. */
	IRealm         EUROPE        = Realm.EUROPE;
	
	/** Russia. */
	IRealm         RUSSIA        = Realm.RUSSIA;
	
	/** Korea. */
	IRealm         KOREA         = Realm.KOREA;
	
	/** Taiwan. */
	IRealm         TAIWAN        = Realm.TAIWAN;
	
	/** South-East Asia. */
	IRealm         SEA           = Realm.SEA;
	
	/** Unknown. */
	IRealm         UNKNOWN       = Realm.UNKNOWN;
	
	
	/** An unmodifiable list of all the realms. */
	List< IRealm > VALUE_LIST    = Collections.unmodifiableList( Arrays.< IRealm > asList( Realm.VALUES ) );
	
}
