/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.slsc2balancedata;

import hu.sllauncher.bean.VersionBean;

/**
 * Constants of the SC2 balance data. These are real constants, can be inlined.
 * 
 * <p>
 * <i>Implementation note:</i> This is an interface instead of a class because constants defined in an interface are implicitly <code>public</code>,
 * <code>static</code> and <code>final</code>, and so these keywords can be omitted!
 * </p>
 * 
 * @author Andras Belicza
 */
public interface BConsts {
	
	/** SC2 Balance Data name. */
	String      SC2_BALANCE_DATA_NAME    = "SC2 Balance Data";
	
	/** SC2 Balance Data version. */
	VersionBean SC2_BALANCE_DATA_VERSION = new VersionBean( 1, 5 );
	
}
