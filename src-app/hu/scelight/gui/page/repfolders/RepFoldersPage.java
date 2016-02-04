/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.gui.page.repfolders;

import hu.scelight.gui.icon.Icons;
import hu.sllauncher.gui.comp.multipage.BasePage;

/**
 * Replay folders page.
 * 
 * @author Andras Belicza
 */
public class RepFoldersPage extends BasePage< RepFoldersComp > {
	
	/**
	 * Creates a new {@link RepFoldersPage}.
	 */
	public RepFoldersPage() {
		super( "Replay Folders", Icons.F_BLUE_FOLDERS_STACK );
	}
	
	@Override
	public RepFoldersComp createPageComp() {
		return new RepFoldersComp();
	}
	
}
