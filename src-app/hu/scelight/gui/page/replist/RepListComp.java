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

import hu.scelight.bean.repfilters.RepFiltersBean;
import hu.scelight.bean.repfolders.RepFolderBean;
import hu.scelight.gui.dialog.RenameRepsDialog;
import hu.scelight.gui.dialog.RepFiltersEditorDialog;
import hu.scelight.gui.dialog.RepListColumnSetupDialog;
import hu.scelight.gui.dialog.SendEmailDialog;
import hu.scelight.gui.dialog.fileops.FileOp;
import hu.scelight.gui.dialog.fileops.FileOpsDialog;
import hu.scelight.gui.icon.Icons;
import hu.scelight.gui.page.multirepanalyzer.MultiRepAnalyzerPage;
import hu.scelight.gui.page.replist.column.IColumn;
import hu.scelight.gui.page.replist.column.RepListColumnRegistry;
import hu.scelight.gui.page.replist.column.impl.BaseCustomColumn;
import hu.scelight.gui.page.replist.column.impl.DateColumn;
import hu.scelight.gui.page.replist.column.impl.FavoredResultColumn;
import hu.scelight.gui.page.replist.column.impl.FileColumn;
import hu.scelight.gui.setting.SettingsGui;
import hu.scelight.gui.tip.Tips;
import hu.scelight.sc2.rep.factory.RepParserEngine;
import hu.scelight.sc2.rep.model.details.Result;
import hu.scelight.sc2.rep.repproc.RepProcessor;
import hu.scelight.search.RepSearchEngine;
import hu.scelight.service.env.Env;
import hu.scelight.service.settings.Settings;
import hu.scelight.util.Utils;
import hu.scelight.util.gui.GuiUtils;
import hu.scelight.util.gui.TableIcon;
import hu.scelight.util.sc2rep.RepCounterJob;
import hu.scelight.util.sc2rep.RepCrawlerJob;
import hu.scelightapi.bean.repfilters.IRepFiltersBean;
import hu.scelightapibase.gui.comp.multipage.IPageClosingListener;
import hu.sllauncher.action.XAction;
import hu.sllauncher.gui.comp.BorderPanel;
import hu.sllauncher.gui.comp.TipIcon;
import hu.sllauncher.gui.comp.ToolBarForTable;
import hu.sllauncher.gui.comp.XButton;
import hu.sllauncher.gui.comp.XCheckBox;
import hu.sllauncher.gui.comp.XLabel;
import hu.sllauncher.gui.comp.XProgressBar;
import hu.sllauncher.gui.comp.table.XTable;
import hu.sllauncher.gui.comp.table.XTableModel;
import hu.sllauncher.gui.comp.table.XTableRowSorter;
import hu.sllauncher.gui.comp.table.renderer.XTableCellRenderer;
import hu.sllauncher.service.job.Job;
import hu.sllauncher.util.DurationFormat;
import hu.sllauncher.util.gui.adapter.ActionAdapter;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.Box;
import javax.swing.JTable;
import javax.swing.RowSorter.SortKey;
import javax.swing.SortOrder;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

