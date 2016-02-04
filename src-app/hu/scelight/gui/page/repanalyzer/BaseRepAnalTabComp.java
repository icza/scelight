/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.gui.page.repanalyzer;

import hu.scelight.sc2.rep.repproc.RepProcessor;
import hu.sllauncher.gui.comp.BorderPanel;

/**
 * Base of components of the Replay Analyzer.
 * 
 * <p>
 * Mostly used for tabs, but the Events table is also uses this.
 * </p>
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class BaseRepAnalTabComp extends BorderPanel {
	
	/** Replay processor. */
	protected final RepProcessor repProc;
	
	/**
	 * Creates a new {@link BaseRepAnalTabComp}.
	 * 
	 * @param repProc replay processor
	 */
	public BaseRepAnalTabComp( final RepProcessor repProc ) {
		this.repProc = repProc;
	}
	
}
