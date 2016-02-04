/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.gui.page.about.extmods.installed;

import hu.sllauncher.gui.page.extmods.installed.LInstalledExtModsPage;
import hu.sllauncher.gui.page.extmods.installed.LInstalledExtModsPageComp;

/**
 * Improved Page to view and configure installed external modules.
 * 
 * @author Andras Belicza
 */
public class InstalledExtModsPage extends LInstalledExtModsPage {
	
	@Override
	public LInstalledExtModsPageComp createPageComp() {
		return new InstalledExtModsComp();
	}
	
}
