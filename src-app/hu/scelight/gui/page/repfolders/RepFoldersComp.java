/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.gui.page.repfolders;

import hu.scelight.Scelight;
import hu.scelight.action.Actions;
import hu.scelight.bean.repfolders.RepFolderBean;
import hu.scelight.bean.repfolders.RepFolderOrigin;
import hu.scelight.bean.repfolders.RepFoldersBean;
import hu.scelight.gui.dialog.RepFiltersEditorDialog;
import hu.scelight.gui.help.Helps;
import hu.scelight.gui.icon.Icons;
import hu.scelight.gui.page.multirepanalyzer.MultiRepAnalyzerPage;
import hu.scelight.gui.page.replist.RepListPage;
import hu.scelight.gui.setting.SettingsGui;
import hu.scelight.service.env.Env;
import hu.scelight.service.settings.Settings;
import hu.scelight.util.Utils;
import hu.scelight.util.gui.GuiUtils;
import hu.scelight.util.gui.TableIcon;
import hu.scelight.util.sc2rep.RepCounterJob;
import hu.scelight.util.sc2rep.RepUtils;
import hu.scelightapi.bean.repfilters.IRepFiltersBean;
import hu.scelightapibase.bean.settings.ISettingChangeEvent;
import hu.scelightapibase.bean.settings.ISettingChangeListener;
import hu.scelightapibase.gui.comp.multipage.IPage;
import hu.sllauncher.action.XAction;
import hu.sllauncher.bean.settings.type.Setting;
import hu.sllauncher.gui.comp.BorderPanel;
import hu.sllauncher.gui.comp.HelpIcon;
import hu.sllauncher.gui.comp.ToolBarForTable;
import hu.sllauncher.gui.comp.XCheckBox;
import hu.sllauncher.gui.comp.XFileChooser;
import hu.sllauncher.gui.comp.XLabel;
import hu.sllauncher.gui.comp.combobox.XComboBox;
import hu.sllauncher.gui.comp.table.XTable;
import hu.sllauncher.gui.comp.table.XTableModel;
import hu.sllauncher.service.job.Job;
import hu.sllauncher.util.gui.LGuiUtils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.RowSorter.SortKey;
import javax.swing.SortOrder;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

