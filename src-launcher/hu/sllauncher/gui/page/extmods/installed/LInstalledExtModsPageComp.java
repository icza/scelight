/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllauncher.gui.page.extmods.installed;

import hu.scelightapibase.gui.comp.multipage.IPageSelectedListener;
import hu.sllauncher.action.XAction;
import hu.sllauncher.bean.module.ExtModManifestBean;
import hu.sllauncher.bean.person.PersonBean;
import hu.sllauncher.gui.comp.BorderPanel;
import hu.sllauncher.gui.comp.Browser;
import hu.sllauncher.gui.comp.HelpIcon;
import hu.sllauncher.gui.comp.ToolBarForTable;
import hu.sllauncher.gui.comp.XLabel;
import hu.sllauncher.gui.comp.XScrollPane;
import hu.sllauncher.gui.comp.XSplitPane;
import hu.sllauncher.gui.comp.XToolBar;
import hu.sllauncher.gui.comp.table.XTable;
import hu.sllauncher.gui.help.LHelps;
import hu.sllauncher.gui.icon.LIcons;
import hu.sllauncher.service.env.LEnv;
import hu.sllauncher.service.settings.LSettings;
import hu.sllauncher.util.LUtils;
import hu.sllauncher.util.gui.LGuiUtils;
import hu.sllauncher.util.gui.adapter.ListSelectionAdapter;

import java.awt.event.ActionEvent;
import java.nio.file.Path;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JSplitPane;
import javax.swing.event.ListSelectionEvent;

