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

import hu.belicza.andras.util.VersionView;
import hu.scelight.gui.icon.Icons;
import hu.scelight.gui.page.replist.column.BaseColumn;
import hu.scelight.sc2.rep.repproc.RepProcessor;

/**
 * Replay full version column.
 * 
 * @author Andras Belicza
 */
public class FullVersionColumn extends BaseColumn< VersionView > {
	
	/**
	 * Creates a new {@link FullVersionColumn}.
	 */
	public FullVersionColumn() {
		super( "Full Version", Icons.F_DOCUMENT_ATTRIBUTE_V, "Replay full version, build number included", VersionView.class, true );
	}
	
	@Override
	public VersionView getData( final RepProcessor repProc ) {
		return new VersionView( repProc.replay.header.major, repProc.replay.header.minor, repProc.replay.header.revision, repProc.replay.header.build );
	}
	
}
