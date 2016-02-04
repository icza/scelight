/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.bean.repfolders;

import hu.scelight.gui.icon.Icons;
import hu.scelight.gui.icon.RIcon;
import hu.scelight.util.gui.TableIcon;
import hu.scelightapibase.util.gui.HasRIcon;
import hu.sllauncher.gui.icon.LRIcon;

/**
 * Replay folder origin.
 * 
 * @author Andras Belicza
 */
public enum RepFolderOrigin implements HasRIcon {
	
	/** Replay backup folder. */
	REP_BACKUP_FOLDER( Icons.F_BLUE_FOLDER_BOOKMARK ),
	
	/** Default SC2 replay folder. */
	DEF_SC2_REP_FOLDER( Icons.F_BLUE_FOLDER_BOOKMARK ),
	
	/** Folders added by the user. */
	USER( Icons.F_BLUE_FOLDER );
	
	
	/** RIcon of the origin. */
	public final RIcon                        ricon;
	
	/** Icon for tables. */
	public final TableIcon< RepFolderOrigin > tableIcon;
	
	
	/**
	 * Creates a new {@link RepFolderOrigin}.
	 * 
	 * @param ricon ricon of the origin
	 */
	private RepFolderOrigin( final RIcon ricon ) {
		this.ricon = ricon;
		tableIcon = new TableIcon<>( this );
	}
	
	@Override
	public LRIcon getRicon() {
		return ricon;
	}
	
}