/**
 * Page component to view and configure installed external modules.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class LInstalledExtModsPageComp extends BorderPanel implements IPageSelectedListener {
	
	/** Refreshes the list of installed external modules. */
	private final XAction refreshAction = new XAction( LIcons.F_ARROW_CIRCLE_315, "Refresh (Re-read Installed Modules)", this ) {
		                                    @Override
		                                    public void actionPerformed( final ActionEvent event ) {
			                                    rebuildTable();
		                                    }
	                                    };
	
	/** Delete selected modules. */
	private final XAction enableAction  = new XAction( LIcons.F_TICK, "Enable Selected Modules", this ) {
		                                    @Override
		                                    public void actionPerformed( final ActionEvent event ) {
			                                    changeSelectedModsEnabled( Boolean.TRUE );
		                                    }
	                                    };
	
	/** Delete selected modules. */
	private final XAction disableAction = new XAction( LIcons.F_CROSS_OCTAGON, "Disable Selected Modules", this ) {
		                                    @Override
		                                    public void actionPerformed( final ActionEvent event ) {
			                                    changeSelectedModsEnabled( Boolean.FALSE );
		                                    }
	                                    };
	
	/** Delete selected modules. */
	private final XAction deleteAction  = new XAction( LIcons.F_CROSS, "Delete / Uninstall Selected Modules... (Delete)", this ) {
		                                    @Override
		                                    public void actionPerformed( final ActionEvent event ) {
			                                    final int[] rows = table.getSelectedModelRows();
			                                    if ( !LGuiUtils.confirm( LUtils.plural( "Are you sure you want to delete %s Installed External Module%s?",
			                                            rows.length ) ) )
				                                    return;
			                                    
			                                    for ( final int row : rows ) {
				                                    final ExtModManifestBean mf = (ExtModManifestBean) table.getModel().getValueAt( row, manifestBeanColIdx );
				                                    if ( !checkExtModuleOnDelete( mf ) )
					                                    continue;
				                                    final Path path = LEnv.PATH_EXT_MODS.resolve( mf.getFolder() );
				                                    LEnv.LOGGER.info( "Deleting installed external module " + mf.getName() + ": " + path );
				                                    while ( !LUtils.deletePath( path ) ) {
					                                    if ( !LGuiUtils.askRetry( "Could not delete folder:", path ) )
						                                    break;
				                                    }
			                                    }
			                                    
			                                    rebuildTable();
		                                    }
	                                    };
	
	
	/**
	 * Checks if the installed external module specified by its manifest can be deleted.
	 * 
	 * <p>
	 * This implementation always returns <code>true</code>, it is intended to provide customization for subclasses.
	 * </p>
	 * 
	 * @param manifest manifest of the external module to be checked
	 * @return true if the module can be deleted; false otherwise
	 */
	protected boolean checkExtModuleOnDelete( final ExtModManifestBean manifest ) {
		return true;
	}
	
	/**
	 * Changes the enabled property of the selected installed external modules.
	 * 
	 * @param enabled enabled property to be set
	 */
	private void changeSelectedModsEnabled( final Boolean enabled ) {
		final InstExtModConfsBean confsBean = LEnv.LAUNCHER_SETTINGS.get( LSettings.INST_EXT_MOD_CONFS ).cloneBean();
		
		for ( final int row : table.getSelectedModelRows() )
			confsBean.getModuleConfForFolder( ( (ExtModManifestBean) table.getModel().getValueAt( row, manifestBeanColIdx ) ).getFolder(), true ).setEnabled(
			        enabled );
		
		LEnv.LAUNCHER_SETTINGS.set( LSettings.INST_EXT_MOD_CONFS, confsBean );
		
		rebuildTable();
	}
	
	
	
	/** Table displaying the installed external modules. */
	private final XTable table = new XTable();
	
	/**
	 * Creates a new {@link LInstalledExtModsPageComp}.
	 */
	public LInstalledExtModsPageComp() {
		buildGui();
	}
	
	/**
	 * Builds the GUI of the page.
	 */
	private void buildGui() {
		final Box toolBarsBox = Box.createVerticalBox();
		addNorth( toolBarsBox );
		
		final XToolBar infoBar = new XToolBar();
		toolBarsBox.add( infoBar );
		infoBar.add( new XLabel( "<html>On this page you can view and manage the <b>Installed</b> external modules.</html>" ).verticalBorder( 5 ) );
		if ( LEnv.APP_STARTED.get() )
			infoBar.add( new XLabel( "<html><b><font color='red'>Restart is required when changes are made!</font></b></html>" ).leftBorder( 4 ) );
		infoBar.finalizeLayout();
		
		final ToolBarForTable toolBar = new ToolBarForTable( table );
		toolBarsBox.add( toolBar );
		toolBar.add( refreshAction );
		toolBar.addSeparator();
		toolBar.addSelectInfoLabel( "Select a Module." );
		toolBar.addSelEnabledButton( enableAction );
		toolBar.addSelEnabledButton( disableAction );
		toolBar.addSeparator();
		toolBar.addSelEnabledButton( deleteAction );
		toolBar.addSeparator();
		toolBar.add( new XLabel( LHelps.EXTERNAL_MODULES.title ).leftBorder( 20 ) );
		toolBar.add( new HelpIcon( LHelps.EXTERNAL_MODULES ).leftBorder( 2 ) );
		toolBar.addSeparator();
		toolBar.add( new XLabel( LHelps.INSTALLED_EXTERNAL_MODULES.title ) );
		toolBar.add( new HelpIcon( LHelps.INSTALLED_EXTERNAL_MODULES ).leftBorder( 2 ) );
		toolBar.finalizeLayout();
		
		final XSplitPane splitPane = new XSplitPane( JSplitPane.VERTICAL_SPLIT );
		splitPane.setResizeWeight( 0.5 );
		table.setDeleteAction( deleteAction );
		splitPane.setTopComponent( table.createWrapperBox( true ) );
		final Browser descBrowser = new Browser();
		final XScrollPane browserScrollPane = new XScrollPane( descBrowser );
		splitPane.setBottomComponent( browserScrollPane );
		addCenter( splitPane );
		
		table.getSelectionModel().addListSelectionListener( new ListSelectionAdapter( true ) {
			@Override
			public void valueChanged( final ListSelectionEvent event ) {
				if ( event != null && event.getValueIsAdjusting() )
					return;
				
				final int selRow = table.getSelectedRow();
				if ( selRow >= 0 ) {
					final ExtModManifestBean mf = (ExtModManifestBean) table.getModel().getValueAt( selRow, manifestBeanColIdx );
					browserScrollPane.setBorder( BorderFactory.createTitledBorder( "Module description: (" + mf.getName() + ")" ) );
					descBrowser.setText( mf.getDescription() );
				} else {
					browserScrollPane.setBorder( BorderFactory.createTitledBorder( "Module description:" ) );
					descBrowser.setText( null );
				}
			}
		} );
		
		// Installed ext module list will be refreshed when page is selected.
	}
	
	
	/** Folder column model index. */
	private int folderColIdx;
	
	/** Ext mod manifest bean column model index. */
	private int manifestBeanColIdx;
	
	/**
	 * Rebuilds the table.
	 */
	private void rebuildTable() {
		table.saveSelection( folderColIdx );
		
		final Vector< Vector< Object > > data = new Vector<>();
		
		final InstExtModConfsBean instExtModConfsBean = LEnv.LAUNCHER_SETTINGS.get( LSettings.INST_EXT_MOD_CONFS );
		
		for ( final ExtModManifestBean mf : InstExtModsUtil.detectInstalledExtMods() ) {
			final InstExtModConfBean conf = instExtModConfsBean.getModuleConfForFolder( mf.getFolder() );
			
			final StringBuilder authorsBuilder = new StringBuilder();
			for ( final PersonBean author : mf.getAuthorList() ) {
				if ( authorsBuilder.length() > 0 )
					authorsBuilder.append( ", " );
				authorsBuilder.append( LEnv.LANG.formatPersonName( author.getPersonName() ) );
			}
			
			data.add( LUtils.vector( mf.getFolder(), mf, mf.getIcon(), mf.getName(), conf == null ? Boolean.FALSE : conf.getEnabled(), mf.getVersion(),
			        LEnv.LANG.formatDate( mf.getBuildInfo().getDate() ), authorsBuilder.toString(), LUtils.tryMakingUrl( mf.getHomePage() ), mf.getShortDesc() ) );
		}
		
		table.getXTableModel()
		        .setDataVector(
		                data,
		                LUtils.vector( "Folder", "ExtModManifestBean", "I", "Name", "Enabled?", "Version", "Release date", "Author", "Home page",
		                        "Short description" ) );
		
		folderColIdx = 0;
		manifestBeanColIdx = 1;
		final int enabledColIdx = 4;
		final int versionColIdx = 5;
		final int dateColIdx = 6;
		table.getXTableRowSorter().setColumnDefaultDescs( true, enabledColIdx, versionColIdx, dateColIdx );
		// Remove columns in descending index
		table.getColumnModel().removeColumn( table.getColumnModel().getColumn( manifestBeanColIdx ) );
		table.getColumnModel().removeColumn( table.getColumnModel().getColumn( folderColIdx ) );
		table.packColumnsExceptLast();
		
		table.restoreSelection( folderColIdx );
	}
	
	@Override
	public void pageSelected() {
		// There might have been new ext modules installed since last selected, so rebuild the table.
		rebuildTable();
	}
	
}
