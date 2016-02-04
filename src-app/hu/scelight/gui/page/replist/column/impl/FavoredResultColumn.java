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
import hu.scelight.sc2.rep.model.details.Result;
import hu.scelight.sc2.rep.model.details.Toon;
import hu.scelight.sc2.rep.repproc.RepProcessor;
import hu.scelight.sc2.rep.repproc.User;
import hu.scelight.util.gui.TableIcon;

/**
 * Match result of favored players column.
 * 
 * @author Andras Belicza
 */
public class FavoredResultColumn extends BaseColumn< TableIcon< Result > > {
	
	/**
	 * Creates a new {@link FavoredResultColumn}.
	 */
	@SuppressWarnings( "unchecked" )
	public FavoredResultColumn() {
		super( "FRes", Result.RICON, "Match result of the first found player from your Favored Player List.",
		        (Class< TableIcon< Result > >) (Object) TableIcon.class, false );
	}
	
	@Override
	public TableIcon< Result > getData( final RepProcessor repProc ) {
		
		for ( final Toon toon : RepProcessor.favoredToonList.get() )
			for ( final User u : repProc.playerUsers )
				if ( u.player.getToon().equals( toon ) )
					return u.player.getResult().tableIcon;
		
		return Result.UNKNOWN.tableIcon;
	}
	
}
