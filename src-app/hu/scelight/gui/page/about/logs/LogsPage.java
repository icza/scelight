/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.gui.page.about.logs;

import hu.sllauncher.gui.page.logs.LLogsPage;
import hu.sllauncher.gui.page.logs.LLogsPageComp;

/**
 * Improved Logs page.
 * 
 * @author Andras Belicza
 */
public class LogsPage extends LLogsPage {
	
	@Override
	public LLogsPageComp createPageComp() {
		return new LogsComp();
	}
	
}
