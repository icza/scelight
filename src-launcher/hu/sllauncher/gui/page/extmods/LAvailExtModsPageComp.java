/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllauncher.gui.page.extmods;

import hu.scelightapibase.gui.comp.multipage.IPageDisposedListener;
import hu.sllauncher.ScelightLauncher;
import hu.sllauncher.action.XAction;
import hu.sllauncher.bean.module.ExtModRefBean;
import hu.sllauncher.bean.module.ModulesBean;
import hu.sllauncher.bean.person.PersonBean;
import hu.sllauncher.gui.comp.BorderPanel;
import hu.sllauncher.gui.comp.HelpIcon;
import hu.sllauncher.gui.comp.ToolBarForTable;
import hu.sllauncher.gui.comp.XLabel;
import hu.sllauncher.gui.comp.XToolBar;
import hu.sllauncher.gui.comp.table.XTable;
import hu.sllauncher.gui.help.LHelps;
import hu.sllauncher.gui.icon.LIcons;
import hu.sllauncher.service.env.LEnv;
import hu.sllauncher.service.settings.LSettings;
import hu.sllauncher.util.LUtils;
import hu.sllauncher.util.gui.adapter.ActionAdapter;

import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.SwingConstants;
import javax.swing.Timer;

/**
 * Page component to view and and install available (official) external modules.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class LAvailExtModsPageComp extends BorderPanel implements IPageDisposedListener {
	
	/** Install / Auto-update Selected Modules. */
	private final XAction enableUpdateAction  = new XAction( LIcons.F_DRIVE_DOWNLOAD, "Install / Auto-update Selected Modules", this ) {
		                                          @Override
		                                          public void actionPerformed( final ActionEvent event ) {
			                                          changeSelectedModsAutoUpdate( Boolean.TRUE );
		                                          }
	                                          };
	
	/** Do Not Auto-update Selected Modules. */
	private final XAction disableUpdateAction = new XAction( LIcons.F_CROSS_OCTAGON, "Disable Auto-update For The Selected Modules", this ) {
		                                          @Override
		                                          public void actionPerformed( final ActionEvent event ) {
			                                          changeSelectedModsAutoUpdate( Boolean.FALSE );
		                                          }
	                                          };
	
	
	/**
	 * Changes the auto-update property of the selected official external modules.
	 * 
	 * @param autoUpdate auto-update property to be set
	 */
	private void changeSelectedModsAutoUpdate( final Boolean autoUpdate ) {
		final OffExtModConfsBean confsBean = LEnv.LAUNCHER_SETTINGS.get( LSettings.OFF_EXT_MOD_CONFS ).cloneBean();
		
		for ( final int row : table.getSelectedModelRows() )
			confsBean.getModuleConfForFolder( ( (ExtModRefBean) table.getModel().getValueAt( row, extModRefColIdx ) ).getFolder(), true ).setAutoUpdate(
			        autoUpdate );
		
		LEnv.LAUNCHER_SETTINGS.set( LSettings.OFF_EXT_MOD_CONFS, confsBean );
		
		rebuildTable();
	}
	
	
	
	/** Wrapper component for the wait info components. */
	private final JComponent waitInfoWrapper = new BorderPanel( new XLabel( "Retrieving official external module list... Please wait...",
	                                                 LIcons.F_HOURGLASS.get(), SwingConstants.CENTER ) );
	
	/** Table displaying the available official external modules. */
	private final XTable     table           = new XTable();
	
	/** Wrapper box for the chat table. */
	private final JComponent tableWrapperBox = table.createWrapperBox( true );
	
	/**
	 * Creates a new {@link LAvailExtModsPageComp}.
	 */
	public LAvailExtModsPageComp() {
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
		infoBar.add( new XLabel( "<html>On this page you can view and install the available <b>Official</b> external modules."
		        + " <b><font color='red'>Restart is required when changes are made!</font></b></html>" ).verticalBorder( 5 ) );
		infoBar.finalizeLayout();
		
		final ToolBarForTable toolBar = new ToolBarForTable( table );
		toolBarsBox.add( toolBar );
		toolBar.addSelectInfoLabel( "Select a Module." );
		toolBar.addSelEnabledButton( enableUpdateAction );
		toolBar.addSeparator();
		toolBar.addSelEnabledButton( disableUpdateAction );
		toolBar.addSeparator();
		toolBar.add( new XLabel( LHelps.EXTERNAL_MODULES.title ).leftBorder( 20 ) );
		toolBar.add( new HelpIcon( LHelps.EXTERNAL_MODULES ).leftBorder( 2 ) );
		toolBar.addSeparator();
		toolBar.add( new XLabel( LHelps.OFFICIAL_EXTERNAL_MODULES.title ) );
		toolBar.add( new HelpIcon( LHelps.OFFICIAL_EXTERNAL_MODULES ).leftBorder( 2 ) );
		toolBar.finalizeLayout();
		
		final Box viewsBox = Box.createVerticalBox();
		viewsBox.add( waitInfoWrapper );
		if ( LEnv.APP_STARTED.get() )
			waitInfoWrapper.setVisible( false ); // App started, we surely have what we need
		else
			tableWrapperBox.setVisible( false );
		viewsBox.add( tableWrapperBox );
		addCenter( viewsBox );
		
		rebuildTable();
	}
	
	
	/** ExtModRef bean column model index. */
	private int   extModRefColIdx;
	
	/** Timer to recheck whether modules bean has been retrieved (and therefore rebuild is required). */
	private Timer recheckTimer;
	
	/**
	 * Rebuilds the table.
	 */
	private void rebuildTable() {
		table.saveSelection( extModRefColIdx );
		
		final ModulesBean modules = ScelightLauncher.INSTANCE().getModules();
		
		// Only build table if modules bean has been retrieved.
		if ( waitInfoWrapper.isVisible() ) {
			if ( modules == null ) {
				if ( recheckTimer == null ) {
					// Start timer to periodically check if modules been has been retrieved
					recheckTimer = new Timer( 250, new ActionAdapter() {
						@Override
						public void actionPerformed( final ActionEvent event ) {
							rebuildTable();
						}
					} );
					recheckTimer.start();
				}
				return;
			} else {
				if ( recheckTimer != null )
					recheckTimer.stop();
				waitInfoWrapper.setVisible( false );
				tableWrapperBox.setVisible( true );
			}
		}
		
		final Vector< Vector< Object > > data = new Vector<>();
		
		final OffExtModConfsBean offExtModConfsBean = LEnv.LAUNCHER_SETTINGS.get( LSettings.OFF_EXT_MOD_CONFS );
		if ( modules.getExtModRefList() != null ) // This might be null if no internet connection...
			for ( final ExtModRefBean emr : modules.getExtModRefList() ) {
				final OffExtModConfBean conf = offExtModConfsBean.getModuleConfForFolder( emr.getFolder() );
				
				final StringBuilder authorsBuilder = new StringBuilder();
				for ( final PersonBean author : emr.getAuthorList() ) {
					if ( authorsBuilder.length() > 0 )
						authorsBuilder.append( ", " );
					authorsBuilder.append( LEnv.LANG.formatPersonName( author.getPersonName() ) );
				}
				
				data.add( LUtils.vector( emr, emr.getIcon(), emr.getName(), conf == null ? Boolean.FALSE : conf.getAutoUpdate(), authorsBuilder.toString(),
				        LUtils.tryMakingUrl( emr.getHomePage() ), emr.getShortDesc() ) );
			}
		
		table.getXTableModel().setDataVector( data, LUtils.vector( "ExtModRef", "I", "Name", "Auto-updated?", "Author", "Home page", "Short description" ) );
		
		extModRefColIdx = 0;
		final int autoUpdateColIdx = 3;
		table.getXTableRowSorter().setColumnDefaultDesc( autoUpdateColIdx, true );
		table.getColumnModel().removeColumn( table.getColumnModel().getColumn( extModRefColIdx ) );
		table.packColumnsExceptLast();
		
		table.restoreSelection( extModRefColIdx );
	}
	
	@Override
	public void pageDisposed() {
		if ( recheckTimer != null )
			recheckTimer.stop();
	}
	
}
