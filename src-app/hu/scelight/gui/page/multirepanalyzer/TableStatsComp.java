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

import hu.scelight.gui.icon.Icons;
import hu.scelight.gui.page.multirepanalyzer.model.Stats;
import hu.scelight.gui.page.replist.RepListPage;
import hu.scelight.gui.setting.SettingsGui;
import hu.scelight.service.env.Env;
import hu.scelight.service.settings.Settings;
import hu.scelightapibase.bean.settings.ISettingChangeEvent;
import hu.scelightapibase.bean.settings.ISettingChangeListener;
import hu.scelightapibase.gui.comp.multipage.IPage;
import hu.sllauncher.action.XAction;
import hu.sllauncher.gui.comp.BorderPanel;
import hu.sllauncher.gui.comp.ToolBarForTable;
import hu.sllauncher.gui.comp.table.XTable;
import hu.sllauncher.gui.comp.table.XTableModel;
import hu.sllauncher.gui.comp.table.XTableRowSorter;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JTable;

/**
 * Table stats component.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class TableStatsComp extends BorderPanel {
	
	/** List replays of the selected row. */
	private final XAction              listRepsAction            = new XAction( Icons.F_BLUE_FOLDER_OPEN_TABLE,
	                                                                     "List Replays of the Selected Row (Shift+Enter)", this ) {
		                                                             @Override
		                                                             public void actionPerformed( final ActionEvent event ) {
			                                                             int count = Env.APP_SETTINGS.get( Settings.MULTI_REP_MAX_STATS_TO_OPEN_TOGETHER );
			                                                             IPage< ? > firstPage = null;
			                                                             
			                                                             for ( final int row : table.getSelectedModelRows() ) {
				                                                             final Stats stats = (Stats) model.getValueAt( row, statsColIdx );
				                                                             final IPage< ? > page = new RepListPage( displayName + " \u00d7 "
				                                                                     + model.getValueAt( row, objColIdx ), stats.getRepFiles() );
				                                                             if ( firstPage == null )
					                                                             firstPage = page;
				                                                             Env.MAIN_FRAME.getRepFoldersPage().addChild( page );
				                                                             
				                                                             if ( --count <= 0 )
					                                                             break;
			                                                             }
			                                                             
			                                                             if ( firstPage != null ) {
				                                                             Env.MAIN_FRAME.rebuildMainPageTree();
				                                                             Env.MAIN_FRAME.multiPageComp.selectPage( firstPage );
			                                                             }
		                                                             }
	                                                             };
	
	/** Multi-Replay Analyze replays of the selected row. */
	private final XAction              analyzeRepsAction         = new XAction( Icons.F_CHART_UP_COLOR,
	                                                                     "Multi-Replay Analyze Replays of the Selected Row (Ctrl+Enter)", this ) {
		                                                             @Override
		                                                             public void actionPerformed( final ActionEvent event ) {
			                                                             int count = Env.APP_SETTINGS.get( Settings.MULTI_REP_MAX_STATS_TO_OPEN_TOGETHER );
			                                                             IPage< ? > firstPage = null;
			                                                             
			                                                             for ( final int row : table.getSelectedModelRows() ) {
				                                                             final Stats stats = (Stats) model.getValueAt( row, statsColIdx );
				                                                             final IPage< ? > page = new MultiRepAnalyzerPage( stats.getRepFiles() );
				                                                             if ( firstPage == null )
					                                                             firstPage = page;
				                                                             Env.MAIN_FRAME.getMultiRepAnalyzersPage().addChild( page );
				                                                             
				                                                             if ( --count <= 0 )
					                                                             break;
			                                                             }
			                                                             
			                                                             if ( firstPage != null ) {
				                                                             Env.MAIN_FRAME.rebuildMainPageTree();
				                                                             Env.MAIN_FRAME.multiPageComp.selectPage( firstPage );
			                                                             }
		                                                             }
	                                                             };
	
	
	/** Table displaying the stats. */
	protected final XTable             table                     = new XTable();
	
	/** Improved model of the stats table. */
	protected final XTableModel        model                     = table.getXTableModel();
	
	/** Column names. */
	protected final Vector< String >   columns                   = new Vector<>();
	
	/** Column classes. */
	protected final List< Class< ? > > columnClasses             = new Vector<>();
	
	/** List of columns to be sorted descendant by default. */
	protected final List< Integer >    defaultDescSortingColList = new ArrayList<>();
	
	/** Stats model column index. */
	protected int                      statsColIdx;
	
	/** Object model column index. */
	protected int                      objColIdx;
	
	/** Display name to be used when replays of stats rows are opened. */
	protected final String             displayName;
	
	/** Stats table tool bar. */
	protected final ToolBarForTable    toolBar                   = new ToolBarForTable( table );
	
	
	/**
	 * Creates a new {@link TableStatsComp}.
	 * 
	 * @param displayName display name to be used when replays of stats rows are opened
	 */
	public TableStatsComp( final String displayName ) {
		this.displayName = displayName;
		
		buildGui();
	}
	
	/**
	 * Builds the GUI of the general table stats comp.
	 */
	protected void buildGui() {
		// Tool bar
		addNorth( toolBar );
		toolBar.addSelectInfoLabel( "Select a Row." );
		toolBar.addSelEnabledButton( listRepsAction );
		toolBar.addSelEnabledButton( analyzeRepsAction );
		
		table.setShiftOpenAction( listRepsAction );
		table.setCtrlOpenAction( analyzeRepsAction );
		addCenter( table.createWrapperBox( true, table.createToolBarParams( this ) ) );
		
		// Listen setting changes
		final ISettingChangeListener scl = new ISettingChangeListener() {
			@Override
			public void valuesChanged( final ISettingChangeEvent event ) {
				if ( event.affected( Settings.MULTI_REP_STRETCH_TABLES ) )
					table.setAutoResizeMode( event.get( Settings.MULTI_REP_STRETCH_TABLES ) ? JTable.AUTO_RESIZE_ALL_COLUMNS : JTable.AUTO_RESIZE_OFF );
			}
		};
		SettingsGui.addBindExecuteScl( scl, Env.APP_SETTINGS, Settings.MULTI_REP_STRETCH_TABLES.SELF_SET, table );
	}
	
	/**
	 * Adds a new column.
	 * 
	 * @param name name of the column
	 * @param class_ class of the column
	 * @return the column index that was added
	 */
	protected int addColumn( final String name, final Class< ? > class_ ) {
		return addColumn( name, class_, false );
	}
	
	/**
	 * Adds a new column.
	 * 
	 * @param name name of the column
	 * @param class_ class of the column
	 * @param defaultDescSorting tells if column is to be sorted descendant by default
	 * @return the column index that was added
	 */
	protected int addColumn( final String name, final Class< ? > class_, final boolean defaultDescSorting ) {
		if ( defaultDescSorting )
			defaultDescSortingColList.add( columns.size() );
		
		columns.add( name );
		columnClasses.add( class_ );
		
		return columns.size() - 1;
	}
	
	/**
	 * Sets the default descendant sorting for columns that were marked so by {@link #addColumn(String, Class, boolean)}.
	 */
	protected void setDefaultDescSortingColumns() {
		final XTableRowSorter xTableRowSorter = table.getXTableRowSorter();
		
		for ( final Integer col : defaultDescSortingColList )
			xTableRowSorter.setColumnDefaultDesc( col, true );
	}
	
}