/**
 * Component of the Replay folders page.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class RepFoldersComp extends BorderPanel {
	
	/** Table displaying the replay folders. */
	private final XTable  table                = new XTable();
	
	
	/** Opens the selected replay folder with the attached filters. */
	private final XAction openAction           = new XAction( Icons.F_BLUE_FOLDER_OPEN_TABLE,
	                                                   "<html>Open Selected Replay Folders  (with <b>attached</b> filters) (Enter)</html>", this ) {
		                                           @Override
		                                           public void actionPerformed( final ActionEvent event ) {
			                                           openSelected( false, false );
		                                           }
	                                           };
	
	/** Searches the selected replay folder with custom filters. */
	private final XAction searchAction         = new XAction( Icons.F_BLUE_FOLDER_SEARCH_RESULT,
	                                                   "<html>Search Selected Replay Folders  (with <b>custom</b> filters)... (Ctrl+Enter)</html>", this ) {
		                                           @Override
		                                           public void actionPerformed( final ActionEvent event ) {
			                                           openSelected( false, true );
		                                           }
	                                           };
	
	/** Analyzes the selected replay folder with the attached filters. */
	private final XAction analyzeAction        = new XAction( Icons.F_CHART_UP_COLOR,
	                                                   "<html>Multi-Replay Analyze Selected Replay Folders  (with <b>attached</b> filters)</html>", this ) {
		                                           @Override
		                                           public void actionPerformed( final ActionEvent event ) {
			                                           openSelected( true, false );
		                                           }
	                                           };
	
	/** Filter-analyzes the selected replay folder with custom filters. */
	private final XAction analyze2Action       = new XAction( Icons.MY_CHART_UP_COLOR_MAGNIFIER,
	                                                   "<html>Multi-Replay Analyze Selected Replay Folders  (with <b>custom</b> filters)... </html>", this ) {
		                                           @Override
		                                           public void actionPerformed( final ActionEvent event ) {
			                                           openSelected( true, true );
		                                           }
	                                           };
	
	/** Show selected in File Browser. */
	private final XAction showInFBrowserAction = new XAction( Icons.F_FOLDER_OPEN, "Show Selected Replay Folders In File Browser (Shift+Enter)", this ) {
		                                           @Override
		                                           public void actionPerformed( final ActionEvent event ) {
			                                           for ( final int row : table.getSelectedModelRows() )
				                                           Utils.showPathInFileBrowser( ( (RepFolderBean) ( table.getModel().getValueAt( row, beanColIdx ) ) )
				                                                   .getPath() );
		                                           }
	                                           };
	
	/** Edit Attached Filters of the selected replay folder. */
	private final XAction editFiltersAction    = new XAction( Icons.F_FUNNEL_PENCIL, "Edit Attached Filters of the Selected Replay Folder", this ) {
		                                           @Override
		                                           public void actionPerformed( final ActionEvent event ) {
			                                           if ( !canPerformModifierAction() )
				                                           return;
			                                           final int selectedRow = table.getSelectedModelRow();
			                                           
			                                           final RepFoldersBean rfsBean = Env.APP_SETTINGS.get( Settings.REPLAY_FOLDERS_BEAN ).cloneBean();
			                                           
			                                           final List< RepFolderBean > rfBeanList = rfsBean.getReplayFolderBeanList();
			                                           
			                                           final RepFolderBean rf = rfBeanList.get( selectedRow );
			                                           new RepFiltersEditorDialog( rfsBean, rf );
		                                           }
	                                           };
	
	/** Moves up the selected replay folder. */
	private final XAction moveUpAction         = new XAction( Icons.F_ARROW_090, "Move Up Selected Replay Folders (Decrement Position)", this ) {
		                                           @Override
		                                           public void actionPerformed( final ActionEvent event ) {
			                                           if ( !canPerformModifierAction() )
				                                           return;
			                                           
			                                           final RepFoldersBean rfsBean = Env.APP_SETTINGS.get( Settings.REPLAY_FOLDERS_BEAN ).cloneBean();
			                                           
			                                           if ( Utils.moveBackward( rfsBean.getReplayFolderBeanList(), table.getSelectedModelRows() ) )
				                                           Env.APP_SETTINGS.set( Settings.REPLAY_FOLDERS_BEAN, rfsBean );
		                                           }
	                                           };
	
	/** Moves down the selected replay folder. */
	private final XAction moveDownAction       = new XAction( Icons.F_ARROW_270, "Move Down Selected Replay Folders (Increment Position)", this ) {
		                                           @Override
		                                           public void actionPerformed( final ActionEvent event ) {
			                                           if ( !canPerformModifierAction() )
				                                           return;
			                                           
			                                           final RepFoldersBean rfsBean = Env.APP_SETTINGS.get( Settings.REPLAY_FOLDERS_BEAN ).cloneBean();
			                                           
			                                           if ( Utils.moveForward( rfsBean.getReplayFolderBeanList(), table.getSelectedModelRows() ) )
				                                           Env.APP_SETTINGS.set( Settings.REPLAY_FOLDERS_BEAN, rfsBean );
		                                           }
	                                           };
	
	/** Removes the selected replay folder. */
	private final XAction removeAction         = new XAction( Icons.F_BLUE_FOLDER_MINUS, "Remove Selected Replay Folders... (Delete)", this ) {
		                                           @Override
		                                           public void actionPerformed( final ActionEvent event ) {
			                                           if ( !canPerformModifierAction() )
				                                           return;
			                                           final int[] selectedRows = table.getSelectedModelRows();
			                                           if ( !GuiUtils.confirm( Utils.plural( "Are you sure you want to remove %s Replay Folder%s?",
			                                                   selectedRows.length ) ) )
				                                           return;
			                                           
			                                           final RepFoldersBean rfsBean = Env.APP_SETTINGS.get( Settings.REPLAY_FOLDERS_BEAN ).cloneBean();
			                                           
			                                           // Sort rows and remove going downward
			                                           Arrays.sort( selectedRows );
			                                           final List< RepFolderBean > rfBeanList = rfsBean.getReplayFolderBeanList();
			                                           // Only rep folders originated from the user can be removed
			                                           for ( int i = selectedRows.length - 1; i >= 0; i-- )
				                                           if ( rfBeanList.get( selectedRows[ i ] ).getOrigin() == RepFolderOrigin.USER )
					                                           rfBeanList.remove( selectedRows[ i ] );
			                                           Env.APP_SETTINGS.set( Settings.REPLAY_FOLDERS_BEAN, rfsBean );
		                                           }
	                                           };
	
	
	/** Enables/disables inline table editing. */
	private final XAction enableEditAction     = new XAction( Icons.F_BLUE_FOLDER_PENCIL, "", this ) {
		                                           {
			                                           setTexts();
		                                           }
		                                           
		                                           /**
		                                            * Sets action texts (name and tool tip) based on the current editable state.
		                                            */
		                                           private void setTexts() {
			                                           if ( table.isEditable() )
				                                           putValue( NAME, "<html>Disable Editing<br><small>inline table</html>" );
			                                           else
				                                           putValue( NAME, "<html>Enable Editing<br><small>inline table</html>" );
			                                           
			                                           putValue( SHORT_DESCRIPTION, ( table.isEditable() ? "Disable" : "Enable" ) + " Inline Table Editing" );
		                                           }
		                                           
		                                           @Override
		                                           public void actionPerformed( final ActionEvent event ) {
			                                           if ( !canPerformModifierAction() )
				                                           return;
			                                           
			                                           table.setEditable( !table.isEditable() );
			                                           setTexts();
			                                           changeEditableButton.setForeground( table.isEditable() ? Color.RED : null );
			                                           changeEditableButton.setFont( null );
			                                           if ( table.isEditable() )
				                                           LGuiUtils.boldFont( changeEditableButton );
			                                           
			                                           // Save settings when finished editing
			                                           if ( !table.isEditable() )
				                                           Scelight.INSTANCE().saveSettings();
			                                           
			                                           updateRefreshActionEnabled();
		                                           }
	                                           };
	
	/** Adds a new replay folder. */
	private final XAction addNewAction         = new XAction( Icons.F_BLUE_FOLDER_PLUS, "Add New Replay Folder...", this ) {
		                                           @Override
		                                           public void actionPerformed( final ActionEvent event ) {
			                                           if ( !canPerformModifierAction() )
				                                           return;
			                                           final XFileChooser fileChooser = new XFileChooser();
			                                           fileChooser.setDialogTitle( "Choose a Replay Folder to add" );
			                                           fileChooser.setApproveButtonText( "Add" );
			                                           fileChooser.setFileSelectionMode( XFileChooser.DIRECTORIES_ONLY );
			                                           final XComboBox< ? > cb = RepUtils.createRepFoldersShortcutCombo( fileChooser );
			                                           cb.setPreferredSize( new Dimension( 300, cb.getPreferredSize().height ) );
			                                           fileChooser.setAccessory( GuiUtils.wrapInPanel( cb ) );
			                                           if ( XFileChooser.APPROVE_OPTION != fileChooser.showOpenDialog( Env.MAIN_FRAME ) )
				                                           return;
			                                           
			                                           final RepFoldersBean rfsBean = Env.APP_SETTINGS.get( Settings.REPLAY_FOLDERS_BEAN ).cloneBean();
			                                           
			                                           final RepFolderBean rf = new RepFolderBean();
			                                           rf.setOrigin( RepFolderOrigin.USER );
			                                           rf.setPath( fileChooser.getSelectedPath() );
			                                           rf.setRecursive( true );
			                                           rf.setMonitored( false );
			                                           rfsBean.getReplayFolderBeanList().add( rf );
			                                           Env.APP_SETTINGS.set( Settings.REPLAY_FOLDERS_BEAN, rfsBean );
		                                           };
	                                           };
	
	/** Restore default replay folders. */
	private final XAction restoreAction        = new XAction( Icons.F_ARROW_RETURN_180_LEFT, "Restore Default Replay Folders...", this ) {
		                                           @Override
		                                           public void actionPerformed( final ActionEvent event ) {
			                                           if ( !canPerformModifierAction() )
				                                           return;
			                                           if ( !GuiUtils.confirm( "Are you sure you want to restore the default Replay Folders?" ) )
				                                           return;
			                                           
			                                           // A simple SettingsBean.reset( Settings.REPLAY_FOLDERS_BEAN )
			                                           // is not enough because it uses the DEFAULT value for the
			                                           // replay backup folder (and not the ACTUAL value)
			                                           final RepFoldersBean rfs = Settings.REPLAY_FOLDERS_BEAN.defaultValue.cloneBean();
			                                           rfs.getRepFolderForOrigin( RepFolderOrigin.REP_BACKUP_FOLDER ).setPath(
			                                                   Env.APP_SETTINGS.get( Settings.REPLAY_BACKUP_FOLDER ) );
			                                           Env.APP_SETTINGS.set( Settings.REPLAY_FOLDERS_BEAN, rfs );
		                                           }
	                                           };
	
	/** Refreshes the replays counts. */
	private final XAction refreshAction        = new XAction( Icons.F_ARROW_CIRCLE_315, "Refresh Replays Count", this ) {
		                                           @Override
		                                           public void actionPerformed( final ActionEvent event ) {
			                                           refreshButton.setVisible( false );
			                                           countingLabel.setVisible( true );
			                                           
			                                           repCounterCoordinator = new RepCounterCoordinator();
			                                           repCounterCoordinator.start();
		                                           }
	                                           };
	
	
	/**
	 * Tests if a replay folder modifier action can be performed.
	 * 
	 * <p>
	 * If a replay counting is in progress, an info dialog will notify the user about it.
	 * </p>
	 * 
	 * @return true if a replay folder modifier action can be performed; false otherwise
	 */
	private boolean canPerformModifierAction() {
		if ( repCounterCoordinator != null ) {
			GuiUtils.showWarningMsg( "Cannot complete action, a replay counting is in progress.", " ",
			        GuiUtils.linkForAction( "View Running Jobs...", Actions.RUNNING_JOBS ) );
			return false;
		}
		return true;
	}
	
	
	/** Reference to the refresh tool bar button. */
	private JButton               refreshButton;
	
	/** Button to change table editing mode. */
	private JButton               changeEditableButton;
	
	/** Label to display counting status. */
	private final XLabel          countingLabel = new XLabel( "Counting...", Icons.F_HOURGLASS.get(), SwingConstants.LEFT ).italicFont().leftBorder( 5 );
	
	/** Reference to the rep counter coordinator (if counting is in progress). */
	private RepCounterCoordinator repCounterCoordinator;
	
	/**
	 * Coordinator of replay counters.
	 * 
	 * @author Andras Belicza
	 */
	private class RepCounterCoordinator extends Job {
		
		/** Working copy of the replay folders bean. */
		private final RepFoldersBean  rfsBean     = Env.APP_SETTINGS.get( Settings.REPLAY_FOLDERS_BEAN ).cloneBean();
		
		/** Parallel counter for each replay folder. */
		private final RepCounterJob[] repCounters = new RepCounterJob[ rfsBean.getReplayFolderBeanList().size() ];
		
		/**
		 * Creates a new {@link RepCounterCoordinator}.
		 */
		public RepCounterCoordinator() {
			super( "Replay Counter Coordinator", Icons.F_BLUE_FOLDERS_STACK );
			
			for ( int i = 0; i < repCounters.length; i++ ) {
				final RepFolderBean rf = rfsBean.getReplayFolderBeanList().get( i );
				repCounters[ i ] = new RepCounterJob( rf );
			}
		}
		
		@Override
		public void jobRun() {
			// Launch counters parallel
			for ( int i = 0; i < repCounters.length; i++ ) {
				rfsBean.getReplayFolderBeanList().get( i ).setCountedAt( new Date() );
				repCounters[ i ].start();
			}
			
			// Wait for the counters to finish
			for ( int i = 0; i < repCounters.length; i++ ) {
				repCounters[ i ].waitToFinish();
				rfsBean.getReplayFolderBeanList().get( i ).setReplaysCount( repCounters[ i ].getCount() );
				// I decided not to publish sub-results, because the finish order is random (not determined),
				// and I don't want to poll counters. This way I also avoid having to clone here again.
			}
			
			GuiUtils.runInEDT( new Runnable() {
				@Override
				public void run() {
					repCounterCoordinator = null;
					
					// Replay backup folder might have been changed during the counting:
					rfsBean.getRepFolderForOrigin( RepFolderOrigin.REP_BACKUP_FOLDER ).setPath( Env.APP_SETTINGS.get( Settings.REPLAY_BACKUP_FOLDER ) );
					
					Env.APP_SETTINGS.set( Settings.REPLAY_FOLDERS_BEAN, rfsBean );
					
					countingLabel.setVisible( false );
					refreshButton.setVisible( true );
				}
			} );
		}
		
		@Override
		public void requestCancel() {
			super.requestCancel();
			// Forward cancel request to sub-tasks
			for ( final RepCounterJob repCounter : repCounters )
				repCounter.requestCancel();
		}
		
	}
	
	/** {@link RepFolderBean} column model index. */
	private int beanColIdx;
	
	/** Path column model index. */
	private int pathColIdx;
	
	/** Recursive column model index. */
	private int recursiveColIdx;
	
	/** Monitored column model index. */
	private int monitoredColIdx;
	
	/** Filters column model index. */
	private int filtersColIdx;
	
	/** Position column model index. */
	private int positionColIdx;
	
	/** Comment column model index. */
	private int commentColIdx;
	
	/** Replay count column model index. */
	private int countColIdx;
	
	/**
	 * Creates a new {@link RepFoldersComp}.
	 */
	public RepFoldersComp() {
		buildGui();
		
		if ( Env.APP_SETTINGS.get( Settings.SHOW_REPLAYS_COUNT ) && Env.APP_SETTINGS.get( Settings.REFR_REPS_COUNT_ON_START ) )
			refreshAction.actionPerformed( null );
	}
	
	/**
	 * Builds the GUI of the page.
	 */
	private void buildGui() {
		final ToolBarForTable toolBar = new ToolBarForTable( table );
		addNorth( toolBar );
		toolBar.addSelectInfoLabel( "Select a Replay Folder." );
		toolBar.addSelEnabledButton( openAction );
		toolBar.addSelEnabledButton( searchAction );
		toolBar.addSelEnabledButton( analyzeAction );
		toolBar.addSelEnabledButton( analyze2Action );
		toolBar.addSelEnabledButton( showInFBrowserAction );
		toolBar.addSeparator();
		toolBar.addSelEnabledButton( editFiltersAction );
		toolBar.addSelEnabledButton( moveUpAction );
		toolBar.addSelEnabledButton( moveDownAction );
		toolBar.addSelEnabledButton( removeAction );
		toolBar.addSeparator();
		toolBar.addSeparator();
		toolBar.add( addNewAction );
		toolBar.addSeparator();
		changeEditableButton = new JButton( enableEditAction );
		toolBar.add( changeEditableButton );
		toolBar.addSeparator();
		toolBar.add( restoreAction );
		toolBar.addSeparator();
		final XCheckBox showRepsCountCheckBox = SettingsGui.createBoundedSettingCheckBox( Settings.SHOW_REPLAYS_COUNT, Env.APP_SETTINGS, null );
		showRepsCountCheckBox.setBorder( BorderFactory.createEmptyBorder( 0, 0, 0, 5 ) );
		toolBar.add( showRepsCountCheckBox );
		GuiUtils.autoCreateDisabledImage( refreshButton = toolBar.add( refreshAction ) );
		countingLabel.setVisible( false );
		toolBar.add( countingLabel );
		toolBar.addSeparator();
		toolBar.add( new HelpIcon( Helps.REPLAY_FOLDERS ) );
		toolBar.finalizeLayout();
		
		table.setOpenAction( openAction );
		table.setCtrlOpenAction( searchAction );
		table.setShiftOpenAction( showInFBrowserAction );
		table.setDeleteAction( removeAction );
		( (DefaultCellEditor) table.getDefaultEditor( String.class ) ).setClickCountToStart( 1 );
		addCenter( table.createWrapperBox( true ) );
		
		registerTableHotkeys();
		
		final XTableModel model = table.getXTableModel();
		model.addTableModelListener( new TableModelListener() {
			@Override
			public void tableChanged( final TableModelEvent event ) {
				if ( !table.isEditing() )
					return;
				
				// Model event returns model indices
				final int col = event.getColumn();
				final int row = event.getFirstRow();
				final Object value = model.getValueAt( row, col );
				
				// TODO do not even allow editing non-editable stuff in the first place
				
				final RepFoldersBean rfsBean = Env.APP_SETTINGS.get( Settings.REPLAY_FOLDERS_BEAN ).cloneBean();
				final RepFolderBean replayFolderBean = rfsBean.getReplayFolderBeanList().get( row );
				if ( col == pathColIdx ) {
					if ( replayFolderBean.isPathEditable() )
						replayFolderBean.setPath( (Path) value );
				} else if ( col == recursiveColIdx )
					replayFolderBean.setRecursive( (Boolean) value );
				else if ( col == monitoredColIdx ) {
					if ( replayFolderBean.isMonitoredEditable() )
						replayFolderBean.setMonitored( (Boolean) value );
				} else if ( col == commentColIdx )
					replayFolderBean.setComment( (String) value );
				
				SwingUtilities.invokeLater( new Runnable() {
					@Override
					public void run() {
						Env.APP_SETTINGS.set( Settings.REPLAY_FOLDERS_BEAN, rfsBean );
					}
				} );
			}
		} );
		
		// Listen setting changes
		final ISettingChangeListener scl = new ISettingChangeListener() {
			@Override
			public void valuesChanged( final ISettingChangeEvent event ) {
				if ( event.affected( Settings.SHOW_REPLAYS_COUNT ) )
					updateRefreshActionEnabled();
				
				if ( event.affectedAny( Settings.REPLAY_FOLDERS_BEAN, Settings.SHOW_REPLAYS_COUNT ) )
					rebuildTable( event.affected( Settings.SHOW_REPLAYS_COUNT ) );
			}
		};
		final Set< Setting< ? > > listendSettingSet = Utils.< Setting< ? > > asNewSet( Settings.SHOW_REPLAYS_COUNT, Settings.REPLAY_FOLDERS_BEAN );
		SettingsGui.addBindExecuteScl( scl, Env.APP_SETTINGS, listendSettingSet, toolBar );
	}
	
	/**
	 * Registers the table hotkeys.
	 */
	private void registerTableHotkeys() {
		// TODO?
	}
	
	/**
	 * Rebuilds the table.
	 * 
	 * @param rebuildHeader tells if column headers are also to be rebuilt
	 */
	private void rebuildTable( final boolean rebuildHeader ) {
		table.saveSelection( beanColIdx );
		
		// Model cannot be updated while editing, so stop editing if it is in progress!
		if ( table.isEditing() )
			table.getCellEditor().stopCellEditing();
		
		final XTableModel model = table.getXTableModel();
		
		final boolean showRepsCount = Env.APP_SETTINGS.get( Settings.SHOW_REPLAYS_COUNT );
		
		if ( rebuildHeader ) {
			// Re-set column headers
			final Vector< String > columns = Utils.asNewVector( "Bean", "I", "Folder", "Include sub-folders?", "Monitored?", "Filters", "Position", "Comment",
			        "# of Replays" );
			final List< Class< ? > > columnClasses = Utils.< Class< ? > > asNewList( RepFolderBean.class, TableIcon.class, Path.class, Boolean.class,
			        Boolean.class, Integer.class, Integer.class, String.class, Integer.class );
			beanColIdx = 0;
			pathColIdx = 2;
			recursiveColIdx = 3;
			monitoredColIdx = 4;
			filtersColIdx = 5;
			positionColIdx = 6;
			commentColIdx = 7;
			countColIdx = 8;
			table.setEditableColModelIndices( pathColIdx, recursiveColIdx, monitoredColIdx, commentColIdx );
			table.getXTableRowSorter().setColumnDefaultDescs( true, recursiveColIdx, monitoredColIdx, filtersColIdx, countColIdx );
			
			model.setColumnIdentifiers( columns );
			model.setColumnClasses( columnClasses );
			if ( !showRepsCount )
				table.getColumnModel().removeColumn( table.getColumnModel().getColumn( countColIdx ) );
			table.getColumnModel().removeColumn( table.getColumnModel().getColumn( beanColIdx ) );
			table.getRowSorter().setSortKeys( Arrays.asList( new SortKey( positionColIdx, SortOrder.ASCENDING ) ) );
		}
		
		// Build table data
		model.getDataVector().clear();
		model.fireTableDataChanged();
		final RepFoldersBean rfsBean = Env.APP_SETTINGS.get( Settings.REPLAY_FOLDERS_BEAN );
		int pos = 1;
		for ( final RepFolderBean rfBean : rfsBean.getReplayFolderBeanList() ) {
			final Vector< Object > row = Utils.< Object > asNewVector( rfBean, rfBean.getOrigin().tableIcon, rfBean.getPath(), rfBean.getRecursive(),
			        rfBean.getMonitored(), rfBean.getActiveFilterCount(), pos++, rfBean.getComment(), rfBean.getReplaysCount() );
			model.addRow( row );
		}
		
		// Pack all columns except the path and comment columns (leave remaining space for them)
		for ( int i = table.getColumnCount() - 1; i >= 0; i-- )
			if ( i != table.convertColumnIndexToView( pathColIdx ) && i != table.convertColumnIndexToView( commentColIdx ) )
				table.packColumns( i );
		
		table.restoreSelection( beanColIdx );
	}
	
	/**
	 * Opens the selected replay folders.
	 * 
	 * @param analyze if true, analyze replays else list
	 * @param useCustomFilters tells if custom filters are to be used; else the attached filters of the replay folders
	 */
	private void openSelected( final boolean analyze, final boolean useCustomFilters ) {
		if ( table.isEditable() )
			return;
		
		IRepFiltersBean customFiltersBean = null;
		if ( useCustomFilters ) {
			final RepFiltersEditorDialog filtersEditorDialog = new RepFiltersEditorDialog();
			if ( !filtersEditorDialog.isOk() )
				return;
			customFiltersBean = filtersEditorDialog.getRepFiltersBean();
		}
		
		IPage< ? > firstPage = null;
		for ( final int row : table.getSelectedModelRows() ) {
			final RepFolderBean rfBean = (RepFolderBean) table.getModel().getValueAt( row, beanColIdx );
			IPage< ? > page;
			
			if ( analyze )
				Env.MAIN_FRAME.getMultiRepAnalyzersPage().addChild(
				        page = useCustomFilters ? new MultiRepAnalyzerPage( rfBean, customFiltersBean ) : new MultiRepAnalyzerPage( rfBean ) );
			else
				Env.MAIN_FRAME.getRepFoldersPage()
				        .addChild( page = useCustomFilters ? new RepListPage( rfBean, customFiltersBean ) : new RepListPage( rfBean ) );
			
			if ( firstPage == null )
				firstPage = page;
		}
		if ( firstPage != null ) {
			Env.MAIN_FRAME.rebuildMainPageTree();
			Env.MAIN_FRAME.multiPageComp.selectPage( firstPage );
		}
	}
	
	/**
	 * Updates the enabled state of the refresh action.
	 */
	private void updateRefreshActionEnabled() {
		refreshAction.setEnabled( !table.isEditable() && Env.APP_SETTINGS.get( Settings.SHOW_REPLAYS_COUNT ) );
	}
	
}
