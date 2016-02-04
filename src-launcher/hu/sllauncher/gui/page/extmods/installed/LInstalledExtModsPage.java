/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllauncher.gui.page.extmods.installed;

import hu.sllauncher.gui.comp.multipage.BasePage;
import hu.sllauncher.gui.icon.LIcons;

/**
 * Page to view and configure installed external modules.
 * 
 * @author Andras Belicza
 */
public class LInstalledExtModsPage extends BasePage< LInstalledExtModsPageComp > {
	
	/**
	 * Creates a new {@link LInstalledExtModsPage}.
	 */
	public LInstalledExtModsPage() {
		super( "Installed Modules", LIcons.F_CATEGORY_GROUP );
	}
	
	@Override
	public LInstalledExtModsPageComp createPageComp() {
		return new LInstalledExtModsPageComp();
	}
	
}