/**
 * Component of the Replay list page.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class RepListComp extends BorderPanel implements IPageClosingListener {
	
	/** Opens the selected replays. */
	private final XAction	openAction			  = new XAction( Icons.F_CHART, "Open Selected Replays (Enter)", this) {
		                                              @Override
		                                              public void actionPerformed( final ActionEvent event ) {
			                                              int count = 0;
			                                              final int max = Env.APP_SETTINGS.get( Settings.MAX_REPS_TO_OPEN_TOGETHER );
			                                              final TableModel model = table.getModel();
			                                              for ( final int row : table.getSelectedModelRows() ) {
				                                              final Path file = (Path) model.getValueAt( row, fileColIdx );
				                                              Env.MAIN_FRAME.repAnalyzersPage.newRepAnalyzerPage( file, count++ == 0 );
				                                              if ( count == max )
					                                              break;
			                                              }
		                                              }
	                                              };
												  
	/** Renames the selected replays. */
	private final XAction	renameAction		  = new XAction( Icons.F_BLUE_DOCUMENT_RENAME, "Rename Selected Replays...", this) {
		                                              @Override
		                                              public void actionPerformed( final ActionEvent event ) {
			                                              new RenameRepsDialog( RepListComp.this );
		                                              }
	                                              };
												  
	/** Watch the selected replay. */
	private final XAction	watchAction			  = new XAction( Icons.SC2_REPLAY,
	        "<html>Watch Selected Replay<div style='padding-left:15px;padding-top:3px'><i>(StarCraft II must NOT be running!)</i></div></html>", this) {
		                                              @Override
		                                              public void actionPerformed( final ActionEvent event ) {
			                                              Utils.launchReplay( (Path) table.getModel().getValueAt( table.getSelectedModelRow(), fileColIdx ) );
		                                              }
	                                              };
												  
	/** Multi-Replay Analyze selected rows. */
	private final XAction	analyzeSelectedAction = new XAction( Icons.MY_CHART_UP_CLR_TBL_SEL_ROW, "Multi-Replay Analyze Selected Replays", this) {
		                                              @Override
		                                              public void actionPerformed( final ActionEvent event ) {
			                                              // Open a new MultiRepAnalyzerPage with the selected replays:
			                                              int[] rows = table.getSelectedModelRows();
			                                              final Path[] repFiles = new Path[ rows.length ];
			                                              final TableModel model = table.getModel();
			                                              for ( int i = rows.length - 1; i >= 0; i-- )
				                                              repFiles[ i ] = (Path) model.getValueAt( rows[ i ], fileColIdx );
															  
			                                              final MultiRepAnalyzerPage mrap = new MultiRepAnalyzerPage( repFiles );
			                                              Env.MAIN_FRAME.getMultiRepAnalyzersPage().addChild( mrap );
			                                              Env.MAIN_FRAME.rebuildMainPageTree();
			                                              Env.MAIN_FRAME.multiPageComp.selectPage( mrap );
		                                              }
	                                              };
												  
	/** Show selected in File Browser. */
	private final XAction	showInFBrowserAction  = new XAction( Icons.F_FOLDER_OPEN, "Show Selected Replay In File Browser (Shift+Enter)", this) {
		                                              @Override
		                                              public void actionPerformed( final ActionEvent event ) {
			                                              Utils.showPathInFileBrowser(
	                                                              (Path) table.getModel().getValueAt( table.getSelectedModelRow(), fileColIdx ) );
		                                              }
	                                              };
												  
	/** File operations on the selected replays (e.g. copy, move, pack). */
	private final XAction[]	fileOpActions		  = new XAction[ FileOp.VALUES.length ];
												  
	{
		for ( int i = 0; i < FileOp.VALUES.length; i++ ) {
			final FileOp fop = FileOp.VALUES[ i ];
			fileOpActions[ i ] = new XAction( fop.ricon, fop.text + " Selected Replays...", this) {
				@Override
				public void actionPerformed( final ActionEvent event ) {
					int[] rows = table.getSelectedModelRows();
					final List< Path > repList = new ArrayList< >();
					final TableModel model = table.getModel();
					for ( final int row : rows )
						repList.add( (Path) model.getValueAt( row, fileColIdx ) );
					new FileOpsDialog( fop, repList, repFolderBean );
				}
			};
		}
	}
	
	/** Email the selected replays. */
	private final XAction		  emailAction		= new XAction( Icons.F_MAIL_AT_SIGN, "Email Selected Replays...", this) {
		                                                @Override
		                                                public void actionPerformed( final ActionEvent event ) {
			                                                if ( !Utils.checkEmailSettings() )
				                                                return;
			                                                int[] rows = table.getSelectedModelRows();
			                                                final List< Path > repList = new ArrayList< >();
			                                                final TableModel model = table.getModel();
			                                                for ( final int row : rows )
				                                                repList.add( (Path) model.getValueAt( row, fileColIdx ) );
			                                                new SendEmailDialog( "Send Replays via Email", repList, null );
		                                                }
	                                                };
													
	/** Deletes the selected replays. */
	private final XAction		  deleteAction		= new XAction( Icons.F_CROSS, "Delete Selected Replays... (Delete)", this) {
		                                                @Override
		                                                public void actionPerformed( final ActionEvent event ) {
			                                                final int[] rows = table.getSelectedModelRows();
			                                                if ( !GuiUtils
	                                                                .confirm( Utils.plural( "Are you sure you want to delete %s replay%s?", rows.length ) ) )
				                                                return;
			                                                final List< Integer > deletedList = new ArrayList< >( rows.length );
			                                                // count: Not necessarily the number of selected rows
			                                                int count = 0;
			                                                int errors = 0;
			                                                final XTableModel model = table.getXTableModel();
			                                                @SuppressWarnings( "unchecked" )
			                                                final Vector< Vector< Object > > data = model.getDataVector();
			                                                for ( final int row : rows ) {
				                                                final Path file = (Path) data.get( row ).get( fileColIdx );
				                                                try {
					                                                if ( Files.deleteIfExists( file ) )
						                                                count++;
					                                                // Being here means file didn't even exist in the first place
	                                                                // First just collect rows, must be removed in descending order!
					                                                deletedList.add( row );
				                                                } catch ( final IOException ie ) {
					                                                Env.LOGGER.error( "Failed to delete replay: " + file, ie );
					                                                errors++;
				                                                }
			                                                }
			                                                // Descending order is a must!
			                                                Collections.sort( deletedList );
			                                                for ( int i = deletedList.size() - 1; i >= 0; i-- )
				                                                data.remove( deletedList.get( i ).intValue() );
			                                                model.fireTableDataChanged();
			                                                table.pack();
			                                                if ( errors == 0 )
				                                                GuiUtils.showInfoMsg( new XLabel( Utils.plural( "Successfully deleted %s replay%s.", count ),
	                                                                    Icons.F_TICK.get() ) );
			                                                else
				                                                GuiUtils.showErrorMsg(
	                                                                    new XLabel( Utils.plural( "Successfully deleted %s replay%s.", count ),
	                                                                            Icons.F_TICK.get() ),
	                                                                    new XLabel( Utils.plural( "Failed to delete %s replay%s.", errors ),
	                                                                            Icons.F_CROSS.get() ) );
																				
		                                                }
	                                                };
													
	/** Edit filters. */
	private final XAction		  editFiltersAction	= new XAction( Icons.F_BLUE_FOLDER_SEARCH_RESULT, "View / Edit Filters and Repeat Search...", this) {
		                                                @Override
		                                                public void actionPerformed( final ActionEvent event ) {
			                                                final RepFiltersEditorDialog filtersEditorDialog = new RepFiltersEditorDialog(
	                                                                repFiltersBean == null ? null : repFiltersBean.< RepFiltersBean > cloneBean() );
			                                                if ( !filtersEditorDialog.isOk() )
				                                                return;
																
			                                                // Open a new RepListPage with the edited filters bean and select it:
			                                                final RepListPage rlp = repFolderBean == null
	                                                                ? new RepListPage( displayName, repFiles, filtersEditorDialog.getRepFiltersBean() )
	                                                                : new RepListPage( repFolderBean, filtersEditorDialog.getRepFiltersBean() );
			                                                Env.MAIN_FRAME.getRepFoldersPage().addChild( rlp );
			                                                Env.MAIN_FRAME.rebuildMainPageTree();
			                                                Env.MAIN_FRAME.multiPageComp.selectPage( rlp );
		                                                }
	                                                };
													
	/** Multi-Replay Analyze. */
	private final XAction		  analyzeAction		= new XAction( Icons.F_CHART_UP_COLOR,
	        "Multi-Replay Analyze All Replays (using the same source and filters)", this) {
		                                                @Override
		                                                public void actionPerformed( final ActionEvent event ) {
			                                                // Open a new MultiRepAnalyzerPage with the edited filters bean and select it:
			                                                final MultiRepAnalyzerPage mrap = repFolderBean == null
	                                                                ? new MultiRepAnalyzerPage( repFiles, repFiltersBean )
	                                                                : new MultiRepAnalyzerPage( repFolderBean, repFiltersBean );
			                                                Env.MAIN_FRAME.getMultiRepAnalyzersPage().addChild( mrap );
			                                                Env.MAIN_FRAME.rebuildMainPageTree();
			                                                Env.MAIN_FRAME.multiPageComp.selectPage( mrap );
		                                                }
	                                                };
													
	/** Column setup. */
	private final XAction		  colSetupAction	= new XAction( Icons.F_EDIT_COLUMN, "Column Setup...", this) {
		                                                @Override
		                                                public void actionPerformed( final ActionEvent event ) {
			                                                new RepListColumnSetupDialog( );
		                                                }
	                                                };
													
													
													
	/** Button to abort listing. */
	private final XButton		  abortButton		= new XButton( "_Abort", Icons.F_CROSS_OCTAGON.get() );
													
	/** Progress bar to display replay listing progress and general info. */
	private final XProgressBar	  progressBar		= new XProgressBar();
													
	/** Table displaying the replay folders. */
	public final XTable			  table				= new XTable();
													
													
	/** Display name of the page. */
	private final String		  displayName;
								  
	/** Replay folder bean to list. */
	private final RepFolderBean	  repFolderBean;
								  
	/** Replay files to list. */
	private final Path[]		  repFiles;
								  
	/** Replay filters to be used. */
	private final IRepFiltersBean repFiltersBean;
								  
	/** Search engine if replays are to be filtered. */
	private final RepSearchEngine searchEngine;
								  
	/** Mandatory file column model index. */
	public final int			  fileColIdx;
								  
	/** Mandatory favored result column model index. */
	public final int			  favResultColIdx;
								  
								  
	/**
	 * Creates a new {@link RepListComp}.
	 * 
	 * @param repFolderBean replay folder bean to list
	 * @param repFiltersBean replay filters to be used
	 */
	public RepListComp( final RepFolderBean repFolderBean, final IRepFiltersBean repFiltersBean ) {
		this( null, repFolderBean, null, repFiltersBean );
	}
	
	/**
	 * Creates a new {@link RepListComp}.
	 * 
	 * @param displayName display name of the page
	 * @param repFiles replay files to list
	 * @param repFiltersBean replay filters to be used
	 */
	public RepListComp( final String displayName, final Path[] repFiles, final IRepFiltersBean repFiltersBean ) {
		this( displayName, null, repFiles, repFiltersBean );
	}
	
	/**
	 * Creates a new {@link RepListComp}.
	 * 
	 * @param displayName display name of the page
	 * @param repFolderBean replay folder bean to list
	 * @param repFiles replay files to list
	 * @param repFiltersBean replay filters to be used
	 */
	private RepListComp( final String displayName, final RepFolderBean repFolderBean, final Path[] repFiles, final IRepFiltersBean repFiltersBean ) {
		this.displayName = displayName;
		this.repFolderBean = repFolderBean;
		this.repFiles = repFiles;
		this.repFiltersBean = repFiltersBean;
		this.searchEngine = repFiltersBean == null ? null : new RepSearchEngine( repFiltersBean );
		
		// Assemble table column list
		final List< Class< IColumn< ? > > > colClassList = Env.APP_SETTINGS.get( Settings.REP_LIST_COLUMNS_BEAN ).getColumnClassList();
		columns = new Vector< >( colClassList.size() + 2 );
		
		// Mandatory FileColumn to the first place!
		fileColIdx = columns.size();
		columns.add( RepListColumnRegistry.getColumnInstance( FileColumn.class ) );
		// Mandatory FavoredResultColumn for table coloring:
		favResultColIdx = columns.size();
		columns.add( RepListColumnRegistry.getColumnInstance( FavoredResultColumn.class ) );
		
		for ( final Class< IColumn< ? > > colClass : colClassList ) {
			// Only show custom columns if skill level is met
			if ( BaseCustomColumn.class.isAssignableFrom( colClass ) && Settings.REP_LIST_CUST_COL_1_NAME.skillLevel.isBelow() )
				continue;
				
			final IColumn< ? > colInstance = RepListColumnRegistry.getColumnInstance( colClass );
			if ( colInstance == null )
				continue;
				
			columns.add( colInstance );
		}
		
		buildGui();
		
		rebuildTable();
	}
	
	/** Columns to build / display. */
	// Put below the constructor to not screw the alignment of the action implementations...
	private final Vector< IColumn< ? > > columns;
	
	/**
	 * Builds the GUI of the page.
	 */
	private void buildGui() {
		final Box northBox = Box.createVerticalBox();
		addNorth( northBox );
		
		final Box progressBox = Box.createHorizontalBox();
		progressBar.setStringPainted( true );
		progressBox.add( progressBar );
		abortButton.addActionListener( new ActionAdapter() {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				if ( repCounterJob != null )
					repCounterJob.requestCancel();
				if ( repSearcherJob != null )
					repSearcherJob.requestCancel();
			}
		} );
		progressBox.add( abortButton );
		northBox.add( progressBox );
		
		final ToolBarForTable toolBar = new ToolBarForTable( table );
		northBox.add( toolBar );
		toolBar.addSelectInfoLabel( "Select a Replay." );
		toolBar.addSelEnabledButton( openAction );
		toolBar.addSelEnabledButton( renameAction );
		toolBar.addSelEnabledButton( watchAction );
		toolBar.addSelEnabledButton( analyzeSelectedAction );
		toolBar.addSelEnabledButton( showInFBrowserAction );
		toolBar.addSeparator();
		for ( final XAction fopa : fileOpActions )
			toolBar.addSelEnabledButton( fopa );
		toolBar.addSelEnabledButton( emailAction );
		toolBar.addSeparator();
		toolBar.addSelEnabledButton( deleteAction );
		toolBar.addSeparator();
		toolBar.addSeparator();
		toolBar.add( editFiltersAction );
		toolBar.add( analyzeAction );
		toolBar.addSeparator();
		toolBar.add( colSetupAction );
		toolBar.addSeparator();
		final XCheckBox stretchToWindowCheckBox = SettingsGui.createBoundedSettingCheckBox( Settings.REP_LIST_STRETCH_TABLE, Env.APP_SETTINGS,
		        new ActionAdapter( true) {
			        @Override
			        public void actionPerformed( final ActionEvent event ) {
				        table.setAutoResizeMode(
		                        Env.APP_SETTINGS.get( Settings.REP_LIST_STRETCH_TABLE ) ? JTable.AUTO_RESIZE_ALL_COLUMNS : JTable.AUTO_RESIZE_OFF );
			        }
		        } );
		stretchToWindowCheckBox.setText( "Stretch to window" );
		toolBar.add( stretchToWindowCheckBox );
		toolBar.addSeparator();
		toolBar.add( new TipIcon( Tips.REPLAY_LIST_TABLE ) );
		toolBar.addSeparator();
		toolBar.add( SettingsGui.createSettingLink( Settings.NODE_REP_LIST ) );
		toolBar.finalizeLayout();
		
		table.setOpenAction( openAction );
		table.setShiftOpenAction( showInFBrowserAction );
		table.setDeleteAction( deleteAction );
		
		// Create a custom table cell renderer which can color the cells (rows)
		table.setTableCellRenderer( new TableCellRenderer() {
			private final XTableCellRenderer xtcrenderer   = table.getXTableCellRenderer();
														   
			private final XTableModel		 model		   = table.getXTableModel();
														   
			private final Color				 COLOR_VICTORY = new Color( 170, 255, 170 );
														   
			private final Color				 COLOR_DEFEAT  = new Color( 255, 170, 170 );
														   
			private final Color				 COLOR_TIE	   = new Color( 170, 170, 255 );
														   
			@Override
			@SuppressWarnings( "unchecked" )
			public Component getTableCellRendererComponent( final JTable table, final Object value, final boolean isSelected, final boolean hasFocus,
		            final int row, final int column ) {
				xtcrenderer.setBackground( null );
				xtcrenderer.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, column );
				
				if ( !isSelected && Env.APP_SETTINGS.get( Settings.REP_LIST_COLOR_TABLE ) ) {
					switch ( ( (TableIcon< Result >) model.getValueAt( table.convertRowIndexToModel( row ), favResultColIdx ) ).represented ) {
						case VICTORY :
							xtcrenderer.setBackground( COLOR_VICTORY );
							break;
						case DEFEAT :
							xtcrenderer.setBackground( COLOR_DEFEAT );
							break;
						case TIE :
							xtcrenderer.setBackground( COLOR_TIE );
							break;
						default :
							break;
					}
				}
				
				return xtcrenderer;
			}
		} );
		
		addCenter( table.createWrapperBox( true, table.createToolBarParams( this ) ) );
		
		registerTableHotkeys();
	}
	
	/**
	 * Registers the table hotkeys.
	 */
	private void registerTableHotkeys() {
		// TODO
	}
	
	/** Currently running rep counter job. */
	private RepCounterJob repCounterJob;
						  
	/** Currently running rep searcher job. */
	private Job			  repSearcherJob;
						  
	/**
	 * Rebuilds the table.
	 */
	private void rebuildTable() {
		if ( repFolderBean == null ) {
			progressBar.setMaximum( repFiles.length );
			searchReplays();
			return;
		}
		
		progressBar.setIndeterminate( true );
		progressBar.setString( "Counting replays..." );
		
		repCounterJob = new RepCounterJob( repFolderBean );
		repCounterJob.setEdtCallback( new Runnable() {
			@Override
			public void run() {
				progressBar.setIndeterminate( false );
				
				if ( repCounterJob.isCancelRequested() ) {
					progressBar.setAborted();
					return;
				}
				
				final Integer count = repCounterJob.getCount();
				if ( count == null ) {
					progressBar.setString( "ERROR! Could not count replays in folder: " + repFolderBean.getPath() );
					abortButton.setVisible( false );
					return;
				}
				if ( count < 0 ) {
					progressBar.setString( "ERROR! Replay folder does not exist: " + repFolderBean.getPath() );
					abortButton.setVisible( false );
					return;
				}
				progressBar.setMaximum( count );
				repCounterJob = null;
				searchReplays();
			}
		} );
		repCounterJob.start();
	}
	
	/**
	 * Crawls the replay folder and lists replays it founds.
	 */
	private void searchReplays() {
		final XTableModel model = table.getXTableModel();
		
		final IColumn< ? >[] cols = new IColumn[ columns.size() ];
		final List< Class< ? > > columnClassList = new ArrayList< >( columns.size() );
		for ( int i = 0; i < columns.size(); i++ )
			columnClassList.add( ( cols[ i ] = columns.get( i ) ).getColumnClass() );
		model.setColumnClasses( columnClassList );
		
		final long total = progressBar.getMaximum();
		
		final Vector< Vector< Object > > data = new Vector< >( (int) total );
		
		final String searchJobName = repFolderBean == null ? Utils.plural( "Replay Searcher: %s replay%s", repFiles.length )
		        : "Replay Searcher: " + repFolderBean.getPath();
				
		repSearcherJob = new RepCrawlerJob( searchJobName, Icons.F_BLUE_FOLDER_OPEN_TABLE, repFolderBean, repFiles, "search") {
			final boolean		  showTime		 = Env.APP_SETTINGS.get( Settings.REP_LIST_SHOW_TIME_DURING_SEARCH );
												 
			final String		  totalString	 = ";   Total: " + Env.LANG.formatNumber( total ) + "   (";
												 
			final double		  totalHundredth = total / 100.0;
												 
			final AtomicInteger	  added			 = new AtomicInteger();
												 
			final AtomicInteger	  filtered		 = new AtomicInteger();
												 
			final AtomicInteger	  errors		 = new AtomicInteger();
												 
			final ExecutorService es			 = Utils.createExecutorService( getName() );
												 
												 
			@Override
			public void jobRun() {
				maximumProgress.set( (int) total );
				publishStatus( false ); // Publish now (required anyway if total = 0)
				
				super.jobRun();
				
				GuiUtils.runInEDT( new Runnable() {
					@Override
					public void run() {
						publishStatus( true );
						if ( cancelRequested )
							progressBar.setAborted();
						repSearcherJob = null;
						abortButton.setVisible( false );
					}
				} );
			}
			
			@Override
			protected void onEnd() {
				Utils.shutdownExecutorService( es );
				GuiUtils.runInEDT( new Runnable() {
					@Override
					public void run() {
						model.setDataVector( data, columns );
						table.setRowHeight( Env.APP_SETTINGS.get( Settings.REP_LIST_ROW_HEIGHT ) );
						// Hide internal Favored result column:
						table.getColumnModel().removeColumn( table.getColumnModel().getColumn( favResultColIdx ) );
						// Hide the internal path column:
						table.getColumnModel().removeColumn( table.getColumnModel().getColumn( fileColIdx ) );
						
						// Model is set, set custom comparators and default sort orders
						final XTableRowSorter rowSorter = table.getXTableRowSorter();
						for ( int i = 0; i < cols.length; i++ ) {
							if ( cols[ i ].getComparator() != null )
								rowSorter.setComparator( i, cols[ i ].getComparator() );
							rowSorter.setColumnDefaultDesc( i, cols[ i ].isDefaultDesc() );
						}
						
						// Sort by date, descending
						for ( int i = 0; i < columns.size(); i++ )
							if ( DateColumn.class.equals( columns.get( i ).getClass() ) ) {
								table.getRowSorter().setSortKeys( Arrays.asList( new SortKey( i, SortOrder.DESCENDING ) ) );
								break;
							}
						table.pack();
					}
				} );
			}
			
			/** Number of reps that have been enqueued for processing. It is only accessed from the job's executing thread. */
			int enqueued;
			
			@Override
			protected void onFoundRepFile( final Path repFile ) {
				// So situation is the following:
		        // We work with multiple threads, but mayContinue() and waitIfPaused() can only be called from the job's thread.
		        // But we also want to respect the paused state. So we have to call/check that here in the job's thread,
		        // and in the workers thread we have to use guestMayContinue().
		        // Also since enqueuing reps for processing is done really fast, we have to maximize the waiting reps,
		        // else we lose control over paused state (because once all reps are queued, we will not be called anymore).
				while ( mayContinue() && enqueued - currentProgress.get() > 16 && checkedSleep( 1 ) == null )
					;
				if ( cancelRequested )
					return;
				enqueued++;
				
				// Schedule rep for processing.
				es.execute( new Runnable() {
					@Override
					public void run() {
						// We are a "guest" thread, not the job's executing thread:
						if ( !guestMayContinue() )
							return;
							
						final RepProcessor repProc = RepParserEngine.getRepProc( repFile );
						
						if ( repProc == null )
							errors.incrementAndGet();
						else {
							// Check if filters match
							if ( searchEngine != null && !searchEngine.isIncluded( repProc ) )
								filtered.incrementAndGet();
							else {
								final Vector< Object > row = new Vector< >( cols.length );
								for ( final IColumn< ? > col : cols )
									row.add( col.getData( repProc ) );
								data.add( row );
								added.incrementAndGet();
							}
						}
						currentProgress.set( added.get() + filtered.get() + errors.get() );
						publishStatus( false );
					}
				} );
			}
			
			private void publishStatus( final boolean ended ) {
				GuiUtils.runInEDT( new Runnable() {
					@Override
					public void run() {
						final int processed = currentProgress.get();
						
						final StringBuilder sb = new StringBuilder( 80 );
						sb.append( "Added: " ).append( Env.LANG.formatNumber( added.get() ) ).append( ";   Filtered out: " )
		                        .append( Env.LANG.formatNumber( filtered.get() ) ).append( ";   Errors: " ).append( Env.LANG.formatNumber( errors.get() ) )
		                        .append( totalString ).append( Env.LANG.formatNumber( total == 0 ? 0 : processed / totalHundredth, 2 ) ).append( "%)" );
								
						if ( showTime && !ended ) {
							final long time = getExecTimeMs();
							sb.append( "   [Time: " ).append( DurationFormat.AUTO.formatDuration( time ) ).append( ";  ETA: " )
		                            .append( processed == 0 ? "- " : DurationFormat.AUTO.formatDuration( time * ( total - processed ) / processed ) )
		                            .append( ']' );
						}
						
						progressBar.setString( sb.toString() );
						
						progressBar.setValue( processed );
					}
				} );
			}
		};
		repSearcherJob.start();
	}
	
	@Override
	public boolean pageClosing() {
		// Take care of running jobs...
		abortButton.doClick( 0 );
		
		return true;
	}
	
}
