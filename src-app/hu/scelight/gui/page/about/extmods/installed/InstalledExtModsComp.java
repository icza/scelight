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

import hu.scelight.Consts;
import hu.scelight.service.env.Env;
import hu.scelight.util.gui.GuiUtils;
import hu.sllauncher.bean.module.ExtModManifestBean;
import hu.sllauncher.gui.page.extmods.installed.LInstalledExtModsPageComp;

/**
 * Improved Page to view and configure installed external modules.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class InstalledExtModsComp extends LInstalledExtModsPageComp {
	
	@Override
	protected boolean checkExtModuleOnDelete( final ExtModManifestBean manifest ) {
		// If a module is started, cannot be deleted
		if ( Env.EXT_MOD_MANAGER.isModuleStarted( manifest.getFolder() ) ) {
			GuiUtils.showWarningMsg( "External module is started therefore cannot be deleted:", manifest.getFolder(), " ", "Restart " + Consts.APP_NAME
			        + " and delete it from the " + Consts.LAUNCHER_NAME + "." );
			return false;
		}
		
		return true;
	}
	
}
