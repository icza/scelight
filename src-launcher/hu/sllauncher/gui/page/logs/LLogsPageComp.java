/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.sllauncher.gui.page.logs;

import hu.scelightapibase.bean.settings.ISettingChangeEvent;
import hu.scelightapibase.bean.settings.ISettingChangeListener;
import hu.scelightapibase.gui.comp.multipage.IPageSelectedListener;
import hu.scelightapibase.util.iface.Func;
import hu.sllauncher.action.XAction;
import hu.sllauncher.gui.comp.BorderPanel;
import hu.sllauncher.gui.comp.ModestLabel;
import hu.sllauncher.gui.comp.XCheckBox;
import hu.sllauncher.gui.comp.XLabel;
import hu.sllauncher.gui.comp.XToolBar;
import hu.sllauncher.gui.comp.combobox.XComboBox;
import hu.sllauncher.gui.comp.table.XTable;
import hu.sllauncher.gui.comp.table.XTableModel;
import hu.sllauncher.gui.comp.table.renderer.XTableCellRenderer;
import hu.sllauncher.gui.icon.LIcons;
import hu.sllauncher.gui.setting.LSettingsGui;
import hu.sllauncher.service.env.LEnv;
import hu.sllauncher.service.log.LogFormatter;
import hu.sllauncher.service.log.LogLevel;
import hu.sllauncher.service.log.LoggedRecord;
import hu.sllauncher.service.settings.LSettings;
import hu.sllauncher.util.LUtils;
import hu.sllauncher.util.SkillLevel;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * Logs page component.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class LLogsPageComp extends BorderPanel implements IPageSelectedListener {
	
	/** Action which refreshes the displayed logs. */
	protected final XAction             refreshAction       = new XAction( LIcons.F_ARROW_CIRCLE_315, "Refresh", this ) {
		                                                        @Override
		                                                        public void actionPerformed( final ActionEvent event ) {
			                                                        refreshLogs();
		                                                        }
	                                                        };
	
	
	/** Box holding the tool bars. */
	protected final Box                 toolBarsBox         = Box.createVerticalBox();
	
	/** Combo box to filter displayed logs (log records). */
	private final XComboBox< LogLevel > filterLevelComboBox = new XComboBox<>( LogLevel.VALUES );
	
	/** Check box to tell if details are to be shown (subsequent lines of log records). */
	private final XCheckBox             showDetailsCheckBox = LSettingsGui.createSettingCheckBox( LSettings.SHOW_LOG_DETAILS, LEnv.LAUNCHER_SETTINGS, null );
	
	/** Label to display records count. */
	private final ModestLabel           recordsCountLabel   = new ModestLabel();
	
	/** Label to display last refresh time. */
	private final ModestLabel           refreshedTimeLabel  = new ModestLabel();
	
	/** Table to display logs. */
	private final XTable                table               = new XTable();
	
	/** Combo box to display the available log files. */
	protected final XComboBox< String > logFilesComboBox    = new XComboBox<>( getLogFileVector() );
	
	
	/**
	 * Label to display current log file size.
	 * <p>
	 * This is only used by the descendant, but is put here because it is used in the overridden method of {@link #refreshLogs()} which is called from the
	 * constructor of this class, therefore it would not yet be initialized in the descendant {@link #refreshLogs()} method. (Descendant fields are only
	 * initialized after super constructor completes!)
	 * </p>
	 */
	protected final ModestLabel         sizeLabel           = new ModestLabel();
	
	
	/**
	 * Creates a new {@link LLogsPageComp}.
	 */
	public LLogsPageComp() {
		buildGui();
		
		// Auto refresh happens when page is made visible: done via implementing PageSelectedListener
	}
	
	/**
	 * Builds the GUI of the logs page component.
	 */
	protected void buildGui() {
		final XAction showInFileBrowserAction = new XAction( LIcons.F_FOLDER_OPEN, "Show in File Browser", this ) {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				LUtils.showPathInFileBrowser( LEnv.PATH_LOGS.resolve( logFilesComboBox.getSelectedItem() ) );
			}
		};
		
		addNorth( toolBarsBox );
		
		// Tool bar
		final XToolBar toolBar = new XToolBar();
		toolBarsBox.add( toolBar );
		
		toolBar.add( refreshAction );
		
		toolBar.addSeparator();
		toolBar.add( new XLabel( "Filter level:" ) );
		filterLevelComboBox.setSelectedItem( LEnv.DEV_MODE ? LogLevel.DEBUG : LogLevel.INFO );
		filterLevelComboBox.addActionListener( refreshAction );
		toolBar.add( filterLevelComboBox );
		
		toolBar.addSeparator();
		showDetailsCheckBox.setText( "Show Details" );
		showDetailsCheckBox.setToolTipText( LSettings.SHOW_LOG_DETAILS.name );
		LSettingsGui.bindVisibilityToSkillLevel( showDetailsCheckBox, LSettings.SHOW_LOG_DETAILS.skillLevel, Boolean.FALSE );
		showDetailsCheckBox.addActionListener( refreshAction );
		toolBar.add( showDetailsCheckBox );
		
		refreshedTimeLabel.leftBorder( 20 );
		LSettingsGui.bindVisibilityToSkillLevel( refreshedTimeLabel, SkillLevel.NORMAL );
		toolBar.add( refreshedTimeLabel );
		
		recordsCountLabel.horizontalBorder( 20 );
		recordsCountLabel.setToolTipText( "<html><b>Displayed</b> records count / <b>All</b> records count" );
		LSettingsGui.bindVisibilityToSkillLevel( recordsCountLabel, SkillLevel.ADVANCED );
		toolBar.add( recordsCountLabel );
		
		final JButton showInFileBrowserButton = toolBar.add( showInFileBrowserAction );
		LSettingsGui.bindVisibilityToSkillLevel( showInFileBrowserButton, SkillLevel.ADVANCED );
		
		toolBar.finalizeLayout();
		
		// Logs table
		// Custom renderer to change the background based on the log level
		table.setTableCellRenderer( new TableCellRenderer() {
			private final XTableCellRenderer xTableCellRenderer = table.getXTableCellRenderer();
			
			private final XTableModel        model              = table.getXTableModel();
			
			@Override
			public Component getTableCellRendererComponent( final JTable table, Object value, final boolean isSelected, final boolean hasFocus, final int row,
			        int column ) {
				xTableCellRenderer.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, column );
				if ( !isSelected )
					xTableCellRenderer.setBackground( ( (LogLevel) model.getValueAt( table.convertRowIndexToModel( row ), 1 ) ).color );
				return xTableCellRenderer;
			}
		} );
		addCenter( table.createWrapperBox( true, table.createToolBarParams( this ) ) );
		
		// Refresh logs when skill level changes because time stamps precision may change
		final ISettingChangeListener scl = new ISettingChangeListener() {
			int counter;
			
			@Override
			public void valuesChanged( final ISettingChangeEvent event ) {
				if ( event.affected( LSettings.SKILL_LEVEL ) )
					if ( counter++ > 0 ) // Skip the initial refresh (when this listener is being added)
						refreshLogs();
			}
		};
		LSettingsGui.addBindExecuteScl( scl, LEnv.LAUNCHER_SETTINGS, LSettings.SKILL_LEVEL.SELF_SET, showDetailsCheckBox );
		
		// Logs will be refreshed when page is selected.
	}
	
	/**
	 * Refreshes the displayed logs from the file.
	 */
	public void refreshLogs() {
		final Path logPath = LEnv.PATH_LOGS.resolve( logFilesComboBox.getSelectedItem() );
		
		final boolean dontShowDetails = !showDetailsCheckBox.isSelected();
		final LogLevel filterLevel = filterLevelComboBox.getSelectedItem();
		
		final XTableModel model = table.getXTableModel();
		
		final Vector< String > columns = LUtils.asNewVector( "Time", "Level", "Message" );
		final Vector< Vector< Object > > data = new Vector<>();
		
		try ( final BufferedReader in = Files.newBufferedReader( logPath, LEnv.UTF8 ) ) {
			final boolean showMs = SkillLevel.ADVANCED.isAtLeast();
			final Func< String, LoggedRecord > logParser = LogFormatter.getParser();
			int count = 0, allCount = 0; // Displayed and All log records count
			String line;
			boolean recordFilteredOut = false;
			LoggedRecord lr = null, lastLr = null;
			
			while ( ( line = in.readLine() ) != null ) {
				lr = logParser.exec( line );
				if ( lr == null && dontShowDetails )
					continue;
				if ( lr != null ) {
					allCount++;
					lastLr = lr;
				}
				
				if ( lr != null )
					recordFilteredOut = lr.logLevel.compareTo( filterLevel ) < 0;
				// if lr == null => recordFilteredOut holds the record starter line's filter status, exactly what we want
				if ( recordFilteredOut )
					continue;
				
				if ( lr != null )
					count++;
				
				final Vector< Object > row = new Vector<>( columns.size() );
				if ( lr == null ) {
					row.add( null );
					// lastLr is surely not null: table gets populated only when the first log record is detected
					row.add( lastLr.logLevel );
					row.add( line );
				} else {
					row.add( LEnv.LANG.formatDateTime( lr.time, showMs ) );
					row.add( lr.logLevel );
					row.add( lr.message );
				}
				data.add( row );
			}
			
			model.setDataVector( data, columns );
			table.setSortable( false );
			table.packColumnsExceptLast();
			
			refreshedTimeLabel.setText( "Refreshed at " + LEnv.LANG.formatTime( new Date() ) );
			recordsCountLabel.setText( "Records: " + LEnv.LANG.formatNumber( count ) + " / " + LEnv.LANG.formatNumber( allCount ) );
		} catch ( final Exception ie ) {
			LEnv.LOGGER.warning( "Failed to read logs.", ie );
		}
	}
	
	@Override
	public void pageSelected() {
		refreshLogs();
	}
	
	/**
	 * Returns the vector of the available log files in descendant order (newest is the first).
	 * <p>
	 * This implementation returns a vector holding only the current log file.
	 * </p>
	 * 
	 * @return the vector of the available log files in descendant order (newest is the first)
	 */
	protected Vector< String > getLogFileVector() {
		final Vector< String > logFileVector = new Vector<>( 1 );
		logFileVector.add( LEnv.LOGGER.activeLogPath.getFileName().toString() );
		return logFileVector;
	}
	
}
