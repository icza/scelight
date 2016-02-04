/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.gui.page.multirepanalyzer;

import hu.scelight.bean.repfolders.RepFolderBean;
import hu.scelight.gui.icon.Icons;
import hu.scelight.util.Utils;
import hu.scelightapi.bean.repfilters.IRepFiltersBean;
import hu.sllauncher.gui.comp.multipage.BasePage;

import java.nio.file.Path;

/**
 * Multi-Replay Analyzer page.
 * 
 * @author Andras Belicza
 */
public class MultiRepAnalyzerPage extends BasePage< MultiRepAnalyzerComp > {
	
	/** Replay folder bean to analyze. */
	private final RepFolderBean   repFolderBean;
	
	/** Replay files to analyze. */
	private final Path[]          repFiles;
	
	/** Replay filters to be used. */
	private final IRepFiltersBean repFiltersBean;
	
	
	/**
	 * Creates a new {@link MultiRepAnalyzerPage}.
	 * 
	 * @param repFolderBean replay folder bean to analyze
	 */
	public MultiRepAnalyzerPage( final RepFolderBean repFolderBean ) {
		this( repFolderBean, repFolderBean.getRepFiltersBean() );
	}
	
	/**
	 * Creates a new {@link MultiRepAnalyzerPage}.
	 * 
	 * @param repFolderBean replay folder bean to analyze
	 * @param repFiltersBean replay filters to be used
	 */
	public MultiRepAnalyzerPage( final RepFolderBean repFolderBean, final IRepFiltersBean repFiltersBean ) {
		this( repFolderBean, null, repFiltersBean );
	}
	
	/**
	 * Creates a new {@link MultiRepAnalyzerPage}.
	 * 
	 * @param repFiles replay files to analyze
	 */
	public MultiRepAnalyzerPage( final Path[] repFiles ) {
		this( repFiles, null );
	}
	
	/**
	 * Creates a new {@link MultiRepAnalyzerPage}.
	 * 
	 * @param repFiles replay files to analyze
	 * @param repFiltersBean replay filters to be used
	 */
	public MultiRepAnalyzerPage( final Path[] repFiles, final IRepFiltersBean repFiltersBean ) {
		this( null, repFiles, repFiltersBean );
	}
	
	/**
	 * Creates a new {@link MultiRepAnalyzerPage}.
	 * 
	 * @param repFolderBean replay folder bean to analyze
	 * @param repFiles replay files to analyze
	 * @param repFiltersBean replay filters to be used
	 */
	private MultiRepAnalyzerPage( final RepFolderBean repFolderBean, final Path[] repFiles, final IRepFiltersBean repFiltersBean ) {
		super( repFolderBean == null ? Utils.plural( "%s replay%s", repFiles.length ) : repFolderBean.getPath().toString(), Icons.F_CHART_UP_COLOR, true );
		
		this.repFolderBean = repFolderBean;
		this.repFiles = repFiles;
		this.repFiltersBean = repFiltersBean;
	}
	
	@Override
	public MultiRepAnalyzerComp createPageComp() {
		return repFolderBean == null ? new MultiRepAnalyzerComp( repFiles, repFiltersBean ) : new MultiRepAnalyzerComp( repFolderBean, repFiltersBean );
	}
	
}
