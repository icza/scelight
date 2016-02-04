/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.gui.page.about.extmods;

import hu.scelight.gui.page.about.extmods.installed.InstalledExtModsPage;
import hu.sllauncher.gui.page.extmods.LAvailExtModsPage;

/**
 * Page to view and install available external modules.
 * 
 * @author Andras Belicza
 */
public class AvailExtModsPage extends LAvailExtModsPage {
	
	@Override
	protected void addChildPages() {
		addChild( new InstalledExtModsPage() );
	}
	
}
