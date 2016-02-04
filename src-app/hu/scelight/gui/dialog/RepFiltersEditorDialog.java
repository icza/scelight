/*
 * Project Scelight
 * 
 * Copyright (c) 2013 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package hu.scelight.gui.dialog;

import hu.belicza.andras.util.VersionView;
import hu.scelight.Scelight;
import hu.scelight.bean.repfilters.RepFilterBean;
import hu.scelight.bean.repfilters.RepFiltersBean;
import hu.scelight.bean.repfolders.RepFolderBean;
import hu.scelight.bean.repfolders.RepFoldersBean;
import hu.scelight.gui.help.Helps;
import hu.scelight.gui.icon.Icons;
import hu.scelight.gui.setting.SettingsGui;
import hu.scelight.search.Connection;
import hu.scelight.search.FilterBy;
import hu.scelight.search.FilterByGroup;
import hu.scelight.search.Operator;
import hu.scelight.search.RepSearchEngine;
import hu.scelight.service.env.Env;
import hu.scelight.service.settings.Settings;
import hu.scelight.util.Utils;
import hu.scelight.util.gui.GuiUtils;
import hu.scelightapi.bean.repfilters.IRepFilterBean;
import hu.scelightapi.bean.repfilters.IRepFiltersBean;
import hu.scelightapi.gui.dialog.IRepFiltersEditorDialog;
import hu.sllauncher.action.XAction;
import hu.sllauncher.gui.comp.Browser;
import hu.sllauncher.gui.comp.HelpIcon;
import hu.sllauncher.gui.comp.Link;
import hu.sllauncher.gui.comp.ToolBarForTable;
import hu.sllauncher.gui.comp.XButton;
import hu.sllauncher.gui.comp.XDialog;
import hu.sllauncher.gui.comp.XFileChooser;
import hu.sllauncher.gui.comp.XLabel;
import hu.sllauncher.gui.comp.XScrollPane;
import hu.sllauncher.gui.comp.XSplitPane;
import hu.sllauncher.gui.comp.XTextField;
import hu.sllauncher.gui.comp.combobox.XComboBox;
import hu.sllauncher.gui.comp.table.XTable;
import hu.sllauncher.gui.comp.table.XTableModel;
import hu.sllauncher.gui.comp.table.editor.BoolCellEditor;
import hu.sllauncher.gui.comp.table.editor.DateCellEditor;
import hu.sllauncher.gui.comp.table.editor.IntegerCellEditor;
import hu.sllauncher.gui.comp.table.renderer.XTableCellRenderer;
import hu.sllauncher.util.SkillLevel;
import hu.sllauncher.util.gui.adapter.ActionAdapter;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultCellEditor;
import javax.swing.JComponent;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.xml.bind.JAXB;

/**
 * A dialog to view / edit the attached Replay filters of a Replay folder or a custom replay filters.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class RepFiltersEditorDialog extends XDialog implements IRepFiltersEditorDialog {
	
	/** Extension of the files holding replay filters. */
	private static final String         REP_FILTERS_FILE_EXT   = "slfilt";
	
	/** Default folder to save to / load from replay filters. */
	private static final Path           SAVE_FOLDER            = Env.PATH_WORKSPACE.resolve( "rep-filters" );
	static {
		if ( !Files.exists( SAVE_FOLDER ) )
			try {
				Files.createDirectories( SAVE_FOLDER );
			} catch ( final IOException ie ) {
				Env.LOGGER.error( "Could not create folder: " + SAVE_FOLDER, ie );
			}
	}
	
	
	
	/** Enable selected filters. */
	private final XAction               enableAction           = new XAction( Icons.F_TICK, "Enable Selected Filters", cp ) {
		                                                           @Override
		                                                           public void actionPerformed( final ActionEvent event ) {
			                                                           final int[] indices = table.getSelectedModelRows();
			                                                           
			                                                           final List< ? extends IRepFilterBean > filterList = repFiltersBean
			                                                                   .getRepFilterBeanList();
			                                                           for ( final int idx : indices )
				                                                           filterList.get( idx ).setEnabled( Boolean.TRUE );
			                                                           
			                                                           rebuildTable();
		                                                           }
	                                                           };
	
	/** Disable selected filters. */
	private final XAction               disableAction          = new XAction( Icons.F_TICK_RED, "Disable Selected Filters", cp ) {
		                                                           @Override
		                                                           public void actionPerformed( final ActionEvent event ) {
			                                                           final int[] indices = table.getSelectedModelRows();
			                                                           
			                                                           final List< ? extends IRepFilterBean > filterList = repFiltersBean
			                                                                   .getRepFilterBeanList();
			                                                           for ( final int idx : indices )
				                                                           filterList.get( idx ).setEnabled( Boolean.FALSE );
			                                                           
			                                                           rebuildTable();
		                                                           }
	                                                           };
	
	/** Moves up the selected filters. */
	private final XAction               moveUpAction           = new XAction( Icons.F_ARROW_090, "Move Up Selected Filters", cp ) {
		                                                           @Override
		                                                           public void actionPerformed( final ActionEvent event ) {
			                                                           Utils.moveBackward( repFiltersBean.getRepFilterBeanList(), table.getSelectedModelRows() );
			                                                           rebuildTable();
		                                                           }
	                                                           };
	
	/** Moves down the selected filters. */
	private final XAction               moveDownAction         = new XAction( Icons.F_ARROW_270, "Move Down Selected Filters", cp ) {
		                                                           @Override
		                                                           public void actionPerformed( final ActionEvent event ) {
			                                                           Utils.moveForward( repFiltersBean.getRepFilterBeanList(), table.getSelectedModelRows() );
			                                                           rebuildTable();
		                                                           }
	                                                           };
	
	/** Removes Selected Filters. */
	private final XAction               removeAction           = new XAction( Icons.F_FUNNEL_MINUS, "Remove Selected Filters... (Delelte)", cp ) {
		                                                           @Override
		                                                           public void actionPerformed( final ActionEvent event ) {
			                                                           final int[] selectedRows = table.getSelectedModelRows();
			                                                           if ( !GuiUtils.confirm( Utils.plural(
			                                                                   "Are you sure you want to remove %s Replay Filter%s?", selectedRows.length ) ) )
				                                                           return;
			                                                           
			                                                           // Sort rows and remove going downward
			                                                           Arrays.sort( selectedRows );
			                                                           final List< ? extends IRepFilterBean > filterList = repFiltersBean
			                                                                   .getRepFilterBeanList();
			                                                           for ( int i = selectedRows.length - 1; i >= 0; i-- )
				                                                           filterList.remove( selectedRows[ i ] );
			                                                           
			                                                           rebuildTable();
		                                                           };
	                                                           };
	
	/** Adds a new filter. */
	private final XAction               addNewAction           = new XAction( Icons.F_FUNNEL_PLUS, "Add New Filter", cp ) {
		                                                           @Override
		                                                           @SuppressWarnings( "unchecked" )
		                                                           public void actionPerformed( final ActionEvent event ) {
			                                                           // TODO comment why safe to ignore warning (is it safe?)
			                                                           ( (List< IRepFilterBean >) repFiltersBean.getRepFilterBeanList() ).add( RepFilterBean
			                                                                   .createNewFilter() );
			                                                           rebuildTable();
		                                                           };
	                                                           };
	
	/** Saves the replay filters to a file. */
	private final XAction               saveAction             = new XAction( Icons.F_DISK, "Save Replay Filters to a File...", cp ) {
		                                                           @Override
		                                                           public void actionPerformed( final ActionEvent event ) {
			                                                           final XFileChooser fc = new XFileChooser( SAVE_FOLDER );
			                                                           fc.setDialogTitle( "Choose a file to save Replay Filters" );
			                                                           fc.setFileFilter( new FileNameExtensionFilter( "Replay Filter files (*."
			                                                                   + REP_FILTERS_FILE_EXT + ')', REP_FILTERS_FILE_EXT ) );
			                                                           
			                                                           // Suggested file name
			                                                           fc.setSelectedFile( Utils.uniqueFile(
			                                                                   SAVE_FOLDER.resolve( "replay-filters." + REP_FILTERS_FILE_EXT ) ).toFile() );
			                                                           
			                                                           if ( XFileChooser.APPROVE_OPTION != fc.showSaveDialog( RepFiltersEditorDialog.this ) )
				                                                           return;
			                                                           Path path = fc.getSelectedPath();
			                                                           // Ensure proper extension
			                                                           if ( !path.getFileName().toString().toLowerCase().endsWith( '.' + REP_FILTERS_FILE_EXT ) )
				                                                           path = path.resolveSibling( path.getFileName().toString() + '.'
				                                                                   + REP_FILTERS_FILE_EXT );
			                                                           if ( Files.exists( path ) )
				                                                           if ( !GuiUtils.confirm( "The selected file already exists?", "Overwrite it?" ) )
					                                                           return;
			                                                           
			                                                           try {
				                                                           JAXB.marshal( repFiltersBean, path.toFile() );
			                                                           } catch ( final Exception e ) {
				                                                           Env.LOGGER.error( "Failed to write to file: " + path, e );
				                                                           GuiUtils.showErrorMsg( "Failed to write to file:", path );
			                                                           }
		                                                           }
	                                                           };
	
	/** Loads replay filters from a file. */
	private final XAction               loadAction             = new XAction( Icons.F_FOLDER_OPEN, "Load Replay Filters from a File...", cp ) {
		                                                           @Override
		                                                           public void actionPerformed( final ActionEvent event ) {
			                                                           final XFileChooser fc = new XFileChooser( SAVE_FOLDER );
			                                                           fc.setDialogTitle( "Choose a file to load Replay Filters from" );
			                                                           fc.setFileFilter( new FileNameExtensionFilter( "Replay Filter files (*."
			                                                                   + REP_FILTERS_FILE_EXT + ")", REP_FILTERS_FILE_EXT ) );
			                                                           
			                                                           if ( XFileChooser.APPROVE_OPTION != fc.showOpenDialog( RepFiltersEditorDialog.this ) )
				                                                           return;
			                                                           
			                                                           try {
				                                                           repFiltersBean = JAXB
				                                                                   .unmarshal( fc.getSelectedPath().toFile(), RepFiltersBean.class );
				                                                           if ( rfBean != null )
					                                                           rfBean.setRepFiltersBean( (RepFiltersBean) repFiltersBean );
			                                                           } catch ( final Exception e ) {
				                                                           Env.LOGGER.error(
				                                                                   "Failed to load Replay filters from file: " + fc.getSelectedPath(), e );
				                                                           GuiUtils.showErrorMsg( "Failed to load Replay filters from file file:",
				                                                                   fc.getSelectedPath() );
				                                                           return;
			                                                           }
			                                                           rebuildTable();
		                                                           }
	                                                           };
	
	
	
	
	/** Operator editor combo box. */
	private final XComboBox< Operator > operatorEditorComboBox = new XComboBox<>();
	
	/** Table cell editor used to edit operator cells. */
	private final TableCellEditor       operatorCellEditor     = new DefaultCellEditor( operatorEditorComboBox );
	
	/** Table displaying the defined/added filters. */
	private final XTable                table                  = new XTable() {
		                                                           @Override
		                                                           public TableCellEditor getCellEditor( final int row, final int column ) {
			                                                           // row and column indices are VIEW indices
			                                                           final int modelColumn = convertColumnIndexToModel( column );
			                                                           if ( modelColumn == filterByColIdx || modelColumn == operatorColIdx
			                                                                   || modelColumn == valueColIdx )
				                                                           return getCustomCellEditor( row, column );
			                                                           return super.getCellEditor( row, column );
		                                                           }
	                                                           };
	
	/** Replay folders bean holding the replay folder whose attached filters are edited (must be a cloned instance). */
	private final RepFoldersBean        rfsBean;
	
	/** Replay folder bean whose attached filters to be displayed / edited. */
	private final RepFolderBean         rfBean;
	
	/** The edited replay filters. */
	private IRepFiltersBean             repFiltersBean;
	
	/** Tells if the editor was closed with the OK button. */
	private boolean                     ok;
	
	
	/** {@link RepFilterBean} column model index. */
	private int                         beanColIdx;
	
	/** Connection column model index. */
	private int                         connectionColIdx;
	
	/** Enabled column model index. */
	private int                         enabledColIdx;
	
	/** Filter by group column model index. */
	private int                         filterByGroupColIdx;
	
	/** Filter by column model index. */
	private int                         filterByColIdx;
	
	/** Operator column model index. */
	private int                         operatorColIdx;
	
	/** Value column model index. */
	private int                         valueColIdx;
	
	/** Comment column model index. */
	private int                         commentColIdx;
	
	
	/** Browser to show an HTML preview of the filters structure. */
	private final Browser               previewBrowser         = new Browser();
	
	
	
	/**
	 * Creates a new {@link RepFiltersEditorDialog}.
	 */
	public RepFiltersEditorDialog() {
		this( null );
	}
	
	/**
	 * Creates a new {@link RepFiltersEditorDialog}.
	 * 
	 * @param customRepFiltersBean custom replay filters bean to edit
	 */
	public RepFiltersEditorDialog( final IRepFiltersBean customRepFiltersBean ) {
		this( customRepFiltersBean, null, null );
	}
	
	/**
	 * Creates a new {@link RepFiltersEditorDialog}.
	 * 
	 * <p>
	 * The editor will display / edit the attached filters of the specified replay folder.
	 * </p>
	 * 
	 * @param rfsBean replay folders bean holding the replay folder whose attached filters are edited (must be a cloned instance)
	 * @param rfBean replay folder bean whose attached filters to be displayed / edited
	 * 
	 * @throws IllegalArgumentException if one and only one of <code>rfsBean</code> and <code>rfBean</code> is <code>null</code>
	 */
	public RepFiltersEditorDialog( final RepFoldersBean rfsBean, final RepFolderBean rfBean ) {
		this( null, rfsBean, rfBean );
	}
	
	/**
	 * Creates a new {@link RepFiltersEditorDialog}.
	 * 
	 * <p>
	 * If <code>customRepFiltersBean</code> is <code>null</code>, the editor will display / edit the attached filters of the specified replay folder.
	 * </p>
	 * 
	 * @param customRepFiltersBean custom replay filters bean to edit
	 * @param rfsBean replay folders bean holding the replay folder whose attached filters are edited (must be a cloned instance)
	 * @param rfBean replay folder bean whose attached filters to be displayed / edited
	 * 
	 */
	private RepFiltersEditorDialog( final IRepFiltersBean customRepFiltersBean, final RepFoldersBean rfsBean, final RepFolderBean rfBean ) {
		super( Env.MAIN_FRAME );
		
		if ( rfsBean == null ^ rfBean == null )
			throw new IllegalArgumentException( "Either both or none of the parameters must be null!" );
		
		this.rfsBean = rfsBean;
		this.rfBean = rfBean;
		if ( rfBean == null ) {
			repFiltersBean = customRepFiltersBean == null ? new RepFiltersBean() : customRepFiltersBean;
			setTitle( "Custom Replay Filters" );
		} else {
			if ( rfBean.getRepFiltersBean() == null )
				rfBean.setRepFiltersBean( new RepFiltersBean() );
			repFiltersBean = rfBean.getRepFiltersBean();
			setTitle( "Attached Replay Filters" + ( rfBean == null ? "" : " - " + rfBean.getPath() ) );
		}
		
		setIconImage( Icons.F_FUNNEL_PENCIL.get().getImage() );
		
		buildGui();
		
		restoreDefaultSize();
		
		setVisible( true );
	}
	
	/**
	 * Builds the GUI of the editor dialog.
	 */
	private void buildGui() {
		final ToolBarForTable toolBar = new ToolBarForTable( table );
		toolBar.addSelectInfoLabel( "Select a Filter." );
		toolBar.addSelEnabledButton( enableAction );
		toolBar.addSelEnabledButton( disableAction );
		toolBar.addSelEnabledButton( moveUpAction );
		toolBar.addSelEnabledButton( moveDownAction );
		toolBar.addSeparator();
		toolBar.addSelEnabledButton( removeAction );
		toolBar.addSeparator();
		toolBar.add( addNewAction );
		toolBar.addSeparator();
		toolBar.add( saveAction );
		toolBar.add( loadAction );
		toolBar.addSeparator();
		toolBar.add( new XLabel( "General" ).leftBorder( 20 ) );
		toolBar.add( new HelpIcon( Helps.REPLAY_FILTERS ).leftBorder( 2 ) );
		toolBar.addSeparator();
		toolBar.add( new XLabel( Helps.PLAYER_FILTERS.title ) );
		toolBar.add( new HelpIcon( Helps.PLAYER_FILTERS ).leftBorder( 2 ) );
		toolBar.addSeparator();
		JComponent comp;
		toolBar.add( comp = new Link( "Regexp", Utils.createUrl( "http://docs.oracle.com/javase/7/docs/api/java/util/regex/Pattern.html#sum" ) ) );
		SettingsGui.bindVisibilityToSkillLevel( comp, SkillLevel.NORMAL );
		toolBar.add( comp = new HelpIcon( Helps.REGULAR_EXPRESSIONS ).leftBorder( 2 ) );
		SettingsGui.bindVisibilityToSkillLevel( comp, SkillLevel.NORMAL );
		toolBar.finalizeLayout();
		cp.addNorth( toolBar );
		
		final XSplitPane splitPane = new XSplitPane( JSplitPane.VERTICAL_SPLIT );
		table.setRowHeight( 24 );
		table.setDeleteAction( removeAction );
		( (DefaultCellEditor) table.getDefaultEditor( String.class ) ).setClickCountToStart( 1 );
		splitPane.setTopComponent( table.createWrapperBox( true ) );
		splitPane.setBottomComponent( new XScrollPane( previewBrowser ) );
		splitPane.setResizeWeight( 0.6 );
		cp.addCenter( splitPane );
		
		final XButton okButton = new XButton( "_OK" );
		okButton.addActionListener( new ActionAdapter() {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				// Clear filters bean if no filter remained
				if ( repFiltersBean.getRepFilterBeanList().isEmpty() ) {
					repFiltersBean = null;
					if ( rfBean != null )
						rfBean.setRepFiltersBean( null );
				}
				
				ok = true;
				close();
				
				if ( rfsBean != null ) {
					Env.APP_SETTINGS.set( Settings.REPLAY_FOLDERS_BEAN, rfsBean );
					// Save settings.
					Scelight.INSTANCE().saveSettings();
				}
			}
		} );
		getButtonsPanel().add( okButton );
		getRootPane().setDefaultButton( okButton );
		getButtonsPanel().add( okButton );
		addCloseButton( "_Cancel" );
		
		// Table header
		final XTableModel model = table.getXTableModel();
		
		final Vector< String > columns = Utils.asNewVector( "Bean", "Enabled?", "Connection", "Filter by Group", "Filter by", "Operator", "Value", "Comment" );
		final List< Class< ? > > columnClasses = Utils.< Class< ? > > asNewList( RepFilterBean.class, Boolean.class, Connection.class, FilterByGroup.class,
		        FilterBy.class, Operator.class, Object.class, String.class );
		
		// Custom cell renderer to indicate errors with red background
		table.setTableCellRenderer( new TableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent( final JTable table, final Object value, final boolean isSelected, final boolean hasFocus,
			        final int row, final int column ) {
				final XTableCellRenderer xTableCellRenderer = RepFiltersEditorDialog.this.table.getXTableCellRenderer();
				xTableCellRenderer.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, column );
				
				boolean error = false;
				final int col = table.convertColumnIndexToModel( column );
				if ( col != commentColIdx && value == null )
					error = true;
				else {
					final RepFilterBean filterBean = (RepFilterBean) model.getValueAt( table.convertRowIndexToModel( row ), beanColIdx );
					final FilterByGroup fbg = filterBean.getFilterByGroup();
					final FilterBy fb = filterBean.getFilterBy();
					final Operator op = filterBean.getOperator();
					if ( col == valueColIdx ) {
						if ( filterBean.getValue() == null )
							error = true;
					} else if ( col == operatorColIdx ) {
						if ( fb == null || !Utils.contains( fb.operators, op ) )
							error = true;
					} else if ( col == filterByColIdx ) {
						if ( fbg == null || !Utils.contains( fbg.filterBys, fb ) )
							error = true;
					}
				}
				
				if ( !isSelected ) // If selected, this colors is "overridden" (set)
					xTableCellRenderer.setBackground( null );
				if ( error )
					xTableCellRenderer.setBackground( XTextField.ERROR_COLOR );
				
				return xTableCellRenderer;
			}
		} );
		
		beanColIdx = 0;
		enabledColIdx = 1;
		connectionColIdx = 2;
		filterByGroupColIdx = 3;
		filterByColIdx = 4;
		operatorColIdx = 5;
		valueColIdx = 6;
		commentColIdx = 7;
		
		model.setColumnIdentifiers( columns );
		model.setColumnClasses( columnClasses );
		
		final XComboBox< Connection > connectionEditorComboBox = new XComboBox<>( Connection.VALUES );
		table.getColumnModel().getColumn( connectionColIdx ).setCellEditor( new DefaultCellEditor( connectionEditorComboBox ) );
		// Pack connection column by hand (due to editor is wider than header and elements)
		table.getColumnModel().getColumn( table.convertColumnIndexToView( connectionColIdx ) )
		        .setPreferredWidth( connectionEditorComboBox.getPreferredSize().width + 3 );
		table.getColumnModel().getColumn( table.convertColumnIndexToView( connectionColIdx ) )
		        .setMaxWidth( connectionEditorComboBox.getPreferredSize().width + 3 );
		
		final XComboBox< FilterByGroup > filterByGroupEditorComboBox = new XComboBox<>( FilterByGroup.VALUES );
		table.getColumnModel().getColumn( filterByGroupColIdx ).setCellEditor( new DefaultCellEditor( filterByGroupEditorComboBox ) );
		// Pack connection column by hand (due to editor is wider than header and elements)
		table.getColumnModel().getColumn( table.convertColumnIndexToView( filterByGroupColIdx ) )
		        .setPreferredWidth( filterByGroupEditorComboBox.getPreferredSize().width + 3 );
		table.getColumnModel().getColumn( table.convertColumnIndexToView( filterByGroupColIdx ) )
		        .setMaxWidth( filterByGroupEditorComboBox.getPreferredSize().width + 3 );
		
		model.addTableModelListener( new TableModelListener() {
			@Override
			public void tableChanged( final TableModelEvent event ) {
				if ( !table.isEditing() )
					return;
				
				// Model event returns model indices
				final int col = event.getColumn();
				final int row = event.getFirstRow();
				if ( col < 0 ) // Experienced this sometimes if saved filters are loaded which contain invalid enum values...
					return;
				final Object value = model.getValueAt( row, col );
				
				final RepFilterBean filterBean = (RepFilterBean) model.getValueAt( row, beanColIdx );
				if ( col == enabledColIdx ) {
					filterBean.setEnabled( (Boolean) value );
					rebuildPreview();
				} else if ( col == connectionColIdx ) {
					filterBean.setConnection( (Connection) value );
					rebuildPreview();
				} else if ( col == filterByGroupColIdx ) {
					
					// Only proceed if filter by group was really changed
					final FilterByGroup oldFbg = filterBean.getFilterByGroup();
					if ( oldFbg == null || value == null || oldFbg != value ) { // Filter by group is an enum...
						filterBean.setFilterByGroup( (FilterByGroup) value );
						// If the new group's filter bys is not the same as the old group's,
						// also reset filter by and operator (use the first supported op of the first filter by)
						// and value (most likely invalid for another filter by)
						if ( oldFbg == null || value == null || oldFbg.filterBys != filterBean.getFilterByGroup().filterBys ) {
							filterBean.setFilterBy( filterBean.getFilterByGroup() == null ? null : filterBean.getFilterByGroup().filterBys[ 0 ] );
							filterBean.setOperator( filterBean.getFilterBy() == null ? null : filterBean.getFilterBy().operators[ 0 ] );
							filterBean.setValue( null );
						}
						
						// Rebuild table (model.fireTableRowsUpdated() is not enough, model is not updated only the Bean!)
						// Also do this "later" else ArrayIndexOutOfBoundsException occurs (due to the hidden bean column).
						SwingUtilities.invokeLater( new Runnable() {
							@Override
							public void run() {
								rebuildTable();
							}
						} );
					}
					
				} else if ( col == filterByColIdx ) {
					
					// Only proceed if filter by was really changed:
					if ( filterBean.getFilterBy() != value ) { // Filter by is an enum...
						filterBean.setFilterBy( (FilterBy) value );
						// Also reset operator (use the first supported op of the filter by)
						// and value (most likely invalid for another filter by)
						filterBean.setOperator( filterBean.getFilterBy() == null ? null : filterBean.getFilterBy().operators[ 0 ] );
						filterBean.setValue( null );
						// Rebuild table (model.fireTableRowsUpdated() is not enough, model is not updated only the Bean!)
						// Also do this "later" else ArrayIndexOutOfBoundsException occurs (due to the hidden bean column).
						SwingUtilities.invokeLater( new Runnable() {
							@Override
							public void run() {
								rebuildTable();
							}
						} );
					}
					
				} else if ( col == operatorColIdx ) {
					filterBean.setOperator( (Operator) value );
					rebuildPreview();
				} else if ( col == valueColIdx ) {
					if ( filterBean.getFilterBy() == FilterBy.VERSION ) {
						// Remove all non-digits except the dot (leave only numbers and the dot):
						if ( value instanceof String ) // Came from the text editor
							filterBean.setValue( VersionView.fromString( ( (String) value ).replaceAll( "[^0-9\\.]", "" ) ) );
						else
							filterBean.setValue( value ); // Might be null or might be an unedited VersionView
						// Also set this optionally modified value to the table model (but of course "later"):
						SwingUtilities.invokeLater( new Runnable() {
							@Override
							public void run() {
								model.setValueAt( filterBean.getValue(), row, col );
							}
						} );
					} else
						filterBean.setValue( value );
					rebuildPreview();
				} else if ( col == commentColIdx ) {
					filterBean.setComment( (String) value );
				}
			}
		} );
		
		table.getColumnModel().removeColumn( table.getColumnModel().getColumn( beanColIdx ) );
		
		table.setSortable( false );
		table.setEditable( true );
		
		rebuildTable();
	}
	
	/**
	 * Rebuilds the table.
	 */
	private void rebuildTable() {
		table.saveSelection( beanColIdx );
		
		final XTableModel model = table.getXTableModel();
		
		model.getDataVector().clear();
		model.fireTableDataChanged();
		
		for ( final IRepFilterBean filterBean : repFiltersBean.getRepFilterBeanList() ) {
			final Vector< Object > row = Utils.< Object > asNewVector( filterBean, filterBean.getEnabled(), filterBean.getConnection(),
			        filterBean.getFilterByGroup(), filterBean.getFilterBy(), filterBean.getOperator(), filterBean.getValue(), filterBean.getComment() );
			model.addRow( row );
		}
		
		table.packColumns( table.convertColumnIndexToView( enabledColIdx ) );
		// Filter by group and Connection cols are already manually packed.
		
		table.restoreSelection( beanColIdx );
		
		rebuildPreview();
	}
	
	/**
	 * Rebuilds the filters preview HTML table.
	 */
	private void rebuildPreview() {
		final StringBuilder sb = new StringBuilder();
		
		sb.append( "<html><head><style>" );
		sb.append( ".conn{font-weight:bold}" );
		sb.append( ".fbg{color:#777777}" );
		sb.append( ".fby{color:blue;font-style:italic}" );
		sb.append( ".op{font-weight:bold}" );
		sb.append( ".fval{}" );
		sb.append( "</style></head><body><b>Filters Structure Preview:</b><br>" );
		new RepSearchEngine( repFiltersBean ).getStructure( sb );
		sb.append( "</body></html>" );
		
		previewBrowser.setText( sb.toString() );
		previewBrowser.setCaretPosition( 0 );
	}
	
	
	/** General value cell editor. */
	private final DefaultCellEditor   generalValueCellEditor  = new DefaultCellEditor( new XTextField() );
	{
		generalValueCellEditor.setClickCountToStart( 1 );
	}
	
	/** Combo box value editor for filter by column and for filter by's having an enum type. */
	private final XComboBox< Object > valueEditorComboBox     = new XComboBox<>();
	{
		// Allow all player filter bys to be visible:
		valueEditorComboBox.setMaximumRowCount( FilterBy.PLAYER_VALUES.length );
	}
	
	/** Value cell editor for filter by column and for filter by's having an enum type. */
	private final DefaultCellEditor   valueComboBoxCellEditor = new DefaultCellEditor( valueEditorComboBox );
	
	/**
	 * Returns a custom cell editor for the filter by, operator and value columns.
	 * 
	 * @param row row view index to return an editor for
	 * @param column column view index to return an editor for
	 * @return a custom cell editor for the filter by, operator and value columns
	 */
	private TableCellEditor getCustomCellEditor( final int row, final int column ) {
		// row and column indices are VIEW indices
		final int modelColumn = table.convertColumnIndexToModel( column );
		
		final FilterBy filterBy = (FilterBy) table.getModel().getValueAt( table.convertRowIndexToModel( row ), filterByColIdx );
		
		if ( modelColumn == filterByColIdx ) {
			
			final FilterByGroup filterByGroup = (FilterByGroup) table.getModel().getValueAt( table.convertRowIndexToModel( row ), filterByGroupColIdx );
			
			valueEditorComboBox.removeAllItems();
			if ( filterByGroup != null )
				for ( final FilterBy o : filterByGroup.filterBys )
					valueEditorComboBox.addItem( o );
			
			return valueComboBoxCellEditor;
			
		} else if ( modelColumn == operatorColIdx ) {
			
			// Only offer operators supported by the row's filter by
			operatorEditorComboBox.removeAllItems();
			
			if ( filterBy != null )
				for ( final Operator op : filterBy.operators )
					operatorEditorComboBox.addItem( op );
			
			return operatorCellEditor;
			
		} else if ( modelColumn == valueColIdx ) {
			
			if ( filterBy == null || filterBy.type.isEnum() ) {
				valueEditorComboBox.removeAllItems();
				if ( filterBy != null )
					for ( final Object o : filterBy.type.getEnumConstants() )
						valueEditorComboBox.addItem( o );
				return valueComboBoxCellEditor;
			} else if ( filterBy.type == Date.class ) {
				return DateCellEditor.INSTANCE;
			} else if ( filterBy.type == Integer.class ) {
				return IntegerCellEditor.INSTANCE;
			} else if ( filterBy.type == Boolean.class ) {
				return BoolCellEditor.INSTANCE;
			} else
				return generalValueCellEditor;
			
		} else
			return null;
	}
	
	@Override
	public boolean isOk() {
		return ok;
	}
	
	@Override
	public IRepFiltersBean getRepFiltersBean() {
		return repFiltersBean;
	}
	
}
