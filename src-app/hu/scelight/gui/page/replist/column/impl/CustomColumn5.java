/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.gui.page.replist.column.impl;

import hu.scelight.gui.page.replist.column.Dependent;
import hu.scelight.service.env.Env;
import hu.scelight.service.settings.Settings;

/**
 * Custom column #5.
 * 
 * @author Andras Belicza
 */
@Dependent
public class CustomColumn5 extends BaseCustomColumn {
	
	/**
	 * Creates a new {@link CustomColumn5}.
	 */
	public CustomColumn5() {
		super( 5, Env.APP_SETTINGS.get( Settings.REP_LIST_CUST_COL_5_NAME ), Env.APP_SETTINGS.get( Settings.REP_LIST_CUST_COL_5_TEMPLATE ) );
	}
	
}
