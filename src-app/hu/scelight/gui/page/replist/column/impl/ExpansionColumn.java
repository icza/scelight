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
import hu.scelight.sc2.rep.model.initdata.gamedesc.ExpansionLevel;
import hu.scelight.sc2.rep.repproc.RepProcessor;
import hu.scelight.util.gui.TableIcon;

/**
 * Expansion level column.
 * 
 * @author Andras Belicza
 */
public class ExpansionColumn extends BaseColumn< TableIcon< ExpansionLevel > > {
	
	/**
	 * Creates a new {@link ExpansionColumn}.
	 */
	@SuppressWarnings( "unchecked" )
	public ExpansionColumn() {
		super( "Exp", ExpansionLevel.RICON, "Expansion level", (Class< TableIcon< ExpansionLevel > >) (Object) TableIcon.class, false );
	}
	
	@Override
	public TableIcon< ExpansionLevel > getData( final RepProcessor repProc ) {
		return repProc.replay.initData.getGameDescription().getExpansionLevel().tableIcon;
	}
	
}
