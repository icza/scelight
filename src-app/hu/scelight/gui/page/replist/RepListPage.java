/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.gui.page.replist;

import hu.scelight.bean.repfolders.RepFolderBean;
import hu.scelight.gui.icon.Icons;
import hu.scelightapi.bean.repfilters.IRepFiltersBean;
import hu.sllauncher.gui.comp.multipage.BasePage;

import java.nio.file.Path;

/**
 * Replay list page.
 * 
 * @author Andras Belicza
 */
public class RepListPage extends BasePage< RepListComp > {
	
	/** Replay folder bean to list. */
	private final RepFolderBean   repFolderBean;
	
	/** Replay files to list. */
	private final Path[]          repFiles;
	
	/** Replay filters to be used. */
	private final IRepFiltersBean repFiltersBean;
	
	
	/**
	 * Creates a new {@link RepListPage}.
	 * 
	 * @param repFolderBean replay folder bean to list
	 */
	public RepListPage( final RepFolderBean repFolderBean ) {
		this( repFolderBean, repFolderBean.getRepFiltersBean() );
	}
	
	/**
	 * Creates a new {@link RepListPage}.
	 * 
	 * @param repFolderBean replay folder bean to list
	 * @param repFiltersBean replay filters to be used
	 */
	public RepListPage( final RepFolderBean repFolderBean, final IRepFiltersBean repFiltersBean ) {
		this( repFolderBean.getPath().toString(), repFolderBean, null, repFiltersBean );
	}
	
	/**
	 * Creates a new {@link RepListPage}.
	 * 
	 * @param displayName display name of the page
	 * @param repFiles replay files to list
	 */
	public RepListPage( final String displayName, final Path[] repFiles ) {
		this( displayName, repFiles, null );
	}
	
	/**
	 * Creates a new {@link RepListPage}.
	 * 
	 * @param displayName display name of the page
	 * @param repFiles replay files to list
	 * @param repFiltersBean replay filters to be used
	 */
	public RepListPage( final String displayName, final Path[] repFiles, final IRepFiltersBean repFiltersBean ) {
		this( displayName, null, repFiles, repFiltersBean );
	}
	
	/**
	 * Creates a new {@link RepListPage}.
	 * 
	 * @param displayName display name of the page
	 * @param repFolderBean replay folder bean to list
	 * @param repFiles replay files to list
	 * @param repFiltersBean replay filters to be used
	 */
	private RepListPage( final String displayName, final RepFolderBean repFolderBean, final Path[] repFiles, final IRepFiltersBean repFiltersBean ) {
		super( displayName, Icons.F_BLUE_FOLDER_OPEN_TABLE, true );
		
		this.repFolderBean = repFolderBean;
		this.repFiles = repFiles;
		this.repFiltersBean = repFiltersBean;
	}
	
	@Override
	public RepListComp createPageComp() {
		return repFolderBean == null ? new RepListComp( displayName, repFiles, repFiltersBean ) : new RepListComp( repFolderBean, repFiltersBean );
	}
	
}
