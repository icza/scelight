/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllauncher.gui.page.extmods;

import hu.sllauncher.gui.comp.multipage.BasePage;
import hu.sllauncher.gui.icon.LIcons;
import hu.sllauncher.gui.page.extmods.installed.LInstalledExtModsPage;

/**
 * Page to view and install available (official) external modules.
 * 
 * @author Andras Belicza
 */
public class LAvailExtModsPage extends BasePage< LAvailExtModsPageComp > {
	
	/**
	 * Creates a new {@link LAvailExtModsPage}.
	 */
	public LAvailExtModsPage() {
		super( "Available Modules", LIcons.F_CATEGORY );
		
		addChildPages();
	}
	
	/**
	 * Adds the child pages.
	 */
	protected void addChildPages() {
		addChild( new LInstalledExtModsPage() );
	}
	
	@Override
	public LAvailExtModsPageComp createPageComp() {
		return new LAvailExtModsPageComp();
	}
	
}
