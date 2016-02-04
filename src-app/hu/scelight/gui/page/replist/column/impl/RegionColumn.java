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

import hu.scelight.gui.page.replist.column.BaseColumn;
import hu.scelight.sc2.rep.model.initdata.gamedesc.Region;
import hu.scelight.sc2.rep.repproc.RepProcessor;
import hu.scelight.util.gui.TableIcon;

/**
 * Region column.
 * 
 * @author Andras Belicza
 */
public class RegionColumn extends BaseColumn< TableIcon< Region > > {
	
	/**
	 * Creates a new {@link RegionColumn}.
	 */
	@SuppressWarnings( "unchecked" )
	public RegionColumn() {
		super( "Reg", Region.RICON, "Region", (Class< TableIcon< Region > >) (Object) TableIcon.class, false );
	}
	
	@Override
	public TableIcon< Region > getData( final RepProcessor repProc ) {
		return repProc.replay.initData.getGameDescription().getRegion().tableIcon;
	}
	
}
