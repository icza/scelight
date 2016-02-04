/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllauncher.gui.page.logs;

import hu.sllauncher.gui.comp.multipage.BasePage;
import hu.sllauncher.gui.icon.LIcons;

/**
 * Logs page.
 * 
 * @author Andras Belicza
 */
public class LLogsPage extends BasePage< LLogsPageComp > {
	
	/**
	 * Creates a new {@link LLogsPage}.
	 */
	public LLogsPage() {
		super( "Logs", LIcons.F_REPORT_EXCLAMATION );
	}
	
	@Override
	public LLogsPageComp createPageComp() {
		return new LLogsPageComp();
	}
	
}
