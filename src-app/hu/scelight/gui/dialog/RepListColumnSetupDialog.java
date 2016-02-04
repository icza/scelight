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

import hu.scelight.bean.RepListColumnsBean;
import hu.scelight.gui.icon.Icons;
import hu.scelight.gui.page.replist.column.IColumn;
import hu.scelight.gui.page.replist.column.RepListColumnRegistry;
import hu.scelight.gui.page.replist.column.impl.BaseCustomColumn;
import hu.scelight.gui.page.replist.column.impl.DateColumn;
import hu.scelight.gui.setting.SettingsGui;
import hu.scelight.service.env.Env;
import hu.scelight.service.settings.Settings;
import hu.scelight.util.Utils;
import hu.sllauncher.action.XAction;
import hu.sllauncher.gui.comp.Link;
import hu.sllauncher.gui.comp.ToolBarForTable;
import hu.sllauncher.gui.comp.XButton;
import hu.sllauncher.gui.comp.XDialog;
import hu.sllauncher.gui.comp.table.XTable;
import hu.sllauncher.gui.comp.table.XTableModel;
import hu.sllauncher.gui.icon.LRIcon;
import hu.sllauncher.util.gui.adapter.ActionAdapter;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

import javax.swing.RowSorter.SortKey;
import javax.swing.SortOrder;
import javax.swing.table.TableModel;

/**
 * A dialog to view / edit the column setup of the replay list tables.
 * 
 * @author Andras Belicza
 */
@SuppressWarnings( "serial" )
public class RepListColumnSetupDialog extends XDialog {
	
	/** Shows the selected columns. */
	private final XAction showAction     = new XAction( Icons.F_EYE, "Show Selected Columns", cp ) {
		                                     @Override
		                                     public void actionPerformed( final ActionEvent event ) {
			                                     final TableModel model = table.getModel();
			                                     
			                                     for ( final int row : table.getSelectedModelRows() ) {
				                                     @SuppressWarnings( "unchecked" )
				                                     final Class< IColumn< ? > > colClass = (Class< IColumn< ? > >) model.getValueAt( row, 0 );
				                                     if ( !rlcBean.getColumnClassList().contains( colClass ) )
					                                     rlcBean.getColumnClassList().add( colClass );
			                                     }
			                                     
			                                     rebuildTable();
		                                     }
	                                     };
	
	/** Hides the selected columns. */
	private final XAction hideAction     = new XAction( Icons.F_EYE_CLOSE, "Hide Selected Columns", cp ) {
		                                     @Override
		                                     public void actionPerformed( final ActionEvent event ) {
			                                     final TableModel model = table.getModel();
			                                     
			                                     for ( final int row : table.getSelectedModelRows() ) {
				                                     @SuppressWarnings( "unchecked" )
				                                     final Class< IColumn< ? > > colClass = (Class< IColumn< ? > >) model.getValueAt( row, 0 );
				                                     // Date column is mandatory, do not hide it!
				                                     if ( !DateColumn.class.equals( colClass ) )
					                                     rlcBean.getColumnClassList().remove( colClass );
			                                     }
			                                     
			                                     rebuildTable();
		                                     }
	                                     };
	
	/** Moves up the selected columns. */
	private final XAction moveUpAction   = new XAction( Icons.F_ARROW_090, "Move Up Selected Columns (Decrement Position)", cp ) {
		                                     @Override
		                                     public void actionPerformed( final ActionEvent event ) {
			                                     Utils.moveBackward( rlcBean.getColumnClassList(), getSelectedVisibleModelIndices() );
			                                     rebuildTable();
		                                     }
	                                     };
	
	/** Moves down the selected columns. */
	private final XAction moveDownAction = new XAction( Icons.F_ARROW_270, "Move Down Selected Columns (Increment Position)", cp ) {
		                                     @Override
		                                     public void actionPerformed( final ActionEvent event ) {
			                                     Utils.moveForward( rlcBean.getColumnClassList(), getSelectedVisibleModelIndices() );
			                                     rebuildTable();
		                                     }
	                                     };
	
	/** Restore default columns. */
	private final XAction restoreAction  = new XAction( Icons.F_ARROW_RETURN_180_LEFT, "Restore Default Columns", cp ) {
		                                     @Override
		                                     public void actionPerformed( final ActionEvent event ) {
			                                     rlcBean = Settings.REP_LIST_COLUMNS_BEAN.defaultValue.cloneBean();
			                                     rebuildTable();
		                                     }
	                                     };
	
	
	/**
	 * Returns the indices (pos-1) of the selected visible columns
	 * 
	 * @return the indices (pos-1) of the selected visible columns
	 */
	private int[] getSelectedVisibleModelIndices() {
		final TableModel model = table.getModel();
		
		// Only visible columns can be moved
		final List< Integer > colIdxList = new ArrayList<>();
		for ( final int row : table.getSelectedModelRows() ) {
			final int pos = rlcBean.getColumnClassList().indexOf( model.getValueAt( row, 0 ) );
			if ( pos >= 0 )
				colIdxList.add( pos );
		}
		
		final int[] result = new int[ colIdxList.size() ];
		
		for ( int i = result.length - 1; i >= 0; i-- )
			result[ i ] = colIdxList.get( i );
		
		return result;
	}
	
	
	/** The edited column setup bean instance. */
	private RepListColumnsBean rlcBean = Env.APP_SETTINGS.get( Settings.REP_LIST_COLUMNS_BEAN ).cloneBean();
	
