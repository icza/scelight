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

import hu.scelight.gui.icon.Icons;
import hu.scelight.gui.page.replist.column.BaseColumn;
import hu.scelight.gui.page.replist.column.Dependent;
import hu.scelight.sc2.map.cache.MapImageCache;
import hu.scelight.sc2.rep.repproc.RepProcessor;
import hu.scelight.service.env.Env;
import hu.scelight.service.settings.Settings;
import hu.sllauncher.gui.icon.LRIcon;

import javax.swing.Icon;

/**
 * Map image column.
 * 
 * @author Andras Belicza
 */
@Dependent
public class MapImageColumn extends BaseColumn< Icon > {
	
	/** Cached row height. */
	private final int rowHeight;
	
	/** Map image height. */
	private final int imageHeight;
	
	/**
	 * Creates a new {@link MapImageColumn}.
	 */
	public MapImageColumn() {
		super( "Map", Icons.F_MAP, "Map image", Icon.class, true );
		
		rowHeight = Env.APP_SETTINGS.get( Settings.REP_LIST_ROW_HEIGHT );
		
		imageHeight = rowHeight * Env.APP_SETTINGS.get( Settings.REP_LIST_MAP_IMAGE_ZOOM ) / 100;
	}
	
	@Override
	public Icon getData( final RepProcessor repProc ) {
		final LRIcon ricon = MapImageCache.getMapImage( repProc );
		
		return ricon == null ? Icons.F_HOURGLASS.size( rowHeight ) : ricon.size( imageHeight );
	}
	
}
