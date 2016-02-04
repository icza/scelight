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

import hu.scelight.gui.icon.Icons;
import hu.scelight.sc2.rep.repproc.RepProcessor;
import hu.scelight.util.Utils;
import hu.sllauncher.gui.comp.multipage.BasePage;

import java.nio.file.Path;

/**
 * Replay analyzer.
 * 
 * @author Andras Belicza
 */
public class RepAnalyzerPage extends BasePage< RepAnalyzerComp > {
	
	/** Replay processor. */
	public final RepProcessor repProc;
	
	/**
	 * Creates a new {@link RepAnalyzerPage}.
	 * 
	 * <p>
	 * If the specified replay file cannot be parsed, the <code>repProc</code> attribute will be null and the replay analyzer page cannot be used. This must
	 * always be checked before proceeding to use the replay analyzer page.
	 * </p>
	 * 
	 * @param file replay file to be analyzed
	 */
	public RepAnalyzerPage( final Path file ) {
		super( Utils.getFileNameWithoutExt( file ), Icons.F_CHART, true );
		
		final RepProcessor repProc = new RepProcessor( file );
		this.repProc = repProc.replay == null ? null : repProc;
	}
	
	@Override
	public RepAnalyzerComp createPageComp() {
		return new RepAnalyzerComp( repProc );
	}
	
}