	/** Table displaying the registered columns. */
	private final XTable       table   = new XTable();
	
	/** Model index of the class column. */
	private int                classColIdx;
	
	/** Model index of the position column. */
	private int                posColIdx;
	
	/** Model index of the description column. */
	private int                descColIdx;
	
	
	/**
	 * Creates a new {@link RepListColumnSetupDialog}.
	 */
	public RepListColumnSetupDialog() {
		super( Env.MAIN_FRAME );
		
		setTitle( "Replay List Column Setup" );
		setIconImage( Icons.F_EDIT_COLUMN.get().getImage() );
		
		buildGui();
		
		restoreDefaultSize();
		
		setVisible( true );
	}
	
	/**
	 * Builds the GUI of the column setup dialog.
	 */
	private void buildGui() {
		final ToolBarForTable toolBar = new ToolBarForTable( table );
		toolBar.addSelectInfoLabel( "Select a Column." );
		toolBar.addSelEnabledButton( showAction );
		toolBar.addSelEnabledButton( hideAction );
		toolBar.addSelEnabledButton( moveUpAction );
		toolBar.addSelEnabledButton( moveDownAction );
		toolBar.addSeparator();
		toolBar.add( restoreAction );
		toolBar.addSeparator();
		final Link customColsSettingLink = SettingsGui.createSettingLink( Settings.NODE_REP_LIST_CUSTOM_COLS );
		SettingsGui.bindVisibilityToSkillLevel( customColsSettingLink, Settings.REP_LIST_CUST_COL_1_NAME.skillLevel );
		toolBar.add( customColsSettingLink );
		toolBar.finalizeLayout();
		cp.addNorth( toolBar );
		
		cp.addCenter( table.createWrapperBox( true ) );
		
		final XButton okButton = new XButton( "_OK" );
		okButton.addActionListener( new ActionAdapter() {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				// Re-set the edited column class list to keep the internal class name list consistent:
				rlcBean.setColumnClassList( rlcBean.getColumnClassList() );
				
				Env.APP_SETTINGS.set( Settings.REP_LIST_COLUMNS_BEAN, rlcBean );
				close();
			}
		} );
		getButtonsPanel().add( okButton );
		getRootPane().setDefaultButton( okButton );
		addCloseButton( "_Cancel" );
		
		// Table header
		final XTableModel model = table.getXTableModel();
		
		final Vector< String > columns = Utils.asNewVector( "Class", "Position", "I", "Name", "Visible?", "Custom?", "Description" );
		final List< Class< ? > > columnClasses = Utils.< Class< ? > > asNewList( Class.class, Integer.class, LRIcon.class, String.class, Boolean.class,
		        Boolean.class, String.class );
		
		classColIdx = 0;
		posColIdx = 1;
		final int iconColIdx = 2;
		final int visibleColIdx = 4;
		final int customColIdx = 5;
		descColIdx = 6;
		
		model.setColumnIdentifiers( columns );
		model.setColumnClasses( columnClasses );
		// Comparator to move columns without position (=-1) to the end
		table.getXTableRowSorter().setComparator( posColIdx, new Comparator< Integer >() {
			@Override
			public int compare( final Integer i1, final Integer i2 ) {
				if ( i1 == -1 && i2 == -1 )
					return 0;
				if ( i1 == -1 )
					return 1;
				if ( i2 == -1 )
					return -1;
				return i1.compareTo( i2 );
			}
		} );
		// Default descending sort order for Icon, Visible and Custom? columns
		table.getXTableRowSorter().setColumnDefaultDescs( true, iconColIdx, visibleColIdx, customColIdx );
		// Hide Class column
		table.getColumnModel().removeColumn( table.getColumnModel().getColumn( classColIdx ) );
		
		rebuildTable();
	}
	
	/**
	 * Rebuilds the table.
	 */
	private void rebuildTable() {
		table.saveSelection( classColIdx );
		
		final XTableModel model = table.getXTableModel();
		
		model.getDataVector().clear();
		model.fireTableDataChanged();
		
		for ( final Class< ? extends IColumn< ? > > colClass : RepListColumnRegistry.COLUMN_LIST ) {
			final boolean isCustom = BaseCustomColumn.class.isAssignableFrom( colClass );
			
			// Only show custom columns if skill level is met
			if ( isCustom && Settings.REP_LIST_CUST_COL_1_NAME.skillLevel.isBelow() )
				continue;
			
			final IColumn< ? > column = RepListColumnRegistry.getColumnInstance( colClass );
			if ( column == null )
				continue;
			
			final int pos = rlcBean.getColumnClassList().indexOf( colClass ) + 1;
			final Vector< Object > row = Utils.< Object > asNewVector( colClass, pos > 0 ? pos : -1, column.getRicon(), column.getDisplayName(), pos > 0,
			        isCustom, column.getDescription() );
			model.addRow( row );
		}
		
		table.getRowSorter().setSortKeys( Arrays.asList( new SortKey( posColIdx, SortOrder.ASCENDING ) ) );
		
		// Pack all columns except the last Description column
		for ( int i = table.getColumnCount() - 1; i >= 0; i-- )
			if ( i != table.convertColumnIndexToView( descColIdx ) )
				table.packColumns( i );
		
		table.restoreSelection( classColIdx );
	}
	
}
